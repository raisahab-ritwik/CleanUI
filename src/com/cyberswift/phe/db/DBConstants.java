package com.cyberswift.phe.db;

public interface DBConstants {

	public static final int DB_VERSION = 1;

	public static final String DB_NAME = "PHEApollo.db";
//	public static final String DB_NAME = Environment.getExternalStorageDirectory() + "/IMIS.db";

	final String CREATE_TABLE_BASE = "CREATE TABLE IF NOT EXISTS ";

	final String ON = " ON ";

	final String PRIMARY_KEY = " PRIMARY KEY";

	final String INTEGER = " Integer";

	final String TEXT = " TEXT";

	final String DATE_TIME = " DATETIME";

	final String BLOB = " BLOB";

	final String AUTO_INCREMENT = " AUTOINCREMENT";

	final String UNIQUE = "UNIQUE";

	final String START_COLUMN = " ( ";

	final String FINISH_COLUMN = " ) ";

	final String COMMA = ",";

	final String ON_CONFLICT_REPLACE = "ON CONFLICT REPLACE";
	
	
	public static final String _ID = "_id";
	
	// ALL STATE LIST Table
	public static final String STATE_LIST_TABLE = " stateListTable";
	public static final String STATE_ID = "stateID";
	public static final String STATE_NAME = "stateName";
	
	// ALL DISTRICT LIST Table
	public static final String DISTRICT_LIST_TABLE = " districtListTable";
	public static final String DISTRICT_ID = "districtID";
	public static final String DISTRICT_NAME = "districtName";
	public static final String DISTRICT_PARENT_STATE_ID = "districtParentStateID";
	
	// ALL BLOCK LIST Table
	public static final String BLOCK_LIST_TABLE = " blockListTable";
	public static final String BLOCK_ID = "blockID";
	public static final String BLOCK_NAME = "blockName";
	public static final String BLOCK_PARENT_DISTRICT_ID = "blockParentDistrictID";
	
	// ALL PANCHAYAT LIST Table
	public static final String PANCHAYAT_LIST_TABLE = " panchayatListTable";
	public static final String PANCHAYAT_ID = "panchayatID";
	public static final String PANCHAYAT_NAME = "panchayatName";
	public static final String PANCHAYAT_PARENT_BLOCK_ID = "panchayatParentBlockID";
	
	// ALL VILLAGE LIST Table
	public static final String VILLAGE_LIST_TABLE = " villageListTable";
	public static final String VILLAGE_ID = "villageID";
	public static final String VILLAGE_NAME = "villageName";
	public static final String VILLAGE_PARENT_PANCHAYAT_ID = "villageParentPanchayatID";
	
	// ALL HABITATION LIST Table
	public static final String HABITATION_LIST_TABLE = " habitationListTable";
	public static final String HABITATION_ID = "habitationID";
	public static final String HABITATION_NAME = "habitationName";
	
	// ALL SCHOOL LIST Table
	public static final String SCHOOL_LIST_TABLE = " schoolListTable";
	public static final String SCHOOL_ID = "schoolID";
	public static final String SCHOOL_NAME = "schoolName";
	public static final String SCHOOL_PARENT_VILLAGE_ID = "schoolParentVillageID";
	public static final String SCHOOL_CLASSIFICATION = "schoolClassification";
	public static final String SCHOOL_CATEGORY = "schoolCategory";
	public static final String SCHOOL_NO_OF_STUDENT = "noOfStudent";
	public static final String SCHOOL_PARENT_BLOCK_ID = "schoolParentBlockID";
	public static final String SCHOOL_PARENT_PANCHAYAT_ID = "schoolParentPanchayatID";
	public static final String SCHOOL_PARENT_DISTRICT_ID = "schoolParentDistrictID";
	public static final String SCHOOL_PARENT_HABITATION_ID = "schoolHabitationID";
	public static final String SCHOOL_LAT = "schoolLat";
	public static final String SCHOOL_LON = "schoolLon";
	public static final String SCHOOL_FACILITY_DRINKING_WATER = "schoolFacilityDrinkingWater";
	public static final String SCHOOL_FACILITY_SANITATION = "schoolFacilitySanitation";
}
