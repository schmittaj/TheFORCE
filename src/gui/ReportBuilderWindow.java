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

/**
 * Window for building reports
 * @author Anthony Schmitt
 *
 */
public class ReportBuilderWindow extends JInternalFrame implements ActionListener, KeyListener, DatabaseChangeListener
{
	private int width, height;
	private String currentFile;
	private String[] reportSelection= {"<Create New Report>"};
	private JComboBox<String> reportSelectBox;
	private JComboBox<String> yearBox;
	private JRadioButton registered, checkedIn, finished;
	private JTextArea limitArea, reportContentsArea;
	private Hashtable<String,String> cityHash, schoolHash, gradeHash;
	private JScrollPane limitScroll, reportContentsScroll;
	private DBHandler dbFriend;
	private JCheckBox schoolCheckBox, gradeCheckBox;
	private SecretMenuToggle mainWindow;
	
	/**
	 * Constructor
	 * @param dbh Database Connection
	 * @param mainWin Main window
	 */
	public ReportBuilderWindow(DBHandler dbh, SecretMenuToggle mainWin)
	{
		super("",true,true,true);
		this.currentFile = null;
		this.dbFriend = dbh;
		this.setTitle("Report Builder");
		this.width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()-25);
		this.height = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4)*3);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setSize(width, height);
		this.mainWindow = mainWin;
		init();
		getReportList();
		this.addKeyListener(this);
		this.setFocusable(true);
	}
	
	
	/**
	 * Initializes the window
	 */
	private void init()
	{
		cityHash = new Hashtable<String,String>();
		schoolHash = new Hashtable<String,String>();
		gradeHash = new Hashtable<String,String>();
		String[][] grades2d = dbFriend.query2DstringRet(Queries.ALL_GRADES_PL_IDS, Queries.ALL_GRADES_PL_IDS_COL_LEN);
		String[][] school2d = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN);
		String[][] city2d = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		for(int a = 0; a < grades2d.length; a++)
		{
			gradeHash.put(grades2d[a][1], grades2d[a][0]);
		}
		for(int a = 0; a < school2d.length; a++)
		{
			schoolHash.put(school2d[a][1], school2d[a][0]);
		}
		for(int a = 0; a < city2d.length; a++)
		{
			cityHash.put(city2d[a][1], city2d[a][0]);
		}
		
		JLabel addParam = new JLabel("Select Parameter");
		registered = new JRadioButton("Registrants");
		checkedIn = new JRadioButton("Readers");
		finished = new JRadioButton("Finishers");
		ButtonGroup paramGroup = new ButtonGroup();
		paramGroup.add(registered);
		paramGroup.add(checkedIn);
		paramGroup.add(finished);
		JPanel paramWindow = new JPanel();
		paramWindow.setLayout(new GridLayout(3,1));
		paramWindow.add(registered);
		paramWindow.add(checkedIn);
		paramWindow.add(finished);
		
		JLabel breakDownLabel = new JLabel("Break Down By");
		schoolCheckBox = new JCheckBox("School");
		gradeCheckBox = new JCheckBox("Grade");
		JLabel limitBy = new JLabel("Limit By");
		limitArea = new JTextArea();
		limitArea.setEditable(false);
		limitScroll = new JScrollPane(limitArea);
		JButton addToReport = new JButton("Add To Report");
		addToReport.addActionListener(this);
		reportSelectBox = new JComboBox<String>(reportSelection);
		
		JPanel paramPanel = new JPanel();
		paramPanel.setLayout(new BorderLayout());
		paramPanel.add(addParam, BorderLayout.NORTH);
		paramPanel.add(paramWindow, BorderLayout.CENTER);
		
		JPanel breakDownPanel = new JPanel();
		breakDownPanel.setLayout(new BorderLayout());
		breakDownPanel.add(breakDownLabel, BorderLayout.NORTH);
		JPanel breakDownInnerPanel = new JPanel();
		breakDownInnerPanel.setLayout(new GridLayout(2,1));
		breakDownInnerPanel.add(schoolCheckBox);
		breakDownInnerPanel.add(gradeCheckBox);
		breakDownPanel.add(breakDownInnerPanel, BorderLayout.CENTER);
		
		JPanel limitPanel = new JPanel();
		limitPanel.setLayout(new BorderLayout());
		limitPanel.add(limitBy, BorderLayout.NORTH);
		JButton lbCity = new JButton("City");
		lbCity.addActionListener(this);
		JButton lbSchool = new JButton("School");
		lbSchool.addActionListener(this);
		JButton lbGrade = new JButton("Grade");
		lbGrade.addActionListener(this);
		JPanel smallLimitPanel = new JPanel();
		smallLimitPanel.setLayout(new GridLayout(4,1));
		smallLimitPanel.add(lbCity);
		smallLimitPanel.add(lbSchool);
		smallLimitPanel.add(lbGrade);
		smallLimitPanel.add(limitScroll);
		limitPanel.add(smallLimitPanel, BorderLayout.CENTER);
		
		Date d = new Date(System.currentTimeMillis());
		int year = d.getYear()+1900;
		int yearRange = year - Constants.OLDEST_YEAR_RECORD + 1;
		String[] yearOptions = new String[yearRange];
		for(int a = 0; a < yearRange; a++)
		{
			yearOptions[yearRange-a-1] = "" + (Constants.OLDEST_YEAR_RECORD+a);
		}
		yearBox = new JComboBox<String>(yearOptions);
		
		reportContentsArea = new JTextArea();
		reportContentsScroll = new JScrollPane(reportContentsArea);

		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,5));
		mainPanel.add(paramPanel);
		mainPanel.add(breakDownPanel);
		mainPanel.add(limitPanel);
		mainPanel.add(yearBox);
		mainPanel.add(reportContentsScroll);
		
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(reportSelectBox, BorderLayout.NORTH);
		this.add(addToReport, BorderLayout.SOUTH);
	}
	
	/**
	 * Grabs the list of reports alread in existance
	 */
	private void getReportList()
	{
		reportSelection = ReportHandler.getReportListWithEmpty();
		int indexSet = 0;
		for(int a = 0; a < reportSelection.length; a++)
		{
			if(currentFile != null)
			{
				if(reportSelection[a].equals(currentFile))
				{
					indexSet = a;
				}
			}
		}
		
		this.remove(reportSelectBox);
		reportSelectBox = new JComboBox<String>(reportSelection);
		reportSelectBox.setSelectedIndex(indexSet);
		reportSelectBox.addActionListener(this);
		this.add(reportSelectBox, BorderLayout.NORTH);
		this.setVisible(false);
		this.paintAll(this.getGraphics());
		this.setVisible(true);
	}
	
	/**
	 * Builds a query
	 * @return The query
	 */
	private String createQuery()
	{
		String output = "SELECT school_id, grade_id, count(*) FROM Program_Data WHERE";
		boolean hasParam = false;

		if(registered.isSelected())
		{
			hasParam = true;
			output += " highest_level_normal >= 0";
		}
		if(checkedIn.isSelected())
		{
			if(hasParam)
			{
				output += " AND";
			}
			hasParam = true;
			output += " highest_level_normal > 0";
		}
		if(finished.isSelected())
		{
			if(hasParam)
			{
				output += " AND";
			}
			hasParam = true;
			output += " highest_level_normal = 10";
		}
		
		if(limitArea.getText().trim().length() > 0)
		{
			if(hasParam)
			{
				output += " AND (";
			}
			String[] limits = limitArea.getText().split("\n");
			for(int a = 0; a < limits.length; a++)
			{
				if(limits[a].contains("city"))
				{
					output += " city_id = ";
					int index = limits[a].indexOf(" ") + 1;
					output += cityHash.get(limits[a].substring(index));
				}
				if(limits[a].contains("school"))
				{
					output += " school_id = ";
					int index = limits[a].indexOf(" ") + 1;
					output += schoolHash.get(limits[a].substring(index));
				}
				if(limits[a].contains("grade"))
				{
					output += " grade_id = ";
					int index = limits[a].indexOf(" ") + 1;
					output += gradeHash.get(limits[a].substring(index));
				}
				if(a != limits.length-1)
				{
					output += " OR";
				}
				else
				{
					output += ")";
				}
			}
		}
		output += " AND year = " + (String)yearBox.getSelectedItem();
		boolean groups = false;
		String groupByText = " GROUP BY ";
		if(schoolCheckBox.isSelected())
		{
			groups = true;
			groupByText += "school_id, ";
		}
		if(gradeCheckBox.isSelected())
		{
			groups = true;
			groupByText += "grade_id, ";
		}
		if(groups)
		{
			groupByText = groupByText.substring(0, groupByText.length()-2);
			output += groupByText;
		}
		output += ";";
		return output;
	}

	/**
	 * Handles actions
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("City"))
		{
			String[] cities = dbFriend.query1DstringRet(Queries.ALL_CITIES_ALPHA);
			JComboBox<String> cityBox = new JComboBox<String>(cities);
			JOptionPane.showMessageDialog(null,cityBox);
			String currentText = limitArea.getText();
			currentText += "city " + cityBox.getSelectedItem() + "\n";
			limitArea.setText(currentText);
		}
		
		if(e.getActionCommand().equals("School"))
		{
			String[] schools = dbFriend.query1DstringRet(Queries.ALL_SCHOOLS_ALPHA);
			JComboBox<String> schoolBox = new JComboBox<String>(schools);
			JOptionPane.showMessageDialog(null,schoolBox);
			String currentText = limitArea.getText();
			currentText += "school " + schoolBox.getSelectedItem() + "\n";
			limitArea.setText(currentText);
		}
		
		if(e.getActionCommand().equals("Grade"))
		{
			String[] grades = dbFriend.query1DstringRet(Queries.ALL_GRADES);
			JComboBox<String> gradeBox = new JComboBox<String>(grades);
			JOptionPane.showMessageDialog(null,gradeBox);
			String currentText = limitArea.getText();
			currentText += "grade " + gradeBox.getSelectedItem() + "\n";
			limitArea.setText(currentText);

		}
		
		if(e.getActionCommand().equals("Add To Report"))
		{
			String fileName = "";
			if(reportSelectBox.getSelectedIndex() == 0)
			{
				fileName = JOptionPane.showInputDialog(null, "Please name report.", "Report Name", JOptionPane.PLAIN_MESSAGE);
			}
			else
			{
				fileName = (String) reportSelectBox.getSelectedItem();
			}
			currentFile = fileName;
			String desc = creatDescription();
			String query = createQuery();
			ReportHandler.fileAppend(fileName, desc, query);
			limitArea.setText("");
			schoolCheckBox.setSelected(false);
			gradeCheckBox.setSelected(false);
			registered.setSelected(false);
			checkedIn.setSelected(false);
			finished.setSelected(false);
			String[] descriptions = ReportHandler.getStatements(currentFile, ReportHandler.DESCRIPTION_STATEMENTS);
			String display = "";
			for(int a = 0; a < descriptions.length; a++)
			{
				display += descriptions[a] + "\n";
			}
			reportContentsArea.setText(display);
			getReportList();
		}
		
		if(e.getActionCommand().equals("comboBoxChanged"))
		{
			if(reportSelectBox.getSelectedIndex() == 0)
			{
				reportContentsArea.setText("");
			}
			else
			{
				currentFile = (String) reportSelectBox.getSelectedItem();
				String[] descriptions = ReportHandler.getStatements(currentFile, ReportHandler.DESCRIPTION_STATEMENTS);
				String display = "";
				for(int a = 0; a < descriptions.length; a++)
				{
					display += descriptions[a] + "\n";
				}
				reportContentsArea.setText(display);
			}
		}
	}
	
	/**
	 * Writes up description of query
	 * @return Description of query
	 */
	private String creatDescription() 
	{
		String output = "";
		output += "Show ";
		if(registered.isSelected())
		{
			output += " Registrants,";
		}
		if(checkedIn.isSelected())
		{
			output += " Readers,";
		}
		if(finished.isSelected())
		{
			output += " Finishers,";
		}
		output = output.substring(0, output.length()-1);
		output += " broken down by ";
		if(schoolCheckBox.isSelected())
		{
			output += " schools,";
		}
		if(gradeCheckBox.isSelected())
		{
			output += " grades,";
		}
		output = output.substring(0, output.length()-1);
		output += " limited by ";
		String[] limitParts = limitArea.getText().split("\n");
		for(int a = 0; a < limitParts.length; a++)
		{
			output += limitParts[a] + ",";
		}
		output = output.substring(0, output.length()-1);
		output += " for year " + (String)yearBox.getSelectedItem();
		return output;
	}


	/**
	 * Updates the database connection
	 * @param dbh New database conneciton
	 */
	public void updateDatabaseConnection(DBHandler dbh)
	{
		this.dbFriend = dbh;
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

	public void databaseChanged() 
	{
		cityHash = new Hashtable<String,String>();
		schoolHash = new Hashtable<String,String>();
		String[][] school2d = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN);
		String[][] city2d = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		
		for(int a = 0; a < school2d.length; a++)
		{
			schoolHash.put(school2d[a][1], school2d[a][0]);
		}
		for(int a = 0; a < city2d.length; a++)
		{
			cityHash.put(city2d[a][1], city2d[a][0]);
		}
		
		
	}
	/*
	public void setVisible(boolean aFlag) 
	{
		registered.setSelected(false);
		checkedIn.setSelected(false);
		finished.setSelected(false);
		limitArea.setText("");
		reportContentsArea.setText("");
		schoolCheckBox.setSelected(false);
		gradeCheckBox.setSelected(false);
		reportSelectBox.setSelectedIndex(0);
		yearBox.setSelectedIndex(0);
		super.setVisible(aFlag);
	}*/
}
