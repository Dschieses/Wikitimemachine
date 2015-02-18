/*
 * Created Oct 2014 - Feb 2015 during COINS
 * by Peter Praeder, Michael Koetting, Vladimir Trajt
 */
package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * 
 * The class contains methods and attributes required to read an XML file, which contains regular expressions needed for parsing dates.
 *
 */
public class XMLReader {

	XMLInputFactory inputFactory;
	InputStream in;
	/**
	 *  Default regEx-Filename.
	 */
	String FileName = "regEx.xml";
	XMLEventReader eventReader;
	/**
	 *  names of the date categories used. They are hard coded as they cover all actually used date categories in Wikipedia. 
	 */
	String[] regExCategoryName = { "yearAD", "yearBC", "centuryAD", "centuryBC", "millenniumAD", "millenniumBC",
			"centuryRangeAD", "centuryRangeBC", "centuryRangeBCAD", "millenniumRangeAD", "millenniumRangeBC",
			"millenniumRangeBCAD"};
	/**
	 *  List, which contains all regular expresisons loaded from a specified XML file and language.
	 */
	ArrayList<ArrayList<String>> regExCategory;
/**
 * 
 * The constructor of an XML reader object. The object can then be used to load regular expressions in memory.
 * @param FileName The XML filename of the file with regular expressions.
 * @param regExCategoryNames the categories of regular expressions which are used in an XML file.
 */
	public XMLReader(String FileName, String[] regExCategoryNames) {
		this.FileName = FileName;
		this.regExCategoryName = regExCategoryNames;
		inputFactory = XMLInputFactory.newInstance();
		try {
			in = new FileInputStream(FileName);
			eventReader = inputFactory.createXMLEventReader(in);
			regExCategory = new ArrayList<ArrayList<String>>();
			//for every category name add a dummy arraylist
			for (String element : regExCategoryName) {
				regExCategory.add(new ArrayList<String>());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * The method loads regular expressions for a specific category from an XML file to memory. 
 * @param listNumber the index position of a specific category of regular expressions in the "regExCategoryName" array.
 * @return a set of regular expressions for a specific category.
 */
	private boolean createRegExList(int listNumber) {
		try {
			// read XML-events and add a regex from the event to the list
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					if (event.asStartElement().getName().toString().equals("item")) {
						event = eventReader.nextEvent();
						regExCategory.get(listNumber).add(event.asCharacters().getData());
					}
				} else if (event.isEndElement()
						&& event.asEndElement().getName().toString().equals(regExCategoryName[listNumber])) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * The method reads through a XML file and stops at the line, where the defined language begins.
	 * @param language  a language name under which language specific regular expressions are stored.
	 * @return true if the defined language is found or false if the defined language is not found in the XML file.
	 */
	private boolean findLanguage(String language) {
		try {
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					if (event.asStartElement().getName().toString().equals(language)) {

						return true;
					}
				}
			}
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * The method creates a list of categories each containing a set of regular expressions. With this a date from a web page, like birth date or death date. 
	 * can be parsed, which occurs in a different class.
	 * @param language a language name under which language specific regular expressions are stored.
	 * @return an array of categories of regular expressions. Each category represents a certain date/date range like just century or century range. 
	 * Each item of the returned array list is a list of regular expressions, which apply to a certain category.
	 */
	public ArrayList<ArrayList<String>> readXML(String language) {

		try {
			// find the beginning of the right language
			if (findLanguage(language)) {
				// choose the right regEx Category
				while (eventReader.hasNext()) {
					XMLEvent event = eventReader.nextEvent();
					if (event.isStartElement()) {
						for (int i = 0; i < regExCategoryName.length; i++) {
							if (event.asStartElement().getName().toString().equals(regExCategoryName[i])) {
								// put all corresponding regEx into the category
								createRegExList(i);
							}
						}
					}
					// if the end of the corresponding language category is
					// reached, end parsing
					else if (event.isEndElement() && event.asEndElement().getName().toString().equals(language)) {
						return regExCategory;
					}
				}

			}
		} catch (Exception e) {

		}
		return regExCategory;
	
	}
}
