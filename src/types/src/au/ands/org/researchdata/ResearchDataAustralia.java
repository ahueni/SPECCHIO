package au.ands.org.researchdata;

import ch.specchio.types.User;


/**
 * Constants and utilities used for interacting with Research Data Australia
 */
public class ResearchDataAustralia {
	
	/** the name of the servlet initialisation paramater that controls whether or not ANDS features are enabled */
	public static final String ANDS_INIT_PARAM_NAME = "au.org.ands.researchdata";
			
	/** the name of the server capability that indicates whether or not ANDS features are enabled */
	public static final String ANDS_SERVER_CAPABILITY = "ANDS";
	
	
	/**
	 * Generate an ANDS party identifier for a user
	 * 
	 * @param prefix	the prefix for the identifier
	 * @param user		the user
	 * 
	 * @return an ANDS identifier
	 */
	public static String generatePartyIdentifier(String prefix, User user) {
		
		return String.format("%s%02d", prefix, user.getUserId());
		
	}

}
