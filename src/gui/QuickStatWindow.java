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
import javax.swing.GroupLayout.*;
import javax.swing.text.*;

import backend.*;
import java.util.*;

public class QuickStatWindow extends JInternalFrame implements KeyListener, ActionListener
{
	private DBHandler dbFriend;
	private SecretMenuToggle mainWindow;
	private int height, width;
	private Hashtable<String, String> schoolHash, gradeHash, programHash, cityHash;
	private JComboBox<String> schoolBox, gradeBox, programBox, cityBox, readBox, yearBox;
	private JTextField readField;
	
	public QuickStatWindow(DBHandler dbh, SecretMenuToggle mainWin)
	{
		this.dbFriend = dbh;
		this.mainWindow = mainWin;
		this.height = 150;
		this.width = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3)*2);
		this.setSize(width, height);
		this.setClosable(true);
		this.setResizable(false);
		this.setLocation(((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/6)),((int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4)));
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setTitle("Quick Stats");
		init();
	}
	
	protected void init()
	{
		String[][] values = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN);
		values = Constants.addCommonEntities(Constants.COMMON_SCHOOLS, values);
		values[0][1] = "";
		schoolHash = new Hashtable<String, String>();
		String[] names = new String[values.length];
		for(int a = 0; a < values.length; a++)
		{
			schoolHash.put(values[a][1], values[a][0]);
			names[a] = values[a][1];
		}
		schoolBox = new JComboBox<String>(names);
		values = dbFriend.query2DstringRet(Queries.ALL_GRADES_PL_IDS, Queries.ALL_GRADES_PL_IDS_COL_LEN);
		String[][] empty = {{"-1",""}};
		values = Constants.addCommonEntities(empty,values);
		gradeHash = new Hashtable<String, String>();
		names = new String[values.length];
		for(int a = 0; a < values.length; a++)
		{
			gradeHash.put(values[a][1], values[a][0]);
			names[a] = values[a][1];
		}
		gradeBox = new JComboBox<String>(names);
		values = dbFriend.query2DstringRet(Queries.ALL_PROGRAMS_PL_IDS, Queries.ALL_PROGRAMS_PL_IDS_COL_LEN);
		values = Constants.addCommonEntities(empty,values);
		programHash = new Hashtable<String, String>();
		names = new String[values.length];
		for(int a = 0; a < values.length; a++)
		{
			programHash.put(values[a][1], values[a][0]);
			names[a] = values[a][1];
		}
		programBox = new JComboBox<String>(names);
		values = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		String[][] citycom = {{"-1",""},{"46","Sun Prairie"}};
		values = Constants.addCommonEntities(citycom,values);
		cityHash = new Hashtable<String, String>();
		names = new String[values.length];
		for(int a = 0; a < values.length; a++)
		{
			cityHash.put(values[a][1], values[a][0]);
			names[a] = values[a][1];
		}
		cityBox = new JComboBox<String>(names); 
		String[] read = {"","Highest Level =","Highest Level <","Highest Level >","Extra Challenge 1","Extra Challenge 2","Extra Challenge 3","Extra Challenge 4","Extra Challenge 5","Extra Challenge 6","Extra Challenge 7","Extra Challenge 8","Extra Challenge 9","Extra Challenge 10"};
		readBox = new JComboBox<String>(read);
		readBox.addActionListener(this);
		readField = new JTextField();
		readField.setEnabled(false);
		Date d = new Date(System.currentTimeMillis());
		int year = d.getYear()+1900;
		String[] years = new String[year-Constants.OLDEST_YEAR_RECORD+1];
		for(int a = 0; a <= (year-Constants.OLDEST_YEAR_RECORD); a++)
		{
			years[a] = ""+(year-a);
		}
		yearBox = new JComboBox<String>(years);
		/*JPanel readPanel = new JPanel();
		readPanel.add(readBox);
		readPanel.add(readField);
		readPanel.setMinimumSize(new Dimension(100,10));*/
		JLabel readLabel = new JLabel("Boxes:");
		JLabel schoolLabel = new JLabel("School:");
		JLabel gradeLabel = new JLabel("Grade:");
		JLabel programLabel = new JLabel("Program:");
		JLabel cityLabel = new JLabel("City:");
		JLabel yearLabel = new JLabel("Year:");
		JLabel blankLabel = new JLabel();
		JPanel topPanel = new JPanel();
		GroupLayout layout = new GroupLayout(topPanel);
		topPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		hGroup.addGroup(layout.createParallelGroup().addComponent(readLabel).addComponent(readBox));
		hGroup.addGroup(layout.createParallelGroup().addComponent(blankLabel).addComponent(readField));
		hGroup.addGroup(layout.createParallelGroup().addComponent(schoolLabel).addComponent(schoolBox));
		hGroup.addGroup(layout.createParallelGroup().addComponent(gradeLabel).addComponent(gradeBox));
		hGroup.addGroup(layout.createParallelGroup().addComponent(programLabel).addComponent(programBox));
		hGroup.addGroup(layout.createParallelGroup().addComponent(cityLabel).addComponent(cityBox));
		hGroup.addGroup(layout.createParallelGroup().addComponent(yearLabel).addComponent(yearBox));
		layout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(readLabel).
				addComponent(blankLabel).addComponent(schoolLabel).addComponent(gradeLabel).
				addComponent(programLabel).addComponent(cityLabel).addComponent(yearLabel));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(readBox).
				addComponent(readField).addComponent(schoolBox).addComponent(gradeBox).
				addComponent(programBox).addComponent(cityBox).addComponent(yearBox));
		layout.setVerticalGroup(vGroup);
		
		/*
		hGroup.addGroup(layout.createParallelGroup().addComponent(readLabel).
				addComponent(schoolLabel).addComponent(gradeLabel).
				addComponent(programLabel).addComponent(cityLabel).addComponent(yearLabel));
		hGroup.addGroup(layout.createParallelGroup().addComponent(readPanel).
				addComponent(schoolBox).addComponent(gradeBox).
				addComponent(programBox).addComponent(cityBox).addComponent(yearBox));
		layout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(readLabel).addComponent(readPanel));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(schoolLabel).addComponent(schoolBox));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(gradeLabel).addComponent(gradeBox));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(programLabel).addComponent(programBox));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(cityLabel).addComponent(cityBox));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(yearLabel).addComponent(yearBox));
		layout.setVerticalGroup(vGroup);
		*/
		JButton getStatButton = new JButton("Get Stats");
		getStatButton.addActionListener(this);
		this.setLayout(new BorderLayout());
		this.add(topPanel, BorderLayout.CENTER);
		this.add(getStatButton, BorderLayout.SOUTH);
	}

	public void updateDatabaseConnection(DBHandler dbh)
	{
		dbFriend = dbh;
		String[][] values = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN);
		Constants.addCommonEntities(Constants.COMMON_SCHOOLS, values);
		values[0][1] = "";
		schoolHash = new Hashtable<String, String>();
		String[] names = new String[values.length];
		for(int a = 0; a < values.length; a++)
		{
			schoolHash.put(values[a][1], values[a][0]);
			names[a] = values[a][1];
		}
		schoolBox = new JComboBox<String>(names);
		values = dbFriend.query2DstringRet(Queries.ALL_GRADES_PL_IDS, Queries.ALL_GRADES_PL_IDS_COL_LEN);
		String[][] empty = {{"-1",""}};
		Constants.addCommonEntities(values,empty);
		gradeHash = new Hashtable<String, String>();
		names = new String[values.length];
		for(int a = 0; a < values.length; a++)
		{
			gradeHash.put(values[a][1], values[a][0]);
			names[a] = values[a][1];
		}
		gradeBox = new JComboBox<String>(names);
		values = dbFriend.query2DstringRet(Queries.ALL_PROGRAMS_PL_IDS, Queries.ALL_PROGRAMS_PL_IDS_COL_LEN);
		Constants.addCommonEntities(values,empty);
		programHash = new Hashtable<String, String>();
		names = new String[values.length];
		for(int a = 0; a < values.length; a++)
		{
			programHash.put(values[a][1], values[a][0]);
			names[a] = values[a][1];
		}
		programBox = new JComboBox<String>(names);
		values = dbFriend.query2DstringRet(Queries.ALL_CITIES_PL_IDS, Queries.ALL_CITIES_PL_IDS_COL_LEN);
		String[][] citycom = {{"-1",""},{"46","Sun Prairie"}};
		Constants.addCommonEntities(values,citycom);
		cityHash = new Hashtable<String, String>();
		names = new String[values.length];
		for(int a = 0; a < values.length; a++)
		{
			cityHash.put(values[a][1], values[a][0]);
			names[a] = values[a][1];
		}
		cityBox = new JComboBox<String>(names);
	}
	
	public void keyTyped(KeyEvent e) 
	{
		
	}

	public void keyPressed(KeyEvent e) 
	{
		
	}

	public void keyReleased(KeyEvent e) 
	{
		if((e.getKeyCode() == KeyEvent.VK_L) & (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK))
		{
			mainWindow.secretMenuKeyboardPress();
		}
	}
	
	public void setVisible(boolean vis)
	{
		super.setVisible(vis);
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("comboBoxChanged"))
		{
			if(readBox.getSelectedIndex()==1)
			{
				readField.setEnabled(true);
			}
			else
			{
				readField.setEnabled(false);
			}
		}
		if(e.getActionCommand().equals("Get Stats"))
		{
			String query = "SELECT count(*) FROM Program_Data WHERE";
			String error = "ERROR!\n";
			boolean multipleWhere = false;
			boolean errors = false;
			
			
			if(schoolBox.getSelectedIndex() != 0)
			{
				String id = schoolHash.get(schoolBox.getSelectedItem());
				query += " school_id = " + id;
				multipleWhere = true;
			}
			if(gradeBox.getSelectedIndex() != 0)
			{
				String id = gradeHash.get(gradeBox.getSelectedItem());
				if(multipleWhere)
				{
					query += " AND";
				}
				else
				{
					multipleWhere = true;
				}
				query += " grade_id = " + id;
			}
			if(programBox.getSelectedIndex() != 0)
			{
				String id = programHash.get(programBox.getSelectedItem());
				if(multipleWhere)
				{
					query += " AND";
				}
				else
				{
					multipleWhere = true;
				}
				query += " program_id = " + id;
			}
			if(cityBox.getSelectedIndex() != 0)
			{
				String id = cityHash.get(cityBox.getSelectedItem());
				if(multipleWhere)
				{
					query += " AND";
				}
				else
				{
					multipleWhere = true;
				}
				query += " city_id = " + id;
			}
			if(readBox.getSelectedIndex() != 0)
			{
				if(multipleWhere)
				{
					query += " AND";
				}
				else
				{
					multipleWhere = true;
				}
				int index = readBox.getSelectedIndex();
				if(index == 1)
				{
					String input = readField.getText().trim();
					if(input.length() == 0)
					{
						errors = true;
						error += " No Highest Level Given.\n";
					}
					else if(!(input.matches("\\d") || input.matches("\\d\\d")))
					{
						errors = true;
						error += " Numbers only.\n";
					}
					else if(Integer.parseInt(input) > 10)
					{
						errors = true;
						error += " Highest Level cannot be higher than 10.\n";
					}
					else if(Integer.parseInt(input) < 0)
					{
						errors = true;
						error += " Highest level cannot be lower than 0.\n";
					}
					else
					{
						query += " highest_level_normal = "+ input;
					}
				}
				else if(index == 2)
				{
					String input = readField.getText().trim();
					if(input.length() == 0)
					{
						errors = true;
						error += " No Highest Level Given.\n";
					}
					else if(!(input.matches("\\d") || input.matches("\\d\\d")))
					{
						errors = true;
						error += " Numbers only.\n";
					}
					else if(Integer.parseInt(input) > 10)
					{
						errors = true;
						error += " Highest Level cannot be higher than 10.\n";
					}
					else if(Integer.parseInt(input) < 0)
					{
						errors = true;
						error += " Highest level cannot be lower than 0.\n";
					}
					else
					{
						query += " highest_level_normal < "+ input;
					}
				}
				else if(index == 3)
				{
					String input = readField.getText().trim();
					if(input.length() == 0)
					{
						errors = true;
						error += " No Highest Level Given.\n";
					}
					else if(!(input.matches("\\d") || input.matches("\\d\\d")))
					{
						errors = true;
						error += " Numbers only.\n";
					}
					else if(Integer.parseInt(input) > 10)
					{
						errors = true;
						error += " Highest Level cannot be higher than 10.\n";
					}
					else if(Integer.parseInt(input) < 0)
					{
						errors = true;
						error += " Highest level cannot be lower than 0.\n";
					}
					else
					{
						query += " highest_level_normal > "+ input;
					}
				}
				else if(index == 4)
				{
					query += " extra_one = 1";
				}
				else if(index == 5)
				{
					query += " extra_two = 1";
				}
				else if(index == 6)
				{
					query += " extra_three = 1";
				}
				else if(index == 7)
				{
					query += " extra_four = 1";
				}
				else if(index == 8)
				{
					query += " extra_five = 1";
				}
				else if(index == 9)
				{
					query += " extra_six = 1";
				}
				else if(index == 10)
				{
					query += " extra_seven = 1";
				}
				else if(index == 11)
				{
					query += " extra_eight = 1";
				}
				else if(index == 12)
				{
					query += " extra_nine = 1";
				}
				else if(index == 13)
				{
					query += " extra_ten = 1";
				}	
			}
			if(multipleWhere)
			{
				query += " AND";
			}
			query += " year = " + yearBox.getSelectedItem();
			query += ";";
			if(!errors)
			{
				//System.out.println(query);
				String result = dbFriend.query1DstringRet(query)[0];
				JOptionPane.showMessageDialog(null,"" + result + " readers match specified criteria.", "Query Results", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(null, error,"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
