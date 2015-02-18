package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 
 *
 * The class is an interface between the database and the crawler. An object of this class is used to access the database. 
 *
 */
public class DbConnector {
/**
 * database name from settings class
 */
	private String db = DbConnectionStrings.getInstance().getDb();
	/**
	 * database port from settings class
	 */
	private String port = DbConnectionStrings.getInstance().getPort();
	/**
	 * database host name from settings class
	 */
	private String host = DbConnectionStrings.getInstance().getHost();
	/**
	 * database user name from settings class
	 */
	private String dbUsr = DbConnectionStrings.getInstance().getDbUsr();
	/**
	 * database password from settings class
	 */
	private String dbPwd = DbConnectionStrings.getInstance().getDbPwd();
	/**
	 * a prepared statement used when initiating SQL queries
	 */
	private PreparedStatement prepStmt;
	/**
	 * a statement used when initiating SQL queries
	 */
	private Statement st;
	/**
	 * a result set of a SQL query
	 */
	private ResultSet rs;
	/**
	 * a database connection
	 */
	private Connection con;

	/**
	 * The method is used when initializing a database connection
	 * @return a database connection
	  */
	private Connection getDbConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, dbUsr, dbPwd);
	}
/**
 * The method constructs a DBConnector object and initializes a database connection
 */
	public DbConnector() throws ClassNotFoundException, SQLException {
		con = this.getDbConnection();
	}

	/**
	 * The method executes a SQL update query on database 
	 * @param statement used statement
	 * @param parameters parameters of the statement
	 *
	 */
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
/**
 * The method is used to execute a query without variable parameters
 * @param query a query to execute
 * 
 */
	public void executeUpdate(String query) throws SQLException {
		st = con.createStatement();
		st.executeUpdate(query);
	}

	/**
	 * The method is used when updating multiple rows of a database at one time and thus decreasing the processing time
	 * @param query a query to execute
	 * @param parameterPageId An array of page ids which should be updated
	 * @param parameterPageRank An array of page rank values which should be written to the corresponding page ids
	 * @param lang used language
	 * 
	 */
	public void executeBatchPageRankUpdate(String query,int[] parameterPageId, float[] parameterPageRank, String lang) throws SQLException {
		con.setAutoCommit(false);
		PreparedStatement pstmt = con.prepareStatement(query);
		
			for (int i = 0;i<parameterPageId.length;i++) {
				pstmt.setInt(2, parameterPageId[i]);
				pstmt.setFloat(1, parameterPageRank[i]);
				pstmt.setString(3, lang);
				pstmt.addBatch();
			}
			pstmt.executeBatch();

		
	}
	/**
	 * The method is used when accessing specific rows of a database
	 * @param statement a statement to execute
	 * @param parameters parameters of the statement
	 * @return a result set of the query
	 * 
	 */
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
/**
 * The method is used to execute a query without variable parameters
 * @param statement a statement to execute
 * @return a result set of the statement
 * 
 */
	public ResultSet executeQuery(String statement) throws SQLException {
		return executeQuery(statement, null);
	}

	/**
	 * The method returns a statement, which can be used to execute a query on a database. The method is used in some cases to dramatically reduce memory consumption. 
	 * @return A statement.
	 */
	public Statement getStatement() {
		
		try {
			return con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * The method closes a result set.
	 * @param rs a result set.
	 * 
	 */
	public void close(ResultSet rs) throws SQLException {
		if (!rs.isClosed()) {
			rs.close();
		}
	}
/**
 * The method closes a connection, a result set, a statement and a prepared statement .
 * 
 */
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
/**
 * The method closes a statement
 * @param st a statement to close
 * 
 */
	public void close(Statement st) throws SQLException {
		if (!st.isClosed()) {
			st.close();
		}
	}
/**
 * The method closes a connection
 * @param con a connection to close
 * 
 */
	public void close(Connection con) throws SQLException {
		if (!con.isClosed()) {
			con.close();
		}
	}

}
