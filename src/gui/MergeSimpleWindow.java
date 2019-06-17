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
 * A simple window for merging items in simple tables (ones with just an ID and Name)
 * @author Anthony Schmitt
 *
 */
public class MergeSimpleWindow  extends JFrame implements ActionListener, KeyListener, DatabaseChangeListener
{

	private DBHandler dbFriend;
	private DatabaseChangeListenerImplementer dbcli;
	private SecretMenuToggle mainWindow;
	private String myTitle, myQuery, myTable;
	private String[][] myList;
	protected Hashtable<String,String> idHash;
	private int width, height, myColLen;
	private JComboBox<String> replaceThis, withThis;
	private JPanel bottomPanel, topPanel;
	
	/**
	 * Constructor
	 * @param dbh Database connection
	 * @param mw Main window
	 * @param dbc DB Change Listener handler
	 * @param title Title for this window
	 * @param mainQuery The query to generate its list
	 * @param colLen The column length that its mainQuery will return
	 * @param tableName The name of the Table in the database
	 */
	public MergeSimpleWindow(DBHandler dbh, SecretMenuToggle mw, DatabaseChangeListenerImplementer dbc, String title, String mainQuery, int colLen, String tableName)
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
		this.setSize(width, height);
		this.setTitle(myTitle);
		this.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/3));
		this.setResizable(false);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		init();
	}
	
	/**
	 * Initializes the window
	 */
	private void init()
	{
		myList = dbFriend.query2DstringRet(myQuery, myColLen);
		idHash = new Hashtable<String,String>();
		for(int a = 0; a < myList.length; a++)
		{
			idHash.put(myList[a][1], myList[a][0]);
		}
		String[] replaceList = new String[myList.length];
		String[] withList = new String[myList.length];		
		for(int a = 0; a < replaceList.length; a++)
		{
			replaceList[a] = myList[a][1];
			withList[a] = myList[a][1];
		}
		JLabel replaceLabel = new JLabel("Replace This " + myTable + ":");
		replaceThis = new JComboBox<String>(replaceList);
	
		//replaceThis.addActionListener(this);
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(replaceLabel, BorderLayout.NORTH);
		topPanel.add(replaceThis, BorderLayout.CENTER);
		JLabel withLabel = new JLabel("With This:");
		withThis = new JComboBox<String>(withList);
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(withLabel, BorderLayout.NORTH);
		bottomPanel.add(withThis, BorderLayout.CENTER);
		JButton add = new JButton("Replace");
		add.addActionListener(this);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(cancel);
		buttonPanel.add(add);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2,1));
		mainPanel.add(topPanel);
		mainPanel.add(bottomPanel);
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
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
		if(e.getActionCommand().equals("Replace"))
		{
			int replaceIndex = Integer.parseInt(idHash.get(replaceThis.getSelectedItem()));
			int withIndex = Integer.parseInt(idHash.get(withThis.getSelectedItem()));
			if(replaceIndex != withIndex)
			{
				dbFriend.executeUpdate("UPDATE Children SET most_recent_" + myTable.toLowerCase() + " = " + withIndex + " WHERE most_recent_" + myTable.toLowerCase() + " = " + replaceIndex + ";");
				dbFriend.executeUpdate("UPDATE Program_Data SET " + myTable.toLowerCase() + "_id = " + withIndex + " WHERE " + myTable.toLowerCase() + "_id = " + replaceIndex + ";");
				dbFriend.executeUpdate("DELETE FROM " + myTable + " WHERE id = " + replaceIndex + ";");
				JOptionPane.showMessageDialog(null, replaceThis.getSelectedItem() + " replaced with " + withThis.getSelectedItem(), "Items Replaced", JOptionPane.INFORMATION_MESSAGE);
				this.setVisible(false);
				dbcli.notifyChange();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Cannot replace with itself.", "Bad Replace", JOptionPane.ERROR_MESSAGE);
			}
		}
		if(e.getActionCommand().equals("Cancel"))
		{
			this.setVisible(false);
		}
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
	 * Updates the contents of the window
	 */
	private void update()
	{
		myList = dbFriend.query2DstringRet(myQuery, myColLen);
		idHash = new Hashtable<String,String>();
		for(int a = 0; a < myList.length; a++)
		{
			idHash.put(myList[a][1], myList[a][0]);
		}
		String[] replaceList = new String[myList.length];
		String[] withList = new String[myList.length];		
		for(int a = 0; a < replaceList.length; a++)
		{
			replaceList[a] = myList[a][1];
			withList[a] = myList[a][1];
		}
		topPanel.remove(replaceThis);
		bottomPanel.remove(withThis);
		replaceThis = new JComboBox<String>(replaceList);
		withThis = new JComboBox<String>(withList);
		topPanel.add(replaceThis, BorderLayout.CENTER);
		bottomPanel.add(withThis, BorderLayout.CENTER);
		this.paintAll(this.getGraphics());
	}

	/**
	 * Makes the window visible
	 */
	public void makeVisible()
	{
		update();
		this.setVisible(true);
	}

	/**
	 * Handles when the database has changed
	 */
	public void databaseChanged() 
	{
		update();
	}
}
