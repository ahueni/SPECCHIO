package ch.specchio.services;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
 
/** Thrown to return a 400 Bad Request response with a list of error messages in the body. */
public class BadRequestException extends WebApplicationException
{
    private static final long serialVersionUID = 1L;
  
    public BadRequestException(String error)
    {
    	super(Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_XML).entity(new GenericEntity<String>( error){}).build());
    }
    
    public BadRequestException(Exception ex)
    {
    	this(ex.getMessage());
    	setStackTrace(ex.getStackTrace());
    }
 

}