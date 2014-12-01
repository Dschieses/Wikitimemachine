package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnector {

	private static DbConnector instance;
	private String db = "wikihistory";
	private String port = "3307";
	private String host = "localhost";
	private String dbUsr = "root";
	private String dbPwd = "derpate";

	private DbConnector() {
	}

	public static synchronized DbConnector getInstance() {
		if (DbConnector.instance == null) {
			DbConnector.instance = new DbConnector();
		}
		return DbConnector.instance;
	}

	public Connection getDbConnection() throws SQLException,
			ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port
				+ "/" + db, dbUsr, dbPwd);
	}

	public ResultSet executeQuery(Connection con, String query)
			throws SQLException {

		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		return rs;
	}

	public int executeUpdate(Connection con, String query) throws SQLException {

		Statement st = con.createStatement();
		int rs = st.executeUpdate(query);
		return rs;
	}

	public void close(ResultSet rs) throws SQLException {
		if (!rs.isClosed()) {
			rs.close();
		}
	}

	public void close(Statement st) throws SQLException {
		if (!st.isClosed()) {
			st.close();
		}
	}

	public void close(Connection con) throws SQLException {
		if (!con.isClosed()) {
			con.close();
		}
	}

}
