package net.alexanderdev.csvparsing;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	
	private String day;
	private String date;
	
	public Logger(String day, String date)
	{
		this.day = day;
		this.date = date;
	}
	
	public void log(String message) throws IOException 
	{ 
      PrintWriter out = new PrintWriter(new FileWriter("Front Line Schedule for " + day +
    		  " " + date + ".txt"
    		  , true), true);
      
      out.println(message);
      out.close();
    }
}
