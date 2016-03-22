package com.cyberswift.phe.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyberswift.phe.dto.Nodes;

public class VillageDataDB implements DBConstants {

	private static VillageDataDB obj = null;

	public synchronized static VillageDataDB obj() {

		if (obj == null)
			obj = new VillageDataDB();
		return obj;

	}

	@SuppressWarnings("finally")
	public Boolean saveVillageData(Context context, ContentValues cv) {
		System.out.println(" ----------  ADD VILLAGE DATA IN FORM DATA TABLE  --------- ");
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(context).getWritableDatabase();
		mdb.beginTransaction();
		try {
			mdb.insert(VILLAGE_LIST_TABLE, null, cv);
			mdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			mdb.endTransaction();
			return true;
		}
	}
	
	public ArrayList<Nodes> getVillageList(Context mContext, String Cluster_Id) {
		
		ArrayList<Nodes> villageList = new ArrayList<Nodes>();
		String[] columns = { _ID, VILLAGE_ID, VILLAGE_NAME, VILLAGE_PARENT_PANCHAYAT_ID };
		
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(mContext).getReadableDatabase();
		Cursor cur = mdb.query(VILLAGE_LIST_TABLE, columns, VILLAGE_PARENT_PANCHAYAT_ID + "=?",
				new String[] { Cluster_Id }, null, null, null);

		if (!isDatabaseEmpty(cur)) {
			// When there exists form data
			try {
				if (cur.moveToFirst()) {
					Nodes villageObj;
					do {
						villageObj = new Nodes();
						villageObj.setNodeId(cur.getString(cur.getColumnIndex(VILLAGE_ID)));
						villageObj.setNodeName(cur.getString(cur.getColumnIndex(VILLAGE_NAME)));
						villageObj.setParentInfo(cur.getString(cur.getColumnIndex(VILLAGE_PARENT_PANCHAYAT_ID)));
						
						villageList.add(villageObj);
					} while (cur.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(villageList, new NodesNameComparator());
		
		return villageList;
	}
	
	public class NodesNameComparator implements Comparator<Nodes> {
		public int compare(Nodes left, Nodes right) {
			return left.getNodeName().compareTo(right.getNodeName());
		}
	}
	
	public void deleteTableData(Context context) {
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(context).getWritableDatabase();
		mdb.delete(VILLAGE_LIST_TABLE, null, null);
	}
	
	public String getLastVillage (Context mContext) {
		String lastVillageID = "0";
		
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(mContext).getReadableDatabase();
		Cursor cur = mdb.rawQuery("SELECT * FROM " + VILLAGE_LIST_TABLE + " ORDER BY "+ _ID + " DESC LIMIT 1;", null);
		
		if (!isDatabaseEmpty(cur)) {
			// When there exists form data
			try {
				if (cur.moveToFirst()) {
					do {
						lastVillageID = cur.getString(cur.getColumnIndex(VILLAGE_ID));
					} while (cur.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lastVillageID;
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
