package ch.specchio.client;

import java.net.ConnectException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

public class SPECCHIOWebClientException extends SPECCHIOClientException {

	/** serialisation version ID */
	private static final long serialVersionUID = 1L;
	
	/** the user message */
	private final String userMessage;
	
	
	/**
	 * Constructor for re-throwing a client handler exception thrown by Jersey.
	 */
	public SPECCHIOWebClientException(ClientHandlerException ex) {
		
		super(ex.getMessage(), ex);
		
		if (ex.getCause() instanceof ConnectException) {
			userMessage = "Could not connect to the server. Please check that the server is running at the server and port specified.";
		} else if (ex.getCause() instanceof UnknownHostException) {
			userMessage = "Unknown server name. Please check that the server name is correct.";
		} else if (ex.getCause() instanceof SSLHandshakeException) {
			userMessage = "Could not validate the server's security certificate. Please check that the server's certificate has been correctly signed and installed.";
		} else if (ex.getCause() instanceof SSLException) {
			userMessage = "Could not find any trusted security certificates. Please check that the specchio.keystore file exists and is correct.";
		} else {
			userMessage = null;
		}
		
	}
	
	
	/**
	 * Constructor for re-throwing a uniform interface exception thrown by Jersey.
	 */
	public SPECCHIOWebClientException(UniformInterfaceException ex) {
		
		super(ex.getMessage(), ex);
		
		userMessage = ex.getMessage();
		
	}
	
	
	/**
	 * Constructor for throwing a new exception.
	 * 
	 * @param message	the message to be reported by the exception
	 */
	public SPECCHIOWebClientException(String message) {
		
		super(message);
		
		userMessage = message;
		
	}
	
	
	/**
	 * Get an error message suitable for displaying to the user.
	 *
	 * @return a brief user-readable error message describing the error
	 */
	public String getUserMessage() {
		
		return (userMessage != null)? userMessage : super.getUserMessage();
		
	}

}
