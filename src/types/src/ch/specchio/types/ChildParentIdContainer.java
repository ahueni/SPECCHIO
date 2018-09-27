package ch.specchio.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ChildParentIdContainer")
public class ChildParentIdContainer {
	
	/** child id */
	private int child_id;
	
	/** parent_id id */
	private int parent_id;
	
	/**
	 * Constructor.
	 */
	public ChildParentIdContainer()
	{
		
	}
	
	/**
	 * Constructor.
	 */
	public ChildParentIdContainer(int child_id, int parent_id)
	{
		this.child_id = child_id;
		this.parent_id = parent_id;
	}

	@XmlElement(name="child_id")
	public int getChild_id() {
		return child_id;
	}

	public void setChild_id(int child_id) {
		this.child_id = child_id;
	}

	@XmlElement(name="parent_id")
	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	
	
	

}
