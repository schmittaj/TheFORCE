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
import javax.swing.*;
import backend.*;

/**
 * For splitting one reader into two different readers.
 * @author Anthony Schmitt
 *
 */
public class ReaderSplitWindow extends JFrame implements ActionListener, KeyListener
{
	private SecretMenuToggle mainWindow;
	private DBHandler dbFriend;
	private DatabaseChangeListenerImplementer dbcli;
	private String reader1id;
	private JTextField r1FirstName, r2FirstName, r1LastName, r2LastName, r1DOB, r2DOB,
		r1Phone, r2Phone, r1Email, r2Email, r1Parent, r2Parent;
	private JCheckBox r1FNB, r2FNB, r1LNB, r2LNB, r1DOBB, r2DOBB, r1PNB, r2PNB, r1EMB, r2EMB,
		r1PB, r2PB, nonePB;
	private JTextField[] r1programData, r2programData;
	private JCheckBox[] r1PDB, r2PDB;
	private JPanel mainPanel, programDataPanel;
	private int height, width;
	
	/**
	 * Constructor.
	 * @param mw Secret Menu Toggle
	 * @param dbh The database connection
	 * @param dbc Notifier for changes to the database
	 */
	public ReaderSplitWindow(SecretMenuToggle mw, DBHandler dbh, DatabaseChangeListenerImplementer dbc)
	{
		this.mainWindow = mw;
		this.dbFriend = dbh;
		this.dbcli = dbc;
		this.height = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4)*3);
		this.width = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3));
		this.setSize(width, height);
		this.setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/6, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/6);
		this.setTitle("Split Reader");
		init();
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * Initializes the window.
	 */
	protected void init()
	{
		r1FirstName = new JTextField();
		r2FirstName = new JTextField();
		JLabel firstNameLabel = new JLabel("First Name");
		r1LastName = new JTextField();
		r2LastName = new JTextField();
		JLabel lastNameLabel = new JLabel("Last Name");
		r1DOB = new JTextField();
		r2DOB = new JTextField();
		JLabel dobLabel = new JLabel("DOB");
		r1Phone = new JTextField();
		r2Phone = new JTextField();
		JLabel phoneLabel = new JLabel("Phone Number");
		r1Email = new JTextField();
		r2Email = new JTextField();
		JLabel emailLabel = new JLabel("Email");
		r1Parent = new JTextField();
		r2Parent = new JTextField();
		JLabel parentLabel = new JLabel("Parent");
		r1FNB = new JCheckBox("<");
		r2FNB = new JCheckBox(">");
		r1FNB.setSelected(true);
		r1FNB.setEnabled(false);
		r2FNB.setSelected(true);
		r2FNB.setEnabled(false);
		JPanel firstNameButtons = new JPanel();
		firstNameButtons.setLayout(new GridLayout(1,2));
		firstNameButtons.add(r1FNB);
		firstNameButtons.add(r2FNB);
		r1LNB = new JCheckBox("<");
		r2LNB = new JCheckBox(">");
		r1LNB.setSelected(true);
		r1LNB.setEnabled(false);
		r2LNB.setSelected(true);
		r2LNB.setEnabled(false);
		JPanel lastNameButtons = new JPanel();
		lastNameButtons.setLayout(new GridLayout(1,2));
		lastNameButtons.add(r1LNB);
		lastNameButtons.add(r2LNB);
		r1DOBB = new JCheckBox("<");
		r2DOBB = new JCheckBox(">");
		r1DOBB.setSelected(true);
		r1DOBB.setEnabled(false);
		r2DOBB.setSelected(true);
		r2DOBB.setEnabled(false);
		JPanel dobButtons = new JPanel();
		dobButtons.setLayout(new GridLayout(1,2));
		dobButtons.add(r1DOBB);
		dobButtons.add(r2DOBB);
		r1PNB = new JCheckBox("<");
		r2PNB = new JCheckBox(">");
		JPanel phoneButtons = new JPanel();
		phoneButtons.setLayout(new GridLayout(1,2));
		phoneButtons.add(r1PNB);
		phoneButtons.add(r2PNB);
		r1EMB = new JCheckBox("<");
		r2EMB = new JCheckBox(">");
		JPanel emailButtons = new JPanel();
		emailButtons.setLayout(new GridLayout(1,2));
		emailButtons.add(r1EMB);
		emailButtons.add(r2EMB);
		r1PB = new JCheckBox("<");
		r2PB = new JCheckBox(">");
		JPanel parentButtons = new JPanel();
		parentButtons.setLayout(new GridLayout(1,2));
		parentButtons.add(r1PB);
		parentButtons.add(r2PB);
		JPanel firstNamePanel = new JPanel();
		firstNamePanel.setLayout(new BorderLayout());
		firstNamePanel.add(firstNameLabel, BorderLayout.NORTH);
		JPanel fninPanel = new JPanel();
		fninPanel.setLayout(new GridLayout(1,3));
		fninPanel.add(r1FirstName);
		fninPanel.add(firstNameButtons);
		fninPanel.add(r2FirstName);
		firstNamePanel.add(fninPanel, BorderLayout.CENTER);
		JPanel lastNamePanel = new JPanel();
		lastNamePanel.setLayout(new BorderLayout());
		JPanel lninPanel = new JPanel();
		lninPanel.setLayout(new GridLayout(1,3));
		lninPanel.add(r1LastName);
		lninPanel.add(lastNameButtons);
		lninPanel.add(r2LastName);
		lastNamePanel.add(lastNameLabel, BorderLayout.NORTH);
		lastNamePanel.add(lninPanel, BorderLayout.CENTER);
		JPanel dobPanel = new JPanel();
		dobPanel.setLayout(new BorderLayout());
		JPanel dobinPanel = new JPanel();
		dobinPanel.setLayout(new GridLayout(1,3));
		dobinPanel.add(r1DOB);
		dobinPanel.add(dobButtons);
		dobinPanel.add(r2DOB);
		dobPanel.add(dobLabel, BorderLayout.NORTH);
		dobPanel.add(dobinPanel, BorderLayout.CENTER);
		JPanel phonePanel = new JPanel();
		phonePanel.setLayout(new BorderLayout());
		JPanel phoneinPanel = new JPanel();
		phoneinPanel.setLayout(new GridLayout(1,3));
		phoneinPanel.add(r1Phone);
		phoneinPanel.add(phoneButtons);
		phoneinPanel.add(r2Phone);
		phonePanel.add(phoneLabel, BorderLayout.NORTH);
		phonePanel.add(phoneinPanel, BorderLayout.CENTER);
		JPanel emailPanel = new JPanel();
		emailPanel.setLayout(new BorderLayout());
		JPanel emailinPanel = new JPanel();
		emailinPanel.setLayout(new GridLayout(1,3));
		emailinPanel.add(r1Email);
		emailinPanel.add(emailButtons);
		emailinPanel.add(r2Email);
		emailPanel.add(emailLabel, BorderLayout.NORTH);
		emailPanel.add(emailinPanel, BorderLayout.CENTER);
		JPanel parentPanel = new JPanel();
		parentPanel.setLayout(new BorderLayout());
		JPanel parentinPanel = new JPanel();
		parentinPanel.setLayout(new GridLayout(1,3));
		parentinPanel.add(r1Parent);
		parentinPanel.add(parentButtons);
		parentinPanel.add(r2Parent);
		parentPanel.add(parentLabel, BorderLayout.NORTH);
		parentPanel.add(parentinPanel, BorderLayout.CENTER);
		programDataPanel = new JPanel();
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(7,1));
		mainPanel.add(firstNamePanel);
		mainPanel.add(lastNamePanel);
		mainPanel.add(dobPanel);
		mainPanel.add(phonePanel);
		mainPanel.add(emailPanel);
		mainPanel.add(parentPanel);
		mainPanel.add(programDataPanel);
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);
		JButton merge = new JButton("Split");
		merge.addActionListener(this);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1,2));
		buttonsPanel.add(merge);
		buttonsPanel.add(cancel);
		this.add(buttonsPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Sets the reader to split.
	 * @param read1 ID of one of the readers to be merged
	 * @param read2 ID of one of the readers to be merged
	 */
	public void setReader(String read1)
	{
		this.reader1id = read1;
		
		String[] r1info = dbFriend.query1DstringRet("SELECT first_name, last_name, date_of_birth, most_recent_parent, most_recent_email, most_recent_phone FROM Children WHERE id = " + reader1id +";", 6);
		String[] r1pd = dbFriend.query1DstringRet("SELECT year FROM Program_Data WHERE child_id = " + reader1id + " ORDER BY year;");
		
		r1FirstName.setText(r1info[0].trim());
		r1LastName.setText(r1info[1].trim());
		r1DOB.setText(r1info[2].trim());
		r1Parent.setText(r1info[3].trim());
		r1Email.setText(r1info[4].trim());
		r1Phone.setText(r1info[5].trim());
		
		r2FirstName.setText(r1info[0].trim());
		r2LastName.setText(r1info[1].trim());
		r2DOB.setText(r1info[2].trim());
		r2Parent.setText(r1info[3].trim());
		r2Email.setText(r1info[4].trim());
		r2Phone.setText(r1info[5].trim());
		
		
		int uniqueVals = r1pd.length;
				
		r1programData = new JTextField[uniqueVals];
		r2programData = new JTextField[uniqueVals];
		r1PDB = new JCheckBox[uniqueVals];
		r2PDB = new JCheckBox[uniqueVals];
		

		mainPanel.remove(programDataPanel);
		
		programDataPanel = new JPanel();
		programDataPanel.setLayout(new GridLayout(uniqueVals,1));
		
		for(int a = 0; a < uniqueVals; a++)
		{
			r1programData[a] = new JTextField(); 
			r1programData[a].setText(r1pd[a]);
			r1programData[a].setEditable(false);
			r2programData[a] = new JTextField(); 
			r2programData[a].setText(r1pd[a]);
			r2programData[a].setEditable(false);
			r1PDB[a] = new JCheckBox("<");
			r2PDB[a] = new JCheckBox(">");
			JPanel aButtonPanel = new JPanel();
			aButtonPanel.setLayout(new GridLayout(1,2));
			aButtonPanel.add(r1PDB[a]);
			aButtonPanel.add(r2PDB[a]);
			JPanel aPanel = new JPanel();
			aPanel.setLayout(new GridLayout(1,3));
			aPanel.add(r1programData[a]);
			aPanel.add(aButtonPanel);
			aPanel.add(r2programData[a]);
			programDataPanel.add(aPanel);
		}
		
		mainPanel.add(programDataPanel);
		
		this.paintAll(this.getGraphics());
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
		if(e.getActionCommand().equals("Split"))
		{
			boolean errors = false;
			String errorMsg = "";
			
			
			if(r1FirstName.getText().trim().equals(r2FirstName.getText().trim()) && r1LastName.getText().trim().equals(r2LastName.getText().trim()) && r1DOB.getText().trim().equals(r2DOB.getText().trim()))
			{
				errors = true;
				errorMsg += "Two different readers cannot have the same\nfirst name, last name and date of birth.\n"
						+ "if this is correct put a designate them as (1) and (2) after their first names.";	
			}
			if(!errors)
			{
				String newReaderID = "" + (Integer.parseInt(dbFriend.query1DstringRet(Queries.HIGHEST_CHILD_ID)[0]) + 1);
				dbFriend.executeUpdate("UPDATE Children SET first_name = \"" + r1FirstName.getText().trim() + "\" WHERE id = " + reader1id + ";");
				dbFriend.executeUpdate("UPDATE Children SET last_name = \"" + r1LastName.getText().trim() + "\" WHERE id = " + reader1id + ";");
				dbFriend.executeUpdate("UPDATE Children SET date_of_birth = \"" + r1DOB.getText().trim() + "\" WHERE id = " + reader1id + ";");
				
				if(r1PB.isSelected())
				{
					dbFriend.executeUpdate("UPDATE Children SET most_recent_parent = \"" + r1Parent.getText().trim() + "\" WHERE id = " + reader1id + ";");
				}
				if(r1EMB.isSelected())
				{
					dbFriend.executeUpdate("UPDATE Children SET most_recent_email = \"" + r1Email.getText().trim() + "\" WHERE id = " + reader1id + ";");
				}
				if(r1PNB.isSelected())
				{
					dbFriend.executeUpdate("UPDATE Children SET most_recent_phone = \"" + r1Phone.getText().trim() + "\" WHERE id = " + reader1id + ";");
				}
				
				String makeReader2 = "INSERT INTO Children VALUES ("+ newReaderID + ",\"" + r2LastName.getText().trim() + "\",\"" + r2FirstName.getText().trim() + "\",\"" + r2DOB.getText().trim() + "\",";
				if(r2PB.isSelected())
				{
					makeReader2 += "\"" + r2Parent.getText().trim() + "\",";
				}
				else
				{
					makeReader2 +=  "null,";
				}
				makeReader2 +=  "null,";
				if(r2EMB.isSelected())
				{
					makeReader2 += "\"" +r2Email.getText().trim() + "\",";
				}
				else
				{
					makeReader2 +=  "null,";
				}
				if(r2PNB.isSelected())
				{
					makeReader2 += "\"" + r2Phone.getText().trim() + "\",";
				}
				else
				{
					makeReader2 +=  "null,";
				}
				makeReader2 +=  "null";
				makeReader2 += ");";
				
				dbFriend.executeUpdate(makeReader2);
				
				
				for(int a = 0; a < r2PDB.length; a++)
				{
					if(r2PDB[a].isSelected())
					{
						String tmpYear = r1programData[a].getText();
						String[] results = dbFriend.query1DstringRet("Select * FROM Program_Data WHERE year = " + tmpYear + " AND child_id = " + reader1id + ";",19);
						for(int b = 0; b < results.length; b++)
						{
							if(results[b].length() == 0)
							{
								results[b] = "null";
							}
						}
						String pgd = "INSERT INTO Program_Data VALUES (" + newReaderID + "," + results[1] + "," + results[2] + ","+ results[3] + ","+ results[4] + ","+ results[5] + ","
								+ results[6] + ","+ results[7] + ","+ results[8] + ","+ results[9] + ","+ results[10] + ","+ results[11] + ","+ results[12] + ","+ results[13] + ","
										+ results[14] + ","+ results[15] + ","+ results[16] + ","+ results[17] + ","+ results[18] + ");";
						dbFriend.executeUpdate(pgd);
						
					}
				}
				for(int a = 0; a < r1PDB.length; a++)
				{
					if(!r1PDB[a].isSelected())
					{
						String tmpYear = r1programData[a].getText();
						dbFriend.executeUpdate("DELETE FROM Program_Data WHERE year = " + tmpYear + " AND child_id = " + reader1id + ";");
					}
				}
				
				
				String[][] results = dbFriend.query2DstringRet("SELECT school_id, city_id FROM Program_Data WHERE child_id = " + reader1id + ";",2);
				if(results != null)
				{
					if(results[results.length-1][0].length() == 0)
					{
						results[results.length-1][0] = "null";
					}
					if(results[results.length-1][1].length() == 0)
					{
						results[results.length-1][1] = "null";
					}
					dbFriend.executeUpdate("UPDATE Children SET most_recent_school = " + results[results.length-1][0] + " WHERE id = " + reader1id + ";");
					dbFriend.executeUpdate("UPDATE Children SET most_recent_city = " + results[results.length-1][1] + " WHERE id = " + reader1id + ";");
				}	
				results = dbFriend.query2DstringRet("SELECT school_id, city_id FROM Program_Data WHERE child_id = " + newReaderID + ";",2);
				if(results != null)
				{
					if(results[results.length-1][0].length() == 0)
					{
						results[results.length-1][0] = "null";
					}
					if(results[results.length-1][1].length() == 0)
					{
						results[results.length-1][1] = "null";
					}
					dbFriend.executeUpdate("UPDATE Children SET most_recent_school = " + results[results.length-1][0] + " WHERE id = " + newReaderID + ";");
					dbFriend.executeUpdate("UPDATE Children SET most_recent_city = " + results[results.length-1][1] + " WHERE id = " + newReaderID + ";");
				}
				
				dbcli.notifyChange();
				this.setVisible(false);
			}
			else
			{
				JOptionPane.showMessageDialog(null,errorMsg,"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		if(e.getActionCommand().equals("Cancel"))
		{
			this.setVisible(false);
		}
	}

	public void updateDatabaseConnection(DBHandler dbh)
	{
		dbFriend = dbh;
	}


}
