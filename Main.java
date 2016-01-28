/*****************************************************************
 *   ____ ______     __  ____   _    ____  ____ ___ _   _  ____  *
 *  / ___/ ___\ \   / / |  _ \ / \  |  _ \/ ___|_ _| \ | |/ ___| *
 * | |   \___ \\ \ / /  | |_) / _ \ | |_) \___ \| ||  \| | |  _  *
 * | |___ ___) |\ V /   |  __/ ___ \|  _ < ___) | || |\  | |_| | *
 *  \____|____/  \_/    |_| /_/   \_\_| \_\____/___|_| \_|\____| *
 *                                                               *
 * Copyright (C) 2015, Christian Alexander, Brett Allen          *
 *****************************************************************/
package net.alexanderdev.csvparsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.text.BadLocationException;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * @author Brett Michael Allen and Christian Bryce Alexander
 * @since Oct 22, 2015, 1:13:31 PM
 */

public class Main
{
	public static final String DATES = "Sun,Mon,Tue,Wed,Thu,Fri,Sat";
	public static final int DAYS = 7;

	public static void main(String[] args) throws IOException, BiffException, RowsExceededException,
													WriteException, InterruptedException, BadLocationException
	{		
		Menu menu = new Menu();
		menu.setVisible(true);
		menu.setResizable(false);
		
		//Set the value of the public menu variable in the Menu class to the value of the menu variable here
		menu.menu = menu;		
	}//End main
	
	//This method embodies all the central processing of this program. It acts as an overloaded main method 
	//and will be called only by the Menu class
	public static void mainDelegate(Menu menu) throws BadLocationException, BiffException, IOException
	{
		//Initialize local variables
		menu.print("Initializing...\n");
		
		//Creating a CsvConverter variable that will be instantiated once the initial
		//xls file is edited programmatically
		CsvConverter converter = null;
		
		Shift[][] schedule;		
		Shift shift1;
		Shift shift2;
		
		ArrayList<Shift> multiShifts = new ArrayList<Shift>();
		ArrayList<String> truncDates = new ArrayList<String>();
		
		boolean multiplier,
				errorProcessing = false,
				fileDiscrepancyHandler = true;
		
		String employee = "",
				shiftPosition = "",
				shiftPosition2 = "",
				shiftTime = "",
				shiftTime2 = "",
				lineA = "",
				lineB = "",
				lines = null;
		
		//
		String fileName = menu.getPath();
		
		String[] dates;					

		int startCol = 0, 
				numEmployees = 0,
				dayNum = 0,
				e = -1, 
				iOffset = 0,
				count = -1;
		
		ArrayList<String> chosenFile = new ArrayList<String>();
		
		menu.print("Initialization complete.\n\n");
		//End initialization
	
		//Try opening chosen file		
		menu.print("Opening file...\n");
		
        try 
        {        	
        	//Create a new ExcelWriter that will receive an excel file as input and eventually parse to .csv		
    		ExcelWriter writer = new ExcelWriter(fileName);
    		
    		//Deleting column A
    		writer.deleteColumn(0);		
    		writer.writeAndClose();
    		
    		try 
    		{
                converter = new CsvConverter();
                
                converter.convertExcelToCSV(writer.getXlsFilePath(),
                		new File(".").getAbsolutePath());
                
                //Delete input.xls
                writer.getXlsFile().delete();
            }
            catch(Exception ex) 
    		{
                System.err.println("Caught an: " + ex.getClass().getName());
                System.err.println("Message: " + ex.getMessage());
                System.err.println("Stacktrace follows:.....");
                ex.printStackTrace(System.out);                
            }
        	
            //Reading text files in the default encoding.
            FileReader fileReader = new FileReader(converter.getCsvFilePath());

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((lines = bufferedReader.readLine()) != null) 
            {
                //System.out.println(lines);
                //csv[count] = lines;
                chosenFile.add(lines);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) 
        {
        	errorProcessing = true;
            System.err.println("Unable to open file '" + fileName + "'"); 
            menu.print("Unable to open file '" + fileName + "'\n");
        }
        catch(IOException ex) 
        {
        	errorProcessing = true;
            System.err.println("Error reading file '" + fileName + "'");
            menu.print("Error reading file '" + fileName + "'\n");
        }
        
        //Delete input.csv
        converter.getCsvFile().delete();
        
        menu.print("Done.\n\n");
        //End open file
        
        //Test method
		//String[] csv = readFile(PATH);
        
        //Process chosen file
        if(!errorProcessing)        
        	  menu.print("Processing file...\n");
        
        String[] csv = chosenFile.toArray(new String[chosenFile.size()]); 
        
		// Search where the dates column starts for data parsing
		for (String line : csv) 
		{
			if (line.contains(DATES)) 
			{
				String[] tokens = line.split(",");

				startCol = indexOf(tokens, "Sun");

				if (startCol == -1) 
				{
					System.err.println("Sunday not found!");
					menu.print("Sunday not found!\n");
					System.exit(-1);
				}
			}

			if (line.startsWith("\"")) 
			{
				numEmployees++;
			}
		}
		// End search
		
		dates = csv[startCol + 3].split(",");
		
		schedule = new Shift[numEmployees][DAYS];		

		// Parsing data
		for (int i = 0; i < csv.length; i++) 
		{
			// Line that contains position for each day
			lineA = csv[i];

			// Line that starts with an employee's name (names are surrounded in "")
			if (lineA.startsWith("\"")) 
			{
				// Current row in schedule
				e++;

				// Line that contains start and end times
				lineB = csv[i + 1];

				// Gets employee name
				employee = lineA.substring(1, lineA.lastIndexOf("\""));

				for (int j = startCol; j < startCol + 7; j++) 
				{
					multiplier = false;
					
					dayNum = j - 2;
					
					count++;
					if(count < dates.length)
					{
						if(startsWithNumber(dates[count]))
						{
							truncDates.add(dates[count]);
						}
					}	
					
					shiftPosition = lineA.split(",")[j + 1];
					
					/*if (shiftPosition.equals(".") || !startsWithNumber(csv[i + 1].split(",")[j]))
					{	
						if(csv[i + 1].split(",")[j].equals(".") || !startsWithNumber(csv[i + 1].split(",")[j]))
						{	
							//Skewed file
							fileDiscrepancyHandler = false;					
						}
					}
					
					//If shiftPosition is blank Or the cell directly below it contains a shiftPosition
					//process that employee
					
					if(fileDiscrepancyHandler)
					{
						if (shiftPosition.equals(".") || !startsWithNumber(csv[i + 1].split(",")[j]))
						{
							//If the cell directly below the initially set shiftPosition is also blank 
							//skip that employee
							if(csv[i + 1].split(",")[j].equals(".") || !startsWithNumber(csv[i + 1].split(",")[j]))
							{										
								continue;
							}
							shiftPosition = csv[i + 1].split(",")[j];
						}
					}
					else
					{
						if (shiftPosition.equals(".") || !startsWithNumber(csv[i + 1].split(",")[j]))
						{
							//If the cell directly below the initially set shiftPosition is also blank 
							//skip that employee
							if(csv[i + 1].split(",")[j].equals("."))
							{							
								continue;
							}							
							
							shiftPosition = csv[i + 1].split(",")[j];
						}
					}				*/	
					
					if (shiftPosition.equals(".") || !startsWithNumber(csv[i + 1].split(",")[j]))
					{
						//If the cell directly below the initially set shiftPosition is also blank 
						//skip that employee
						if(csv[i + 1].split(",")[j].equals(".") || !startsWithNumber(csv[i + 1].split(",")[j]))
						{							
							continue;
						}							
						
						shiftPosition = csv[i + 1].split(",")[j];
					}
					
					shiftTime = lineB.split(",")[j];

					//Skips start and end times (vacations, unpaid days off) if both
					//the initial field and field below are invalid
					if (!startsWithNumber(shiftTime))
					{	
						iOffset = i + 1;						
						lineB = csv[iOffset + 1];
												
						shiftTime = lineB.split(",")[j];
						
						//If the shiftTime is still not a number skip that employee
						if(!startsWithNumber(shiftTime))
							continue;
					}
					
					//Create new shift
					try 
					{	
						shift1 = new Shift(employee, shiftPosition, shiftTime.split("-")[0], shiftTime.split("-")[1],
								dayNum);
					} 
					catch (ArrayIndexOutOfBoundsException e1) 
					{
						break;
					}
					
					//Assign new shift1
					schedule[e][j - startCol] = shift1;
					
					//Determine if the there is a second portion of the current employee's shift
					try
					{
						if(!csv[i + 4].split(",")[j].equals(".") &&
								!startsWithNumber(csv[i + 4].split(",")[j]) &&
								!csv[i + 4].startsWith("\""))
						{
							multiplier = true;
							
							shiftPosition2 = csv[i + 4].split(",")[j];
							shiftTime2 = csv[i + 5].split(",")[j];
						}
						else if(!csv[i + 3].split(",")[j].equals(".") &&
								!startsWithNumber(csv[i + 3].split(",")[j]) &&
								!csv[i + 3].startsWith("\""))
						{
							multiplier = true;
							
							shiftPosition2 = csv[i + 3].split(",")[j];
							shiftTime2 = csv[i + 4].split(",")[j];
						}
						
						if(multiplier)
						{
							try 
							{	
								shift2 = new Shift(employee, shiftPosition2, shiftTime2.split("-")[0],
										shiftTime2.split("-")[1],
										dayNum);
								
								multiShifts.add(shift2);
							} 
							catch (ArrayIndexOutOfBoundsException e1) 
							{
								continue;
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e1)
					{
						continue;
					}
				}
			}
		}
		//End parsing
		
		//Delete input.xls and input.csv
		
		
		//Combine original schedule with multiShifts. Overwrite null elements of original schedule
		combine(schedule, numEmployees, DAYS, multiShifts);
		
		//Sort the schedule in chronological order to use for displaying
		sortAscending(schedule, numEmployees, DAYS);
		
		//Remove all duplicate shifts from schedule array
		removeDuplicates(schedule, numEmployees, DAYS);
		
		menu.print("Done.\n\n");
		
		//Transfer specified variables to the Menu class for rendering purposes
		menu.transferVariables(schedule, numEmployees, DAYS, truncDates);
	}//End mainDelegate
	
	public static void combine(Shift[][] myArray1, int rows, int cols, ArrayList<Shift> myArray2)
	{
		int i = 0;
		
		//Traverse myArray1 and replace all null elements with an element of myArray2
		for(int x = 0; x < rows; x++)
		{
			for(int y = 0; y < cols; y++)
			{
				if(myArray1[x][y] == null)
				{
					if(i < myArray2.size())
					{
						myArray1[x][y] = myArray2.get(i);
						i++;
					}
				}
			}
		}
	}
	
	//Traverse schedule and remove all duplicates
	public static void removeDuplicates(Shift[][] myArray, int rows, int cols)
	{
		for(int a = 0; a < rows; a++)
		{
			for(int b = 0; b < cols; b++)
			{			
				for(int c = 0; c < rows; c++)
				{
					for(int d = 0; d < cols; d++)
					{
						//If not the same element at the same location
						if ((a == c) && (b == d)) 
						{
							if ((a + 1) < cols) 
							{
								//Only proceed if testing myArray[0][0]
								if (myArray[c][a + 1] != null && myArray[a][b] != null) {
									if (myArray[c][a + 1].day.equals(myArray[a][b].day)
											&& myArray[c][a + 1].position.contains(myArray[a][b].position)											
											&& myArray[c][a + 1].employee.contains(myArray[a][b].employee)) 
									{	
										myArray[c][a + 1] = null;
										//myArray[a][b] = null;
									}
								} 
							} 
						} 
						//Otherwise include cols at 0
						else
						{
							if (myArray[c][d] != null && myArray[a][b] != null) {
								if (myArray[c][d].day.equals(myArray[a][b].day)
										&& myArray[c][d].position.contains(myArray[a][b].position)										
										&& myArray[c][d].employee.contains(myArray[a][b].employee)) 
								{	
									myArray[c][d] = null;
									//myArray[a][b] = null;
								}
							} 
						}
					}
				}
			}
		}
	}
	
	//Chronologically sorting schedule 
	//Sorting algorithm: Ascending (least to greatest)(earliest to latest)
	public static void sortAscending(Shift[][] myArray, int rows, int cols)
	{		
		Shift temp = new Shift();
		
		//numEmployees represents rows, days represents columns
		for(int a = 0; a < rows; a++)
		{
			for(int b = 0; b < cols; b++)
			{
				for(int c = 0; c < rows; c++)
				{
					for(int d = 0; d < cols; d++)
					{
						/*If schedule is not null and military time of the initial element is
						greater than military time of current element then swap the current element
						and	initial element*/
						if(myArray[c][d] != null && myArray[a][b] != null)
						{
							if(getMilitaryTime(myArray[c][d].startTime) > getMilitaryTime(
									myArray[a][b].startTime))
							{
								temp = myArray[a][b];
								myArray[a][b] = myArray[c][d];
								myArray[c][d] = temp;
							}
						}
					}
				}
			}
		}
	}
	
	public static void renderChoice(String day, Shift[][] myArray, int rows, int cols,
			int choice,  ArrayList<String> truncDates, Menu menu) throws IOException, BiffException,
			RowsExceededException, WriteException, BadLocationException
	{
		String[] toks = new String[2];
		String date = "";
		String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		
		toks = truncDates.get(choice - 1).split("-");
		date = toks[1].toUpperCase() + "-" + toks[0];
		
		//Determine which schedule(s) to print based on checkbox state(s) of menu
		if(menu.getCbxFrontLineState())
		{
			menu.print("\nRendering choice...\n");
			
			//Load in the Excel template
			InputStream frontLineTemplate = Main.class.getResourceAsStream("/frontLineTemplate.xls");
			
			//Create Excel writer for Front Line shifts
			ExcelWriter frontLineSchedule = new ExcelWriter(frontLineTemplate, "Front Line Schedule", day, date, year);
			
			//Write the day to cell B1
			frontLineSchedule.overwriteEmptyCell(1, 0, day, 0);
			
			//Write the date to cell N1
			frontLineSchedule.overwriteEmptyCell(13, 0, date + "-" + year, 0);
			
			displayFrontLineSchedule(day, myArray, rows, cols, frontLineSchedule);	
			
			menu.print("Done.\n\nFront Line schedule for " + day + ", " + date + "-" + year + " has been created.\n");
			
			frontLineSchedule.writeAndClose();
		}
		
		if(menu.getCbxCashierState())
		{
			menu.print("\nRendering choice...\n");
			
			//Load in the Excel template
			InputStream cashierTemplate = Main.class.getResourceAsStream("/cashierTemplate.xls");
			
			//Create Excel writer for Cashier shifts
			ExcelWriter cashierSchedule = new ExcelWriter(cashierTemplate, "Cashier Schedule", day, date, year);
			
			//Write the day to cell B1
			cashierSchedule.overwriteEmptyCell(1, 0, day, 0);
			
			//Write the date to cell F1
			cashierSchedule.overwriteEmptyCell(5, 0, date + "-" + year, 0);
			
			displayCashierSchedule(day, myArray, rows, cols, cashierSchedule);
			
			menu.print("Done.\n\nCashier schedule for " + day + ", " + date + "-" + year + " has been created.\n");
			
			cashierSchedule.writeAndClose();
		}
		
		/*System.out.println("\n****************************************************");
		System.out.println("Created By: Brett Allen");
		System.out.println("****************************************************\n");*/		
	}
	
	//Method that calls the displayShifts method using the positions included in wall schedule
	public static void displayFrontLineSchedule(String day, Shift[][] myArray, int rows,
			int cols, ExcelWriter frontLineSchedule) throws IOException, RowsExceededException, WriteException
	{			
		displayFrontLineShifts("Front Line Supv", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Selfcheck Attendant", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Member Services", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Front Door", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Stock/Cart Retriever", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Recovery", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Food", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Tire", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Maintenance", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Deli", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Bakery", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Office", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Meat", day, myArray, rows, cols, frontLineSchedule);		
		
		displayFrontLineShifts("Produce", day, myArray, rows, cols, frontLineSchedule);				
	}
	
	public static void displayCashierSchedule(String day, Shift[][] myArray, int rows, int cols,
			ExcelWriter cashierSchedule) throws IOException, RowsExceededException, WriteException
	{	
		displayCashierShifts("Cashier", day, myArray, rows, cols, cashierSchedule);
	}
	
	public static void displayTest(Shift[][] myArray, int rows, int cols, boolean printNull)
	{	
		for(int x = 0; x < rows; x++)
		{
			for(int y = 0; y < cols; y++)
			{
				if(printNull == false)
				{
					if(myArray[x][y] != null
							&& myArray[x][y].day.equals("Sunday")
							&& getMilitaryTime(myArray[x][y].endTime) > 900)
					{						
						System.out.println(myArray[x][y]);
					}	
				}
				else
				{
					System.out.println(myArray[x][y] + "\n");					
				}				
			}			
		}
	}
	
	//Need to figure out a way to prevent printing duplicate shifts here
	public static void displayFrontLineShifts(String position, String day, Shift[][] myArray, int rows,
			int cols, ExcelWriter frontLineSchedule) throws IOException, RowsExceededException, WriteException
	{
		int excelCol = 0,
			excelRow = 0;
		
		for(int x = 0; x < rows; x++)
		{
			for(int y = 0; y < cols; y++)
			{
				if(myArray[x][y] != null && myArray[x][y].day == day
						&& myArray[x][y].position.contains(position)
						&& getMilitaryTime(myArray[x][y].endTime) > 900)
				{	
					//Implementing switch logic for jre compliance level 1.6
					if(myArray[x][y].position.contains("Front Line Supv"))
					{
						excelCol = 0;
						excelRow = 4;
					}
					else if(myArray[x][y].position.contains("Selfcheck Attendant"))
					{
						excelCol = 0;
						excelRow = 12;
					}
					else if(myArray[x][y].position.contains("Member Services"))
					{
						excelCol = 0;
						excelRow = 20;
					}
					else if(myArray[x][y].position.contains("Front Door"))
					{
						excelCol = 0;
						excelRow = 28;
					}
					else if(myArray[x][y].position.contains("Stock/Cart Retriever"))
					{
						excelCol = 0;
						excelRow = 36;
					}
					else if(myArray[x][y].position.contains("Recovery"))
					{
						excelCol = 6;
						excelRow = 4;
					}
					else if(myArray[x][y].position.contains("Food"))
					{
						excelCol = 6;
						excelRow = 12;
					}
					else if(myArray[x][y].position.contains("Tire"))
					{
						excelCol = 6;
						excelRow = 20;
					}
					else if(myArray[x][y].position.contains("Maintenance"))
					{
						excelCol = 6;
						excelRow = 28;
					}
					else if(myArray[x][y].position.contains("Deli")) //Going to have to handle overflow for Deli department
					{
						excelCol = 6;
						excelRow = 36;
					}
					else if(myArray[x][y].position.contains("Bakery"))
					{
						excelCol = 12;
						excelRow = 4;
					}
					else if(myArray[x][y].position.contains("Office"))
					{
						excelCol = 12;
						excelRow = 12;
					}
					else if(myArray[x][y].position.contains("Meat"))
					{
						excelCol = 12;
						excelRow = 20;
					}
					else if(myArray[x][y].position.contains("Produce"))
					{
						excelCol = 12;
						excelRow = 28;
					}
					//End switch
					
					//Traverse excel template and fill name and time only if there is not already a name and time there
					while(excelRow < excelRow + 4)
					{
						if(frontLineSchedule.isEmptyCell(excelCol, excelRow))
						{	
							//Write the employees name to the ExcelWriter
							frontLineSchedule.overwriteEmptyCell(excelCol, excelRow, myArray[x][y].getName(), 1);
							
							//Write that employees shift time next to the employee's name
							frontLineSchedule.overwriteEmptyCell(excelCol + 1, excelRow, myArray[x][y].getShiftTime(), 2);
							
							//Make boarders for breaks and lunch cells
							frontLineSchedule.overwriteEmptyCell(excelCol + 2, excelRow, "", 1);
							frontLineSchedule.overwriteEmptyCell(excelCol + 3, excelRow, "", 1);
							frontLineSchedule.overwriteEmptyCell(excelCol + 4, excelRow, "", 1);
							break;
						}
						
						excelRow++;
					}
					
				}			
			}			
		}
	}
	
	public static void displayCashierShifts(String position, String day, Shift[][] myArray, int rows,
			int cols, ExcelWriter cashierSchedule) throws RowsExceededException, WriteException, IOException
	{
		//Start at cell A4
		int excelCol = 0,
			excelRow = 3;
		
		for(int x = 0; x < rows; x++)
		{
			for(int y = 0; y < cols; y++)
			{
				if(myArray[x][y] != null && myArray[x][y].day == day
						&& myArray[x][y].position.contains(position)
						&& getMilitaryTime(myArray[x][y].endTime) > 900)
				{	
					//Write the employees name to the ExcelWriter
					cashierSchedule.overwriteEmptyCell(excelCol, excelRow, myArray[x][y].getName(), 1);
					
					//Write that employees shift time next to the employee's name
					cashierSchedule.overwriteEmptyCell(excelCol + 1, excelRow, myArray[x][y].getShiftTime(), 2);
					
					//Make boarders for the breaks, lunch, and comments cells
					cashierSchedule.overwriteEmptyCell(excelCol + 2, excelRow, "", 1);
					cashierSchedule.overwriteEmptyCell(excelCol + 3, excelRow, "", 1);
					cashierSchedule.overwriteEmptyCell(excelCol + 4, excelRow, "", 1);
					cashierSchedule.overwriteEmptyCell(excelCol + 5, excelRow, "", 1);
					
					excelRow++;
				}
			}
		}
		
		//Once all cashiers have been written to the excel file, write a section for notes
		cashierSchedule.overwriteEmptyCell(excelCol, excelRow, "Notes:", 3);
	}
	
	//Get time in standard format and convert it to military time for comparison purposes
	public static int getMilitaryTime(String standardTime)
	{
		int militaryTime = 0;		
		String[] tmp;
		boolean pastNoon = false;
		
		if(standardTime == "2400")
		{
			try
			{
				militaryTime = Integer.parseInt(standardTime);
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
			}
			return militaryTime;
		}
		else
		{
			 tmp = standardTime.split(":");
			 
			 if(standardTime.contains("p"))
			 {
				pastNoon = true;
			 }
				
			tmp[1] = tmp[1].replaceAll("[^\\d.]", "");
			
			try 
			{
				militaryTime = Integer.parseInt(tmp[0] + tmp[1]);
			} 
			catch (NumberFormatException e) 
			{
				e.printStackTrace();
			}
			
			if(pastNoon)
			{
				if(militaryTime < 1200)
					return militaryTime + 1200;
				else
					return militaryTime;
				/*if(militaryTime >= 1200)
					return militaryTime;
				else
					return militaryTime + 1200;*/
			}
			else
			{
				if(militaryTime < 1200)
					return militaryTime;
				else
					return militaryTime - 1200;
			}
		}
	}

	//Searching for shift times
	public static boolean startsWithNumber(String shiftTime) 
	{
		String[] nums = 
		{
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
		};

		for (String num : nums)
			if (shiftTime.startsWith(num))
				return true;
		return false;
	}

	public static int indexOf(String[] tokens, String find) 
	{
		for (int i = 0; i < tokens.length; i++)
			if (tokens[i].contains(find))
				return i;
		return -1;
	}

	@SuppressWarnings("unchecked")
	public static String[] readFile(String path) 
	{
		@SuppressWarnings("rawtypes")
		ArrayList<String> lines = new ArrayList();

		InputStream inStream = Main.class.getResourceAsStream(path);

		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

		String line;

		try 
		{
			while ((line = reader.readLine()) != null) 
			{
				lines.add(line);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				reader.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}

		return lines.toArray(new String[lines.size()]);
	}

	public static void writeFile(String path, String[] lines) 
	{
		BufferedWriter writer = null;

		try 
		{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));

			for (String line : lines) 
			{
				writer.write(line);
				writer.newLine();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				writer.close();
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}	
}
