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
 * Writes data to CSV file format.
 * @author Anthony Schmitt
 *
 */
public class CSVWriter 
{

	/**
	 * Writes data to CSV file
	 * @param filePath Path for CSV file
	 * @param contents data to write to CSV file
	 */
	public static void writeCSVFile(String filePath, String[][] contents)
	{
		try 
		{
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
			int cols = contents[0].length; //assumes not empty
			for(int a = 0; a < contents.length; a++)
			{
//System.out.println("a: " + a);
				String outLine = "";
				for(int b = 0; b < cols; b++) 
				{
//System.out.println("b: " + b);
					if(contents[a][b] != null)
					{
						if(contents[a][b].contains(","))
						{
							outLine += "\"" + contents[a][b] + "\"";
						}
						else
						{
							outLine += contents[a][b];
						}
						if(b != cols-1)
						{
							outLine += ",";
						}
					}
					else
					{
						if(b != cols-1)
						{
							outLine += ",";
						}
					}
				}
				writer.write(outLine + "\r\n");
			}
			writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Writes data to CSV file
	 * @param filePath Path for CSV file
	 * @param things data to write to CSV file
	 */
	public static void writeCSVFile(String filePath, ArrayList<String[][]> things)
	{
		try 
		{
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
			for(int i = 0; i < things.size(); i++)
			{
				String[][] contents = things.get(i);
				int cols = contents[0].length; //assumes not empty
				for(int a = 0; a < contents.length; a++)
				{
	//System.out.println("a: " + a);
					String outLine = "";
					for(int b = 0; b < cols; b++) 
					{
	//System.out.println("b: " + b);
						if(contents[a][b] != null)
						{
							if(contents[a][b].contains(","))
							{
								outLine += "\"" + contents[a][b] + "\"";
							}
							else
							{
								outLine += contents[a][b];
							}
							if(b != cols-1)
							{
								outLine += ",";
							}
						}
						else
						{
							if(b != cols-1)
							{
								outLine += ",";
							}
						}
					}
					writer.write(outLine + "\r\n");
				}
			}
			writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes data to CSVFile, puts in string 'null' for any null values
	 * @param filePath CSV file path
	 * @param contents Data to write to the CSV file
	 */
	public static void writeCSVFilewithNull(String filePath, String[][] contents)
	{
		try 
		{
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
			int cols = contents[0].length; //assumes not empty
			for(int a = 0; a < contents.length; a++)
			{
//System.out.println("a: " + a);
				String outLine = "";
				for(int b = 0; b < cols; b++) 
				{
//System.out.println("b: " + b);
					if(contents[a][b] != null)
					{
						if(contents[a][b].contains(","))
						{
							outLine += "\"" + contents[a][b] + "\"";
						}
						else
						{
							outLine += contents[a][b];
						}
						if(b != cols-1)
						{
							outLine += ",";
						}
					}
					else
					{
						if(b != cols-1)
						{
							outLine += "null,";
						}
					}
				}
				writer.write(outLine + "\r\n");
			}
			writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
