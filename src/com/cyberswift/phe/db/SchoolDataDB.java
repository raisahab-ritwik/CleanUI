package com.cyberswift.phe.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyberswift.phe.dto.FacilityClass;
import com.cyberswift.phe.dto.SchoolClass;

public class SchoolDataDB implements DBConstants {

	private static SchoolDataDB obj = null;

	public synchronized static SchoolDataDB obj() {

		if (obj == null)
			obj = new SchoolDataDB();
		return obj;

	}

	@SuppressWarnings("finally")
	public Boolean saveSchoolData(Context context, ContentValues cv) {
		System.out.println(" ----------  ADD SCHOOL DATA IN FORM DATA TABLE  --------- ");
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(context).getWritableDatabase();
		mdb.beginTransaction();
		try {
			mdb.insert(SCHOOL_LIST_TABLE, null, cv);
			mdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			mdb.endTransaction();
			return true;
		}
	}

	public ArrayList<SchoolClass> getSchoolList(Context mContext, String Village_Id) {
		
		ArrayList<SchoolClass> schoolList = new ArrayList<SchoolClass>();
		String[] columns = { _ID, SCHOOL_ID, SCHOOL_NAME, SCHOOL_PARENT_VILLAGE_ID, SCHOOL_CLASSIFICATION,
				SCHOOL_CATEGORY, SCHOOL_NO_OF_STUDENT, SCHOOL_PARENT_BLOCK_ID, SCHOOL_PARENT_PANCHAYAT_ID,
				SCHOOL_PARENT_DISTRICT_ID, SCHOOL_PARENT_HABITATION_ID, SCHOOL_LAT, SCHOOL_LON,
				SCHOOL_FACILITY_DRINKING_WATER, SCHOOL_FACILITY_SANITATION };
		
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(mContext).getReadableDatabase();
		Cursor cur = mdb.query(SCHOOL_LIST_TABLE, columns, SCHOOL_PARENT_VILLAGE_ID + "=?",
				new String[] { Village_Id }, null, null, null);

		if (!isDatabaseEmpty(cur)) {
			// When there exists form data
			try {
				if (cur.moveToFirst()) {
					SchoolClass schoolObj;
					do {
						schoolObj = new SchoolClass();
						schoolObj.setBlock_Id(cur.getString(cur.getColumnIndex(SCHOOL_PARENT_BLOCK_ID)));
						schoolObj.setCluster_Id(cur.getString(cur.getColumnIndex(SCHOOL_PARENT_PANCHAYAT_ID)));
						schoolObj.setDistrict_Id(cur.getString(cur.getColumnIndex(SCHOOL_PARENT_DISTRICT_ID)));
						schoolObj.setHabitation_Id(cur.getString(cur.getColumnIndex(SCHOOL_PARENT_HABITATION_ID)));
						schoolObj.setId(cur.getString(cur.getColumnIndex(SCHOOL_ID)));
						schoolObj.setLat(cur.getString(cur.getColumnIndex(SCHOOL_LAT)));
						schoolObj.setLon(cur.getString(cur.getColumnIndex(SCHOOL_LON)));
						schoolObj.setNo_Of_Student(cur.getString(cur.getColumnIndex(SCHOOL_NO_OF_STUDENT)));
						schoolObj.setSchool_Category(cur.getString(cur.getColumnIndex(SCHOOL_CATEGORY)));
						schoolObj.setSchool_Classification(cur.getString(cur.getColumnIndex(SCHOOL_CLASSIFICATION)));
						schoolObj.setSchool_Name(cur.getString(cur.getColumnIndex(SCHOOL_NAME)));
						schoolObj.setVillage_Id(cur.getString(cur.getColumnIndex(SCHOOL_PARENT_VILLAGE_ID)));
						ArrayList<FacilityClass> facilityList = new ArrayList<FacilityClass>();
						FacilityClass facility;
						
						facility = new FacilityClass();
						facility.setName("Drinking Water");
						facility.setTag("Facility_Drinking_Water");
						facility.setIsSelected((cur.getString(cur.getColumnIndex(SCHOOL_FACILITY_DRINKING_WATER)).equalsIgnoreCase("1"))? true : false);
						facilityList.add(facility);
						
						facility = new FacilityClass();
						facility.setName("Sanitation");
						facility.setTag("Facility_Sanitation");
						facility.setIsSelected((cur.getString(cur.getColumnIndex(SCHOOL_FACILITY_SANITATION)).equalsIgnoreCase("1"))? true : false);
						facilityList.add(facility);
						
						schoolObj.setFacilityList(facilityList);
						
						schoolList.add(schoolObj);
					} while (cur.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(schoolList, new SchoolNameComparator());
		
		return schoolList;
	}
	
	public class SchoolNameComparator implements Comparator<SchoolClass> {
		public int compare(SchoolClass left, SchoolClass right) {
			return left.getSchool_Name().compareTo(right.getSchool_Name());
		}
	}
	
	public String getLastSchool (Context mContext) {
		String lastSchoolID = "0";
		
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(mContext).getReadableDatabase();
		Cursor cur = mdb.rawQuery("SELECT * FROM " + SCHOOL_LIST_TABLE + " ORDER BY "+ _ID + " DESC LIMIT 1;", null);
		
		if (!isDatabaseEmpty(cur)) {
			// When there exists form data
			try {
				if (cur.moveToFirst()) {
					do {
						lastSchoolID = cur.getString(cur.getColumnIndex(SCHOOL_ID));
					} while (cur.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lastSchoolID;
	}
	
	public void deleteTableData(Context context) {
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(context).getWritableDatabase();
		mdb.delete(SCHOOL_LIST_TABLE, null, null);
	}
	
	private Boolean isDatabaseEmpty(Cursor mCursor) {
		if (mCursor.moveToFirst()) {
			// NOT EMPTY
			return false;
		} else {
			// IS EMPTY
			return true;
		}
	}
}
