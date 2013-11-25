package ch.specchio.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SpecchioMessage")
public class SpecchioMessage {
	
	public static String INFO = "INFO";
	public static String WARNING = "WARNING";
	public static String ERROR = "ERROR";
	
	private String message;
	private String type;
	
	public SpecchioMessage()
	{		
	}
	
	public SpecchioMessage(String message, String type)
	{		
		this.message = message;
		this.type = type;
	}

	@XmlElement(name="message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@XmlElement(name="type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public String toString() {
		return type + ": " + message;
	}
	
	
}
