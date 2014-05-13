package ch.specchio.services;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import au.ands.org.researchdata.ResearchDataAustralia;

import ch.specchio.factories.SPECCHIOFactory;
import ch.specchio.factories.SPECCHIOFactoryException;
import ch.specchio.types.Capabilities;


/**
 * Base class for all SPECCHIO web services. This class provides a few common
 * security services used throughout the web application.
 */
public class SPECCHIOService {
	
	/** service version number */
	private static final String VERSION = "3.0.2";
	
	/** server capabilities */
	private Capabilities capabilities = null;
	
	/** the servlet configuration */
	@Context
	private ServletConfig config;
	
	/** the HTTP request */
	@Context
	private HttpServletRequest request;
	
	/** the HTTP response */
	@Context
	private HttpServletResponse response;
	
	/** the security context */
	@Context
	private SecurityContext security;
	
	/** the "Authorization" header */
	@HeaderParam("Authorization")
	private String auth;
	
	/** the user name associated with the request*/
	private String username = null;
	
	/** the password associated with the reqeust */
	private String password = null;
	
	
	/**
	 * Extract the username and password from the "Authorization" header
	 */
	private void configureAuthorization() {
		
		// split the "Authorization" header into the type and token
		String auth_parts[] = auth.split(" ");
		
		// decode the cookie
		String cookie = new String(javax.xml.bind.DatatypeConverter.parseBase64Binary(auth_parts[1]));
		
		// split the cookie into the username and password
		String cookie_parts[] = cookie.split(":");
		username = cookie_parts[0];
		password = cookie_parts[1];
		
	}
	
	
	/**
	 * Configure the server's capabilities object.
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	private void configureCapabilities() throws SPECCHIOFactoryException {
		
		// create a new capabilities object
		capabilities = new Capabilities();
	
		// set version number
		capabilities.setCapability(Capabilities.VERSION, VERSION);
	
		// enable or disable ANDS features
		String andsParameter = config.getInitParameter(ResearchDataAustralia.ANDS_INIT_PARAM_NAME);
		if (andsParameter != null && !andsParameter.equalsIgnoreCase("disabled")) {
			capabilities.setCapability(ResearchDataAustralia.ANDS_SERVER_CAPABILITY, "enabled");
		}
		
		// set database capabilities
		SPECCHIOFactory factory = new SPECCHIOFactory();
		Long maxObjectSize = factory.getMaximumQuerySize() - 1024;
		capabilities.setCapability(Capabilities.MAX_OBJECT_SIZE, maxObjectSize.toString());
		factory.dispose();
		
	}
	
	
	/**
	 * Get the password associated with the request.
	 * 
	 * @return the password sent in the "Authorization" header
	 */
	public String getClientPassword() {
		
		if (password == null) {
			configureAuthorization();
		}
	
		return password;
		
	}
	
	
	/**
	 * Get the user name associated with the request.
	 * 
	 * @return the username sent in the "Authorization" header
	 */
	public String getClientUsername() {
		
		if (username == null) {
			configureAuthorization();
		}
		
		return username;
			
	}
	
	
	/**
	 * Get the current request.
	 * 
	 * @return the HttpServletRequest object for the current reuest
	 */
	public HttpServletRequest getRequest() {
		
		return request;
		
	}
	
	
	/**
	 * Get the servlet response for the current request.
	 * 
	 * @return the HttpServletResponse object for the current request
	 */
	public HttpServletResponse getResponse() {
		
		return response;
		
	}
	
	
	/**
	 * Get the security context for the current request.
	 * 
	 * @return the SecurityContext object for the current request
	 */
	public SecurityContext getSecurityContext() {
		
		return security;
		
	}
	
	
	/**
	 * Get the server capabilities.
	 * 
	 * @return a Capabilities object
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public Capabilities getServerCapabilities() throws SPECCHIOFactoryException {
		
		if (capabilities == null) {
			configureCapabilities();
		}
		
		return capabilities;
		
	}
	
	
	/**
	 * Get the value of a server capability.
	 * 
	 * @param capability	the capability
	 * 
	 * @return the value of the capability, or null if the capability does not exist
	 * 
	 * @throws SPECCHIOFactoryException	database error
	 */
	public String getServerCapability(String capability) throws SPECCHIOFactoryException {
		
		return getServerCapabilities().getCapability(capability);
		
	}

}
