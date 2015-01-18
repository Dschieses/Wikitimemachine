package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLReader {
	
	 XMLInputFactory inputFactory;
	 InputStream in;
	//Default regEx-Filename
	 String FileName="regEx.xml";
	 XMLEventReader eventReader;
	 //names of the date categories used, by default as follow
	 String[] regExCategoryName ={"yearAD","yearBC","centuryAD","centuryBC","millenniumAD","millenniumBC","centuryRangeAD","centuryRangeBC","centuryRangeBCAD","millenniumRangeAD","millenniumRangeBC","millenniumRangeBCAD"};
	 //List, which contains all loaded regular expresisons
	 ArrayList<ArrayList<String>>regExCategory;
	 
	public XMLReader(String FileName, String[] regExCategoryNames){
		this.FileName=FileName;
		this.regExCategoryName=regExCategoryNames;
		inputFactory = XMLInputFactory.newInstance();
		try {
			in= new FileInputStream(FileName);
			eventReader = inputFactory.createXMLEventReader(in);
			regExCategory = new ArrayList<ArrayList<String>>();
			for(int i=0;i<regExCategoryName.length;i++){
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
	
	private boolean findLanguage(String language) {
		try {
			while (eventReader.hasNext()) {
				 XMLEvent event =  eventReader.nextEvent();
				 if (event.isStartElement()) {
			       		if(event.asStartElement().getName().toString().equals(language)){

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
	
	private boolean createRegExList(int listNumber){
		try{ 
			//read XML-events and add a regex from the event to the list
		while (eventReader.hasNext()) {
			 XMLEvent event = eventReader.nextEvent();
		        if (event.isStartElement()) {
		        	  		if(event.asStartElement().getName().toString().equals("item")) {
		        	  			event = eventReader.nextEvent();
		        	  			regExCategory.get(listNumber).add(event.asCharacters().getData());
		        	  		}
		        		}
		        else if(event.isEndElement() && event.asEndElement().getName().toString().equals(regExCategoryName[listNumber])){
		        	return true;
		        }
		 }
		}
		catch (Exception e) {
			 e.printStackTrace();
		}
		return false;
	}
	
 	public ArrayList<ArrayList<String>> readXML(String language) {

			try{
				//find the beginning of the right language		
		  if(findLanguage(language)){
			  //choose the right regEx Category
			  while (eventReader.hasNext()) {
			        XMLEvent event = eventReader.nextEvent();
			        if (event.isStartElement()) {
			        	for(int i=0;i<regExCategoryName.length;i++){
			        		if(event.asStartElement().getName().toString().equals(regExCategoryName[i]))
			        		{
			        			//put all corresponding regEx into the category 
			        			createRegExList(i);
			        		}
			        	}
			        }
			        //if the end of the corresponding language category is reached, end parsing
			        else if(event.isEndElement() && event.asEndElement().getName().toString().equals(language)){
			        	return regExCategory;
			        }
			  }
			  
			}
	  }
	  catch (Exception e) {
		  
	  }
 		 return regExCategory;
		 // return RegExCategory;
	  }
}
