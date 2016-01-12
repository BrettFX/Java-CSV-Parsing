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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Christian Bryce Alexander and Brett Michael Allen
 * @since Oct 22, 2015, 1:13:31 PM
 */
public class Main 
{
	public static final String DATES = "Sun,Mon,Tue,Wed,Thu,Fri,Sat";
	public static final int DAYS = 7;
	
	public static final String TEST1 = "/Test Schedule for Brett.csv";
	public static final String TEST2 = "/FLS Wall Schedule.csv";
	public static final String PATH = "/wall schedule.csv";

	public static void main(String[] args) 
	{	
		Shift[][] schedule;		
		Shift shift1;
		Shift shift2;
		ArrayList<Shift> multiShifts = new ArrayList<Shift>();
		String[] csv = readFile(PATH);		
		String day = "";
		
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		String employee = "",
				shiftPosition = "",
				shiftPosition2 = "",
				shiftTime = "",
				shiftTime2 = "",
				lineA = "",
				lineB = "";

		int startCol = 0, 
				numEmployees = 0, 
				e = -1, 
				choice = -1,
				iOffset = 0;
		
		//Used for input validation
		char invalidChoice = ' ';
		
		boolean multiplier;
		
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

		System.out.print("There are ");
		System.err.print(numEmployees + " employees ");
		System.out.println("in this file\n");
		
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
					
					shiftPosition = lineA.split(",")[j + 1];		
					
					//If shiftPosition is blank Or the cell directly below it contains a shiftPosition
					//process that employee
					if (shiftPosition.equals(".") || !startsWithNumber(csv[i + 1].split(",")[j]))
					{
						//If the cell directly below the initial set shiftPosition is also blank 
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
									j - 2);
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
								!startsWithNumber(csv[i + 4].split(",")[j]))
						{
							multiplier = true;
							
							shiftPosition2 = csv[i + 4].split(",")[j];
							shiftTime2 = csv[i + 5].split(",")[j];
						}
						
						if(multiplier)
						{
							try 
							{	
								shift2 = new Shift(employee, shiftPosition2, shiftTime2.split("-")[0],
										shiftTime2.split("-")[1],
											j - 2);
								
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
		
		//displayTest(schedule, numEmployees, DAYS, false);
		
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
				System.out.println("\nSchedule for " + day + ":\n");
				displayWallSchedule(day, schedule, numEmployees, DAYS);
				break;
			case 2:
				day = "Monday";
				System.out.println("\nSchedule for " + day + ":\n");
				displayWallSchedule(day, schedule, numEmployees, DAYS);
				break;
			case 3:
				day = "Tuesday";
				System.out.println("\nSchedule for " + day + ":\n");
				displayWallSchedule(day, schedule, numEmployees, DAYS);
				break;
			case 4:
				day = "Wednesday";
				System.out.println("\nSchedule for " + day + ":\n");
				displayWallSchedule(day, schedule, numEmployees, DAYS);
				break;
			case 5:
				day = "Thursday";
				System.out.println("\nSchedule for " + day + ":\n");
				displayWallSchedule(day, schedule, numEmployees, DAYS);
				break;
			case 6:
				day = "Friday";
				System.out.println("\nSchedule for " + day + ":\n");
				displayWallSchedule(day, schedule, numEmployees, DAYS);
				break;
			case 7:
				day = "Saturday";
				System.out.println("\nSchedule for " + day + ":\n");
				displayWallSchedule(day, schedule, numEmployees, DAYS);
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
	
	//Chronologically sorting Shift[][] 
	//Sorting algorithm: Ascending (least to greatest)(earliest to latest)
	public static void sortAscending(Shift[][] unsortedSchedule, int numEmployees, int days)
	{		
		Shift temp = new Shift();
		
		//numEmployees represents rows, days represents columns
		for(int a = 0; a < numEmployees; a++)
		{
			for(int b = 0; b < days; b++)
			{
				for(int c = 0; c < numEmployees; c++)
				{
					for(int d = 0; d < days; d++)
					{
						/*If schedule is not null and military time of the initial element is
						greater than military time of current element then swap the current element
						and	initial element*/
						if(unsortedSchedule[c][d] != null && unsortedSchedule[a][b] != null)
						{
							if(getMilitaryTime(unsortedSchedule[c][d].startTime) > getMilitaryTime(
									unsortedSchedule[a][b].startTime))
							{
								temp = unsortedSchedule[a][b];
								unsortedSchedule[a][b] = unsortedSchedule[c][d];
								unsortedSchedule[c][d] = temp;
							}
						}
					}
				}
			}
		}
	}
	
	//Method that calls the displayShifts method using the positions included in wall schedule
	public static void displayWallSchedule(String day, Shift[][] myArray, int rows, int cols)
	{
		System.out.println("***********FRONT LINE SUPV**********");
		displayShifts("Front Line Supv", day, myArray, rows, cols);
		
		System.out.println("***********SELF CHECK-OUT**********");
		displayShifts("Selfcheck Attendant", day, myArray, rows, cols);
		
		System.out.println("***********MEMBER SERVICES**********");
		displayShifts("Member Services", day, myArray, rows, cols);
		
		System.out.println("***********FRONT DOOR**********");
		displayShifts("Front Door", day, myArray, rows, cols);
		
		System.out.println("***********CARTS**********");
		displayShifts("Stock/Cart Retriever", day, myArray, rows, cols);
		
		System.out.println("***********RECOVERY**********");
		displayShifts("Recovery", day, myArray, rows, cols);
		
		System.out.println("***********FOOD COURT**********");
		displayShifts("Food", day, myArray, rows, cols);
		
		System.out.println("***********TIRE BAY**********");
		displayShifts("Tire", day, myArray, rows, cols);
		
		System.out.println("***********MAINTENANCE**********");
		displayShifts("Maintenance", day, myArray, rows, cols);
		
		System.out.println("***********DELI**********");
		displayShifts("Deli", day, myArray, rows, cols);
		
		System.out.println("***********BAKERY**********");
		displayShifts("Bakery", day, myArray, rows, cols);
		
		System.out.println("***********CASH OFFICE**********");
		displayShifts("Office", day, myArray, rows, cols);
		
		System.out.println("***********MEAT**********");
		displayShifts("Meat", day, myArray, rows, cols);
		
		System.out.println("***********PRODUCE**********");
		displayShifts("Produce", day, myArray, rows, cols);
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
					if(myArray[x][y] != null && myArray[x][y].employee.contains("Sanchez"))
					{						
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
	public static void displayShifts(String position, String date, Shift[][] myArray, int rows,
			int cols)
	{
		for(int x = 0; x < rows; x++)
		{
			for(int y = 0; y < cols; y++)
			{
				if(myArray[x][y] != null && myArray[x][y].date == date
						&& myArray[x][y].position.contains(position))
				{						
					System.out.println(myArray[x][y]);
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
