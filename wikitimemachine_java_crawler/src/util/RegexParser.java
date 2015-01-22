package util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser {

	// List, which contains all loaded regular expresisons
	ArrayList<ArrayList<String>> regExCategory;
	// Change in the name order requires a change in the match date method: the
	// calculations are bound to the order
	String[] regExCategoryName = { "yearAD", "yearBC", "centuryAD", "centuryBC", "millenniumAD", "millenniumBC",
			"centuryRangeAD", "centuryRangeBC", "centuryRangeBCAD", "millenniumRangeAD", "millenniumRangeBC",
			"millenniumRangeBCAD" };

	private Pattern r;
	private Matcher m;

	public RegexParser() {
		// TODO create regEx file path and make languages changeable
		XMLReader read = new XMLReader("src/util/regEx.xml", regExCategoryName);
		regExCategory = read.readXML("de_DE");
	}

	public int matchDate(String input) {
		for (int i = 0; i < regExCategory.size(); i++) {
			/*
				 *
				 */
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

	

	private int parseCenturyADRangetoYear() {
		return (((Integer.parseInt(m.group(1)) * 100) - 50) + ((Integer.parseInt(m.group(2)) * 100) - 50)) / 2;
	}

	private int parseCenturyADtoYear() {
		return ((Integer.parseInt(m.group(1)) * 100) - 50);
	}

	private int parseCenturyBCADRangetoYear() {
		// a century is 100 years. The mean is the difference between 2
		// centuries divided by 2 and multiplied with 100, for a better
		// implementation only *50 is executed
		return (Integer.parseInt(m.group(1)) - Integer.parseInt(m.group(2))) * 50;

	}

	private int parseCenturyBCRangetoYear() {
		return -1 * parseCenturyADRangetoYear();
	}

	private int parseCenturyBCtoYear() {
		return -1 * parseCenturyADtoYear();
	}

	private int parseMillenniumADRangetoYear() {
		return (((Integer.parseInt(m.group(1)) * 1000) - 500) + ((Integer.parseInt(m.group(2)) * 1000) - 500)) / 2;

	}

	private int parseMillenniumADtoYear() {
		return (Integer.parseInt(m.group(1))) * 1000;
	}

	private int parseMillenniumBCADRangetoYear() {
		return (Integer.parseInt(m.group(1)) - Integer.parseInt(m.group(2))) * 500;

	}

	private int parseMillenniumBCRangetoYear() {
		return -1 * parseMillenniumADRangetoYear();
	}

	private int parseMillenniumBCtoYear() {
		return -(Integer.parseInt(m.group(1))) * 1000;
	}

	private int parseYearAD() {
		return Integer.parseInt(m.group(1));
	}

	private int parseYearBC() {
		return -1 * parseYearAD();
	}
}
