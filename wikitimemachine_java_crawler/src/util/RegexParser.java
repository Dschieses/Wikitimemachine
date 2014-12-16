package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser {

	public static void main(String[] args) {
		int Jahr = match("Kategorie:Geboren 1984");
		int Jahr2 = match("Kategorie:Geboren im 20. Jahrhundert");
		int Jahr3 = match("Kategorie:Geboren im 27. oder 26. Jahrhundert v. Chr.");

	}

	static String gerBirth = "Kategorie:Geboren (\\d{1,4})";

	static String gerBirthCentury = "Kategorie:Geboren im (\\d{1,2}). Jahrhundert";
	static String gerBirthCentury2 = "Kategorie:Geboren \\((\\d{1,4}). Jahrhundert\\)";
	static String gerBirthCenturyBCAD = "Kategorie:Geboren im (\\d{1,2}). Jahrhundert v. Chr. oder (\\d{1,2} Jahrhundert ";
	static String gerBirthCenturyBC = "Kategorie:Geboren im (\\d{1,2}). Jahrhundert v. Chr.";
	// Century UnKnown
	static String gerBirthCenturyUK = "Kategorie:Geboren im (\\d{1,2}). oder (\\d{1,2}). Jahrhundert v. Chr.";

	static String gerBirthMilleniumBCAD = "Kategorie:Geboren im (\\d{1,2}). Jahrtausend v. Chr. oder (\\d{1,2} Jahrtausend ";
	// Millenium UnKnown
	static String gerBirthMilleniumUK = "Kategorie:Geboren im (\\d{1,2}). oder (\\d{1,2}). Jahrtausend v. Chr.";
	static String gerBirthMilleniumBC = "Kategorie:Geboren im (\\d{1,2}). Jahrtausend v. Chr.";
	static String gerBirthMillenium = "Kategorie:Geboren im (\\d{1,2}). Jahrtausend";

	static String gerDeath1 = "Kategorie:Gestorben (\\d{1,4})/";
	static String gerDeath2 = "Kategorie:Gestorben im (\\d{1,2}). Jahrhundert";
	static String gerDeath3 = "Kategorie:Gestorben im (\\d{1,2}). oder (\\d{1,2}). Jahrtausend v. Chr.";

	public static int match(String input) {
		Pattern r;
		Matcher m;
		if (input.matches(gerBirth)) {
			r = Pattern.compile(gerBirth);
			m = r.matcher(input);
			if (m.find()) {
				return Integer.parseInt(m.group(1));

			}

		} else if (input.matches(gerBirthCentury)) {
			r = Pattern.compile(gerBirthCentury);
			m = r.matcher(input);
			if (m.find()) {
				return Integer.parseInt(m.group(1)) * 100 - 50;

			}
		} else if (input.matches(gerBirthMillenium)) {
			r = Pattern.compile(gerBirthMillenium);
			m = r.matcher(input);
			if (m.find()) {
				return -1 * (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2 * 1000;

			}
		} else if (input.matches(gerBirthCenturyUK)) {
			r = Pattern.compile(gerBirthCenturyUK);
			m = r.matcher(input);
			if (m.find()) {
				return -1 * (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2 * 100;

			}
		}

		return -99;
	}

}
