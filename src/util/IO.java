package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

import entity.Person;

public class IO {
	private FileWriter fw;

	public List<Person> readFromJsonFile(String path) throws FileNotFoundException {
		Scanner testScanner = new Scanner(new BufferedReader(new FileReader(
				path)));
		Gson g = new Gson();
		List<Person> pageList = new ArrayList<>();
		while (testScanner.hasNext()) {
			pageList.add(g.fromJson(testScanner.nextLine(), Person.class));
		}
		testScanner.close();
		return pageList;
	}

	public void writeToJsonFile(List<Person> personList,String path) throws IOException {
		Gson g = new Gson();
		fw = new FileWriter(path);

		for (Iterator<Person> iterator = personList.iterator(); iterator
				.hasNext();) {			
			fw.write(g.toJson(iterator.next()) + "\n");
			fw.flush();			
		}		
	}

}
