package util;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import entity.Category;
import entity.Page;

public class ApiCaller {

	private Connection c;
	private PreparedStatement stmt;
	private FileWriter fw;

	public ApiCaller() {

	}

	public void start(String startUrl) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		String uhrzeit = sdf.format(new Date());
		System.out.println(uhrzeit);
		// String startString = sendGet(startUrl);
		// JSONObject startJson = getJSON(startString);
		// getApContinue(startJson);
		// String line = "";
		// // fw = new FileWriter("C:/Users/Peter/Desktop/people.csv");
		// do {
		// // List<Page> list = getPageInfo(startJson);
		// //
		// // for (Page page : list) {
		// // // getCategories(page);
		// // // Prüfe ob kategorie zu Person gehört
		// // line = page.getPageid() + ";" + page.getTitle() + ";"
		// // + page.getNs();
		// // fw.write(line + "\n");
		// // fw.flush();
		// //
		// // }
		//
		// startString = sendGet(startUrl + getEncoded(apcontinue));
		// startJson = getJSON(startString);
		//
		// } while (getApContinue(startJson));
		//
		// sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		// uhrzeit = sdf.format(new Date());
		// System.out.println(uhrzeit);
		// System.out.println(apContinueList.size());
		// fw = new FileWriter("C:/Users/Peter/Desktop/apcontinue.csv");
		// for (String string : apContinueList) {
		// fw.write(string + "\n");
		// fw.flush();
		// }
		CategoryApi ca = new CategoryApi();
		Category c = new Category();
		
		
		LinkApi la = new LinkApi();
		boolean isPeople=false;
		// String line = "";
		// fw = new FileWriter("C:/Users/Peter/Desktop/people_mann.csv");
		// do {
	
//		c.setTitle("Mann");
//		List<Page> listMale = ca.getCategoryMembers(c);
		c.setTitle("Bundeskanzler (Deutschland)");
		List<Page> listFemale = ca.getCategoryMembers(c);
		
		List<Page> personList = new ArrayList<Page>();
//		personList.addAll(listMale);
		personList.addAll(listFemale);
		
		for (Iterator<Page> iterator = personList.iterator(); iterator.hasNext();) {
		    Page p = iterator.next();
		    List<Page> linkList = la.getOutgoingLinks(p);
		    //Iteriere über alle Links die ausgehen
		    for (Iterator<Page> linkIterator = linkList.iterator(); linkIterator.hasNext();) {
		    	Page link = linkIterator.next();
		    	//Iteriere über alle Personen die wir gefunden haben
		    	for (Iterator<Page> personIterator = personList.iterator(); personIterator.hasNext();) {
			    	Page person = personIterator.next();
			    if(person.getPageid()==link.getPageid())
			    {//Link entspricht einer Person die wir schon kennen
			    	isPeople=true;
			    	break;
			    }
		    	}
		    	if(isPeople)
		    	{		    		
		    		isPeople=false;
		    	}
		    	else{
		    		linkIterator.remove();
		    	}
		    }
		    p.setLinkList(linkList);
		}

		// for (Page page : list) {
		//
		// line = page.getPageid() + ";" + page.getTitle() + ";"
		// + page.getNs();
		// fw.write(line + "\n");
		// fw.flush();
		//
		// }
		//
		// } while (ca.getCmContinue());
		sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		uhrzeit = sdf.format(new Date());
		System.out.println(uhrzeit);

	}

}
