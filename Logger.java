package net.alexanderdev.csvparsing;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	
	private static String day;
	private static String date;
	
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
      
      out.write(message);
      out.write("\n");
      out.close();
    }
}
