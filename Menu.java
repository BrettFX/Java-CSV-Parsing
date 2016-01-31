package net.alexanderdev.csvparsing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	private int rows, 
				cols;
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
	
	//JCheckBoxes
	private JCheckBox cbxFrontLine;
	private JCheckBox cbxCashier;
	
	//JLabels
	private JLabel lblChoice;
	private JLabel lblCbxChooser;
	
	//Delegate menu object
	public Menu menu;
	
	public Menu()
	{
		//Set the title of the JFrame
		super("BJ's Wholesale Scheduler (Version 3.5.2)");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 676, 628);
		getContentPane().setLayout(null);
		
		path = new JTextField();
		path.setEditable(false);
		path.setBounds(121, 14, 525, 24);
		getContentPane().add(path);
		path.setColumns(10);
		
		lblChoice = new JLabel("Choose day(s) to process:");
		lblChoice.setFont(new Font("Consolas", Font.PLAIN, 14));
		lblChoice.setBounds(12, 144, 261, 16);
		getContentPane().add(lblChoice);
		
		lblCbxChooser = new JLabel("Select schedule(s) to create:");
		lblCbxChooser.setFont(new Font("Consolas", Font.PLAIN, 14));
		lblCbxChooser.setBounds(12, 55, 243, 16);
		getContentPane().add(lblCbxChooser);

		btnOpenFile = new JButton("Open File");
		btnOpenFile.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnOpenFile.setBounds(12, 13, 97, 25);
		getContentPane().add(btnOpenFile);
		
		btnSunday = new JButton("Process Sunday");
		btnSunday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnSunday.setEnabled(false);
		btnSunday.setBounds(22, 173, 238, 25);
		getContentPane().add(btnSunday);
		
		btnMonday = new JButton("Process Monday");
		btnMonday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnMonday.setEnabled(false);
		btnMonday.setBounds(22, 211, 238, 25);
		getContentPane().add(btnMonday);
		
		btnTuesday = new JButton("Process Tuesday");
		btnTuesday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnTuesday.setEnabled(false);
		btnTuesday.setBounds(22, 249, 238, 25);
		getContentPane().add(btnTuesday);
		
		btnWednesday = new JButton("Process Wednesday");
		btnWednesday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnWednesday.setEnabled(false);
		btnWednesday.setBounds(22, 287, 238, 25);
		getContentPane().add(btnWednesday);
		
		btnThursday = new JButton("Process Thursday");
		btnThursday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnThursday.setEnabled(false);
		btnThursday.setBounds(22, 325, 238, 25);
		getContentPane().add(btnThursday);
		
		btnFriday = new JButton("Process Friday");
		btnFriday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnFriday.setEnabled(false);
		btnFriday.setBounds(22, 363, 238, 25);
		getContentPane().add(btnFriday);
		
		btnSaturday = new JButton("Process Saturday");
		btnSaturday.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnSaturday.setEnabled(false);
		btnSaturday.setBounds(22, 401, 238, 25);
		getContentPane().add(btnSaturday);		

		btnAll = new JButton("Process All Days");
		btnAll.setFont(new Font("Consolas", Font.PLAIN, 13));
		btnAll.setEnabled(false);
		btnAll.setBounds(22, 439, 238, 25);
		getContentPane().add(btnAll);
		
		btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Consolas", Font.PLAIN, 18));
		btnReset.setEnabled(false);
		btnReset.setBounds(22, 477, 238, 40);
		getContentPane().add(btnReset);

		btnCopyright = new JButton("Created By: Brett Allen");
		btnCopyright.setEnabled(false);
		btnCopyright.setForeground(Color.WHITE);
		btnCopyright.setBackground(Color.LIGHT_GRAY);
		btnCopyright.setFont(new Font("Consolas", Font.PLAIN, 16));
		btnCopyright.setBounds(300, 477, 330, 87);
		getContentPane().add(btnCopyright);
		
		btnInstructions = new JButton("Instructions");
		btnInstructions.setFont(new Font("Consolas", Font.PLAIN, 18));
		btnInstructions.setBounds(22, 524, 238, 40);
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
		scrollPane.setBounds(300, 51, 330, 415);		
		getContentPane().add(scrollPane);
		
		cbxFrontLine = new JCheckBox("Front Line Schedule");
		cbxFrontLine.setEnabled(false);
		cbxFrontLine.setFont(new Font("Consolas", Font.PLAIN, 13));
		cbxFrontLine.setBounds(12, 80, 170, 25);
		getContentPane().add(cbxFrontLine);
		
		cbxCashier = new JCheckBox("Cashier Schedule");
		cbxCashier.setEnabled(false);
		cbxCashier.setFont(new Font("Consolas", Font.PLAIN, 13));
		cbxCashier.setBounds(12, 110, 155, 25);
		getContentPane().add(cbxCashier);

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
				toggleCheckboxState(false);				
				toggleCheckboxes(false);
				toggleButtons(false);
				btnReset.setEnabled(false);
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
		
		cbxFrontLine.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				if(cbxFrontLine.isSelected())
					toggleButtons(true);
				else if (!cbxFrontLine.isSelected() && !cbxCashier.isSelected())
					toggleButtons(false);
			}
		});
		
		cbxCashier.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				if(cbxCashier.isSelected())
					toggleButtons(true);
				else if (!cbxFrontLine.isSelected() && !cbxCashier.isSelected())
					toggleButtons(false);
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
	
	public boolean getCbxFrontLineState()
	{
		return cbxFrontLine.isSelected();
	}
	
	public boolean getCbxCashierState()
	{
		return cbxCashier.isSelected();
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
		myPath.setCurrentDirectory(new File("C:/Users/Brett/Documents/BJs Wholesale Club/Schedules"));
		myPath.setDialogTitle("Open");
		
		if(myPath.showOpenDialog(btnOpenFile) != JFileChooser.APPROVE_OPTION)		
			path.setText("");
		else
			path.setText(myPath.getSelectedFile().getAbsolutePath());
		
		//Enable processing buttons
		if(path.getText().contains(".xls"))
		{	
			//Set cursor to loading symbol
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
			//Call the main classes mainDelegate method when a new file has been chosen in order to process that file
			Main.mainDelegate(menu);
			
			toggleCheckboxes(true);
			btnReset.setEnabled(true);
			
			//Turn off wait cursor
			setCursor(null);
		}	
		else if(path.getText().equals(""))
		{
			toggleButtons(false);
			toggleCheckboxState(false);
			toggleCheckboxes(false);
			JOptionPane.showMessageDialog(null, "No file chosen.", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			toggleButtons(false);
			toggleCheckboxState(false);
			toggleCheckboxes(false);
			path.setText("");
			JOptionPane.showMessageDialog(null, "You must choose an Excel File (.xls)", "ERROR", JOptionPane.ERROR_MESSAGE);			
		}
	}
	
	private void toggleButtons(boolean b)
	{
		//Only enable the processing buttons when at least one type of schedule to be
		//process is selected		
		btnSunday.setEnabled(b);
		btnMonday.setEnabled(b);
		btnTuesday.setEnabled(b);
		btnWednesday.setEnabled(b);
		btnThursday.setEnabled(b);
		btnFriday.setEnabled(b);
		btnSaturday.setEnabled(b);
		btnAll.setEnabled(b);
	}
	
	private void toggleCheckboxes(boolean b)
	{
		cbxFrontLine.setEnabled(b);
		cbxCashier.setEnabled(b);
	}
	
	private void toggleCheckboxState(boolean b)
	{
		cbxFrontLine.setSelected(b);
		cbxCashier.setSelected(b);
	}
	
	private void displayInstructions()
	{
		JOptionPane.showMessageDialog(null,
				"Please complete the following before using this program:\n\n"
				+ "    1) Save the schedule for the week by department (All labor) as an excel file (.xls).\n"
				+ "             - When saving the excel file it is imperative that you save it correctly.\n"
				+ "             - When the save dialog box appears Click the drop down arrow next to \"Save as type:\"\n"
				+ "               and select the option \"Excel 97-2003 Workbook (*.xls)\"\n\n"
				+ "    2) (Optional) Rename the Excel file that is being saved.\n\n"
				+ "Using this program:\n"
				+ "    - Begin by clicking \"Open File\"\n"
				+ "    - Navigate to the Excel file (file_name.xls) previously saved.\n"
				+ "    - Open the Excel file by clicking \"open\"\n"
				+ "    - If the correct file was chosen the checkboxes will enable, allowing you to select schedule types\n"
				+ "      to be processed.\n"
				+ "    - At least one checkbox must be checked in order for the action buttons to enable. Once a checkbox\n"
				+ "      is checked, the action buttons will enable and you can select which day(s) to create a schedule for.\n"
				+ "    - Select any option by clicking the button designated for the desired operation.\n"
				+ "    - Click \"Reset\" to clear everything (console, file path, disable checkboxes and buttons)\n"
				+ "    - If you choose to reset everything you must choose another file in order to re-enable the checkboxes",
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
