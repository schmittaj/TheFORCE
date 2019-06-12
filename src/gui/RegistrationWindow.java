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

import backend.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Window for registering new readers
 * @author Anthony Schmitt
 *
 */
public class RegistrationWindow  extends JFrame implements ActionListener, KeyListener
{
	//TODO implement DCL?
	//TODO Check grade against age to flag any possible input errors
	private DBHandler dbFriend;
	private String[][] schools, grades, cities, programs;
	private Hashtable<String, String> schoolsNameToID, schoolsIDtoName, gradesNameToID, gradesIDtoName, citiesNameToID, citiesIDtoName, programsIDtoName, programsNamesToID;
	private String currentID;
	private JComboBox<String> schoolBox, gradeBox, cityBox, programBox;
	private JLabel disp, dispName;
	private String firstName, lastName;
	private DatabaseChangeListenerImplementer dbcli;
	private int width, height;
	private SecretMenuToggle mainWindow;
	
	/**
	 * Constructor
	 * @param dbh The database connection
	 * @param curr Current reader's ID
	 * @param dbc DB change handler
	 * @param mainWin Main window
	 */
	public RegistrationWindow(DBHandler dbh, String curr, DatabaseChangeListenerImplementer dbc, SecretMenuToggle mainWin)
	{
		super();
		this.dbFriend = dbh;
		this.currentID = curr;
		this.dbcli = dbc;
		this.width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4);
		this.height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2.5);
		init();
		this.mainWindow = mainWin;
		this.addKeyListener(this);
		this.setFocusable(true);
	}

	
	
	/**
	 * Initializes the window
	 */
	private void init()
	{
		this.setTitle("Register Reader");
		
		schools = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS_ALPHA, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN);
		schoolsIDtoName = new Hashtable<String, String>();
		schoolsNameToID = new Hashtable<String, String>();
		for(int a = 0; a < schools.length; a++)
		{
			schoolsIDtoName.put(schools[a][0], schools[a][1]);
			schoolsNameToID.put(schools[a][1], schools[a][0]);
		}
		schoolsNameToID.put("None", "-1");
		grades = dbFriend.query2DstringRet(Queries.ALL_GRADES_PL_IDS, Queries.ALL_GRADES_PL_IDS_COL_LEN);
		gradesIDtoName = new Hashtable<String, String>();
		gradesNameToID = new Hashtable<String, String>();
		for(int a = 0; a < grades.length; a++)
		{
			gradesIDtoName.put(grades[a][0], grades[a][1]);
			gradesNameToID.put(grades[a][1], grades[a][0]);
		}
		gradesNameToID.put("None", "-1");
		String[][] gradeThing = {{"-1","None"}};
		grades = Constants.addCommonEntities(gradeThing, grades);
		cities = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS_ALPHA, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		citiesIDtoName = new Hashtable<String, String>();
		citiesNameToID = new Hashtable<String, String>();
		for(int a = 0; a < cities.length; a++)
		{
			citiesIDtoName.put(cities[a][0], cities[a][1]);
			citiesNameToID.put(cities[a][1], cities[a][0]);
		}
		schools = Constants.addCommonEntities(Constants.COMMON_SCHOOLS, schools);
		cities = Constants.addCommonEntities(Constants.COMMON_CITIES, cities);
		programs = dbFriend.query2DstringRet(Queries.ALL_PROGRAMS_PL_IDS, Queries.ALL_PROGRAMS_PL_IDS_COL_LEN);
		programsIDtoName = new Hashtable<String, String>();
		programsNamesToID = new Hashtable<String, String>();
		for(int a = 0; a < programs.length; a++)
		{
			programsIDtoName.put(programs[a][0], programs[a][1]);
			programsNamesToID.put(programs[a][1], programs[a][0]);
		}
		
		String[] schoolList = new String[schools.length];
		for(int a = 0; a < schools.length; a++)
		{
			schoolList[a] = schools[a][1];
		}
		schoolBox = new JComboBox<String>(schoolList);
		String[] gradeList = new String[grades.length];
		for(int a = 0; a < grades.length; a++)
		{
			gradeList[a] = grades[a][1];
		}
		gradeBox = new JComboBox<String>(gradeList);
		String[] cityList = new String[cities.length];
		for(int a = 0; a < cities.length; a++)
		{
			cityList[a] = cities[a][1];
		}
		cityBox = new JComboBox<String>(cityList);
		String[] programList = new String[programs.length];
		for(int a = 0; a < programList.length; a++)
		{
			programList[a] = programs[a][1];
		}
		programBox = new JComboBox<String>(programList);
		

		firstName = dbFriend.query1DstringRet("SELECT first_name FROM Children WHERE ID = " + currentID + ";")[0];
		lastName = dbFriend.query1DstringRet("SELECT last_name FROM Children WHERE ID = " + currentID + ";")[0];

		
		disp = new JLabel("Select the information for this year for:");
		dispName = new JLabel(firstName + " " + lastName);
		disp.setHorizontalAlignment(JLabel.CENTER);
		dispName.setHorizontalAlignment(JLabel.CENTER);
		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new GridLayout(2,1,0,-20));
		displayPanel.add(disp);
		displayPanel.add(dispName);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(5,1,0,0));
		mainPanel.add(displayPanel);
		JPanel programPanel = new JPanel();
		programPanel.setLayout(new FlowLayout());
		JLabel programLabel = new JLabel("Program:");
		programLabel.setHorizontalAlignment(JLabel.RIGHT);
		programLabel.setBorder(new EmptyBorder(0,40,0,10));
		programPanel.add(programLabel);
		//programBox.setMinimumSize(new Dimension(width/2, 10));
		programBox.setBorder(new EmptyBorder(0,0,0,10));
		programBox.setPreferredSize(new Dimension(200,30));
		programPanel.add(programBox);
		mainPanel.add(programPanel);
		JPanel schoolPanel = new JPanel();
		schoolPanel.setLayout(new FlowLayout());
		JLabel school = new JLabel("School/Daycare:");
		school.setHorizontalAlignment(JLabel.RIGHT);
		school.setBorder(new EmptyBorder(0,0,0,10));
		schoolPanel.add(school);
		schoolBox.setSize(width/2,20);
		schoolBox.setBorder(new EmptyBorder(0,0,0,10));
		schoolBox.setPreferredSize(new Dimension(200,30));
		schoolPanel.add(schoolBox);
		mainPanel.add(schoolPanel);
		JPanel gradePanel = new JPanel();
		gradePanel.setLayout(new FlowLayout());
		JLabel grade = new JLabel("Grade:");
		grade.setHorizontalAlignment(JLabel.RIGHT);
		grade.setBorder(new EmptyBorder(0,55,0,10));
		gradePanel.add(grade);
		gradeBox.setSize(width/2,20);
		gradeBox.setBorder(new EmptyBorder(0,0,0,10));
		gradeBox.setPreferredSize(new Dimension(200,30));
		gradePanel.add(gradeBox);
		mainPanel.add(gradePanel);
		JPanel cityPanel = new JPanel();
		cityPanel.setLayout(new FlowLayout());
		JLabel city = new JLabel("City:");
		city.setHorizontalAlignment(JLabel.RIGHT);
		city.setBorder(new EmptyBorder(0,65,0,10));
		cityPanel.add(city);
		cityBox.setSize(width/2, 20);
		cityBox.setBorder(new EmptyBorder(0,0,0,10));
		cityBox.setPreferredSize(new Dimension(200,30));
		cityPanel.add(cityBox);
		mainPanel.add(cityPanel);
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		this.setResizable(false);
		JButton done = new JButton("Done");
		JButton cancel = new JButton("Cancel");
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(done);
		buttonPanel.add(cancel);
		done.addActionListener(this);
		cancel.addActionListener(this);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setSize(width,height);
		this.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/5));
		this.setVisible(false);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * Sets to a new reader
	 * @param curr New reader's ID
	 */
	public void setCurrentID(String curr)
	{
		this.currentID = curr;
		firstName = dbFriend.query1DstringRet("SELECT first_name FROM Children WHERE ID = " + currentID + ";")[0];
		lastName = dbFriend.query1DstringRet("SELECT last_name FROM Children WHERE ID = " + currentID + ";")[0];
		String[][] pastInfo = dbFriend.query2DstringRet("SELECT year, grade_id, school_id, city_id, program_id FROM Program_Data WHERE child_id = " + currentID + ";", 5);
		if(pastInfo != null)          
		{
			int mostRecentYear = -1;
			int mostRecentYearIndex = -1;
			for(int a = 0; a < pastInfo.length; a++)
			{
				if(Integer.parseInt(pastInfo[a][0]) > mostRecentYear)
				{
					mostRecentYear = Integer.parseInt(pastInfo[a][0]);
					mostRecentYearIndex = a;
				}
			}
			String foundValue = gradesIDtoName.get(pastInfo[mostRecentYearIndex][1]);
			if(foundValue != null)
			{
				gradeBox.setSelectedItem(foundValue);
			}
			foundValue = schoolsIDtoName.get(pastInfo[mostRecentYearIndex][2]);
			if(foundValue != null)
			{
				schoolBox.setSelectedItem(foundValue);
			}
			foundValue = citiesIDtoName.get(pastInfo[mostRecentYearIndex][3]);
			if(foundValue != null)
			{
				cityBox.setSelectedItem(foundValue);
			}
			foundValue = programsIDtoName.get(pastInfo[mostRecentYearIndex][4]);
			if(foundValue != null)
			{
				programBox.setSelectedItem(foundValue);
			}
		}
		dispName.setText(firstName + " " + lastName);
	}
	

	/**
	 * Handles actions
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("Done"))
		{
			//TODO make sure there is a selected school if there is a selected grade
			Date d = new Date(System.currentTimeMillis());
			int year = d.getYear()+1900;
			String schoolID = schoolsNameToID.get((String)schoolBox.getSelectedItem());
			if(schoolID.equals("-1"))
			{
				schoolID = "null";
			}
			String cityID = citiesNameToID.get((String)cityBox.getSelectedItem());
			String gradeID = gradesNameToID.get((String)gradeBox.getSelectedItem());
			if(gradeID.equals("-1"))
			{
				gradeID = "null";
			}
			String programID = programsNamesToID.get((String)programBox.getSelectedItem());
			String verifyMessage = "Is this correct for " + firstName + " " + lastName + "\n"
			+ "School: " + (String) schoolBox.getSelectedItem() + "\n"
			+ "Grade: " + (String) gradeBox.getSelectedItem() + "\n"
			+ "Program: " + (String) programBox.getSelectedItem() + "\n"
			+ "City: " + (String) cityBox.getSelectedItem();
			int verifyValue = JOptionPane.showConfirmDialog(null, verifyMessage, "Verify Information", JOptionPane.YES_NO_OPTION);
			if(verifyValue == JOptionPane.YES_OPTION)
			{
				String birthday = dbFriend.query1DstringRet("SELECT date_of_birth FROM Children WHERE id = " + currentID + ";", 1)[0];
				int age = -1;
				if(!birthday.equals(""))
				{
					age = calculateAge(birthday, year);
				}
				dbFriend.executeUpdate("INSERT INTO Program_Data VALUES (" + currentID + "," + year + "," + gradeID + "," + schoolID + "," + cityID + "," + programID + "," + "0,false,false,false,false,false,false,false,false,false,false,false," + age + ");");
				dbFriend.executeUpdate("UPDATE Children SET most_recent_school = " + schoolID + ", most_recent_city = " + cityID + " WHERE id = " + currentID + ";");
				this.setVisible(false);
				dbcli.notifyChange();
				JOptionPane.showMessageDialog(null, firstName + " " + lastName + " registered.", "Reader Registered", JOptionPane.INFORMATION_MESSAGE);
				
			}
		}
		else if(e.getActionCommand().equals("Cancel"))
		{
			this.setVisible(false);
		}
	}

	/**
	 * Calculates the age of a person based on a set date
	 * @param bday Birthday
	 * @param year Year wanting to know age
	 * @return Age
	 */
	private int calculateAge(String bday, int year)
	{
		int output = -1;
		
		String[] brokenUpAge = bday.split("/");
		int kidMonth = Integer.parseInt(brokenUpAge[0]);
		int kidDay = Integer.parseInt(brokenUpAge[1]);
		int kidYear = Integer.parseInt(brokenUpAge[2]);
		
		output = year - kidYear;
		if(kidMonth > Constants.AGE_CALC_MONTH)
		{
			output -= 1;
		}
		else if(kidMonth == Constants.AGE_CALC_MONTH)
		{
			 if(kidDay > Constants.AGE_CALC_DAY)
			 {
				 output -= 1;
			 }
		}
		
		return output;
	}
	
	
	/**
	 * Updates the database connection
	 * @param dbh New database conneciton
	 */
	public void updateDatabaseConnection(DBHandler dbh)
	{
		dbFriend = dbh;
		schools = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS_ALPHA, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN);
		schoolsIDtoName = new Hashtable<String, String>();
		schoolsNameToID = new Hashtable<String, String>();
		for(int a = 0; a < schools.length; a++)
		{
			schoolsIDtoName.put(schools[a][0], schools[a][1]);
			schoolsNameToID.put(schools[a][1], schools[a][0]);
		}
		schoolsNameToID.put("None", "-1");
		grades = dbFriend.query2DstringRet(Queries.ALL_GRADES_PL_IDS, Queries.ALL_GRADES_PL_IDS_COL_LEN);
		gradesIDtoName = new Hashtable<String, String>();
		gradesNameToID = new Hashtable<String, String>();
		for(int a = 0; a < grades.length; a++)
		{
			gradesIDtoName.put(grades[a][0], grades[a][1]);
			gradesNameToID.put(grades[a][1], grades[a][0]);
		}
		gradesNameToID.put("None", "-1");
		String[][] gradeThing = {{"-1","None"}};
		grades = Constants.addCommonEntities(gradeThing, grades);
		cities = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS_ALPHA, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		citiesIDtoName = new Hashtable<String, String>();
		citiesNameToID = new Hashtable<String, String>();
		for(int a = 0; a < cities.length; a++)
		{
			citiesIDtoName.put(cities[a][0], cities[a][1]);
			citiesNameToID.put(cities[a][1], cities[a][0]);
		}
		schools = Constants.addCommonEntities(Constants.COMMON_SCHOOLS, schools);
		cities = Constants.addCommonEntities(Constants.COMMON_CITIES, cities);
		programs = dbFriend.query2DstringRet(Queries.ALL_PROGRAMS_PL_IDS, Queries.ALL_PROGRAMS_PL_IDS_COL_LEN);
		programsIDtoName = new Hashtable<String, String>();
		programsNamesToID = new Hashtable<String, String>();
		for(int a = 0; a < programs.length; a++)
		{
			programsIDtoName.put(programs[a][0], programs[a][1]);
			programsNamesToID.put(programs[a][1], programs[a][0]);
		}
		String[] schoolList = new String[schools.length];
		for(int a = 0; a < schools.length; a++)
		{
			schoolList[a] = schools[a][1];
		}
		schoolBox = new JComboBox<String>(schoolList);
		String[] gradeList = new String[grades.length];
		for(int a = 0; a < grades.length; a++)
		{
			gradeList[a] = grades[a][1];
		}
		gradeBox = new JComboBox<String>(gradeList);
		String[] cityList = new String[cities.length];
		for(int a = 0; a < cities.length; a++)
		{
			cityList[a] = cities[a][1];
		}
		cityBox = new JComboBox<String>(cityList);
		String[] programList = new String[programs.length];
		for(int a = 0; a < programList.length; a++)
		{
			programList[a] = programs[a][1];
		}
		programBox = new JComboBox<String>(programList);
		this.paintAll(this.getGraphics());
		//TODO may have to remove and re-add components 
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
	 * Toggles visibility
	 */
	public void setVisible(boolean b) 
	{
		super.setVisible(b);
		if(!b && schoolBox != null)
		{
			schoolBox.setSelectedIndex(0);
			gradeBox.setSelectedIndex(0);
			cityBox.setSelectedIndex(0);
			programBox.setSelectedIndex(0);
		}
	}
}
