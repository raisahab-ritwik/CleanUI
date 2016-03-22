package com.cyberswift.phe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PHEApolloDatabase extends SQLiteOpenHelper implements DBConstants {

	private static final String TAG = "PHEApolloDatabase";
	private static PHEApolloDatabase mDatabase;
	private SQLiteDatabase mDb;

	public PHEApolloDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public static final PHEApolloDatabase getInstance(Context context) {
		if (mDatabase == null) {
			mDatabase = new PHEApolloDatabase(context);
			mDatabase.getWritableDatabase();
		}
		return mDatabase;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.i(TAG, "oncreate tables");
		// create table
		String[] createStatements = getCreatetableStatements();
		int total = createStatements.length;
		for (int i = 0; i < total; i++) {
			Log.i(TAG, "executing create query " + createStatements[i]);
			Log.i("Database", "Database created");
			db.execSQL(createStatements[i]);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	private String[] getCreatetableStatements() {

		String[] create = new String[7];

		// stateListTable --> _id, stateID, stateName
		String STATE_LIST_TABLE_ST = CREATE_TABLE_BASE + STATE_LIST_TABLE + START_COLUMN + _ID + INTEGER + PRIMARY_KEY
				+ AUTO_INCREMENT + COMMA + STATE_ID + TEXT + COMMA + STATE_NAME + TEXT +COMMA + UNIQUE + START_COLUMN
				+ STATE_ID + FINISH_COLUMN + ON_CONFLICT_REPLACE + FINISH_COLUMN;
		create[0] = STATE_LIST_TABLE_ST;

		// districtListTable --> _id, districtID, districtName, districtParentStateID,
		String DISTRICT_LIST_TABLE_ST = CREATE_TABLE_BASE + DISTRICT_LIST_TABLE + START_COLUMN + _ID + INTEGER
				+ PRIMARY_KEY + AUTO_INCREMENT + COMMA + DISTRICT_ID + TEXT + COMMA + DISTRICT_NAME + TEXT + COMMA
				+ DISTRICT_PARENT_STATE_ID + TEXT + COMMA + UNIQUE + START_COLUMN + DISTRICT_ID + FINISH_COLUMN
				+ ON_CONFLICT_REPLACE + FINISH_COLUMN;
		create[1] = DISTRICT_LIST_TABLE_ST;

		// blockListTable --> _id, blockID, blockName, blockParentDistrictID,
		String BLOCK_LIST_TABLE_ST = CREATE_TABLE_BASE + BLOCK_LIST_TABLE + START_COLUMN + _ID + INTEGER
				+ PRIMARY_KEY + AUTO_INCREMENT + COMMA + BLOCK_ID + TEXT + COMMA + BLOCK_NAME + TEXT + COMMA
				+ BLOCK_PARENT_DISTRICT_ID + TEXT + COMMA + UNIQUE + START_COLUMN + BLOCK_ID + FINISH_COLUMN
				+ ON_CONFLICT_REPLACE + FINISH_COLUMN;
		create[2] = BLOCK_LIST_TABLE_ST;

		// panchayatListTable --> _id, panchayatID, panchayatName, panchayatParentBlockID,
		String PANCHAYAT_LIST_TABLE_ST = CREATE_TABLE_BASE + PANCHAYAT_LIST_TABLE + START_COLUMN + _ID + INTEGER
				+ PRIMARY_KEY + AUTO_INCREMENT + COMMA + PANCHAYAT_ID + TEXT + COMMA + PANCHAYAT_NAME + TEXT + COMMA
				+ PANCHAYAT_PARENT_BLOCK_ID + TEXT + COMMA + UNIQUE + START_COLUMN + PANCHAYAT_ID + FINISH_COLUMN
				+ ON_CONFLICT_REPLACE + FINISH_COLUMN;
		create[3] = PANCHAYAT_LIST_TABLE_ST;

		// villageListTable --> _id, villageID, villageName, villageParentPanchayatID,
		String VILLAGE_LIST_TABLE_ST = CREATE_TABLE_BASE + VILLAGE_LIST_TABLE + START_COLUMN + _ID + INTEGER
				+ PRIMARY_KEY + AUTO_INCREMENT + COMMA + VILLAGE_ID + TEXT + COMMA + VILLAGE_NAME + TEXT + COMMA
				+ VILLAGE_PARENT_PANCHAYAT_ID + TEXT + COMMA + UNIQUE + START_COLUMN + VILLAGE_ID + FINISH_COLUMN
				+ ON_CONFLICT_REPLACE + FINISH_COLUMN;
		create[4] = VILLAGE_LIST_TABLE_ST;

		// habitationListTable --> _id, habitationID, habitationName,
		String HABITATION_LIST_TABLE_ST = CREATE_TABLE_BASE + HABITATION_LIST_TABLE + START_COLUMN + _ID + INTEGER
				+ PRIMARY_KEY + AUTO_INCREMENT + COMMA + HABITATION_ID + TEXT + COMMA + HABITATION_NAME + COMMA
				+ UNIQUE + START_COLUMN + HABITATION_ID + FINISH_COLUMN + ON_CONFLICT_REPLACE + FINISH_COLUMN;
		create[5] = HABITATION_LIST_TABLE_ST;

//		schoolListTable --> _id, schoolID, schoolName, schoolParentVillageID, schoolClassification, schoolCategory,
//		noOfStudent, schoolParentBlockID, schoolParentPanchayatID, schoolParentDistrictID, schoolHabitationID,
//		schoolLat, schoolLon, schoolFacilityDrinkingWater, schoolFacilitySanitation
		String SCHOOL_LIST_TABLE_ST = CREATE_TABLE_BASE + SCHOOL_LIST_TABLE + START_COLUMN + _ID + INTEGER
				+ PRIMARY_KEY + AUTO_INCREMENT + COMMA + SCHOOL_ID + INTEGER + COMMA + SCHOOL_NAME + TEXT + COMMA
				+ SCHOOL_PARENT_VILLAGE_ID + TEXT + COMMA + SCHOOL_CLASSIFICATION + TEXT + COMMA + SCHOOL_CATEGORY
				+ TEXT + COMMA + SCHOOL_NO_OF_STUDENT + INTEGER + COMMA + SCHOOL_PARENT_BLOCK_ID + TEXT + COMMA
				+ SCHOOL_PARENT_PANCHAYAT_ID + TEXT + COMMA + SCHOOL_PARENT_DISTRICT_ID + TEXT + COMMA
				+ SCHOOL_PARENT_HABITATION_ID + TEXT + COMMA + SCHOOL_LAT + TEXT + COMMA + SCHOOL_LON + TEXT + COMMA
				+ SCHOOL_FACILITY_DRINKING_WATER + TEXT + COMMA + SCHOOL_FACILITY_SANITATION + TEXT + COMMA + UNIQUE
				+ START_COLUMN + SCHOOL_ID + FINISH_COLUMN + ON_CONFLICT_REPLACE + FINISH_COLUMN;
		create[6] = SCHOOL_LIST_TABLE_ST;
		
		return create;
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {

		return mDb != null ? mDb : (mDb = super.getWritableDatabase());
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {

		return mDb != null ? mDb : (mDb = super.getReadableDatabase());
	}

	public void startmanagingcursor() {
		mDatabase.startmanagingcursor();
	}

}
