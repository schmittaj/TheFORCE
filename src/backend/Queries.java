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
}
