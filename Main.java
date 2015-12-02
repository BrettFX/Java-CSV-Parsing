/*****************************************************************
 *   ____ ______     __  ____   _    ____  ____ ___ _   _  ____  *
 *  / ___/ ___\ \   / / |  _ \ / \  |  _ \/ ___|_ _| \ | |/ ___| *
 * | |   \___ \\ \ / /  | |_) / _ \ | |_) \___ \| ||  \| | |  _  *
 * | |___ ___) |\ V /   |  __/ ___ \|  _ < ___) | || |\  | |_| | *
 *  \____|____/  \_/    |_| /_/   \_\_| \_\____/___|_| \_|\____| *
 *                                                               *
 * Copyright (C) 2015, Christian Alexander, Brett Allen          *
 *****************************************************************/
//package here

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * @author Christian Bryce Alexander and Brett Michael Allen
 * @since Oct 22, 2015, 1:13:31 PM
 */
public class Main {
	public static final String DATES = "Sun,Mon,Tue,Wed,Thu,Fri,Sat";

	// Put file in root of "res" folder and prepend a "/" in front of the
	// filename
	public static final String TEST = "/Test Schedule for Brett.csv";
	public static final String FULL = "/FLS Wall Schedule.csv";

	public static void main(String[] args) {
		Shift[][] schedule;

		//              TEST or FULL Here
		//                       |
		//                       V
		String[] csv = readFile(TEST);

		int startCol = 0;

		int numEmployees = 0;

		// Finds where the dates column starts for data parsing
		for (String line : csv) {
			if (line.contains(DATES)) {
				String[] tokens = line.split(",");

				startCol = indexOf(tokens, "Sun");

				if (startCol == -1) {
					System.err.println("Sunday not found!");
					System.exit(-1);
				}
			}

			if (line.startsWith("\"")) {
				numEmployees++;
			}
		}
		// end find

		schedule = new Shift[numEmployees][7];

		int e = -1;

		// Parsing data
		for (int i = 0; i < csv.length; i++) {
			// Line that contains position for each day
			String lineA = csv[i];

			// Line that starts with an employee's name (names are surrounded in "")
			if (lineA.startsWith("\"")) {
				// Current row in schedule
				e++;

				// Line that contains start and end times
				String lineB = csv[i + 1];

				// gets employee name
				String employee = lineA.substring(1, lineA.lastIndexOf("\""));

				for (int j = startCol; j < startCol + 7; j++) {
					String shiftPosition = lineA.split(",")[j + 1];

					// skips empty fields
					if (shiftPosition.equals("."))
						continue;

					String shiftTime = lineB.split(",")[j];

					// skips invalid start and end times (or vacations, unpaid
					// days off)
					if (!startsWithNumber(shiftTime))
						continue;

					// creates new shift
					Shift shift = new Shift(employee, shiftPosition, shiftTime.split("-")[0],
							shiftTime.split("-")[1]);

					// assigns new shift
					schedule[e][j - startCol] = shift;
				}
			}
		}

		//Implement chronological sorting algorithm here (will be withing for loop for shiftRow[]
		//Sorting algorithm: Ascending (least to greatest)(earliest to latest)
		/*boolean swapped = true;
		int tmp;
		int[] unsorted = {1,3,4,8,9,6,2,7,5};
		
		do{
			swapped = false;
			for(int i = 0; i < (unsorted.length - 1);i++){
				if(unsorted[i] > unsorted[i + 1]){
					tmp = unsorted[i];
					unsorted[i] = unsorted[i + 1];
					unsorted[i + 1] = tmp;
					swapped = true;
				}
			}
		}while(swapped);
		
		for(int sorted : unsorted)
			System.out.println(sorted);*/
		
		// Print out all shifts that are parsed by the program and sorted in chronological order
		for(int x = 0; x < numEmployees; x++){
			for(int y = 0; y < 7; y++){
				if(schedule[x][y] != null){
					System.out.println(schedule[x][y]);					
				}
			}
		}	
		
		// Print out all shifts that are parsed by the program (initial method)
		/*for (Shift[] shiftRow : schedule)
			for (Shift shift : shiftRow)
				if(shift != null)
					System.out.println(shift);*/
	}

	//Searching for shift times
	public static boolean startsWithNumber(String shiftTime) {
		String[] nums = {
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
		};

		for (String num : nums)
			if (shiftTime.startsWith(num))
				return true;

		return false;
	}

	public static int indexOf(String[] tokens, String find) {
		for (int i = 0; i < tokens.length; i++)
			if (tokens[i].contains(find))
				return i;
		return -1;
	}

	public static String[] readFile(String path) {
		ArrayList<String> lines = new ArrayList<>();

		InputStream inStream = Main.class.getResourceAsStream(path);

		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

		String line;

		try {
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return lines.toArray(new String[lines.size()]);
	}

	public static void writeFile(String path, String[] lines) {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));

			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
