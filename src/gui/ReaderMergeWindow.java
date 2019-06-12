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
import java.util.Date;

import javax.swing.*;
import backend.*;

/**
 * This window is for merging two readers into one reader, for in the case of a duplicate entry with 
 * different program data information.
 * @author Anthony Schmitt
 *
 */
public class ReaderMergeWindow extends JFrame implements ActionListener, KeyListener
{
	private SecretMenuToggle mainWindow;
	private DBHandler dbFriend;
	private DatabaseChangeListenerImplementer dbcli;
	private String reader1id, reader2id;
	private JTextField r1FirstName, r2FirstName, r1LastName, r2LastName, r1DOB, r2DOB,
		r1Phone, r2Phone, r1Email, r2Email, r1Parent, r2Parent;
	private JRadioButton r1FNB, r2FNB, r1LNB, r2LNB, r1DOBB, r2DOBB, r1PNB, r2PNB, nonePNB, r1EMB, r2EMB,
		noneEMB, r1PB, r2PB, nonePB;
	private JTextField[] r1programData, r2programData;
	private JRadioButton[] r1PDB, r2PDB, nonePDB;
	private JPanel mainPanel, programDataPanel;
	private int height, width;
	
	/**
	 * Constructor.
	 * @param mw Secret Menu Toggle
	 * @param dbh The database connection
	 * @param dbc Notifier for changes to the database
	 */
	public ReaderMergeWindow(SecretMenuToggle mw, DBHandler dbh, DatabaseChangeListenerImplementer dbc)
	{
		this.mainWindow = mw;
		this.dbFriend = dbh;
		this.dbcli = dbc;
		this.height = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4)*3);
		this.width = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3));
		this.setSize(width, height);
		this.setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/6, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/6);
		this.setTitle("Merge Readers");
		init();
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * Initializes the window.
	 */
	protected void init()
	{
		r1FirstName = new JTextField();
		r1FirstName.setEditable(false);
		r2FirstName = new JTextField();
		r2FirstName.setEditable(false);
		JLabel firstNameLabel = new JLabel("First Name");
		r1LastName = new JTextField();
		r1LastName.setEditable(false);
		r2LastName = new JTextField();
		r2LastName.setEditable(false);
		JLabel lastNameLabel = new JLabel("Last Name");
		r1DOB = new JTextField();
		r1DOB.setEditable(false);
		r2DOB = new JTextField();
		r2DOB.setEditable(false);
		JLabel dobLabel = new JLabel("DOB");
		r1Phone = new JTextField();
		r1Phone.setEditable(false);
		r2Phone = new JTextField();
		r2Phone.setEditable(false);
		JLabel phoneLabel = new JLabel("Phone Number");
		r1Email = new JTextField();
		r1Email.setEditable(false);
		r2Email = new JTextField();
		r2Email.setEditable(false);
		JLabel emailLabel = new JLabel("Email");
		r1Parent = new JTextField();
		r1Parent.setEditable(false);
		r2Parent = new JTextField();
		r2Parent.setEditable(false);
		JLabel parentLabel = new JLabel("Parent");
		r1FNB = new JRadioButton("<");
		r2FNB = new JRadioButton(">");
		ButtonGroup firstNameGroup = new ButtonGroup();
		firstNameGroup.add(r1FNB);
		firstNameGroup.add(r2FNB);
		JPanel firstNameButtons = new JPanel();
		firstNameButtons.setLayout(new GridLayout(1,3));
		firstNameButtons.add(r1FNB);
		JRadioButton fnemptyButton = new JRadioButton();
		fnemptyButton.setEnabled(false);
		firstNameButtons.add(fnemptyButton);
		firstNameButtons.add(r2FNB);
		r1LNB = new JRadioButton("<");
		r2LNB = new JRadioButton(">");
		ButtonGroup lastNameGroup = new ButtonGroup();
		lastNameGroup.add(r1LNB);
		lastNameGroup.add(r2LNB);
		JPanel lastNameButtons = new JPanel();
		lastNameButtons.setLayout(new GridLayout(1,3));
		lastNameButtons.add(r1LNB);
		JRadioButton lnemptyButton = new JRadioButton();
		lnemptyButton.setEnabled(false);
		lastNameButtons.add(lnemptyButton);
		lastNameButtons.add(r2LNB);
		r1DOBB = new JRadioButton("<");
		r2DOBB = new JRadioButton(">");
		ButtonGroup dobGroup = new ButtonGroup();
		dobGroup.add(r1DOBB);
		dobGroup.add(r2DOBB);
		JPanel dobButtons = new JPanel();
		dobButtons.setLayout(new GridLayout(1,3));
		dobButtons.add(r1DOBB);
		JRadioButton dobemptyButton = new JRadioButton();
		dobemptyButton.setEnabled(false);
		dobButtons.add(dobemptyButton);
		dobButtons.add(r2DOBB);
		r1PNB = new JRadioButton("<");
		r2PNB = new JRadioButton(">");
		nonePNB = new JRadioButton("-");
		ButtonGroup phoneGroup = new ButtonGroup();
		phoneGroup.add(r1PNB);
		phoneGroup.add(r2PNB);
		phoneGroup.add(nonePNB);
		JPanel phoneButtons = new JPanel();
		phoneButtons.setLayout(new GridLayout(1,3));
		phoneButtons.add(r1PNB);
		phoneButtons.add(nonePNB);
		phoneButtons.add(r2PNB);
		r1EMB = new JRadioButton("<");
		r2EMB = new JRadioButton(">");
		noneEMB = new JRadioButton("-");
		ButtonGroup emailGroup = new ButtonGroup();
		emailGroup.add(r1EMB);
		emailGroup.add(noneEMB);
		emailGroup.add(r2EMB);
		JPanel emailButtons = new JPanel();
		emailButtons.setLayout(new GridLayout(1,3));
		emailButtons.add(r1EMB);
		emailButtons.add(noneEMB);
		emailButtons.add(r2EMB);
		r1PB = new JRadioButton("<");
		r2PB = new JRadioButton(">");
		nonePB = new JRadioButton("-");
		ButtonGroup parentGroup = new ButtonGroup();
		parentGroup.add(r1PB);
		parentGroup.add(r2PB);
		parentGroup.add(nonePB);
		JPanel parentButtons = new JPanel();
		parentButtons.setLayout(new GridLayout(1,3));
		parentButtons.add(r1PB);
		parentButtons.add(nonePB);
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
		JButton merge = new JButton("Merge");
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
	 * Sets the two readers to merge.
	 * @param read1 ID of one of the readers to be merged
	 * @param read2 ID of one of the readers to be merged
	 */
	public void setReaders(String read1, String read2)
	{
		this.reader1id = read1;
		this.reader2id = read2;
		
		String[] r1info = dbFriend.query1DstringRet("SELECT first_name, last_name, date_of_birth, most_recent_parent, most_recent_email, most_recent_phone FROM Children WHERE id = " + reader1id +";", 6);
		String[] r2info = dbFriend.query1DstringRet("SELECT first_name, last_name, date_of_birth, most_recent_parent, most_recent_email, most_recent_phone FROM Children WHERE id = " + reader2id +";", 6);
		String[] r1pd = dbFriend.query1DstringRet("SELECT year FROM Program_Data WHERE child_id = " + reader1id + " ORDER BY year;");
		String[] r2pd = dbFriend.query1DstringRet("SELECT year FROM Program_Data WHERE child_id = " + reader2id + " ORDER BY year;");
		
		r1FirstName.setText(r1info[0].trim());
		r1LastName.setText(r1info[1].trim());
		r1DOB.setText(r1info[2].trim());
		r1Parent.setText(r1info[3].trim());
		r1Email.setText(r1info[4].trim());
		r1Phone.setText(r1info[5].trim());
		
		r2FirstName.setText(r2info[0].trim());
		r2LastName.setText(r2info[1].trim());
		r2DOB.setText(r2info[2].trim());
		r2Parent.setText(r2info[3].trim());
		r2Email.setText(r2info[4].trim());
		r2Phone.setText(r2info[5].trim());
		
		if(r1DOB.getText().length() == 0)
		{
			r2DOBB.setSelected(true);
		}
		else if(r2DOB.getText().length() == 0)
		{
			r1DOBB.setSelected(true);
		}
		
		if(r1Parent.getText().length() == 0 && r2Parent.getText().length() == 0)
		{
			nonePB.setSelected(true);
		}
		else if(r1Parent.getText().length() == 0)
		{
			
			r2PB.setSelected(true);
		}
		else if(r2Parent.getText().length() == 0)
		{
			r1PB.setSelected(true);
		}
		
		if(r1Email.getText().length() == 0 && r2Email.getText().length() == 0)
		{
			noneEMB.setSelected(true);
		}
		else if(r1Email.getText().length() == 0)
		{
			r2EMB.setSelected(true);
		}
		else if(r2Email.getText().length() == 0)
		{
			r1EMB.setSelected(true);
		}
		
		if(r1Phone.getText().length() == 0 && r2Phone.getText().length() == 0)
		{
			nonePNB.setSelected(true);
		}
		else if(r1Phone.getText().length() == 0)
		{
			r2PNB.setSelected(true);
		}
		else if(r2Phone.getText().length() == 0)
		{
			r1PNB.setSelected(true);
		}
		
		int uniqueVals = r1pd.length;
		int lowestYear = 9999;
		for(int a = 0; a < r2pd.length; a++)
		{
			boolean matchFound = false;
			if(Integer.parseInt(r2pd[a]) < lowestYear)
			{
				lowestYear = Integer.parseInt(r2pd[a]);
			}
			for(int b = 0; b < r1pd.length; b++)
			{
				if(r1pd[b].equals(r2pd[a]))
				{
					matchFound = true;
				}
				if(Integer.parseInt(r1pd[b]) < lowestYear)
				{
					lowestYear = Integer.parseInt(r1pd[b]);
				}
			}
			if(!matchFound)
			{
				uniqueVals++;
			}
		}
		
		r1programData = new JTextField[uniqueVals];
		r2programData = new JTextField[uniqueVals];
		r1PDB = new JRadioButton[uniqueVals];
		r2PDB = new JRadioButton[uniqueVals];
		nonePDB = new JRadioButton[uniqueVals];

		mainPanel.remove(programDataPanel);
		
		programDataPanel = new JPanel();
		programDataPanel.setLayout(new GridLayout(uniqueVals,1));
		
		int r1ctr = 0;
		int r2ctr = 0;
		if(r1pd.length > 0 && r2pd.length > 0)
		{
			for(int a = 0; a < uniqueVals; a++)
			{
				if((r1ctr < r1pd.length && r1pd[r1ctr].trim().equals(""+lowestYear)) || (r2ctr < r2pd.length && r2pd[r2ctr].trim().equals(""+lowestYear)))
				{
					r1programData[a] = new JTextField(); 
					if(r1ctr < r1pd.length && r1pd[r1ctr].trim().equals(""+lowestYear))
					{
						r1programData[a].setText(""+lowestYear);
						r1ctr++;
					}
					r1programData[a].setEditable(false);
					r2programData[a] = new JTextField(); 
					if(r2ctr < r2pd.length && r2pd[r2ctr].trim().equals(""+lowestYear))
					{
						r2programData[a].setText(""+lowestYear);
						r2ctr++;
					}
					r2programData[a].setEditable(false);
					lowestYear++;
					r1PDB[a] = new JRadioButton("<");
					r2PDB[a] = new JRadioButton(">");
					nonePDB[a] = new JRadioButton("-");
					ButtonGroup aButtonGroup = new ButtonGroup();
					aButtonGroup.add(r1PDB[a]);
					aButtonGroup.add(r2PDB[a]);
					aButtonGroup.add(nonePDB[a]);
					JPanel aButtonPanel = new JPanel();
					aButtonPanel.setLayout(new GridLayout(1,3));
					aButtonPanel.add(r1PDB[a]);
					aButtonPanel.add(nonePDB[a]);
					aButtonPanel.add(r2PDB[a]);
					if(r1programData[a].getText().length() == 0 && r2programData[a].getText().length() == 0)
					{
						nonePDB[a].setSelected(true);
					}
					else if(r1programData[a].getText().length() == 0)
					{
						r2PDB[a].setSelected(true);
					}
					else if(r2programData[a].getText().length() == 0)
					{
						r1PDB[a].setSelected(true);
					}
					JPanel aPanel = new JPanel();
					aPanel.setLayout(new GridLayout(1,3));
					aPanel.add(r1programData[a]);
					aPanel.add(aButtonPanel);
					aPanel.add(r2programData[a]);
					programDataPanel.add(aPanel);
				}
				else
				{
					a -= 1;
					lowestYear++;
				}
			}
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
		if(e.getActionCommand().equals("Merge"))
		{
			boolean errors = false;
			String errorMsg = "";
			if(!r1FNB.isSelected() && !r2FNB.isSelected()) 
			{
				errors = true;
				errorMsg += "No first name selected\n";
			}
			if(!r1LNB.isSelected() && !r2LNB.isSelected())
			{
				errors = true;
				errorMsg += "No last name selected\n";	
			}
			if(!r1DOBB.isSelected() && !r2DOBB.isSelected())
			{
				errors = true;
				errorMsg += "No date of birth selected\n";	
			}
			if(!errors)
			{
				if(r2FNB.isSelected())
				{
					dbFriend.executeUpdate("UPDATE Children SET first_name = \"" + r2FirstName.getText().trim() + "\" WHERE id = " + reader1id + ";");
				}
				if(r2LNB.isSelected())
				{
					dbFriend.executeUpdate("UPDATE Children SET last_name = \"" + r2LastName.getText().trim() + "\" WHERE id = " + reader1id + ";");
				}
				if(r2DOBB.isSelected())
				{
					dbFriend.executeUpdate("UPDATE Children SET date_of_birth = \"" + r2DOB.getText().trim() + "\" WHERE id = " + reader1id + ";");
				}
				if(r2PB.isSelected())
				{
					dbFriend.executeUpdate("UPDATE Children SET most_recent_parent = \"" + r2Parent.getText().trim() + "\" WHERE id = " + reader1id + ";");
				}
				if(nonePB.isSelected())
				{
					dbFriend.executeUpdate("UPDATE Children SET most_recent_parent = null WHERE id = " + reader1id + ";");
				}
				if(r2EMB.isSelected())
				{
					dbFriend.executeUpdate("UPDATE Children SET most_recent_email = \"" + r2Email.getText().trim() + "\" WHERE id = " + reader1id + ";");
				}
				if(noneEMB.isSelected())
				{
					dbFriend.executeUpdate("UPDATE Children SET most_recent_email = null WHERE id = " + reader1id + ";");
				}
				if(r2PNB.isSelected())
				{
					dbFriend.executeUpdate("UPDATE Children SET most_recent_phone = \"" + r2Phone.getText().trim() + "\" WHERE id = " + reader1id + ";");
				}
				if(nonePNB.isSelected())
				{
					dbFriend.executeUpdate("UPDATE Children SET most_recent_phone = null WHERE id = " + reader1id + ";");
				}
				
				for(int a = 0; a < r2PDB.length; a++)
				{
					if(r2PDB[a].isSelected())
					{
						if(r1programData[a].getText().length() != 0) 
						{
							String tmpYear = r1programData[a].getText();
							dbFriend.executeUpdate("DELETE FROM Program_Data WHERE year = " + tmpYear + " AND child_id = " + reader1id + ";");
						}
						String tmpYear = r2programData[a].getText();
						dbFriend.executeUpdate("UPDATE Program_Data SET child_id = " + reader1id + " WHERE year = " + tmpYear + " AND child_id = " + reader2id + ";");
					}
					else
					{
						if(r2programData[a].getText().length() != 0)
						{
							String tmpYear = r2programData[a].getText();
							dbFriend.executeUpdate("DELETE FROM Program_Data WHERE year = " + tmpYear + " AND child_id = " + reader2id + ";");
						}
					}
				}
				for(int a = 0; a < nonePDB.length; a++)
				{
					if(nonePDB[a].isSelected())
					{
						if(r1programData[a].getText().length() != 0) 
						{
							String tmpYear = r1programData[a].getText();
							dbFriend.executeUpdate("DELETE FROM Program_Data WHERE year = " + tmpYear + " AND child_id = " + reader1id + ";");
						}
						if(r2programData[a].getText().length() != 0) 
						{
							String tmpYear = r2programData[a].getText();
							dbFriend.executeUpdate("DELETE FROM Program_Data WHERE year = " + tmpYear + " AND child_id = " + reader2id + ";");
						}
					}
				}
				dbFriend.executeUpdate("DELETE FROM Children WHERE id = " + reader2id + ";");
				
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

	/**
	 * Updates the connection to the database.
	 * @param dbh New database connection
	 */
	public void updateDatabaseConnection(DBHandler dbh)
	{
		dbFriend = dbh;
	}

}
