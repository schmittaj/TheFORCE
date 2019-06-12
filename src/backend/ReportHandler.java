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


package backend;

import java.io.*;
import java.util.*;

/**
 * Helper class for dealing with Reports.
 * @author Anthony Schmitt
 *
 */
public class ReportHandler 
{
	public static final int DESCRIPTION_STATEMENTS = 0;
	public static final int QUERY_STATEMENTS = 1;
	public static final int ALL_STATEMENTS = 2;
	
	/**
	 * Grabs all the .rpt files from the reports folder
	 * @return Array of all the report file names
	 */
	public final static String[] getReportList()
	{
		ArrayList<String> reportList = new ArrayList<String>();
		File reportFolder = new File("reports");
		File[] fileList = reportFolder.listFiles();
		for(int a = 0; a < fileList.length; a++)
		{
			String nameHolder = fileList[a].getName();
			String[] splitHolder = nameHolder.split("\\.");
			if(splitHolder[1].equals("rpt"))
			{
				reportList.add(splitHolder[0]);
			}
		}
		String[] reportSelection = new String[reportList.size()];
		for(int a = 0; a < reportList.size(); a++)
		{
			reportSelection[a] = reportList.get(a);
		}
		return(reportSelection);
	}
	
	/**
	 * Grabs all the .rpt files from the reports folder and on top <Create New Report>
	 * @return Array of all the report file names
	 */
	public final static String[] getReportListWithEmpty()
	{
		ArrayList<String> reportList = new ArrayList<String>();
		File reportFolder = new File("reports");
		File[] fileList = reportFolder.listFiles();
		reportList.add("<Create New Report>");
		for(int a = 0; a < fileList.length; a++)
		{
			String nameHolder = fileList[a].getName();
			String[] splitHolder = nameHolder.split("\\.");
			if(splitHolder[1].equals("rpt"))
			{
				reportList.add(splitHolder[0]);
			}
		}
		String[] reportSelection = new String[reportList.size()];
		for(int a = 0; a < reportList.size(); a++)
		{
			reportSelection[a] = reportList.get(a);
		}
		return(reportSelection);
	}
	
	/**
	 * Adds new Queries and their descriptions to Reports
	 * @param fileName Name of the report file
	 * @param desc Description of the Query
	 * @param query The SQL Query
	 * @return True if successful
	 */
	public final static boolean fileAppend(String fileName, String desc, String query)
	{
		File report = new File("reports/" + fileName + ".rpt");
		boolean success = true;
		try 
		{
			FileWriter writer = new FileWriter(report, true);
			writer.write(desc + "\n");
			writer.write(query + "\n");
			writer.close();
		} 
		catch (IOException e) 
		{
			success = false;
			e.printStackTrace();
		}
		return(success);
	}
	
	/**
	 * Returns a list of statements
	 * @param filename Report to grab statements from
	 * @param typeOfStatement Type of statements to return 
	 * @return Array of specified types of statements from the report
	 */
	public final static String[] getStatements(String filename, int typeOfStatement)
	{
		String fullFileName = "reports/" + filename + ".rpt";
		String[] output = null;
		try 
		{
			BufferedReader read = new BufferedReader(new FileReader(fullFileName));
			String currentLine = read.readLine();
			ArrayList<String> lines = new ArrayList<String>();
			while(currentLine != null)
			{
				if(typeOfStatement == DESCRIPTION_STATEMENTS || typeOfStatement == ALL_STATEMENTS)
				{
					lines.add(currentLine);
				}
				currentLine = read.readLine();
				if(typeOfStatement == QUERY_STATEMENTS || typeOfStatement == ALL_STATEMENTS)
				{
					lines.add(currentLine);
				}
				currentLine = read.readLine();
			}
			read.close();
			output = new String[lines.size()];
			for(int a = 0; a < lines.size(); a++)
			{
				output[a] = lines.get(a);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return(output);
	}
	
	/**
	 * Runs a report and outputs the results into a .csv file
	 * @param filename report file name
	 * @param dbFriend Database handler
	 */
	public final static void runAndWrite(String filename, DBHandler dbFriend)
	{
		String fullFileName = "reports/" + filename + ".rpt";
		try 
		{
			BufferedReader read = new BufferedReader(new FileReader(fullFileName));
			String currentLine = read.readLine();
			String[][] schoolsList = dbFriend.query2DstringRet(Queries.ALL_SCHOOLS_PL_IDS, Queries.ALL_SCHOOLS_PL_IDS_COL_LEN);
			String[][] gradeList = dbFriend.query2DstringRet(Queries.ALL_GRADES_PL_IDS, Queries.ALL_GRADES_PL_IDS_COL_LEN);
			Hashtable<String, String> schoolHash = new Hashtable<String, String>();
			for(int a = 0; a < schoolsList.length; a++)
			{
				schoolHash.put(schoolsList[a][0], schoolsList[a][1]);
			}
			Hashtable<String, String> gradeHash = new Hashtable<String, String>();
			for(int a = 0; a < gradeList.length; a++)
			{
				gradeHash.put(gradeList[a][0], gradeList[a][1]);
			}
			ArrayList<String[][]> results = new ArrayList<String[][]>();
			while(currentLine != null)
			{
				String[][] descriptionLine = new String[1][1];
				descriptionLine[0][0] = currentLine;
				results.add(descriptionLine);
				
				String[][] columnTitles = {{"School","Grade","Count"}};
				results.add(columnTitles);
				
				currentLine = read.readLine(); 
				
				String[][] queryResult = dbFriend.query2DstringRet(currentLine, 3);
				for(int a = 0; a < queryResult.length; a++)
				{
					if(queryResult[a][0] != null && queryResult[a][0].length() > 0)
					{
						queryResult[a][0] = schoolHash.get(queryResult[a][0]);
					}
					if(queryResult[a][1] != null && queryResult[a][1].length() > 0)
					{
						queryResult[a][1] = gradeHash.get(queryResult[a][1]);
					}
				}
/*for(int a = 0; a < queryResult.length; a++)
{
	for(int b = 0; b < queryResult[0].length; b++)
	{
		System.out.print(queryResult[a][b] + "|");
	}
	System.out.println();
}*/
				results.add(queryResult);
				currentLine = read.readLine();
			}
			read.close();
			CSVWriter.writeCSVFile(filename + ".csv", results);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//TODO remove queries from file
}
