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
	
	//JLabels
	private JLabel lblChoice;
	
	public Menu()
	{
		//Set the title of the JFrame
		super("Front Line Schedule Converter (Version 3.1)");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 676, 531);
		getContentPane().setLayout(null);
		
		path = new JTextField();
		path.setEditable(false);
		path.setBounds(121, 14, 525, 24);
		getContentPane().add(path);
		path.setColumns(10);
		
		lblChoice = new JLabel("Choose day to process schedule for:");
		lblChoice.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblChoice.setBounds(22, 51, 261, 16);
		getContentPane().add(lblChoice);

		btnOpenFile = new JButton("Open File");
		btnOpenFile.setBounds(12, 13, 97, 25);
		getContentPane().add(btnOpenFile);
		
		btnSunday = new JButton("Process Sunday");
		btnSunday.setEnabled(false);
		btnSunday.setBounds(32, 80, 238, 25);
		getContentPane().add(btnSunday);
		
		btnMonday = new JButton("Process Monday");
		btnMonday.setEnabled(false);
		btnMonday.setBounds(32, 118, 238, 25);
		getContentPane().add(btnMonday);
		
		btnTuesday = new JButton("Process Tuesday");
		btnTuesday.setEnabled(false);
		btnTuesday.setBounds(32, 156, 238, 25);
		getContentPane().add(btnTuesday);
		
		btnWednesday = new JButton("Process Wednesday");
		btnWednesday.setEnabled(false);
		btnWednesday.setBounds(32, 194, 238, 25);
		getContentPane().add(btnWednesday);
		
		btnThursday = new JButton("Process Thursday");
		btnThursday.setEnabled(false);
		btnThursday.setBounds(32, 232, 238, 25);
		getContentPane().add(btnThursday);
		
		btnFriday = new JButton("Process Friday");
		btnFriday.setEnabled(false);
		btnFriday.setBounds(32, 270, 238, 25);
		getContentPane().add(btnFriday);
		
		btnSaturday = new JButton("Process Saturday");
		btnSaturday.setEnabled(false);
		btnSaturday.setBounds(32, 308, 238, 25);
		getContentPane().add(btnSaturday);		

		btnAll = new JButton("Process All Days");
		btnAll.setEnabled(false);
		btnAll.setBounds(32, 346, 238, 25);
		getContentPane().add(btnAll);		

		btnCopyright = new JButton(" Copyright (C) 2016, Brett Allen");
		btnCopyright.setEnabled(false);
		btnCopyright.setForeground(Color.WHITE);
		btnCopyright.setBackground(Color.LIGHT_GRAY);
		btnCopyright.setFont(new Font("Arial Black", Font.PLAIN, 16));
		btnCopyright.setBounds(32, 384, 598, 87);
		getContentPane().add(btnCopyright);
		
		logArea = new JTextArea();
		logArea.setFont(new Font("Tahoma", Font.PLAIN, 16));
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
		
		JOptionPane.showMessageDialog(null,
				"There are some steps to follow in order to provide the required input parameters\n"
				+ "for this program to work properly. Please complete the following:\n\n"
				+ "\t1) Save the schedule for the week by department (All labor) as an excel file (.xls).\n"
				+ "\t2) Navigate to the excel file that you just saved and open it (Click yes for the dialog box\n"
				+ "\tthat comes up).\n"
				+ "\t3) Right click column A (should hightlight all of column A).\n"
				+ "\t4) Left click \"Delete\" (everything should shift left).\n"
				+ "\t5) Left click \"File\".\n"
				+ "\t6) Left click \"Save and Send\".\n"
				+ "\t7) Left click \"Change file type\".\n"
				+ "\t8) Change the file type to Comma Separate Format (.csv) by Left clicking that option.\n"
				+ "\t9) Left click \"Save As\" and save the file (to any location prefered).",
				"Instructions", JOptionPane.INFORMATION_MESSAGE
		);
		
		openFileDialog();
		
		//Action listeners
		btnOpenFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				openFileDialog();
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
				JOptionPane.showMessageDialog(null, "Button not programmed yet...", "Instructions", JOptionPane.INFORMATION_MESSAGE);
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
	
	public void openFileDialog()
	{
		FileNameExtensionFilter filter = new FileNameExtensionFilter("csv files", "csv");
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
		if(path.getText().contains(".csv"))
		{
			btnSunday.setEnabled(true);
			btnMonday.setEnabled(true);
			btnTuesday.setEnabled(true);
			btnWednesday.setEnabled(true);
			btnThursday.setEnabled(true);
			btnFriday.setEnabled(true);
			btnSaturday.setEnabled(true);
			btnAll.setEnabled(true);
		}	
		else if(path.getText().equals(""))
		{
			JOptionPane.showMessageDialog(null, "No file chosen.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "You must choose a Comma Separated File (.csv)", "ERROR", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void render(String day, Shift[][] myArray, int rows, int cols,
			int choice,  ArrayList<String> truncDates) throws RowsExceededException, BiffException, WriteException,
																IOException, BadLocationException
	{
		print("\nRendering choice...\n");
		
		Main.renderChoice(day, myArray, rows, cols, choice, truncDates);
		
		print("Done.\n\n");
		print("The schedule for " + day + " was successfully created.\n");
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
