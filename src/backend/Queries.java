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
 * Holder for much used and static queries, and their column lengths.
 * @author Anthony Schmitt
 *
 */
public class Queries 
{
	public static final String ALL_KIDS_INFO = "SELECT last_name,first_name,date_of_birth,most_recent_phone,most_recent_school,most_recent_city FROM Children;";
	public static final int ALL_KIDS_INFO_COL_LEN = 6;
	public static final String ALL_KIDS_IDS = "SELECT id FROM Children;";
	public static final String ALL_KIDS_INFO_PL_IDS = "SELECT * FROM Children ORDER BY last_name ASC, first_name ASC;";
	public static final int ALL_KIDS_INFO_PL_IDS_LEN = 9;
	public static final String ALL_SCHOOLS_PL_IDS = "SELECT * FROM School;";
	public static final String ALL_SCHOOLS_PL_IDS_ALPHA = "SELECT * FROM School ORDER BY name;";
	public static final int ALL_SCHOOLS_PL_IDS_COL_LEN = 2;
	public static final String ALL_CITIES_PL_IDS = "SELECT * FROM City;";
	public static final String ALL_CITIES_PL_IDS_ALPHA = "SELECT * FROM City ORDER BY name;";
	public static final int ALL_CITIES_PL_IDS_COL_LEN = 2;
	public static final String ALL_CITIES = "SELECT name FROM City;";
	public static final String ALL_CITIES_ALPHA = "SELECT name FROM City ORDER BY name;";
	public static final String ALL_GRADES = "SELECT name FROM Grade;";
	public static final String ALL_SCHOOLS = "SELECT name FROM School;";
	public static final String ALL_SCHOOLS_ALPHA = "SELECT name FROM School ORDER BY name;";
	public static final String ALL_GRADES_PL_IDS = "SELECT * FROM Grade;";
	public static final String ALL_GRADES_PL_IDS_ALPHA = "SELECT * FROM Grade ORDER BY name;";
	public static final int ALL_GRADES_PL_IDS_COL_LEN = 2;
	public static final String ALL_PROGRAMS_PL_IDS = "SELECT * FROM Program;";
	public static final int ALL_PROGRAMS_PL_IDS_COL_LEN = 2;
	public static final String HIGHEST_CHILD_ID = "SELECT MAX(id) FROM Children;";
	public static final String ALL_WEEKLY_STATS = "SELECT * FROM Weekly_Stats WHERE program_id = -1;";
	public static final int ALL_WEEKLY_STATS_COL_LEN = 25;
	public static final String INFANT_WEEKLY_STATS = "SELECT * FROM Weekly_Stats WHERE program_id = 0;";
	public static final int INFANT_WEEKLY_STATS_COL_LEN = 25;
	public static final String CHILDREN_WEEKLY_STATS = "SELECT * FROM Weekly_Stats WHERE program_id = 1;";
	public static final int CHILDREN_WEEKLY_STATS_COL_LEN = 25;
	public static final String TEEN_WEEKLY_STATS = "SELECT * FROM Weekly_Stats WHERE program_id = 2;";
	public static final int TEEN_WEEKLY_STATS_COL_LEN = 25;
	
	/**
	 * Does the query:
	 * SELECT count(tabl.child_id)
	 * FROM
	 *    (SELECT info.child_id, min(info.year) as yr
	 *	   FROM 
	 *	      (SELECT Program_Data.child_id, Program_Data.year 
	 *	       FROM Program_Data, 
	 *		      (SELECT child_id 
	 *		       FROM Program_Data 
	 *		       WHERE year = _param_year_) as kids 
	 *	       WHERE Program_Data.child_id = kids.child_id) as info
	 *     GROUP BY info.child_id) as tabl
     * WHERE tabl.yr = _param_year_;
	 * @param year Year for which you want to know if it was the first year a person did the program
	 * @param dbfriend Database connection
	 * @return Count of unique readers for that year
	 */
	public static int Get_Uqique_For_Year(int year, DBHandler dbfriend)
	{
		int output = -1;
		
		String[] val = dbfriend.query1DstringRet("SELECT count(tabl.child_id)\r\n" + 
				"FROM\r\n" + 
				"	(SELECT info.child_id, min(info.year) as yr\r\n" + 
				"	FROM \r\n" + 
				"		(SELECT Program_Data.child_id, Program_Data.year \r\n" + 
				"		FROM Program_Data, \r\n" + 
				"			(SELECT child_id \r\n" + 
				"			FROM Program_Data \r\n" + 
				"			WHERE year = " + year + ") as kids \r\n" + 
				"		WHERE Program_Data.child_id = kids.child_id) as info\r\n" + 
				"	GROUP BY info.child_id) as tabl\r\n" + 
				"WHERE tabl.yr = " + year + 
				"ORDER BY kids.last_name, kids.first_name;");
		
		if(val != null)
		{
			output = Integer.parseInt(val[0]);
		}
		
		return output;
	}
	
	/**
	 * Runs the query:
	 * SELECT kids.first_name, kids.last_name, kids.date_of_birth, data.age, kids.most_recent_phone, kids. most_recent_email, kids.most_recent_parent, data.city_id, data.school_id, data.grade_id, data.program_id, data.highest_level_normal, data.extra_one, data.extra_two, data.extra_three, data.extra_four, data.extra_five, data.extra_six, data.extra_seven, data.extra_eight, data.extra_nine, data.extra_ten, data.pool_pass_given
	 * FROM
	 *    (SELECT child_id, grade_id, school_id, city_id, program_id, highest_level_normal, extra_one, extra_two, extra_three, extra_four, extra_five, extra_six, extra_seven, extra_eight, extra_nine, extra_ten, pool_pass_given, age 
	 *     FROM Program_Data 
	 *     WHERE _param_year_ = 2018) as data,
	 *    (SELECT id, last_name, first_name, date_of_birth, most_recent_parent, most_recent_email, most_recent_phone 
	 *     FROM Children) as kids
	 * WHERE data.child_id = kids.id;
	 * @param year year for which you want data
	 * @param dbfriend Database connection
	 * @return Specified year's data
	 */
	public static String[][] getYearData(int year, DBHandler dbfriend)
	{
		return dbfriend.query2DstringRet("SELECT kids.first_name, kids.last_name, kids.date_of_birth, data.age, kids.most_recent_phone, kids. most_recent_email, kids.most_recent_parent, data.city_id, data.school_id, data.grade_id, data.program_id, data.highest_level_normal, data.extra_one, data.extra_two, data.extra_three, data.extra_four, data.extra_five, data.extra_six, data.extra_seven, data.extra_eight, data.extra_nine, data.extra_ten, data.pool_pass_given\r\n" + 
				"FROM\r\n" + 
				"	(SELECT child_id, grade_id, school_id, city_id, program_id, highest_level_normal, extra_one, extra_two, extra_three, extra_four, extra_five, extra_six, extra_seven, extra_eight, extra_nine, extra_ten, pool_pass_given, age \r\n" + 
				"	 FROM Program_Data \r\n" + 
				"	 WHERE year = "+year+") as data,\r\n" + 
				"	(SELECT id, last_name, first_name, date_of_birth, most_recent_parent, most_recent_email, most_recent_phone \r\n" + 
				"	FROM Children) as kids\r\n" + 
				"WHERE data.child_id = kids.id\r\n"	+ 
				"ORDER BY kids.last_name, kids.first_name;",23);
	}
}
