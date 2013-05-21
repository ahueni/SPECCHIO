package ch.specchio.types;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;


/**
 * This class represents the research group that contributed to a campaign.
 */
@XmlRootElement(name="research_group")
public class ResearchGroup {
	
	/** group identifier */
	private int researchGroupId;
	
	/** group name */
	private String name;
	
	/** members */
	private ArrayList<User> members;
	
	
	/** default constructor */
	public ResearchGroup()
	{
		this("");
	}
	
	/** constructor with name one */
	public ResearchGroup(String name)
	{
		this(0, name);
	}
	
	/** constructor */
	public ResearchGroup(int researchGroupId, String name)
	{
		this.researchGroupId = researchGroupId;
		this.name = name;
		this.members = new ArrayList<User>();
	}
	
	
	@XmlElement(name="research_group_id")
	public int getId() { return this.researchGroupId; }
	public void setId(int researchGroupId) { this.researchGroupId = researchGroupId; }
	
	@XmlElement(name="name")
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	@XmlElement(name="members")
	public ArrayList<User> getMembers() { return this.members; }
	public void setMembers(ArrayList<User> members) { this.members = members; }
	public User getMember(int i) { return this.members.get(i); }
	public void addMember(User member) { this.members.add(member); }

}
