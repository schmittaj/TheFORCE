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
import java.util.*;
import java.awt.event.*;
import java.beans.*;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.MaskFormatter;

import backend.*;

/**
 * This window is for finding readers via a table and search bar.
 * @author Anthony Schmitt
 *
 */
public class KidFind extends JInternalFrame implements ListSelectionListener, KeyListener, DatabaseChangeListener, ActionListener, MouseListener
{
	protected MyTableModel model;
	protected JTable theTable;
	private String phoneFilter, schoolFilter, cityFilter, DOBFilterMonth, DOBFilterDay, DOBFilterYear;
	private static String[] columns = {"Last Name","First Name","DOB","Most Recent Phone Number","Most Recent School","Most Recent City"};
	protected JScrollPane mainView;
	private int height;
	private int width;
	private int lastSelectedRow = -1;
	private int selectedRow = -1;
	private DBHandler dbFriend;
	protected JTextField nameIn, lastNameField, firstNameField;
	protected String oldField = "", oldLast, oldFirst;;
	protected String[][] allKids;
	//protected String[] allKidsIDs;
	protected Hashtable<String,String> kidIdHash, schoolHash, cityHash;
	protected String[][] currentList, nameLimitedList;
	//protected String[] currentListIDs;
	protected KidView viewer;
	protected DatabaseChangeListenerImplementer dbcli;
	protected JButton addReader;
	protected KidAdd addKid;
	//protected boolean keyPressed;
	private SecretMenuToggle mainWindow;
	private String lastFirstName, lastLastName,	lastDOB;
	private JButton advSearch;
	protected JPanel namePanel, advSearchPanel;
	protected JFormattedTextField phoneNum;
	private boolean advSearchMode;
	protected JComboBox<String> dayBox, monthBox, yearBox, schoolBox, cityBox;
	
	/**
	 * Constructor
	 * @param dbin Database Connection
	 * @param dbc DB change listener handler
	 * @param mainWin Main Window
	 */
	public KidFind(DBHandler dbin, DatabaseChangeListenerImplementer dbc, SecretMenuToggle mainWin)
	{
		super("",true,true,true,true);
		//this.keyPressed = false;
		this.height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()-100);
		this.width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 50);
		this.setLayout(new BorderLayout());
		this.dbFriend = dbin;
		this.dbcli = dbc;
		init();
		this.mainWindow = mainWin;
		this.viewer = new KidView(dbFriend, dbcli, mainWindow);
		this.viewer.setVisible(false);
		dbcli.addListener(viewer);
		this.addKid = new KidAdd(dbFriend, mainWindow, dbcli);
		addKid.setVisible(false);
		this.setTitle("Find Readers");
	}
	
	/**
	 * Initializes the object
	 */
	protected void init()
	{
		this.lastFirstName = "";
		this.lastLastName = "";
		this.oldLast = "";
		this.oldFirst = "";
		this.lastDOB = "";
		this.advSearchMode = false;
		this.phoneFilter = "";
		this.schoolFilter = "";
		this.cityFilter = "";
		this.DOBFilterMonth = "";
		this.DOBFilterDay = "";
		this.DOBFilterYear = "";
		setUpTable();
		
		advSearch = new JButton("Advanced Search");
		advSearch.addActionListener(this);
		this.nameIn = new JTextField();
		nameIn.setFocusable(true);
		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD,16));
		
		namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.add(nameLabel, BorderLayout.WEST);
		namePanel.add(nameIn, BorderLayout.CENTER);
		namePanel.add(advSearch, BorderLayout.EAST);
		namePanel.setPreferredSize(new Dimension(500,40));
		this.add(namePanel, BorderLayout.NORTH);
		this.add(mainView, BorderLayout.CENTER);
		this.addReader = new JButton("Add New Reader");
		addReader.setPreferredSize(new Dimension(500,40));
		addReader.addActionListener(this);
		nameIn.addKeyListener(this);
		this.add(addReader, BorderLayout.SOUTH);
		
		advSearchPanel = new JPanel();
		advSearchPanel.setPreferredSize(new Dimension(500,80));
		lastNameField = new JTextField();
		lastNameField.addKeyListener(this);
		JLabel lastNameLabel = new JLabel("Last Name:");
		JPanel lastNamePanel = new JPanel();
		lastNamePanel.setLayout(new BorderLayout());
		lastNamePanel.add(lastNameLabel, BorderLayout.WEST);
		lastNamePanel.add(lastNameField, BorderLayout.CENTER);
		
		firstNameField = new JTextField();
		firstNameField.addKeyListener(this);
		JLabel firstNameLabel = new JLabel("First Name:");
		JPanel firstNamePanel = new JPanel();
		firstNamePanel.setLayout(new BorderLayout());
		firstNamePanel.add(firstNameLabel, BorderLayout.WEST);
		firstNamePanel.add(firstNameField, BorderLayout.CENTER);
		
		
		String[] monthList = {"<select>","<none>","1 - January","2 - February","3 - March","4 - April","5 - May","6 - June","7 - July","8 - August","9 - September","10 - October","11 - November","12 - December"};
		String[] dayList = {"<select>","<none>","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
		String[] yearList = new String[Constants.GENERAL_YEAR_RANGE+2];
		yearList[0] = "<select>";
		yearList[1] = "<none>";
		
		Date d = new Date(System.currentTimeMillis());
		int year = d.getYear()+1900;
		for(int a = 2; a < Constants.GENERAL_YEAR_RANGE+2; a++)
		{
			yearList[a] = "" + (year - a); 
		}
		
		dayBox = new JComboBox<String>(dayList);
		dayBox.addActionListener(this);
		monthBox = new JComboBox<String>(monthList);
		monthBox.addActionListener(this);
		yearBox = new JComboBox<String>(yearList);
		yearBox.addActionListener(this);
		JLabel dobLabel = new JLabel("DOB:");
		JPanel dobPartPanel = new JPanel();
		dobPartPanel.setLayout(new GridLayout(1,3));
		dobPartPanel.add(monthBox);
		dobPartPanel.add(dayBox);
		dobPartPanel.add(yearBox);
		JPanel dobPanel = new JPanel();
		dobPanel.setLayout(new BorderLayout());
		dobPanel.add(dobLabel, BorderLayout.WEST);
		dobPanel.add(dobPartPanel, BorderLayout.CENTER);
		
		phoneNum = null;
		JLabel phoneLabel = new JLabel("Phone:");
		try 
		{
			phoneNum = new JFormattedTextField(new MaskFormatter("###-###-####"));
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		phoneNum.addKeyListener(this);
		JPanel phonePanel = new JPanel();
		phonePanel.setLayout(new BorderLayout());
		phonePanel.add(phoneLabel, BorderLayout.WEST);
		phonePanel.add(phoneNum, BorderLayout.CENTER);
		
		String[][] cityList = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS_ALPHA, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		cityHash = new Hashtable<String,String>(); //Do we need this even?
		for(int a = 0; a < cityList.length; a++)
		{
			cityHash.put(cityList[a][1],cityList[a][0]);
		}
		cityHash.put("-2","<select>");
		cityHash.put("-1","None");
		String[][] common_cities = {{"-2","<select>"},{"-1","None"},{"46","Sun Prairie"}};
		cityList = Constants.addCommonEntities(common_cities, cityList);
		String[] citiesUse = new String[cityList.length];
		for(int a = 0; a < cityList.length; a++)
		{
			citiesUse[a] = cityList[a][1];
		}
		cityBox = new JComboBox<String>(citiesUse);
		cityBox.addActionListener(this);
		JLabel cityLabel = new JLabel("City:");
		JPanel cityPanel = new JPanel();
		cityPanel.setLayout(new BorderLayout());
		cityPanel.add(cityLabel, BorderLayout.WEST);
		cityPanel.add(cityBox, BorderLayout.CENTER);
		
		String[][] schoolList = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS_ALPHA, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN);
		schoolHash = new Hashtable<String,String>(); //Do we need this even?
		for(int a = 0; a < schoolList.length; a++)
		{
			schoolHash.put(schoolList[a][1],schoolList[a][0]);
		}
		schoolHash.put("-2","<select>");
		schoolHash.put("-1","None");
		String[][] common_schools = 
			{{"-2","<select>"},
			{"-1","None"},
			{"33","CH Bird Elementary"},
			{"59","Creekside Elementary"},
			{"82","Eastside Elementary"},
			{"125","Horizon Elementary"},
			{"200","Meadow View Elementary"},
			{"207","Northside Elementary"},
			{"251","Royal Oaks Elementary"},
			{"294","Token Springs Elementary"},
			{"304","Westside Elementary"},
			{"218","Patrick Marsh Middle School"},
			{"233","Prairie View Middle School"},
			{"30","Cardinal Heights Upper Middle School"},
			{"283","Sun Prairie High School"}};
		schoolList = Constants.addCommonEntities(common_schools, schoolList);
		String[] schoolsUse = new String[schoolList.length];
		for(int a = 0; a < schoolList.length; a++)
		{
			schoolsUse[a] = schoolList[a][1];
		}
		schoolBox = new JComboBox<String>(schoolsUse);
		schoolBox.addActionListener(this);
		JLabel schoolLabel = new JLabel("School:");
		JPanel schoolPanel = new JPanel();
		schoolPanel.setLayout(new BorderLayout());
		schoolPanel.add(schoolLabel, BorderLayout.WEST);
		schoolPanel.add(schoolBox, BorderLayout.CENTER);
		
		JPanel advMostPanel = new JPanel();
		advMostPanel.setLayout(new GridLayout(3,2));
		advMostPanel.add(firstNamePanel);
		advMostPanel.add(lastNamePanel);
		advMostPanel.add(dobPanel);
		advMostPanel.add(phonePanel);
		advMostPanel.add(schoolPanel);
		advMostPanel.add(cityPanel);
		
		JButton clear = new JButton("Clear");
		clear.addActionListener(this);
		JButton basic = new JButton("Basic Search");
		basic.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1));
		buttonPanel.add(clear);
		buttonPanel.add(basic);
		
		advSearchPanel.setLayout(new BorderLayout());
		advSearchPanel.add(advMostPanel, BorderLayout.CENTER);
		advSearchPanel.add(buttonPanel, BorderLayout.EAST);
		
		//last name, first name, dob, phone, school, city
		
		this.setSize(new Dimension(width,height));
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * Sets up our reader table
	 */
	protected void setUpTable() 
	{
		String[][] data = {{"","","","","",""}};

		data = dbFriend.query2DstringRet(Queries.ALL_KIDS_INFO_PL_IDS,Queries.ALL_KIDS_INFO_PL_IDS_LEN);
		
		kidIdHash = new Hashtable<String,String>();
		this.allKids = new String[data.length][data[0].length-1];
		for(int a = 0; a < data.length; a++)
		{
			kidIdHash.put(data[a][1]+" "+data[a][2]+" "+data[a][3], data[a][0]);
			this.allKids[a][0] = data[a][1];
			this.allKids[a][1] = data[a][2];
			this.allKids[a][2] = data[a][3];
			this.allKids[a][3] = data[a][7];
			this.allKids[a][4] = data[a][5];
			this.allKids[a][5] = data[a][8];
		}
		
		swapReferences();
		this.currentList = allKids.clone();
		model = new MyTableModel(columns,allKids);
				
		theTable = new JTable();
		theTable.setModel(model);
		theTable.setRowSorter(new TableRowSorter(model));
		mainView = new JScrollPane(theTable);

		theTable.setFillsViewportHeight(true);
		theTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		theTable.getSelectionModel().addListSelectionListener(this);
		theTable.addMouseListener(this);
	}

	/**
	 * Handles selection in the table
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		lastSelectedRow = selectedRow;
		selectedRow = theTable.getSelectedRow();
		if(selectedRow == -1)
		{
			selectedRow = lastSelectedRow;
		}
		else //need this here to prevent every key press from opening a new window
		{
	
			boolean sameKid = false;
			if(lastFirstName.equals(theTable.getValueAt(selectedRow,0)) && lastLastName.equals(theTable.getValueAt(selectedRow,1)) && lastDOB.equals(theTable.getValueAt(selectedRow,2)) )
			{
				sameKid = true;
			}
			if(!sameKid)
			{
				viewer.setReader(kidIdHash.get(theTable.getValueAt(selectedRow,0)+" "+theTable.getValueAt(selectedRow,1)+" "+theTable.getValueAt(selectedRow,2)));
				lastFirstName = (String)theTable.getValueAt(selectedRow,0);
				lastLastName = (String)theTable.getValueAt(selectedRow,1);
				lastDOB = (String)theTable.getValueAt(selectedRow,2);
				viewer.makeVisible();
			}
		}
	}
	
	
	/**
	 * Updates the list of readers.
	 * @param areaUpdated 0 for name, 1 for phone number, 2 for DOB month, 3 for DOB day, 4 for DOB year, 5 for school, 6 for city, anything else for just refresh
	 * @param filterChange Change to filter
	 */
	public void updateList(int areaUpdated, String filterChange)
	{
		if(nameLimitedList != null)
		{
			currentList = nameLimitedList.clone();
		}
		
		if(areaUpdated == 1)
		{
			phoneFilter = filterChange;
		}
		else if(areaUpdated == 2)
		{
			if(filterChange.equals("<none>"))
			{
				DOBFilterMonth = "none";
			}
			else if(filterChange.equals("<select>"))
			{
				DOBFilterMonth = "";
			}
			else
			{
				String[] monthParts = filterChange.split(" ");
				DOBFilterMonth = monthParts[0];
			}
		}
		else if(areaUpdated == 3)
		{
			DOBFilterDay = filterChange;
			if(filterChange.equals("<select>"))
			{
				DOBFilterDay = "";
			}
			else if(filterChange.equals("<none>"))
			{
				DOBFilterDay = "none";
			}
		}
		else if(areaUpdated == 4)
		{
			DOBFilterYear = filterChange;
			if(filterChange.equals("<select>"))
			{
				DOBFilterYear = "";
			}
			if(filterChange.equals("<none>"))
			{
				DOBFilterYear = "none";
			}
		}
		else if(areaUpdated == 5)
		{
			schoolFilter = filterChange;
			if(filterChange.equals("<select>"))
			{
				schoolFilter = "";
			}
		}
		else if(areaUpdated == 6)
		{
			cityFilter = filterChange;
			if(filterChange.equals("<select>"))
			{
				cityFilter = "";
			}
		}
		
		if(!phoneFilter.equals(""))
		{
			ArrayList<String[]> goodList = new ArrayList<String[]>();
			for(int a = 0; a < currentList.length; a++)
			{
				if(currentList[a][3].startsWith(phoneFilter))
				{
					goodList.add(currentList[a]);
				}
			}
			
			currentList = new String[goodList.size()][6];
			for(int a = 0; a < goodList.size(); a++)
			{
				currentList[a] = goodList.get(a);
			}
		}
		
		if(!schoolFilter.equals(""))
		{
			ArrayList<String[]> goodList = new ArrayList<String[]>();
			if(schoolFilter.equals("None"))
			{
				for(int a = 0; a < currentList.length; a++)
				{
					if(currentList[a][4].equals(""))
					{
						goodList.add(currentList[a]);
					}
				}
			}
			else
			{
				for(int a = 0; a < currentList.length; a++)
				{
					if(currentList[a][4].equals(schoolFilter))
					{
						goodList.add(currentList[a]);
					}
				}
			}
			
			currentList = new String[goodList.size()][6];
			for(int a = 0; a < goodList.size(); a++)
			{
				currentList[a] = goodList.get(a);
			}
		}
		
		if(!cityFilter.equals(""))
		{
			ArrayList<String[]> goodList = new ArrayList<String[]>();
			if(cityFilter.equals("None"))
			{
				for(int a = 0; a < currentList.length; a++)
				{
					if(currentList[a][5].equals(""))
					{
						goodList.add(currentList[a]);
					}
				}
			}
			else
			{
				for(int a = 0; a < currentList.length; a++)
				{
					if(currentList[a][5].equals(cityFilter))
					{
						goodList.add(currentList[a]);
					}
				}
			}
			
			currentList = new String[goodList.size()][6];
			for(int a = 0; a < goodList.size(); a++)
			{
				currentList[a] = goodList.get(a);
			}
		}
		
		
		if(!DOBFilterMonth.equals("") && !DOBFilterMonth.equals("none"))
		{
			ArrayList<String[]> goodList = new ArrayList<String[]>();
			
			for(int a = 0; a < currentList.length; a++)
			{
				if(!currentList[a][2].equals(""))
				{
					String[] dateSplit = currentList[a][2].split("/");
					if(dateSplit[0].equals(DOBFilterMonth))
					{
						goodList.add(currentList[a]);
					}
				}
			}
			
			currentList = new String[goodList.size()][6];
			for(int a = 0; a < goodList.size(); a++)
			{
				currentList[a] = goodList.get(a);
			}
		}
		
		if(!DOBFilterDay.equals("") && !DOBFilterDay.equals("none"))
		{
			ArrayList<String[]> goodList = new ArrayList<String[]>();
			
			for(int a = 0; a < currentList.length; a++)
			{
				if(!currentList[a][2].equals(""))
				{
					String[] dateSplit = currentList[a][2].split("/");
					if(dateSplit[1].equals(DOBFilterDay))
					{
						goodList.add(currentList[a]);
					}
				}
			}
			
			currentList = new String[goodList.size()][6];
			for(int a = 0; a < goodList.size(); a++)
			{
				currentList[a] = goodList.get(a);
			}
		}
		
		if(!DOBFilterYear.equals("") && !DOBFilterYear.equals("none"))
		{
			ArrayList<String[]> goodList = new ArrayList<String[]>();
			
			for(int a = 0; a < currentList.length; a++)
			{
				if(!currentList[a][2].equals(""))
				{
					String[] dateSplit = currentList[a][2].split("/");
					if(dateSplit[2].equals(DOBFilterYear))
					{
						goodList.add(currentList[a]);
					}
				}
			}
			
			currentList = new String[goodList.size()][6];
			for(int a = 0; a < goodList.size(); a++)
			{
				currentList[a] = goodList.get(a);
			}
		}
		
		if(DOBFilterMonth.equals("none")  && DOBFilterDay.equals("none")  && DOBFilterYear.equals("none"))
		{
			ArrayList<String[]> goodList = new ArrayList<String[]>();
			
			for(int a = 0; a < currentList.length; a++)
			{
				if(currentList[a][2].equals(""))
				{
					goodList.add(currentList[a]);	
				}
			}
			
			currentList = new String[goodList.size()][6];
			for(int a = 0; a < goodList.size(); a++)
			{
				currentList[a] = goodList.get(a);
			}
		}
		
		if(currentList == null || currentList.length == 0)
		{
			String[][] blank = {{"","","","","",""}};
			currentList = blank;
		}
		
		model = new MyTableModel(columns,currentList);
		model.fireTableDataChanged();
		theTable.setModel(model);
		theTable.setRowSorter(new TableRowSorter(model));
	}
	
	
	/**
	 * Updates the whole window.
	 */
	public void fullUpdate()
	{
		//
		lastLastName = "";
		lastFirstName = "";
		//
		String[][] data = dbFriend.query2DstringRet(Queries.ALL_KIDS_INFO_PL_IDS,Queries.ALL_KIDS_INFO_PL_IDS_LEN);
		kidIdHash = new Hashtable<String,String>();
		this.allKids = new String[data.length][data[0].length-1];
		for(int a = 0; a < data.length; a++)
		{
			kidIdHash.put(data[a][1]+" "+data[a][2]+" "+data[a][3], data[a][0]);
			this.allKids[a][0] = data[a][1];
			this.allKids[a][1] = data[a][2];
			this.allKids[a][2] = data[a][3];
			this.allKids[a][3] = data[a][7];
			this.allKids[a][4] = data[a][5];
			this.allKids[a][5] = data[a][8];
		}
		swapReferences();
		this.currentList = allKids.clone();
		model = new MyTableModel(columns,allKids);
		model.fireTableDataChanged();	
		theTable.setModel(model);
		this.paintAll(this.getGraphics());
		if(advSearchMode)
		{
			clearAdvSearch();
		}
		textUpdated(true);
		//updatedText(true);
	}
	
	/**
	 * Helper function to remove IDs and replace them with their names
	 */
	private void swapReferences()
	{
		String[][] schools = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN);
		String[][] cities = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		for(int a = 0; a < allKids.length; a++)
		{
			for(int b = 0; b < schools.length; b++)
			{
				if(allKids[a][4].equals(schools[b][0]))
				{
					allKids[a][4] = schools[b][1];
				}
			}
			for(int b = 0; b < cities.length; b++)
			{
				if(allKids[a][5].equals(cities[b][0]))
				{
					allKids[a][5] = cities[b][1];
				}
			}
		}
	}
	
	/**
	 * Makes this screen visible. Makes sure that any changes made in other screens are reflected here.
	 */
	public void makeVisible()
	{
		fullUpdate();
		this.setVisible(true);
	}

	/**
	 * Key typed handler
	 */
	public void keyTyped(KeyEvent e) 
	{
		
	}

	/**
	 * Key Pressed handler
	 */
	public void keyPressed(KeyEvent e) 
	{
	
	}

	/**
	 * Key released handler
	 */
	public void keyReleased(KeyEvent e) 
	{
		if((e.getKeyCode() == KeyEvent.VK_L) & (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK))
		{
			mainWindow.secretMenuKeyboardPress();
		}
		else
		{
			if(nameIn == e.getSource())
			{
				textUpdated(false);
				//updatedText(false);
			}
			if(lastNameField == e.getSource())
			{
				textUpdated(false);
			}
			if(firstNameField == e.getSource())
			{
				textUpdated(false);
			}
			if(phoneNum == e.getSource())
			{
				String emptyPhone = "   -   -    ";
				if(phoneNum.getText().equals(emptyPhone))
				{
					updateList(1,"");
				}
				else
				{
					int blankSpot = phoneNum.getText().indexOf(' ');
					if(blankSpot != -1)
					{
						updateList(1,phoneNum.getText().substring(0,blankSpot));
					}
					else
					{
						updateList(1,phoneNum.getText());
					}
				}
			}
		}
	}
		
	
	/**
	 * Updates the table contents based on the text entered in the search bar
	 * @param ignoreOld True if you want to update the table when the text hasn't changed (like after an update)
	 * @param mode 0 = basic, 1 = advanced
	 */
	private void textUpdated(boolean ignoreOld)
	{
		String lastNameIn = null;
		String firstNameIn = null;
		
		if(advSearchMode == false)
		{
			String inBox = nameIn.getText().toLowerCase();
			if(inBox.contains(","))
			{
				String[] parts = inBox.split(",");
				lastNameIn = parts[0].trim();
				if(parts.length > 1)
				{
					firstNameIn = parts[1].trim();
				}
				else
				{
						firstNameIn = "";
				}
			}
			else if(inBox.trim().contains(" ")) //not handling last names with a space in them
			{
				String in = inBox.trim();
				int place = in.indexOf(" ");
				firstNameIn = in.substring(0, place).trim();
				lastNameIn = in.substring(place).trim();
			}
			else
			{
				lastNameIn = inBox.trim();
				firstNameIn = inBox.trim();
			}
		}
		else
		{
			
			lastNameIn = lastNameField.getText().trim().toLowerCase();
			firstNameIn = firstNameField.getText().trim().toLowerCase();
		}
		boolean goodToUpdate = false;
		if(lastNameIn != oldLast || firstNameIn != oldFirst || ignoreOld )
		{
			goodToUpdate = true;
			oldLast = lastNameIn;
			oldFirst = firstNameIn;
		}
		if(goodToUpdate)
		{
			ArrayList<String[]> buildingList = new ArrayList<String[]>();
			String[][] workingSet = getListFromNamesEditDistance(firstNameIn, lastNameIn);
			
			boolean one = false;
			if(firstNameIn.equals(lastNameIn))
			{
				one = true;
			}
			for(int a = 0; a < workingSet.length; a++)
			{
				boolean added = false;
				if(!one)
				{
					if( (workingSet[a][0].toLowerCase().contains(lastNameIn.toLowerCase()) && workingSet[a][1].toLowerCase().contains(firstNameIn.toLowerCase())) && !added)
					{
						buildingList.add(workingSet[a]);
						added = true;
					}
					else if(Double.parseDouble(workingSet[a][workingSet[0].length-1]) >= Constants.SIMILARITY_THRESHOLD_FIRST_NAME && Double.parseDouble(workingSet[a][workingSet[0].length-2]) >= Constants.SIMILARITY_THRESHOLD_LAST_NAME && !added)
					{
						buildingList.add(workingSet[a]);
						added = true;
					}
					if(lastNameIn.length() > 0)
					{
						if(Double.parseDouble(workingSet[a][workingSet[0].length-2]) >= Constants.SIMILARITY_THRESHOLD_LAST_NAME && workingSet[a][1].toLowerCase().contains(firstNameIn.toLowerCase()) && !added)
						{
							buildingList.add(workingSet[a]);
							added = true;
						}
					}
					if(firstNameIn.length() > 0)
					{
						if(Double.parseDouble(workingSet[a][workingSet[0].length-1]) >= Constants.SIMILARITY_THRESHOLD_FIRST_NAME && workingSet[a][0].toLowerCase().contains(lastNameIn.toLowerCase()) && !added)
						{
							buildingList.add(workingSet[a]);
							added = true;
						}
					}
				}
				else
				{
					if( (workingSet[a][0].toLowerCase().contains(lastNameIn.toLowerCase()) || workingSet[a][1].toLowerCase().contains(firstNameIn.toLowerCase()) || Double.parseDouble(workingSet[a][workingSet[0].length-1]) >= Constants.SIMILARITY_THRESHOLD_FIRST_NAME  || Double.parseDouble(workingSet[a][workingSet[0].length-2]) >= Constants.SIMILARITY_THRESHOLD_LAST_NAME) && !added)
					{
						buildingList.add(workingSet[a]);
						added = true;
					}
				}
			}
			
			
			nameLimitedList = new String[buildingList.size()][6];
			
			for(int a = 0; a < buildingList.size(); a++)
			{
				for(int b = 0; b < 6; b++)
				{
					nameLimitedList[a][b] = buildingList.get(a)[b];
				}
				
			}
		
			updateList(0,null);
		}
	}

	
	/**
	 * Handles changes in the database
	 **/
	public void databaseChanged() 
	{
		/*boolean wasVisible = this.isVisible();
		if(wasVisible)
		{
			this.setVisible(false);
		}*/
		fullUpdate();
		/*if(wasVisible)
		{
			this.setVisible(true);
		}*/
	}
	
	/**
	 * Updates the database connection
	 * @param dbh The new database connection
	 */
	public void updateDatabaseConnection(DBHandler dbh)
	{
		dbFriend = dbh;
		fullUpdate();
		viewer.updateDatabaseConnection(dbFriend);
		addKid.updateDatabaseConnection(dbFriend);
	}

	/**
	 * Handles actions
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("Add New Reader"))
		{
			if(nameIn.getText().trim().length()!=0)
			{
				addKid.passInName(nameIn.getText().trim());
			}
			addKid.setVisible(true);
		}
		if(e.getActionCommand().equals("Advanced Search"))
		{
			this.remove(namePanel);
			this.add(advSearchPanel, BorderLayout.NORTH);
			advSearchMode = true;
			nameIn.setText("");
			textUpdated(false);
			updateList(0,null);
			this.paintAll(this.getGraphics());
		}
		if(e.getActionCommand().equals("Basic Search"))
		{
			this.remove(advSearchPanel);
			this.add(namePanel, BorderLayout.NORTH);
			advSearchMode = false;
			clearAdvSearch();
			this.paintAll(this.getGraphics());
		}
		if(e.getActionCommand().equals("Clear"))
		{
			clearAdvSearch();
		}
		if(e.getSource() == cityBox)
		{
			updateList(6,(String)cityBox.getSelectedItem());
		}
		if(e.getSource() == schoolBox)
		{
			updateList(5,(String)schoolBox.getSelectedItem());
		}
		if(e.getSource() == dayBox)
		{
			updateList(3,(String)dayBox.getSelectedItem());
		}
		if(e.getSource() == monthBox)
		{
			updateList(2,(String)monthBox.getSelectedItem());
		}
		if(e.getSource() == yearBox)
		{
			updateList(4,(String)yearBox.getSelectedItem());
		}
	}
	
	
	private String[][] getListFromNamesEditDistance(String firstName, String lastName)
	{
		String lastNameIn = lastName;
		String firstNameIn = firstName;
		
		ArrayList<String[]> selectedList = new ArrayList<String[]>();
		ArrayList<Double> selectedListWeightsLast = new ArrayList<Double>();
		ArrayList<Double> selectedListWeightsFirst = new ArrayList<Double>();
		
		for(int a = 0; a < allKids.length; a++)
		{
			double lastNameWeight = SimilarityMeasures.editDistance(lastNameIn, allKids[a][0]);
			double firstNameWeight = SimilarityMeasures.editDistance(firstNameIn, allKids[a][1]);
				
			selectedList.add(allKids[a]);
			selectedListWeightsLast.add(lastNameWeight);
			selectedListWeightsFirst.add(firstNameWeight);
			
		}
		
		String[][] output = new String[selectedList.size()][allKids[0].length+2];
		for(int a = 0; a < output.length; a++)
		{
			for(int b = 0; b < output[0].length; b++)
			{
				if(b == output[0].length-2)
				{
					output[a][b] = "" + selectedListWeightsLast.get(a);
				}
				else if(b == output[0].length-1)
				{
					output[a][b] = "" + selectedListWeightsFirst.get(a);
				}
				else
				{
					output[a][b] = selectedList.get(a)[b];
				}
			}
		}
		
		return output;
		
	}
	
	
	
	/**
	 * Toggles visibility of window
	 */
	public void setVisible(boolean aFlag) 
	{
		super.setVisible(aFlag);
		if(!aFlag && nameIn != null)
		{
			nameIn.setText("");
			if(advSearchMode)
			{
				clearAdvSearch();
			}
			advSearchMode = false;
			resetList();
		}
	}
	
	/**
	 * Forces the KidView to refresh, for change of modes.
	 */
	public void refreshKidViewer()
	{
		this.viewer.refresh();
	}
	
	
	
	/**
	 * Resets all the advanced search fields and boxes to their starting spot
	 */
	private void clearAdvSearch()
	{
		lastNameField.setText("");
		firstNameField.setText("");
		textUpdated(false);
		updateList(0,null);
		phoneNum.setText("   -   -    ");
		updateList(1,"");
		dayBox.setSelectedIndex(0);
		monthBox.setSelectedIndex(0);
		yearBox.setSelectedIndex(0);
		schoolBox.setSelectedIndex(0);
		cityBox.setSelectedIndex(0);
		resetList();
	}
	
	/**
	 * Resets the list or readers to the complete list
	 */
	private void resetList()
	{
		oldFirst = "";
		oldLast = "";
		this.currentList = allKids.clone();
		updateList(0,null);
	}

	public void mouseClicked(MouseEvent e) 
	{
		if(e.getSource() == theTable)
		{
			boolean sameKid = false;
			if(lastFirstName.equals(theTable.getValueAt(selectedRow,0)) && lastLastName.equals(theTable.getValueAt(selectedRow,1)) && lastDOB.equals(theTable.getValueAt(selectedRow,2)) )
			{
				sameKid = true;
			}
			if(sameKid)
			{
				//viewer.setReader(kidIdHash.get(theTable.getValueAt(selectedRow,0)+" "+theTable.getValueAt(selectedRow,1)+" "+theTable.getValueAt(selectedRow,2)));
				//lastFirstName = (String)theTable.getValueAt(selectedRow,0);
				//lastLastName = (String)theTable.getValueAt(selectedRow,1);
				//lastDOB = (String)theTable.getValueAt(selectedRow,2);
				viewer.makeVisible();
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