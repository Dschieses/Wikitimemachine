/*
 * Created Oct 2014 - Feb 2015 during COINS
 * by Peter Praeder, Michael Koetting, Vladimir Trajt
 */
package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import entity.Category;
import entity.Person;

/**
 * 
 * The class has all methods implemented for creating/updating SQL database entities. This covers "pages", "category", "pagetocategory" and "connection" tables.
 *  
 *
 */
public class SqlUtil {
	
	//int i = 0;
	private int listSplit = 25;
	/**
	 *  a SQL query for inserting a new person entity into the "pages" table.
	 */
	private String personUpdate = "INSERT INTO pages (pageId,title,ns,lang) VALUES (?,?,?,?);";
	/**
	 *  a SQL query for inserting a new category entity into the "category" table.
	 */
	private String categoryUpdate = "INSERT INTO category (categoryTitle,lang) VALUES (?,?);";
	/**
	 * a SQL query for getting a category id of a specific category title.
	 */	
	private String getCategory = "SELECT categoryId FROM category WHERE categoryTitle=?";
	/**
	 *  a SQL query for inserting a new page-to-category entity into the "personToCategory" table.
	 */
	private String personToCategory = "INSERT INTO pagetocategory (pageId,lang,categoryId) VALUES (?,?,?)";
	private String lastInserted = "SELECT LAST_INSERT_ID()";
	/**
	 *  a SQL query for inserting a new connection entity into the "connection" table. The connection represents a link betwenn 2 page id entities.
	 */
	private String linkUpdate = "INSERT INTO connection (fromPageId,toPageId,lang) VALUES (?,?,?)";
	/**
	 * a SQL query for getting a set of category names.
	 */	
	private String selectAllCategories = "SELECT DISTINCT categoryTitle,categoryId FROM category WHERE categoryTitle  LIKE '%geboren%' OR categoryTitle  LIKE '%gestorben%'";
	/**
	 * a SQL query for getting an indegree value of a specific pageid.
	 */	
	private String indegreeQuery = "SELECT COUNT(*) as indegree,toPageId FROM `connection` GROUP BY toPageId";
	/**
	 * a SQL query for getting an outdegree value of a specific pageid.
	 */	
	private String outdegreeQuery = "SELECT COUNT(*) as outdegree,fromPageId, lang FROM `connection` GROUP BY fromPageId, lang";
	/**
	 * a SQL query for setting a birth date for a specific page id.
	 */	
	private final String birthQuery = "UPDATE pages SET birthDate=? WHERE lang=? AND pageid IN (SELECT pageId FROM pagetocategory WHERE lang=? AND categoryId=?)";
	/**
	 * a SQL query for setting a death date for a specific page id.
	 */	
	private final String deathQuery = "UPDATE pages SET deathDate=? WHERE lang=? AND pageid IN (SELECT pageId FROM pagetocategory WHERE lang=? AND categoryId=?)";
	/**
	 *  a SQL query for updating indegree for a specific page id.
	 */
	private String updateIndegreeQuery = "UPDATE pages SET indegree=? WHERE pageid=?";
	/**
	 *  a SQL query for updating outdegree for a specific page id.
	 */
	private String updateOutdegreeQuery = "UPDATE pages SET outdegree=? WHERE pageid=?";
	/**
	 *  a SQL query for updating page rank for a specific page id.
	 */
	private String updatePageRankQuery = "UPDATE pages SET pagerank=? WHERE pageid=? AND lang=?";
	/**
	 *  a SQL query for getting the biggest value of a page id.
	 */
	private String MaxPageidQuery = "select MAX(Pageid) as pageid from pages ";
	/**
	 *  a SQL query for getting the total number of all page entities.
	 */
	private String pageCountQuery = "SELECT COUNT(*) as pages FROM `pages`";
	/**
	 *  a SQL query for getting all page ids and corresponding page ranks.
	 */
	private String pagerankPageidQuery = "SELECT pageid,pagerank FROM `pages`";
	/**
	 *  a SQL query for getting all connections between a specific page id and other page ids.
	 */
	private String connectionsQuery = "SELECT fromPageId, toPageId FROM `connection` Where fromPageId=";
	/**
	 *  a SQL query for getting the number of all connections from a specific page id
	 */
	private String totalConnectionsQuery = "SELECT Count(*) as connectionSum, fromPageId as fromPageId FROM `connection` Group by fromPageId";
	
/**
 * maximum number of threads used
 */
	private int maxThreads = 15;
		
	protected boolean watchDogFinished = false;
	
	/**
	 * A counter for finished category extraction.
	 */
	int categoriesFinished = 0;
	/**
	 * A counter for finished person extraction.
	 */
	int personsFinished = 0;
	/**
	 * A counter for finished connection extraction.
	 */
	int connectionsFinished = 0;

	/**
	 * a constant for Pagerank calculation
	 */
	private final float D=0.85f;  
	

	
	public void computePagerank(String lang) {
		
		ResultSet pageid = null;
				ResultSet pageCount = null;
				ResultSet outdegree=null;
				DbConnector db = null;
				float[] pageRank=null;
				int[] pagerankid; //store pageIds here. Array is a collection of pageIds. With this it is simplier to access all pageIds
				int[][]connectionsArray=null;
				
				
				float outdegreeCount[]=null; //a variable needed for calculations
				int n=0; // a constant for a number of pages; appears in calculations of pagerank
				try {
					db = new DbConnector();
				} catch (ClassNotFoundException e1) {
					
					e1.printStackTrace();
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
				// get Max, PageId, how many pages in total and outdegree for each page
				//Then use pageid to initialize the pagerank array
				try {
					pageid = db.executeQuery(MaxPageidQuery);
					pageCount=db.executeQuery(pageCountQuery);
					outdegree=db.executeQuery(outdegreeQuery);
					while (pageid.next()) {
			
						
				//	if(pageid.getString("lang").equals(lang)){
					//	connectionsArray=new ArrayList[pageid.getInt("pageid")+1];

						connectionsArray=new int[pageid.getInt("pageid")+1][];
						pageRank=new float[pageid.getInt("pageid")+1];
						//	pageRank=new float[Integer.parseInt(pageid.getString("pageid"))+1];
						outdegreeCount=new float[pageRank.length];
				//		}
					}
					//get pages counted
					if (pageCount.next()) {
			//			if(pageCount.getString("lang").equals(lang)){
						n=pageCount.getInt("pages");
						
						
						//	n=Integer.parseInt(pageCount.getString("pages"));
			//			}
						
					}
				
					//get outdegrees of each page
					while (outdegree.next()) {
			//			if(outdegree.getString("lang").equals(lang)){
							
							//a division by D is done here instead of multiplication step in the updatePagerank Method, which is done for optimization purposes. 
						outdegreeCount[outdegree.getInt("fromPageId")]=((long)outdegree.getInt("outdegree")/D);
						//outdegreeCount[Integer.parseInt(outdegree.getString("fromPageId"))]=Long.parseLong(outdegree.getString("outdegree"))/D;
			//			}
						
					}
					
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
					
				pagerankid=new int[n];
				try{
					ResultSet connections=null;
					connections=db.executeQuery(totalConnectionsQuery);
					while (connections.next()) {
			
						connectionsArray[connections.getInt("fromPageId")]=new int[connections.getInt("connectionSum")];
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
				//and other variables for each pageid 
				
				try {
					int pagerankidIndex=0;
					while (pageid.next()) {
						pagerankid[pagerankidIndex]=pageid.getInt("pageid");
						pageRank[pagerankid[pagerankidIndex]]=1f;
						pagerankidIndex++;
						
						}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				//initialize connections
				pageid=null;
				pageCount =null;
				outdegree =null;
				
				
				//initialize a value only if the corresponding pageid exists
				try {
					
					//create temp array for last indexes of each Pagerank
					int[] connectionsArrayCursor=new int[connectionsArray.length];
		
					ResultSet connections=null;
					for(int pagerankidIndex=0;pagerankidIndex < pagerankid.length ;pagerankidIndex++){
					Statement st=db.getStatement();
					connections=st.executeQuery((connectionsQuery+pagerankid[pagerankidIndex]));
							while (connections.next()) {
						
							connectionsArray[pagerankid [pagerankidIndex ]][connectionsArrayCursor[pagerankid [pagerankidIndex ]]]=connections.getInt("topageid");
							
							connectionsArrayCursor[pagerankid [pagerankidIndex ]]++;
							//put(pagerankid[pagerankidIndex], new ArrayList<Integer>());
					
							
							}
						connections=null;
						st.close();
					//	System.out.println("wave "+pagerankidIndex+" completed"+(System.currentTimeMillis()-ct));
						
					}
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				pageRank=updatePageRank(pageRank, connectionsArray, n,  outdegreeCount);
			//save the actual pagerank to the database
			try {
					int batchArrayIndex=0;
					int[] parameterPageId=new int[300000];
					float[] parameterPageRank=new float[300000];
					
					for(int pageRankIdIndex=0;pageRankIdIndex<pagerankid.length;pageRankIdIndex++){
						parameterPageId[batchArrayIndex]=pagerankid[pageRankIdIndex];
						parameterPageRank[batchArrayIndex]=pageRank[pagerankid[pageRankIdIndex]];
						batchArrayIndex++;
						if(batchArrayIndex==300000){
						db.executeBatchPageRankUpdate(updatePageRankQuery, parameterPageId, parameterPageRank,lang);	
						batchArrayIndex=0;
						}
						}
					if(batchArrayIndex>0){
						db.executeBatchPageRankUpdate(updatePageRankQuery, parameterPageId, parameterPageRank,lang);	
						batchArrayIndex=0;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				try {
					db.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}		
		
	}
	/**
	 * The method is used to only compute pagerank after all pre-work is being done. That means all necessary data is loaded from a database and is initialized as objects.
	 * The computation is done according to the recursive formula taken from Wikipedia. The implementation is done as an iterative process in order to mind errors occurring due to continuous recursion.
	 * Further the memory consumption and computation speed are taken into the consideration.
	 * One iteration consists of 3 steps: 1. calculate the sum of incoming page rank quotes for each page. 2. Add the constant (1-d). 3. Determine whether the difference to the previous result is significant, i.d. bigger than 0.00002. 
	 * @param pageRank an array of pagerank values, where each index is a page id. The last index is the maximum page id value and the 0-index is not used.
	 * @param connectionsArray For each page id an array of connections to other page id is stored here. The connection definition is used according to the page rank calculation. The first index is page id. The second index is the index of a connection. Each array cell contains the destination page id. 
	 * @param n total number of pages.
	 * @param outdegreeCount outdegree of each page id. The index of the array is a page id.
	 * @return calculated array pageranks for each page id. The index is a page id .
	 */
	public float[] updatePageRank(float[] pageRank,	int[][] connectionsArray,int n, float[] outdegreeCount){
		float prconst =(float) ((1d-D));
		boolean noChange=true;
		float[] pageRankTmp=new float[pageRank.length];
		
		do{
			noChange=true;
		//initiate sum operation of the pagerank
for(int fromPageidIndex=0;fromPageidIndex < connectionsArray.length ;fromPageidIndex++){
	if(connectionsArray[fromPageidIndex]!=null)
			for(int toPageidIndex=0;toPageidIndex < connectionsArray[fromPageidIndex].length; toPageidIndex++ ){
				pageRankTmp[connectionsArray[fromPageidIndex][toPageidIndex]]=
						pageRankTmp[connectionsArray[fromPageidIndex][toPageidIndex]]+
						(pageRank[fromPageidIndex]/	outdegreeCount[fromPageidIndex]);
						}
				
			}
			
			for(int i=1;i<pageRankTmp.length;i++){	
				pageRankTmp[i]=prconst+pageRankTmp[i] ;
				if( noChange && (pageRank[i] - pageRankTmp[i]>0.00002f)||(pageRank[i] -pageRankTmp[i]<-0.00002f)) {
					noChange=false;
					System.out.println(pageRank[i] - pageRankTmp[i]);
				}
				
			}
			

			for(int i=0;i<pageRank.length;i++){
				pageRank[i]=pageRankTmp[i];
				pageRankTmp[i]=0f;
			}
			
	}while(!noChange);
		return pageRank;
	}
/**
 * The method determines the year, in which a person was born/died. It gets from data stored in categories an appropriate birth/death date and stores it into the right 
 * database table.
 * @param lang a language in which the birth date/death date is written.
 * 
 */
	public void determineDates(final String lang) throws SQLException, ClassNotFoundException {
		DbConnector db = new DbConnector();
		ResultSet r;
		r = db.executeQuery(selectAllCategories);
		RegexParser rp = new RegexParser(lang);

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
/**
 * The method sets indegree for each page id. It does not need any inputs nor outputs as it works directly with the database. It has a 2 steps approach. In the 1. step it gets the indegree for a page id by executing a SQL query. 2. step is write the indegree into the database.
 */
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

	/**
	 * The method sets outdegree for each page id. It does not need any inputs nor outputs as it works directly with the database. It has a 2 steps approach. In the 1. step it gets the outdegree for a page id by executing a SQL query. 2. step is write the indegree into the database.
	 */
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
 /**
  * A method to watch over active Threads
  * @param method
  */
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
/**
 * The method stores attributes of objects "person" depending on which kind of an attribute should be stored 
 * @param pList a list of objects "person"
 * @param method a kind of operation which should be applied to the list
 * @param language a current language of Wikipedia pages
 * 
 * 
 */
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
/**
 * Stores categories to which a person of a provided list belongs to 
 * @param pList list of persons
 * @param lang  a current language of Wikipedia pages
 */
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
/**
 * 
 *Stores connections  which a person of a provided list has to other persons on Wikipedia 
 * @param pList list of persons
 * @param lang  a current language of Wikipedia pages
 */
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
/**
 * Stores pages which represent a person in a specific Wikipedia language 
 * @param pList list of persons
 * @param lang  a current language of Wikipedia pages
 */
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
