package ch.specchio.services;

import javax.annotation.security.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import ch.specchio.constants.UserRoles;
import ch.specchio.factories.SPECCHIOFactoryException;
import ch.specchio.factories.UserFactory;
import ch.specchio.types.User;


/**
 * User services.
 */
@Path("/user")
@DeclareRoles({UserRoles.ADMIN, UserRoles.USER})
public class UserService extends SPECCHIOService {
	
	
	/**
	 * List all of the users in the database.
	 * 
	 * @return an array of User objects
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("list")
	public User[] list() throws SPECCHIOFactoryException {
		
		UserFactory factory = new UserFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		User users[] = factory.getUsers();
		factory.dispose();
		
		return users;
		
	}
	
	
	/**
	 * Log in to the database.
	 * 
	 * @return a user object representing the user
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("login")
	public User login() throws SPECCHIOFactoryException {
		
		UserFactory factory = new UserFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		User user = factory.getUser(getClientUsername());
		factory.dispose();
		
		return user;
		
	}
	
	
	/**
	 * Log out from the database.
	 * 
	 * @return an empty string
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("logout")
	public String logout() {
		
		return "";
		
	}
	
	
	/**
	 * Update a user.
	 * 
	 * @param user	the new user data
	 * 
	 * @throws SecurityException		a non-admin user tried to modify admin-only settings
	 * @throws SPECCHIOFactoryException	database error
	 * 
	 * @return an empty string
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	@Path("update")
	public String updateUser(User user) throws SPECCHIOFactoryException {
		
		// check that non-admin users are behaving themselves
		if (!getSecurityContext().isUserInRole(UserRoles.ADMIN)) {
			if (!getClientUsername().equals(user.getUsername())) {
				// a non-admin user is trying to modify someone else's details
				throw new SecurityException("Non-admin users may only modify their own details.");
			}
			if (!UserRoles.USER.equals(user.getRole())) {
				// a non-admin user is trying to modify someone's role
				throw new SecurityException("Non-admin users may not modify their roles.");
			}
		}
		
		UserFactory factory = new UserFactory(getClientUsername(), getClientPassword(), getDataSourceName(), isAdmin());
		factory.updateUser(user);
		factory.dispose();
		
		return "";
		
	}

}
