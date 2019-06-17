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
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Window for checking in readers
 * @author Anthony Schmitt
 *
 */
public class CheckInWindow extends JFrame implements ActionListener, KeyListener
{
	private String currentID;
	private JSpinner highestSpinner;
	private JCheckBox ec1,ec2,ec3,ec4,ec5,ec6,ec7,ec8,ec9,ec10,poolPass;
	private JButton done, cancel;
	private DBHandler dbFriend;
	private String firstName, lastName;
	private JLabel mainLabel;
	private DatabaseChangeListenerImplementer dbcli;
	private int width, height;
	private SecretMenuToggle mainWindow;
	
	/**
	 * Constructor
	 * @param dbh Database Handler
	 * @param curr Current reader
	 * @param dbc Implementer for DB Change Listener
	 * @param mainWin Main Window
	 */
	public CheckInWindow(DBHandler dbh, String curr, DatabaseChangeListenerImplementer dbc, SecretMenuToggle mainWin)
	{
		this.dbFriend = dbh;
		this.currentID = curr;
		this.dbcli = dbc;
		this.width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/4);
		this.height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2);
		init();
		this.mainWindow = mainWin;
		this.addKeyListener(this);
		this.setFocusable(true);
	}
	
	/**
	 * Initializes window
	 */
	private void init()
	{
		this.setTitle("Check In Reader");
		firstName = dbFriend.query1DstringRet("SELECT first_name FROM Children WHERE ID = " + currentID + ";")[0];
		lastName = dbFriend.query1DstringRet("SELECT last_name FROM Children WHERE ID = " + currentID + ";")[0];
		mainLabel = new JLabel("Reader: " + firstName + " " + lastName);
		mainLabel.setHorizontalAlignment(JLabel.CENTER);
		JLabel highestBoxLabel = new JLabel("Highest Box Completed:");
		highestBoxLabel.setHorizontalAlignment(JLabel.RIGHT);
		highestBoxLabel.setBorder(new EmptyBorder(0,0,0,20));
		String[] spinnerValues = {"0","1","2","3","4","5","6","7","8","9","10"};
		SpinnerListModel highestModel = new SpinnerListModel(spinnerValues);
		highestSpinner = new JSpinner(highestModel);
		highestSpinner.setFont(highestSpinner.getFont().deriveFont(Font.BOLD, 20));
		JPanel spinnerPanel = new JPanel();
		spinnerPanel.setLayout(new GridLayout(1,2));
		highestBoxLabel.setBorder(new EmptyBorder(0,10,0,10));
		spinnerPanel.add(highestBoxLabel);
		highestSpinner.setPreferredSize(new Dimension(60,20));
		highestSpinner.setMinimumSize(new Dimension(60,20));
		highestSpinner.setBorder(new EmptyBorder(0,30,0,30));
		spinnerPanel.add(highestSpinner);
		spinnerPanel.setBorder(new EmptyBorder(0,0,0,10));
		ec1 = new JCheckBox("Extra Challenge Box 1");
		ec2 = new JCheckBox("Extra Challenge Box 2");
		ec3 = new JCheckBox("Extra Challenge Box 3");
		ec4 = new JCheckBox("Extra Challenge Box 4");
		ec5 = new JCheckBox("Extra Challenge Box 5");
		ec6 = new JCheckBox("Extra Challenge Box 6");
		ec7 = new JCheckBox("Extra Challenge Box 7");
		ec8 = new JCheckBox("Extra Challenge Box 8");
		ec9 = new JCheckBox("Extra Challenge Box 9");
		ec10 = new JCheckBox("Extra Challenge Box 10");
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
		poolPass = new JCheckBox("Pool Pass Given?");
		done = new JButton("Done");
		done.addActionListener(this);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(cancel);
		buttonPanel.add(done);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(4,1));
		mainPanel.add(mainLabel);
		mainPanel.add(spinnerPanel);
		mainPanel.add(extraPanel);
		poolPass.setBorder(new EmptyBorder(0,100,0,0));
		mainPanel.add(poolPass);
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4);
		this.setSize(width, height);
		this.setResizable(false);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * Sets the current reader
	 * @param curr Current reader's ID
	 */
	public void setCurrentID(String curr)
	{
		this.currentID = curr;
		firstName = dbFriend.query1DstringRet("SELECT first_name FROM Children WHERE ID = " + currentID + ";")[0];
		lastName = dbFriend.query1DstringRet("SELECT last_name FROM Children WHERE ID = " + currentID + ";")[0];	

		mainLabel.setText("Reader: " + firstName + " " + lastName);
		Date d = new Date(System.currentTimeMillis());
		int year = d.getYear()+1900;
		
		String[][] result = {{"","","","","","","","","","","","","","","","","",""}}; 
		result = dbFriend.query2DstringRet("SELECT * FROM Program_Data WHERE child_id = " + currentID + " AND year = " + year + ";", 18);

		
		highestSpinner.setValue(result[0][6]); //highest level
		
		uncheckBoxes();
		
		if(Integer.parseInt(result[0][7]) == 1)
		{
			ec1.setSelected(true);
		}
		if(Integer.parseInt(result[0][8]) == 1)
		{
			ec2.setSelected(true);
		}
		if(Integer.parseInt(result[0][9]) == 1)
		{
			ec3.setSelected(true);
		}
		if(Integer.parseInt(result[0][10]) == 1)
		{
			ec4.setSelected(true);
		}
		if(Integer.parseInt(result[0][11]) == 1)
		{
			ec5.setSelected(true);
		}
		if(Integer.parseInt(result[0][12]) == 1)
		{
			ec6.setSelected(true);
		}
		if(Integer.parseInt(result[0][13]) == 1)
		{
			ec7.setSelected(true);
		}
		if(Integer.parseInt(result[0][14]) == 1)
		{
			ec8.setSelected(true);
		}
		if(Integer.parseInt(result[0][15]) == 1)
		{
			ec9.setSelected(true);
		}
		if(Integer.parseInt(result[0][16]) == 1)
		{
			ec10.setSelected(true);
		}
		if(Integer.parseInt(result[0][17]) == 1)
		{
			poolPass.setSelected(true);
		}
	}
	
	/**
	 * Un-checks all the check boxes
	 */
	private void uncheckBoxes()
	{	
		ec1.setSelected(false);
		ec2.setSelected(false);
		ec3.setSelected(false);
		ec4.setSelected(false);
		ec5.setSelected(false);
		ec6.setSelected(false);
		ec7.setSelected(false);
		ec8.setSelected(false);
		ec9.setSelected(false);
		ec10.setSelected(false);
		poolPass.setSelected(false);
	}
	
	/**
	 * Action Hanlder
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("Done"))
		{
			Date d = new Date(System.currentTimeMillis());
			int year = d.getYear()+1900;
			String valueHolder = (String) highestSpinner.getValue();
			dbFriend.executeUpdate("UPDATE Program_Data SET highest_level_normal = " + valueHolder + " WHERE child_id = " + currentID + " and year = " + year + ";");
			if(ec1.isSelected())
			{
				valueHolder = "true";
			}
			else
			{
				valueHolder = "false";
			}
			dbFriend.executeUpdate("UPDATE Program_Data SET extra_one = " + valueHolder + " WHERE child_id = " + currentID + " and year = " + year + ";");
			if(ec2.isSelected())
			{
				valueHolder = "true";
			}
			else
			{
				valueHolder = "false";
			}
			dbFriend.executeUpdate("UPDATE Program_Data SET extra_two = " + valueHolder + " WHERE child_id = " + currentID + " and year = " + year + ";");
			if(ec3.isSelected())
			{
				valueHolder = "true";
			}
			else
			{
				valueHolder = "false";
			}
			dbFriend.executeUpdate("UPDATE Program_Data SET extra_three = " + valueHolder + " WHERE child_id = " + currentID + " and year = " + year + ";");
			if(ec4.isSelected())
			{
				valueHolder = "true";
			}
			else
			{
				valueHolder = "false";
			}
			dbFriend.executeUpdate("UPDATE Program_Data SET extra_four = " + valueHolder + " WHERE child_id = " + currentID + " and year = " + year + ";");
			if(ec5.isSelected())
			{
				valueHolder = "true";
			}
			else
			{
				valueHolder = "false";
			}
			dbFriend.executeUpdate("UPDATE Program_Data SET extra_five = " + valueHolder + " WHERE child_id = " + currentID + " and year = " + year + ";");
			if(ec6.isSelected())
			{
				valueHolder = "true";
			}
			else
			{
				valueHolder = "false";
			}
			dbFriend.executeUpdate("UPDATE Program_Data SET extra_six = " + valueHolder + " WHERE child_id = " + currentID + " and year = " + year + ";");
			if(ec7.isSelected())
			{
				valueHolder = "true";
			}
			else
			{
				valueHolder = "false";
			}
			dbFriend.executeUpdate("UPDATE Program_Data SET extra_seven = " + valueHolder + " WHERE child_id = " + currentID + " and year = " + year + ";");
			if(ec8.isSelected())
			{
				valueHolder = "true";
			}
			else
			{
				valueHolder = "false";
			}
			dbFriend.executeUpdate("UPDATE Program_Data SET extra_eight = " + valueHolder + " WHERE child_id = " + currentID + " and year = " + year + ";");
			if(ec9.isSelected())
			{
				valueHolder = "true";
			}
			else
			{
				valueHolder = "false";
			}
			dbFriend.executeUpdate("UPDATE Program_Data SET extra_nine = " + valueHolder + " WHERE child_id = " + currentID + " and year = " + year + ";");
			if(ec10.isSelected())
			{
				valueHolder = "true";
			}
			else
			{
				valueHolder = "false";
			}
			dbFriend.executeUpdate("UPDATE Program_Data SET extra_ten = " + valueHolder + " WHERE child_id = " + currentID + " and year = " + year + ";");
			if(poolPass.isSelected())
			{
				valueHolder = "true";
			}
			else
			{
				valueHolder = "false";
			}
			dbFriend.executeUpdate("UPDATE Program_Data SET pool_pass_given = " + valueHolder + " WHERE child_id = " + currentID + " and year = " + year + ";");
			JOptionPane.showMessageDialog(null, firstName + " " + lastName + " progress has been updated.", "Reader Checked In", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(false);
			dbcli.notifyChange();
		}
		else if(e.getActionCommand().equals("Cancel"))
		{
			this.setVisible(false);
		}
	}
	
	/**
	 * Updates database connection
	 * @param dbh
	 */
	public void updateDatabaseConnection(DBHandler dbh)
	{
		dbFriend = dbh;
	}

	/**
	 * Typed key handler
	 */
	public void keyTyped(KeyEvent e) 
	{
	}

	/**
	 * Pressed key handler
	 */
	public void keyPressed(KeyEvent e) 
	{
	}

	/**
	 * Released key handler
	 */
	public void keyReleased(KeyEvent e) 
	{
		if((e.getKeyCode() == KeyEvent.VK_L) &(e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK))
		{
			mainWindow.secretMenuKeyboardPress();
		}
	}
}
