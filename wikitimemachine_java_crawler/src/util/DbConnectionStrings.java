package util;

public class DbConnectionStrings {

	private String db = "wikitimemachine";
	private String port = "3307";
	private String host = "localhost";
	private String dbUsr = "root";
	private String dbPwd = "derpate";
	private static DbConnectionStrings instance;

	private DbConnectionStrings() {

	}

	public static DbConnectionStrings getInstance() {
		if (instance == null) {
			instance = new DbConnectionStrings();
		}
		return instance;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDbUsr() {
		return dbUsr;
	}

	public void setDbUsr(String dbUsr) {
		this.dbUsr = dbUsr;
	}

	public String getDbPwd() {
		return dbPwd;
	}

	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}

}
