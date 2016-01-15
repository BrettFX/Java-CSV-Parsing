package net.alexanderdev.csvparsing;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	
	private static String day;
	
	public Logger(String day)
	{
		this.day = day;
	}
	
	public void log(String message) throws IOException 
	{ 
      PrintWriter out = new PrintWriter(new FileWriter("Front Line Schedule for " + day + ".txt"
    		  , true), true);
      
      out.write(message);
      out.write("\n");
      out.close();
    }
}
