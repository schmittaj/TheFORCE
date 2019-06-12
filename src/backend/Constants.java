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

/**
 * This class holds constants for the program.
 * @author Anthony Schmitt
 *
 */
public class Constants 
{
	/*public static final double SIMILARITY_THRESHOLD = 0.75;
	public static final double SIMILARITY_PRIMARY_NAME_WEIGHT = 0.7;
	public static final double SIMILARITY_SECONDARY_NAME_WEIGHT = 0.3;*/
	public static final double SIMILARITY_THRESHOLD_ITEMS = 0.7;
	public static final double SIMILARITY_THRESHOLD_LAST_NAME = 0.71; //allows 2 errors on average last name length in database of 7
	public static final double SIMILARITY_THRESHOLD_FIRST_NAME =  0.66; //allows 2 errors on average first name length in database of 6
	public static final int AGE_CALC_MONTH = 7;
	public static final int AGE_CALC_DAY = 31;
	public static final int SIGN_UP_AGE_RANGE = 25;
	public static final int HIGHEST_MONTH_AGE_TO_DISPLAY = 18;
	public static final int[] MONTH_DAYS = {31,29,31,30,31,30,31,31,30,31,30,31};
	public static final int GENERAL_YEAR_RANGE = 40;
	public static final int BLOCKING_WINDOW_SIZE = 45; //code assumes odd number
	public static final String[][] COMMON_CITIES = {{"46","Sun Prairie"}};
	public static final int START_MONTH = 6;
	public static final int END_MONTH = 8;
	public static final int OLDEST_YEAR_RECORD = 2014;
	public static boolean adminEdit = false;
	public static boolean mergeMode = false;
	public static boolean splitMode = false;
	public static boolean regularDB = false;
	
	public static final String[][] COMMON_SCHOOLS = 
			{{"-1","None"},
			{"33","CH Bird Elementary"},
			{"59","Creekside Elementary"},
			{"82","Eastside Elementary"},
			{"125","Horizon Elementary"},
			{"200","Meadow View Elementary"},
			{"207","Northside Elementary"},
			{"251","Royal Oaks Elementary"},
			{"294","Token Springs Elementary"},
			{"304","Westside Elementary"},
			{"218","Patrick Marsh Middle School"},
			{"233","Prairie View Middle School"},
			{"30","Cardinal Heights Upper Middle School"},
			{"283","Sun Prairie High School"}};
	
	/**
	 * Assumes common and input are both not null, and have the same second dimension size
	 * @param common The common items to add to the top of the list
	 * @param input The normal list
	 * @return List with common item on top and the rest under (with commons repeated in their normal places)
	 */
	public static String[][] addCommonEntities(String[][] common, String[][] input)
	{
		String[][] output = new String[input.length+common.length][input[0].length]; //assumes input not null
		int index = 0;
		for(int a = 0; a < common.length; a++)
		{
			for(int b = 0; b < common[0].length; b++)
			{
				output[index][b] = common[a][b];
			}
			index++;
		}
		for(int a = 0; a < input.length; a++)
		{
			for(int b = 0; b < input[0].length; b++)
			{
				output[index][b] = input[a][b];
			}
			index++;
		}
		return output;
	}
	
	/**
	 * Calculates how many years old a person is given their birth date and the year to calculate their age in.
	 * @param dob Date of Birth
	 * @param year Year to calculate age in
	 * @return Age in years
	 */
	public static String calcAgeForDB(String dob, int year)
	{
		String output = "";
		
		String[] brokenUp = dob.split("/");
		int kidMonth = Integer.parseInt(brokenUp[0]);
		int kidDay = Integer.parseInt(brokenUp[1]);
		int kidYear = Integer.parseInt(brokenUp[2]);
		int yearValue = year - kidYear;
		int monthValue = Constants.AGE_CALC_MONTH - kidMonth;
		if(Constants.AGE_CALC_MONTH != kidMonth)
		{
			if(monthValue >= 0)
			{
				monthValue = 0;
			}
			else
			{
				monthValue = -1;
			}
		}
		else
		{
			int dayValue = Constants.AGE_CALC_DAY - kidDay;
			if(dayValue >= 0)
			{
				monthValue = 0;
			}
			else
			{
				monthValue = -1;
			}
		}
		
		output = "" + (yearValue + monthValue);
		
		return output;
	}
	
	/**
	 * Checks if given Date is a valid date (i.e. not something like Feb 31).
	 * @param dob Date in M(M)/D(D)/YYYY form
	 * @return True if date is valid
	 */
	public static boolean validDOB(String dob)
	{
		boolean output = true;
		
		String[] brokenUp = dob.split("/");
		int kidMonth = Integer.parseInt(brokenUp[0]);
		int kidDay = Integer.parseInt(brokenUp[1]);
		int kidYear = Integer.parseInt(brokenUp[2]);
		
		if(kidMonth == 2)
		{
			if(kidDay > MONTH_DAYS[kidMonth-1])
			{
				output = false;
			}
			else if(kidDay == 29)
			{
				if(kidYear % 4 != 0)
				{
					output = false;
				}
				if(kidYear % 400 == 0)
				{
					output = false;
				}
			}
		}
		else
		{
			if(kidDay > MONTH_DAYS[kidMonth-1])
			{
				output = false;
			}
		}
		
		return output;
	}
}
