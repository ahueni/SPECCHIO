package ch.specchio.eav_db;

import java.sql.SQLException;

import ch.specchio.types.Metadata;

public abstract class PrimaryData {
	
	protected EAVDBServices EAVDB;
	
	public PrimaryData(EAVDBServices EAVDB)
	{
		this.EAVDB = EAVDB;
	}
	

	public abstract int get_id();
	
	
	public abstract Metadata get_metadata();


	public abstract int insert_into_db() throws SQLException;
	
	

}
