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

/**
 * @author Christian Bryce Alexander and Brett Michael Allen
 * @since Oct 22, 2015, 1:44:50 PM
 */
public class Shift {
	public String employee;
	public String position;
	public String startTime;
	public String endTime;
	public String date;
	
	public Shift(){
		employee = "";
		position = "";
		startTime = "";
		endTime = "";
		date = "";
	}

	public Shift(String employee, String position, String startTime, String endTime, int day) {
		this.employee = employee.trim();
		this.position = position.trim();
		this.startTime = startTime.trim();
		this.endTime = endTime.trim();
		
		switch(day){
		case 0:
			date = "Sunday";
			break;
		case 1:
			date = "Monday";
			break;
		case 2:
			date = "Tuesday";
			break;
		case 3:
			date = "Wednesday";
			break;
		case 4:
			date = "Thursday";
			break;
		case 5:
			date = "Friday";
			break;
		case 6:
			date = "Saturday";
		default:
			break;
		}
	}
	
	public String getDate()
	{
		return date;
	}
	
	public String getPosition()
	{
		return position;
	}

	@Override
	public String toString() {
		return "Employee: " + employee + "\nPosition: " + position + "\nDay: " + date + 
				"\nTime: " + startTime + " - " + endTime + "\n";
	}
}
