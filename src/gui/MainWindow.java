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
import java.awt.image.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.*;

import backend.*;

/**
 * This is the main window for the program, holds all the internal windows.
 * @author Anthony Schmitt
 *
 */
public class MainWindow extends JFrame implements ActionListener, KeyListener, SecretMenuToggle
{
	private JMenuBar mBar;
	private int height, width;
	private JDesktopPane desktop;
	private String title = "SPPL Summer Reading Program, Program";
	private KidFind kFind;
	private DBHandler dbFriend;
	private String dbPath = "";
	private DatabaseChangeListenerImplementer dbcli;
	private ReportBuilderWindow repBuildWin;
	private boolean databaseOpen, secretMenuVisible;
	private JComboBox<String> reportSelectBox;
	private String[] reportSelection;
	private JMenu secretMenu;
	private Adder cityAdd, schoolAdd;
	private MergeSimpleWindow mergeSchoolWin, mergeCityWin;
	private ReaderStats readStat;
	private KidAdd readAdd;
	private JCheckBoxMenuItem editProgramData, mergeReaders, splitReader;
	private QuickStatWindow quickStat;
	
	/**
	 * Constructor.
	 */
	public MainWindow()
	{
		dbcli = new DatabaseChangeListenerImplementer();
		databaseOpen = false;
		secretMenuVisible = false;
		File dbLocFile = new File("databaseLocation.loc");
		if(dbLocFile.exists())
		{
			try 
			{
				BufferedReader dbLocRead = new BufferedReader(new FileReader(dbLocFile));
				String location = dbLocRead.readLine().trim();
				dbLocRead.close();
				if(!location.isEmpty())
				{
					dbPath = location;
					setDatabase();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "No Previous Database File Located. Please select a database file.", "Missing Database File", JOptionPane.WARNING_MESSAGE);
				}
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "No Previous Database File Located. Please select a database file.", "Missing Database File", JOptionPane.WARNING_MESSAGE);
		}
		//setDatabase();
		//setUpWindows();
		init();
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setAutoRequestFocus(true);
	}
	
	/**
	 * Sets the database for the program
	 */
	public void setDatabase()
	{
		dbFriend = new DBHandler(dbPath);
		
		String dbfile = dbPath.substring(dbPath.lastIndexOf('\\')+1);
		if(!dbfile.toLowerCase().contains("test"))
		{
			Constants.regularDB = true;
		}
		else
		{
			Constants.regularDB = false;
		}
		
		if(databaseOpen)
		{
			kFind.updateDatabaseConnection(dbFriend);
			repBuildWin.updateDatabaseConnection(dbFriend);
			cityAdd.updateDatabaseConnection(dbFriend);
			schoolAdd.updateDatabaseConnection(dbFriend);
			mergeSchoolWin.updateDatabaseConnection(dbFriend);
			mergeCityWin.updateDatabaseConnection(dbFriend);
			readStat.updateDatabaseConnection(dbFriend);
			quickStat.updateDatabaseConnection(dbFriend);
		}
		else
		{
			setUpWindows();
		}
		doBackup();
		title = "The FORCE                                              " + dbPath;
		this.setTitle(title);
		databaseOpen = true;
	}
	
	/**
	 * Closes the database connection. Currently not used (the Database Handler takes care of it).
	 */
	public void closeDatabaseConnection()
	{
		//dbFriend.closeConnection();
	}
	
	/**
	 * Initializes the object.
	 */
	protected void init()
	{
		BufferedImage img = null;
		try 
		{
			img = ImageIO.read(new File("book_icon.png"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		this.setIconImage(img);
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()-35;
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		setUpMenus();
		desktop = new JDesktopPane();
		this.setContentPane(desktop);
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		if(databaseOpen)
		{
		  setUpWindows();
		}
		//setUpWindows();
		this.setJMenuBar(mBar);
		this.setSize(new Dimension(width,height));
		this.setTitle(title);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Sets up all of the windows. 
	 */
	private void setUpWindows()
	{
		kFind = new KidFind(dbFriend, dbcli, this);
		this.add(kFind);
		kFind.setVisible(false);
		dbcli.addListener(kFind);
		repBuildWin = new ReportBuilderWindow(dbFriend, this);
		this.add(repBuildWin);
		dbcli.addListener(repBuildWin);
		repBuildWin.setVisible(false);
		readAdd = new KidAdd(dbFriend, this, dbcli);
		readAdd.setVisible(false);
		cityAdd = new Adder(dbFriend, this, dbcli,"City Adder",Queries.ALL_CITIES_PL_IDS, Queries.ALL_CITIES_PL_IDS_COL_LEN,"City");
		schoolAdd = new Adder(dbFriend, this, dbcli,"School/Daycare Adder",Queries.ALL_SCHOOLS_PL_IDS, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN,"School");
		mergeSchoolWin = new MergeSimpleWindow(dbFriend, this, dbcli, "Merge Schools", Queries.ALL_SCHOOLS_PL_IDS_ALPHA, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN, "School");
		mergeCityWin = new MergeSimpleWindow(dbFriend, this, dbcli, "Merge Cities", Queries.ALL_CITIES_PL_IDS_ALPHA, Queries.ALL_CITIES_PL_IDS_COL_LEN,"City");
		readStat = new ReaderStats(dbFriend,this);
		quickStat = new QuickStatWindow(dbFriend,this);
		quickStat.setVisible(false);
		this.add(quickStat);
	}

	/**
	 * Sets up the menus.
	 */
	private void setUpMenus()
	{
		secretMenu = new JMenu("Admin Tools");
		JMenuItem mergeCities = new JMenuItem("Merge Cities");
		mergeCities.addActionListener(this);
		secretMenu.add(mergeCities);
		JMenuItem mergeSchools = new JMenuItem("Merge Schools");
		mergeSchools.addActionListener(this);
		secretMenu.add(mergeSchools);
		editProgramData = new JCheckBoxMenuItem("Edit Program Data",false);
		editProgramData.addActionListener(this);
		secretMenu.add(editProgramData);
		mergeReaders = new JCheckBoxMenuItem("Merge Readers",false);
		mergeReaders.addActionListener(this);
		secretMenu.add(mergeReaders);
		splitReader = new JCheckBoxMenuItem("Split Reader",false);
		splitReader.addActionListener(this);
		secretMenu.add(splitReader);

		
		JMenu menu1 = new JMenu("File");
		JMenuItem itm1 = new JMenuItem("Set Database Path");
		itm1.addActionListener(this);
		JMenuItem setDBalt = new JMenuItem("Set Database Path FileShare");
		setDBalt.addActionListener(this);
		JMenuItem itm2 = new JMenuItem("Exit");
		itm2.addActionListener(this);
		JMenu menu2 = new JMenu("Reading Program");
		JMenuItem itm3 = new JMenuItem("Look Up Kids");
		itm3.addActionListener(this);
		JMenuItem addReader = new JMenuItem("Add Reader to Database");
		addReader.addActionListener(this);
		JMenuItem addCity = new JMenuItem("Add City to Database");
		addCity.addActionListener(this);
		JMenuItem addSchool = new JMenuItem("Add School/Daycare to Database");
		addSchool.addActionListener(this);
		JMenu menu3 = new JMenu("Statistics");
		JMenuItem itm4 = new JMenuItem("Current Program Stats");
		itm4.addActionListener(this);
		JMenuItem itm5 = new JMenuItem("Quick Stats");
		itm5.addActionListener(this);
		JMenuItem itm6 = new JMenuItem("Report Runner");
		itm6.addActionListener(this);
		JMenuItem itm7 = new JMenuItem("Report Creator");
		itm7.addActionListener(this);
		JMenu menu4 = new JMenu("About");
		JMenuItem item8 = new JMenuItem("About");
		item8.addActionListener(this);
		menu4.add(item8);
		menu1.add(itm1);
		menu1.add(setDBalt);
		menu1.add(itm2);
		menu2.add(itm3);
		menu2.add(addReader);
		menu2.add(addCity);
		menu2.add(addSchool);
		menu3.add(itm4);
		menu3.add(itm5);
		menu3.add(itm6);
		menu3.add(itm7);
		mBar = new JMenuBar();
		mBar.add(menu1);
		mBar.add(menu2);
		mBar.add(menu3);
		mBar.add(menu4);
	}

	/**
	 * Handles the menu selections.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Set Database Path"))
		{
			JFileChooser fileChoose = new JFileChooser();
			int choice = fileChoose.showOpenDialog(this);
			if(choice == JFileChooser.APPROVE_OPTION)
			{
				dbPath = fileChoose.getSelectedFile().getPath();
				try 
				{
					FileWriter write = new FileWriter(new File("databaseLocation.loc"));
					write.write(dbPath);
					write.close();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
			setDatabase();
		}
		if(e.getActionCommand().equals("Set Database Path FileShare"))
		{
			File f = new File("\\\\10.17.101.88\\fileshare\\Youth Services");
			FileSystemView fsv = FileSystemView.getFileSystemView();
			f = fsv.getParentDirectory(f);
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(f);
			int choice = fileChooser.showOpenDialog(this);
			if(choice == JFileChooser.APPROVE_OPTION)
			{
				dbPath = fileChooser.getSelectedFile().getPath();
				try 
				{
					FileWriter write = new FileWriter(new File("databaseLocation.loc"));
					write.write(dbPath);
					write.close();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
			setDatabase();
		}
		if(e.getActionCommand().equals("Exit"))
		{
			this.dispose();
		}
		if(databaseOpen)
		{
			if(e.getActionCommand().equals("Look Up Kids"))
			{
				kFind.makeVisible();
			}
			if(e.getActionCommand().equals("Add City to Database"))
			{
				cityAdd.makeVisible();
			}
			if(e.getActionCommand().equals("Add School/Daycare to Database"))
			{
				schoolAdd.makeVisible();
			}
			if(e.getActionCommand().equals("Current Program Stats"))
			{
				readStat.makeVisible();
			}
			if(e.getActionCommand().equals("Quick Stats"))
			{
				quickStat.setVisible(true);
			}
			if(e.getActionCommand().equals("Report Runner"))
			{
				getReportList();
				JLabel word = new JLabel("Select Report to Run:");
				JPanel inPanel = new JPanel();
				inPanel.setLayout(new BorderLayout());
				inPanel.add(word, BorderLayout.NORTH);
				inPanel.add(reportSelectBox, BorderLayout.CENTER);
				JOptionPane.showMessageDialog(null, inPanel, "Report Runner", JOptionPane.QUESTION_MESSAGE);
				ReportHandler.runAndWrite((String) reportSelectBox.getSelectedItem(), dbFriend);
				JOptionPane.showMessageDialog(null, "Report File Created", "Report Run", JOptionPane.INFORMATION_MESSAGE);
			}
			if(e.getActionCommand().equals("Report Creator"))
			{
				repBuildWin.setVisible(true);
			}
			if(e.getActionCommand().equals("Merge Schools"))
			{
				mergeSchoolWin.makeVisible();

			}
			if(e.getActionCommand().equals("Merge Cities"))
			{
				mergeCityWin.makeVisible();
			}
			if(e.getActionCommand().equals("Merge Readers"))
			{
				if(mergeReaders.isSelected())
				{
					Constants.mergeMode = true;
					Constants.splitMode = false;
					Constants.adminEdit = false;
					editProgramData.setSelected(false);
					splitReader.setSelected(false);
				}
				if(!mergeReaders.isSelected())
				{
					Constants.mergeMode = false;
				}
				kFind.refreshKidViewer();
			}
			if(e.getActionCommand().equals("Split Reader"))
			{
				if(splitReader.isSelected())
				{
					Constants.mergeMode = false;
					Constants.splitMode = true;
					Constants.adminEdit = false;
					editProgramData.setSelected(false);
					mergeReaders.setSelected(false);
				}
				if(!splitReader.isSelected())
				{
					Constants.splitMode = false;
				}
				kFind.refreshKidViewer();
			}
			if(e.getActionCommand().equals("Edit Program Data"))
			{
				if(editProgramData.isSelected())
				{
					Constants.mergeMode = false;
					Constants.splitMode = false;
					Constants.adminEdit = true;
					mergeReaders.setSelected(false);
					splitReader.setSelected(false);
				}
				if(!editProgramData.isSelected())
				{
					Constants.adminEdit = false;
				}
				kFind.refreshKidViewer();
			}
			if(e.getActionCommand().equals("Add Reader to Database"))
			{
				readAdd.setVisible(true);
			}
			if(e.getActionCommand().equals("About"))
			{
				JOptionPane.showMessageDialog(null,"The FORCE (Filled Out Reading Cards Entered) is a database front end\n" + 
						"for a SQLite database for tracking and doing statistics on participants in the summer\n" + 
						"reading program that is run by the Sun Prairie Public Library.\n" + 
						"Souce code can be found at: https://github.com/schmittaj/TheFORCE\n" + 
						"Copyright (C) 2019  Anthony Schmitt (schmittaj@gmail.com)\n" + 
						"\n" + 
						"This program is free software: you can redistribute it and/or modify\n" + 
						"it under the terms of the GNU General Public License as published by\n" + 
						"the Free Software Foundation, either version 3 of the License, or\n" + 
						"(at your option) any later version.\n" + 
						"\n" + 
						"This program is distributed in the hope that it will be useful,\n" + 
						"but WITHOUT ANY WARRANTY; without even the implied warranty of\n" + 
						"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" + 
						"GNU General Public License for more details.\n" + 
						"\n" + 
						"You should have received a copy of the GNU General Public License\n" + 
						"along with this program.  If not, see <https://www.gnu.org/licenses/>.\n","About",JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "No database specified. Please select a database file.", "Missing Database", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * Checks to see if the database has been backed up yet on the current day
	 * and creates a backup if not.
	 */
	private void doBackup()
	{
		String dbfile = dbPath.substring(dbPath.lastIndexOf('\\')+1);
		if(!dbfile.toLowerCase().contains("test"))
		{
			Date d = new Date(System.currentTimeMillis());
			int year = d.getYear()+1900;
			int day = d.getDate();
			int month = d.getMonth()+1;
			String curDate = month + "-" + day + "-" + year;
			String bakFileName = curDate+"_"+dbfile+".bak";
			File backupFolder = new File("backup");
			File[] fileList = backupFolder.listFiles();
			boolean found = false;
			int counter = 0;
			while(!found && counter < fileList.length)
			{
				if(fileList[counter].getName().equals(bakFileName))
				{
					found = true;
				}
				counter++;
			}
			if(!found)
			{
				try 
				{
					Files.copy(Paths.get(dbPath), Paths.get("backup\\"+bakFileName));
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Generates list of reports based on files in the Report Folder
	 */
	private void getReportList()
	{
		reportSelection = ReportHandler.getReportList();
		reportSelectBox = new JComboBox<String>(reportSelection);
	}
	
	/**
	 * Properly disposed of all of its held objects
	 */
	public void dispose()
	{
		closeDatabaseConnection();
		super.dispose();
		System.exit(0);
	}
	
	/**
	 * Toggles off the secret menu, can be called by other objects
	 */
	public void secretMenuKeyboardPress()
	{
		if(secretMenuVisible)
		{
			secretMenuVisible = false;
			mBar.remove(secretMenu);
			this.setJMenuBar(mBar);
			Constants.mergeMode = false;
			Constants.splitMode = false;
			Constants.adminEdit = false;
			editProgramData.setSelected(false);
			mergeReaders.setSelected(false);
			splitReader.setSelected(false);
			kFind.refreshKidViewer();
		}
		else
		{
			secretMenuVisible = true;
			mBar.add(secretMenu);
			this.setJMenuBar(mBar);
		}
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
			secretMenuKeyboardPress();
		}
	}


}
