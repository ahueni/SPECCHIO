package ch.specchio.eav_db;

/**
 * Definitions of table and view names.
 */
public class TableNames {
	
	/** all table names */
	public static final String[] TABLES = new String[] {
		"campaign",
		"campaign_path",
		"hierarchy_level",
		"spectrum_datalink",
		"spectrum",
		"specchio_user",
		"specchio_user_group",
		"institute",
		"quality_level",
		"datalink_type",
		"target_homogeneity",
		"file_format",
		"landcover",
		"measurement_unit",
		"measurement_type",
		"illumination_source",
		"sampling_environment",
		"target_category",
		"sensor",
		"sensor_element",
		"sensor_element_type",
		"instrument",
		"calibration",
		"goniometer",
		"manufacturer",
		"instrument_x_picture",
	  	"reference_x_picture",  
		"reference",
		"reference_brand",
		"reference_type",
		"schema_info",	
		"country",
		"instrumentation_picture",
		"instrumentation_factors",
		"unit",
		"category",
		"attribute",
		"eav",
		"spectrum_x_eav",
		"hierarchy_level_x_spectrum",
		"taxonomy",
		"research_group",
		"research_group_members"
	};
	
	/** view names */
	public static final String[] VIEWS = new String[] {
		"campaign_view",
		"campaign_path_view",
		"hierarchy_level_view",
		"hierarchy_level_x_spectrum_view",
		"spectrum_datalink_view",
		"spectrum_x_eav_view",
		"eav_view",
		"research_group_view",
		"research_group_members_view"
	};
	

	/** user-updateable columns of the spectrum_view table */
	public static final String[] SPECTRUM_VIEW_COLS = new String[] {
		"spectrum_id",
		"goniometer_id",
		"target_homogeneity_id",
		"illumination_source_id",
		"sampling_environment_id",
		"measurement_type_id",
		"measurement_unit_id",
		"landcover_id",
		"measurement",
		"hierarchy_level_id",
		"sensor_id",
		"file_format_id",
		"campaign_id",
		"instrument_id",
		"reference_id",
		"required_quality_level_id",
		"quality_level_id",
	};
	
	
	/** user-updateable user information tables */
	public static final String[] USER_TABLES = new String[] {
		"specchio_user",
		"specchio_user_group",
		"institute"
	};

}
