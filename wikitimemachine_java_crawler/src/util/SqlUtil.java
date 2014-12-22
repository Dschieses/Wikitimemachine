package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import entity.Category;
import entity.Person;

public class SqlUtil {

	int i = 0;
	Connection c;
	private int listSplit = 50;
	private String personUpdate = "INSERT INTO pages (pageId,title,ns,lang) VALUES (?,?,?,?);";
	private String categoryUpdate = "INSERT INTO category (categoryTitle,lang) VALUES (?,?);";
	private String getCategory = "SELECT categoryId FROM category WHERE categoryTitle=?";
	private String personToCategory = "INSERT INTO pagetocategory VALUES (?,?,?)";
	private String lastInserted = "SELECT LAST_INSERT_ID()";
	private String linkUpdate = "INSERT INTO connection (fromPageId,toPageId,lang) VALUES (?,?,?)";
	private String selectAllCategories = "SELECT * FROM category";
	private String selectCategoryPeople = "SELECT pageId FROM pagetocategory WHERE categoryId=?";
	private String updateIndegree = "UPDATE pages a SET indegree = (SELECT COUNT(*) AS Anzahl FROM `connection` WHERE toPageId=a.pageid)";
	private String updateOutdegree = "UPDATE pages a SET outdegree = (SELECT COUNT(*) AS Anzahl FROM `connection` WHERE fromPageId=a.pageid)";
	private String updateDate = "UPDATE pages SET ?=? WHERE pageId IN (?);";

	public void storePersons(List<Person> pList) throws SQLException, ClassNotFoundException {
		if (pList == null) {
			return;
		}
		for (List<Person> list : CommonFunctions.split(pList, listSplit)) {
			// storePages(list, "DE");
			storeCategories(list, "DE");
			// storeConnections(list, "DE");
		}
	}

	public void determineDates() {
		DbConnector db = new DbConnector();
		Connection c = null;
		try {
			c = db.getDbConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			ResultSet r = db.executeQuery(c, selectAllCategories);
			while (r.next()) {
				String category = r.getString("categoryTitle");
				int catId = r.getInt("categoryId");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

					e1.printStackTrace();
				}

				for (Person p : pList) {
					// TODO muss wieder weg
					// LinkListe löschen
					p.setLinkList(null);
					if (p.getCategoryList() == null) {
						continue;
					}
					for (Category cat : p.getCategoryList()) {
						String title = cat.getTitle();

						try {
							ResultSet r = db.executeQuery(c, getCategory, Arrays.asList(title));
							if (r.first()) {
								id = r.getString("categoryId");
							} else {
								db.executeUpdate(c, categoryUpdate, Arrays.asList(title, lang));

								r = db.executeQuery(c, lastInserted, null);
								if (r.next()) {
									id = r.getString("LAST_INSERT_ID()");

								}
							}
							db.close(r);
							// db.close();

						} catch (SQLException e) {
							// If Cause of the error is an already entered
							// Category. Set the link to page correctly
							if (e.getMessage().startsWith("Duplicate entry")) {
								try {
									ResultSet r = db.executeQuery(c, getCategory, Arrays.asList(title));
									if (r.first()) {
										id = r.getString("categoryId");
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
					p.setCategoryList(null);

				}
				try {
					db.close();
					db.close(c);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

	private void storeConnections(final List<Person> pList, final String lang) {
		new Thread() {

			@Override
			public void run() {
				for (Person p : pList) {
					DbConnector db = new DbConnector();
					Connection c = null;
					try {
						c = db.getDbConnection();
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						// e1.printStackTrace();
					}

					if (p.getLinkList() == null) {
						continue;
					}
					for (Person linkPerson : p.getLinkList()) {
						try {
							db.executeUpdate(c, linkUpdate, Arrays.asList(String.valueOf(p.getPageid()),
									String.valueOf(linkPerson.getPageid()), lang));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						}
					}

					try {
						db.close();
						db.close(c);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						// e1.printStackTrace();
					}
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

				for (Person p : pList) {
					try {
						db.executeUpdate(c, personUpdate, Arrays.asList(String.valueOf(p.getPageid()), p.getTitle(),
								String.valueOf(p.getNs()), lang));

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				try {
					db.close();
					db.close(c);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

}
