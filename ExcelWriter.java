package net.alexanderdev.csvparsing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import jxl.Cell;
import jxl.CellType;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelWriter 
{
	private Workbook input;
	private WritableWorkbook copy;
	private WritableSheet sheet1;
	private WritableCell cell;
	private File inFile;
	
	public ExcelWriter(InputStream path, String day, String date, String year) throws BiffException, IOException
	{	
		input = Workbook.getWorkbook(path);		
		
		copy = Workbook.createWorkbook(new File("Front Line Schedule for " + day +
	    		  " " + date + "-" + year + ".xls"), input);
		
		//Set the writable sheet to Sheet1 of the input excel file
		sheet1 = copy.getSheet(0);
	}
	
	public ExcelWriter(String path) throws BiffException, IOException
	{		
		input = Workbook.getWorkbook(new File(path));	
		
		inFile = new File("input.xls");
		
		copy = Workbook.createWorkbook(inFile, input);
		
		//Set the writable sheet to Sheet1 of the input excel file
		sheet1 = copy.getSheet(0);
	}
	
	public void deleteColumn(int col)
	{
		sheet1.removeColumn(col);
	}
	
	public boolean isEmptyCell(int col, int row)
	{
		cell = sheet1.getWritableCell(col, row);
		
		if(cell.getType() == CellType.EMPTY)
			return true;
		return false;
	}
	
	public void overwriteEmptyCell(int col, int row, String text, int cellFormat) throws RowsExceededException, WriteException, IOException
	{		
		//Set writable cell to the first name cell within FRONT LINE SUPERVISOR table		
		Label lbl = new Label(col , row, text, format(cellFormat));
		sheet1.addCell(lbl);
	}
	
	public WritableWorkbook getCopy()
	{
		return copy;
	}
	
	public File getXlsFile()
	{
		return inFile;
	}
	
	public String getXlsFilePath()
	{
		return inFile.getAbsolutePath().toString();
	}
	
	public WritableCellFormat format(int choice) throws WriteException
	{
		WritableFont cellFont;
		WritableCellFormat cellFormat;
		
		switch(choice)
		{
		case 0://Day and Date
			//Set the font to Tahoma 12pt
			cellFont = new WritableFont(WritableFont.TAHOMA, 12);
			
			cellFormat = new WritableCellFormat(cellFont);
			
			return cellFormat;
		case 1://Names
			//Set the font to Tahoma 10pt
			cellFont = new WritableFont(WritableFont.TAHOMA, 10);
			
			//Set the cell format to have all boarders (prevents overwriting boarders)
			cellFormat = new WritableCellFormat(cellFont);
			cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			
			return cellFormat;
		case 2: //Shift times
			//Set the font to 9pt
			cellFont = new WritableFont(WritableFont.TAHOMA, 9);
			
			cellFormat = new WritableCellFormat(cellFont);
			cellFormat.setAlignment(Alignment.CENTRE);
			cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			
			return cellFormat;
		default:
			return null;	
		}
	}
	
	public String text(int col, int row)
	{
		Cell c = sheet1.getCell(col, row);
		
		return c.getContents();
	}
	
	public void write() throws IOException
	{
		copy.write();
	}
	
	public void close() throws IOException
	{
		try 
		{
			copy.close();
		} 
		catch (WriteException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void writeAndClose() throws IOException
	{
		copy.write();
		
		try 
		{
			copy.close();
		} 
		catch (WriteException e) 
		{
			e.printStackTrace();
		}
	}
}
