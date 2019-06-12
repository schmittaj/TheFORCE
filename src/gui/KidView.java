/*
 *  The FORCE (Filled Out Reading Cards Entered) is a database front end
 *  for a SQLite database for tracking and doing statistics on participants in the summer
 *  reading program that is run by the Sun Prairie Public Library.
 *  Copyright (C) 2019  Anthony Schmitt (schmittaj@gmail.com)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *  
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.util.*;
import backend.*;

/**
 * Show detailed information about reader.
 * @author Anthony Schmitt
 *
 */
public class KidView extends JFrame implements ActionListener, DatabaseChangeListener, KeyListener, MouseListener
{
	private DBHandler dbFriend;
	private boolean editState;
	private int width;
	private int height;
	private String currentID, mergeID;
	private JButton register, checkIn, edit, done, cancel, addYear, adminCancel, adminDone, mergeButton, splitButton;
	private JPanel normalButtons, editButtons, editAdminButtons, mergeButtons, splitButtons;
	private JComboBox<String> dobMonth, dobYear, dobDay;
	private String[] monthList = {"<select>","1 - January","2 - February","3 - March","4 - April","5 - May","6 - June","7 - July","8 - August","9 - September","10 - October","11 - November","12 - December"};
	private String[] dayList = {"<select>","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	private String[] yearList = new String[Constants.GENERAL_YEAR_RANGE+1];
	private int[][] valueIndexes;
	private JTextField fName, lName, email, parent, city, school;
	private JFormattedTextField phoneNum;
	private String fNameOrig, lNameOrig, dobOrig, emailOrig, phoneNumOrig, parentOrig, yearsExisting;
	private String emptyPhoneNum = "   -   -    ";
	private DatabaseChangeListenerImplementer dbcli;
	private RegistrationWindow regWin;
	private CheckInWindow cinWin;
	private SecretMenuToggle mainWindow;
	private String[][] schools, cities, grades, programs;
	private Hashtable<String,String> schoolsIDtoName, schoolsNameToID, citiesIDtoName, citiesNameToID, gradesIDtoName, gradesNameToID, programsIDtoName, programsNameToID;
	private ArrayList<ArrayList<JComboBox<String>>> comboBoxes;
	private ArrayList<int[]> comboBoxesOrigIndex;
	private ArrayList<ArrayList<JTextField>> textFields;
	private ArrayList<String[]> fieldsOrigValues;
	private ArrayList<JButton> ecButtons;
	private JPanel kidInfo, mainBodyPanel, table;
	private ReaderMergeWindow rmw;
	private ReaderSplitWindow rsw;
	
	/**
	 * Constructor
	 * @param dbh Database connection
	 * @param dbc Database update listener
	 * @param mainWin Main window
	 */
	public KidView(DBHandler dbh, DatabaseChangeListenerImplementer dbc, SecretMenuToggle mainWin)
	{
		this.dbFriend = dbh;
		this.dbcli = dbc;
		this.height = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()/3)*1.5);
		this.width = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()/5)*4);
		this.setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/6, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/6);
		init();
		this.setTitle("View Reader");
		this.mainWindow = mainWin;
		regWin = new RegistrationWindow(dbFriend,"1",dbcli,mainWindow);
		cinWin = new CheckInWindow(dbFriend,"1",dbcli,mainWindow);
		rmw = new ReaderMergeWindow(mainWindow,dbFriend,dbcli);
		rsw = new ReaderSplitWindow(mainWindow, dbFriend, dbcli);
		
		this.addKeyListener(this);
		this.setFocusable(true);
		UIManager.put("ComboBox.disabledForeground", Color.GRAY);
	}
	
	/**
	 * Initializes window
	 */
	protected void init()
	{
		this.mergeID = "";
		this.schoolsIDtoName = new Hashtable<String,String>();
		this.schoolsNameToID = new Hashtable<String,String>();
		this.citiesIDtoName = new Hashtable<String,String>();
		this.citiesNameToID = new Hashtable<String,String>();
		this.gradesIDtoName = new Hashtable<String,String>();
		this.gradesNameToID = new Hashtable<String,String>();
		this.programsIDtoName = new Hashtable<String,String>();
		this.programsNameToID = new Hashtable<String,String>();
		
		this.schools = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS_ALPHA, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		String[][] none = {{"-1","None"}};
		for(int a = 0; a < schools.length; a++)
		{
			schoolsIDtoName.put(schools[a][0], schools[a][1]);
			schoolsNameToID.put(schools[a][1], schools[a][0]);
		}
		schoolsIDtoName.put("-1","None");
		schoolsNameToID.put("None","-1");
		this.cities = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS_ALPHA, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		for(int a = 0; a < cities.length; a++)
		{
			citiesIDtoName.put(cities[a][0], cities[a][1]);
			citiesNameToID.put(cities[a][1], cities[a][0]);
		}
		schools = Constants.addCommonEntities(Constants.COMMON_SCHOOLS, schools);
		cities = Constants.addCommonEntities(Constants.COMMON_CITIES, cities);
		cities = Constants.addCommonEntities(none, cities);
		citiesIDtoName.put("-1", "None");
		citiesNameToID.put("None", "-1");
		this.grades = dbFriend.query2DstringRet(Queries.ALL_GRADES_PL_IDS, Queries.ALL_GRADES_PL_IDS_COL_LEN);
		grades = Constants.addCommonEntities(none, grades);
		for(int a = 0; a < grades.length; a++)
		{
			gradesIDtoName.put(grades[a][0], grades[a][1]);
			gradesNameToID.put(grades[a][1], grades[a][0]);
		}
		this.programs = dbFriend.query2DstringRet(Queries.ALL_PROGRAMS_PL_IDS, Queries.ALL_PROGRAMS_PL_IDS_COL_LEN);
		for(int a = 0; a < programs.length; a++)
		{
			programsIDtoName.put(programs[a][0], programs[a][1]);
			programsNameToID.put(programs[a][1], programs[a][0]);
		}
		
		this.yearsExisting = "";
		
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(width,height));
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		setUpButtons();
		
		Date d = new Date(System.currentTimeMillis());
		int year = d.getYear()+1900;
		yearList[0] = "<select>";
		for(int a = 0; a < Constants.GENERAL_YEAR_RANGE-1; a++)
		{
			yearList[a+1] = "" + (year-a); 
		}
		
		editState = false;
		fName = new JTextField();
		lName = new JTextField();
		
		//dob = new JTextField();
		this.dobMonth = new JComboBox<String>(monthList);
		this.dobDay = new JComboBox<String>(dayList);
		this.dobYear = new JComboBox<String>(yearList);
		JPanel dobInsidePanel = new JPanel();
		dobInsidePanel.setLayout(new GridLayout(1,3));
		dobInsidePanel.add(dobMonth);
		dobInsidePanel.add(dobDay);
		dobInsidePanel.add(dobYear);
		
		email = new JTextField();
		parent = new JTextField();
		school = new JTextField();
		school.setEditable(false);
		city = new JTextField();
		city.setEditable(false);
		
		try 
		{
			phoneNum = new JFormattedTextField(new MaskFormatter("###-###-####"));
			phoneNum.setCaretPosition(0);
			phoneNum.addMouseListener(this);
			phoneNum.setEditable(false);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		
		
		
		disableInputs();
		
		JPanel topPart = new JPanel();
		topPart.setLayout(new GridLayout(1,3));
		topPart.setBorder(new EmptyBorder(5,0,5,0));
		JPanel topPart1 = new JPanel();
		JLabel fNameLbl = new JLabel("First Name");
		fNameLbl.setHorizontalAlignment(JLabel.CENTER);
		topPart1.setLayout(new BorderLayout());
		topPart1.add(fNameLbl, BorderLayout.NORTH);
		topPart1.add(fName, BorderLayout.CENTER);
		topPart1.setBorder(new EmptyBorder(0,0,0,5));
		topPart.add(topPart1);
		JPanel topPart2 = new JPanel();
		JLabel lNameLbl = new JLabel("Last Name");
		lNameLbl.setHorizontalAlignment(JLabel.CENTER);
		topPart2.setLayout(new BorderLayout());
		topPart2.add(lNameLbl, BorderLayout.NORTH);
		topPart2.add(lName, BorderLayout.CENTER);
		topPart2.setBorder(new EmptyBorder(0,0,0,5));
		topPart.add(topPart2);
		JPanel topPart3 = new JPanel();
		topPart3.setLayout(new BorderLayout());
		JLabel dobLbl = new JLabel("Date of Birth");
		dobLbl.setHorizontalAlignment(JLabel.CENTER);
		topPart3.add(dobLbl, BorderLayout.NORTH);
		topPart3.add(dobInsidePanel, BorderLayout.CENTER);
		topPart.add(topPart3);		
		
		JPanel middlePart = new JPanel();
		middlePart.setLayout(new GridLayout(1,3));
		middlePart.setBorder(new EmptyBorder(5,0,5,0));
		JPanel middlePart1 = new JPanel();
		middlePart1.setLayout(new BorderLayout());
		JLabel phoneLbl = new JLabel("Phone Number");
		phoneLbl.setHorizontalAlignment(JLabel.CENTER);
		middlePart1.add(phoneLbl, BorderLayout.NORTH);
		middlePart1.add(phoneNum, BorderLayout.CENTER);
		middlePart1.setBorder(new EmptyBorder(0,0,0,5));
		middlePart.add(middlePart1);
		JPanel middlePart2 = new JPanel();
		middlePart2.setLayout(new BorderLayout());
		JLabel emailLbl = new JLabel("Email");
		emailLbl.setHorizontalAlignment(JLabel.CENTER);
		middlePart2.add(emailLbl, BorderLayout.NORTH);
		middlePart2.add(email, BorderLayout.CENTER);
		middlePart2.setBorder(new EmptyBorder(0,0,0,5));
		middlePart.add(middlePart2);
		JPanel middlePart3 = new JPanel();
		middlePart3.setLayout(new BorderLayout());
		JLabel parentLbl = new JLabel("Parent");
		parentLbl.setHorizontalAlignment(JLabel.CENTER);
		middlePart3.add(parentLbl, BorderLayout.NORTH);
		middlePart3.add(parent, BorderLayout.CENTER);
		middlePart.add(middlePart3);
		
		JPanel bottomPart = new JPanel();
		bottomPart.setLayout(new GridLayout(1,2));
		bottomPart.setBorder(new EmptyBorder(5,0,5,0));
		JPanel bottomPart1 = new JPanel();
		bottomPart1.setLayout(new BorderLayout());
		JLabel schoolLbl = new JLabel("School");
		schoolLbl.setHorizontalAlignment(JLabel.CENTER);
		bottomPart1.add(schoolLbl, BorderLayout.NORTH);
		bottomPart1.add(school, BorderLayout.CENTER);
		bottomPart1.setBorder(new EmptyBorder(0,0,0,5));
		bottomPart.add(bottomPart1);
		JPanel bottomPart2 = new JPanel();
		bottomPart2.setLayout(new BorderLayout());
		JLabel cityLbl = new JLabel("City");
		cityLbl.setHorizontalAlignment(JLabel.CENTER);
		bottomPart2.add(cityLbl, BorderLayout.NORTH);
		bottomPart2.add(city, BorderLayout.CENTER);
		bottomPart.add(bottomPart2);
		
		kidInfo = new JPanel();
		kidInfo.setLayout(new GridLayout(3,1));
		kidInfo.add(topPart);
		kidInfo.add(middlePart);
		kidInfo.add(bottomPart);
		
		
		//tablePane = new JScrollPane();
		
		table = new JPanel();
		
		mainBodyPanel = new JPanel();
		mainBodyPanel.setLayout(new GridLayout(2,1));
		mainBodyPanel.setBorder(new EmptyBorder(10,0,0,0));
		mainBodyPanel.add(kidInfo);
		//mainBodyPanel.add(tablePane);
		mainBodyPanel.add(table);
		this.add(mainBodyPanel,BorderLayout.CENTER);
	}
	
	/**
	 * Sets up the buttons
	 */
	protected void setUpButtons()
	{
		register = new JButton("Register");
		register.addActionListener(this);
		checkIn = new JButton("Check In");
		checkIn.addActionListener(this);
		edit = new JButton("Edit");
		edit.addActionListener(this);
		done = new JButton("Done");
		done.addActionListener(this);
		adminDone = new JButton("Done");
		adminDone.addActionListener(this);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		adminCancel = new JButton("Cancel");
		adminCancel.addActionListener(this);
		addYear = new JButton("Add Missing Year");
		addYear.addActionListener(this);
		mergeButton = new JButton("Merge This Reader");
		mergeButton.addActionListener(this);
		splitButton = new JButton("Split This Reader");
		splitButton.addActionListener(this);
		
		normalButtons = new JPanel();
		normalButtons.setLayout(new GridLayout(1,3));
		normalButtons.add(register);
		normalButtons.add(checkIn);
		normalButtons.add(edit);
		this.add(normalButtons, BorderLayout.SOUTH);
		
		editButtons = new JPanel();
		editButtons.setLayout(new GridLayout(1,2));
		editButtons.add(done);
		editButtons.add(cancel);
		
		editAdminButtons = new JPanel();
		editAdminButtons.setLayout(new GridLayout(1,3));
		editAdminButtons.add(addYear);
		editAdminButtons.add(adminDone);
		editAdminButtons.add(adminCancel);
		
		mergeButtons = new JPanel();
		mergeButtons.setLayout(new GridLayout(1,1));
		mergeButtons.add(mergeButton);
		
		splitButtons = new JPanel();
		splitButtons.setLayout(new GridLayout(1,1));
		splitButtons.add(splitButton);
	}

	/**
	 * Sets the info in the window to specified reader's info
	 * @param id Reader's id
	 */
	protected void setReader(String id)
	{
		this.currentID = id;
		String[] curKidInfo = dbFriend.query1DstringRet("SELECT first_name, last_name, date_of_birth, most_recent_phone, most_recent_email, most_recent_parent, most_recent_school, most_recent_city FROM Children WHERE id = " + currentID + ";", 8);
		String[][] pgrmInfo = dbFriend.query2DstringRet("SELECT * FROM Program_Data WHERE child_id = " + currentID + ";", 19);
		
		//first_name, last_name, date_of_birth, most_recent_phone, most_recent_email, most_recent_parent, most_recent_school, most_recent_city
		if(curKidInfo != null)
		{
			fName.setText(curKidInfo[0]);
			lName.setText(curKidInfo[1]);
			if(!curKidInfo[2].trim().equals(""))
			{
				String[] dobValHolder = curKidInfo[2].split("/");
				dobMonth.setSelectedIndex(Integer.parseInt(dobValHolder[0]));
				dobDay.setSelectedIndex(Integer.parseInt(dobValHolder[1]));
				dobYear.setSelectedItem(dobValHolder[2]);
			}
			else
			{
				dobMonth.setSelectedIndex(0);
				dobDay.setSelectedIndex(0);
				dobYear.setSelectedIndex(0);
			}
			phoneNum.setText(curKidInfo[3]);
			email.setText(curKidInfo[4]);
			parent.setText(curKidInfo[5]);
			school.setText(schoolsIDtoName.get(curKidInfo[6]));
			city.setText(citiesIDtoName.get(curKidInfo[7]));
	
			
			this.remove(mainBodyPanel);
			this.remove(normalButtons);
			this.setLayout(new BorderLayout());
			//tablePane = new JScrollPane(makeTable(pgrmInfo,curKidInfo[2]));
			table = makeTable(pgrmInfo,curKidInfo[2]);
			
			mainBodyPanel = new JPanel();
			mainBodyPanel.setLayout(new GridLayout(2,1));
			mainBodyPanel.setBorder(new EmptyBorder(10,0,0,0));
			mainBodyPanel.add(kidInfo);
			//mainBodyPanel.add(tablePane);
			mainBodyPanel.add(table);
			this.add(mainBodyPanel,BorderLayout.CENTER);
			this.add(normalButtons, BorderLayout.SOUTH);
			
			this.paintAll(this.getGraphics());	
			
			fNameOrig = fName.getText();
			lNameOrig = lName.getText();
			//dobOrig = dob.getText();
			String dobStr = "";
			if(dobMonth.getSelectedIndex() != 0 && dobDay.getSelectedIndex() != 0 && dobYear.getSelectedIndex() != 0)
			{
				dobStr += dobMonth.getSelectedIndex() + "/";
				dobStr += dobDay.getSelectedItem() + "/";
				dobStr += dobYear.getSelectedItem();
			}
			dobOrig = dobStr;
			emailOrig = email.getText();
			phoneNumOrig = phoneNum.getText();
			parentOrig = parent.getText();
		}
	}
	
	/**
	 * Handles actions
	 */
	public void actionPerformed(ActionEvent e) 
	{
		for(int a = 0; a < ecButtons.size(); a++) // extra challange buttons
		{
			if(ecButtons.get(a).equals(e.getSource()))
			{
				String newVal = ecButtons.get(a).getText();
				JCheckBox ec1 = new JCheckBox("Extra Challenge Box 1");
				JCheckBox ec2 = new JCheckBox("Extra Challenge Box 2");
				JCheckBox ec3 = new JCheckBox("Extra Challenge Box 3");
				JCheckBox ec4 = new JCheckBox("Extra Challenge Box 4");
				JCheckBox ec5 = new JCheckBox("Extra Challenge Box 5");
				JCheckBox ec6 = new JCheckBox("Extra Challenge Box 6");
				JCheckBox ec7 = new JCheckBox("Extra Challenge Box 7");
				JCheckBox ec8 = new JCheckBox("Extra Challenge Box 8");
				JCheckBox ec9 = new JCheckBox("Extra Challenge Box 9");
				JCheckBox ec10 = new JCheckBox("Extra Challenge Box 10");
				if(newVal.split(" ")[0].equals("1"))
				{
					ec1.setSelected(true);
				}
				if(newVal.contains("2"))
				{
					ec2.setSelected(true);
				}
				if(newVal.contains("3"))
				{
					ec3.setSelected(true);
				}
				if(newVal.contains("4"))
				{
					ec4.setSelected(true);
				}
				if(newVal.contains("5"))
				{
					ec5.setSelected(true);
				}
				if(newVal.contains("6"))
				{
					ec6.setSelected(true);
				}
				if(newVal.contains("7"))
				{
					ec7.setSelected(true);
				}
				if(newVal.contains("8"))
				{
					ec8.setSelected(true);
				}
				if(newVal.contains("9"))
				{
					ec9.setSelected(true);
				}
				if(newVal.contains("10"))
				{
					ec10.setSelected(true);
				}
				JPanel extraPanel = new JPanel();
				extraPanel.setLayout(new GridLayout(5,2));
				extraPanel.add(ec1);
				extraPanel.add(ec6);
				extraPanel.add(ec2);
				extraPanel.add(ec7);
				extraPanel.add(ec3);
				extraPanel.add(ec8);
				extraPanel.add(ec4);
				extraPanel.add(ec9);
				extraPanel.add(ec5);
				extraPanel.add(ec10);
				int choice = JOptionPane.showConfirmDialog(null,extraPanel,"Edit Extra Boxes",JOptionPane.OK_CANCEL_OPTION);
				if(choice == JOptionPane.OK_OPTION)
				{
					String buttonShow = "";
					if(ec1.isSelected())
					{
						buttonShow += "1 ";
					}
					if(ec2.isSelected())
					{
						buttonShow += "2 ";
					}
					if(ec3.isSelected())
					{
						buttonShow += "3 ";
					}
					if(ec4.isSelected())
					{
						buttonShow += "4 ";
					}
					if(ec5.isSelected())
					{
						buttonShow += "5 ";
					}
					if(ec6.isSelected())
					{
						buttonShow += "6 ";
					}
					if(ec7.isSelected())
					{
						buttonShow += "7 ";
					}
					if(ec8.isSelected())
					{
						buttonShow += "8 ";
					}
					if(ec9.isSelected())
					{
						buttonShow += "9 ";
					}
					if(ec10.isSelected())
					{
						buttonShow += "10";
					}
					ecButtons.get(a).setText(buttonShow);
					this.paintAll(this.getGraphics());
				}
			}
		}

		if(e.getActionCommand().equals("Register"))
		{
			Date d = new Date(System.currentTimeMillis());
			int year = d.getYear()+1900;
			String[][] result = dbFriend.query2DstringRet("SELECT * FROM Program_Data WHERE child_id = " + currentID + " AND year = " + year + ";", 19);
			if(result == null)
			{
				regWin.setCurrentID(currentID);
				regWin.setVisible(true);
			}
			else
			{
				JOptionPane.showMessageDialog(null, fNameOrig + " " + lNameOrig + " is already registered for " + year + "'s program", "Reader Already Registered", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getActionCommand().equals("Check In"))
		{
			Date d = new Date(System.currentTimeMillis());
			int year = d.getYear()+1900;
			String[][] result = dbFriend.query2DstringRet("SELECT * FROM Program_Data WHERE child_id = " + currentID + " AND year = " + year + ";", 19);
			if(result != null)
			{
				cinWin.setCurrentID(currentID);
				cinWin.setVisible(true);
			}
			else
			{
				JOptionPane.showMessageDialog(null, fNameOrig + " " + lNameOrig + " is NOT yet registered for " + year + "'s program", "Reader Not Registered", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getActionCommand().equals("Edit"))
		{
			this.remove(normalButtons);
			if(Constants.adminEdit)
			{
				this.add(editAdminButtons, BorderLayout.SOUTH);
			}
			else
			{
				this.add(editButtons, BorderLayout.SOUTH);
			}
			this.paintAll(this.getGraphics());
			this.editState = true;
			enableInputs();
		}
		else if(e.getActionCommand().equals("Add Missing Year"))
		{	
			Date d = new Date(System.currentTimeMillis());
			int year = d.getYear()+1900;
			int arraySize = (year-Constants.OLDEST_YEAR_RECORD+1) - (yearsExisting.length()/5); //won't work after the year 9999
			if(!yearsExisting.contains(""+year))
			{
				arraySize -= 1;
			}
			if(arraySize > 0)
			{
				String[] years = new String[arraySize];
				int counter = 0;
				for(int a = Constants.OLDEST_YEAR_RECORD; a < year; a++)
				{
					String yrStr = ""+a;
					if(!yearsExisting.contains(yrStr))
					{
						years[counter] = yrStr;
						counter++;
					}
				}
				JComboBox<String> yearBox = new JComboBox<String>(years);
				int choice = JOptionPane.showConfirmDialog(null,yearBox,"Pick Year To Add",JOptionPane.OK_CANCEL_OPTION);
				if(choice == JOptionPane.OK_OPTION)
				{
					String yearToAdd = (String) yearBox.getSelectedItem();
					JLabel highestBoxLabel = new JLabel("Highest Box Completed:");
					highestBoxLabel.setHorizontalAlignment(JLabel.RIGHT);
					highestBoxLabel.setBorder(new EmptyBorder(0,0,0,20));
					String[] spinnerValues = {"0","1","2","3","4","5","6","7","8","9","10"};
					JComboBox<String> highestSpinner = new JComboBox<String>(spinnerValues);
					JPanel highestPanel = new JPanel();
					highestPanel.setLayout(new BorderLayout());
					highestPanel.add(highestBoxLabel, BorderLayout.NORTH);
					highestPanel.add(highestSpinner, BorderLayout.CENTER);
					JCheckBox ec1 = new JCheckBox("Extra Challenge Box 1");
					JCheckBox ec2 = new JCheckBox("Extra Challenge Box 2");
					JCheckBox ec3 = new JCheckBox("Extra Challenge Box 3");
					JCheckBox ec4 = new JCheckBox("Extra Challenge Box 4");
					JCheckBox ec5 = new JCheckBox("Extra Challenge Box 5");
					JCheckBox ec6 = new JCheckBox("Extra Challenge Box 6");
					JCheckBox ec7 = new JCheckBox("Extra Challenge Box 7");
					JCheckBox ec8 = new JCheckBox("Extra Challenge Box 8");
					JCheckBox ec9 = new JCheckBox("Extra Challenge Box 9");
					JCheckBox ec10 = new JCheckBox("Extra Challenge Box 10");
					JPanel extraPanel = new JPanel();
					extraPanel.setLayout(new GridLayout(5,2));
					extraPanel.add(ec1);
					extraPanel.add(ec6);
					extraPanel.add(ec2);
					extraPanel.add(ec7);
					extraPanel.add(ec3);
					extraPanel.add(ec8);
					extraPanel.add(ec4);
					extraPanel.add(ec9);
					extraPanel.add(ec5);
					extraPanel.add(ec10);
					extraPanel.setBorder(new EmptyBorder(10,0,0,0));
					
					String[] schoolList = new String[schools.length];
					for(int a = 0; a < schools.length; a++)
					{
						schoolList[a] = schools[a][1];
					}
					JComboBox<String> schoolBox = new JComboBox<String>(schoolList);
					JLabel schoolLabel = new JLabel("School");
					JPanel schoolPanel = new JPanel();
					schoolPanel.setLayout(new BorderLayout());
					schoolPanel.add(schoolBox, BorderLayout.CENTER);
					schoolPanel.add(schoolLabel, BorderLayout.NORTH);
					String[] gradeList = new String[grades.length];
					for(int a = 0; a < grades.length; a++)
					{
						gradeList[a] = grades[a][1];
					}
					JComboBox<String> gradeBox = new JComboBox<String>(gradeList);
					JLabel gradeLabel = new JLabel("Grade");
					JPanel gradePanel = new JPanel();
					gradePanel.setLayout(new BorderLayout());
					gradePanel.add(gradeBox, BorderLayout.CENTER);
					gradePanel.add(gradeLabel, BorderLayout.NORTH);
					String[] cityList = new String[cities.length+1];
					cityList[0] = "none";
					citiesIDtoName.put("-1","none");
					citiesNameToID.put("none","-1");
					for(int a = 0; a < cities.length; a++)
					{
						cityList[a+1] = cities[a][1];
					}
					JComboBox<String> cityBox = new JComboBox<String>(cityList);
					JLabel cityLabel = new JLabel("City");
					JPanel cityPanel = new JPanel();
					cityPanel.setLayout(new BorderLayout());
					cityPanel.add(cityBox, BorderLayout.CENTER);
					cityPanel.add(cityLabel, BorderLayout.NORTH);
					String[] programList = new String[programs.length];
					for(int a = 0; a < programList.length; a++)
					{
						programList[a] = programs[a][1];
					}
					JComboBox<String> programBox = new JComboBox<String>(programList);
					JLabel programLabel = new JLabel("Program");
					JPanel programPanel = new JPanel();
					programPanel.setLayout(new BorderLayout());
					programPanel.add(programBox, BorderLayout.CENTER);
					programPanel.add(programLabel, BorderLayout.NORTH);
					
					JPanel boxPanel = new JPanel(new GridLayout(5,1));
					boxPanel.add(highestPanel);
					boxPanel.add(cityPanel);
					boxPanel.add(schoolPanel);
					boxPanel.add(gradePanel);
					boxPanel.add(programPanel);
					
					JPanel mainPanel = new JPanel(new GridLayout(1,2));
					mainPanel.add(boxPanel);
					mainPanel.add(extraPanel);
		
					int difference = 1000;
					int closestYearIndex = -1;
					int yearCheck = Integer.parseInt(yearToAdd);
					for(int a = 0; a < valueIndexes.length; a++)
					{
						int dif = valueIndexes[a][0]-yearCheck;
						if(Math.abs(dif) < Math.abs(difference))
						{
							difference = dif;
							closestYearIndex = a;
						}
					}
					if(closestYearIndex != -1)
					{
						programBox.setSelectedItem(programsIDtoName.get(""+valueIndexes[closestYearIndex][1]));
						if(valueIndexes[closestYearIndex][2] != -1)
						{
							schoolBox.setSelectedItem(schoolsIDtoName.get(""+valueIndexes[closestYearIndex][2]));
						}
						if(valueIndexes[closestYearIndex][3] != -1)
						{
							gradeBox.setSelectedItem(gradesIDtoName.get(""+valueIndexes[closestYearIndex][3]));
						}
						if(valueIndexes[closestYearIndex][4] != -1)
						{
							cityBox.setSelectedItem(citiesIDtoName.get(""+valueIndexes[closestYearIndex][4]));
						}
					}
					choice = JOptionPane.showConfirmDialog(null,mainPanel,"Enter Information for " + yearToAdd,JOptionPane.OK_CANCEL_OPTION);
					
					if(choice == JOptionPane.OK_OPTION)
					{
						String gradeID = "null";
						String valueHolder = gradesNameToID.get(gradeBox.getSelectedItem());
						if(!valueHolder.equals("-1"))
						{
							gradeID = valueHolder;
						}
						String schoolID = "null";
						valueHolder = schoolsNameToID.get(schoolBox.getSelectedItem());
						if(!valueHolder.equals("-1"))
						{
							schoolID = valueHolder;
						}
						String cityID = "null";
						valueHolder = citiesNameToID.get(cityBox.getSelectedItem());
						if(!valueHolder.equals("-1"))
						{
							cityID = valueHolder;
						}
						String programID = programsNameToID.get(programBox.getSelectedItem());;
						String highLevel = (String) highestSpinner.getSelectedItem();
						String ex1 = "false";
						if(ec1.isSelected())
						{
							ex1 = "true";
						}
						String ex2 = "false";
						if(ec2.isSelected())
						{
							ex2 = "true";
						}
						String ex3 = "false";
						if(ec3.isSelected())
						{
							ex3 = "true";
						}
						String ex4 = "false";
						if(ec4.isSelected())
						{
							ex4 = "true";
						}
						String ex5 = "false";
						if(ec5.isSelected())
						{
							ex5 = "true";
						}
						String ex6 = "false";
						if(ec6.isSelected())
						{
							ex6 = "true";
						}
						String ex7 = "false";
						if(ec7.isSelected())
						{
							ex7 = "true";
						}
						String ex8 = "false";
						if(ec8.isSelected())
						{
							ex8 = "true";
						}
						String ex9 = "false";
						if(ec9.isSelected())
						{
							ex9 = "true";
						}
						String ex10 = "false";
						if(ec10.isSelected())
						{
							ex10 = "true";
						}
						String age = "null";
						if(dobOrig.length() > 0)
						{
							age = Constants.calcAgeForDB(dobOrig, Integer.parseInt(yearToAdd));
						}
						
						dbFriend.executeUpdate("INSERT INTO Program_Data VALUES (" + currentID + "," + yearToAdd + "," + 
						gradeID + "," + schoolID + "," + cityID + "," + programID + "," + highLevel + "," + ex1 + "," + ex2 +
						"," + ex3 + "," + ex4 + "," + ex5 + "," + ex6 + "," + ex7 + "," + ex8 + "," + ex9 + "," +
						ex10 + ",false," + age + ");");
						
						JOptionPane.showMessageDialog(null,yearToAdd + " added.","Missing Year Added",JOptionPane.INFORMATION_MESSAGE);
						
						fullUpdate();
						enableInputs();
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null,"There are no years that can be added","No years to add",JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getActionCommand().equals("Done"))
		{
			boolean changes = false;
			boolean errors = false;
			boolean DOBChange = false;
			String newDOB = null;
			String errorMessage = "";
			String verificationStatement = "Please verify that the following changes are correct:\n";
			ArrayList<String> updateQueries = new ArrayList<String>();
			String valueHolder = fName.getText();
			if(!valueHolder.equals(fNameOrig))
			{
				changes = true;
				verificationStatement += "Change First Name from: " + fNameOrig + " to " + valueHolder + "\n";
				updateQueries.add("UPDATE Children set first_name = \"" + valueHolder + "\" WHERE id = " + currentID + ";");
			}
			valueHolder = lName.getText();
			if(!valueHolder.equals(lNameOrig))
			{
				changes = true;
				verificationStatement += "Change Last Name from: " + lNameOrig + " to " + valueHolder + "\n";
				updateQueries.add("UPDATE Children set last_name = \"" + valueHolder + "\" WHERE id = " + currentID + ";");
			
			}
			//valueHolder = dob.getText();
			String dobStr = "";
			if(dobMonth.getSelectedIndex() != 0 && dobDay.getSelectedIndex() != 0 && dobYear.getSelectedIndex() != 0)	
			{
				dobStr += (dobMonth.getSelectedIndex()) + "/";
				dobStr += dobDay.getSelectedItem() + "/";
				dobStr += dobYear.getSelectedItem();
			}
			valueHolder = dobStr;
			if(!valueHolder.equals(dobOrig))
			{
				if(!Constants.validDOB(dobStr))
				{
					errors = true;
					errorMessage += "Invalid Birth Date.\n";
				}
				else
				{
					changes = true;
					DOBChange = true;
					newDOB = valueHolder;
					verificationStatement += "Change Date of Birth from: " + dobOrig + " to " + valueHolder + "\n";
					updateQueries.add("UPDATE Children set date_of_birth = \"" + valueHolder + "\" WHERE id = " + currentID + ";");
				}
			}
			valueHolder = phoneNum.getText();
			if(!valueHolder.equals(phoneNumOrig))
			{
				changes = true;
				if(valueHolder.equals(emptyPhoneNum)) //phone number removed
				{
					valueHolder = "";
				}
				if(valueHolder.length() != 0 && !valueHolder.matches("\\d\\d\\d-\\d\\d\\d-\\d\\d\\d\\d"))
				{
					errors = true;
					errorMessage += "Invalid Phone Number.\n";
				}
				else
				{
					if(phoneNumOrig.equals(emptyPhoneNum))
					{
						verificationStatement += "Change Phone Number from:  to " + valueHolder + "\n";
					}
					else
					{
						verificationStatement += "Change Phone Number from: " + phoneNumOrig + " to " + valueHolder + "\n";
					}				
					updateQueries.add("UPDATE Children set most_recent_phone = \"" + valueHolder + "\" WHERE id = " + currentID + ";");
				}
			}
			valueHolder = email.getText();
			if(!valueHolder.equals(emailOrig))
			{
				if(valueHolder.trim().length() != 0 && !valueHolder.contains("@") && !valueHolder.contains("."))
				{
					errors = true;
					errorMessage += "Invalid Email Address.\n";
				}
				else
				{
					changes = true;
					verificationStatement += "Change Email from: " + emailOrig + " to " + valueHolder + "\n";
					updateQueries.add("UPDATE Children set most_recent_email = \"" + valueHolder + "\" WHERE id = " + currentID + ";");
				}
			}
			valueHolder = parent.getText();
			if(!valueHolder.equals(parentOrig))
			{
				changes = true;
				verificationStatement += "Change Parent from: " + parentOrig + " to " + valueHolder + "\n";
				updateQueries.add("UPDATE Children set most_recent_parent = \"" + valueHolder + "\" WHERE id = " + currentID + ";");
			}
			
			Date d = new Date(System.currentTimeMillis());
			int currentYear = d.getYear()+1900;
			
			if(Constants.adminEdit)
			{
				
				for(int a = 0; a < comboBoxes.size(); a++)
				{
					int year = Integer.parseInt(textFields.get(a).get(0).getText());
					//Comboboxes programBox, schoolBox, GradeBox, Citybox
					if(comboBoxesOrigIndex.get(a)[0] != comboBoxes.get(a).get(0).getSelectedIndex()) //program
					{
						changes = true;
						String val = comboBoxes.get(a).get(0).getItemAt(comboBoxesOrigIndex.get(a)[0]);
						String newVal = (String)comboBoxes.get(a).get(0).getSelectedItem();
						verificationStatement += "Change program in year: " + year + " to " + newVal + " from " + val + "\n";
						newVal = programsNameToID.get((String)comboBoxes.get(a).get(0).getSelectedItem());
						updateQueries.add("UPDATE Program_Data set program_id = " + newVal + " WHERE child_id = " + currentID + " AND year = " + year + ";");
					}
					if(comboBoxesOrigIndex.get(a)[1] != comboBoxes.get(a).get(1).getSelectedIndex()) //school
					{
						if(comboBoxesOrigIndex.get(a)[1] != -1)
						{
							changes = true;
							String val = comboBoxes.get(a).get(1).getItemAt(comboBoxesOrigIndex.get(a)[1]);
							String newVal = (String)comboBoxes.get(a).get(1).getSelectedItem();
							verificationStatement += "Change school in year: " + year + " to " + newVal + " from " + val + "\n";
							if(comboBoxes.get(a).get(1).getSelectedIndex() == 0) 
							{
								newVal = "null";
							}
							else
							{
								newVal = schoolsNameToID.get((String)comboBoxes.get(a).get(1).getSelectedItem());
							}
							updateQueries.add("UPDATE Program_Data set school_id = " + newVal + " WHERE child_id = " + currentID + " AND year = " + year + ";");
							if(year == currentYear)
							{
								updateQueries.add("UPDATE Children set most_recent_school = " + newVal + " WHERE id = " + currentID + ";");
							}
						}
					}
					if(comboBoxesOrigIndex.get(a)[2] != comboBoxes.get(a).get(2).getSelectedIndex()) //grade
					{
						if(comboBoxesOrigIndex.get(a)[2] != -1)
						{
							changes = true;
							String val = comboBoxes.get(a).get(2).getItemAt(comboBoxesOrigIndex.get(a)[2]);
							String newVal = (String)comboBoxes.get(a).get(2).getSelectedItem();
							verificationStatement += "Change grade in year: " + year + " to " + newVal + " from " + val + "\n";
							if(comboBoxes.get(a).get(2).getSelectedIndex() == 0)
							{
								newVal = "null";
							}
							else
							{
								newVal = gradesNameToID.get((String)comboBoxes.get(a).get(2).getSelectedItem());
							}
							updateQueries.add("UPDATE Program_Data set grade_id = " + newVal + " WHERE child_id = " + currentID + " AND year = " + year + ";");
						}
					}
					if(comboBoxesOrigIndex.get(a)[3] != comboBoxes.get(a).get(3).getSelectedIndex()) //city
					{
						if(comboBoxesOrigIndex.get(a)[3] != -1)
						{
							changes = true;
							String val = comboBoxes.get(a).get(3).getItemAt(comboBoxesOrigIndex.get(a)[3]);
							String newVal = (String)comboBoxes.get(a).get(3).getSelectedItem();
							verificationStatement += "Change city in year: " + year + " to " + newVal + " from " + val + "\n";
							if(comboBoxes.get(a).get(3).getSelectedIndex() == 0)
							{
								newVal = "null";
							}
							else
							{
								newVal = citiesNameToID.get((String)comboBoxes.get(a).get(3).getSelectedItem());
							}
							updateQueries.add("UPDATE Program_Data set city_id = " + newVal + " WHERE child_id = " + currentID + " AND year = " + year + ";");
							if(year == currentYear)
							{
								updateQueries.add("UPDATE Children set most_recent_city = " + newVal + " WHERE id = " + currentID + ";");
							}
						}
					}
					
					if(!fieldsOrigValues.get(a)[1].equals(textFields.get(a).get(1).getText().trim())) //highest level
					{
						changes = true;
						String val = fieldsOrigValues.get(a)[1];
						String newVal = textFields.get(a).get(1).getText().trim();
						if(newVal.length() > 0 && !newVal.matches("([1-9]|10)"))
						{
							errors = true;
							errorMessage += "Highest Level level must either be blank or a number between 1-10\n";
						}
						verificationStatement += "Change highest level in year: " + year + " to " + newVal + " from " + val + "\n";
						if(newVal.length() > 0)
						{
							updateQueries.add("UPDATE Program_Data set highest_level_normal = " + newVal + " WHERE child_id = " + currentID + " AND year = " + year + ";");
						}
						else
						{
							updateQueries.add("UPDATE Program_Data set highest_level_normal = null WHERE child_id = " + currentID + " AND year = " + year + ";");
						}
					}
					
					if(!fieldsOrigValues.get(a)[2].equals(ecButtons.get(a).getText())) //ec boxes
					{
						changes = true;
						String val = fieldsOrigValues.get(a)[2];
						String newVal = ecButtons.get(a).getText();
						
						verificationStatement += "Change ec boxes in year: " + year + " to " + newVal + " from " + val + "\n";
						if(newVal.length() > 0)
						{
							if(newVal.split(" ")[0].equals("1"))
							{
								updateQueries.add("UPDATE Program_Data set extra_one = true WHERE child_id = " + currentID + " AND year = " + year + ";");
							}
							else
							{
								updateQueries.add("UPDATE Program_Data set extra_one = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							}
							if(newVal.contains("2"))
							{
								updateQueries.add("UPDATE Program_Data set extra_two = true WHERE child_id = " + currentID + " AND year = " + year + ";");
							}
							else
							{
								updateQueries.add("UPDATE Program_Data set extra_two = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							}
							if(newVal.contains("3"))
							{
								updateQueries.add("UPDATE Program_Data set extra_three = true WHERE child_id = " + currentID + " AND year = " + year + ";");
							}
							else
							{
								updateQueries.add("UPDATE Program_Data set extra_three = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							}
							if(newVal.contains("4"))
							{
								updateQueries.add("UPDATE Program_Data set extra_four = true WHERE child_id = " + currentID + " AND year = " + year + ";");
							}
							else
							{
								updateQueries.add("UPDATE Program_Data set extra_four = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							}
							if(newVal.contains("5"))
							{
								updateQueries.add("UPDATE Program_Data set extra_five = true WHERE child_id = " + currentID + " AND year = " + year + ";");
							}
							else
							{
								updateQueries.add("UPDATE Program_Data set extra_five = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							}
							if(newVal.contains("6"))
							{
								updateQueries.add("UPDATE Program_Data set extra_six = true WHERE child_id = " + currentID + " AND year = " + year + ";");
							}
							else
							{
								updateQueries.add("UPDATE Program_Data set extra_six = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							}
							if(newVal.contains("7"))
							{
								updateQueries.add("UPDATE Program_Data set extra_seven = true WHERE child_id = " + currentID + " AND year = " + year + ";");
							}
							else
							{
								updateQueries.add("UPDATE Program_Data set extra_seven = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							}
							if(newVal.contains("8"))
							{
								updateQueries.add("UPDATE Program_Data set extra_eight = true WHERE child_id = " + currentID + " AND year = " + year + ";");
							}
							else
							{
								updateQueries.add("UPDATE Program_Data set extra_eight = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							}
							if(newVal.contains("9"))
							{
								updateQueries.add("UPDATE Program_Data set extra_nine = true WHERE child_id = " + currentID + " AND year = " + year + ";");
							}
							else
							{
								updateQueries.add("UPDATE Program_Data set extra_nine = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							}
							if(newVal.contains("10"))
							{
								updateQueries.add("UPDATE Program_Data set extra_ten = true WHERE child_id = " + currentID + " AND year = " + year + ";");
							}
							else
							{
								updateQueries.add("UPDATE Program_Data set extra_ten = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							}
						}
						else
						{
							
							updateQueries.add("UPDATE Program_Data set extra_one = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							updateQueries.add("UPDATE Program_Data set extra_two = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							updateQueries.add("UPDATE Program_Data set extra_three = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							updateQueries.add("UPDATE Program_Data set extra_four = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
							updateQueries.add("UPDATE Program_Data set extra_five = false WHERE child_id = " + currentID + " AND year = " + year + ";");														
							updateQueries.add("UPDATE Program_Data set extra_six = false WHERE child_id = " + currentID + " AND year = " + year + ";");															
							updateQueries.add("UPDATE Program_Data set extra_seven = false WHERE child_id = " + currentID + " AND year = " + year + ";");															
							updateQueries.add("UPDATE Program_Data set extra_eight = false WHERE child_id = " + currentID + " AND year = " + year + ";");															
							updateQueries.add("UPDATE Program_Data set extra_nine = false WHERE child_id = " + currentID + " AND year = " + year + ";");															
							updateQueries.add("UPDATE Program_Data set extra_ten = false WHERE child_id = " + currentID + " AND year = " + year + ";");	
						}
						
					}
				}
			}
			else
			{
				if(textFields.size()>0)
				{
					int year = Integer.parseInt(textFields.get(textFields.size()-1).get(0).getText());
					//Comboboxes programBox, schoolBox, GradeBox, Citybox
					if(year == currentYear)
					{
						int a = comboBoxes.size()-1;
						if(comboBoxesOrigIndex.get(a)[0] != comboBoxes.get(a).get(0).getSelectedIndex()) //program
						{
							changes = true;
							String val = comboBoxes.get(a).get(0).getItemAt(comboBoxesOrigIndex.get(a)[0]);
							String newVal = (String)comboBoxes.get(a).get(0).getSelectedItem();
							verificationStatement += "Change program in year: " + year + " to " + val + " from " + newVal + "\n";
							newVal = programsNameToID.get((String)comboBoxes.get(a).get(0).getSelectedItem());
							updateQueries.add("UPDATE Program_Data set program_id = " + newVal + " WHERE child_id = " + currentID + " AND year = " + year + ";");
						}
						if(comboBoxesOrigIndex.get(a)[1] != comboBoxes.get(a).get(1).getSelectedIndex()) //school
						{
							if(comboBoxesOrigIndex.get(a)[1] != -1)
							{
								changes = true;
								String val = comboBoxes.get(a).get(1).getItemAt(comboBoxesOrigIndex.get(a)[1]);
								String newVal = (String)comboBoxes.get(a).get(1).getSelectedItem();
								verificationStatement += "Change school in year: " + year + " to " + val + " from " + newVal + "\n";
								if(comboBoxes.get(a).get(1).getSelectedIndex() == 0)
								{
									newVal = "null";
								}
								else
								{	
									newVal = schoolsNameToID.get((String)comboBoxes.get(a).get(1).getSelectedItem());
								}
								updateQueries.add("UPDATE Program_Data set school_id = " + newVal + " WHERE child_id = " + currentID + " AND year = " + year + ";");
								updateQueries.add("UPDATE Children set most_recent_school = " + newVal + " WHERE id = " + currentID + ";");
							}
							if(comboBoxes.get(a).get(1).getSelectedIndex() == 0)
							{
								System.out.println("HI");
							}
						}
						if(comboBoxesOrigIndex.get(a)[2] != comboBoxes.get(a).get(2).getSelectedIndex()) //grade
						{
							if(comboBoxesOrigIndex.get(a)[2] != -1)
							{
								changes = true;
								String val = comboBoxes.get(a).get(2).getItemAt(comboBoxesOrigIndex.get(a)[2]);
								String newVal = (String)comboBoxes.get(a).get(2).getSelectedItem();
								verificationStatement += "Change grade in year: " + year + " to " + val + " from " + newVal + "\n";
								if(comboBoxes.get(a).get(2).getSelectedIndex() == 0)
								{
									newVal = "null";
								}
								else
								{
									newVal = gradesNameToID.get((String)comboBoxes.get(a).get(2).getSelectedItem());
								}
								updateQueries.add("UPDATE Program_Data set grade_id = " + newVal + " WHERE child_id = " + currentID + " AND year = " + year + ";");
							}
						}
						if(comboBoxesOrigIndex.get(a)[3] != comboBoxes.get(a).get(3).getSelectedIndex()) //city
						{
							if(comboBoxesOrigIndex.get(a)[3] != -1)
							{
								changes = true;
								String val = comboBoxes.get(a).get(3).getItemAt(comboBoxesOrigIndex.get(a)[3]);
								String newVal = (String)comboBoxes.get(a).get(3).getSelectedItem();
								verificationStatement += "Change city in year: " + year + " to " + val + " from " + newVal + "\n";
								if(comboBoxes.get(a).get(3).getSelectedIndex() == 0)
								{
									newVal = "null";
								}
								else 
								{
									newVal = citiesNameToID.get((String)comboBoxes.get(a).get(3).getSelectedItem());
								}
								updateQueries.add("UPDATE Program_Data set city_id = " + newVal + " WHERE child_id = " + currentID + " AND year = " + year + ";");
								updateQueries.add("UPDATE Children set most_recent_city = " + newVal + " WHERE id = " + currentID + ";");
							}
						}
					}
				}
			}
			
			if(changes)
			{
				if(!errors)
				{
					int verifyChanges = JOptionPane.showConfirmDialog(null, verificationStatement,"Confirm",JOptionPane.YES_NO_OPTION);
					if(verifyChanges == JOptionPane.YES_OPTION)
					{
						this.remove(editButtons);
						this.add(normalButtons, BorderLayout.SOUTH);
						this.paintAll(this.getGraphics());
						this.editState = false;
						for(int a = 0; a < updateQueries.size(); a++)
						{
							dbFriend.executeUpdate(updateQueries.get(a));
						}
						if(DOBChange)
						{
							String[] years = dbFriend.query1DstringRet("SELECT year FROM Program_Data WHERE child_id = " + currentID + ";");
							for(int a = 0; a < years.length; a++)
							{
								String ageCalc = Constants.calcAgeForDB(newDOB,Integer.parseInt(years[a]));
								dbFriend.executeUpdate("UPDATE Program_Data SET age = " + ageCalc + " WHERE year = " + years[a] + " AND child_id = " + currentID + ";");
								//System.out.println("UPDATE Program_Data SET age = " + ageCalc + " WHERE year = " + years[a] + " AND child_id = " + currentID + ";");
							}
						}
						fNameOrig = fName.getText();
						lNameOrig = lName.getText();
						//dobOrig = dob.getText();
						dobStr = "";
						if(dobMonth.getSelectedIndex() != 0 && dobDay.getSelectedIndex() != 0 && dobYear.getSelectedIndex() != 0)	
						{	
							dobStr += (dobMonth.getSelectedIndex()) + "/";
							dobStr += dobDay.getSelectedItem() + "/";
							dobStr += dobYear.getSelectedItem();
						}
						dobOrig = dobStr;
						emailOrig = email.getText();
						phoneNumOrig = phoneNum.getText();
						parentOrig = parent.getText();
						for(int a = 0; a < comboBoxes.size(); a++)
						{	
							//Comboboxes programBox, schoolBox, GradeBox, Citybox
							comboBoxesOrigIndex.get(a)[0] = comboBoxes.get(a).get(0).getSelectedIndex(); //program
							comboBoxesOrigIndex.get(a)[1] = comboBoxes.get(a).get(1).getSelectedIndex(); //school
							comboBoxesOrigIndex.get(a)[2] = comboBoxes.get(a).get(2).getSelectedIndex(); //grade
							comboBoxesOrigIndex.get(a)[3] = comboBoxes.get(a).get(3).getSelectedIndex(); //city
						}
						disableInputs();
						dbcli.notifyChange();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, errorMessage, "Input Error", JOptionPane.WARNING_MESSAGE);
				}
			}
			else
			{
				if(Constants.adminEdit)
				{
					this.remove(editAdminButtons);
				}
				else
				{
					this.remove(editButtons);
				}
				this.add(normalButtons, BorderLayout.SOUTH);
				this.paintAll(this.getGraphics());
				this.editState = false;
				disableInputs();
			}
		}
		else if(e.getActionCommand().equals("Cancel"))
		{
			if(Constants.adminEdit)
			{
				this.remove(editAdminButtons);
			}
			else
			{
				this.remove(editButtons);
			}
			this.add(normalButtons, BorderLayout.SOUTH);
			this.paintAll(this.getGraphics());
			this.editState = false;
			fName.setText(fNameOrig);
			lName.setText(lNameOrig);
			//dob.setText(dobOrig);
			if(!dobOrig.trim().equals(""))
			{
				String[] dobValHolder = dobOrig.split("/");
				dobMonth.setSelectedIndex(Integer.parseInt(dobValHolder[0]));
				dobDay.setSelectedIndex(Integer.parseInt(dobValHolder[1]));
				dobYear.setSelectedItem(dobValHolder[2]);
			}
			else
			{
				dobMonth.setSelectedIndex(0);
				dobDay.setSelectedIndex(0);
				dobYear.setSelectedIndex(0);
			}
			phoneNum.setText(phoneNumOrig);
			email.setText(emailOrig);
			parent.setText(parentOrig);
			disableInputs();
		}
		else if(e.getActionCommand().equals("Merge This Reader"))
		{
			if(mergeID.length() == 0)
			{
				mergeID = currentID;
				JOptionPane.showMessageDialog(null,"Select another reader to merge with","Select Reder to Merge",JOptionPane.INFORMATION_MESSAGE);
				this.setVisible(false);
			}
			else
			{
				String[] queryHolder = dbFriend.query1DstringRet("SELECT first_name, last_name FROM Children WHERE id = " + mergeID + ";",2);
				String kid1Name = queryHolder[0] + " " + queryHolder[1];
				queryHolder = dbFriend.query1DstringRet("SELECT first_name, last_name FROM Children WHERE id = " + currentID + ";",2);
				String kid2Name = queryHolder[0] + " " + queryHolder[1];
				int doOrNot = JOptionPane.showConfirmDialog(null,"Are you sure you want to merge " + kid1Name + " with " + kid2Name, "Confirm Merge", JOptionPane.YES_NO_OPTION);
				if(doOrNot == JOptionPane.YES_OPTION)
				{
					rmw.setReaders(mergeID,currentID);
					this.setVisible(false);
					rmw.setVisible(true);
				}
				else
				{
					mergeID = "";
					JOptionPane.showMessageDialog(null,"OK, resetting merge inputs","No Merger",JOptionPane.INFORMATION_MESSAGE);
					this.setVisible(false);
				}
			}
		}
		else if(e.getActionCommand().equals("Split This Reader"))
		{
			rsw.setReader(currentID);
			this.setVisible(false);
			rsw.setVisible(true);
		}
	}
	
	/**
	 * Enables Inputs
	 */
	protected void enableInputs()
	{
		//Comboboxes programBox, schoolBox, GradeBox, Citybox
		//fieldBox yearfield, highestField, extrafield, poolfield, agefield
		this.fName.setEditable(true);
		this.lName.setEditable(true);
		this.dobMonth.setEnabled(true);
		this.dobDay.setEnabled(true);
		this.dobYear.setEnabled(true);
		this.phoneNum.setEditable(true);
		this.email.setEditable(true);
		this.parent.setEditable(true);
		phoneNum.setCaretPosition(0);
		if(Constants.adminEdit)
		{
			for(int a = 0; a < comboBoxes.size(); a++)
			{
				for(int b = 0; b < comboBoxes.get(0).size(); b++)
				{
					comboBoxes.get(a).get(b).setEnabled(true);
				}
				textFields.get(a).get(1).setEditable(true);
				ecButtons.get(a).setEnabled(true);
			}
			
		}
		else
		{
			Date d = new Date(System.currentTimeMillis());
			int year = d.getYear()+1900;
			if(textFields.size() > 0)
			{
				if(year == Integer.parseInt(textFields.get(textFields.size()-1).get(0).getText()))
				{
					for(int a = 0; a < comboBoxes.get(comboBoxes.size()-1).size(); a++)
					{
						comboBoxes.get(comboBoxes.size()-1).get(a).setEnabled(true);
					}
				}
			}
		}
	}
	
	/**
	 * Disables Inputs
	 */
	protected void disableInputs()
	{
		this.fName.setEditable(false);
		this.lName.setEditable(false);
		this.dobMonth.setEnabled(false);
		this.dobDay.setEnabled(false);
		this.dobYear.setEnabled(false);
		this.phoneNum.setEditable(false);
		phoneNum.setCaretPosition(0);
		this.email.setEditable(false);
		this.parent.setEditable(false);
		if(currentID != null)
		{
			if(Constants.adminEdit)
			{
				for(int a = 0; a < comboBoxes.size(); a++)
				{
					for(int b = 0; b < comboBoxes.get(0).size(); b++)
					{
						comboBoxes.get(a).get(b).setEnabled(false);
					}
					textFields.get(a).get(1).setEditable(false);
					ecButtons.get(a).setEnabled(false);
				}
			}
			else
			{
				Date d = new Date(System.currentTimeMillis());
				int year = d.getYear()+1900;
				if(!textFields.isEmpty())
				{
					if(year == Integer.parseInt(textFields.get(textFields.size()-1).get(0).getText()))
					{
						for(int a = 0; a < comboBoxes.get(comboBoxes.size()-1).size(); a++)
						{
							comboBoxes.get(comboBoxes.size()-1).get(a).setEnabled(false);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Makes window visible
	 */
	public void makeVisible()
	{
		this.remove(mergeButtons);
		this.remove(splitButtons);
		this.remove(normalButtons);
		if(editState)
		{
			mergeID = "";
			if(Constants.adminEdit)
			{
				this.remove(editAdminButtons);
			}
			else
			{
				this.remove(editButtons);
			}
			this.add(normalButtons, BorderLayout.SOUTH);
		}
		if(Constants.mergeMode)
		{
			this.remove(editAdminButtons);
			this.remove(editButtons);
			this.remove(splitButtons);
			this.add(mergeButtons, BorderLayout.SOUTH);
		}
		else
		{
			mergeID = "";
		}
		if(Constants.splitMode)
		{
			mergeID = "";
			this.remove(editAdminButtons);
			this.remove(editButtons);
			this.remove(mergeButtons);
			this.add(splitButtons, BorderLayout.SOUTH);
		}
		if(!Constants.mergeMode && !Constants.splitMode && !editState)
		{
			this.add(normalButtons, BorderLayout.SOUTH);
		}
		this.paintAll(this.getGraphics());
		this.editState = false;
		disableInputs();
		this.setVisible(true);
	}

	/**
	 * Updates the window information
	 */
	private void fullUpdate()
	{
		if(currentID != null)
		{
			setReader(currentID);
		}
		this.paintAll(this.getGraphics());		
	}
	
	/**
	 * Handles database changes
	 */
	public void databaseChanged() 
	{
		//System.out.println(this.currentID);
		boolean wasVisible = this.isVisible();
		if(wasVisible)
		{
			this.setVisible(false);
		}
		fullUpdate();
		updateLists();
		if(wasVisible)
		{
			this.setVisible(true);
		}
	}
	
	/**
	 * Updates database connection
	 * @param dbh New database connection
	 */
	public void updateDatabaseConnection(DBHandler dbh)
	{
		dbFriend = dbh;
		
		updateLists();
		
		regWin.updateDatabaseConnection(dbFriend);
		cinWin.updateDatabaseConnection(dbFriend);
		rmw.updateDatabaseConnection(dbFriend);
		rsw.updateDatabaseConnection(dbFriend);
	}
	
	/**
	 * Updates the lists and hashes
	 */
	private void updateLists()
	{
		this.schoolsIDtoName = new Hashtable<String,String>();
		this.schoolsNameToID = new Hashtable<String,String>();
		this.citiesIDtoName = new Hashtable<String,String>();
		this.citiesNameToID = new Hashtable<String,String>();
		this.gradesIDtoName = new Hashtable<String,String>();
		this.gradesNameToID = new Hashtable<String,String>();
		this.programsIDtoName = new Hashtable<String,String>();
		this.programsNameToID = new Hashtable<String,String>();
		
		this.schools = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS_ALPHA, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		String[][] none = {{"-1","None"}};
		for(int a = 0; a < schools.length; a++)
		{
			schoolsIDtoName.put(schools[a][0], schools[a][1]);
			schoolsNameToID.put(schools[a][1], schools[a][0]);
		}
		schoolsIDtoName.put("-1","None");
		schoolsNameToID.put("None","-1");
		this.cities = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS_ALPHA, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		for(int a = 0; a < cities.length; a++)
		{
			citiesIDtoName.put(cities[a][0], cities[a][1]);
			citiesNameToID.put(cities[a][1], cities[a][0]);
		}
		schools = Constants.addCommonEntities(Constants.COMMON_SCHOOLS, schools);
		cities = Constants.addCommonEntities(Constants.COMMON_CITIES, cities);
		cities = Constants.addCommonEntities(none, cities);
		citiesIDtoName.put("-1", "None");
		citiesNameToID.put("None", "-1");
		this.grades = dbFriend.query2DstringRet(Queries.ALL_GRADES_PL_IDS, Queries.ALL_GRADES_PL_IDS_COL_LEN);
		grades = Constants.addCommonEntities(none, grades);
		for(int a = 0; a < grades.length; a++)
		{
			gradesIDtoName.put(grades[a][0], grades[a][1]);
			gradesNameToID.put(grades[a][1], grades[a][0]);
		}
		this.programs = dbFriend.query2DstringRet(Queries.ALL_PROGRAMS_PL_IDS, Queries.ALL_PROGRAMS_PL_IDS_COL_LEN);
		for(int a = 0; a < programs.length; a++)
		{
			programsIDtoName.put(programs[a][0], programs[a][1]);
			programsNameToID.put(programs[a][1], programs[a][0]);
		}
		
		setReader(currentID);
	}
	
	/**
	 * 
	 * @param pastInfo Result from the query asking for past years info of current reader
	 * @return
	 */
	protected JPanel makeTable(String[][] pastInfo, String dob)
	{	
		this.yearsExisting = "";
		//   0		 1			2			3			4		5			6				7	8	9	10	11	12	13	14	15	16	17			18
		//child_id	year	grade_id	school_id	city_id	program_id	highlest_level_norm	e1	e2	e3	e4	e5	e6	e7	e8	e9	e10	pool_pass	age
		
		//static   combo     combo   combo   combo    static           static           static   static
		//"Year" "Program" "School" "Grade" "City" "Highest Level" "Extra Challenge" "Pool Pass" "Age"
		JPanel output = new JPanel();
		
		JLabel yearLabel = new JLabel("Year");
		JLabel programLabel = new JLabel("Program");
		JLabel schoolLabel = new JLabel("School");
		JLabel gradeLabel = new JLabel("Grade");
		JLabel cityLabel = new JLabel("City");
		JLabel highestLabel = new JLabel("Highest Level");
		JLabel extraLabel = new JLabel("Extra Challange");
		JLabel poolLabel = new JLabel("Pool Pass");
		JLabel ageLabel = new JLabel("Age");
		JPanel labelPanel = new JPanel();
		
		
		labelPanel.setLayout(new GridLayout(1,9));
		yearLabel.setMaximumSize(new Dimension(10,10));
		labelPanel.add(yearLabel);
		programLabel.setMaximumSize(new Dimension(10,10));
		labelPanel.add(programLabel);
		schoolLabel.setMaximumSize(new Dimension(10,10));
		labelPanel.add(schoolLabel);
		gradeLabel.setMaximumSize(new Dimension(10,10));
		labelPanel.add(gradeLabel);
		cityLabel.setMaximumSize(new Dimension(10,10));
		labelPanel.add(cityLabel);
		highestLabel.setMaximumSize(new Dimension(10,10));
		labelPanel.add(highestLabel);
		extraLabel.setMaximumSize(new Dimension(10,10));
		labelPanel.add(extraLabel);
		poolLabel.setMaximumSize(new Dimension(10,10));
		labelPanel.add(poolLabel);
		ageLabel.setMaximumSize(new Dimension(10,10));
		labelPanel.add(ageLabel);
		
		labelPanel.setSize(width, height);

		comboBoxes = new ArrayList<ArrayList<JComboBox<String>>>();
		textFields = new ArrayList<ArrayList<JTextField>>();
		comboBoxesOrigIndex = new ArrayList<int[]>();
		fieldsOrigValues = new ArrayList<String[]>();
		ecButtons = new ArrayList<JButton>();
		int panelSize = 1;
		if(pastInfo != null) 
		{
			panelSize += pastInfo.length;
		}
		output.setLayout(new GridLayout(panelSize,1));
		output.setSize(width, height);
		output.add(labelPanel);
		if(pastInfo != null)
		{
			valueIndexes = new int[pastInfo.length][5];
			for(int a = 0; a < pastInfo.length; a++)
			{
				ArrayList<JComboBox<String>> boxes = new ArrayList<JComboBox<String>>();
				ArrayList<JTextField> fields = new ArrayList<JTextField>();
				int[] origIndexes = new int[4];
				String[] origField = new String[5]; //including the EC, even though it's a button
				
				String[] schoolNames = new String[schools.length];
				for(int b = 0; b < schools.length; b++)
				{
					schoolNames[b] = schools[b][1];
				}
				String[] citiesNames = new String[cities.length];
				for(int b = 0; b < cities.length; b++)
				{
					citiesNames[b] = cities[b][1];
				}
				String[] gradesNames = new String[grades.length];
				for(int b = 0; b < grades.length; b++)
				{
					gradesNames[b] = grades[b][1];
				}
				String[] programsNames = new String[programs.length];
				for(int b = 0; b < programs.length; b++)
				{
					programsNames[b] = programs[b][1];
				}
				JTextField yearField = new JTextField(pastInfo[a][1]);
				fields.add(yearField);
				origField[0] = pastInfo[a][1];
				valueIndexes[a][0] = Integer.parseInt(pastInfo[a][1]);
				yearsExisting += pastInfo[a][1] +" ";
				yearField.setEditable(false);
				JComboBox<String> programBox = new JComboBox<String>(programsNames);
				programBox.setSelectedItem(programsIDtoName.get(pastInfo[a][5]));
				origIndexes[0] = programBox.getSelectedIndex();
				valueIndexes[a][1] = Integer.parseInt(pastInfo[a][5]);
				boxes.add(programBox);
				programBox.setEnabled(false);
				JComboBox<String> schoolBox = new JComboBox<String>(schoolNames);
				if(!pastInfo[a][3].equals(""))
				{
					schoolBox.setSelectedItem(schoolsIDtoName.get(pastInfo[a][3]));
					origIndexes[1] = schoolBox.getSelectedIndex();
					valueIndexes[a][2] = Integer.parseInt(pastInfo[a][3]);
				}
				else
				{
					origIndexes[1] = -1;
					valueIndexes[a][2] = -1;
				}
				boxes.add(schoolBox);
				schoolBox.setEnabled(false);
				JComboBox<String> gradeBox = new JComboBox<String>(gradesNames);
				if(!pastInfo[a][2].equals(""))
				{
					gradeBox.setSelectedItem(gradesIDtoName.get(pastInfo[a][2]));
					origIndexes[2] = gradeBox.getSelectedIndex();
					valueIndexes[a][3] = Integer.parseInt(pastInfo[a][2]);
				}
				else
				{
					origIndexes[2] = -1;
					valueIndexes[a][3] = -1;
				}
				boxes.add(gradeBox);
				gradeBox.setEnabled(false);
				JComboBox<String> cityBox = new JComboBox<String>(citiesNames);
				if(!pastInfo[a][4].equals(""))
				{
					cityBox.setSelectedItem(citiesIDtoName.get(pastInfo[a][4]));
					origIndexes[3] = cityBox.getSelectedIndex();
					valueIndexes[a][4] = Integer.parseInt(pastInfo[a][4]); 
				}
				else
				{
					origIndexes[3] = -1;
					valueIndexes[a][4] = -1;
				}
				boxes.add(cityBox);
				cityBox.setEnabled(false);
				JTextField highestField = new JTextField(pastInfo[a][6]);
				fields.add(highestField);
				origField[1] = pastInfo[a][6];
				highestField.setEditable(false);

				
				String ec = "";
				
				for(int b = 7; b < 17; b++) //extra challange
				{
					if(pastInfo[a][b].equalsIgnoreCase("1"))
					{
						ec += b-6 + " ";
					}
				}
				JButton extraButton = new JButton(ec);
				origField[2] = ec;
				ecButtons.add(extraButton);
				extraButton.setEnabled(false);
				extraButton.addActionListener(this);
				
				String pp = "False";
				if(pastInfo[a][17].equalsIgnoreCase("1"))
				{
					pp = "True";
				}
				JTextField poolField = new JTextField(pp);
				origField[3] = pp; 
				fields.add(poolField);
				poolField.setEditable(false);
				
				String value = "";
				if(!dob.equals(""))
				{
					if(!pastInfo[a][1].equals("") && ageUnderThreshold(dob,Integer.parseInt(pastInfo[a][1])))
					{
						int months = calcMonths(dob,Integer.parseInt(pastInfo[a][1]));
					
						value = "" + months + " months";
						if(months == 1)
						{
							value = "" + months + " month";
						}
					}
					else
					{
						value = Constants.calcAgeForDB(dob,Integer.parseInt(pastInfo[a][1]));
					}
				}
				JTextField ageField = new JTextField(value);
				origField[4] = value; 
				
				fields.add(ageField);
				ageField.setEditable(false);
				JPanel inPanel = new JPanel();				
				
				inPanel.setLayout(new GridLayout(1,9));
				//yearField.setMaximumSize(new Dimension(10,10));
				inPanel.add(yearField);
				//programBox.setMaximumSize(new Dimension(10,10));
				inPanel.add(programBox);
				//schoolBox.setMaximumSize(new Dimension(10,10));
				inPanel.add(schoolBox);
				//gradeBox.setMaximumSize(new Dimension(10,10));
				inPanel.add(gradeBox);
				//cityBox.setMaximumSize(new Dimension(10,10));
				inPanel.add(cityBox);
				//highestField.setMaximumSize(new Dimension(10,10));
				inPanel.add(highestField);
				//extraButton.setMaximumSize(new Dimension(10,10));
				inPanel.add(extraButton);
				//poolField.setMaximumSize(new Dimension(10,10));
				inPanel.add(poolField);
				//ageField.setMaximumSize(new Dimension(10,10));
				inPanel.add(ageField);
				inPanel.setSize(width, height);
				
				
				output.add(inPanel);
				comboBoxes.add(boxes);
				textFields.add(fields);
				comboBoxesOrigIndex.add(origIndexes);
				fieldsOrigValues.add(origField);
			}
		}
		return output;
	}
	
	/**
	 * Handles key types
	 */
	public void keyTyped(KeyEvent e) 
	{
	}

	/**
	 * Handles key presses
	 */
	public void keyPressed(KeyEvent e) 
	{
	}

	/**
	 * Handles key releases
	 */
	public void keyReleased(KeyEvent e) 
	{
		if((e.getKeyCode() == KeyEvent.VK_L) &(e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK))
		{
			mainWindow.secretMenuKeyboardPress();
		}
	}
	
	/**
	 * Checks if age is under threshold for displaying age in months. Should be moved to Constants
	 * @param dob Date of birth
	 * @param year Year to check age for
	 * @return If age is under month display threshold
	 */
	private boolean ageUnderThreshold(String dob, int year)
	{
		boolean output = false;
		int monthsOld = calcMonths(dob,year);
		if(monthsOld <= Constants.HIGHEST_MONTH_AGE_TO_DISPLAY)
		{
			output = true;
		}
		return output;
	}
	
	/**
	 * Gives age in months. Should be moved to Constants
	 * @param dob Date of birth
	 * @param year Year to calculate for
	 * @return Age in months
	 */
	private int calcMonths(String dob, int year)
	{
		int output = -1;
		
		String[] brokenUp = dob.split("/");
		int kidMonth = Integer.parseInt(brokenUp[0]);
		int kidYear = Integer.parseInt(brokenUp[2]);
		int yearValue = year - kidYear;
		int monthValue = Constants.AGE_CALC_MONTH - kidMonth;
		output = (yearValue * 12) + monthValue;
		
		return output;
	}
	
	/**
	 * Refreshes window.
	 */
	public void refresh()
	{
		if(editState)
		{
			this.remove(editAdminButtons);
			this.remove(editButtons);
			this.add(normalButtons, BorderLayout.SOUTH);
			editState = false;
		}
		fullUpdate();
	}

	public void mouseClicked(MouseEvent e) 
	{
		if(e.getSource() == phoneNum)
		{
			if(phoneNum.getText().equals(emptyPhoneNum))
			{
				phoneNum.setCaretPosition(0);
			}
		}
	}

	public void mousePressed(MouseEvent e) 
	{	
	}

	public void mouseReleased(MouseEvent e) 
	{	
	}

	public void mouseEntered(MouseEvent e) 
	{	
	}

	public void mouseExited(MouseEvent e) 
	{	
	}
}
