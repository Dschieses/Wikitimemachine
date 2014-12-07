package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

import entity.Person;

public class IO {
	
	
	public List<Person> readFromJsonFile() throws FileNotFoundException
	{
		Scanner testScanner = new Scanner(new BufferedReader(new FileReader("C:\\people_frau_link.csv")));	   
	    Gson g = new Gson();	   
	    List<Person> pageList = new ArrayList<>();
	     while (testScanner.hasNext())
	     {
	    	 pageList.add(g.fromJson(testScanner.nextLine(), Person.class));
	     }
	    testScanner.close();
		return pageList;
	}

}
