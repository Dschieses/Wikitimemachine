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
	private String indegreeQuery = "SELECT COUNT(*) as indegree,toPageId FROM `connection` GROUP BY toPageId";
	private String outdegreeQuery = "SELECT COUNT(*) as outdegree,fromPageId, lang FROM `connection` GROUP BY fromPageId, lang";
	private final String birthQuery = "UPDATE pages SET birthDate=? WHERE lang=? AND pageid IN (SELECT pageId FROM pagetocategory WHERE lang=? AND categoryId=?)";
	private final String deathQuery = "UPDATE pages SET deathDate=? WHERE lang=? AND pageid IN (SELECT pageId FROM pagetocategory WHERE lang=? AND categoryId=?)";
	private String updateIndegreeQuery = "UPDATE pages SET indegree=? WHERE pageid=?";
	private String updateOutdegreeQuery = "UPDATE pages SET outdegree=? WHERE pageid=?";
	private int maxThreads = 15;
	private ResultSet r;
	protected boolean watchDogFinished = false;
	int categoriesFinished = 0;
	int personsFinished = 0;
	int connectionsFinished = 0;

	private String updatePageRankQuery = "UPDATE pages SET pagerank=? WHERE pageid=? AND lang=?";
	private String MaxPageidQuery = "select MAX(Pageid) as pageid, lang from pages Group by lang";
	private String pageCountQuery = "SELECT COUNT(*) as pages, lang FROM `connection` Group by lang";
	private String pagerankPageidQuery = "SELECT pageid,pagerank,lang FROM `pages`";
	private String connectionsQuery = "SELECT fromPageId, toPageId, lang FROM `connection`";
	private final float D=0.85f; //a constant for Pagerank calculation 
	
	
	public void computePagerank(final String lang) {
		new Thread() {
			@Override
			public void run() {
				
				ResultSet pageid = null;
				ResultSet pageCount = null;
				ResultSet outdegree=null;
				DbConnector db = null;
				Double[] pageRank=null;
				Double outdegreeCount[]=null; //a variable needed for calculations
				int n=0; // a constant for a number of pages
				try {
					db = new DbConnector();
				} catch (ClassNotFoundException e1) {
					
					e1.printStackTrace();
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
				
				try {
					pageid = db.executeQuery(MaxPageidQuery);
					pageCount=db.executeQuery(pageCountQuery);
					outdegree=db.executeQuery(outdegreeQuery);
					while (pageid.next()) {
			
					if(pageid.getString("lang").equals(lang)){
						
							pageRank=new Double[Integer.parseInt(pageid.getString("pageid"))+1];
							outdegreeCount=new Double[pageRank.length];
						}
					}
					//get pages counted
					while (pageCount.next()) {
						if(pageCount.getString("lang").equals(lang)){
							n=Integer.parseInt(pageCount.getString("pages"));
						}
						
					}
					//get outdegrees of each page
					while (outdegree.next()) {
						if(outdegree.getString("lang").equals(lang)){
							
							//a division by D is done here instead of multiplication step in the updatePagerank Method, which is done for optimization purposes. 
							outdegreeCount[Integer.parseInt(outdegree.getString("fromPageId"))]=Double.parseDouble(outdegree.getString("outdegree"))/D;
						}
						
					}
					
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				try {
					pageid = db.executeQuery(pagerankPageidQuery);
				} catch (SQLException e) {
				
					e.printStackTrace();
				}

				//initialize PageRank with value 1
				try {
					while (pageid.next()) {
						
						pageRank[Integer.parseInt(pageid.getString("pageid"))]=1d;
						}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pageRank=updatePageRank(db, pageRank, lang, n,outdegreeCount);
			//save the actual pagerank to the database
				try {
					pageid = db.executeQuery(pagerankPageidQuery);
				} catch (SQLException e) {
					
					e.printStackTrace();
				}

				
				try {
					while (pageid.next()) {
						
						db.executeUpdate(updatePageRankQuery,
								Arrays.asList(pageRank[Integer.parseInt(pageid.getString("pageid"))].toString(), pageid.getString("pageid"), pageid.getString("lang")));
						
					}
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
	
	public Double[] updatePageRank(DbConnector db, Double[] pageRank,  String lang, int n, Double[] outdegreeCount){
		double prconst =(double) ((1d-D));
		//ResultSet pageid=null;
		ResultSet connections=null;
		Double[] pageRankTmp=new Double[pageRank.length];
		//initialize a value only if the corresponding pageid exists
		for(int i=0;i<pageRankTmp.length;i++){
			if(pageRank[i]!=null)pageRankTmp[i]=0d;
		}
		try {
			connections = db.executeQuery(connectionsQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		//initiate sum operation of the pagerank
		try {
			while (connections.next()) {
				if(connections.getString("lang").equals(lang)){
				
				pageRankTmp[Integer.parseInt(connections.getString("topageid"))]=
						pageRankTmp[
						         Integer.parseInt(connections.getString("topageid"))]+
						(pageRank[Integer.parseInt(connections.getString("frompageid"))]/
								outdegreeCount[Integer.parseInt(connections.getString("frompageid"))]);
			}
			}
			for(int i=1;i<pageRankTmp.length;i++){
				if(pageRankTmp[i]!=null){
				
				pageRankTmp[i]=prconst+pageRankTmp[i] ;
				}
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean noChange=true;
		for(int i=1;i<pageRank.length;i++){
			if(pageRankTmp[i]!=null){
				
			if( (Math.round (pageRank[i] * 1000 ) / 1000d)!=(Math.round (pageRankTmp[i] * 1000 ) / 1000d)) {
				noChange=false;
				i=pageRank.length;
			}
			}
		}
		if(!noChange){
			return updatePageRank(db, pageRankTmp, lang, n,outdegreeCount);
		}
		return pageRankTmp;
	}


	public void determineDates(final String lang) throws SQLException, ClassNotFoundException {
		DbConnector db = new DbConnector();

		r = db.executeQuery(selectAllCategories);
		RegexParser rp = new RegexParser();

		while (r.next()) {
			String category = r.getString("categoryTitle");
			final int catId = r.getInt("categoryId");
			final int yearBirth = rp.matchDate(category);
			final int yearDeath = rp.matchDate(category);
			if (yearBirth != -9999) {
				while (Thread.activeCount() > maxThreads) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
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
						try {
							db.executeUpdate(birthQuery,
									Arrays.asList(String.valueOf(yearBirth), lang, lang, String.valueOf(catId)));
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
			} else if (yearDeath != -9999) {
				while (Thread.activeCount() > maxThreads) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
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
						try {
							db.executeUpdate(deathQuery,
									Arrays.asList(String.valueOf(yearDeath), lang, lang, String.valueOf(catId)));
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
		}
		JOptionPane.showMessageDialog(null, "Finished");
		setIndegree();
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
					r = db.executeQuery(indegreeQuery);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					while (r.next()) {
						db.executeUpdate(updateIndegreeQuery,
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
					r = db.executeQuery(outdegreeQuery);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					while (r.next()) {
						db.executeUpdate(updateOutdegreeQuery,
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

	public void startWatchDog(final StoreMethods method) {
		new Thread() {
			@Override
			public void run() {

				switch (method) {
				case Pages:
					while (personsFinished != listSplit) {
						System.out.println("WatchDog: " + method.toString() + " Still Alive");

						CommonFunctions.printCurrentTimestamp();
						try {
							Thread.sleep(300 * 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				case Connections:
					while (connectionsFinished != listSplit) {
						System.out.println("WatchDog: " + method.toString() + " Still Alive");

						CommonFunctions.printCurrentTimestamp();
						try {
							Thread.sleep(300 * 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				case Categories:
					while (categoriesFinished != listSplit) {
						System.out.println("WatchDog: " + method.toString() + " Still Alive");

						CommonFunctions.printCurrentTimestamp();
						try {
							Thread.sleep(300 * 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;

				}

				switch (method) {
				case Pages:
					JOptionPane.showOptionDialog(null, "All Persons stored.", "Done!", JOptionPane.OK_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, new String[] { "OK" }, "OK");
					break;
				case Connections:
					JOptionPane.showOptionDialog(null, "All Connections stored.", "Done!", JOptionPane.OK_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, new String[] { "OK" }, "OK");
					break;
				case Categories:
					JOptionPane.showOptionDialog(null, "All Categories stored.", "Done!", JOptionPane.OK_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, new String[] { "OK" }, "OK");
					break;

				}
			}
		}.start();
	}

	public void store(List<Person> pList, StoreMethods method, String language) throws SQLException,
			ClassNotFoundException {
		if (pList == null) {
			return;
		}
		switch (method) {
		case Pages:
			for (List<Person> list : CommonFunctions.split(pList, listSplit)) {
				storePages(list, language);
			}
			break;
		case Connections:
			for (List<Person> list : CommonFunctions.split(pList, listSplit)) {
				storeConnections(list, language);
			}
			break;
		case Categories:
			for (List<Person> list : CommonFunctions.split(pList, listSplit)) {
				storeCategories(list, language);
			}
			break;

		}

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
				categoriesFinished++;

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
				connectionsFinished++;
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
				personsFinished++;
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
