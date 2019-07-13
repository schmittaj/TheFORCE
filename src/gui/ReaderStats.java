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
import javax.swing.border.*;
import backend.*;

/**
 * This class shows the Registered, Checked In, and Finished readers. It also shows the number of
 * readers completed though each box for the current program.
 * @author Anthony Schmitt
 *
 */
public class ReaderStats extends JFrame implements ActionListener, KeyListener
{
	private DBHandler dbFriend;
	private SecretMenuToggle mainWindow;
	private JLabel regRep, readRep, finishRep, regLabel, readLabel, finishLabel;
	private JPanel superPanel;
	private JComboBox<String> pickBox;
	
	/**
	 * Constructor
	 * @param dbh Database connection
	 * @param mw Main window
	 */
	public ReaderStats(DBHandler dbh, SecretMenuToggle mw)
	{
		super();
		this.dbFriend = dbh;
		this.mainWindow = mw;
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		int height = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()/3));
		int width = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3)*2);
		this.setSize(width, height);
		this.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth()/6)), (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()/3)));
		
		databaseCheckEntry();

		init();
	}
	
	/**
	 * Initializes all the visuals and sets up all the panels.
	 */
	private void init()
	{
		//TODO add comboBox to select program
		regLabel = new JLabel("Registrants:");
		readLabel = new JLabel("Readers:");
		finishLabel = new JLabel("Finishers:");
		regLabel.setBorder(new EmptyBorder(0,20,0,0));
		readLabel.setBorder(new EmptyBorder(0,20,0,0));
		finishLabel.setBorder(new EmptyBorder(0,20,0,0));
		Date d = new Date(System.currentTimeMillis());
		int year = d.getYear()+1900;
		this.setTitle(year + " Reader Stats");
		String[] val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + ";", 1);
		regRep = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal >= 1;", 1);
		readRep = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal >= 10;", 1);
		finishRep = new JLabel(val[0]);
		
		JPanel overallPanel = new JPanel();
		overallPanel.setLayout(new GridLayout(3,2));
		overallPanel.add(regLabel);
		overallPanel.add(regRep);
		overallPanel.add(readLabel);
		overallPanel.add(readRep);
		overallPanel.add(finishLabel);
		overallPanel.add(finishRep);
		//JButton ok = new JButton("OK");
		//ok.addActionListener(this);
		String[] pickList = {"All","Children","Infant","Teen"};
		pickBox = new JComboBox<String>(pickList);
		pickBox.addActionListener(this);
		this.setLayout(new BorderLayout());
		this.add(pickBox, BorderLayout.SOUTH);
		
		//TODO  change from last year
		JLabel box0Label = new JLabel(" 0: ");
		JLabel box1Label = new JLabel(" 1: ");
		JLabel box2Label = new JLabel(" 2: ");
		JLabel box3Label = new JLabel(" 3: ");
		JLabel box4Label = new JLabel(" 4: ");
		JLabel box5Label = new JLabel(" 5: ");
		JLabel box6Label = new JLabel(" 6: ");
		JLabel box7Label = new JLabel(" 7: ");
		JLabel box8Label = new JLabel(" 8: ");
		JLabel box9Label = new JLabel(" 9: ");
		JLabel box10Label = new JLabel(" 10: ");
	
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 0;", 1);
		JLabel box0 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 1;");
		JLabel box1 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 2;");
		JLabel box2 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 3;");
		JLabel box3 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 4;");
		JLabel box4 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 5;");
		JLabel box5 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 6;");
		JLabel box6 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 7;");
		JLabel box7 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 8;");
		JLabel box8 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 9;");
		JLabel box9 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 10;");
		JLabel box10 = new JLabel(val[0]);
		JPanel boxStatusPanel = new JPanel();
		boxStatusPanel.setLayout(new GridLayout(11,2));
		boxStatusPanel.add(box0Label);
		boxStatusPanel.add(box0);
		boxStatusPanel.add(box1Label);
		boxStatusPanel.add(box1);
		boxStatusPanel.add(box2Label);
		boxStatusPanel.add(box2);
		boxStatusPanel.add(box3Label);
		boxStatusPanel.add(box3);
		boxStatusPanel.add(box4Label);
		boxStatusPanel.add(box4);
		boxStatusPanel.add(box5Label);
		boxStatusPanel.add(box5);
		boxStatusPanel.add(box6Label);
		boxStatusPanel.add(box6);
		boxStatusPanel.add(box7Label);
		boxStatusPanel.add(box7);
		boxStatusPanel.add(box8Label);
		boxStatusPanel.add(box8);
		boxStatusPanel.add(box9Label);
		boxStatusPanel.add(box9);
		boxStatusPanel.add(box10Label);
		boxStatusPanel.add(box10);
		JPanel overallStatusPanel = new JPanel();
		overallStatusPanel.setLayout(new BorderLayout());
		JLabel overallLabel = new JLabel("YTD Info:", JLabel.CENTER);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,2));
		mainPanel.add(overallPanel);
		mainPanel.add(boxStatusPanel);
		overallStatusPanel.add(mainPanel, BorderLayout.CENTER);
		overallStatusPanel.add(overallLabel, BorderLayout.NORTH);
		
		String[] pastVals = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
		if(Constants.regularDB)
		{
			pastVals = getDiffFromLastWeek(-1);
		}
		JLabel diffregLabel = new JLabel("Registrants:");
		JLabel diffreadLabel = new JLabel("Readers:");
		JLabel difffinishLabel = new JLabel("Finishers:");
		diffregLabel.setBorder(new EmptyBorder(0,20,0,0));
		diffreadLabel.setBorder(new EmptyBorder(0,20,0,0));
		difffinishLabel.setBorder(new EmptyBorder(0,20,0,0));
		JLabel diffregRep = new JLabel(pastVals[0]);
		JLabel diffreadRep = new JLabel(pastVals[1]);
		JLabel difffinishRep = new JLabel(pastVals[2]);
		
		JPanel diffoverallPanel = new JPanel();
		diffoverallPanel.setLayout(new GridLayout(3,2));
		diffoverallPanel.add(diffregLabel);
		diffoverallPanel.add(diffregRep);
		diffoverallPanel.add(diffreadLabel);
		diffoverallPanel.add(diffreadRep);
		diffoverallPanel.add(difffinishLabel);
		diffoverallPanel.add(difffinishRep);
		
		JLabel diffbox0Label = new JLabel(" 0: ");
		JLabel diffbox1Label = new JLabel(" 1: ");
		JLabel diffbox2Label = new JLabel(" 2: ");
		JLabel diffbox3Label = new JLabel(" 3: ");
		JLabel diffbox4Label = new JLabel(" 4: ");
		JLabel diffbox5Label = new JLabel(" 5: ");
		JLabel diffbox6Label = new JLabel(" 6: ");
		JLabel diffbox7Label = new JLabel(" 7: ");
		JLabel diffbox8Label = new JLabel(" 8: ");
		JLabel diffbox9Label = new JLabel(" 9: ");
		JLabel diffbox10Label = new JLabel(" 10: ");
		JLabel diffbox0 = new JLabel(pastVals[3]);
		JLabel diffbox1 = new JLabel(pastVals[4]);
		JLabel diffbox2 = new JLabel(pastVals[5]);
		JLabel diffbox3 = new JLabel(pastVals[6]);
		JLabel diffbox4 = new JLabel(pastVals[7]);
		JLabel diffbox5 = new JLabel(pastVals[8]);
		JLabel diffbox6 = new JLabel(pastVals[9]);
		JLabel diffbox7 = new JLabel(pastVals[10]);
		JLabel diffbox8 = new JLabel(pastVals[11]);
		JLabel diffbox9 = new JLabel(pastVals[12]);
		JLabel diffbox10 = new JLabel(pastVals[13]);
		JPanel diffboxStatusPanel = new JPanel();
		diffboxStatusPanel.setLayout(new GridLayout(11,2));
		diffboxStatusPanel.add(diffbox0Label);
		diffboxStatusPanel.add(diffbox0);
		diffboxStatusPanel.add(diffbox1Label);
		diffboxStatusPanel.add(diffbox1);
		diffboxStatusPanel.add(diffbox2Label);
		diffboxStatusPanel.add(diffbox2);
		diffboxStatusPanel.add(diffbox3Label);
		diffboxStatusPanel.add(diffbox3);
		diffboxStatusPanel.add(diffbox4Label);
		diffboxStatusPanel.add(diffbox4);
		diffboxStatusPanel.add(diffbox5Label);
		diffboxStatusPanel.add(diffbox5);
		diffboxStatusPanel.add(diffbox6Label);
		diffboxStatusPanel.add(diffbox6);
		diffboxStatusPanel.add(diffbox7Label);
		diffboxStatusPanel.add(diffbox7);
		diffboxStatusPanel.add(diffbox8Label);
		diffboxStatusPanel.add(diffbox8);
		diffboxStatusPanel.add(diffbox9Label);
		diffboxStatusPanel.add(diffbox9);
		diffboxStatusPanel.add(diffbox10Label);
		diffboxStatusPanel.add(diffbox10);
	
		JPanel diffoverallStatusPanel = new JPanel();
		diffoverallStatusPanel.setLayout(new BorderLayout());
		JLabel diffoverallLabel = new JLabel("Change from Last Week:", JLabel.CENTER);
		JPanel diffmainPanel = new JPanel();
		diffmainPanel.setLayout(new GridLayout(1,2));
		diffmainPanel.add(diffoverallPanel);
		diffmainPanel.add(diffboxStatusPanel);
		diffoverallStatusPanel.add(diffmainPanel, BorderLayout.CENTER);
		diffoverallStatusPanel.add(diffoverallLabel, BorderLayout.NORTH);
		
		superPanel = new JPanel();
		superPanel.setLayout(new GridLayout(1,2));
		superPanel.add(overallStatusPanel);
		superPanel.add(diffoverallStatusPanel);
		
		this.add(superPanel, BorderLayout.CENTER);
		
	}

	/** 
	 * Checks to see if there is a weekly stats entry in the database, and if not adds one.
	 */
	private void databaseCheckEntry()
	{
		if(Constants.regularDB)
		{
			Date d = new Date(System.currentTimeMillis());
			int thisMonth = d.getMonth() + 1;
			
			if(thisMonth >= Constants.START_MONTH && thisMonth <= Constants.END_MONTH)
			{
				String[][] weekStats = dbFriend.query2DstringRet(Queries.ALL_WEEKLY_STATS, Queries.ALL_WEEKLY_STATS_COL_LEN);
				boolean haveEntry = false;
				if(weekStats != null)
				{
					for(int a = 0; a < weekStats.length; a++)
					{
						if(currentWeek(weekStats[a][0]))
						{
							haveEntry = true;
						}
					}
				}
				if(!haveEntry)
				{
		
					int thisDay = d.getDate();
					int thisYear = d.getYear() + 1900;
					String todaysDate = "" + thisMonth + "/" + thisDay + "/" + thisYear;
					
					String query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + ";"; // registered
					String reg = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal > 0;"; // readers
					String read = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 10;"; // finishers
					String finish = dbFriend.query1DstringRet(query)[0]; 
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 1;"; // 
					String box1 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 2;"; // 
					String box2 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 3;"; // 
					String box3 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 4;"; // 
					String box4 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 5;"; // 
					String box5 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 6;"; // 
					String box6 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 7;"; // 
					String box7 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 8;"; // 
					String box8 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 9;"; //
					String box9 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_one = true;"; //
					String ec1 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_two = true;"; //
					String ec2 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_three = true;"; //
					String ec3 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_four = true;"; //
					String ec4 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_five = true;"; //
					String ec5 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_six = true;"; //
					String ec6 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_seven = true;"; //
					String ec7 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_eight = true;"; //
					String ec8 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_nine = true;"; //
					String ec9 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_ten = true;"; //
					String ec10 = dbFriend.query1DstringRet(query)[0];
					String program = "-1";
					String toRun = "INSERT INTO Weekly_Stats VALUES (\""+todaysDate+"\","+reg+","+read+","+finish+","+box1+","+box2+","+box3+","+box4+","+box5+","
							+box6+","+box7+","+box8+","+box9+","+finish+","+ec1+","+ec2+","+ec3+","+ec4+","+ec5+","+ec6+","+ec7+","+ec8+","+ec9+","+ec10+","+program+");";
					dbFriend.executeUpdate(toRun);
					
					//infant
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + ";"; // registered
					reg = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal > 0;"; // readers
					read = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 10;"; // finishers
					finish = dbFriend.query1DstringRet(query)[0]; 
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 1;"; // 
					box1 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 2;"; // 
					box2 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 3;"; // 
					box3 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 4;"; // 
					box4 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 5;"; // 
					box5 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 6;"; // 
					box6 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 7;"; // 
					box7 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 8;"; // 
					box8 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 9;"; //
					box9 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_one = true;"; //
					ec1 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_two = true;"; //
					ec2 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_three = true;"; //
					ec3 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_four = true;"; //
					ec4 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_five = true;"; //
					ec5 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_six = true;"; //
					ec6 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_seven = true;"; //
					ec7 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_eight = true;"; //
					ec8 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_nine = true;"; //
					ec9 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_ten = true;"; //
					ec10 = dbFriend.query1DstringRet(query)[0];
					program = "0";
					toRun = "INSERT INTO Weekly_Stats VALUES (\""+todaysDate+"\","+reg+","+read+","+finish+","+box1+","+box2+","+box3+","+box4+","+box5+","
							+box6+","+box7+","+box8+","+box9+","+finish+","+ec1+","+ec2+","+ec3+","+ec4+","+ec5+","+ec6+","+ec7+","+ec8+","+ec9+","+ec10+","+program+");";
					dbFriend.executeUpdate(toRun);
					
					//children
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + ";"; // registered
					reg = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal > 0;"; // readers
					read = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 10;"; // finishers
					finish = dbFriend.query1DstringRet(query)[0]; 
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 1;"; // 
					box1 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 2;"; // 
					box2 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 3;"; // 
					box3 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 4;"; // 
					box4 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 5;"; // 
					box5 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 6;"; // 
					box6 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 7;"; // 
					box7 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 8;"; // 
					box8 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 9;"; //
					box9 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_one = true;"; //
					ec1 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_two = true;"; //
					ec2 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_three = true;"; //
					ec3 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_four = true;"; //
					ec4 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_five = true;"; //
					ec5 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_six = true;"; //
					ec6 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_seven = true;"; //
					ec7 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_eight = true;"; //
					ec8 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_nine = true;"; //
					ec9 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_ten = true;"; //
					ec10 = dbFriend.query1DstringRet(query)[0];
					program = "1";
					toRun = "INSERT INTO Weekly_Stats VALUES (\""+todaysDate+"\","+reg+","+read+","+finish+","+box1+","+box2+","+box3+","+box4+","+box5+","
							+box6+","+box7+","+box8+","+box9+","+finish+","+ec1+","+ec2+","+ec3+","+ec4+","+ec5+","+ec6+","+ec7+","+ec8+","+ec9+","+ec10+","+program+");";
					dbFriend.executeUpdate(toRun);
					
					//teen
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + ";"; // registered
					reg = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal > 0;"; // readers
					read = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 10;"; // finishers
					finish = dbFriend.query1DstringRet(query)[0]; 
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 1;"; // 
					box1 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 2;"; // 
					box2 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 3;"; // 
					box3 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 4;"; // 
					box4 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 5;"; // 
					box5 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 6;"; // 
					box6 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 7;"; // 
					box7 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 8;"; // 
					box8 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal = 9;"; //
					box9 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_one = true;"; //
					ec1 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_two = true;"; //
					ec2 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_three = true;"; //
					ec3 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_four = true;"; //
					ec4 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_five = true;"; //
					ec5 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_six = true;"; //
					ec6 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_seven = true;"; //
					ec7 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_eight = true;"; //
					ec8 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_nine = true;"; //
					ec9 = dbFriend.query1DstringRet(query)[0];
					query = "SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND extra_ten = true;"; //
					ec10 = dbFriend.query1DstringRet(query)[0];
					program = "2";
					toRun = "INSERT INTO Weekly_Stats VALUES (\""+todaysDate+"\","+reg+","+read+","+finish+","+box1+","+box2+","+box3+","+box4+","+box5+","
							+box6+","+box7+","+box8+","+box9+","+finish+","+ec1+","+ec2+","+ec3+","+ec4+","+ec5+","+ec6+","+ec7+","+ec8+","+ec9+","+ec10+","+program+");";
					dbFriend.executeUpdate(toRun);
				}
			}
		}
	}
	
	/**
	 * Checks to see if given date is in the current Week
	 * @param dateToCheck date in form M(M)/D(D)/YYYY
	 * @return True if date is in current week.
	 */
	private boolean currentWeek(String dateToCheck)
	{
		boolean output = false;
		Date d = new Date(System.currentTimeMillis());
		int chk = d.getDay();
		int chkd = d.getDate();
	
		
		String[] tmp = dateToCheck.split("/");
		int thisMonth = d.getMonth() + 1;
		int thisYear = d.getYear() + 1900;
		int weekStart = chkd - chk;
		int weekEnd = weekStart+6;
		int dayCheck = Integer.parseInt(tmp[1]);
		int yearCheck = Integer.parseInt(tmp[2]);
		int monthCheck = Integer.parseInt(tmp[0]);
		
		if(thisYear == yearCheck && thisMonth == monthCheck && dayCheck >= weekStart && dayCheck <= weekEnd)
		{
			output = true;
		}
		
		return output;
	}
	
	/**
	 * Gets the difference in values from last compared to the current week
	 * @return Array of differences
	 */
	private String[] getDiffFromLastWeek(int program)
	{
		String[] output = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
		String[][] weekStats = null;
		if(program == -1)
		{
			weekStats = dbFriend.query2DstringRet(Queries.ALL_WEEKLY_STATS, Queries.ALL_WEEKLY_STATS_COL_LEN);
		}
		else if(program == 0)
		{
			weekStats = dbFriend.query2DstringRet(Queries.INFANT_WEEKLY_STATS, Queries.INFANT_WEEKLY_STATS_COL_LEN);
		}
		else if(program == 1)
		{
			weekStats = dbFriend.query2DstringRet(Queries.CHILDREN_WEEKLY_STATS, Queries.CHILDREN_WEEKLY_STATS_COL_LEN);
		}
		else if(program == 2)
		{
			weekStats = dbFriend.query2DstringRet(Queries.TEEN_WEEKLY_STATS, Queries.TEEN_WEEKLY_STATS_COL_LEN);
		}
		
		Date d = new Date(System.currentTimeMillis());
		int thisYear = d.getYear() + 1900;
		if(weekStats != null)
		{	
			int thisWeekIndex = weekStats.length-1;
			int lastWeekIndex = weekStats.length-2;
			
			if(weekStats[thisWeekIndex][0].contains(""+thisYear))
			{
				if(weekStats.length > 1 && weekStats[lastWeekIndex][0].contains(""+thisYear))
				{
					String val0 = "0";
					String val1 = "0";
					String val2 = "0";
					String val3 = "0";
					String val4 = "0";
					String val5 = "0";
					String val6 = "0";
					String val7 = "0";
					String val8 = "0";
					String val9 = "0";
					String val10 = "0";
					String valRead = "0";
					String valReg = "0";
				
					if(program == -1)
					{
						val0 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND highest_level_normal = 0;")[0];
						val1 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND highest_level_normal = 1;")[0];
						val2 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND highest_level_normal = 2;")[0];
						val3 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND highest_level_normal = 3;")[0];
						val4 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND highest_level_normal = 4;")[0];
						val5 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND highest_level_normal = 5;")[0];
						val6 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND highest_level_normal = 6;")[0];
						val7 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND highest_level_normal = 7;")[0];
						val8 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND highest_level_normal = 8;")[0];
						val9 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND highest_level_normal = 9;")[0];
						val10 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND highest_level_normal = 10;")[0];
						valRead = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND highest_level_normal > 0;")[0];
						valReg = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + ";")[0];
					}
					else
					{
						val0 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND program_id = " + program + " AND highest_level_normal = 0;")[0];
						val1 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND program_id = " + program + " AND highest_level_normal = 1;")[0];
						val2 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND program_id = " + program + " AND highest_level_normal = 2;")[0];
						val3 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND program_id = " + program + " AND highest_level_normal = 3;")[0];
						val4 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND program_id = " + program + " AND highest_level_normal = 4;")[0];
						val5 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND program_id = " + program + " AND highest_level_normal = 5;")[0];
						val6 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND program_id = " + program + " AND highest_level_normal = 6;")[0];
						val7 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND program_id = " + program + " AND highest_level_normal = 7;")[0];
						val8 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND program_id = " + program + " AND highest_level_normal = 8;")[0];
						val9 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND program_id = " + program + " AND highest_level_normal = 9;")[0];
						val10 = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND program_id = " + program + " AND highest_level_normal = 10;")[0];
						valRead = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = "+ thisYear + " AND program_id = " + program + " AND highest_level_normal > 0;")[0];
						valReg = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + thisYear + " AND program_id = " + program + ";")[0];
					}
					output[0] = ""+ (Integer.parseInt(valReg)-Integer.parseInt(weekStats[lastWeekIndex][1]));
					output[1] = ""+ (Integer.parseInt(valRead)-Integer.parseInt(weekStats[lastWeekIndex][2]));
					output[2] = ""+ (Integer.parseInt(val10)-Integer.parseInt(weekStats[lastWeekIndex][3]));
					output[3] = ""+ (Integer.parseInt(val0)-Integer.parseInt(weekStats[lastWeekIndex][1]));
					output[4] = ""+ (Integer.parseInt(val1)-Integer.parseInt(weekStats[lastWeekIndex][4]));
					output[5] = ""+ (Integer.parseInt(val2)-Integer.parseInt(weekStats[lastWeekIndex][5]));
					output[6] = ""+ (Integer.parseInt(val3)-Integer.parseInt(weekStats[lastWeekIndex][6]));
					output[7] = ""+ (Integer.parseInt(val4)-Integer.parseInt(weekStats[lastWeekIndex][7]));
					output[8] = ""+ (Integer.parseInt(val5)-Integer.parseInt(weekStats[lastWeekIndex][8]));
					output[9] = ""+ (Integer.parseInt(val6)-Integer.parseInt(weekStats[lastWeekIndex][9]));
					output[10] = ""+ (Integer.parseInt(val7)-Integer.parseInt(weekStats[lastWeekIndex][10]));
					output[11] = ""+ (Integer.parseInt(val8)-Integer.parseInt(weekStats[lastWeekIndex][11]));
					output[12] = ""+ (Integer.parseInt(val9)-Integer.parseInt(weekStats[lastWeekIndex][12]));
					output[13] = ""+ (Integer.parseInt(val10)-Integer.parseInt(weekStats[lastWeekIndex][13]));
				}
				/*else
				{
					for(int a = 0; a < weekStats[0].length; a++)
					{
						System.out.print(weekStats[thisWeekIndex][a] + ", ");
					}
					System.out.println();
					
					output[0] = weekStats[thisWeekIndex][1];
					output[1] = weekStats[thisWeekIndex][2];
					output[2] = weekStats[thisWeekIndex][3];
					output[3] = weekStats[thisWeekIndex][1];
					output[4] = weekStats[thisWeekIndex][4];
					output[5] = weekStats[thisWeekIndex][5];
					output[6] = weekStats[thisWeekIndex][6];
					output[7] = weekStats[thisWeekIndex][7];
					output[8] = weekStats[thisWeekIndex][8];
					output[9] = weekStats[thisWeekIndex][9];
					output[10] = weekStats[thisWeekIndex][10];
					output[11] = weekStats[thisWeekIndex][11];
					output[12] = weekStats[thisWeekIndex][12];
					output[13] = weekStats[thisWeekIndex][13];
				}*/
			}
		}
		return output;
	}
	
	/**
	 * Key typed events
	 */
	public void keyTyped(KeyEvent e) 
	{
	
	}

	/**
	 * Key Pressed events
	 */
	public void keyPressed(KeyEvent e) 
	{
		
	}

	/**
	 * Key Released events
	 */
	public void keyReleased(KeyEvent e) 
	{
		if((e.getKeyCode() == KeyEvent.VK_L) &(e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK))
		{
			mainWindow.secretMenuKeyboardPress();
		}
	}

	/**
	 * Passes in a new database to connect to.
	 * @param dbh The database to use.
	 */
	public void updateDatabaseConnection(DBHandler dbh)
	{
		this.dbFriend = dbh;
		update();
	}
	
	/**
	 * Makes window visible
	 */
	public void makeVisible()
	{
		update();
		setVisible(true);
	}
	
	/**
	 * Updates the information in the window.
	 */
	private void update()
	{
		displayForAllPrograms();
		this.paintAll(this.getGraphics());
	}
	
	/**
	 * Handles actions
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("comboBoxChanged"))
		{
			int pick = pickBox.getSelectedIndex();
			if(pick == 0)
			{
				displayForAllPrograms();
			}
			else if(pick == 1)
			{
				displayForChildrenProgram();
			}
			else if(pick == 2)
			{
				displayForInfantProgram();
			}
			else if(pick == 3)
			{
				displayForTeenProgram();
			}
		}
	}
	
	private void displayForAllPrograms()
	{
		Date d = new Date(System.currentTimeMillis());
		int year = d.getYear()+1900;
		
		String[] val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + ";", 1);
		regRep = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal >= 1;", 1);
		readRep = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal >= 10;", 1);
		finishRep = new JLabel(val[0]);
		
		JPanel overallPanel = new JPanel();
		overallPanel.setLayout(new GridLayout(3,2));
		overallPanel.add(regLabel);
		overallPanel.add(regRep);
		overallPanel.add(readLabel);
		overallPanel.add(readRep);
		overallPanel.add(finishLabel);
		overallPanel.add(finishRep);
		
		//TODO  change from last year
		JLabel box0Label = new JLabel(" 0: ");
		JLabel box1Label = new JLabel(" 1: ");
		JLabel box2Label = new JLabel(" 2: ");
		JLabel box3Label = new JLabel(" 3: ");
		JLabel box4Label = new JLabel(" 4: ");
		JLabel box5Label = new JLabel(" 5: ");
		JLabel box6Label = new JLabel(" 6: ");
		JLabel box7Label = new JLabel(" 7: ");
		JLabel box8Label = new JLabel(" 8: ");
		JLabel box9Label = new JLabel(" 9: ");
		JLabel box10Label = new JLabel(" 10: ");
	
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 0;", 1);
		JLabel box0 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 1;");
		JLabel box1 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 2;");
		JLabel box2 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 3;");
		JLabel box3 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 4;");
		JLabel box4 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 5;");
		JLabel box5 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 6;");
		JLabel box6 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 7;");
		JLabel box7 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 8;");
		JLabel box8 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 9;");
		JLabel box9 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND highest_level_normal = 10;");
		JLabel box10 = new JLabel(val[0]);
		JPanel boxStatusPanel = new JPanel();
		boxStatusPanel.setLayout(new GridLayout(11,2));
		boxStatusPanel.add(box0Label);
		boxStatusPanel.add(box0);
		boxStatusPanel.add(box1Label);
		boxStatusPanel.add(box1);
		boxStatusPanel.add(box2Label);
		boxStatusPanel.add(box2);
		boxStatusPanel.add(box3Label);
		boxStatusPanel.add(box3);
		boxStatusPanel.add(box4Label);
		boxStatusPanel.add(box4);
		boxStatusPanel.add(box5Label);
		boxStatusPanel.add(box5);
		boxStatusPanel.add(box6Label);
		boxStatusPanel.add(box6);
		boxStatusPanel.add(box7Label);
		boxStatusPanel.add(box7);
		boxStatusPanel.add(box8Label);
		boxStatusPanel.add(box8);
		boxStatusPanel.add(box9Label);
		boxStatusPanel.add(box9);
		boxStatusPanel.add(box10Label);
		boxStatusPanel.add(box10);
		JPanel overallStatusPanel = new JPanel();
		overallStatusPanel.setLayout(new BorderLayout());
		JLabel overallLabel = new JLabel("YTD Info:", JLabel.CENTER);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,2));
		mainPanel.add(overallPanel);
		mainPanel.add(boxStatusPanel);
		overallStatusPanel.add(mainPanel, BorderLayout.CENTER);
		overallStatusPanel.add(overallLabel, BorderLayout.NORTH);
		
		String[] pastVals = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
		if(Constants.regularDB)
		{
			pastVals = getDiffFromLastWeek(-1);
		}
		
		JLabel diffregLabel = new JLabel("Registrants:");
		JLabel diffreadLabel = new JLabel("Readers:");
		JLabel difffinishLabel = new JLabel("Finishers:");
		diffregLabel.setBorder(new EmptyBorder(0,20,0,0));
		diffreadLabel.setBorder(new EmptyBorder(0,20,0,0));
		difffinishLabel.setBorder(new EmptyBorder(0,20,0,0));
		JLabel diffregRep = new JLabel(pastVals[0]);
		JLabel diffreadRep = new JLabel(pastVals[1]);
		JLabel difffinishRep = new JLabel(pastVals[2]);
		
		JPanel diffoverallPanel = new JPanel();
		diffoverallPanel.setLayout(new GridLayout(3,2));
		diffoverallPanel.add(diffregLabel);
		diffoverallPanel.add(diffregRep);
		diffoverallPanel.add(diffreadLabel);
		diffoverallPanel.add(diffreadRep);
		diffoverallPanel.add(difffinishLabel);
		diffoverallPanel.add(difffinishRep);
		
		JLabel diffbox0Label = new JLabel(" 0: ");
		JLabel diffbox1Label = new JLabel(" 1: ");
		JLabel diffbox2Label = new JLabel(" 2: ");
		JLabel diffbox3Label = new JLabel(" 3: ");
		JLabel diffbox4Label = new JLabel(" 4: ");
		JLabel diffbox5Label = new JLabel(" 5: ");
		JLabel diffbox6Label = new JLabel(" 6: ");
		JLabel diffbox7Label = new JLabel(" 7: ");
		JLabel diffbox8Label = new JLabel(" 8: ");
		JLabel diffbox9Label = new JLabel(" 9: ");
		JLabel diffbox10Label = new JLabel(" 10: ");
		JLabel diffbox0 = new JLabel(pastVals[3]);
		JLabel diffbox1 = new JLabel(pastVals[4]);
		JLabel diffbox2 = new JLabel(pastVals[5]);
		JLabel diffbox3 = new JLabel(pastVals[6]);
		JLabel diffbox4 = new JLabel(pastVals[7]);
		JLabel diffbox5 = new JLabel(pastVals[8]);
		JLabel diffbox6 = new JLabel(pastVals[9]);
		JLabel diffbox7 = new JLabel(pastVals[10]);
		JLabel diffbox8 = new JLabel(pastVals[11]);
		JLabel diffbox9 = new JLabel(pastVals[12]);
		JLabel diffbox10 = new JLabel(pastVals[13]);
		JPanel diffboxStatusPanel = new JPanel();
		diffboxStatusPanel.setLayout(new GridLayout(11,2));
		diffboxStatusPanel.add(diffbox0Label);
		diffboxStatusPanel.add(diffbox0);
		diffboxStatusPanel.add(diffbox1Label);
		diffboxStatusPanel.add(diffbox1);
		diffboxStatusPanel.add(diffbox2Label);
		diffboxStatusPanel.add(diffbox2);
		diffboxStatusPanel.add(diffbox3Label);
		diffboxStatusPanel.add(diffbox3);
		diffboxStatusPanel.add(diffbox4Label);
		diffboxStatusPanel.add(diffbox4);
		diffboxStatusPanel.add(diffbox5Label);
		diffboxStatusPanel.add(diffbox5);
		diffboxStatusPanel.add(diffbox6Label);
		diffboxStatusPanel.add(diffbox6);
		diffboxStatusPanel.add(diffbox7Label);
		diffboxStatusPanel.add(diffbox7);
		diffboxStatusPanel.add(diffbox8Label);
		diffboxStatusPanel.add(diffbox8);
		diffboxStatusPanel.add(diffbox9Label);
		diffboxStatusPanel.add(diffbox9);
		diffboxStatusPanel.add(diffbox10Label);
		diffboxStatusPanel.add(diffbox10);
	
		JPanel diffoverallStatusPanel = new JPanel();
		diffoverallStatusPanel.setLayout(new BorderLayout());
		JLabel diffoverallLabel = new JLabel("Change from Last Week:", JLabel.CENTER);
		JPanel diffmainPanel = new JPanel();
		diffmainPanel.setLayout(new GridLayout(1,2));
		diffmainPanel.add(diffoverallPanel);
		diffmainPanel.add(diffboxStatusPanel);
		diffoverallStatusPanel.add(diffmainPanel, BorderLayout.CENTER);
		diffoverallStatusPanel.add(diffoverallLabel, BorderLayout.NORTH);
		
		this.remove(superPanel);
		
		superPanel = new JPanel();
		superPanel.setLayout(new GridLayout(1,2));
		superPanel.add(overallStatusPanel);
		superPanel.add(diffoverallStatusPanel);
		
		
		this.add(superPanel, BorderLayout.CENTER);
		this.paintAll(this.getGraphics());
	}
	
	private void displayForChildrenProgram()
	{
		Date d = new Date(System.currentTimeMillis());
		int year = d.getYear()+1900;
		
		String[] val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1;", 1);
		regRep = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal >= 1;", 1);
		readRep = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal >= 10;", 1);
		finishRep = new JLabel(val[0]);
		
		JPanel overallPanel = new JPanel();
		overallPanel.setLayout(new GridLayout(3,2));
		overallPanel.add(regLabel);
		overallPanel.add(regRep);
		overallPanel.add(readLabel);
		overallPanel.add(readRep);
		overallPanel.add(finishLabel);
		overallPanel.add(finishRep);
		
		//TODO  change from last year
		JLabel box0Label = new JLabel(" 0: ");
		JLabel box1Label = new JLabel(" 1: ");
		JLabel box2Label = new JLabel(" 2: ");
		JLabel box3Label = new JLabel(" 3: ");
		JLabel box4Label = new JLabel(" 4: ");
		JLabel box5Label = new JLabel(" 5: ");
		JLabel box6Label = new JLabel(" 6: ");
		JLabel box7Label = new JLabel(" 7: ");
		JLabel box8Label = new JLabel(" 8: ");
		JLabel box9Label = new JLabel(" 9: ");
		JLabel box10Label = new JLabel(" 10: ");
	
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal = 0;", 1);
		JLabel box0 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal = 1;");
		JLabel box1 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal = 2;");
		JLabel box2 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal = 3;");
		JLabel box3 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal = 4;");
		JLabel box4 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal = 5;");
		JLabel box5 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal = 6;");
		JLabel box6 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal = 7;");
		JLabel box7 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal = 8;");
		JLabel box8 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal = 9;");
		JLabel box9 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 1 AND highest_level_normal = 10;");
		JLabel box10 = new JLabel(val[0]);
		JPanel boxStatusPanel = new JPanel();
		boxStatusPanel.setLayout(new GridLayout(11,2));
		boxStatusPanel.add(box0Label);
		boxStatusPanel.add(box0);
		boxStatusPanel.add(box1Label);
		boxStatusPanel.add(box1);
		boxStatusPanel.add(box2Label);
		boxStatusPanel.add(box2);
		boxStatusPanel.add(box3Label);
		boxStatusPanel.add(box3);
		boxStatusPanel.add(box4Label);
		boxStatusPanel.add(box4);
		boxStatusPanel.add(box5Label);
		boxStatusPanel.add(box5);
		boxStatusPanel.add(box6Label);
		boxStatusPanel.add(box6);
		boxStatusPanel.add(box7Label);
		boxStatusPanel.add(box7);
		boxStatusPanel.add(box8Label);
		boxStatusPanel.add(box8);
		boxStatusPanel.add(box9Label);
		boxStatusPanel.add(box9);
		boxStatusPanel.add(box10Label);
		boxStatusPanel.add(box10);
		JPanel overallStatusPanel = new JPanel();
		overallStatusPanel.setLayout(new BorderLayout());
		JLabel overallLabel = new JLabel("YTD Info:", JLabel.CENTER);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,2));
		mainPanel.add(overallPanel);
		mainPanel.add(boxStatusPanel);
		overallStatusPanel.add(mainPanel, BorderLayout.CENTER);
		overallStatusPanel.add(overallLabel, BorderLayout.NORTH);
		
		String[] pastVals = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
		if(Constants.regularDB)
		{
			pastVals = getDiffFromLastWeek(1);
		}
		JLabel diffregLabel = new JLabel("Registrants:");
		JLabel diffreadLabel = new JLabel("Readers:");
		JLabel difffinishLabel = new JLabel("Finishers:");
		diffregLabel.setBorder(new EmptyBorder(0,20,0,0));
		diffreadLabel.setBorder(new EmptyBorder(0,20,0,0));
		difffinishLabel.setBorder(new EmptyBorder(0,20,0,0));
		JLabel diffregRep = new JLabel(pastVals[0]);
		JLabel diffreadRep = new JLabel(pastVals[1]);
		JLabel difffinishRep = new JLabel(pastVals[2]);
		
		JPanel diffoverallPanel = new JPanel();
		diffoverallPanel.setLayout(new GridLayout(3,2));
		diffoverallPanel.add(diffregLabel);
		diffoverallPanel.add(diffregRep);
		diffoverallPanel.add(diffreadLabel);
		diffoverallPanel.add(diffreadRep);
		diffoverallPanel.add(difffinishLabel);
		diffoverallPanel.add(difffinishRep);
		
		JLabel diffbox0Label = new JLabel(" 0: ");
		JLabel diffbox1Label = new JLabel(" 1: ");
		JLabel diffbox2Label = new JLabel(" 2: ");
		JLabel diffbox3Label = new JLabel(" 3: ");
		JLabel diffbox4Label = new JLabel(" 4: ");
		JLabel diffbox5Label = new JLabel(" 5: ");
		JLabel diffbox6Label = new JLabel(" 6: ");
		JLabel diffbox7Label = new JLabel(" 7: ");
		JLabel diffbox8Label = new JLabel(" 8: ");
		JLabel diffbox9Label = new JLabel(" 9: ");
		JLabel diffbox10Label = new JLabel(" 10: ");
		JLabel diffbox0 = new JLabel(pastVals[3]);
		JLabel diffbox1 = new JLabel(pastVals[4]);
		JLabel diffbox2 = new JLabel(pastVals[5]);
		JLabel diffbox3 = new JLabel(pastVals[6]);
		JLabel diffbox4 = new JLabel(pastVals[7]);
		JLabel diffbox5 = new JLabel(pastVals[8]);
		JLabel diffbox6 = new JLabel(pastVals[9]);
		JLabel diffbox7 = new JLabel(pastVals[10]);
		JLabel diffbox8 = new JLabel(pastVals[11]);
		JLabel diffbox9 = new JLabel(pastVals[12]);
		JLabel diffbox10 = new JLabel(pastVals[13]);
		JPanel diffboxStatusPanel = new JPanel();
		diffboxStatusPanel.setLayout(new GridLayout(11,2));
		diffboxStatusPanel.add(diffbox0Label);
		diffboxStatusPanel.add(diffbox0);
		diffboxStatusPanel.add(diffbox1Label);
		diffboxStatusPanel.add(diffbox1);
		diffboxStatusPanel.add(diffbox2Label);
		diffboxStatusPanel.add(diffbox2);
		diffboxStatusPanel.add(diffbox3Label);
		diffboxStatusPanel.add(diffbox3);
		diffboxStatusPanel.add(diffbox4Label);
		diffboxStatusPanel.add(diffbox4);
		diffboxStatusPanel.add(diffbox5Label);
		diffboxStatusPanel.add(diffbox5);
		diffboxStatusPanel.add(diffbox6Label);
		diffboxStatusPanel.add(diffbox6);
		diffboxStatusPanel.add(diffbox7Label);
		diffboxStatusPanel.add(diffbox7);
		diffboxStatusPanel.add(diffbox8Label);
		diffboxStatusPanel.add(diffbox8);
		diffboxStatusPanel.add(diffbox9Label);
		diffboxStatusPanel.add(diffbox9);
		diffboxStatusPanel.add(diffbox10Label);
		diffboxStatusPanel.add(diffbox10);
	
		JPanel diffoverallStatusPanel = new JPanel();
		diffoverallStatusPanel.setLayout(new BorderLayout());
		JLabel diffoverallLabel = new JLabel("Change from Last Week:", JLabel.CENTER);
		JPanel diffmainPanel = new JPanel();
		diffmainPanel.setLayout(new GridLayout(1,2));
		diffmainPanel.add(diffoverallPanel);
		diffmainPanel.add(diffboxStatusPanel);
		diffoverallStatusPanel.add(diffmainPanel, BorderLayout.CENTER);
		diffoverallStatusPanel.add(diffoverallLabel, BorderLayout.NORTH);
		
		this.remove(superPanel);
		
		superPanel = new JPanel();
		superPanel.setLayout(new GridLayout(1,2));
		superPanel.add(overallStatusPanel);
		superPanel.add(diffoverallStatusPanel);
		
		
		this.add(superPanel, BorderLayout.CENTER);
		this.paintAll(this.getGraphics());
	}
	
	private void displayForInfantProgram()
	{
		Date d = new Date(System.currentTimeMillis());
		int year = d.getYear()+1900;
		
		String[] val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0;", 1);
		regRep = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal >= 1;", 1);
		readRep = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal >= 10;", 1);
		finishRep = new JLabel(val[0]);
		
		JPanel overallPanel = new JPanel();
		overallPanel.setLayout(new GridLayout(3,2));
		overallPanel.add(regLabel);
		overallPanel.add(regRep);
		overallPanel.add(readLabel);
		overallPanel.add(readRep);
		overallPanel.add(finishLabel);
		overallPanel.add(finishRep);
		
		//TODO  change from last year
		JLabel box0Label = new JLabel(" 0: ");
		JLabel box1Label = new JLabel(" 1: ");
		JLabel box2Label = new JLabel(" 2: ");
		JLabel box3Label = new JLabel(" 3: ");
		JLabel box4Label = new JLabel(" 4: ");
		JLabel box5Label = new JLabel(" 5: ");
		JLabel box6Label = new JLabel(" 6: ");
		JLabel box7Label = new JLabel(" 7: ");
		JLabel box8Label = new JLabel(" 8: ");
		JLabel box9Label = new JLabel(" 9: ");
		JLabel box10Label = new JLabel(" 10: ");
	
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal = 0;", 1);
		JLabel box0 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal = 1;");
		JLabel box1 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal = 2;");
		JLabel box2 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal = 3;");
		JLabel box3 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal = 4;");
		JLabel box4 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal = 5;");
		JLabel box5 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal = 6;");
		JLabel box6 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal = 7;");
		JLabel box7 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal = 8;");
		JLabel box8 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal = 9;");
		JLabel box9 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 0 AND highest_level_normal = 10;");
		JLabel box10 = new JLabel(val[0]);
		JPanel boxStatusPanel = new JPanel();
		boxStatusPanel.setLayout(new GridLayout(11,2));
		boxStatusPanel.add(box0Label);
		boxStatusPanel.add(box0);
		boxStatusPanel.add(box1Label);
		boxStatusPanel.add(box1);
		boxStatusPanel.add(box2Label);
		boxStatusPanel.add(box2);
		boxStatusPanel.add(box3Label);
		boxStatusPanel.add(box3);
		boxStatusPanel.add(box4Label);
		boxStatusPanel.add(box4);
		boxStatusPanel.add(box5Label);
		boxStatusPanel.add(box5);
		boxStatusPanel.add(box6Label);
		boxStatusPanel.add(box6);
		boxStatusPanel.add(box7Label);
		boxStatusPanel.add(box7);
		boxStatusPanel.add(box8Label);
		boxStatusPanel.add(box8);
		boxStatusPanel.add(box9Label);
		boxStatusPanel.add(box9);
		boxStatusPanel.add(box10Label);
		boxStatusPanel.add(box10);
		JPanel overallStatusPanel = new JPanel();
		overallStatusPanel.setLayout(new BorderLayout());
		JLabel overallLabel = new JLabel("YTD Info:", JLabel.CENTER);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,2));
		mainPanel.add(overallPanel);
		mainPanel.add(boxStatusPanel);
		overallStatusPanel.add(mainPanel, BorderLayout.CENTER);
		overallStatusPanel.add(overallLabel, BorderLayout.NORTH);
		
		String[] pastVals = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
		if(Constants.regularDB)
		{
			pastVals = getDiffFromLastWeek(0);
		}
		JLabel diffregLabel = new JLabel("Registrants:");
		JLabel diffreadLabel = new JLabel("Readers:");
		JLabel difffinishLabel = new JLabel("Finishers:");
		diffregLabel.setBorder(new EmptyBorder(0,20,0,0));
		diffreadLabel.setBorder(new EmptyBorder(0,20,0,0));
		difffinishLabel.setBorder(new EmptyBorder(0,20,0,0));
		JLabel diffregRep = new JLabel(pastVals[0]);
		JLabel diffreadRep = new JLabel(pastVals[1]);
		JLabel difffinishRep = new JLabel(pastVals[2]);
		
		JPanel diffoverallPanel = new JPanel();
		diffoverallPanel.setLayout(new GridLayout(3,2));
		diffoverallPanel.add(diffregLabel);
		diffoverallPanel.add(diffregRep);
		diffoverallPanel.add(diffreadLabel);
		diffoverallPanel.add(diffreadRep);
		diffoverallPanel.add(difffinishLabel);
		diffoverallPanel.add(difffinishRep);
		
		JLabel diffbox0Label = new JLabel(" 0: ");
		JLabel diffbox1Label = new JLabel(" 1: ");
		JLabel diffbox2Label = new JLabel(" 2: ");
		JLabel diffbox3Label = new JLabel(" 3: ");
		JLabel diffbox4Label = new JLabel(" 4: ");
		JLabel diffbox5Label = new JLabel(" 5: ");
		JLabel diffbox6Label = new JLabel(" 6: ");
		JLabel diffbox7Label = new JLabel(" 7: ");
		JLabel diffbox8Label = new JLabel(" 8: ");
		JLabel diffbox9Label = new JLabel(" 9: ");
		JLabel diffbox10Label = new JLabel(" 10: ");
		JLabel diffbox0 = new JLabel(pastVals[3]);
		JLabel diffbox1 = new JLabel(pastVals[4]);
		JLabel diffbox2 = new JLabel(pastVals[5]);
		JLabel diffbox3 = new JLabel(pastVals[6]);
		JLabel diffbox4 = new JLabel(pastVals[7]);
		JLabel diffbox5 = new JLabel(pastVals[8]);
		JLabel diffbox6 = new JLabel(pastVals[9]);
		JLabel diffbox7 = new JLabel(pastVals[10]);
		JLabel diffbox8 = new JLabel(pastVals[11]);
		JLabel diffbox9 = new JLabel(pastVals[12]);
		JLabel diffbox10 = new JLabel(pastVals[13]);
		JPanel diffboxStatusPanel = new JPanel();
		diffboxStatusPanel.setLayout(new GridLayout(11,2));
		diffboxStatusPanel.add(diffbox0Label);
		diffboxStatusPanel.add(diffbox0);
		diffboxStatusPanel.add(diffbox1Label);
		diffboxStatusPanel.add(diffbox1);
		diffboxStatusPanel.add(diffbox2Label);
		diffboxStatusPanel.add(diffbox2);
		diffboxStatusPanel.add(diffbox3Label);
		diffboxStatusPanel.add(diffbox3);
		diffboxStatusPanel.add(diffbox4Label);
		diffboxStatusPanel.add(diffbox4);
		diffboxStatusPanel.add(diffbox5Label);
		diffboxStatusPanel.add(diffbox5);
		diffboxStatusPanel.add(diffbox6Label);
		diffboxStatusPanel.add(diffbox6);
		diffboxStatusPanel.add(diffbox7Label);
		diffboxStatusPanel.add(diffbox7);
		diffboxStatusPanel.add(diffbox8Label);
		diffboxStatusPanel.add(diffbox8);
		diffboxStatusPanel.add(diffbox9Label);
		diffboxStatusPanel.add(diffbox9);
		diffboxStatusPanel.add(diffbox10Label);
		diffboxStatusPanel.add(diffbox10);
	
		JPanel diffoverallStatusPanel = new JPanel();
		diffoverallStatusPanel.setLayout(new BorderLayout());
		JLabel diffoverallLabel = new JLabel("Change from Last Week:", JLabel.CENTER);
		JPanel diffmainPanel = new JPanel();
		diffmainPanel.setLayout(new GridLayout(1,2));
		diffmainPanel.add(diffoverallPanel);
		diffmainPanel.add(diffboxStatusPanel);
		diffoverallStatusPanel.add(diffmainPanel, BorderLayout.CENTER);
		diffoverallStatusPanel.add(diffoverallLabel, BorderLayout.NORTH);
		
		this.remove(superPanel);
		
		superPanel = new JPanel();
		superPanel.setLayout(new GridLayout(1,2));
		superPanel.add(overallStatusPanel);
		superPanel.add(diffoverallStatusPanel);
		
		
		this.add(superPanel, BorderLayout.CENTER);
		this.paintAll(this.getGraphics());
	}
	
	private void displayForTeenProgram()
	{
		Date d = new Date(System.currentTimeMillis());
		int year = d.getYear()+1900;
		
		String[] val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2;", 1);
		regRep = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal >= 1;", 1);
		readRep = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal >= 10;", 1);
		finishRep = new JLabel(val[0]);
		
		JPanel overallPanel = new JPanel();
		overallPanel.setLayout(new GridLayout(3,2));
		overallPanel.add(regLabel);
		overallPanel.add(regRep);
		overallPanel.add(readLabel);
		overallPanel.add(readRep);
		overallPanel.add(finishLabel);
		overallPanel.add(finishRep);
		
		//TODO  change from last year
		JLabel box0Label = new JLabel(" 0: ");
		JLabel box1Label = new JLabel(" 1: ");
		JLabel box2Label = new JLabel(" 2: ");
		JLabel box3Label = new JLabel(" 3: ");
		JLabel box4Label = new JLabel(" 4: ");
		JLabel box5Label = new JLabel(" 5: ");
		JLabel box6Label = new JLabel(" 6: ");
		JLabel box7Label = new JLabel(" 7: ");
		JLabel box8Label = new JLabel(" 8: ");
		JLabel box9Label = new JLabel(" 9: ");
		JLabel box10Label = new JLabel(" 10: ");
	
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal = 0;", 1);
		JLabel box0 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal = 1;");
		JLabel box1 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal = 2;");
		JLabel box2 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal = 3;");
		JLabel box3 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal = 4;");
		JLabel box4 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal = 5;");
		JLabel box5 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal = 6;");
		JLabel box6 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal = 7;");
		JLabel box7 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal = 8;");
		JLabel box8 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal = 9;");
		JLabel box9 = new JLabel(val[0]);
		val = dbFriend.query1DstringRet("SELECT count(*) FROM Program_Data WHERE year = " + year + " AND program_id = 2 AND highest_level_normal = 10;");
		JLabel box10 = new JLabel(val[0]);
		JPanel boxStatusPanel = new JPanel();
		boxStatusPanel.setLayout(new GridLayout(11,2));
		boxStatusPanel.add(box0Label);
		boxStatusPanel.add(box0);
		boxStatusPanel.add(box1Label);
		boxStatusPanel.add(box1);
		boxStatusPanel.add(box2Label);
		boxStatusPanel.add(box2);
		boxStatusPanel.add(box3Label);
		boxStatusPanel.add(box3);
		boxStatusPanel.add(box4Label);
		boxStatusPanel.add(box4);
		boxStatusPanel.add(box5Label);
		boxStatusPanel.add(box5);
		boxStatusPanel.add(box6Label);
		boxStatusPanel.add(box6);
		boxStatusPanel.add(box7Label);
		boxStatusPanel.add(box7);
		boxStatusPanel.add(box8Label);
		boxStatusPanel.add(box8);
		boxStatusPanel.add(box9Label);
		boxStatusPanel.add(box9);
		boxStatusPanel.add(box10Label);
		boxStatusPanel.add(box10);
		JPanel overallStatusPanel = new JPanel();
		overallStatusPanel.setLayout(new BorderLayout());
		JLabel overallLabel = new JLabel("YTD Info:", JLabel.CENTER);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(1,2));
		mainPanel.add(overallPanel);
		mainPanel.add(boxStatusPanel);
		overallStatusPanel.add(mainPanel, BorderLayout.CENTER);
		overallStatusPanel.add(overallLabel, BorderLayout.NORTH);
		
		String[] pastVals = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
		if(Constants.regularDB)
		{
			pastVals = getDiffFromLastWeek(2);
		}
		JLabel diffregLabel = new JLabel("Registrants:");
		JLabel diffreadLabel = new JLabel("Readers:");
		JLabel difffinishLabel = new JLabel("Finishers:");
		diffregLabel.setBorder(new EmptyBorder(0,20,0,0));
		diffreadLabel.setBorder(new EmptyBorder(0,20,0,0));
		difffinishLabel.setBorder(new EmptyBorder(0,20,0,0));
		JLabel diffregRep = new JLabel(pastVals[0]);
		JLabel diffreadRep = new JLabel(pastVals[1]);
		JLabel difffinishRep = new JLabel(pastVals[2]);
		
		JPanel diffoverallPanel = new JPanel();
		diffoverallPanel.setLayout(new GridLayout(3,2));
		diffoverallPanel.add(diffregLabel);
		diffoverallPanel.add(diffregRep);
		diffoverallPanel.add(diffreadLabel);
		diffoverallPanel.add(diffreadRep);
		diffoverallPanel.add(difffinishLabel);
		diffoverallPanel.add(difffinishRep);
		
		JLabel diffbox0Label = new JLabel(" 0: ");
		JLabel diffbox1Label = new JLabel(" 1: ");
		JLabel diffbox2Label = new JLabel(" 2: ");
		JLabel diffbox3Label = new JLabel(" 3: ");
		JLabel diffbox4Label = new JLabel(" 4: ");
		JLabel diffbox5Label = new JLabel(" 5: ");
		JLabel diffbox6Label = new JLabel(" 6: ");
		JLabel diffbox7Label = new JLabel(" 7: ");
		JLabel diffbox8Label = new JLabel(" 8: ");
		JLabel diffbox9Label = new JLabel(" 9: ");
		JLabel diffbox10Label = new JLabel(" 10: ");
		JLabel diffbox0 = new JLabel(pastVals[3]);
		JLabel diffbox1 = new JLabel(pastVals[4]);
		JLabel diffbox2 = new JLabel(pastVals[5]);
		JLabel diffbox3 = new JLabel(pastVals[6]);
		JLabel diffbox4 = new JLabel(pastVals[7]);
		JLabel diffbox5 = new JLabel(pastVals[8]);
		JLabel diffbox6 = new JLabel(pastVals[9]);
		JLabel diffbox7 = new JLabel(pastVals[10]);
		JLabel diffbox8 = new JLabel(pastVals[11]);
		JLabel diffbox9 = new JLabel(pastVals[12]);
		JLabel diffbox10 = new JLabel(pastVals[13]);
		JPanel diffboxStatusPanel = new JPanel();
		diffboxStatusPanel.setLayout(new GridLayout(11,2));
		diffboxStatusPanel.add(diffbox0Label);
		diffboxStatusPanel.add(diffbox0);
		diffboxStatusPanel.add(diffbox1Label);
		diffboxStatusPanel.add(diffbox1);
		diffboxStatusPanel.add(diffbox2Label);
		diffboxStatusPanel.add(diffbox2);
		diffboxStatusPanel.add(diffbox3Label);
		diffboxStatusPanel.add(diffbox3);
		diffboxStatusPanel.add(diffbox4Label);
		diffboxStatusPanel.add(diffbox4);
		diffboxStatusPanel.add(diffbox5Label);
		diffboxStatusPanel.add(diffbox5);
		diffboxStatusPanel.add(diffbox6Label);
		diffboxStatusPanel.add(diffbox6);
		diffboxStatusPanel.add(diffbox7Label);
		diffboxStatusPanel.add(diffbox7);
		diffboxStatusPanel.add(diffbox8Label);
		diffboxStatusPanel.add(diffbox8);
		diffboxStatusPanel.add(diffbox9Label);
		diffboxStatusPanel.add(diffbox9);
		diffboxStatusPanel.add(diffbox10Label);
		diffboxStatusPanel.add(diffbox10);
	
		JPanel diffoverallStatusPanel = new JPanel();
		diffoverallStatusPanel.setLayout(new BorderLayout());
		JLabel diffoverallLabel = new JLabel("Change from Last Week:", JLabel.CENTER);
		JPanel diffmainPanel = new JPanel();
		diffmainPanel.setLayout(new GridLayout(1,2));
		diffmainPanel.add(diffoverallPanel);
		diffmainPanel.add(diffboxStatusPanel);
		diffoverallStatusPanel.add(diffmainPanel, BorderLayout.CENTER);
		diffoverallStatusPanel.add(diffoverallLabel, BorderLayout.NORTH);
		
		this.remove(superPanel);
		
		superPanel = new JPanel();
		superPanel.setLayout(new GridLayout(1,2));
		superPanel.add(overallStatusPanel);
		superPanel.add(diffoverallStatusPanel);
		
		
		this.add(superPanel, BorderLayout.CENTER);
		this.paintAll(this.getGraphics());
	}
}
