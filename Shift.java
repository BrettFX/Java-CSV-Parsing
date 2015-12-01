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

/**
 * @author Christian Bryce Alexander and Brett Michael Allen
 * @since Oct 22, 2015, 1:44:50 PM
 */
public class Shift {
	public String employee;
	public String position;
	public String startTime;
	public String endTime;

	public Shift(String employee, String position, String startTime, String endTime) {
		this.employee = employee.trim();
		this.position = position.trim();
		this.startTime = startTime.trim();
		this.endTime = endTime.trim();
	}

	@Override
	public String toString() {
		return employee + "\nPosition: " + position + 
				"\nTime: " + startTime + " - " + endTime + "\n";
	}
}
