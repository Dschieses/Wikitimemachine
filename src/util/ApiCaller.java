/*
 * Created Oct 2014 - Feb 2015 during COINS
 * by Peter Praeder, Michael Koetting, Vladimir Trajt
 */
package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;

import entity.Category;
import entity.Person;


/**
 * The Class ApiCaller.
 */
public class ApiCaller {

	/** The person list. */
	List<Person> personList;

	/** The category list. */
	List<Category> categoryList = new ArrayList<>();
	
	/** The category finished list. */
	List<Boolean> categoryFinishedList = new ArrayList<>();
	
	/** The is people. */
	boolean isPeople = false;
	
	/** The clean links finished. */
	boolean cleanLinksFinished = false;
	
	/** The g. */
	Gson g = new Gson();
	
	/** The path. */
	private String path;

	/**
	 * Instantiates a new api caller.
	 *
	 * @param path the path
	 * @param categoryTitleList the category title list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ApiCaller(String path, List<String> categoryTitleList)
			throws IOException {
		this.path = path;

		for (Iterator<String> iterator = categoryTitleList.iterator(); iterator
				.hasNext();) {
			String title = iterator.next();
			Category c = new Category(title);
			categoryList.add(c);
		}
	}

	/**
	 * Start.
	 *
	 * @throws Exception the exception
	 */
	public void start() throws Exception {
		CommonFunctions.printCurrentTimestamp();
		

		personList = new ArrayList<Person>();

		for (Iterator<Category> iterator = categoryList.iterator(); iterator
				.hasNext();) {
			final Category c = iterator.next();
			new Thread() {
				@Override
				public void run() {
					CategoryApi ca = new CategoryApi();
					try {
						personList.addAll(ca.getCategoryMembers(c));
						categoryFinishedList.add(true);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					System.out.println("Category '" + c.getTitle() + "' done");
					CommonFunctions.printCurrentTimestamp();
				}
			}.start();
			
		}
		waitForLinks();
		
		// Läuft asynchron
		getCategories();
		getLinks();
		cleanLinks();

		while (!cleanLinksFinished) {
			Thread.sleep(1);
		}
		IO io = new IO();
		io.writeToJsonFile(personList, path);

	}	
	
	/**
	 *This function sets the main process to sleep while the initial fetching of categorymembers hasn't finished.
	 */
	private void waitForLinks()
	{
		while(categoryFinishedList.size()!=categoryList.size())
		{
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("Wait for links error:");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets the categories.
	 *
	 * @return the categories
	 */
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

	/**
	 * Gets the categories.
	 *
	 * @param pl the pl
	 * @param ca the ca
	 * @return the categories
	 */
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
						
						e1.printStackTrace();
					}
				}

			}
		}.start();
	}

	/**
	 * Gets the links.
	 *
	 * @param pl the pl
	 * @param la the la
	 * @return the links
	 */
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
						
						e1.printStackTrace();
					}
				}

			}
		}.start();
	}

	/**
	 * Gets the links.
	 *
	 * @return the links
	 */
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

	/**
	 * Clean links.
	 */
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
							
							e.printStackTrace();
						}
					}
					// Iteriere über alle Links die ausgehen
					for (Iterator<Person> linkIterator = p.getLinkList()
							.iterator(); linkIterator.hasNext();) {
						Person link = linkIterator.next();
						// Iteriere über alle Personen die wir gefunden haben
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
						if (isPeople) { // Verknüpfe Personen
							isPeople = false;
						} else {
							linkIterator.remove();
						}
					}
				}
				CommonFunctions.printCurrentTimestamp();
				cleanLinksFinished = true;
				
			}
		}.start();
	}

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 *
	 * @param path the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
