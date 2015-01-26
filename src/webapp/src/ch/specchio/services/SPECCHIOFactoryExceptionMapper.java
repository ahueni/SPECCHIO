package ch.specchio.services;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import ch.specchio.factories.SPECCHIOFactoryException;

@Provider
public class SPECCHIOFactoryExceptionMapper implements ExceptionMapper<SPECCHIOFactoryException> {
    @Override
    public Response toResponse(SPECCHIOFactoryException ex) {
    	
//    	Response r = Response.status(Response.Status.BAD_REQUEST).entity("SPECCHIOFactoryException").build();
    	
    	
        ErrorMessage errorMessage = new ErrorMessage();        
        //setHttpStatus(ex, errorMessage);
        errorMessage.setCode(500);
        errorMessage.setMessage(ex.getMessage());
//        StringWriter errorStackTrace = new StringWriter();
//        ex.printStackTrace(new PrintWriter(errorStackTrace));
//        errorMessage.setDeveloperMessage(errorStackTrace.toString());
//        errorMessage.setLink(AppConstants.BLOG_POST_URL);
 
        Response r =  Response.status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                //.type(MediaType.TEXT_PLAIN)
                .build();        
    	
    	return r;
    }
}
