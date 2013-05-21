
package ch.specchio.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//This statement means that class "RegistryObjects.java" is the root-element of our example
@XmlRootElement
public class RegistryObjects {

	// XmlElement sets the name of the entities
	@XmlElement(name = "registryObject")
	private ArrayList<RegistryObject> registryObjectList;

	public void setRegistryObjectList(ArrayList<RegistryObject> registryObjectList) {
		this.registryObjectList = registryObjectList;
	}

	public ArrayList<RegistryObject> getRegistryObjectsList() {
		return registryObjectList;
	}

}
