package ch.specchio.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;


/**
 * This class describes an update to metadata, for use with the metadata services.
 */
@XmlRootElement(name="metadata_update")
@XmlSeeAlso({SerialisableBufferedImage.class, ArrayListWrapper.class})
public class MetadataUpdateDescriptor {
	
	/** the new metadata */
	private MetaParameter mp;
	
	/** the identifiers of the objects to be updated */
	private Integer[] ids;
	
	/** the metadata to be replaced (if any) */
	private MetaParameter mp_old;
	
	
	/**
	 * Constructor.
	 * 
	 * @param mp		the new metadata
	 * @param ids		the identifiers to be updated
	 * @param mp_old	the metadata to be replaced
	 */
	public MetadataUpdateDescriptor(MetaParameter mp, Integer ids[], MetaParameter mp_old) {
		
		this.ids = ids;
		this.mp = mp;
		this.mp_old = mp_old;
		
	}
	
	
	/**
	 * Default constructor.
	 */
	public MetadataUpdateDescriptor() {
		
		this(null, new Integer[0], null);
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param mp	the new metadata
	 */
	public MetadataUpdateDescriptor(MetaParameter mp) {
		
		this(mp, new Integer[0], null);
		
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param mp	the new metadata
	 * @params ids	the identifiers to be updated
	 * @param mp_old	the metadata to be replaced
	 */
	public MetadataUpdateDescriptor(MetaParameter mp, List<Integer> ids, MetaParameter mp_old) {
		
		this(mp, ids.toArray(new Integer[1]), mp_old);
		
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param mp	the new metadata
	 * @params ids	the identifiers to be updated
	 */
	public MetadataUpdateDescriptor(MetaParameter mp, List<Integer> ids) {
		
		this(mp, ids, null);
		
	}
	
	
	@XmlElement(name="mp")
	public MetaParameter getMetaParameter() { return this.mp; }
	public void setMetaParameter(MetaParameter mp) { this.mp = mp; }
	
	@XmlElement(name="ids")
	public Integer[] getIds() { return this.ids; }
	public void setIds(Integer[] ids) { this.ids = ids; }
	public ArrayList<Integer> getIdsAsList()
	{
		ArrayList<Integer> id_list = new ArrayList<Integer>(ids.length);
		for (Integer frame_id : ids) {
			id_list.add(frame_id);
		}	
		return id_list;
	}
	
	@XmlElement(name="mp_old")
	public MetaParameter getOldMetaParameter() { return this.mp_old; }
	public void setOldMetaParameter(MetaParameter mp_old) { this.mp_old = mp_old; }
	public boolean hasOldMetaParameter() { return mp_old != null; }
	
}
