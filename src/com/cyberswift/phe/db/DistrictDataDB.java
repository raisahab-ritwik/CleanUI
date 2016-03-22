package com.cyberswift.phe.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyberswift.phe.dto.Nodes;

public class DistrictDataDB implements DBConstants {

	private static DistrictDataDB obj = null;

	public synchronized static DistrictDataDB obj() {

		if (obj == null)
			obj = new DistrictDataDB();
		return obj;

	}

	@SuppressWarnings("finally")
	public Boolean saveDistrictData(Context context, ContentValues cv) {
		System.out.println(" ----------  ADD DISTRICT DATA IN FORM DATA TABLE  --------- ");
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(context).getWritableDatabase();
		mdb.beginTransaction();
		try {
			mdb.insert(DISTRICT_LIST_TABLE, null, cv);
			mdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			mdb.endTransaction();
			return true;
		}
	}
	
	public ArrayList<Nodes> getDistrictList(Context mContext, String State_Id) {
		
		ArrayList<Nodes> districtList = new ArrayList<Nodes>();
		String[] columns = { _ID, DISTRICT_ID, DISTRICT_NAME, DISTRICT_PARENT_STATE_ID };
		
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(mContext).getReadableDatabase();
		Cursor cur = mdb.query(DISTRICT_LIST_TABLE, columns, DISTRICT_PARENT_STATE_ID + "=?",
				new String[] { State_Id }, null, null, null);

		if (!isDatabaseEmpty(cur)) {
			// When there exists form data
			try {
				if (cur.moveToFirst()) {
					Nodes districtObj;
					do {
						districtObj = new Nodes();
						districtObj.setNodeId(cur.getString(cur.getColumnIndex(DISTRICT_ID)));
						districtObj.setNodeName(cur.getString(cur.getColumnIndex(DISTRICT_NAME)));
						districtObj.setParentInfo(cur.getString(cur.getColumnIndex(DISTRICT_PARENT_STATE_ID)));
						
						districtList.add(districtObj);
					} while (cur.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(districtList, new NodesNameComparator());
		
		return districtList;
	}
	
	public class NodesNameComparator implements Comparator<Nodes> {
		public int compare(Nodes left, Nodes right) {
			return left.getNodeName().compareTo(right.getNodeName());
		}
	}
	
	public void deleteTableData(Context context) {
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(context).getWritableDatabase();
		mdb.delete(DISTRICT_LIST_TABLE, null, null);
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
