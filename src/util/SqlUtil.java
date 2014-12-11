package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import entity.Category;
import entity.Person;

public class SqlUtil {

	int i = 0;
	Connection c;
	private int listSplit = 20;
	private String personUpdate = "INSERT INTO pages (pageId,title,ns,lang) VALUES (?,?,?,?);";
	private String categoryUpdate = "INSERT INTO category (categoryTitle,lang) VALUES (?,?);";
	private String getCategory = "SELECT id FROM category WHERE categoryTitle=?";
	private String personToCategory = "INSERT INTO pagetocategory VALUES (?,?,?)";
	private String lastInserted = "SELECT LAST_INSERT_ID()";
	private String linkUpdate = "INSERT INTO connection (fromPageId,toPageId,lang) VALUES (?,?,?)";

	public void storePersons(List<Person> pList) throws SQLException, ClassNotFoundException {
		if (pList == null) {
			return;
		}
		for (Iterator<List<Person>> iterator = CommonFunctions.split(pList, listSplit).iterator(); iterator.hasNext();) {
			List<Person> list = iterator.next();
			storePages(list, "DE");
			storeCategories(list, "DE");
			storeLinks(list, "DE");
		}
	}

	private void storeCategories(final List<Person> pList, final String lang) {
		new Thread() {

			private String id;

			@Override
			public void run() {
				DbConnector db = new DbConnector();
				Connection c = null;
				try {
					c = db.getDbConnection();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				for (Iterator<Person> iterator = pList.iterator(); iterator.hasNext();) {
					Person p = iterator.next();
					if (p.getCategoryList() == null) {
						continue;
					}
					for (Iterator<Category> catIterator = p.getCategoryList().iterator(); catIterator.hasNext();) {
						String title = catIterator.next().getTitle();

						try {
							ResultSet r = db.executeQuery(c, getCategory, Arrays.asList(title));
							if (r.first()) {
								id = r.getString("id");
							} else {
								db.executeUpdate(c, categoryUpdate, Arrays.asList(title, lang));

								r = db.executeQuery(c, lastInserted, null);
								if (r.next()) {
									id = r.getString("LAST_INSERT_ID()");

								}
							}
							// db.close();

						} catch (SQLException e) {
							// If Cause of the error is an already entered
							// Category. Set the link to page correctly
							if (e.getMessage().startsWith("Duplicate entry")) {
								try {
									ResultSet r = db.executeQuery(c, getCategory, Arrays.asList(title));
									if (r.first()) {
										id = r.getString("id");
									}
									db.close(r);
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

							} else {
								e.printStackTrace();
							}

						}
						try {
							db.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							db.executeUpdate(c, personToCategory,
									Arrays.asList(String.valueOf(p.getPageid()), lang, String.valueOf(id)));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				try {
					db.close();
					c.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

	private void storeLinks(final List<Person> pList, final String lang) {
		new Thread() {

			@Override
			public void run() {

				DbConnector db = new DbConnector();
				Connection c = null;
				try {
					c = db.getDbConnection();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				for (Iterator<Person> iterator = pList.iterator(); iterator.hasNext();) {
					Person p = iterator.next();
					if (p.getLinkList() == null) {
						continue;
					}
					for (Iterator<Person> linkIterator = p.getLinkList().iterator(); linkIterator.hasNext();) {
						String id = String.valueOf(linkIterator.next().getPageid());

						try {
							db.executeUpdate(c, linkUpdate, Arrays.asList(String.valueOf(p.getPageid()), id, lang));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				try {
					db.close();
					c.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}.start();

	}

	private void storePages(final List<Person> pList, final String lang) {
		new Thread() {
			@Override
			public void run() {
				DbConnector db = new DbConnector();
				Connection c = null;
				try {
					c = db.getDbConnection();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				for (Iterator<Person> iterator = pList.iterator(); iterator.hasNext();) {
					Person p = iterator.next();
					int pageId = p.getPageid();
					String title = p.getTitle();
					int ns = p.getNs();
					try {
						db.executeUpdate(c, personUpdate,
								Arrays.asList(String.valueOf(pageId), title, String.valueOf(ns), lang));

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				try {
					db.close();
					c.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

}
