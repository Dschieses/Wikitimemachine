package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import entity.Category;
import entity.Person;

public class SqlUtil {

	int i = 0;
	private int listSplit = 25;
	private String personUpdate = "INSERT INTO pages (pageId,title,ns,lang) VALUES (?,?,?,?);";
	private String categoryUpdate = "INSERT INTO category (categoryTitle,lang) VALUES (?,?);";
	private String getCategory = "SELECT categoryId FROM category WHERE categoryTitle=?";
	private String personToCategory = "INSERT INTO pagetocategory (pageId,lang,categoryId) VALUES (?,?,?)";
	private String lastInserted = "SELECT LAST_INSERT_ID()";
	private String linkUpdate = "INSERT INTO connection (fromPageId,toPageId,lang) VALUES (?,?,?)";
	private String selectAllCategories = "SELECT DISTINCT categoryTitle,categoryId FROM category WHERE categoryTitle  LIKE '%geboren%' OR categoryTitle  LIKE '%gestorben%'";
	private String updateIndegree = "SELECT COUNT(*) as indegree,toPageId FROM `connection` GROUP BY toPageId";
	private String updateOutdegree = "SELECT COUNT(*) as outdegree,fromPageId FROM `connection` GROUP BY fromPageId";
	private int maxThreads = 15;
	private ResultSet r;

	public void storePersons(List<Person> pList) throws SQLException, ClassNotFoundException {
		if (pList == null) {
			return;
		}
		for (List<Person> list : CommonFunctions.split(pList, listSplit)) {
			storePages(list, "DE");
			storeCategories(list, "DE");
			storeConnections(list, "DE");
		}
	}

	public void determineDates(final String lang) throws SQLException, ClassNotFoundException {
		DbConnector db = new DbConnector();

		r = db.executeQuery(selectAllCategories);
		RegexParser rp = new RegexParser();
		final String birthQuery = "UPDATE pages SET birthDate=? WHERE lang=? AND pageid IN (SELECT pageId FROM pagetocategory WHERE lang=? AND categoryId=?)";
		final String deathQuery = "UPDATE pages SET deathDate=? WHERE lang=? AND pageid IN (SELECT pageId FROM pagetocategory WHERE lang=? AND categoryId=?)";

		while (r.next()) {

			// String category = r.getString("categoryTitle");
			// final int catId = r.getInt("categoryId");
			// final int yearBirth = rp.matchBirth(category);
			// final int yearDeath = rp.matchDeath(category);
			// if (yearBirth != -999) {
			// while (Thread.activeCount() > maxThreads) {
			// try {
			// Thread.sleep(1);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			// new Thread() {
			// @Override
			// public void run() {
			// DbConnector db = null;
			// try {
			// db = new DbConnector();
			// } catch (ClassNotFoundException | SQLException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// try {
			// db.executeUpdate(birthQuery,
			// Arrays.asList(String.valueOf(yearBirth), lang, lang,
			// String.valueOf(catId)));
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// try {
			// db.close();
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			// }.start();
			// } else if (yearDeath != -999) {
			// while (Thread.activeCount() > maxThreads) {
			// try {
			// Thread.sleep(1);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			// new Thread() {
			// @Override
			// public void run() {
			// DbConnector db = null;
			// try {
			// db = new DbConnector();
			// } catch (ClassNotFoundException | SQLException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// try {
			// db.executeUpdate(deathQuery,
			// Arrays.asList(String.valueOf(yearDeath), lang, lang,
			// String.valueOf(catId)));
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// try {
			// db.close();
			// } catch (SQLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			// }.start();
			// }
		}
		// JOptionPane.showMessageDialog(null, "Finished");
		// setIndegree();
		setOutdegree();

	}

	private void setIndegree() {
		new Thread() {
			@Override
			public void run() {
				DbConnector db = null;
				try {
					db = new DbConnector();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ResultSet r = null;
				try {
					r = db.executeQuery(updateIndegree);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					while (r.next()) {
						db.executeUpdate("UPDATE pages SET indegree=? WHERE pageid=?",
								Arrays.asList(r.getString("indegree"), r.getString("toPageId")));
					}
					JOptionPane.showMessageDialog(null, "Finished");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					db.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();
	}

	private void setOutdegree() {
		new Thread() {
			@Override
			public void run() {
				DbConnector db = null;
				try {
					db = new DbConnector();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ResultSet r = null;
				try {
					r = db.executeQuery(updateOutdegree);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					while (r.next()) {
						db.executeUpdate("UPDATE pages SET outdegree=? WHERE pageid=?",
								Arrays.asList(r.getString("outdegree"), r.getString("fromPageId")));
					}
					JOptionPane.showMessageDialog(null, "Finished");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					db.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();
	}

	// TODO: Check closing and connections
	private void storeCategories(final List<Person> pList, final String lang) {
		new Thread() {

			private String id;

			@Override
			public void run() {
				for (Person p : pList) {
					DbConnector db = null;
					try {
						db = new DbConnector();
					} catch (ClassNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					if (p.getCategoryList() == null) {
						continue;
					}
					for (Category cat : p.getCategoryList()) {
						String title = cat.getTitle();

						try {
							ResultSet r = db.executeQuery(getCategory, Arrays.asList(title));
							if (r.first()) {
								id = r.getString("categoryId");
							} else {
								db.executeUpdate(categoryUpdate, Arrays.asList(title, lang));

								r = db.executeQuery(lastInserted, null);
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
									ResultSet r = db.executeQuery(getCategory, Arrays.asList(title));
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
							db.executeUpdate(personToCategory,
									Arrays.asList(String.valueOf(p.getPageid()), lang, String.valueOf(id)));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					p.setCategoryList(null);
					try {
						db.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}.start();

	}

	private void storeConnections(final List<Person> pList, final String lang) {
		new Thread() {

			@Override
			public void run() {
				for (Person p : pList) {
					DbConnector db = null;
					try {
						db = new DbConnector();
					} catch (ClassNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					if (p.getLinkList() == null) {
						continue;
					}
					for (Person linkPerson : p.getLinkList()) {
						try {
							db.executeUpdate(linkUpdate, Arrays.asList(String.valueOf(p.getPageid()),
									String.valueOf(linkPerson.getPageid()), lang));
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						}
					}
					try {
						db.close();
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
				DbConnector db = null;
				try {
					db = new DbConnector();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for (Person p : pList) {
					try {
						db.executeUpdate(personUpdate, Arrays.asList(String.valueOf(p.getPageid()), p.getTitle(),
								String.valueOf(p.getNs()), lang));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					db.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

}
