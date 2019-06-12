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
import javax.swing.*;
import backend.*;

/**
 * Window for adding Schools or Cities (or any simple ID|VARCHAR) to database
 * @author Anthony Schmitt
 *
 */
public class Adder extends JFrame implements ActionListener, KeyListener
{

	private DBHandler dbFriend;
	private DatabaseChangeListenerImplementer dbcli;
	private SecretMenuToggle mainWindow;
	private String myQuery, myTitle, myTable;
	private int myColLen, width, height;
	private String[][] itemList;
	private JTextField inputField;
	
	/**
	 * Generic class for adding to the tables in the database where there are just an id and a name.
	 * @param dbh Database Connection Handler
	 * @param mw Main Window for implementing the secret menu item
	 * @param dbc Database change listener implementer to notify of changes
	 * @param title Title for the window
	 * @param mainQuery The query to get it's list
	 * @param colLen The number of columns the mainQuery will return
	 * @param tableName The name of the table in the database
	 */
	public Adder(DBHandler dbh, SecretMenuToggle mw, DatabaseChangeListenerImplementer dbc, String title, String mainQuery, int colLen, String tableName)
	{
		super();
		this.dbFriend = dbh;
		this.dbcli = dbc;
		this.mainWindow = mw;
		this.myTitle = title;
		this.myQuery = mainQuery;
		this.myColLen = colLen;
		this.myTable = tableName;
		this.width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4);
		this.height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4);
		init();
	}
	
	/**
	 * Initializes the window
	 */
	public void init()
	{
		this.setTitle(myTitle);
		this.itemList = dbFriend.query2DstringRet(myQuery, myColLen);
		inputField = new JTextField();
		JLabel msg = new JLabel("Enter new item:");
		JButton addEntry = new JButton("Add");
		addEntry.addActionListener(this);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(msg, BorderLayout.NORTH);
		mainPanel.add(inputField, BorderLayout.CENTER);
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(cancel);
		buttonPanel.add(addEntry);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setSize(width, height);
		this.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4));
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * Updates the list of items
	 */
	public void update()
	{
		this.itemList = dbFriend.query2DstringRet(myQuery, myColLen);
	}
	
	/**
	 * Updates the database connection
	 * @param dbh New database connection
	 */
	public void updateDatabaseConnection(DBHandler dbh)
	{
		this.dbFriend = dbh;
		update();
	}
	
	/**
	 * Makes the window visible.
	 */
	public void makeVisible()
	{
		update();
		this.setVisible(true);
	}
	
	/**
	 * Key typed handling
	 */
	public void keyTyped(KeyEvent e) 
	{	
	}

	/**
	 * Key pressed handling
	 */
	public void keyPressed(KeyEvent e) 
	{
	}

	/**
	 * Key released handling
	 */
	public void keyReleased(KeyEvent e) 
	{
		if((e.getKeyCode() == KeyEvent.VK_L) &(e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK))
		{
			mainWindow.secretMenuKeyboardPress();
		}
	}

	/**
	 * Action handling
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("Add"))
		{
			String input = inputField.getText().trim();
			if(input.length() > 0)
			{
				ArrayList<String[]> similarItems = new ArrayList<String[]>();
				for(int a = 0; a < itemList.length; a++)
				{
					String checkedItem = itemList[a][1];
					
					//Specifically in the case of schools
					if(checkedItem.toLowerCase().contains("school") && !input.toLowerCase().contains("school"))
					{
						checkedItem = checkedItem.toLowerCase().replace("school", "");
					}
					if(checkedItem.toLowerCase().contains("elementary") && !input.toLowerCase().contains("elementary"))
					{
						checkedItem = checkedItem.toLowerCase().replace("elementary", "");
					}
					
					
					if(SimilarityMeasures.editDistance(checkedItem, input) >= Constants.SIMILARITY_THRESHOLD_ITEMS) //assumes index 1 is the name
					{
						similarItems.add(itemList[a]);
					}
				}
				
				for(int a = 0; a < similarItems.size(); a++)
				{
					System.out.println(similarItems.get(a)[1]);
				}
				JLabel kidsMessage = new JLabel("Are you sure that the entered " + myTable + " is not one of these already in the database?");
				
				JRadioButton[] choices = new JRadioButton[similarItems.size()+1];
				ButtonGroup buttonGroup = new ButtonGroup();
				for(int a = 0; a < choices.length; a++)
				{
					choices[a] = new JRadioButton();
					buttonGroup.add(choices[a]);
				}
				JLabel[] potentialMatchList = new JLabel[similarItems.size()+1];
				potentialMatchList[0] = new JLabel("The entered " + myTable + " is NOT listed below.");
				for(int a = 1; a < potentialMatchList.length; a++)
				{
					potentialMatchList[a] = new JLabel(similarItems.get(a-1)[1]);
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
				
				//TODO just don't allow if exactly the same as another
				
				int verifyValue = JOptionPane.showConfirmDialog(null, mainPanel, "Add " + myTable + " Confirmation", JOptionPane.OK_CANCEL_OPTION);
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
						String idHold = dbFriend.query1DstringRet("SELECT MAX(id) FROM " + myTable + ";")[0];
						int newID = Integer.parseInt(idHold) + 1;
						dbFriend.executeUpdate("INSERT INTO " + myTable + " VALUES (" + newID + ",\""+input+"\");");
						this.inputField.setText("");
						this.setVisible(false);
						JOptionPane.showMessageDialog(null, input + " was added.", myTable + " Added", JOptionPane.INFORMATION_MESSAGE);
						dbcli.notifyChange();
					}
					else
					{
						JOptionPane.showMessageDialog(null, "OK", "No Action Required", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null,"You must enter something","Blank entry", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(e.getActionCommand().equals("Cancel"))
			{
				this.inputField.setText("");
				this.setVisible(false);
			}
		}
	}

}
