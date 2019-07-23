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
import java.util.*;
import javax.swing.*;
import backend.*;

/**
 * This window shows a simple dialogue and pulls all the data for a specified year and dumps it into a csv
 * @author Anthony Schmitt
 *
 */
public class DataDumpWindow extends JFrame implements ActionListener, KeyListener
{
	
	private SecretMenuToggle mainWindow;
	private DBHandler dbFriend;
	private int height, width;
	private JTextField nameField;
	private JComboBox<String> yearBox;
	
	/**
	 * Constructor
	 * @param mw Secret Menu
	 * @param dbh Database Connection
	 */
	public DataDumpWindow(SecretMenuToggle mw, DBHandler dbh)
	{
		this.mainWindow = mw;
		this.dbFriend = dbh;
		this.height = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4));
		this.width = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4));
		this.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3), (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()/3)));;
		this.setSize(width,height);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setTitle("Download Year Data");
		init();
	}
	
	/**
	 * Sets up window
	 */
	private void init()
	{
		nameField = new JTextField("<Default>");
		Date d = new Date(System.currentTimeMillis());
		int year = d.getYear()+1900;
		String[] years = new String[year-Constants.OLDEST_YEAR_RECORD+1];
		for(int a = 0; a <= (year-Constants.OLDEST_YEAR_RECORD); a++)
		{
			years[a] = ""+(year-a);
		}
		yearBox = new JComboBox<String>(years);
		JLabel nameLabel = new JLabel("Filename:");
		JLabel yearLabel = new JLabel("Select Year");
		JButton getButton = new JButton("Get Data");
		getButton.addActionListener(this);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.add(nameLabel, BorderLayout.WEST);
		namePanel.add(nameField, BorderLayout.CENTER);
		JPanel yearPanel = new JPanel();
		yearPanel.setLayout(new BorderLayout());
		yearPanel.add(yearLabel, BorderLayout.WEST);
		yearPanel.add(yearBox, BorderLayout.CENTER);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(2,1));
		topPanel.add(namePanel);
		topPanel.add(yearPanel);
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1,2));
		bottomPanel.add(getButton);
		bottomPanel.add(cancelButton);
		this.setLayout(new BorderLayout());
		this.add(topPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * Updates database connection
	 * @param dbh new database connection
	 */
	public void updateDatabaseConnection(DBHandler dbh)
	{
		this.dbFriend = dbh;
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
	 * Handles actions
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("Get Data"))
		{
			String fileName = nameField.getText().trim();
			String year = (String) yearBox.getSelectedItem();
			if(fileName.equals("<Default>"))
			{
				Date d = new Date(System.currentTimeMillis());
				int month = d.getMonth()+1;
				int day = d.getDate();
				int yr = d.getYear()+1900;
				fileName = year + "data" + month + "-" + day + "-" + year; 
			}
			fileName += ".csv";
			
			String[][] schools = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS_ALPHA, Queries.ALL_CITIES_PL_IDS_COL_LEN);
			Hashtable<String,String> schoolsIDtoName = new Hashtable<String, String>();
			for(int a = 0; a < schools.length; a++)
			{
				schoolsIDtoName.put(schools[a][0], schools[a][1]);
			}
			String[][] cities = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS_ALPHA, Queries.ALL_CITIES_PL_IDS_COL_LEN);
			Hashtable<String,String> citiesIDtoName = new Hashtable<String, String>();
			for(int a = 0; a < cities.length; a++)
			{
				citiesIDtoName.put(cities[a][0], cities[a][1]);
			}
			String[][] grades = dbFriend.query2DstringRet(Queries.ALL_GRADES_PL_IDS, Queries.ALL_GRADES_PL_IDS_COL_LEN);
			Hashtable<String,String> gradesIDtoName = new Hashtable<String, String>();
			for(int a = 0; a < grades.length; a++)
			{
				gradesIDtoName.put(grades[a][0], grades[a][1]);
			}
			String[][] programs = dbFriend.query2DstringRet(Queries.ALL_PROGRAMS_PL_IDS, Queries.ALL_PROGRAMS_PL_IDS_COL_LEN);
			Hashtable<String,String> programsIDtoName = new Hashtable<String, String>();
			for(int a = 0; a < programs.length; a++)
			{
				programsIDtoName.put(programs[a][0], programs[a][1]);
			}
			
			String[][] data = Queries.getYearData(Integer.parseInt(year), dbFriend);
			//first name, last name, dob, age, phone, email, parent, city, school, grade, program, boxes completed, ec1, ec2, ec3, ec4, ec5, ec6, ec7, ec8, ec9, ec10, pp
			for(int a = 0; a < data.length; a++)
			{
				if(!data[a][7].isEmpty())
				{
					data[a][7] = citiesIDtoName.get(data[a][7]); //city
				}
				if(!data[a][8].isEmpty())
				{
					data[a][8] = schoolsIDtoName.get(data[a][8]);  //school
				}
				if(!data[a][9].isEmpty())
				{
					data[a][9] = gradesIDtoName.get(data[a][9]);  //grade
				}
				if(!data[a][10].isEmpty())
				{
					data[a][10] = programsIDtoName.get(data[a][10]);  //program
				}
			}
			
			String[] topLine = {"First Name", "Last Name", "Date of Birth", "Age", "Phone", "Email", "Parent", "City", "School", "Grade", "Program", "Boxes Completed", "Extra Challange Box 1", "Extra Challange Box 2", "Extra Challange Box 3", "Extra Challange Box 4", "Extra Challange Box 5", "Extra Challange Box 6", "Extra Challange Box 7", "Extra Challange Box 8", "Extra Challange Box 9", "Extra Challange Box 10", "Pool Pass"};
			String[][] dataToWrite = new String[data.length+1][data[0].length];
			for(int a = 0; a < topLine.length; a++)
			{
				dataToWrite[0][a] = topLine[a];
			}
			for(int a = 0; a < data.length; a++)
			{
				for(int b = 0; b < data[0].length; b++)
				{
					dataToWrite[a+1][b] = data[a][b];
				}
			}
			CSVWriter.writeCSVFile(fileName, dataToWrite);
			this.setVisible(false);
			JOptionPane.showMessageDialog(null,fileName + " written.", "File Written", JOptionPane.INFORMATION_MESSAGE);
			yearBox.setSelectedIndex(0);
			nameField.setText("<Default>");
			
		}
		if(e.getActionCommand().equals("Cancel"))
		{
			yearBox.setSelectedIndex(0);
			nameField.setText("<Default>");
			this.setVisible(false);
		}
	}

}
