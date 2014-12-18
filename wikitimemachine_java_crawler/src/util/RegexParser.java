package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser {

	public static void main(String[] args) {
		int Jahr = match("Kategorie:Geboren 1984");
		int Jahr2 = match("Kategorie:Geboren im 20. Jahrhundert");
		int Jahr3 = match("Kategorie:Geboren im 27. oder 26. Jahrhundert v. Chr.");

	}

	static String gerBirthAD = "Kategorie:Geboren (\\d{1,4})";
	static String gerBirthBC = "Kategorie:Geboren (\\d{1,4})";

	
	static String gerBirthCenturyAD = "Kategorie:Geboren im (\\d{1,2}). Jahrhundert";
	static String gerBirthCenturyAD2 = "Kategorie:Geboren \\((\\d{1,4}). Jahrhundert\\)";
	static String gerBirthCenturyBC = "Kategorie:Geboren im (\\d{1,2}). Jahrhundert v. Chr.";
//is actually not used as there are no entries for this regEx
	static String gerBirthCenturyBC2 =  "Kategorie:Geboren \\((\\d{1,4}). Jahrhundert\\) v. Chr.";

	static String gerBirthCenturyBCAD = "Kategorie:Geboren im (\\d{1,2}). Jahrhundert v. Chr. oder (\\d{1,2} Jahrhundert ";
	
	// Century not exactly defined (Range)
	static String gerBirthCenturyRBC = "Kategorie:Geboren im (\\d{1,2}). oder (\\d{1,2}). Jahrhundert v. Chr.";
	static String gerBirthCenturyRAD = "Kategorie:Geboren im (\\d{1,2}). oder (\\d{1,2}). Jahrhundert";
	
	static String gerBirthMilleniumAD = "Kategorie:Geboren im (\\d{1,2}). Jahrtausend";
	static String gerBirthMilleniumBC = "Kategorie:Geboren im (\\d{1,2}). Jahrtausend v. Chr.";
	
	static String gerBirthMilleniumBCAD = "Kategorie:Geboren im (\\d{1,2}). Jahrtausend v. Chr. oder (\\d{1,2} Jahrtausend ";
	// Millenium not exactly defined (Range)
	static String gerBirthMilleniumRAD = "Kategorie:Geboren im (\\d{1,2}). oder (\\d{1,2}). Jahrtausend";
	static String gerBirthMilleniumRBC = "Kategorie:Geboren im (\\d{1,2}). oder (\\d{1,2}). Jahrtausend v. Chr.";
	
//same regEx for death dates
	static String gerDeathAD = "Kategorie:Gestorben (\\d{1,4})";
	static String gerDeathBC = "Kategorie:Gestorben (\\d{1,4})";

	
	static String gerDeathCenturyAD = "Kategorie:Gestorben im (\\d{1,2}). Jahrhundert";
	static String gerDeathCenturyAD2 = "Kategorie:Gestorben \\((\\d{1,4}). Jahrhundert\\)";
	static String gerDeathCenturyBC = "Kategorie:Gestorben im (\\d{1,2}). Jahrhundert v. Chr.";
//is actually not used as there are no entries for this regEx
	static String gerDeathCenturyBC2 =  "Kategorie:Gestorben \\((\\d{1,4}). Jahrhundert\\) v. Chr.";

	static String gerDeathCenturyBCAD = "Kategorie:Gestorben im (\\d{1,2}). Jahrhundert v. Chr. oder (\\d{1,2} Jahrhundert ";
	
	// Century not exactly defined (Range)
	static String gerDeathCenturyRBC = "Kategorie:Gestorben im (\\d{1,2}). oder (\\d{1,2}). Jahrhundert v. Chr.";
	static String gerDeathCenturyRAD = "Kategorie:Gestorben im (\\d{1,2}). oder (\\d{1,2}). Jahrhundert";
	
	static String gerDeathMilleniumAD = "Kategorie:Gestorben im (\\d{1,2}). Jahrtausend";
	static String gerDeathMilleniumBC = "Kategorie:Gestorben im (\\d{1,2}). Jahrtausend v. Chr.";
	
	static String gerDeathMilleniumBCAD = "Kategorie:Gestorben im (\\d{1,2}). Jahrtausend v. Chr. oder (\\d{1,2} Jahrtausend ";
	// Millenium not exactly defined (Range)
	static String gerDeathMilleniumRAD = "Kategorie:Gestorben im (\\d{1,2}). oder (\\d{1,2}). Jahrtausend";
	static String gerDeathMilleniumRBC = "Kategorie:Gestorben im (\\d{1,2}). oder (\\d{1,2}). Jahrtausend v. Chr.";
		
	
	
	static String gerDeath1 = "Kategorie:Gestorben (\\d{1,4})/";
	static String gerDeath2 = "Kategorie:Gestorben im (\\d{1,2}). Jahrhundert";
	static String gerDeath3 = "Kategorie:Gestorben im (\\d{1,2}). oder (\\d{1,2}). Jahrtausend v. Chr.";
	

	public static int match(String input) {
		Pattern r;
		Matcher m;
		if (input.matches(gerBirthAD)) {
			r = Pattern.compile(gerBirthAD);
			m = r.matcher(input);
			if (m.find()) {
				return Integer.parseInt(m.group(1));

			}
		}
		else if (input.matches(gerBirthBC)) {
			r = Pattern.compile(gerBirthBC);
			m = r.matcher(input);
			if (m.find()) {
				return Integer.parseInt(m.group(1));

			}			




		} else if (input.matches(gerBirthCenturyAD)) {
			r = Pattern.compile(gerBirthCenturyAD);
			m = r.matcher(input);
			if (m.find()) {
				return Integer.parseInt(m.group(1)) * 100 - 50;

			}
		} 
		else if (input.matches(gerBirthCenturyAD2)) {
			r = Pattern.compile(gerBirthCenturyAD2);
			m = r.matcher(input);
			if (m.find()) {
				return Integer.parseInt(m.group(1));

			}			
		}
			else if (input.matches(gerBirthCenturyBC)) {
				r = Pattern.compile(gerBirthCenturyBC);
				m = r.matcher(input);
				if (m.find()) {
					return Integer.parseInt(m.group(1));

				}			
			}
				else if (input.matches(gerBirthCenturyBC2)) {
					r = Pattern.compile(gerBirthCenturyBC2);
					m = r.matcher(input);
					if (m.find()) {
						return Integer.parseInt(m.group(1));

					}	
				}
					else if (input.matches(gerBirthCenturyBCAD)) {
						r = Pattern.compile(gerBirthCenturyBCAD);
						m = r.matcher(input);
						if (m.find()) {
							return Integer.parseInt(m.group(1));

						}
					}
						else if (input.matches(gerBirthCenturyRBC)) {
							r = Pattern.compile(gerBirthCenturyRBC);
							m = r.matcher(input);
							if (m.find()) {
								return Integer.parseInt(m.group(1));

							}
						}
							else if (input.matches(gerBirthCenturyRAD)) {
								r = Pattern.compile(gerBirthCenturyRAD);
								m = r.matcher(input);
								if (m.find()) {
									return Integer.parseInt(m.group(1));

								}			
							}

		
		
		
		else if (input.matches(gerBirthMilleniumAD)) {
			r = Pattern.compile(gerBirthMilleniumAD);
			m = r.matcher(input);
			if (m.find()) {
				return -1 * (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2 * 1000;

			}
		} else if (input.matches(gerBirthMilleniumBC)) {
			r = Pattern.compile(gerBirthMilleniumBC);
			m = r.matcher(input);
			if (m.find()) {
				return -1 * (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2 * 1000;

			}
		}else if (input.matches(gerBirthMilleniumBCAD)) {
			r = Pattern.compile(gerBirthMilleniumBCAD);
			m = r.matcher(input);
			if (m.find()) {
				return -1 * (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2 * 1000;

			}
		}else if (input.matches(gerBirthMilleniumRAD)) {
			r = Pattern.compile(gerBirthMilleniumRAD);
			m = r.matcher(input);
			if (m.find()) {
				return -1 * (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2 * 1000;

			}
		}else if (input.matches(gerBirthMilleniumRBC)) {
			r = Pattern.compile(gerBirthMilleniumRBC);
			m = r.matcher(input);
			if (m.find()) {
				return -1 * (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2 * 1000;

			}
		}
		
		/*
		 * If needed for Death the same
		 * 
		 * if (input.matches(gerDeathAD)) {
			r = Pattern.compile(gerDeathAD);
			m = r.matcher(input);
			if (m.find()) {
				return Integer.parseInt(m.group(1));

			}
		}
		else if (input.matches(gerDeathBC)) {
			r = Pattern.compile(gerDeathBC);
			m = r.matcher(input);
			if (m.find()) {
				return Integer.parseInt(m.group(1));

			}			




		} else if (input.matches(gerDeathCenturyAD)) {
			r = Pattern.compile(gerDeathCenturyAD);
			m = r.matcher(input);
			if (m.find()) {
				return Integer.parseInt(m.group(1)) * 100 - 50;

			}
		} 
		else if (input.matches(gerDeathCenturyAD2)) {
			r = Pattern.compile(gerDeathCenturyAD2);
			m = r.matcher(input);
			if (m.find()) {
				return Integer.parseInt(m.group(1));

			}			
		}
			else if (input.matches(gerDeathCenturyBC)) {
				r = Pattern.compile(gerDeathCenturyBC);
				m = r.matcher(input);
				if (m.find()) {
					return Integer.parseInt(m.group(1));

				}			
			}
				else if (input.matches(gerDeathCenturyBC2)) {
					r = Pattern.compile(gerDeathCenturyBC2);
					m = r.matcher(input);
					if (m.find()) {
						return Integer.parseInt(m.group(1));

					}	
				}
					else if (input.matches(gerDeathCenturyBCAD)) {
						r = Pattern.compile(gerDeathCenturyBCAD);
						m = r.matcher(input);
						if (m.find()) {
							return Integer.parseInt(m.group(1));

						}
					}
						else if (input.matches(gerDeathCenturyRBC)) {
							r = Pattern.compile(gerDeathCenturyRBC);
							m = r.matcher(input);
							if (m.find()) {
								return Integer.parseInt(m.group(1));

							}
						}
							else if (input.matches(gerDeathCenturyRAD)) {
								r = Pattern.compile(gerDeathCenturyRAD);
								m = r.matcher(input);
								if (m.find()) {
									return Integer.parseInt(m.group(1));

								}			
							}

		
		
		
		else if (input.matches(gerDeathMilleniumAD)) {
			r = Pattern.compile(gerDeathMilleniumAD);
			m = r.matcher(input);
			if (m.find()) {
				return -1 * (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2 * 1000;

			}
		} else if (input.matches(gerDeathMilleniumBC)) {
			r = Pattern.compile(gerDeathMilleniumBC);
			m = r.matcher(input);
			if (m.find()) {
				return -1 * (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2 * 1000;

			}
		}else if (input.matches(gerDeathMilleniumBCAD)) {
			r = Pattern.compile(gerDeathMilleniumBCAD);
			m = r.matcher(input);
			if (m.find()) {
				return -1 * (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2 * 1000;

			}
		}else if (input.matches(gerDeathMilleniumRAD)) {
			r = Pattern.compile(gerDeathMilleniumRAD);
			m = r.matcher(input);
			if (m.find()) {
				return -1 * (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2 * 1000;

			}
		}else if (input.matches(gerDeathMilleniumRBC)) {
			r = Pattern.compile(gerDeathMilleniumRBC);
			m = r.matcher(input);
			if (m.find()) {
				return -1 * (Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2))) / 2 * 1000;

			}
		}
		 * 
		 * 
		 */

		return -99;
	}

}
