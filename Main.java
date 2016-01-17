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
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFileChooser;

/**
 * @author Brett Michael Allen and Christian Bryce Alexander
 * @since Oct 22, 2015, 1:13:31 PM
 */

public class Main 
{
	public static final String DATES = "Sun,Mon,Tue,Wed,Thu,Fri,Sat";
	public static final int DAYS = 7;
	
	public static final String TEST1 = "/Test Schedule for Brett.csv";
	public static final String TEST2 = "/FLS Wall Schedule.csv";
	public static final String PATH = "/wall schedule.csv";

	public static void main(String[] args) throws IOException 
	{	
		Shift[][] schedule;		
		Shift shift1;
		Shift shift2;
		
		ArrayList<Shift> multiShifts = new ArrayList<Shift>();
		ArrayList<String> truncDates = new ArrayList<String>();
		
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		//Delegate file chooser to specification file (class)
		JButton open = new JButton();
		JFileChooser myPath = new JFileChooser();
		
		
		//Use this path if testing on main: "C:/Users/Brett/Workspace/Java/CSV Parsing/res"
		myPath.setCurrentDirectory(new File("C:/Users/Brett/Documents/BJs Wholesale Club/Schedules/01-16-16"));
		myPath.setDialogTitle("Open");
		
		
		if(myPath.showOpenDialog(open) == JFileChooser.APPROVE_OPTION){}		
		//End chooser
		
		String employee = "",
				day = "",
				date = "",
				shiftPosition = "",
				shiftPosition2 = "",
				shiftTime = "",
				shiftTime2 = "",
				lineA = "",
				lineB = "",
				lines = null,				
				fileName = myPath.getSelectedFile().getAbsolutePath();
		
		String[] dates,
					toks = new String[2];

		int startCol = 0, 
				numEmployees = 0,
				dayNum = 0,
				e = -1, 
				choice = -1,
				iOffset = 0,
				count = -1;				
		
		//Used for input validation
		char invalidChoice = ' ';
		
		boolean multiplier;
		
		ArrayList<String> chosenFile = new ArrayList<String>();
	
        try 
        {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

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
            System.err.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) 
        {
            System.err.println("Error reading file '" + fileName + "'");
        } 
        
        //Test method
		//String[] csv = readFile(PATH);
        
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
					System.exit(-1);
				}
			}

			if (line.startsWith("\"")) {
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
					
					//If shiftPosition is blank Or the cell directly below it contains a shiftPosition
					//process that employee
					if (shiftPosition.equals(".") || !startsWithNumber(csv[i + 1].split(",")[j]))
					{
						//If the cell directly below the initially set shiftPosition is also blank 
						//skip that employee
						if(csv[i + 1].split(",")[j].equals("."))
							continue;
						
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
		
		//Combine original schedule with multiShifts. Overwrite null elements of original schedule
		combine(schedule, numEmployees, DAYS, multiShifts);
		
		//Sort the schedule in chronological order to use for displaying
		sortAscending(schedule, numEmployees, DAYS);
		
		//Remove all duplicate shifts from schedule array
		removeDuplicates(schedule, numEmployees, DAYS);
		
		//Display menu and display schedule	
		do
		{
			displayMenu();
			try
			{
				choice = input.nextInt();
				invalidChoice = ' ';
			}
			catch(InputMismatchException exception)
			{
				invalidChoice = input.next().charAt(0);				
			}			
			
			//Validate choice
			while(choice < 0 || choice > 7 || invalidChoice != ' ')
			{
				System.err.println("\nError! Invalid input.");
				System.err.println("Enter a number 1 - 7\n");
				displayMenu();
				try
				{
					choice = input.nextInt();
					invalidChoice = ' ';
				}
				catch(InputMismatchException exception)
				{
					invalidChoice = input.next().charAt(0);			
				}
			}
			
			switch(choice)
			{
			case 1:
				day = "Sunday";
				
				toks = truncDates.get(choice - 1).split("-");
				date = toks[1].toUpperCase() + "-" + toks[0];
				
				Logger sunday = new Logger(day, date);
				
				System.out.println("\n" + day.toUpperCase() + " " +
						date + ":");
				
				sunday.log("\n" + day.toUpperCase() + " " +
						date + ":");
				
				displayWallSchedule(day, schedule, numEmployees, DAYS, sunday);
				break;
			case 2:
				day = "Monday";
				
				toks = truncDates.get(choice - 1).split("-");
				date = toks[1].toUpperCase() + "-" + toks[0];

				Logger monday = new Logger(day, date);
				
				System.out.println("\n" + day.toUpperCase() + " " +
						date + ":");
				
				monday.log("\n" + day.toUpperCase() + " " +
						date + ":");
				
				displayWallSchedule(day, schedule, numEmployees, DAYS, monday);
				break;
			case 3:
				day = "Tuesday";
				
				toks = truncDates.get(choice - 1).split("-");
				date = toks[1].toUpperCase() + "-" + toks[0];

				Logger tuesday = new Logger(day, date);
				
				System.out.println("\n" + day.toUpperCase() + " " +
						date + ":");
				
				tuesday.log("\n" + day.toUpperCase() + " " +
						date + ":");
				
				displayWallSchedule(day, schedule, numEmployees, DAYS, tuesday);
				break;
			case 4:
				day = "Wednesday";
				
				toks = truncDates.get(choice - 1).split("-");
				date = toks[1].toUpperCase() + "-" + toks[0];

				Logger wednesday = new Logger(day, date);
				
				System.out.println("\n" + day.toUpperCase() + " " +
						date + ":");
				
				wednesday.log("\n" + day.toUpperCase() + " " +
						date + ":");
				
				displayWallSchedule(day, schedule, numEmployees, DAYS, wednesday);
				break;
			case 5:
				day = "Thursday";
				
				toks = truncDates.get(choice - 1).split("-");
				date = toks[1].toUpperCase() + "-" + toks[0];

				Logger thursday = new Logger(day, date);
				
				System.out.println("\n" + day.toUpperCase() + " " +
						date + ":");
				
				thursday.log("\n" + day.toUpperCase() + " " +
						date + ":");
				
				displayWallSchedule(day, schedule, numEmployees, DAYS, thursday);
				break;
			case 6:
				day = "Friday";
				
				toks = truncDates.get(choice - 1).split("-");
				date = toks[1].toUpperCase() + "-" + toks[0];

				Logger friday = new Logger(day, date);
				
				System.out.println("\n" + day.toUpperCase() + " " +
						date + ":");
				
				friday.log("\n" + day.toUpperCase() + " " +
						date + ":");
				
				displayWallSchedule(day, schedule, numEmployees, DAYS, friday);
				break;
			case 7:
				day = "Saturday";
				
				toks = truncDates.get(choice - 1).split("-");
				date = toks[1].toUpperCase() + "-" + toks[0];

				Logger saturday = new Logger(day, date);
				
				System.out.println("\n" + day.toUpperCase() + " " +
						date + ":");
				
				saturday.log("\n" + day.toUpperCase() + " " +
						date + ":");
				
				displayWallSchedule(day, schedule, numEmployees, DAYS, saturday);
				break;
			default:
				break;
			}
			
		}while(choice != 0);
	}
	
	public static void displayMenu()
	{
		/*System.out.println("\n\t\tHOME");
		System.out.println("-----------------------------------------");*/
		System.out.println("Please choose a day to print (1 - 7):\n");
		System.out.println("1) Sunday");
		System.out.println("2) Monday");
		System.out.println("3) Tuesday");
		System.out.println("4) Wednesday");
		System.out.println("5) Thursday");
		System.out.println("6) Friday");
		System.out.println("7) Saturday");
		System.out.println("0) Exit\n");
		System.out.print(">> ");
	}	
	
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
	
	//Chronologically sorting Shift[][] 
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
	
	//Remove duplicates method here
	
	//Method that calls the displayShifts method using the positions included in wall schedule
	public static void displayWallSchedule(String day, Shift[][] myArray, int rows, int cols, Logger file) throws IOException
	{
		System.out.println("\n***********FRONT LINE SUPV**********");
		file.log("\n***********FRONT LINE SUPV**********");
		displayShifts("Front Line Supv", day, myArray, rows, cols, file);
		
		System.out.println("\n***********SELF CHECK-OUT**********");
		file.log("\n***********SELF CHECK-OUT**********");
		displayShifts("Selfcheck Attendant", day, myArray, rows, cols, file);
		
		System.out.println("\n***********MEMBER SERVICES**********");
		file.log("\n***********MEMBER SERVICES**********");
		displayShifts("Member Services", day, myArray, rows, cols, file);
		
		System.out.println("\n***********FRONT DOOR**********");
		file.log("\n***********FRONT DOOR**********");
		displayShifts("Front Door", day, myArray, rows, cols, file);
		
		System.out.println("\n***********CARTS************");
		file.log("\n***********CARTS************");
		displayShifts("Stock/Cart Retriever", day, myArray, rows, cols, file);
		
		System.out.println("\n***********RECOVERY**********");
		file.log("\n***********RECOVERY**********");
		displayShifts("Recovery", day, myArray, rows, cols, file);
		
		System.out.println("\n***********FOOD COURT***********");
		file.log("\n***********FOOD COURT***********");
		displayShifts("Food", day, myArray, rows, cols, file);
		
		System.out.println("\n***********TIRE BAY**********");
		file.log("\n***********TIRE BAY**********");
		displayShifts("Tire", day, myArray, rows, cols, file);
		
		System.out.println("\n***********MAINTENANCE**********");
		file.log("\n***********MAINTENANCE**********");
		displayShifts("Maintenance", day, myArray, rows, cols, file);
		
		System.out.println("\n***********DELI**********");
		file.log("\n***********DELI**********");
		displayShifts("Deli", day, myArray, rows, cols, file);
		
		System.out.println("\n***********BAKERY**********");
		file.log("\n***********BAKERY**********");
		displayShifts("Bakery", day, myArray, rows, cols, file);
		
		System.out.println("\n***********CASH OFFICE**********");
		file.log("\n***********CASH OFFICE**********");
		displayShifts("Office", day, myArray, rows, cols, file);
		
		System.out.println("\n***********MEAT**********");
		file.log("\n***********MEAT**********");
		displayShifts("Meat", day, myArray, rows, cols, file);
		
		System.out.println("\n***********PRODUCE**********");
		file.log("\n***********PRODUCE**********");
		displayShifts("Produce", day, myArray, rows, cols, file);
		
		System.out.println("\n****************************************************");
		System.out.println("Copyright (C) 2016, Brett Allen, Christian Alexander");
		System.out.println("****************************************************\n");
		
		file.log("\n****************************************************");
		file.log("Copyright (C) 2016, Brett Allen");
		file.log("****************************************************\n");
	}
	
	public static void displayCashierSchedule(String day, Shift[][] myArray, int rows, int cols, Logger file) throws IOException
	{
		System.out.println("\n*********CASHIER SCHEDULE**********");
		file.log("\n*********CASHIER SCHEDULE**********");
		displayShifts("Cashier", day, myArray, rows, cols, file);
		
		System.out.println("\n****************************************************");
		System.out.println("Copyright (C) 2016, Brett Allen, Christian Alexander");
		System.out.println("****************************************************\n");
		
		file.log("\n****************************************************");
		file.log("Copyright (C) 2016, Brett Allen");
		file.log("****************************************************\n");
	}
	
	public static void displayTest(Shift[][] myArray, int rows, int cols, boolean printNull)
	{	
		int numNull = 0;
		
		for(int x = 0; x < rows; x++)
		{
			for(int y = 0; y < cols; y++)
			{
				if(printNull == false)
				{
					if(myArray[x][y] != null)
					{						
						System.out.println("@[" + x + "][" + y + "]");
						System.out.println(myArray[x][y]);
					}	
					else
					{
						numNull++;
					}
				}
				else
				{
					System.out.println(myArray[x][y] + "\n");					
				}				
			}			
		}
		
		System.out.print("There were ");
		System.err.println(numNull + " null shifts\n");
	}
	
	//Need to figure out a way to prevent printing duplicate shifts here
	public static void displayShifts(String position, String day, Shift[][] myArray, int rows,
			int cols, Logger file) throws IOException
	{
		for(int x = 0; x < rows; x++)
		{
			for(int y = 0; y < cols; y++)
			{
				if(myArray[x][y] != null && myArray[x][y].day == day
						&& myArray[x][y].position.contains(position)
						&& getMilitaryTime(myArray[x][y].endTime) > 900)
				{						
					System.out.println(myArray[x][y]);
					file.log(myArray[x][y].displayShift());
				}			
			}			
		}
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

	public static String[] readFile(String path) 
	{
		ArrayList<String> lines = new ArrayList<>();

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
