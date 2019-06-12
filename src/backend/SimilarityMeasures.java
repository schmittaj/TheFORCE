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
 * Similarity Measures for comparing reader fields.
 * @author Anthony Schmitt
 *
 */
public class SimilarityMeasures 
{
	/**
	 * Calculates a normalized Edit Distance
	 * @param x String to compare
	 * @param y Other String to compare
	 * @return Normalized edit distance
	 */
	public static double editDistance(String x, String y)
	{
		if(x != null && y != null && x.length() != 0 && y.length() != 0)
		{
			int[][] calculation = new int[x.length()][y.length()];
			for(int a = 0; a < calculation.length; a++)
			{
				calculation[a][0] = a;
			}
			
			for(int a = 0; a < calculation[0].length; a++)
			{
				calculation[0][a] = a;
			}
			
			for(int a = 1; a < calculation.length; a++)
			{
				for(int b = 1; b < calculation[0].length; b++)
				{
					if(y.charAt(b) == x.charAt(a))
					{
						calculation[a][b] = calculation[a-1][b-1];
					}
					else
					{
						calculation[a][b] = min((calculation[a-1][b-1]+1),(calculation[a-1][b]+1),(calculation[a][b-1]+1));
					}
				}
			}
			
			return (1 - ((double)calculation[x.length()-1][y.length()-1])/(max(x.length(), y.length())));
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Helper function that fives the minimum value out of 3 integers
	 * @param a an int
	 * @param b an int
	 * @param c an int
	 * @return The minimum of the 3 inputed integers
	 */
	private static int min(int a, int b, int c)
	{
		int output = a;
		if(b < output)
		{
			output = b;
		}
		if(c < output)
		{
			output = c;
		}
		return output;
	}

	/**
	 * Helper function that gives the max value of two integers
	 * @param a an int
	 * @param b an int
	 * @return The max of the inputed integers
	 */
	private static int max(int a, int b)
	{
		int output = a;
		if(b > output)
		{
			output = b;
		}
		return output;
	}
	
	/**
	 * Calculated a normalized difference between years
	 * @param x a Year
	 * @param y another Year
	 * @return Normalized difference between the years
	 */
	public static double yearDifference(String x, String y)
	{
		if(x != null && y != null && x.length() != 0 && y.length() != 0)
		{
			double value = (1 - (double)Math.abs( (Integer.parseInt(x) - Integer.parseInt(y)) ) / 100);
			return (value);
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Says if the two strings are the same or not
	 * @param x a String
	 * @param y another String
	 * @return different (1) or not (0)
	 */
	public static double placeDifference(String x, String y)
	{
		double output = 0;
		if(x != null && y != null)
		{
			if(x.equals(y))
			{
				output = 1;
			}
		}
		return output;
		
	}
	
	/**
	 * Calculated a normalized difference between ages making them equivalent for their given years,
	 * i.e. if someone is 10 in 2010 and 1 in 2001 will say the ages are equivalent.
	 * @param x an age
	 * @param y another age
	 * @param xYear year of age x
	 * @param yYear year of age y
	 * @return Normalized difference in age
	 */
	public static double ageDifference(String x, String y, String xYear, String yYear)
	{
		double output = 0;
		if(x != null && y != null && x.length() != 0 && y.length() != 0)
		{
			int xYearVal = Integer.parseInt(xYear);
			int yYearVal = Integer.parseInt(yYear);
			int xVal = Integer.parseInt(x);
			int yVal = Integer.parseInt(y);
			int yearDiff = Math.abs( (xYearVal - yYearVal) );
			output = (1 - (double)Math.abs( (xVal - yVal) - yearDiff ) / 18);
		}
		return output;
	}
	
	/**
	 * Calculates a normalized difference in grade levels based on entered years, 
	 * i.e. if it is grade 1 in 2010 and grade 3 in 2012 they are seen as the same 
	 * @param x a grade
	 * @param y another grade
	 * @param xYear year of grade x
	 * @param yYear year of grade y
	 * @return Normalized grade difference
	 */
	public static double gradeDifference(String x, String y, String xYear, String yYear)
	{
		double xVal = 0;
		double yVal = 0;
		if(x != null && y != null)
		{
			switch (x)
			{
				case "Pre-K":
					xVal = 1;
					break;
				case "4K":
					xVal = 1;
					break;
				case "4k":
					xVal = 1;
					break;
				case "Early Childhood":
					xVal = 1;
					break;
				case "K":
					xVal = 2;
					break;
				case "k":
					xVal = 2;
					break;
				case "1":
					xVal = 3;
					break;
				case "2":
					xVal = 4;
					break;
				case "3":
					xVal = 5;
					break;
				case "4":
					xVal = 6;
					break;
				case "4th":
					xVal = 6;
					break;
				case "5":
					xVal = 7;
					break;
				case "6":
					xVal = 8;
					break;
				case "7":
					xVal = 9;
					break;
				case "8":
					xVal = 10;
					break;
				case "9":
					xVal = 11;
					break;
				case "10":
					xVal = 12;
					break;
				case "11":
					xVal = 13;
					break;
				case "12":
					xVal = 14;
					break;
				default:
					System.out.println("error with value: " + x);
			}
			switch (y)
			{
				case "Pre-K":
					yVal = 1;
					break;
				case "4K":
					yVal = 1;
					break;
				case "4k":
					yVal = 1;
					break;
				case "Early Childhood":
					yVal = 1;
					break;
				case "K":
					yVal = 2;
					break;
				case "k":
					yVal = 2;
					break;
				case "1":
					yVal = 3;
					break;
				case "2":
					yVal = 4;
					break;
				case "3":
					yVal = 5;
					break;
				case "4":
					yVal = 6;
					break;
				case "4th":
					yVal = 6;
					break;
				case "5":
					yVal = 7;
					break;
				case "6":
					yVal = 8;
					break;
				case "7":
					yVal = 9;
					break;
				case "8":
					yVal = 10;
					break;
				case "9":
					yVal = 11;
					break;
				case "10":
					yVal = 12;
					break;
				case "11":
					yVal = 13;
					break;
				case "12":
					yVal = 14;
					break;
				default:
					System.out.println("error with value: " + y);
			}
		}
		int xYearVal = Integer.parseInt(xYear);
		int yYearVal = Integer.parseInt(yYear);
		int yearDiff = Math.abs( (xYearVal - yYearVal) );
		double output = (1 - (double)Math.abs( (xVal - yVal) - yearDiff ) / 14);
		return(output);
	}
	
	/**
	 * Calculated a normalized difference in Dates of Birth
	 * @param dob1 a Date of Birth
	 * @param dob2 another Date of Birth
	 * @return Normalized difference of the dates
	 */
	public static double dobDiff(String dob1, String dob2)
	{
		double output = 0;
		
		if(dob1 != null && dob2 != null && !dob1.equals("") && !dob2.equals(""))
		{
			String[] brokenUp = dob1.split("/");
			double month1 = Integer.parseInt(brokenUp[0]);
			double day1 = Integer.parseInt(brokenUp[1]);
			double year1 = Integer.parseInt(brokenUp[2]);
			
			brokenUp = dob2.split("/");
			double month2 = Integer.parseInt(brokenUp[0]);
			double day2 = Integer.parseInt(brokenUp[1]);
			double year2 = Integer.parseInt(brokenUp[2]);
			
			output = 1 - ((Math.abs(month1-month2)/12)+(Math.abs(day1-day2)/31)+(Math.abs(year1-year2)/14));
		}
		
		return output;
	}
}
