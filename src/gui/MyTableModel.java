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

import javax.swing.table.*;

/**
 * This was necessary for updating information in the tables
 * @author Anthony Schmitt
 *
 */
class MyTableModel extends AbstractTableModel 
{
	
    private String[] columnNames;
    private Object[][] data;

    /**
     * Constructor
     * @param colTitle Column Titles
     * @param datas Data for the table
     */
    public MyTableModel(String[] colTitle, Object[][] datas)
    {
    	this.columnNames = colTitle;
    	this.data = datas;
    }
    
    /**
     * Returns the number of columns
     */
    public int getColumnCount() 
    {
    	int output = 0;
    	if(columnNames != null)
    	{
    		output = columnNames.length;
    	}
    	return output;
    }

    /**
     * Returns the number of rows
     */
    public int getRowCount() 
    {
    	int output = 0;
    	if(data != null)
    	{
    		output = data.length;
    	}
    	return output;
    }

    /**
     * Gives column name based on row
     */
    public String getColumnName(int col) 
    {
    	String output = "";
    	if(columnNames != null && col <= columnNames.length)
    	{
    		output = columnNames[col];
    	}
    	return output;
    }

    /**
     * Returns the object in specified place in table.
     */
    public Object getValueAt(int row, int col) 
    {
    	Object output = null;
    	if(row >= 0)
    	{
    		output = data[row][col];	
    	}
    	return output;
    }

    /**
     * Return the class of the items in a column.
     */
    public Class getColumnClass(int c) 
    {
    	Class output = null;
    	if(getValueAt(0,c) != null)
    	{
    		output = getValueAt(0, c).getClass();
    	}
    	else
    	{
    		String a = "";
    		output = a.getClass();
    	}
        return output;
    }
   
    /**
     * Cells are all set as not editable.
     */
    public boolean isCellEditable(int row, int col) 
    {
            return false;
    }
    
    /**
     * Sets the data and column titles
     * @param colTitle Column titles
     * @param datas Data for table
     */
    public void setData(String[] colTitle, Object[][] datas)
    {
    	this.columnNames = colTitle;
    	this.data = datas;
    }
}
