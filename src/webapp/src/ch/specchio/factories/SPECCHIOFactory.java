package ch.specchio.factories;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import ch.specchio.eav_db.Attributes;
import ch.specchio.eav_db.EAVDBServices;
import ch.specchio.eav_db.SQL_StatementBuilder;


/**
 * Base class for all factories. This class encapsulates the database
 * information common to all factories.
 */
public class SPECCHIOFactory {
	
	/** the suffix used for the name of the temporary database */
	private static final String TEMP_DATABASE_SUFFIX = "_temp";
	
	/** the data source name */
	protected static String datasource_name = null;	
	
	/** the data source */
	private static DataSource ds = null;
	
	/** the database connection */
	private Connection conn = null;
	
	/** did this object create its own database connection? */
	private boolean my_conn = false;
	
	/** the data cache */
	private static DataCache cache = null;
	
	/** attributes server */
	private static Attributes attr = null;
	
	/** eav services */
	private EAVDBServices eav = null;
	
	/** SQL statement builder */
	private SQL_StatementBuilder sql = null;
	
	/** database name */
	private String databaseName = null;
	
	/** user part of the database user string */
	private String databaseUserName = null;
	
	/** host part of the database user string */
	private String databaseUserHost = null;
	
	
	/**
	 * Construct a factory using the default connection to the database.
	 * 
	 * @throws SPECCHIOFactoryException	could not establish initial context
	 */
	public SPECCHIOFactory(String ds_name) throws SPECCHIOFactoryException {
		
		try {
			// set up database connection
			SPECCHIOFactory.datasource_name = ds_name;
			init(getDataSource(ds_name).getConnection());
			this.my_conn = true;
		}
		catch (SQLException ex) {
			// bad username or password
			throw new SecurityException(ex);
		}
		
	}
	
	
	/**
	 * Construct a factory using a specific user's connection to the database.
	 * 
	 * @param db_user		database account user name
	 * @param db_password	database account password
	 * 
	 * @throws SPECCHIOFactoryException	could not establish initial context
	 */
	public SPECCHIOFactory(String db_user, String db_password, String ds_name) throws SPECCHIOFactoryException {
		
		try {
			// set up database connection
			SPECCHIOFactory.datasource_name = ds_name;
			init(getDataSource(ds_name).getConnection(db_user, db_password));
			this.my_conn = true;
		}
		catch (SQLException ex) {
			// bad username or password
			throw new SPECCHIOFactoryException(ex);
		}
		
	}
	
	
	/**
	 * Copy constructor. Construct a new factory that uses the same database connection
	 * as an existing factory.
	 * 
	 * @param factory	the existing factory
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public SPECCHIOFactory(SPECCHIOFactory factory) throws SPECCHIOFactoryException {
		
		init(factory.getConnection());
		this.my_conn = false;
		
	}
	
	
	/**
	 * Configure the attributes cache.
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	private synchronized void configureCaches() throws SPECCHIOFactoryException {
		
		if (SPECCHIOFactory.attr == null || SPECCHIOFactory.cache == null) {
			// need to populate at least one cache
			
			try {
				// get an SQL statement builder using the default connection to the database
				Connection conn = getDataSource(datasource_name).getConnection();
				SQL_StatementBuilder sql = new SQL_StatementBuilder(conn);
			
				if (SPECCHIOFactory.attr == null) {
					// construct and populate the attributes object
					SPECCHIOFactory.attr = new Attributes(sql);
				}
				
				if (SPECCHIOFactory.cache == null) {
					// construct and populate the data cache
					SPECCHIOFactory.cache = new DataCache(sql, datasource_name);
				}
				
				// close the connection
				conn.close();
			}
			catch (SQLException ex) {
				// database error
				throw new SPECCHIOFactoryException(ex);
			}
		
		}
		
	}
	
	
	/**
	 * Configure the member variables that store the database user information.
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	private void configureDatabaseUserInfo() throws SPECCHIOFactoryException {
		
		try {
			Statement stmt = sql.createStatement();
			ResultSet rs = stmt.executeQuery("select current_user()");
			while (rs.next()) {
				String userString = rs.getString(1);
				String parts[] = userString.split("@");
				databaseUserName = parts[0];
				databaseUserHost = parts[1];
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
	}

	
	
	/**
	 * Initialise the factory. This is a helper method for the constructors.
	 * 
	 * @param conn	the database connection to use
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private void init(Connection conn) throws SPECCHIOFactoryException {
		
		// make sure static members are configured
		configureCaches();
		
		// save the database connection
		this.conn = conn;
		
		// create a statement builder for this connection
		this.sql = new SQL_StatementBuilder(conn);
		
		// set up EAV services
		this.eav = new EAVDBServices(getStatementBuilder(), getAttributes(), getDatabaseUserName());
		this.eav.set_primary_x_eav_tablename("spectrum_x_eav", "spectrum_x_eav_view", "spectrum_id", "spectrum");
		this.eav.set_eav_view_name("eav_view");
		
	}
	
	
	/**
	 * Dispose of the factory.
	 */
	public void dispose() {
		
		try {
			// release the database connection
			if (this.my_conn) {
				this.conn.close();
			}
		}
		catch (SQLException ex) {
			// don't know what might cause this
		}
		
	}
	
	
	/**
	 * Garbage collection.
	 */
	public void finalize() {
		
		this.dispose();
		
	}
	
	
	/**
	 * Get a reference to the attributes service.
	 * 
	 * @return a reference to the Attributes object
	 */
	public Attributes getAttributes() {
		
		return attr;
		
	}
	
	
	/**
	 * Get a connection to the database.
	 */
	public Connection getConnection() {
		
		return conn;
		
	}
	
	
	/**
	 * Get a reference to the data cache, creating the cache if it does not alerady exist.
	 * 
	 * @return a reference to the unique instance of the data cache.
	 * 
	 * @throws SPECCHIOFactoryException	could not create the cache
	 */
	public static DataCache getDataCache() throws SPECCHIOFactoryException {
		
		return cache;
		
	}
	
	
	/**
	 * Get a reference to the data source.
	 * 
	 * @return a reference to the data source for this factory
	 * 
	 * @throws SPECCHIOFactoryException	could not establish the initial context
	 */
	private static DataSource getDataSource(String DataSource) throws SPECCHIOFactoryException {
		
		//if (ds == null) {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource)ctx.lookup(DataSource); // "jdbc/specchio_test"
			}
			catch (NamingException ex) {
				// not sure what causes this
				throw new SPECCHIOFactoryException(ex);
			}
		//}
		
		return ds;
		
	}
	
	
	/**
	 * Get a reference to the EAV services.
	 * 
	 * @return a reference to the EAVDBServices object
	 */
	public EAVDBServices getEavServices() {
		
		return eav;
		
	}
	
	
	/**
	 * Get the name of the database schema in use.
	 * 
	 * There is surely a better way of doing this, but the following code is
	 * the only suggestion that I could find.
	 * 
	 * @return the name of the database
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public String getDatabaseName() throws SPECCHIOFactoryException {
		
		if (databaseName == null) {
			try {
				Statement stmt = sql.createStatement();
				ResultSet rs = stmt.executeQuery("select schema()");
				while (rs.next()) {
					this.databaseName = rs.getString(1);
				}
			}
			catch (SQLException ex) {
				// database error
				throw new SPECCHIOFactoryException(ex);
			}
		}
		
		return databaseName;
		
	}
	
	
	/**
	 * Get the hostname part of the user string under which we are connected to the database.
	 * 
	 * @return the part of the user string following the "@"
	 * 
	 * @throws SQLException	database error
	 */
	public String getDatabaseUserHost() throws SPECCHIOFactoryException {
		
		// make sure the database user variables are configured
		if (databaseUserHost == null) {
			configureDatabaseUserInfo();
		}
		
		return databaseUserHost;
		
	}
	
	
	/**
	 * Get the username part of the user string under which are connected to the database.
	 * 
	 * @return the part of the user string before the "@"
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public String getDatabaseUserName() throws SPECCHIOFactoryException {
		
		// make sure the database user variables are configured
		if (databaseUserName == null) {
			configureDatabaseUserInfo();
		}
		
		return databaseUserName;
		
	}
	
	
	/**
	 * Get the datasource name of the current source identifying the database to be used.
	 * 
	 * @return datasource name
	 * 
	 */
	public String getSourceName() {
		
		return SPECCHIOFactory.datasource_name;		
	}	
	
	
	/**
	 * Get the maximum size of a database query.
	 * 
	 * @return the value of max_allowed_packet
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public long getMaximumQuerySize() throws SPECCHIOFactoryException {
		
		long max = 0;
		
		try {
			Statement stmt = getStatementBuilder().createStatement();
			ResultSet rs = stmt.executeQuery("show variables like " + getStatementBuilder().quote_string("max_allowed_packet"));
			while (rs.next()) {
				max = rs.getLong(2);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return max;
		
	}
	
	
	/**
	 * Get a reference to the statement builder for this factory.
	 * 
	 * @returns a reference to the SQL_StatementBuilder object
	 */
	public SQL_StatementBuilder getStatementBuilder() {
		
		return sql;
		
	}
	
	
	/**
	 * Get the name of the database in which temporary tables can be created by
	 * ordinary users.
	 *
	 * @return the name of the temporary database
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public String getTempDatabaseName() throws SPECCHIOFactoryException {
		
		return getDatabaseName() + TEMP_DATABASE_SUFFIX;
		
	}

}
