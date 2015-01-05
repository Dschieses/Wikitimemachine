package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DbConnector {

	private String db = "wikitimemachine";
	private String port = "3307";
	private String host = "localhost";
	private String dbUsr = "root";
	private String dbPwd = "derpate";
	private PreparedStatement prepStmt;
	private Statement st;
	private ResultSet rs;
	private Connection con;

	private Connection getDbConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, dbUsr, dbPwd);
	}

	public DbConnector() throws ClassNotFoundException, SQLException {
		con = this.getDbConnection();
	}

	public void executeUpdate(String statement, List<String> parameters) throws SQLException {

		prepStmt = con.prepareStatement(statement);
		int i = 1;
		if (parameters != null) {
			for (String iterator : parameters) {

				prepStmt.setString(i, iterator);
				i++;
			}
		}
		prepStmt.executeUpdate();
	}

	public void executeUpdate(String query) throws SQLException {
		st = con.createStatement();
		st.executeUpdate(query);
	}

	public ResultSet executeQuery(String statement, List<String> parameters) throws SQLException {

		prepStmt = con.prepareStatement(statement);
		int i = 1;
		if (parameters != null) {
			for (String iterator : parameters) {

				prepStmt.setString(i, iterator);
				i++;
			}
		}
		rs = prepStmt.executeQuery();

		return rs;
	}

	public ResultSet executeQuery(String statement) throws SQLException {
		return executeQuery(statement, null);
	}

	public void close(ResultSet rs) throws SQLException {
		if (!rs.isClosed()) {
			rs.close();
		}
	}

	public void close() throws SQLException {
		if (con != null && !con.isClosed()) {
			con.close();
		}
		if (rs != null && !rs.isClosed()) {
			rs.close();
		}
		if (prepStmt != null && !prepStmt.isClosed()) {
			prepStmt.close();
		}
		if (st != null && !st.isClosed()) {
			st.close();
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
