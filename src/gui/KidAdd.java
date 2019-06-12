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
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import backend.*;

/**
 * Window for adding readers to the database
 * @author Anthony Schmitt
 *
 */
public class KidAdd extends JFrame implements ActionListener, KeyListener
{
	protected DBHandler dbFriend;
	protected JTextField firstName, lastName, parent, email;
	protected JFormattedTextField phoneNum;
	protected JComboBox<String> dobMonth, dobYear, dobDay, programBox, schoolBox, gradeBox, cityBox;
	protected String[] monthList = {"1 - January","2 - February","3 - March","4 - April","5 - May","6 - June","7 - July","8 - August","9 - September","10 - October","11 - November","12 - December"};
	protected String[] dayList = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	protected String[] yearList = new String[Constants.SIGN_UP_AGE_RANGE];
	protected int height, width;
	protected JCheckBox alsoRegister;
	private String[][] kids;
	private Hashtable<String,String> schoolHash, gradeHash, citiesHash, programsHash;
	private SecretMenuToggle mainWindow;
	private int year;
	protected DatabaseChangeListenerImplementer dbcli;
	
	/**
	 * Constructor
	 * @param dbh Database Handler
	 * @param mainWin Main window
	 * @param dbc DB change notifier
	 */
	public KidAdd(DBHandler dbh, SecretMenuToggle mainWin, DatabaseChangeListenerImplementer dbc)
	{
		super();
		this.dbFriend = dbh;
		this.dbcli = dbc;
		this.height = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4)*2.8);
		this.width = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4));
		this.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2.9), (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()/10)));;
		this.setSize(width,height);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setTitle("Add New Reader");
		init();
		this.mainWindow = mainWin;
		this.addKeyListener(this);
		this.setResizable(false);
	}
	
	/**
	 * Initializes the window
	 */
	protected void init()
	{

		String[][] schools = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS_ALPHA, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN);
		schoolHash = new Hashtable<String, String>();
		for(int a = 0; a < schools.length; a++)
		{
			schoolHash.put(schools[a][1], schools[a][0]);
		}
		schoolHash.put("None", "-1");
		schools = Constants.addCommonEntities(Constants.COMMON_SCHOOLS, schools);
		String[][] grades = dbFriend.query2DstringRet(Queries.ALL_GRADES_PL_IDS, Queries.ALL_GRADES_PL_IDS_COL_LEN);
		gradeHash = new Hashtable<String, String>();
		for(int a = 0; a < grades.length; a++)
		{
			gradeHash.put(grades[a][1], grades[a][0]);
		}
		gradeHash.put("None", "-1");
		String[][] noneEntry = {{"-1","None"}};
		grades = Constants.addCommonEntities(noneEntry, grades);
		String[][] cities = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS_ALPHA, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		citiesHash = new Hashtable<String, String>();
		for(int a = 0; a < cities.length; a++)
		{
			citiesHash.put(cities[a][1], cities[a][0]);
		}
		cities = Constants.addCommonEntities(Constants.COMMON_CITIES, cities);
		String[][] programs = dbFriend.query2DstringRet(Queries.ALL_PROGRAMS_PL_IDS, Queries.ALL_PROGRAMS_PL_IDS_COL_LEN);
		programsHash = new Hashtable<String, String>();
		for(int a = 0; a < programs.length; a++)
		{
			programsHash.put(programs[a][1], programs[a][0]);
		}
		kids = dbFriend.query2DstringRet(Queries.ALL_KIDS_INFO_PL_IDS, Queries.ALL_KIDS_INFO_PL_IDS_LEN);
		
		
		Date d = new Date(System.currentTimeMillis());
		year = d.getYear()+1900;
		for(int a = 0; a < Constants.SIGN_UP_AGE_RANGE; a++)
		{
			yearList[a] = "" + (year - a); 
		}
		this.firstName = new JTextField();
		this.lastName = new JTextField();
		try 
		{
			phoneNum = new JFormattedTextField(new MaskFormatter("###-###-####"));
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		this.parent = new JTextField();
		this.email = new JTextField();
		this.dobMonth = new JComboBox<String>(monthList);
		this.dobDay = new JComboBox<String>(dayList);
		this.dobYear = new JComboBox<String>(yearList);
		
		JLabel firstNameLabel = new JLabel("First Name:");
		JLabel lastNameLabel = new JLabel("Last Name:");
		JLabel monthLabel = new JLabel("Month:");
		JLabel dayLabel = new JLabel("Day:");
		JLabel yearLabel = new JLabel("Year:");
		JLabel dobLabel = new JLabel("Date of Birth");
		JLabel parentLabel = new JLabel("Parent/Guardian:");
		JLabel emailLabel = new JLabel("Email:");
		JLabel phoneLabel = new JLabel("Phone:");
		
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
		
		programBox.setEnabled(false);
		schoolBox.setEnabled(false);
		cityBox.setEnabled(false);
		gradeBox.setEnabled(false);
		
		JLabel cityLabel = new JLabel("City:");
		JLabel schoolLabel = new JLabel("School/Daycare:");
		JLabel gradeLabel = new JLabel("Grade:");
		JLabel programLabel = new JLabel("Program:");
		
		alsoRegister = new JCheckBox("Register new Reader for this year's program?");
		alsoRegister.addActionListener(this);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		double yweight = 0.0;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,10,0,0);
		mainPanel.add(lastNameLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,0,0,10);
		mainPanel.add(lastName,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,10,30,0);
		mainPanel.add(firstNameLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,0,30,10);
		mainPanel.add(firstName,c);
		
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		c.weightx = 0;
		c.weighty = yweight;
		c.insets = new Insets(0,30,10,0);
		//c.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(dobLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,10,0,0);
		mainPanel.add(monthLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,0,0,10);
		mainPanel.add(dobMonth,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,10,0,0);
		mainPanel.add(dayLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,0,0,10);
		mainPanel.add(dobDay,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,10,30,0);
		mainPanel.add(yearLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 5;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,0,30,10);
		mainPanel.add(dobYear,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 6;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,10,0,0);
		mainPanel.add(parentLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 6;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,0,0,10);
		mainPanel.add(parent,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 7;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,10,0,0);
		mainPanel.add(phoneLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 7;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,0,0,10);
		mainPanel.add(phoneNum,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 8;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,10,30,0);
		mainPanel.add(emailLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 8;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,0,30,10);
		mainPanel.add(email,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 2;
		c.weightx = 0;
		c.weighty = yweight;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0,20,10,0);
		mainPanel.add(alsoRegister,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 10;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,10,0,0);
		mainPanel.add(programLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 10;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,0,0,10);
		mainPanel.add(programBox,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 11;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,10,0,0);
		mainPanel.add(schoolLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 11;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,0,0,10);
		mainPanel.add(schoolBox,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 12;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,10,0,0);
		mainPanel.add(gradeLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 12;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,0,0,10);
		mainPanel.add(gradeBox,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 13;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,10,0,0);
		mainPanel.add(cityLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 13;
		c.weightx = 1;
		c.weighty = yweight;
		c.insets = new Insets(0,0,0,10);
		mainPanel.add(cityBox,c);
		
		
		JButton add = new JButton("Add Reader");
		add.addActionListener(this);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(cancel);
		buttonPanel.add(add);
		
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * For passing in a name if one was in the kid finder search box
	 * @param name Passed in name
	 */
	public void passInName(String name)
	{
		boolean lastFirst = false;
		boolean firstLast = false;
		if(name.contains(","))
		{
			lastFirst = true;
		}
		if(name.contains(" "))
		{
			firstLast = true;
		}
		if(lastFirst && firstLast)
		{
			lastFirst = false;
			firstLast = false;
		}
		if(lastFirst)
		{
			String[] broken = name.split(",");
			lastName.setText(broken[0].trim());
			if(broken.length > 1)
			{
				firstName.setText(broken[1].trim());
			}
		}
		if(firstLast)
		{
			String[] broken = name.split(" ");
			lastName.setText(broken[1].trim());
			if(broken.length > 1)
			{
				firstName.setText(broken[0].trim());
			}
		}
	}
	
	/**
	 * Action handler
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("Add Reader"))
		{
			String schoolIndex = schoolHash.get((String)schoolBox.getSelectedItem());
			if(schoolIndex.equals("-1"))
			{
				schoolIndex = "";
			}
			String cityIndex = citiesHash.get((String)cityBox.getSelectedItem());
			String[][] checkList = blockNames(firstName.getText().trim(), lastName.getText().trim());
			String[][] kidInfoList = new String[checkList.length-1][9];
			String[] checkingKid = new String[9];
			checkingKid[0] = "-1";
			checkingKid[1] = lastName.getText().trim();
			checkingKid[2] = firstName.getText().trim();
			checkingKid[3] = dobAsStr();
			checkingKid[4] = parent.getText().trim();
			checkingKid[5] = schoolIndex;
			checkingKid[6] = email.getText().trim();
			checkingKid[7] = phoneNum.getText();
			if(checkingKid[7].equals("   -   -    "))
			{
				checkingKid[7] = "";
			}
			checkingKid[8] = cityIndex;
			if(!alsoRegister.isSelected())
			{
				checkingKid[8] = "";
				checkingKid[5] = "";
			}
			
			boolean validInputs = true;
			String errorMessage = "";
			if(checkingKid[1].length() == 0)
			{
				validInputs = false;
				errorMessage += "No Last Name entered.\n";
			}
			if(checkingKid[2].length() == 0)
			{
				validInputs = false;
				errorMessage += "No First Name entered.\n";
			}
			if(!Constants.validDOB(checkingKid[3]))
			{
				validInputs = false;
				errorMessage += "Invalid Birth Date.\n";
			}
			//TODO if grade other than none is selected, make sure a school is selected
			if(validInputs)
			{
				int index = 0;
				for(int a = 0; a < checkList.length; a++)
				{
					if(!checkList[a][0].equals("-1"))
					{
						for(int b = 0; b < kids.length; b++)
						{
							if(checkList[a][0].equals(kids[b][0]))
							{
								kidInfoList[index][0] = kids[b][0]; //id
								kidInfoList[index][1] = kids[b][1]; //last name
								kidInfoList[index][2] = kids[b][2]; //first name
								kidInfoList[index][3] = kids[b][3]; //dob
								kidInfoList[index][4] = kids[b][4]; //parent
								kidInfoList[index][5] = kids[b][5]; //school id
								kidInfoList[index][6] = kids[b][6]; //email
								kidInfoList[index][7] = kids[b][7]; //phone
								kidInfoList[index][8] = kids[b][8]; //city id
								index++;
							}
						}
					}
				}
				ArrayList<String[]> potentialMatches = new ArrayList<String[]>();
				for(int b = 0; b < kidInfoList.length; b++) 
				{
					double lastNameSim = SimilarityMeasures.editDistance(checkingKid[1], kidInfoList[b][1]);
					double firstNameSim = SimilarityMeasures.editDistance(checkingKid[2], kidInfoList[b][2]);
					double dobSim = SimilarityMeasures.dobDiff(checkingKid[3], kidInfoList[b][3]);
					double parentSim = SimilarityMeasures.editDistance(checkingKid[4], kidInfoList[b][4]);
					double schoolSim = SimilarityMeasures.placeDifference(checkingKid[5], kidInfoList[b][5]);
					double emailSim = SimilarityMeasures.editDistance(checkingKid[6], kidInfoList[b][6]);
					double phoneNumberSim = SimilarityMeasures.editDistance(checkingKid[7], kidInfoList[b][7]);
					double citySim = SimilarityMeasures.placeDifference(checkingKid[8], kidInfoList[b][8]);
					
					double lastNameWeight = 0.2;
					double firstNameWeight = 0.2;
					double dobWeight = 0.2;
					double parentWeight = 0.5;
					double schoolWeight = 0.1;
					double emailWeight = 0.05;
					double phoneNumWeight = 0.05;
					double cityWeight = 0.15;
					
					
					/*for(int a = 0; a < 9; a++)
					{
						System.out.println(a);
						System.out.println(checkingKid[a]);
						System.out.println(kidInfoList[b][a]);
						System.out.println("______");
					}*/
					
					if(checkingKid[1].equals("") || kidInfoList[b][1].equals(""))
					{
						lastNameWeight = 0;
					}
					if(checkingKid[2].equals("") || kidInfoList[b][2].equals(""))
					{
						firstNameWeight = 0;
					}
					if(checkingKid[3].equals("") || kidInfoList[b][3].equals(""))
					{
						dobWeight = 0;
					}
					if(checkingKid[4].equals("") || kidInfoList[b][4].equals(""))
					{
						parentWeight = 0;
					}
					if(checkingKid[5].equals("") || kidInfoList[b][5].equals(""))
					{
						schoolWeight = 0;
					}
					if(checkingKid[6].equals("") || kidInfoList[b][6].equals(""))
					{
						emailWeight = 0;
					}
					if(checkingKid[7].equals("") || kidInfoList[b][7].equals(""))
					{
						phoneNumWeight = 0;
					}
					if(checkingKid[8].equals("") || kidInfoList[b][8].equals(""))
					{
						cityWeight = 0;
					}
					
					double totalWeight = lastNameWeight + firstNameWeight + dobWeight + parentWeight + schoolWeight + emailWeight + phoneNumWeight + cityWeight;
					
					double lastNameTotal = (lastNameSim*lastNameWeight);
					double firstNameTotal = (firstNameSim*firstNameWeight);
					double dobTotal = (dobSim*dobWeight);
					double parentTotal = (parentSim * parentWeight);
					double schoolTotal = (schoolSim * schoolWeight);
					double emailTotal = (emailSim * emailWeight); 
					double phoneTotal = (phoneNumberSim * phoneNumWeight);
					double cityTotal = (citySim * cityWeight);
					
					/*
					System.out.println(kidInfoList[b][0] + "\t" + kidInfoList[b][1] + "\t" + kidInfoList[b][2]);
					
					System.out.println("lastNameTotal: " + lastNameTotal);
					System.out.println("firstNameTotal: " + firstNameTotal);
					System.out.println("dobTotal: " + dobTotal);
					System.out.println("parentsTotal: " + parentTotal);
					System.out.println("schoolTotal: " + schoolTotal);
					System.out.println("emailTotal: " + emailTotal);
					System.out.println("phoneTotal: " + phoneTotal);
					System.out.println("cityTotal: " + cityTotal);
					*/
					
					double totalSim = lastNameTotal + firstNameTotal + dobTotal + parentTotal + schoolTotal 
							+ emailTotal + phoneTotal + cityTotal;
					
					//System.out.println("total: " + totalSim);
					
					totalSim = totalSim/totalWeight;
					
					/*
					System.out.println("TotalWeight: " + totalWeight);
					System.out.println("adjusted total: " + totalSim);
					System.out.println();*/
					
					if(totalSim > 0.85)
					{
						/*System.out.println(totalSim);
						System.out.println(kidInfoList[b][0] + "    " + kidInfoList[b][2] + "    " + kidInfoList[b][1]);*/
						potentialMatches.add(kidInfoList[b]);
					}
				}
				
				JLabel kidsMessage = new JLabel("Are you sure that the entered Reader is not one of these already in the database?");
				/*for(int a = 0; a < potentialMatches.size(); a++)
				{
					kidsMessage += potentialMatches.get(a)[2] + "     " + potentialMatches.get(a)[1] + "     " + potentialMatches.get(a)[3] + "\n";
				}*/
				JRadioButton[] choices = new JRadioButton[potentialMatches.size()+1];
				ButtonGroup buttonGroup = new ButtonGroup();
				for(int a = 0; a < choices.length; a++)
				{
					choices[a] = new JRadioButton();
					buttonGroup.add(choices[a]);
				}
				JLabel[] potentialMatchList = new JLabel[potentialMatches.size()+1];
				potentialMatchList[0] = new JLabel("The entered Reader is NOT listed below.");
				for(int a = 1; a < potentialMatchList.length; a++)
				{
					potentialMatchList[a] = new JLabel(potentialMatches.get(a-1)[2] + "     " + potentialMatches.get(a-1)[1] + "     " + potentialMatches.get(a-1)[3]);
				}
				choices[0].setSelected(true);
				JPanel listPanel = new JPanel();
				listPanel.setLayout(new GridLayout(potentialMatchList.length,1));
				for(int a = 0; a < potentialMatchList.length; a++)
				{
					JPanel rowPanel = new JPanel();
					rowPanel.setLayout(new BorderLayout());
					rowPanel.add(choices[a],BorderLayout.WEST);
					rowPanel.add(potentialMatchList[a], BorderLayout.CENTER);
					listPanel.add(rowPanel);
				}
				JPanel mainPanel = new JPanel();
				mainPanel.setLayout(new BorderLayout());
				mainPanel.add(kidsMessage, BorderLayout.NORTH);
				mainPanel.add(listPanel, BorderLayout.CENTER);
				
				int verifyValue = JOptionPane.showConfirmDialog(null, mainPanel, "Add Reader Confirmation", JOptionPane.OK_CANCEL_OPTION);
				if(verifyValue == JOptionPane.OK_OPTION)
				{
					int selectedChoice = -1;
					for(int a = 0; a < choices.length; a++)
					{
						if(choices[a].isSelected())
						{
							selectedChoice = a;
						}
					}
					if(selectedChoice == 0)
					{
						String idHold = dbFriend.query1DstringRet(Queries.HIGHEST_CHILD_ID)[0];
						int newID = Integer.parseInt(idHold) + 1;
						if(checkingKid[4].equals(""))
						{
							checkingKid[4] = "null";
						}
						else
						{
							checkingKid[4] = "\"" + checkingKid[4] + "\"";
						}
						if(checkingKid[5].equals(""))
						{
							checkingKid[5] = "null";
						}
						if(checkingKid[6].equals(""))
						{
							checkingKid[6] = "null";
						}
						else
						{
							checkingKid[6] = "\"" + checkingKid[6] + "\"";
						}
						if(checkingKid[7].equals(""))
						{
							checkingKid[7] = "null";
						}
						else
						{
							checkingKid[7] = "\"" + checkingKid[7] + "\"";
						}
						if(checkingKid[8].equals(""))
						{
							checkingKid[8] = "null";
						}
						//System.out.println("INSERT INTO Children VALUES (" + newID + ",\""+checkingKid[1]+"\",\""+checkingKid[2]+"\",\""+checkingKid[3]+"\","+checkingKid[4]+","+checkingKid[5]+","+checkingKid[6]+","+checkingKid[7]+","+checkingKid[8]+");");
						dbFriend.executeUpdate("INSERT INTO Children VALUES (" + newID + ",\""+checkingKid[1]+"\",\""+checkingKid[2]+"\",\""+checkingKid[3]+"\","+checkingKid[4]+","+checkingKid[5]+","+checkingKid[6]+","+checkingKid[7]+","+checkingKid[8]+");");
						if(alsoRegister.isSelected())
						{
							String grade_id = gradeHash.get((String)gradeBox.getSelectedItem());
							if(grade_id.equals("-1"))
							{
								grade_id = "null";
							}
							String school_id = checkingKid[5];
							String city_id = checkingKid[8];
							String program_id = programsHash.get((String)programBox.getSelectedItem());
							String ageCalc = Constants.calcAgeForDB(checkingKid[3],year);
							dbFriend.executeUpdate("INSERT INTO Program_Data VALUES (" + newID + "," + year + "," + grade_id + "," + school_id + "," + city_id + "," + program_id + ",0,false,false,false,false,false,false,false,false,false,false,false," + ageCalc + ");");
						}
						dbcli.notifyChange();
						firstName.setText("");
						lastName.setText("");
						parent.setText("");
						email.setText("");
						phoneNum.setText("");
						dobMonth.setSelectedIndex(0);
						dobYear.setSelectedIndex(0);
						dobDay.setSelectedIndex(0);
						programBox.setSelectedIndex(0);
						schoolBox.setSelectedIndex(0);
						gradeBox.setSelectedIndex(0);
						cityBox.setSelectedIndex(0);
						alsoRegister.setSelected(false);
						this.setVisible(false);
						if(alsoRegister.isSelected())
						{
							JOptionPane.showMessageDialog(null, checkingKid[2] + " " + checkingKid[1] + " was added and registered.", "Reader Added and Registered", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							JOptionPane.showMessageDialog(null, checkingKid[2] + " " + checkingKid[1] + " was added.", "Reader Added", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else
					{	
						String usedID = potentialMatches.get(selectedChoice)[0];
						if(alsoRegister.isSelected())
						{
							String grade_id = gradeHash.get((String)gradeBox.getSelectedItem());
							String school_id = checkingKid[5];
							String city_id = checkingKid[8];
							String program_id = programsHash.get((String)programBox.getSelectedItem());
							String ageCalc = Constants.calcAgeForDB(checkingKid[3],year);
							dbFriend.executeUpdate("INSERT INTO Program_Data VALUES (" + usedID + "," + year + "," + grade_id + "," + school_id + "," + city_id + "," + program_id + ",0,false,false,false,false,false,false,false,false,flase,false,false," + ageCalc + ");");
							JOptionPane.showMessageDialog(null, potentialMatches.get(selectedChoice)[2] + " " + potentialMatches.get(selectedChoice)[1] + " was registered.", "Reader Registered", JOptionPane.INFORMATION_MESSAGE);
							dbcli.notifyChange();
						}
						else
						{
							JOptionPane.showMessageDialog(null, "OK", "No Action Required", JOptionPane.INFORMATION_MESSAGE);
						}
						firstName.setText("");
						lastName.setText("");
						parent.setText("");
						email.setText("");
						phoneNum.setText("");
						dobMonth.setSelectedIndex(0);
						dobYear.setSelectedIndex(0);
						dobDay.setSelectedIndex(0);
						programBox.setSelectedIndex(0);
						schoolBox.setSelectedIndex(0);
						gradeBox.setSelectedIndex(0);
						cityBox.setSelectedIndex(0);
						alsoRegister.setSelected(false);
						this.setVisible(false);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, errorMessage, "Cannot Add Reader", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		if(e.getActionCommand().equals("Cancel"))
		{
			firstName.setText("");
			lastName.setText("");
			parent.setText("");
			email.setText("");
			phoneNum.setText("");
			dobMonth.setSelectedIndex(0);
			dobYear.setSelectedIndex(0);
			dobDay.setSelectedIndex(0);
			programBox.setSelectedIndex(0);
			schoolBox.setSelectedIndex(0);
			gradeBox.setSelectedIndex(0);
			cityBox.setSelectedIndex(0);
			alsoRegister.setSelected(false);
			this.setVisible(false);
		}
		if(alsoRegister.isSelected())
		{
			cityBox.setEnabled(true);
			gradeBox.setEnabled(true);
			schoolBox.setEnabled(true);
			programBox.setEnabled(true);
		}
		else
		{
			cityBox.setEnabled(false);
			gradeBox.setEnabled(false);
			schoolBox.setEnabled(false);
			programBox.setEnabled(false);
		}
	}

	/**
	 * Generates blocking key for names and gives the block to check against
	 * @param firstName Entered First Name
	 * @param lastName Entered Last Name
	 * @return Block of Constants.BLOCKING_WINDOW_SIZE with the passed in name in the center, or as close to it as possible 
	 */
	private String[][] blockNames(String firstName, String lastName)
	{
		String[][] output = new String[Constants.BLOCKING_WINDOW_SIZE][2];
		String[][] full = new String[kids.length+1][2];
		full[0][0] = "-1";
		full[0][1] = generateBlockingKey(firstName.toLowerCase(),lastName.toLowerCase());
		for(int a = 1; a < kids.length+1; a++)
		{
			full[a][0] = kids[a-1][0];
			full[a][1] = generateBlockingKey(kids[a-1][2].toLowerCase(),kids[a-1][1].toLowerCase());
		}
		StringArrayCompare compair = new StringArrayCompare();
		Arrays.sort(full, compair);
		int index = -1;
		for(int a = 0; a < full.length; a++)
		{
			if(full[a][0].equals("-1"))
			{
				index = a;
			}
		}
		int startIndex = index - ((Constants.BLOCKING_WINDOW_SIZE-1)/2);//assumes odd number
		if(startIndex < 0)
		{
			startIndex = 0;
		}
		int endIndex = startIndex + Constants.BLOCKING_WINDOW_SIZE-1;
		if(endIndex >= full.length)
		{
			endIndex = full.length -1;
			startIndex = endIndex - (Constants.BLOCKING_WINDOW_SIZE-1);
		}
		index = 0;
		for(int a = startIndex; a <= endIndex; a++)
		{
			output[index][0] = full[a][0];
			output[index][1] = full[a][1];
			index++;
		}
		return output;
	}
	
	/**
	 * Generates a blocking key from a given name
	 * @param first First Name
	 * @param last Last Name
	 * @return Blocking Key
	 */
	public String generateBlockingKey(String first, String last)
	{
		String output = "";
		int index = 0;
		while(output.length() < 3 & index < last.length())
		{
			char currentChar = last.toLowerCase().charAt(index++);
			if(!Character.toString(currentChar).matches("[aeiou .:,;'0123456789]") )
			{
				output += currentChar;
			}
		}
		index = 0;
		while(output.length() < 6 & index < first.length())
		{
			char currentChar = first.toLowerCase().charAt(index++);
			if(!Character.toString(currentChar).matches("[aeiou .:,;'0123456789]") )
			{
				output += currentChar;
			}
		}
		return output;
	}
	
	//TODO deal with -1 for nones
	
	/**
	 * Updates the database connection.
	 * @param dbh Database connection
	 */
	public void updateDatabaseConnection(DBHandler dbh)
	{
		this.dbFriend = dbh;
		String[][] schools = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS_ALPHA, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN);
		schoolHash = new Hashtable<String, String>();
		for(int a = 0; a < schools.length; a++)
		{
			schoolHash.put(schools[a][1], schools[a][0]);
		}
		schoolHash.put("None", "-1");
		schools = Constants.addCommonEntities(Constants.COMMON_SCHOOLS, schools);
		String[][] grades = dbFriend.query2DstringRet(Queries.ALL_GRADES_PL_IDS, Queries.ALL_GRADES_PL_IDS_COL_LEN);
		gradeHash = new Hashtable<String, String>();
		for(int a = 0; a < grades.length; a++)
		{
			gradeHash.put(grades[a][1], grades[a][0]);
		}
		gradeHash.put("None", "-1");
		String[][] noneEntry = {{"-1","None"}};
		grades = Constants.addCommonEntities(noneEntry, grades);
		String[][] cities = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS_ALPHA, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		citiesHash = new Hashtable<String, String>();
		for(int a = 0; a < cities.length; a++)
		{
			citiesHash.put(cities[a][1], cities[a][0]);
		}
		cities = Constants.addCommonEntities(Constants.COMMON_CITIES, cities);
		String[][] programs = dbFriend.query2DstringRet(Queries.ALL_PROGRAMS_PL_IDS, Queries.ALL_PROGRAMS_PL_IDS_COL_LEN);
		programsHash = new Hashtable<String, String>();
		for(int a = 0; a < programs.length; a++)
		{
			programsHash.put(programs[a][1], programs[a][0]);
		}
		kids = dbFriend.query2DstringRet(Queries.ALL_KIDS_INFO_PL_IDS, Queries.ALL_KIDS_INFO_PL_IDS_LEN);
		
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
		
		programBox.setEnabled(false);
		schoolBox.setEnabled(false);
		cityBox.setEnabled(false);
		gradeBox.setEnabled(false);
		
		//(seems not) may have to remove then re add these to their parents
	}
	
	/**
	 * Handles typed keys
	 */
	public void keyTyped(KeyEvent e) 
	{
	}

	/**
	 * Handles pressed keys
	 */
	public void keyPressed(KeyEvent e) 
	{
	}

	/**
	 * Handles released keys
	 */
	public void keyReleased(KeyEvent e) 
	{
		if((e.getKeyCode() == KeyEvent.VK_L) &(e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK))
		{
			mainWindow.secretMenuKeyboardPress();
		}
	}
	
	/**
	 * Turns the Date of Birth into a string
	 * @return Date of Birth as String
	 */
	private String dobAsStr()
	{
		//TODO isn't there a Constants function for this?
		String output = "";
		int month = dobMonth.getSelectedIndex()+1;
		String day = (String)dobDay.getSelectedItem();
		String year = (String)dobYear.getSelectedItem();
		output += month + "/" + day + "/" + year; 
		return output;
	}
	
	
}


