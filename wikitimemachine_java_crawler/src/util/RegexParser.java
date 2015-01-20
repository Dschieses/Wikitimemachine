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

	/*
	 * String gerBirthAD = "Kategorie:Geboren (\\d{1,4})"; String gerBirthBC =
	 * "Kategorie:Geboren (\\d{1,4}) v. Chr.";
	 * 
	 * String gerBirthCenturyAD =
	 * "Kategorie:Geboren im (\\d{1,2}). Jahrhundert"; String gerBirthCenturyAD2
	 * = "Kategorie:Geboren \\((\\d{1,4}). Jahrhundert\\)"; String
	 * gerBirthCenturyBC =
	 * "Kategorie:Geboren im (\\d{1,2}). Jahrhundert v. Chr."; // is actually
	 * not used as there are no entries for this regEx String gerBirthCenturyBC2
	 * = "Kategorie:Geboren \\((\\d{1,4}). Jahrhundert\\) v. Chr.";
	 * 
	 * String gerBirthCenturyBCAD =
	 * "Kategorie:Geboren im (\\d{1,2}). Jahrhundert v. Chr. oder (\\d{1,2}). Jahrhundert"
	 * ;
	 * 
	 * // Century not exactly defined (Range) String gerBirthCenturyRBC =
	 * "Kategorie:Geboren im (\\d{1,2}). oder (\\d{1,2}). Jahrhundert v. Chr.";
	 * String gerBirthCenturyRAD =
	 * "Kategorie:Geboren im (\\d{1,2}). oder (\\d{1,2}). Jahrhundert";
	 * 
	 * String gerBirthMilleniumAD =
	 * "Kategorie:Geboren im (\\d{1,2}). Jahrtausend"; String
	 * gerBirthMilleniumBC =
	 * "Kategorie:Geboren im (\\d{1,2}). Jahrtausend v. Chr.";
	 * 
	 * String gerBirthMilleniumBCAD =
	 * "Kategorie:Geboren im (\\d{1,2}). Jahrtausend v. Chr. oder (\\d{1,2}). Jahrtausend"
	 * ; // Millenium not exactly defined (Range) String gerBirthMilleniumRAD =
	 * "Kategorie:Geboren im (\\d{1,2}). oder (\\d{1,2}). Jahrtausend"; String
	 * gerBirthMilleniumRBC =
	 * "Kategorie:Geboren im (\\d{1,2}). oder (\\d{1,2}). Jahrtausend v. Chr.";
	 * 
	 * // same regEx for death dates String gerDeathAD =
	 * "Kategorie:Gestorben (\\d{1,4})"; String gerDeathBC =
	 * "Kategorie:Gestorben (\\d{1,4}) v. Chr.";
	 * 
	 * String gerDeathCenturyAD =
	 * "Kategorie:Gestorben im (\\d{1,2}). Jahrhundert"; String
	 * gerDeathCenturyAD2 = "Kategorie:Gestorben \\((\\d{1,4}). Jahrhundert\\)";
	 * String gerDeathCenturyBC =
	 * "Kategorie:Gestorben im (\\d{1,2}). Jahrhundert v. Chr."; // is actually
	 * not used as there are no entries for this regEx String gerDeathCenturyBC2
	 * = "Kategorie:Gestorben \\((\\d{1,4}). Jahrhundert\\) v. Chr.";
	 * 
	 * String gerDeathCenturyBCAD =
	 * "Kategorie:Gestorben im (\\d{1,2}). Jahrhundert v. Chr. oder (\\d{1,2}). Jahrhundert"
	 * ;
	 * 
	 * // Century not exactly defined (Range) String gerDeathCenturyRBC =
	 * "Kategorie:Gestorben im (\\d{1,2}). oder (\\d{1,2}). Jahrhundert v. Chr."
	 * ; String gerDeathCenturyRAD =
	 * "Kategorie:Gestorben im (\\d{1,2}). oder (\\d{1,2}). Jahrhundert";
	 * 
	 * String gerDeathMilleniumAD =
	 * "Kategorie:Gestorben im (\\d{1,2}). Jahrtausend"; String
	 * gerDeathMilleniumBC =
	 * "Kategorie:Gestorben im (\\d{1,2}). Jahrtausend v. Chr.";
	 * 
	 * String gerDeathMilleniumBCAD =
	 * "Kategorie:Gestorben im (\\d{1,2}). Jahrtausend v. Chr. oder (\\d{1,2}). Jahrtausend"
	 * ; // Millenium not exactly defined (Range) String gerDeathMilleniumRAD =
	 * "Kategorie:Gestorben im (\\d{1,2}). oder (\\d{1,2}). Jahrtausend"; String
	 * gerDeathMilleniumRBC =
	 * "Kategorie:Gestorben im (\\d{1,2}). oder (\\d{1,2}). Jahrtausend v. Chr."
	 * ;
	 * 
	 * private Pattern r; private Matcher m;
	 * 
	 * public int matchBirth(String input) { try { if
	 * (input.matches(gerBirthAD)) { r = Pattern.compile(gerBirthAD); m =
	 * r.matcher(input); if (m.find()) { return Integer.parseInt(m.group(1));
	 * 
	 * } } else if (input.matches(gerBirthBC)) { r =
	 * Pattern.compile(gerBirthBC); m = r.matcher(input); if (m.find()) { return
	 * -1 * Integer.parseInt(m.group(1));
	 * 
	 * }
	 * 
	 * } else if (input.matches(gerBirthCenturyAD)) { r =
	 * Pattern.compile(gerBirthCenturyAD); m = r.matcher(input); if (m.find()) {
	 * return Integer.parseInt(m.group(1)) * 100 - 50;
	 * 
	 * } } else if (input.matches(gerBirthCenturyAD2)) { r =
	 * Pattern.compile(gerBirthCenturyAD2); m = r.matcher(input); if (m.find())
	 * { return Integer.parseInt(m.group(1)) * 100 - 50;
	 * 
	 * } } else if (input.matches(gerBirthCenturyBC)) { r =
	 * Pattern.compile(gerBirthCenturyBC); m = r.matcher(input); if (m.find()) {
	 * return -1 * Integer.parseInt(m.group(1)) * 100 + 50;
	 * 
	 * } } else if (input.matches(gerBirthCenturyBC2)) { r =
	 * Pattern.compile(gerBirthCenturyBC2); m = r.matcher(input); if (m.find())
	 * { return -1 * Integer.parseInt(m.group(1)) * 100 + 50;
	 * 
	 * }
	 * 
	 * } // TODO: Check if correct else if (input.matches(gerBirthCenturyBCAD))
	 * { // r = Pattern.compile(gerBirthCenturyBCAD); // m = r.matcher(input);
	 * // if (m.find()) { // return Integer.parseInt(m.group(1)); // // } return
	 * 0; } else if (input.matches(gerBirthCenturyRBC)) { r =
	 * Pattern.compile(gerBirthCenturyRBC); m = r.matcher(input); if (m.find())
	 * { return -1 * ((Integer.parseInt(m.group(1)) * 100 + 50) +
	 * (Integer.parseInt(m.group(2)) * 100 + 50)) / 2;
	 * 
	 * } }
	 * 
	 * else if (input.matches(gerBirthCenturyRAD)) { r =
	 * Pattern.compile(gerBirthCenturyRAD); m = r.matcher(input); if (m.find())
	 * { return ((Integer.parseInt(m.group(1)) * 100 - 50) +
	 * (Integer.parseInt(m.group(2)) * 100 - 50)) / 2;
	 * 
	 * } }
	 * 
	 * else if (input.matches(gerBirthMilleniumAD)) { r =
	 * Pattern.compile(gerBirthMilleniumAD); m = r.matcher(input); if (m.find())
	 * { return -1 * (Integer.parseInt(m.group(1))) * 1000;
	 * 
	 * } } else if (input.matches(gerBirthMilleniumBC)) { r =
	 * Pattern.compile(gerBirthMilleniumBC); m = r.matcher(input);
	 * 
	 * if (m.find()) { return -1 * Integer.parseInt(m.group(1)) * 1000;
	 * 
	 * } } else if (input.matches(gerBirthMilleniumBCAD)) { r =
	 * Pattern.compile(gerBirthMilleniumBCAD); m = r.matcher(input); if
	 * (m.find()) { return -1 * (Integer.parseInt(m.group(1)) +
	 * Integer.parseInt(m.group(2))) / 2 * 1000;
	 * 
	 * } } else if (input.matches(gerBirthMilleniumRAD)) { r =
	 * Pattern.compile(gerBirthMilleniumRAD); m = r.matcher(input); if
	 * (m.find()) { return (Integer.parseInt(m.group(1)) +
	 * Integer.parseInt(m.group(2))) / 2 * 1000;
	 * 
	 * } } else if (input.matches(gerBirthMilleniumRBC)) { r =
	 * Pattern.compile(gerBirthMilleniumRBC); m = r.matcher(input); if
	 * (m.find()) { return -1 * (Integer.parseInt(m.group(1)) +
	 * Integer.parseInt(m.group(2))) / 2 * 1000;
	 * 
	 * } } } catch (Exception e) { System.out.println(input);
	 * e.printStackTrace(); }
	 * 
	 * return -999;
	 * 
	 * }
	 * 
	 * public int matchDeath(String input) { try { if
	 * (input.matches(gerDeathAD)) { r = Pattern.compile(gerDeathAD); m =
	 * r.matcher(input); if (m.find()) { return Integer.parseInt(m.group(1));
	 * 
	 * } } else if (input.matches(gerDeathBC)) { r =
	 * Pattern.compile(gerDeathBC); m = r.matcher(input); if (m.find()) { return
	 * -1 * Integer.parseInt(m.group(1));
	 * 
	 * }
	 * 
	 * } else if (input.matches(gerDeathCenturyAD)) { r =
	 * Pattern.compile(gerDeathCenturyAD); m = r.matcher(input); if (m.find()) {
	 * return Integer.parseInt(m.group(1)) * 100 - 50;
	 * 
	 * } } else if (input.matches(gerDeathCenturyAD2)) { r =
	 * Pattern.compile(gerDeathCenturyAD2); m = r.matcher(input); if (m.find())
	 * { return Integer.parseInt(m.group(1)) * 100 - 50;
	 * 
	 * } } else if (input.matches(gerDeathCenturyBC)) { r =
	 * Pattern.compile(gerDeathCenturyBC); m = r.matcher(input); if (m.find()) {
	 * return -1 * Integer.parseInt(m.group(1)) * 100 + 50;
	 * 
	 * } } else if (input.matches(gerDeathCenturyBC2)) { r =
	 * Pattern.compile(gerDeathCenturyBC2); m = r.matcher(input); if (m.find())
	 * { return -1 * Integer.parseInt(m.group(1)) * 100 + 50;
	 * 
	 * }
	 * 
	 * } // TODO: Check if correct else if (input.matches(gerDeathCenturyBCAD))
	 * { // r = Pattern.compile(gerDeathCenturyBCAD); // m = r.matcher(input);
	 * // if (m.find()) { // return Integer.parseInt(m.group(1)); // // } return
	 * 0; } else if (input.matches(gerDeathCenturyRBC)) { r =
	 * Pattern.compile(gerDeathCenturyRBC); m = r.matcher(input); if (m.find())
	 * { return -1 * ((Integer.parseInt(m.group(1)) * 100 + 50) +
	 * (Integer.parseInt(m.group(2)) * 100 + 50)) / 2;
	 * 
	 * } }
	 * 
	 * else if (input.matches(gerDeathCenturyRAD)) { r =
	 * Pattern.compile(gerDeathCenturyRAD); m = r.matcher(input); if (m.find())
	 * { return ((Integer.parseInt(m.group(1)) * 100 - 50) +
	 * (Integer.parseInt(m.group(2)) * 100 - 50)) / 2;
	 * 
	 * } }
	 * 
	 * else if (input.matches(gerDeathMilleniumAD)) { r =
	 * Pattern.compile(gerDeathMilleniumAD); m = r.matcher(input); if (m.find())
	 * { return -1 * (Integer.parseInt(m.group(1))) * 1000;
	 * 
	 * } } else if (input.matches(gerDeathMilleniumBC)) { r =
	 * Pattern.compile(gerDeathMilleniumBC); m = r.matcher(input);
	 * 
	 * if (m.find()) { return -1 * Integer.parseInt(m.group(1)) * 1000;
	 * 
	 * } } else if (input.matches(gerDeathMilleniumBCAD)) { r =
	 * Pattern.compile(gerDeathMilleniumBCAD); m = r.matcher(input); if
	 * (m.find()) { return -1 * (Integer.parseInt(m.group(1)) +
	 * Integer.parseInt(m.group(2))) / 2 * 1000;
	 * 
	 * } } else if (input.matches(gerDeathMilleniumRAD)) { r =
	 * Pattern.compile(gerDeathMilleniumRAD); m = r.matcher(input); if
	 * (m.find()) { return (Integer.parseInt(m.group(1)) +
	 * Integer.parseInt(m.group(2))) / 2 * 1000;
	 * 
	 * } } else if (input.matches(gerDeathMilleniumRBC)) { r =
	 * Pattern.compile(gerDeathMilleniumRBC); m = r.matcher(input); if
	 * (m.find()) { return -1 * (Integer.parseInt(m.group(1)) +
	 * Integer.parseInt(m.group(2))) / 2 * 1000;
	 * 
	 * } } } catch (Exception e) { System.out.println(input);
	 * e.printStackTrace(); }
	 * 
	 * return -999; }
	 */

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
