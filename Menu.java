package net.alexanderdev.csvparsing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
import java.awt.Component;
import javax.swing.JSeparator;


public class Menu extends JFrame implements ActionListener
{	
	private static final long serialVersionUID = 1L;
	private Shift[][] myArray;
	private int rows, 
				cols;
	private ArrayList<String> truncDates;
	
	//Delegate menu object
	public Menu menu;
	private JTabbedPane tabbedPane;
	private JTextField path;
	private JLabel lblCbxChooser;
	private JCheckBox cbxFrontLine;
	private JCheckBox cbxCashier;
	private JLabel lblChoice;
	private JButton btnSunday;
	private JButton btnMonday;
	private JButton btnTuesday;
	private JButton btnWednesday;
	private JButton btnThursday;
	private JButton btnFriday;
	private JButton btnSaturday;
	private JButton btnAll;
	private JButton btnOpenFile;
	private  JButton btnReset;
	private JButton btnInstructions;
	private JButton btnSearch;
	
	private JScrollPane scrollPane;
	
	private JTextArea logArea;
	private JTextField txtSearch;
	private JLabel lblFound;
	private JLabel lblSunday;
	private JLabel lblMonday;
	private JLabel lblTuesday;
	private JLabel lblWednesday;
	private JLabel lblThursday;
	private JLabel lblFriday;
	private JLabel lblSaturday;
	private JLabel lblSearch;
	private JTextField txtSunday;
	private JTextField txtMonday;
	private JTextField txtTuesday;
	private JTextField txtWednesday;
	private JTextField txtFriday;
	private JTextField txtSaturday;
	private JTextField txtThursday;
	private JLabel lblEmployee;
	private JTextField txtEmployee;
	
	public Menu()
	{
		//Set the title of the JFrame
		super("BJ's Wholesale Scheduler (Version 4.0.0)");
		getContentPane().setFont(new Font("Consolas", Font.PLAIN, 13));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 676, 685);
		getContentPane().setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Consolas", Font.PLAIN, 13));
		tabbedPane.setBounds(0, 0, 669, 650);
		
		//Create tab
		JComponent panel1 = makeTextPanel("panel1");
		tabbedPane.add("Create", panel1);		
		
		//Search tab
		JComponent panel2 = makeTextPanel("panel2");
		tabbedPane.add("Search", panel2);		
		
		getContentPane().add(tabbedPane);		
		
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
				lblSearch.setText("");
				resetDays();
				resetSearchFields();
				toggleCheckboxState(false);				
				toggleCheckboxes(false);
				toggleButtons(false);
				toggleSearch(false);
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
		
		btnSearch.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{			
				resetDays();
				
				if(!txtSearch.getText().equals("") && !txtSearch.getText().equals(null))
				{
					if(isEmployee(myArray, rows, cols, txtSearch.getText()))
					{
						txtSunday.setText(getEmployeeShift(myArray, rows, cols, lblSunday.getText(), txtEmployee.getText()));
						txtMonday.setText(getEmployeeShift(myArray, rows, cols, lblMonday.getText(), txtEmployee.getText()));
						txtTuesday.setText(getEmployeeShift(myArray, rows, cols, lblTuesday.getText(), txtEmployee.getText()));
						txtWednesday.setText(getEmployeeShift(myArray, rows, cols, lblWednesday.getText(), txtEmployee.getText()));
						txtThursday.setText(getEmployeeShift(myArray, rows, cols, lblThursday.getText(), txtEmployee.getText()));
						txtFriday.setText(getEmployeeShift(myArray, rows, cols, lblFriday.getText(), txtEmployee.getText()));
						txtSaturday.setText(getEmployeeShift(myArray, rows, cols, lblSaturday.getText(), txtEmployee.getText()));
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Employee not found!", "ERROR", JOptionPane.ERROR_MESSAGE);
						resetDays();
						resetSearchFields();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Search string empty!", "ERROR", JOptionPane.ERROR_MESSAGE);
					resetDays();
					resetSearchFields();
				}
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
	
	protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        panel.setLayout(null);
        
        lblSearch = new JLabel("Enter employee's name (Capitalize first letter of names):");
        lblSearch.setFont(new Font("Consolas", Font.PLAIN, 16));
        lblSearch.setBounds(12, 13, 640, 16);
        panel.add(lblSearch);
        
        btnSearch = new JButton("Search");
        btnSearch.setEnabled(false);
        btnSearch.setFont(new Font("Consolas", Font.PLAIN, 13));
        btnSearch.setBounds(22, 42, 97, 25);
        panel.add(btnSearch);
        
        txtSearch = new JTextField();
        txtSearch.setEnabled(false);
        txtSearch.setFont(new Font("Consolas", Font.PLAIN, 16));
        txtSearch.setBounds(130, 45, 392, 22);
        panel.add(txtSearch);
        txtSearch.setColumns(10);
        
        lblFound = new JLabel("Employee's schedule:");
        lblFound.setFont(new Font("Consolas", Font.PLAIN, 16));
        lblFound.setBounds(12, 80, 622, 16);
        panel.add(lblFound);
        
        lblSunday = new JLabel("Sunday");
        lblSunday.setFont(new Font("Consolas", Font.PLAIN, 16));
        lblSunday.setBounds(34, 144, 85, 16);
        panel.add(lblSunday);
        
        lblMonday = new JLabel("Monday");
        lblMonday.setFont(new Font("Consolas", Font.PLAIN, 16));
        lblMonday.setBounds(34, 173, 85, 16);
        panel.add(lblMonday);
        
        lblTuesday = new JLabel("Tuesday");
        lblTuesday.setFont(new Font("Consolas", Font.PLAIN, 16));
        lblTuesday.setBounds(34, 202, 85, 16);
        panel.add(lblTuesday);
        
        lblWednesday = new JLabel("Wednesday");
        lblWednesday.setFont(new Font("Consolas", Font.PLAIN, 16));
        lblWednesday.setBounds(34, 231, 85, 16);
        panel.add(lblWednesday);
        
        lblThursday = new JLabel("Thursday");
        lblThursday.setFont(new Font("Consolas", Font.PLAIN, 16));
        lblThursday.setBounds(34, 260, 85, 16);
        panel.add(lblThursday);
        
        lblFriday = new JLabel("Friday");
        lblFriday.setFont(new Font("Consolas", Font.PLAIN, 16));
        lblFriday.setBounds(34, 289, 85, 16);
        panel.add(lblFriday);
        
        lblSaturday = new JLabel("Saturday");
        lblSaturday.setFont(new Font("Consolas", Font.PLAIN, 16));
        lblSaturday.setBounds(34, 318, 85, 16);
        panel.add(lblSaturday);
        
        txtSunday = new JTextField();
        txtSunday.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtSunday.setEditable(false);
        txtSunday.setBounds(130, 140, 392, 22);
        panel.add(txtSunday);
        txtSunday.setColumns(10);
        
        txtMonday = new JTextField();
        txtMonday.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtMonday.setEditable(false);
        txtMonday.setColumns(10);
        txtMonday.setBounds(130, 169, 392, 22);
        panel.add(txtMonday);
        
        txtTuesday = new JTextField();
        txtTuesday.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtTuesday.setEditable(false);
        txtTuesday.setColumns(10);
        txtTuesday.setBounds(130, 196, 392, 22);
        panel.add(txtTuesday);
        
        txtWednesday = new JTextField();
        txtWednesday.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtWednesday.setEditable(false);
        txtWednesday.setColumns(10);
        txtWednesday.setBounds(130, 225, 392, 22);
        panel.add(txtWednesday);
        
        txtFriday = new JTextField();
        txtFriday.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtFriday.setEditable(false);
        txtFriday.setColumns(10);
        txtFriday.setBounds(130, 283, 392, 22);
        panel.add(txtFriday);
        
        txtSaturday = new JTextField();
        txtSaturday.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtSaturday.setEditable(false);
        txtSaturday.setColumns(10);
        txtSaturday.setBounds(130, 315, 392, 24);
        panel.add(txtSaturday);
        
        txtThursday = new JTextField();
        txtThursday.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtThursday.setEditable(false);
        txtThursday.setColumns(10);
        txtThursday.setBounds(130, 256, 392, 22);
        panel.add(txtThursday);
        
        lblEmployee = new JLabel("Employee");
        lblEmployee.setFont(new Font("Consolas", Font.PLAIN, 16));
        lblEmployee.setBounds(34, 115, 85, 16);
        panel.add(lblEmployee);
        
        txtEmployee = new JTextField();
        txtEmployee.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtEmployee.setEditable(false);
        txtEmployee.setColumns(10);
        txtEmployee.setBounds(130, 109, 392, 22);
        panel.add(txtEmployee);
        
        if(text == "panel1")
        {
            btnOpenFile = new JButton("Open File");
            btnOpenFile.setBounds(12, 13, 97, 25);
            panel.add(btnOpenFile);
            
            path = new JTextField();
            path.setEnabled(false);
            path.setEditable(false);
            path.setBounds(121, 14, 520, 22);
            panel.add(path);
            path.setColumns(10);
            
            lblCbxChooser = new JLabel("Select schedule(s) to create:");
            lblCbxChooser.setFont(new Font("Consolas", Font.PLAIN, 16));
            lblCbxChooser.setBounds(12, 51, 266, 16);
            panel.add(lblCbxChooser);
            
            cbxFrontLine = new JCheckBox("Front Line Schedule");
            cbxFrontLine.setEnabled(false);
            cbxFrontLine.setFont(new Font("Consolas", Font.PLAIN, 13));
            cbxFrontLine.setBounds(22, 76, 168, 25);
            panel.add(cbxFrontLine);
            
            cbxCashier = new JCheckBox("Cashier Schedule");
            cbxCashier.setEnabled(false);
            cbxCashier.setFont(new Font("Consolas", Font.PLAIN, 13));
            cbxCashier.setBounds(22, 106, 164, 25);
            panel.add(cbxCashier);
            
            lblChoice = new JLabel("Choose day(s) to process:");
            lblChoice.setFont(new Font("Consolas", Font.PLAIN, 16));
            lblChoice.setBounds(12, 140, 266, 16);
            panel.add(lblChoice);
            
            btnSunday = new JButton("Sunday");
            btnSunday.setEnabled(false);
            btnSunday.setFont(new Font("Consolas", Font.PLAIN, 13));
            btnSunday.setBounds(22, 169, 256, 25);
            panel.add(btnSunday);
            
            btnMonday = new JButton("Monday");
            btnMonday.setEnabled(false);
            btnMonday.setFont(new Font("Consolas", Font.PLAIN, 13));
            btnMonday.setBounds(22, 207, 256, 25);
            panel.add(btnMonday);
            
            btnTuesday = new JButton("Tuesday");
            btnTuesday.setEnabled(false);
            btnTuesday.setFont(new Font("Consolas", Font.PLAIN, 13));
            btnTuesday.setBounds(22, 245, 256, 25);
            panel.add(btnTuesday);
            
            btnWednesday = new JButton("Wednesday");
            btnWednesday.setEnabled(false);
            btnWednesday.setFont(new Font("Consolas", Font.PLAIN, 13));
            btnWednesday.setBounds(22, 283, 256, 25);
            panel.add(btnWednesday);
            
            btnThursday = new JButton("Thursday");
            btnThursday.setEnabled(false);
            btnThursday.setFont(new Font("Consolas", Font.PLAIN, 13));
            btnThursday.setBounds(22, 321, 256, 25);
            panel.add(btnThursday);
            
            btnFriday = new JButton("Friday");
            btnFriday.setEnabled(false);
            btnFriday.setFont(new Font("Consolas", Font.PLAIN, 13));
            btnFriday.setBounds(22, 359, 256, 25);
            panel.add(btnFriday);
            
            btnSaturday = new JButton("Saturday");
            btnSaturday.setEnabled(false);
            btnSaturday.setFont(new Font("Consolas", Font.PLAIN, 13));
            btnSaturday.setBounds(22, 397, 256, 25);
            panel.add(btnSaturday);
            
            btnAll = new JButton("All");
            btnAll.setEnabled(false);
            btnAll.setFont(new Font("Consolas", Font.PLAIN, 13));
            btnAll.setBounds(22, 435, 256, 25);
            panel.add(btnAll);
            
            scrollPane = new JScrollPane();
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBounds(304, 51, 337, 409);        
            panel.add(scrollPane);
            
            logArea = new JTextArea();
            logArea.setEditable(false);
            logArea.setFont(new Font("Consolas", Font.PLAIN, 16));
            DefaultCaret caret = (DefaultCaret)logArea.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            scrollPane.setViewportView(logArea);
            
            btnReset = new JButton("Reset");
            btnReset.setEnabled(false);
            btnReset.setFont(new Font("Consolas", Font.PLAIN, 16));
            btnReset.setBounds(22, 488, 256, 45);
            panel.add(btnReset);
            
            JSeparator separator = new JSeparator();
            separator.setBounds(12, 473, 629, 2);
            panel.add(separator);
            
            btnInstructions = new JButton("Instructions");
            btnInstructions.setFont(new Font("Consolas", Font.PLAIN, 16));
            btnInstructions.setBounds(22, 546, 256, 45);
            panel.add(btnInstructions);
            
            JButton btnCopyright = new JButton("Created By: Brett Allen");
            btnCopyright.setFont(new Font("Consolas", Font.PLAIN, 16));
            btnCopyright.setEnabled(false);
            btnCopyright.setBounds(304, 488, 337, 103);
            panel.add(btnCopyright);
        }
        
        if(text == "panel2")
        {
        	/*lblSearch = new JLabel("Enter employee's name (Capitalize first letter of names):");
            lblSearch.setFont(new Font("Consolas", Font.PLAIN, 16));
            lblSearch.setBounds(12, 13, 640, 16);
            panel.add(lblSearch);
            
            btnSearch = new JButton("Search");
            btnSearch.setEnabled(false);
            btnSearch.setFont(new Font("Consolas", Font.PLAIN, 13));
            btnSearch.setBounds(22, 42, 97, 25);
            panel.add(btnSearch);
            
            txtSearch = new JTextField();
            txtSearch.setEnabled(false);
            txtSearch.setFont(new Font("Consolas", Font.PLAIN, 16));
            txtSearch.setBounds(130, 45, 249, 22);
            panel.add(txtSearch);
            txtSearch.setColumns(10);
            
            lblFound = new JLabel("Employee's schedule:");
            lblFound.setFont(new Font("Consolas", Font.PLAIN, 16));
            lblFound.setBounds(12, 80, 622, 16);
            panel.add(lblFound);
            
            lblSunday = new JLabel("Sunday");
            lblSunday.setFont(new Font("Consolas", Font.PLAIN, 16));
            lblSunday.setBounds(34, 144, 85, 16);
            panel.add(lblSunday);
            
            lblMonday = new JLabel("Monday");
            lblMonday.setFont(new Font("Consolas", Font.PLAIN, 16));
            lblMonday.setBounds(34, 173, 85, 16);
            panel.add(lblMonday);
            
            lblTuesday = new JLabel("Tuesday");
            lblTuesday.setFont(new Font("Consolas", Font.PLAIN, 16));
            lblTuesday.setBounds(34, 202, 85, 16);
            panel.add(lblTuesday);
            
            lblWednesday = new JLabel("Wednesday");
            lblWednesday.setFont(new Font("Consolas", Font.PLAIN, 16));
            lblWednesday.setBounds(34, 231, 85, 16);
            panel.add(lblWednesday);
            
            lblThursday = new JLabel("Thursday");
            lblThursday.setFont(new Font("Consolas", Font.PLAIN, 16));
            lblThursday.setBounds(34, 260, 85, 16);
            panel.add(lblThursday);
            
            lblFriday = new JLabel("Friday");
            lblFriday.setFont(new Font("Consolas", Font.PLAIN, 16));
            lblFriday.setBounds(34, 289, 85, 16);
            panel.add(lblFriday);
            
            lblSaturday = new JLabel("Saturday");
            lblSaturday.setFont(new Font("Consolas", Font.PLAIN, 16));
            lblSaturday.setBounds(34, 318, 85, 16);
            panel.add(lblSaturday);
            
            txtSunday = new JTextField();
            txtSunday.setFont(new Font("Consolas", Font.PLAIN, 13));
            txtSunday.setEditable(false);
            txtSunday.setBounds(130, 140, 249, 22);
            panel.add(txtSunday);
            txtSunday.setColumns(10);
            
            txtMonday = new JTextField();
            txtMonday.setFont(new Font("Consolas", Font.PLAIN, 13));
            txtMonday.setEditable(false);
            txtMonday.setColumns(10);
            txtMonday.setBounds(130, 169, 249, 22);
            panel.add(txtMonday);
            
            txtTuesday = new JTextField();
            txtTuesday.setFont(new Font("Consolas", Font.PLAIN, 13));
            txtTuesday.setEditable(false);
            txtTuesday.setColumns(10);
            txtTuesday.setBounds(130, 196, 249, 22);
            panel.add(txtTuesday);
            
            txtWednesday = new JTextField();
            txtWednesday.setFont(new Font("Consolas", Font.PLAIN, 13));
            txtWednesday.setEditable(false);
            txtWednesday.setColumns(10);
            txtWednesday.setBounds(130, 225, 249, 22);
            panel.add(txtWednesday);
            
            txtFriday = new JTextField();
            txtFriday.setFont(new Font("Consolas", Font.PLAIN, 13));
            txtFriday.setEditable(false);
            txtFriday.setColumns(10);
            txtFriday.setBounds(130, 283, 249, 22);
            panel.add(txtFriday);
            
            txtSaturday = new JTextField();
            txtSaturday.setFont(new Font("Consolas", Font.PLAIN, 13));
            txtSaturday.setEditable(false);
            txtSaturday.setColumns(10);
            txtSaturday.setBounds(130, 312, 249, 22);
            panel.add(txtSaturday);
            
            txtThursday = new JTextField();
            txtThursday.setFont(new Font("Consolas", Font.PLAIN, 13));
            txtThursday.setEditable(false);
            txtThursday.setColumns(10);
            txtThursday.setBounds(130, 256, 249, 22);
            panel.add(txtThursday);
            
            lblEmployee = new JLabel("Employee");
            lblEmployee.setFont(new Font("Consolas", Font.PLAIN, 16));
            lblEmployee.setBounds(34, 115, 85, 16);
            panel.add(lblEmployee);
            
            txtEmployee = new JTextField();
            txtEmployee.setFont(new Font("Consolas", Font.PLAIN, 13));
            txtEmployee.setEditable(false);
            txtEmployee.setColumns(10);
            txtEmployee.setBounds(130, 109, 249, 22);
            panel.add(txtEmployee);*/
        }
        
        return panel;
    }
	
	private boolean isEmployee(Shift[][] myArray, int rows, int cols, String name)
	{	
		for(int x = 0; x < rows; x++)
		{
			for(int y = 0; y < cols; y++)
			{	
				if(myArray[x][y] != null)
				{
					if(myArray[x][y].getFullName().toLowerCase().contains(name.toLowerCase()) 
							|| myArray[x][y].getName().toLowerCase().contains(name.toLowerCase()))
					{
						txtEmployee.setText(myArray[x][y].getFullName());
						return true;
					}
				}
			}
		}		
		return false;
	}
	
	private String getEmployeeShift(Shift[][] myArray, int rows, int cols, String day, String name)
	{	
		String shift = "";
		
		for(int x = 0; x < rows; x++)
		{
			for(int y = 0; y < cols; y++)
			{
				if(myArray[x][y] != null && myArray[x][y].day.contains(day))
				{
					if(myArray[x][y].getFullName().contains(name) || myArray[x][y].getName().contains(name))
					{
						shift += myArray[x][y].getShiftTime() + " " + myArray[x][y].position + "; ";
					}
				}
			}
		}
		
		if(shift == "")
			return "Off";
		return shift;
	}
	
	private void resetDays()
	{		
		txtSunday.setText("");
		txtMonday.setText("");
		txtTuesday.setText("");
		txtWednesday.setText("");
		txtThursday.setText("");
		txtFriday.setText("");
		txtSaturday.setText("");
	}
	
	private void resetSearchFields()
	{
		txtSearch.setText("");
		txtEmployee.setText("");
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
			
			toggleSearch(true);
			toggleCheckboxes(true);
			btnReset.setEnabled(true);
			
			//Turn off wait cursor
			setCursor(null);
		}	
		else if(path.getText().equals(""))
		{
			toggleButtons(false);
			toggleSearch(false);
			toggleCheckboxState(false);
			toggleCheckboxes(false);
			resetDays();
			resetSearchFields();
			JOptionPane.showMessageDialog(null, "No file chosen.", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			toggleButtons(false);
			toggleSearch(false);
			toggleCheckboxState(false);
			toggleCheckboxes(false);
			resetDays();
			resetSearchFields();
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
	
	private void toggleSearch(boolean b)
	{
		btnSearch.setEnabled(b);
		txtSearch.setEnabled(b);
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
