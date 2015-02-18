/*
 * Created Oct 2014 - Feb 2015 during COINS
 * by Peter Praeder, Michael Koetting, Vladimir Trajt
 */
package util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * An object of this class can be used to parse date strings of Wikipedia entries. By creating an object you read an XML file with regular expressions and save regular expressions as attributes of this object.
 * Then you can use methods of this object to get an exact or estimated year in which a person was born or died.
 *
 */
public class RegexParser {

	/**
	 *  List, which contains all loaded regular expresisons
	 */
	ArrayList<ArrayList<String>> regExCategory;
	/**
	 *  Change in the name order requires a change in the match date method: the calculations are bound to the order
	 */
	String[] regExCategoryName = { "yearAD", "yearBC", "centuryAD", "centuryBC", "millenniumAD", "millenniumBC",
			"centuryRangeAD", "centuryRangeBC", "centuryRangeBCAD", "millenniumRangeAD", "millenniumRangeBC",
			"millenniumRangeBCAD" };
	/**
	 * 
	 */
	private Pattern r;
	private Matcher m;
/**
 * The method is called when constructing an object of this class. By default  German language regular expressions are loaded.
 */
	public RegexParser() {
		XMLReader read = new XMLReader("src/util/regEx.xml", regExCategoryName);
		regExCategory = read.readXML("de_DE");
	}
	/**
	 * The method is called when constructing an object of this class. Regular expressions of the specified language are loaded.
	 * @param lang A language in which regular expressions are written
	 */
	public RegexParser(String lang) {
		XMLReader read = new XMLReader("src/util/regEx.xml", regExCategoryName);
		regExCategory = read.readXML(lang);
	}
	
/**
 * The method returns an exact or estimated year of birth or death by use of regular expressions.
 * @param input A String which contains information about when a person was born or died.
 * @return an integer value which represents a year in which a person was born or died.
 */
	public int matchDate(String input) {
		for (int i = 0; i < regExCategory.size(); i++) {
	
			for (String regex : regExCategory.get(i)) {
				if (input.matches(regex)) {
					r = Pattern.compile(regex);
					m = r.matcher(input);
					if (m.find()) {
						System.out.print("gotit");
						switch (i) {
						case 0:
							return parseYearAD();

						case 1:
							return parseYearBC();

						case 2:
							return parseCenturyADtoYear();

						case 3:
							return parseCenturyBCtoYear();

						case 4:
							return parseMillenniumADtoYear();

						case 5:
							return parseMillenniumBCtoYear();

						case 6:
							return parseCenturyADRangetoYear();

						case 7:
							return parseCenturyBCRangetoYear();

						case 8:
							return parseCenturyBCADRangetoYear();

						case 9:
							return parseMillenniumADRangetoYear();

						case 10:
							return parseMillenniumBCRangetoYear();

						case 11:
							return parseMillenniumBCADRangetoYear();
						}
					}
				}
			}
		}

		return -9999;
	}

	
/**
 * The method a year by parsing an attribute of the Matcher object m and then doing some calculation with this value.
 * The Matcher m is an attribute of a RegexParser object.
 * @return A year in which a person was born or died.
 */
	private int parseCenturyADRangetoYear() {
		return (((Integer.parseInt(m.group(1)) * 100) - 50) + ((Integer.parseInt(m.group(2)) * 100) - 50)) / 2;
	}

	/**
	 * The method a year by parsing an attribute of the Matcher object m and then doing some calculation with this value.
	 * The Matcher m is an attribute of a RegexParser object.
	 * @return A year in which a person was born or died.
	 */
	private int parseCenturyADtoYear() {
		return ((Integer.parseInt(m.group(1)) * 100) - 50);
	}
	/**
	 * The method a year by parsing an attribute of the Matcher object m and then doing some calculation with this value.
	 * The Matcher m is an attribute of a RegexParser object.
	 * @return A year in which a person was born or died.
	 */
	private int parseCenturyBCADRangetoYear() {
		// a century is 100 years. The mean is the difference between 2
		// centuries divided by 2 and multiplied with 100, for a better
		// implementation only *50 is executed
		return (Integer.parseInt(m.group(1)) - Integer.parseInt(m.group(2))) * 50;

	}
	/**
	 * The method a year by parsing an attribute of the Matcher object m and then doing some calculation with this value.
	 * The Matcher m is an attribute of a RegexParser object.
	 * @return A year in which a person was born or died.
	 */
	private int parseCenturyBCRangetoYear() {
		return -1 * parseCenturyADRangetoYear();
	}
	/**
	 * The method a year by parsing an attribute of the Matcher object m and then doing some calculation with this value.
	 * The Matcher m is an attribute of a RegexParser object.
	 * @return A year in which a person was born or died.
	 */
	private int parseCenturyBCtoYear() {
		return -1 * parseCenturyADtoYear();
	}
	/**
	 * The method a year by parsing an attribute of the Matcher object m and then doing some calculation with this value.
	 * The Matcher m is an attribute of a RegexParser object.
	 * @return A year in which a person was born or died.
	 */
	private int parseMillenniumADRangetoYear() {
		return (((Integer.parseInt(m.group(1)) * 1000) - 500) + ((Integer.parseInt(m.group(2)) * 1000) - 500)) / 2;

	}
	/**
	 * The method a year by parsing an attribute of the Matcher object m and then doing some calculation with this value.
	 * The Matcher m is an attribute of a RegexParser object.
	 * @return A year in which a person was born or died.
	 */
	private int parseMillenniumADtoYear() {
		return (Integer.parseInt(m.group(1))) * 1000;
	}
	/**
	 * The method a year by parsing an attribute of the Matcher object m and then doing some calculation with this value.
	 * The Matcher m is an attribute of a RegexParser object.
	 * @return A year in which a person was born or died.
	 */
	private int parseMillenniumBCADRangetoYear() {
		return (Integer.parseInt(m.group(1)) - Integer.parseInt(m.group(2))) * 500;

	}
	/**
	 * The method a year by parsing an attribute of the Matcher object m and then doing some calculation with this value.
	 * The Matcher m is an attribute of a RegexParser object.
	 * @return A year in which a person was born or died.
	 */
	private int parseMillenniumBCRangetoYear() {
		return -1 * parseMillenniumADRangetoYear();
	}
	/**
	 * The method a year by parsing an attribute of the Matcher object m and then doing some calculation with this value.
	 * The Matcher m is an attribute of a RegexParser object.
	 * @return A year in which a person was born or died.
	 */
	private int parseMillenniumBCtoYear() {
		return -(Integer.parseInt(m.group(1))) * 1000;
	}
	/**
	 * The method a year by parsing an attribute of the Matcher object m and then doing some calculation with this value.
	 * The Matcher m is an attribute of a RegexParser object.
	 * @return A year in which a person was born or died.
	 */
	private int parseYearAD() {
		return Integer.parseInt(m.group(1));
	}
	/**
	 * The method a year by parsing an attribute of the Matcher object m and then doing some calculation with this value.
	 * The Matcher m is an attribute of a RegexParser object.
	 * @return A year in which a person was born or died.
	 */
	private int parseYearBC() {
		return -1 * parseYearAD();
	}
}
