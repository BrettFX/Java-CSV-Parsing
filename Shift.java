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

public class Shift 
{	
	public String employee;
	public String position;
	public String startTime;
	public String endTime;		
	public String day;
	
	public Shift()
	{
		employee = "";
		position = "";
		startTime = "";
		endTime = "";
		day = "";
	}

	public Shift(String employee, String position, String startTime, String endTime, int day) 
	{
		this.employee = employee.trim();
		this.position = position.trim();
		this.startTime = startTime.trim();
		this.endTime = endTime.trim();
		
		switch(day)
		{
		case 0:
			this.day = "Sunday";
			break;
		case 1:
			this.day = "Monday";
			break;
		case 2:
			this.day = "Tuesday";
			break;
		case 3:
			this.day = "Wednesday";
			break;
		case 4:
			this.day = "Thursday";
			break;
		case 5:
			this.day = "Friday";
			break;
		case 6:
			this.day = "Saturday";
		default:
			break;
		}
	}
	
	public String displayShift()
	{	
		return "\n" + employee.split(",")[1].trim() + " " + employee.split(",")[0].trim().charAt(0) 
				+ ": " +	startTime + " - " + endTime;
	}
	
	public String getShiftTime()
	{
		String[] truncStart = new String[2];
		
		if(startTime.contains("a"))
			truncStart = startTime.split("a");
		else if(startTime.contains("p"))
			truncStart = startTime.split("p");
		
		return truncStart[0] + "-" + endTime;
	}
	
	public String getName()
	{
		String nickName = "";
		
		if(employee.split(",")[1].trim().contains("Christopher"))
		{
			nickName = "Chris";
			
			return nickName + " " + employee.split(",")[0].trim().charAt(0);
		}		
		else if(employee.split(",")[1].trim().contains("Mekdes"))
		{
			nickName = "Mimi";
			
			return nickName + " " + employee.split(",")[0].trim().charAt(0);
		}
		
		return employee.split(",")[1].trim() + " " + employee.split(",")[0].trim().charAt(0);
	}

	@Override
	public String toString() 
	{		
		return "\n" + employee.split(",")[1].trim() + " " + employee.split(",")[0].trim().charAt(0) 
				+ ": " +	startTime + " - " + endTime;
		
		/*return "Day: " + day + "\nEmployee: " + employee + "\nPosition: " + position + 
				"\nTime: " + startTime + " - " + endTime + "\n";*/
	}
}
