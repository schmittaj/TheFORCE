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
 * For reading in, and writing out convenient groupings of IDs from tables for making report generation easier.
 * @author Anthony Schmitt
 *
 */
public class GroupList 
{
	private String tableName, listName;
	private ArrayList<String> list;
	
	/**
	 * Generic constructor
	 */
	public GroupList()
	{
		this.tableName = "";
		this.listName = "";
		this.list = new ArrayList<String>();
	}
	
	/**
	 * Full constructor
	 * @param name Name for GroupList
	 * @param table Table name from the database
	 * @param lst List of IDs for the table in the database
	 */
	public GroupList(String name, String table, String[] lst)
	{
		this.tableName = table;
		this.listName = name;
		this.list = new ArrayList<String>();
		for(int a = 0; a < lst.length; a++)
		{
			list.add(lst[a]);
		}
	}
	
	/**
	 * Gives table name
	 * @return Table name
	 */
	public String getTableName()
	{
		return this.tableName;
	}
	
	/**
	 * Gives GroupList name
	 * @return GroupList name
	 */
	public String getListName()
	{
		return this.listName;
	}
	
	/**
	 * Gives list of IDs
	 * @return List of IDs
	 */
	public String[] getList()
	{
		return (String[]) this.list.toArray();
	}
	
	//TODO remove groupList from file, check that all reference ids still exist, otherwise remove them
	
	/**
	 * Adds a new GroupList to specified file
	 * @param grp GroupList to add to file
	 * @param fileName File of GroupLists
	 */
	public void addNewGroupListToFile(GroupList grp, String fileName)
	{
		try 
		{
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName,true)));
			writer.write(grp.getListName() + ":" + grp.getTableName() + "\r\n");
			String[] tmp = grp.getList();
			for(int a = 0; a < tmp.length; a++)
			{
				writer.write(tmp[a]);
				if(a != tmp.length -1)
				{
					writer.write(",");
				}
			}
			writer.write("\r\n");
			writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Factory for creating GroupLists previously specified and saved in a file.
	 * @param fileName GroupList file
	 * @return Array of GroupLists
	 */
	public GroupList[] readFromFile(String fileName)
	{
		GroupList[] output = null;
		ArrayList<GroupList> inputs = new ArrayList<GroupList>();
		
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String currentLine = reader.readLine();
			while(currentLine != null)
			{
				String[] holder = currentLine.split(":");
				String lname = holder[0];
				String tname = holder[1];
				currentLine = reader.readLine();
				holder = currentLine.split(",");
				GroupList tmpLst = new GroupList(lname, tname, holder);
				inputs.add(tmpLst);
				currentLine = reader.readLine();
			}
			reader.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		if(!inputs.isEmpty())
		{
			output = new GroupList[inputs.size()];
			for(int a = 0; a < inputs.size(); a++)
			{
				output[a] = inputs.get(a);
			}
		}
		
		return output;
	}
}
