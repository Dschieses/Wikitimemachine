package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;

import entity.Category;
import entity.Person;

public class ApiCaller {

	List<Person> personList;
	CategoryApi ca = new CategoryApi();

	Category category = new Category();
	boolean isPeople = false;
	Gson g = new Gson();

	public ApiCaller() throws IOException {

	}

	public void start() throws Exception {
		CommonFunctions.printCurrentTimestamp();
		
		category.setTitle("Bundeskanzler_(Deutschland)");
		List<Person> list = ca.getCategoryMembers(category);
		// todo: async
		// dupletten evtl entfernen
		personList = new ArrayList<Person>();
		// personList.addAll(listMale);
		personList.addAll(list);

		// L�uft asynchron
		getCategories();
		getLinks();
		cleanLinks();
	}

	public void getCategories() {
		final List<Person> personList1 = personList.subList(0,
				personList.size() / 4);
		final List<Person> personList2 = personList.subList(
				personList.size() / 4, personList.size() / 3);
		final List<Person> personList3 = personList.subList(
				personList.size() / 3, personList.size() / 2);
		final List<Person> personList4 = personList.subList(
				personList.size() / 2, personList.size());
		final CategoryApi ca1 = new CategoryApi();
		final CategoryApi ca2 = new CategoryApi();
		final CategoryApi ca3 = new CategoryApi();
		final CategoryApi ca4 = new CategoryApi();

		getCategories(personList1, ca1);
		getCategories(personList2, ca2);
		getCategories(personList3, ca3);
		getCategories(personList4, ca4);

	}

	public void getCategories(final List<Person> pl, final CategoryApi ca) {
		new Thread() {
			@Override
			public void run() {
				for (Iterator<Person> iterator = pl.iterator(); iterator
						.hasNext();) {
					Person p = iterator.next();
					try {
						ca.getCategories(p);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		}.start();
	}

	public void getLinks(final List<Person> pl, final LinkApi la) {
		new Thread() {
			@Override
			public void run() {
				for (Iterator<Person> iterator = pl.iterator(); iterator
						.hasNext();) {
					Person p = iterator.next();
					try {
						la.getOutgoingLinks(p);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		}.start();
	}

	public void getLinks() {
		final List<Person> personList1 = personList.subList(0,
				personList.size() / 4);
		final List<Person> personList2 = personList.subList(
				personList.size() / 4, personList.size() / 3);
		final List<Person> personList3 = personList.subList(
				personList.size() / 3, personList.size() / 2);
		final List<Person> personList4 = personList.subList(
				personList.size() / 2, personList.size());
		final LinkApi la1 = new LinkApi();
		final LinkApi la2 = new LinkApi();
		final LinkApi la3 = new LinkApi();
		final LinkApi la4 = new LinkApi();

		getLinks(personList1, la1);
		getLinks(personList2, la2);
		getLinks(personList3, la3);
		getLinks(personList4, la4);

	}

	public void cleanLinks() {
		new Thread() {
			@Override
			public void run() {
				for (Iterator<Person> iterator = personList.iterator(); iterator
						.hasNext();) {
					Person p = iterator.next();
					while (p.getLinkList() == null) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					// Iteriere �ber alle Links die ausgehen
					for (Iterator<Person> linkIterator = p.getLinkList()
							.iterator(); linkIterator.hasNext();) {
						Person link = linkIterator.next();
						// Iteriere �ber alle Personen die wir gefunden haben
						for (Iterator<Person> personIterator = personList
								.iterator(); personIterator.hasNext();) {
							Person person = personIterator.next();
							if (person.getTitle().equals(link.getTitle())) {// Link
																			// entspricht
																			// einer
																			// Person
																			// die
																			// wir
																			// schon
																			// kennen
								isPeople = true;
								link.setPageid(person.getPageid());
								break;
							}
						}
						if (isPeople) { // Verkn�pfe Personen
							isPeople = false;
						} else {
							linkIterator.remove();
						}
					}
				}
				CommonFunctions.printCurrentTimestamp();
			}
		}.start();
	}

}
