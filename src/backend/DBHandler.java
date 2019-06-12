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

import java.sql.*;
import java.util.*;

import org.sqlite.SQLiteConnection;

/**
 * Handles the connection to the SQLite database
 * @author Anthony Schmitt
 *
 */
public class DBHandler 
{
	private Connection conn = null;
	//private SQLiteConnection con = null;
	private String url;
	//private String fileLoc;
	
	/**
	 * Constructor
	 * @param dbFileLocation Location for the SQLite database file
	 */
	public DBHandler(String dbFileLocation)
	{
		//fileLoc = dbFileLocation;
		this.url = "jdbc:sqlite:" + dbFileLocation;
	}
	
	public void openSecureConnection()
	{
		try
		{
			conn = DriverManager.getConnection(url,"","prairie");
			conn.setAutoCommit(false);
			//System.out.println("Connected");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens the database connection
	 */
	public void openConnection()
	{
		try
		{
			conn = DriverManager.getConnection(url);
			conn.setAutoCommit(false);
			//System.out.println("Connected");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the database connection
	 */
	public void closeConnection()
	{
		try 
		{
			conn.commit();
			conn.close();
			//System.out.println("Connection Closed");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Executes an update on the database
	 * @param statement SQL update, insert, or delete statement
	 */
	public void executeUpdate(String statement)
	{
		try 
		{
			openConnection();
			Statement state = conn.createStatement();
			state.setQueryTimeout(30);
			state.executeUpdate(statement);
			state.close();
			closeConnection();
			//System.out.println(statement);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs a query and gives back the ResultSet object created
	 * @param statement SQL query
	 * @return ResultSet of the SQL query
	 */
	public ResultSet executeQuery(String statement)
	{
		ResultSet output = null;
		try 
		{
			openConnection();
			Statement state = conn.createStatement();
			state.setQueryTimeout(30);
			output = state.executeQuery(statement);
			state.closeOnCompletion();
			closeConnection();
			//System.out.println("NOT USED??");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return output;
	}
			
	/**
	 * Executes SQL query and returns the data as a 2D String array
	 * @param statement SQL query
	 * @param colLen Number of columns that should be in the result set of the query
	 * @return 2D String array of results of the query
	 */
	public String[][] query2DstringRet(String statement, int colLen)
	{
		String[][] output = null;
		try 
		{
			openConnection();
			Statement state = conn.createStatement();
			
			state.setQueryTimeout(30);
			ResultSet rs = state.executeQuery(statement);
			boolean notEmpty = rs.next();
			if(notEmpty)
			{
				ArrayList<String[]> rl = new ArrayList<String[]>();
				while(!rs.isAfterLast())
				{
					String[] tempList = new String[colLen];
					for(int a = 1; a <= colLen; a++)
					{
						String tempString = rs.getString(a);
						if(!rs.wasNull())
						{
							tempList[a-1] = tempString;
						}
						else
						{
							tempList[a-1] = "";
						}
					}
					rs.next();
					rl.add(tempList);
				}
				output = new String[rl.size()][colLen];
				for(int a = 0; a < rl.size(); a++)
				{
					output[a] = rl.get(a);
				}
			}
			else
			{
				//System.out.println(statement);
			}
			rs.close();
			state.close();
			closeConnection();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * Executes SQL query and returns the data as a 1D String array, for if you expect just 1 column, 
	 * but multiple rows as a result.
	 * @param statement SQL query
	 * @return 1D String array of results of the query
	 */
	public String[] query1DstringRet(String statement)
	{
		String[] output = null;
		try 
		{
			openConnection();
			Statement state = conn.createStatement();
			state.setQueryTimeout(30);
			ResultSet rs = state.executeQuery(statement);
			ArrayList<String> rl = new ArrayList<String>();
			rs.next();
			while(!rs.isAfterLast())
			{
				String tempStr = rs.getString(1);
				if(rs.wasNull())
				{
					tempStr = "";
				}
				
				rs.next();
				rl.add(tempStr);
			}
			output = new String[rl.size()];
			for(int a = 0; a < rl.size(); a++)
			{
				output[a] = rl.get(a);
			}
			rs.close();
			state.close();
			closeConnection();
			//System.out.println("is this used?");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return output;
	}
	
	/**
	 * Executes SQL query and returns the data as a 1D String array, for if you expect multiple columns,
	 * but just one row as a result.
	 * @param statement SQL query
	 * @param colLen Number of columns that should be in the result set of the query
	 * @return 1D String array of results of the query
	 */
	public String[] query1DstringRet(String statement, int colLen)
	{
		String[] output = null;
		try 
		{
			openConnection();
			Statement state = conn.createStatement();
			state.setQueryTimeout(30);
			
			ResultSet rs = state.executeQuery(statement);
			ArrayList<String> rl = new ArrayList<String>();
			rs.next();
			if(!rs.isClosed())
			{
				for(int a = 1; a <= colLen; a++)
				{
					String tempStr = rs.getString(a);
					if(rs.wasNull())
					{
						tempStr = "";
					}
					rl.add(tempStr);
				}
				output = new String[colLen];
				for(int a = 0; a < colLen; a++)
				{
					output[a] = rl.get(a);
				}
			}
			else
			{
				//System.out.println(statement);
			}
			rs.close();
			state.close();
			closeConnection();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return output;
	}
	
}
