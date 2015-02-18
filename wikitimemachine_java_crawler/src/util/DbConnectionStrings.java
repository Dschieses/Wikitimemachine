package util;
/**
 * 
 * The class contains all settings, which are necessary for a database access
 *
 */
public class DbConnectionStrings {
/**
 * the name of the database used
 */
	private String db = "wikitimemachine";
	/**
	 * The database access port
	 */
	   private String port = "3307";
	 /**
	  *  The host name
	  */
	private String host = "localhost";
	/**
	 * The user name, needed to log in
	 */
	private String dbUsr = "root";
	/**
	 * The password, needed to log in
	 */
	private String dbPwd = "derpate";
	
	/**
	 * An object which is used when accessing database settings
	 */
	private static DbConnectionStrings instance;

	/**
	 * an empty constructor for a DbConnectionStrings object
	 */
	private DbConnectionStrings() {

	}

	/**
	 * The method returns a DbConnectionStrings object. This object can then be used to access database settings
	 * @return  a DbConnectionStrings object. 
	 */
	public static DbConnectionStrings getInstance() {
		if (instance == null) {
			instance = new DbConnectionStrings();
		}
		return instance;
	}
/**
 * This method is used when getting a database name. It is needed for a database connection in the DBConnector class 
 * @return the current database name
 */
	public String getDb() {
		return db;
	}

	/**
	 * A method, which changes the current database name
	 * @param db a new name for a database
	 */
	public void setDb(String db) {
		this.db = db;
	}
/**
 * This method is used when getting a database access port. It is needed for a database connection in the DBConnector class 
 * @return the current database access port
 */
	public String getPort() {
		return port;
	}
/**
 *   A method, which changes the current database port
 *  
 * @param port new port value
 */
	public void setPort(String port) {
		this.port = port;
	}
/**
 * A method, which gets the host name
 * @return the current host name
 */
	public String getHost() {
		return host;
	}
/**
 * A method to set new host name
 * @param host new host name value
 */
	public void setHost(String host) {
		this.host = host;
	}
/**
 * A method, which gets the database user name
 * @return The current user name
 */
	public String getDbUsr() {
		return dbUsr;
	}
/**
 * A method to set new user name
 * @param dbUsr new user name
 */
	public void setDbUsr(String dbUsr) {
		this.dbUsr = dbUsr;
	}
/**
 * A method, which gets the database password
 * @return the current database password
 */
	public String getDbPwd() {
		return dbPwd;
	}

	/**
	 * A method to set new password, which should be used to access the database
	 * @param dbPwd new password, which should be used to access the database
	 */
	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}

}
