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

import java.util.*;

/**
 * The Implementation portion for notifying classes implementing the DatabaseChangeListener of database changes.
 * @author Anthony Schmitt
 *
 */
public class DatabaseChangeListenerImplementer 
{
	private ArrayList<DatabaseChangeListener> listenList;
	
	/**
	 * Constructor
	 */
	public DatabaseChangeListenerImplementer()
	{
		listenList = new ArrayList<DatabaseChangeListener>();
	}
	
	/**
	 * Adds listeners to list
	 * @param lis DB change listener
	 */
	public void addListener(DatabaseChangeListener lis)
	{
		listenList.add(lis);
	}
	
	/**
	 * Notifies all the listeners in the list of a change and invokes their change handlers
	 */
	public void notifyChange()
	{
		for(int a = 0; a < listenList.size(); a++)
		{
			listenList.get(a).databaseChanged();
		}
	}
}
