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

	/** The clean links finished. */
	List<Boolean> cleanLinksFinished = new ArrayList<>();

	/** The g. */
	Gson g = new Gson();

	/** The path. */
	private String path;

	protected boolean watchDogFinished;

	private int splitListSize=0;

	/**
	 * Instantiates a new api caller.
	 *
	 * @param path
	 *            the path
	 * @param categoryTitleList
	 *            the category title list
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
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
	 * @throws Exception
	 *             the exception
	 */
	public void start() throws Exception {
		startWatchDog();
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
		waitForCategoryMembers();
		

		// runs async
		getCategoriesLinksAndClean(10);

		waitForCleanedLinks();

		IO io = new IO();
		io.writeToJsonFile(personList, path);
		watchDogFinished = true;
		System.out.println("Finally done");
		CommonFunctions.printCurrentTimestamp();

	}
	public void startWatchDog() {
		new Thread() {
			public void run() {
				while (!watchDogFinished) {
//					for (Iterator<Person> iterator = personList.iterator(); iterator
//							.hasNext();) {
//						Person p = iterator.next();
//						if (p.getCategoryList() == null||p.getLinkList() == null) {
//							break;
//						}						
//						if (!iterator.hasNext()) {
//							watchDogFinished = true;
//						}
//					}		
					System.out.println("WatchDog: Still Alive");
					CommonFunctions.printCurrentTimestamp();
					try {
						Thread.sleep(300*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * This function sets the main process to sleep while the initial fetching
	 * of categorymembers hasn't finished.
	 */
	private void waitForCategoryMembers() {
		while (categoryFinishedList.size() != categoryList.size()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("Wait for category members error:");
				e.printStackTrace();
			}
		}
	}

	private void waitForCleanedLinks() {
		while (cleanLinksFinished.size() != splitListSize) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("Wait for cleaned links error:");
				e.printStackTrace();
			}
		}
	}

	public void getCategoriesLinksAndClean(int listSplit) {
		for (Iterator<List<Person>> iterator = CommonFunctions.split(
				personList, listSplit).iterator(); iterator.hasNext();) {
			splitListSize++;
			List<Person> list = iterator.next();
			getCategories(list, new CategoryApi());
			getLinks(list, new LinkApi());
			cleanLinks(list);
		}
	}

	/**
	 * Gets the categories.
	 *
	 * @param pl
	 *            Part of the whole personList
	 * @param ca
	 *            A new CategoryApi for every task
	 * @return All categories to which a person belong
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
	 * @param pl
	 *            Part of the whole personList
	 * @param la
	 *            A new LinkApi for every task
	 * @return Outgoing links to each person
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
	 * This function compare the outgoing links to all known persons. If link is
	 * not "known" reference is deleted
	 */
	public void cleanLinks(final List<Person> pl) {
		new Thread() {

			@Override
			public void run() {
				for (Iterator<Person> iterator = pl.iterator(); iterator
						.hasNext();) {
					Person p = iterator.next();
					while (p.getLinkList() == null) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// Iterate over all links for this page
					for (Iterator<Person> linkIterator = p.getLinkList()
							.iterator(); linkIterator.hasNext();) {
						Person link = linkIterator.next();
						boolean isPeople = false;
						// Check if link is in our person list
						for (Iterator<Person> personIterator = personList
								.iterator(); personIterator.hasNext();) {
							Person person = personIterator.next();
							// Link is a Person we know
							if (person.getTitle().equals(link.getTitle())) {
								isPeople = true;
								link.setPageid(person.getPageid());
								break;
							}
						}
						if (isPeople) {
							// TODO: Connect People
							isPeople = false;
						} else {
							linkIterator.remove();
						}
					}
				}

				cleanLinksFinished.add(true);

			}
		}.start();
	}
}
