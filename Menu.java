package net.alexanderdev.csvparsing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class Menu extends JFrame implements ActionListener
{	
	private static final long serialVersionUID = 1L;
	private JTextField path;
	private JTextArea logArea;
	private Shift[][] myArray;
	private int rows, cols;
	private ArrayList<String> truncDates;
	
	//JButtons
	private JButton btnOpenFile;
	private JButton btnSunday;
	private JButton btnMonday;
	private JButton btnTuesday;
	private JButton btnWednesday;
	private JButton btnThursday;
	private JButton btnFriday;
	private JButton btnSaturday;
	private JButton btnAll;
	private JButton btnCopyright;
	private JButton btnReset;
	private JButton btnInstructions;
	
	//JLabels
	private JLabel lblChoice;
	
	//Delegate menu object
	public Menu menu;
	
	public Menu()
	{
		//Set the title of the JFrame
		super("BJ's Wholesale Front Line Schedule Converter (Version 3.2)");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 676, 531);
		getContentPane().setLayout(null);
		
		path = new JTextField();
		path.setEditable(false);
		path.setBounds(121, 14, 525, 24);
		getContentPane().add(path);
		path.setColumns(10);
		
		lblChoice = new JLabel("Choose day to process:");
		lblChoice.setFont(new Font("Consolas", Font.PLAIN, 14));
		lblChoice.setBounds(22, 51, 261, 16);
		getContentPane().add(lblChoice);

		btnOpenFile = new JButton("Open File");
		btnOpenFile.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnOpenFile.setBounds(12, 13, 97, 25);
		getContentPane().add(btnOpenFile);
		
		btnSunday = new JButton("Process Sunday");
		btnSunday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnSunday.setEnabled(false);
		btnSunday.setBounds(32, 80, 238, 25);
		getContentPane().add(btnSunday);
		
		btnMonday = new JButton("Process Monday");
		btnMonday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnMonday.setEnabled(false);
		btnMonday.setBounds(32, 118, 238, 25);
		getContentPane().add(btnMonday);
		
		btnTuesday = new JButton("Process Tuesday");
		btnTuesday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnTuesday.setEnabled(false);
		btnTuesday.setBounds(32, 156, 238, 25);
		getContentPane().add(btnTuesday);
		
		btnWednesday = new JButton("Process Wednesday");
		btnWednesday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnWednesday.setEnabled(false);
		btnWednesday.setBounds(32, 194, 238, 25);
		getContentPane().add(btnWednesday);
		
		btnThursday = new JButton("Process Thursday");
		btnThursday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnThursday.setEnabled(false);
		btnThursday.setBounds(32, 232, 238, 25);
		getContentPane().add(btnThursday);
		
		btnFriday = new JButton("Process Friday");
		btnFriday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnFriday.setEnabled(false);
		btnFriday.setBounds(32, 270, 238, 25);
		getContentPane().add(btnFriday);
		
		btnSaturday = new JButton("Process Saturday");
		btnSaturday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnSaturday.setEnabled(false);
		btnSaturday.setBounds(32, 308, 238, 25);
		getContentPane().add(btnSaturday);		

		btnAll = new JButton("Process All Days");
		btnAll.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnAll.setEnabled(false);
		btnAll.setBounds(32, 346, 238, 25);
		getContentPane().add(btnAll);
		
		btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Consolas", Font.PLAIN, 18));
		btnReset.setEnabled(false);
		btnReset.setBounds(32, 384, 238, 40);
		getContentPane().add(btnReset);

		btnCopyright = new JButton("Created By: Brett Allen");
		btnCopyright.setEnabled(false);
		btnCopyright.setForeground(Color.WHITE);
		btnCopyright.setBackground(Color.LIGHT_GRAY);
		btnCopyright.setFont(new Font("Consolas", Font.PLAIN, 16));
		btnCopyright.setBounds(300, 384, 330, 87);
		getContentPane().add(btnCopyright);
		
		btnInstructions = new JButton("Instructions");
		btnInstructions.setFont(new Font("Consolas", Font.PLAIN, 18));
		btnInstructions.setBounds(32, 431, 238, 40);
		getContentPane().add(btnInstructions);
		
		logArea = new JTextArea();
		logArea.setFont(new Font("Consolas", Font.PLAIN, 16));
		logArea.setEditable(false);	
		logArea.setWrapStyleWord(true);
		logArea.setLineWrap(true);
		
		//Setting the caret to be always keep up with the scrollPane (user won't have to scroll down)
		DefaultCaret caret = (DefaultCaret)logArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		//Create scrollPane to handle lines exceeding height of logArea
		JScrollPane scrollPane =  new JScrollPane(logArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
                );
		scrollPane.setBounds(300, 51, 330, 320);		
		getContentPane().add(scrollPane);
		
		this.displayInstructions();
		
		//Action listeners
		btnOpenFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				try 
				{
					openFileDialog();
				} 
				catch (BiffException e1) {}
				catch (BadLocationException e1) {}
				catch (IOException e1) {}
			}
		});
		
		btnSunday.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{	
				try{render("Sunday", myArray, rows, cols, 1, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
			}
		});
		
		btnMonday.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				try{render("Monday", myArray, rows, cols, 2, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
			}
		});
		
		btnTuesday.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				try{render("Tuesday", myArray, rows, cols, 3, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
			}
		});
		
		btnWednesday.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				try{render("Wednesday", myArray, rows, cols, 4, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
			}
		});
		
		btnThursday.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				try{render("Thursday", myArray, rows, cols, 5, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
			}
		});
		
		btnFriday.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				try{render("Friday", myArray, rows, cols, 6, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
			}
		});
		
		btnSaturday.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				try{render("Saturday", myArray, rows, cols, 7, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
			}
		});
		
		btnAll.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				try{render("Sunday", myArray, rows, cols, 1, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
				
				try{render("Monday", myArray, rows, cols, 2, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
				
				try{render("Tuesday", myArray, rows, cols, 3, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
				
				try{render("Wednesday", myArray, rows, cols, 4, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
				
				try{render("Thursday", myArray, rows, cols, 5, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
				
				try{render("Friday", myArray, rows, cols, 6, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
				
				try{render("Saturday", myArray, rows, cols, 7, truncDates);} 
				catch (RowsExceededException e2){}
				catch (BiffException e2) {}
				catch (WriteException e2){}
				catch (IOException e2){} 
				catch (BadLocationException e2){}
			}
		});
		
		btnReset.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				logArea.setText("");
				path.setText("");
				toggleButtons(false);
			}
		});
		
		btnInstructions.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				displayInstructions();
			}
		});
		//End action listeners
	}
	
	public String getPath()
	{	
		if(path.getText() != null)
			return path.getText();
		return "";
	}
	
	public void transferVariables(Shift[][] myArray, int rows, int cols, ArrayList<String> truncDates)
	{	
		this.myArray = myArray;
		this.rows = rows;
		this.cols = cols;
		this.truncDates = truncDates;
	}
	
	private void openFileDialog() throws BadLocationException, BiffException, IOException
	{
		FileNameExtensionFilter filter = new FileNameExtensionFilter("xls files", "xls");
		JFileChooser myPath = new JFileChooser();
		
		myPath.setFileFilter(filter);
		
		//Use these paths if testing on main: "C:/Users/Brett/Workspace/Java/CSV Parsing/res"
		//									  "C:/Users/Brett/Documents/BJs Wholesale Club/Schedules"
		myPath.setCurrentDirectory(new File("C:/"));
		myPath.setDialogTitle("Open");
		
		if(myPath.showOpenDialog(btnOpenFile) != JFileChooser.APPROVE_OPTION)		
			path.setText("");
		else
			path.setText(myPath.getSelectedFile().getAbsolutePath());
		
		//Enable processing buttons
		if(path.getText().contains(".xls"))
		{
			toggleButtons(true);
			
			//Call the main classes mainDelegate method when a new file has been chosen in order to process that file
			Main.mainDelegate(menu);
		}	
		else if(path.getText().equals(""))
		{
			toggleButtons(false);
			JOptionPane.showMessageDialog(null, "No file chosen.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			toggleButtons(false);
			JOptionPane.showMessageDialog(null, "You must choose an Excel File (.xls)", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void toggleButtons(boolean b)
	{
		btnSunday.setEnabled(b);
		btnMonday.setEnabled(b);
		btnTuesday.setEnabled(b);
		btnWednesday.setEnabled(b);
		btnThursday.setEnabled(b);
		btnFriday.setEnabled(b);
		btnSaturday.setEnabled(b);
		btnAll.setEnabled(b);
		btnReset.setEnabled(b);
	}
	
	private void displayInstructions()
	{
		JOptionPane.showMessageDialog(null,
				"Please complete the following before using this program:\n\n"
				+ "    1) Save the schedule for the week by department (All labor) as an excel file (.xls).\n"
				+ "         When saving the excel file it is imperative that you save it correctly.\n"
				+ "         When the save dialog box appears Click the drop down arrow next to \"Save as type:\"\n"
				+ "         and select the option \"Excel 97-2003 Workbook (*.xls)\"\n\n"
				+ "    2) (Optional) Rename the Excel file that is being saved.\n\n"
				+ "Using this program:\n"
				+ "    - Begin by clicking \"Open File\"\n"
				+ "    - Navigate to the Excel file (file_name.xls) previously saved.\n"
				+ "    - Open the Excel file by clicking \"open\"\n"
				+ "    - If the correct file was chosen the action buttons will enable, allowing you to process schedules\n"
				+ "      for the week.\n"
				+ "    - Select any option by clicking the button designated for the desired operation.\n"
				+ "    - Click \"Reset\" to clear everything (console, file path, disable buttons)\n"
				+ "    - If you choose to reset everything you must choose another file in order to re-enable the buttons",
				"Instructions", JOptionPane.INFORMATION_MESSAGE
		);
	}
	
	public void render(String day, Shift[][] myArray, int rows, int cols,
			int choice,  ArrayList<String> truncDates) throws RowsExceededException, BiffException, WriteException,
																IOException, BadLocationException
	{
		Main.renderChoice(day, myArray, rows, cols, choice, truncDates, menu);
	}
	
	public void print(String text) throws BadLocationException
	{     
		Document doc = logArea.getDocument();
		logArea.getDocument().insertString(doc.getLength(), text, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{			
	}
}
