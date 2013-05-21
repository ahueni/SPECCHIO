package ch.specchio.client;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

/**
 * This HTTP client filter corrects problems with HTTP response objects
 * created when SPECCHIO is run within environments such as MATLAB.
 */
public class SPECCHIOWebClientFilter extends ClientFilter {

	/**
	 * Handle an HTTP response.
	 * 
	 * @param requestIn	the request object created by the previous filter
	 * 
	 * @return a response object
	 * 
	 * @throws ClientHandlerException
	 */
	@Override
	public ClientResponse handle(ClientRequest requestIn) throws ClientHandlerException {
		
		
		// pass the request to the next filter in the chain
		ClientResponse responseIn = getNext().handle(requestIn);
		ClientResponse response = responseIn;
		
		MultivaluedMap<String, String> headers = responseIn.getHeaders();
		if (responseIn.getType() == null) {
			// missing content type; force XML
			headers.putSingle(HttpHeaders.CONTENT_TYPE, "application/xml");
		}
		
		return response;
	}

}
