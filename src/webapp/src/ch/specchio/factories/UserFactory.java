package ch.specchio.factories;

import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.specchio.constants.Limits;
import ch.specchio.constants.UserRoles;
import ch.specchio.eav_db.SQL_StatementBuilder;
import ch.specchio.eav_db.TableNames;
import ch.specchio.types.Country;
import ch.specchio.types.Institute;
import ch.specchio.types.User;


/**
 * Class for manipulating users and institutes in the database.
 */
public class UserFactory extends SPECCHIOFactory {
	
	/** default password length */
	public static int DEFAULT_PASSWORD_LENGTH = 6;
	
	/** default password strength */
	public static int DEFAULT_PASSWORD_STRENGTH = 2;
	
	
	/**
	 * Construct a factory using the default connection to the database.
	 * 
	 * @throws SPECCHIOFactoryException	could not establish initial context
	 */
	public UserFactory() throws SPECCHIOFactoryException {
		
		super();
		
	}
	
	
	/**
	 * Construct a factory using another factory's connection to the database.
	 * 
	 * @param other	the factory whose connection will be used
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public UserFactory(SPECCHIOFactory other) throws SPECCHIOFactoryException {
		
		super(other);
		
	}
	
	
	/**
	 * Construct a factory using a specific user's connection to the database.
	 * 
	 * @param db_user		database account user name
	 * @param db_password	database account password
	 * 
	 * @throws SPECCHIOFactoryException	could not establish initial context
	 */
	public UserFactory(String db_user, String db_password) throws SPECCHIOFactoryException {

		super(db_user, db_password);
		
	}
	
	
	/**
	 * Build a User object from a ResultSet obejct returned by executing buildUserQueryString.
	 * 
	 * @param rs	the result set
	 * 
	 * @return a User object corresponding to the current row of rs
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	private User buildUserObject(ResultSet rs) throws SQLException, SPECCHIOFactoryException {
		
		try {
			User user = new User();
			user.setUserId(rs.getInt("user_id"));
			user.setUsername(rs.getString("specchio_user.user"));
			user.setFirstName(rs.getString("first_name"));
			user.setLastName(rs.getString("last_name"));
			user.setTitle(rs.getString("title"));
			user.setEmailAddress(rs.getString("email"));
			user.setWwwAddress(rs.getString("www"));
			int instituteId = rs.getInt("institute_id");
			if (instituteId > 0) {
				user.setInstitute(getDataCache().get_institute_by_id(instituteId));
			}
			user.setExternalId(rs.getString("external_id"));
			user.setRole(rs.getString("group_name"));
			
			return user;
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
	}
	
	
	/**
	 * Build an SQL query suitable for creating a user object. Pass the ResultSet object
	 * returned by this query to buildUserObject to create the actual user objects.
	 * 
	 * @param SQL		the SQL statement builder to use for building the query
	 * @param column	the name of the column by which to restrict the selection (null for no restrictions)
	 * @param value		the value of the column to which to restrict the selection
	 * 
	 * @return an SQL query that will return all users with column equal to value
	 * 
	 * @throws SQLException	'value' cannot be converted to an SQL value
	 */
	private String buildUserQueryString(SQL_StatementBuilder SQL, String column, Object value) throws SQLException {
		
		StringBuffer query = new StringBuffer();

		// start with no restrictions
		query.append("select user_id, specchio_user.user, first_name, last_name, title, email, www, institute_id, external_id, group_name");
		query.append(" from specchio_user, specchio_user_group");
		query.append(" where specchio_user.user=specchio_user_group.user");
		
		if (column != null) {
			// add restriction
			query.append( " and ");
			query.append(SQL.prefix("specchio_user", column) + "=" + SQL.quote_value(value));
		}
		
		return query.toString();
		
	}
	
	
	/**
	 * Generate a random password.
	 * 
	 * @param length	the length of the new password
	 * @param strength	strength parameter
	 * 
	 * @return a randomly-generated password of the specified length
	 */
	private String generatePassword(int length, int strength) {
		
		// set up the characters to choose from
		StringBuffer vowels = new StringBuffer("aeuy");
		StringBuffer consonants = new StringBuffer("bdghjmnpqrstvz");
		if ((strength & 1) == 1) {
			consonants.append("BDGHJLMNPQRSTVWXZ");
		}
		if ((strength & 2) == 2) {
	        vowels.append("AEUY");
	    }
	    if ((strength & 4) == 4) {
	        consonants.append("23456789");
	    }
	    if ((strength & 8) == 8) {
	        consonants.append("@#$%");
	    }

	    // build the password
	    byte b[] = new byte[1];
	    SecureRandom rand = new SecureRandom();
	    StringBuffer password = new StringBuffer();
	    Date date = new Date();
	    boolean alt = (date.getTime() % 2) == 1;
	    for (int i = 0; i < length; i++) {
	    	rand.nextBytes(b);
	        if (alt) {
	            password.append(consonants.charAt(Math.abs(b[0]) % consonants.length()));
	            alt = false;
	        } else {
	            password.append(vowels.charAt(Math.abs(b[0]) % vowels.length()));
	            alt = true;
	        }
	    }
	    
	    return password.toString();
	    
	}
	
	
	/**
	 * Generate a unique user name for a given first name and last name.
	 * 
	 * @param first		the first name of the user for which the username is intended
	 * @param last		the last name of the user for which the username is intended
	 * 
	 * @return a unique username
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	private String generateUsername(String first, String last) throws SPECCHIOFactoryException {
		
		// start with the first character of the first name and first six characters of the surname
		StringBuffer username = new StringBuffer();
		username.append(first.charAt(0));
		if  (last.length() >= 6) {
			username.append(last.substring(0, 6));
		} else {
			username.append(last);
		}
		int baseLength = username.length();
		
		try {
		
			// prepare an SQL statement for testing for the existence of a username
			PreparedStatement pstmt = getStatementBuilder().prepareStatement(
					"select count(*) from (" +
							"select user from specchio.specchio_user where user=?" +
							" union " +
							"select user from mysql.user where user=?" +
					") as user"
				);
			
			// append digits until we find a unique username
			int matches = 0;
			int number = 0;
			do {
				
				// see if the proposed username already exists
				pstmt.setString(1, username.toString());
				pstmt.setString(2, username.toString());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					matches = rs.getInt(1);
				}
				
				if (matches > 0) {
					// username already exists; append the next digit in the sequence
					number++;
					username.delete(baseLength, username.length());
					username.append(Integer.toString(number));
				}
				
			} while (matches > 0);
			
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return username.toString();
		
	}
	
	
	/**
	 * Generate a password with the default length and strength.
	 * 
	 * @return a random password with length DEFAULT_PASSWORD_LENGTH
	 */
	public String generatePassword() {
		
		return generatePassword(DEFAULT_PASSWORD_LENGTH, DEFAULT_PASSWORD_LENGTH);
		
	}
	
	
	/**
	 * Get a list of all of the countries in the database.
	 * 
	 * @return an array containing a Country object for every country in the database
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public Country[] getCountries() throws SPECCHIOFactoryException {
		
		List<Country> countries = new ArrayList<Country>();
		
		try {
			Statement stmt = getStatementBuilder().createStatement();
			String query = "select country_id, name from country";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				countries.add(new Country(rs.getInt(1), rs.getString(2)));
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return countries.toArray(new Country[countries.size()]);
		
	}
							
	
	/**
	 * Get a list of all of the institutes in the database.
	 * 
	 * @return an array containing an Institute object for every institute in the database
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public Institute[] getInstitutes() throws SPECCHIOFactoryException {
		
		List<Institute> institutes = getDataCache().get_institutes();
		return institutes.toArray(new Institute[institutes.size()]);
		
	}
	
	
	/**
	 * Get a user object by identifier.
	 * 
	 * @param userId	the user identifier
	 * 
	 * @return a user object correspond to userId, or null of the identifier does not exist
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public User getUser(int userId) throws SPECCHIOFactoryException {
		
		return getUser("user_id", userId);
		
	}
	
	
	/**
	 * Get a user object by username.
	 * 
	 * @param username	the username
	 * 
	 * @return a user object corresponding to username, or null if the user does not exist
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public User getUser(String username) throws SPECCHIOFactoryException {
		
		return getUser("user", username);
		
	}
		
		
	/**
	 * Helper method for getUser()
	 * 
	 * @param column	the name of the column to search for a match
	 * @param value		the value that the column should match
	 * 
	 * @return a user object corresponding to the row with column equal to value, or null if there are no such rows
	 *
	 * @throws SPECCHIOFactoryException	database error
	 */
	private User getUser(String column, Object value) throws SPECCHIOFactoryException {
		
		User user = null;
		
		try {
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			String query = buildUserQueryString(SQL, column, value);
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				user = buildUserObject(rs);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return user;
		
	}
	
	
	/**
	 * Get all of the users in the database.
	 * 
	 * @return an array of User objects
	 *
	 * @throws SPECCHIOFactoryException	database error
	 */
	public User[] getUsers() throws SPECCHIOFactoryException {
		
		ArrayList<User> users = new ArrayList<User>();
		
		try {
			// create SQL-building objects
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			
			// get all of the users in the database
			String query = buildUserQueryString(SQL, null, null);
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				users.add(buildUserObject(rs));
			}
			rs.close();
			
			// clean up
			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return users.toArray(new User[users.size()]);
			
		
	}
	
	
	/**
	 * Grant administrative rights to a user.
	 * 
	 * @param user	the user
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private void grantAdminRights(User user) throws SPECCHIOFactoryException {
		
		try {
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			String query;
			
			// build a quoted string that identifies the user
			String userString = SQL.quote_string(user.getUsername()) + "@" + SQL.quote_string(getDatabaseUserHost());
			
			// grant table permissions
			query = "GRANT SELECT, DELETE, INSERT, UPDATE, ALTER, DROP, CREATE, CREATE VIEW, GRANT OPTION, TRIGGER ON " +
					getDatabaseName() + ".* TO " + userString;
			stmt.executeUpdate(query);
			
			// allow creation and manipulation of temporary tables
			query = "GRANT SELECT, DELETE, INSERT, UPDATE, CREATE TEMPORARY TABLES, GRANT OPTION ON " +
					SQL.prefix(getTempDatabaseName(), "*") +
					" TO " + userString;
			stmt.executeUpdate(query);
			
			// grant right to add users
			query = "GRANT SUPER, CREATE USER on *.* TO " + userString;
			stmt.executeUpdate(query);
			query = "GRANT INSERT ON `mysql`.`user` TO " + userString;
			stmt.executeUpdate(query);
			query = "UPDATE mysql.user SET Reload_priv='Y', Process_priv='Y', Update_priv='Y', Delete_priv='Y', Select_priv='Y' " +
					"where User=" + SQL.quote_string(user.getUsername());
			stmt.executeUpdate(query);
			
			// flush privileges
			query = "FLUSH PRIVILEGES";
			stmt.executeUpdate(query);
			
			// clean up
			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
	}
	
	
	/**
	 * Grant normal user rights to a user.
	 * 
	 * @param user	the user
	 * 
	 * @throws SPECCHIOFactoryException database error
	 */
	private void grantUserRights(User user) throws SPECCHIOFactoryException {
		
		try {
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			String query;
			
			// build a quoted string that identifies the user
			String userString = SQL.quote_string(user.getUsername()) + "@" + SQL.quote_string(getDatabaseUserHost());
		  	
			// grant privileges on views
			for (String view : TableNames.VIEWS) {
				query = "GRANT SELECT, INSERT, UPDATE, DELETE" +
						" ON " + SQL.prefix(getDatabaseName(), view) +
						" TO " + userString;
				stmt.executeUpdate(query);
			}
			
			// grant privileges on tables
			for (String table : TableNames.TABLES) {
				query = "GRANT SELECT ON " + SQL.prefix(getDatabaseName(), table) + " TO " + userString;
				stmt.executeUpdate(query);
			}
			
			// allow creation and manipulation of temporary tables
			query = "GRANT SELECT, DELETE, INSERT, UPDATE, CREATE TEMPORARY TABLES ON " +
					SQL.prefix(getTempDatabaseName(), "*") +
					" TO " + userString;
			stmt.executeUpdate(query);
			
			// special grants for the spectrum table
			String spectrumView = SQL.prefix(getDatabaseName(), "spectrum_view");
			stmt.executeUpdate(
					"GRANT DELETE ON " + spectrumView + " TO " + userString
				);
			stmt.executeUpdate(
				"GRANT SELECT, INSERT, UPDATE(" + SQL.conc_cols(TableNames.SPECTRUM_VIEW_COLS) + ") ON " +
				spectrumView + " TO " + userString
			);
			stmt.executeUpdate(
				"GRANT SELECT (is_reference) ON " + spectrumView + " TO " + userString
			);
			
			// grant privileges on the user tables
			for (String table : TableNames.USER_TABLES) {
				query = "GRANT UPDATE ON " + SQL.prefix(getDatabaseName(), table) + " TO " + userString;
				stmt.executeUpdate(query);
			}
			
			// flush privileges
			stmt.executeUpdate("FLUSH PRIVILEGES");
			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
	}
	
	
	/**
	 * Insert a new institute into the database. The institute ID field of the
	 * input institute object will be over-written with identifier of the new
	 * institute.
	 * 
	 * @param institute	the new institute
	 * 
	 * @return	the identifier of the new institute
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public int insertInstitute(Institute institute) throws SPECCHIOFactoryException {
		
		try {
			
			// add the new institute to the database
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			String query = "insert into institute(name, department, street, street_no, po_code, city, country_id, www) values(" +
					SQL.conc_values(
							institute.getInstituteName(),
							institute.getDepartment(),
							institute.getStreet(),
							institute.getStreetNumber(),
							institute.getPostOfficeCode(),
							institute.getCity(),
							(institute.getCountry() != null)? Integer.toString(institute.getCountry().getId()) : null,
							institute.getWWWAddress()
					) + ")";
			stmt.executeUpdate(query);
			
			// get the identifier of the new institute
			ResultSet rs = stmt.executeQuery("select last_insert_id()");
			while (rs.next()) {
				institute.setInstituteId(rs.getInt(1));
			}
			
			// clean up
			stmt.close();
			
			// add the new institute to the data cache
			getDataCache().add_institute(institute);
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return institute.getInstituteId();
	}
				


	/**
	 * Insert a new user into the database. The user ID, username and password fields of
	 * the user object will be over-written with newly-generated data.
	 * 
	 * @param user		the user to be created
	 * 
	 * @return the identifier of the new user
	 * 
	 * @throws IllegalArgumentException	the user object contains invalid data
	 * @throws SPECCHIOFactoryException	database error
	 */
	public int insertUser(User user) throws SPECCHIOFactoryException, IllegalArgumentException {
		
		// check that the fields are not too long
		if (user.getFirstName() == null || user.getFirstName().length() == 0) {
			throw new IllegalArgumentException("Users must have a first name.");
		} else if (user.getLastName() == null || user.getLastName().length() == 0) {
			throw new IllegalArgumentException("Users must have a last name.");
		} else if (user.getFirstName().length() > Limits.MAX_LEN_NAME || user.getLastName().length() > Limits.MAX_LEN_NAME) {
			throw new IllegalArgumentException("Names cannot contain more than " + Limits.MAX_LEN_NAME + " characters.");
		} else if (user.getEmailAddress() ==  null || user.getEmailAddress().length() == 0) {
			throw new IllegalArgumentException("Users must have an e-mail address.");
		} else if (user.getEmailAddress().length() > Limits.MAX_LEN_EMAIL) {
			throw new IllegalArgumentException("E-mail addresses cannot contain more than " + Limits.MAX_LEN_EMAIL + " characters.");
		} else if (user.getWwwAddress() != null && user.getWwwAddress().length() > Limits.MAX_LEN_WWW) {
			throw new IllegalArgumentException("WWW address cannot contain more than " + Limits.MAX_LEN_WWW + " characters.");
		}
		
		try {
			// generate a username and password for the user
			user.setUsername(generateUsername(user.getFirstName(), user.getLastName()));
			user.setPassword(generatePassword());
			
			// initialise SQL-building objects
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			String query;
			
			// create database user
			String userString = SQL.quote_string(user.getUsername()) + "@" + SQL.quote_string(getDatabaseUserHost());
			query = "create user " + userString + " " +
					"identified by " + SQL.quote_string(user.getPassword());
			stmt.executeUpdate(query);
			
			// work out institute identifier
			Institute inst = user.getInstitute();
			String instIdString = (inst != null)? Integer.toString(inst.getInstituteId()) : "null";
			
			// update user table
			query = "insert into specchio_user(user,first_name,last_name,institute_id,email,www,admin,password,external_id) values(" +
					SQL.quote_string(user.getUsername()) + "," +
					SQL.quote_string(user.getFirstName()) + "," +
					SQL.quote_string(user.getLastName()) + "," +
					instIdString + "," +
					SQL.quote_string(user.getEmailAddress()) + "," +
					SQL.quote_string(user.getWwwAddress()) + "," +
					(user.isInRole(UserRoles.ADMIN)? "1" : "0") + "," +
					"MD5(" + SQL.quote_string(user.getPassword()) + ")," +
					SQL.quote_string(user.getExternalId()) +
				")";
			stmt.executeUpdate(query);
			
			// update group table
			query = "insert into specchio_user_group(user,group_name) values(" +
					SQL.quote_string(user.getUsername()) + "," +
					SQL.quote_string(user.getRole()) +
					")";
			stmt.executeUpdate(query);
			
			// get the new user's identifier
			query = "select last_insert_id()";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				user.setUserId(rs.getInt(1));
			}
			rs.close();
			
			// clean up
			stmt.close();
			
			// grant rights
			if (user.isInRole(UserRoles.ADMIN)) {
				grantAdminRights(user);
			} else {
				grantUserRights(user);
			}
			
		}
		catch (SQLException ex) {
			//database error
			throw new SPECCHIOFactoryException(ex);
		}
		
		return user.getUserId();
		
	}
	
	
	/**
	 * Update a user in the database.
	 * 
	 * @param user	the new user
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public void updateUser(User user) throws SPECCHIOFactoryException {
		
		try {
			// set up SQL-building objects
			SQL_StatementBuilder SQL = getStatementBuilder();
			Statement stmt = SQL.createStatement();
			String query;
			
			// build a "where" condition that will match the user
			String userCondition = "user=" + SQL.quote_string(user.getUsername());
			
			// update the user table
			ArrayList<String> attr_and_vals = new ArrayList<String>(10);
			attr_and_vals.add("first_name"); attr_and_vals.add(user.getFirstName());
			attr_and_vals.add("last_name"); attr_and_vals.add(user.getLastName());
			attr_and_vals.add("title"); attr_and_vals.add(user.getTitle());
			attr_and_vals.add("email"); attr_and_vals.add(user.getEmailAddress());
			attr_and_vals.add("www"); attr_and_vals.add(user.getWwwAddress());
			attr_and_vals.add("external_id"); attr_and_vals.add(user.getExternalId());
			query = SQL.assemble_sql_update_query(
					SQL.conc_attr_and_vals("specchio_user", attr_and_vals.toArray(new String[attr_and_vals.size()])),
					"specchio_user",
					userCondition
				);
			stmt.executeUpdate(query);
			
			// update "admin" flag
			String adminFlag = (user.isInRole(UserRoles.ADMIN))? "1" : "0";
			query = "update specchio_user set admin=" + adminFlag + " where " + userCondition;
			stmt.executeUpdate(query);
			
			Institute inst = user.getInstitute();
			if (inst != null) {
				// update the institute identifier
				query = "update specchio_user" +
						" set institute_id=" + Integer.toString(inst.getInstituteId()) +
						" where " + userCondition;
				stmt.executeUpdate(query);
			}
			
			// update group table
			query = "update specchio_user_group" +
					" set group_name=" + SQL.quote_string(user.getRole()) +
					" where " + userCondition;
			stmt.executeUpdate(query);
			
			// clean up
			stmt.close();
		}
		catch (SQLException ex) {
			// database error
			throw new SPECCHIOFactoryException(ex);
		}
		
	}

}
