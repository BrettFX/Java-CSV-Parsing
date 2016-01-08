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
		String[] csv = readFile(TEST2);
		String day = "";
		Scanner input = new Scanner(System.in);

		int startCol = 0;
		int numEmployees = 0;
		int e = -1;
		int choice = -1;
		
		int iOffset = 0,
			jOffset = 0;
		
		//Used for input validation
		char invalidChoice = ' ';
		
		//Used to handle discrepancies in input file
		boolean outlier = false;

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
			String lineA = csv[i];

			// Line that starts with an employee's name (names are surrounded in "")
			if (lineA.startsWith("\"")) 
			{
				// Current row in schedule
				e++;

				// Line that contains start and end times
				String lineB = csv[i + 1];

				// gets employee name
				String employee = lineA.substring(1, lineA.lastIndexOf("\""));

				for (int j = startCol; j < startCol + 7; j++) 
				{
					String shiftPosition = lineA.split(",")[j + 1];

					// skips empty fields
					if (shiftPosition.equals("."))
					{
						continue;					
					}
					
					String shiftTime = lineB.split(",")[j];
					
					//System.out.println("1st:" + employee + " - " + shiftPosition);
					
					/*If shiftTime doesn't start with a number and the current employee has
					NOT be assigned a shift time then go to the next row in the .csv file
					and get the the shift time for that employee. Otherwise, continue
					
					DO NOT GO TO THE NEXT ROW IN THE SCHEDULE ARRAY IF AN EMPLOYEE'S SHIFT
					TIME HAS NOT BEEN DETERMINED
					*/

					// skips invalid start and end times (or vacations, unpaid days off)					
					if (!startsWithNumber(shiftTime))
					{
						/*System.out.println(employee + "\'s shift did not start with a number!");
						System.out.println("The attempted shift time was: " + shiftTime);*/
						
						/*Try to go to the next row in the .csv file and if there are shift times
						process those shift times for the employee that would've been skipped.
						Else skip and continue because that means the employee is off that day.*/
						
						//Set the the row controlling variable to the next line in the .csv file
						//and try to get a shift time
						
						iOffset = i + 1;						
						lineB = csv[iOffset + 1];
						
						shiftTime = lineB.split(",")[j];
						
						//Now if the shiftTime is still not a number skip that employee
						if(!startsWithNumber(shiftTime))
						{							
							continue;
						}
						
						//shiftTime = lineA.split(",")[offset];
						
						//System.out.println(employee + " - " + shiftTime);
						
						/*if(!startsWithNumber(shiftTime))
						{
							continue;
						}	*/	
					}
					
					//System.out.println("2nd:" + employee + " - " + shiftPosition);

					// creates new shift
					Shift shift;
					
					shift = new Shift(employee, shiftPosition, shiftTime.split("-")[0], shiftTime.split("-")[1],
							j - 2);
					
					// assigns new shift
					schedule[e][j - startCol] = shift;
					
					/*System.out.println(schedule[e][j - startCol].employee + 
							" - " + schedule[e][j - startCol].position);*/
				}
			}
		}
		//End parsing
		
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
				System.err.println("\nError! Invalid input.\n");
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
		
		// Print out all shifts that are parsed by the program (initial method)
		/*for (Shift[] shiftRow : schedule)
			for (Shift shift : shiftRow)
				if(shift != null)
					System.out.println(shift);*/
	}
	
	public static void displayMenu()
	{
		/*System.out.println("\n\t\tHOME");
		System.out.println("-----------------------------------------");*/
		System.out.println("Please choose a day to print:\n");
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
	
	//Chronologically sorting Shift[][] 
	//Sorting algorithm: Ascending (least to greatest)(earliest to latest)
	public static void sortAscending(Shift[][] unsortedSchedule, int numEmployees, int days)
	{
		
		Shift temp = new Shift();
		
		//Have to make sure to only compare elements that have the same day and
		//position
		
		//numEmployees represents rows, days represents columns
		for(int a = 0; a < numEmployees; a++)
		{
			for(int b = 0; b < days; b++)
			{
				for(int c = 0; c < numEmployees; c++)
				{
					for(int d = 0; d < days; d++)
					{
						//If schedule is not null and day and position are same as next element
						//Else if null, set military time to 2400 to push to end of sorted array
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
						else
						{
							//Push all null elements to the end of the array for correct days(might be unnecessary)							
						}
					}
				}
			}
		}
	}
	
	//Method that calls the displayShifts method using the positions included in wall schedule
	public static void displayWallSchedule(String day, Shift[][] myArray, int rows, int cols)
	{
		displayShifts("Front Line Supv", day, myArray, rows, cols);
		displayShifts("Selfcheck Attendant", day, myArray, rows, cols);
		displayShifts("Member Services", day, myArray, rows, cols);
		displayShifts("Front Door", day, myArray, rows, cols);
		displayShifts("Stock/Cart Retriever", day, myArray, rows, cols);
		displayShifts("Recovery", day, myArray, rows, cols);
		displayShifts("Food", day, myArray, rows, cols);
		displayShifts("Tire", day, myArray, rows, cols);
		displayShifts("Maintenance", day, myArray, rows, cols);
		displayShifts("Deli", day, myArray, rows, cols);
		displayShifts("Bakery", day, myArray, rows, cols);
		displayShifts("Office", day, myArray, rows, cols);
		displayShifts("Meat", day, myArray, rows, cols);
		displayShifts("Produce", day, myArray, rows, cols);
	}
	
	public static void displayTest(Shift[][] myArray, int rows, int cols, boolean printNull)
	{		
		for(int x = 0; x < rows; x++)
		{
			for(int y = 0; y < cols; y++)
			{
				if(printNull == false)
				{
					if(myArray[x][y] != null && myArray[x][y].position.contains("Office"))
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
	
	//Need to figure out a way to print only the date and position specified...
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
			
			if(pastNoon && militaryTime != 1200)
			{
				return militaryTime + 1200;
			}
			else
			{
				return militaryTime;
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
