package com.cyberswift.phe.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyberswift.phe.dto.Nodes;

public class PanchayatDataDB implements DBConstants {

	private static PanchayatDataDB obj = null;

	public synchronized static PanchayatDataDB obj() {

		if (obj == null)
			obj = new PanchayatDataDB();
		return obj;

	}

	@SuppressWarnings("finally")
	public Boolean savePanchayatData(Context context, ContentValues cv) {
		System.out.println(" ----------  ADD PANCHAYAT DATA IN FORM DATA TABLE  --------- ");
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(context).getWritableDatabase();
		mdb.beginTransaction();
		try {
			mdb.insert(PANCHAYAT_LIST_TABLE, null, cv);
			mdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			mdb.endTransaction();
			return true;
		}
	}
	
	public ArrayList<Nodes> getPanchayatList(Context mContext, String Block_Id) {
		
		ArrayList<Nodes> panchayatList = new ArrayList<Nodes>();
		String[] columns = { _ID, PANCHAYAT_ID, PANCHAYAT_NAME, PANCHAYAT_PARENT_BLOCK_ID };
		
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(mContext).getReadableDatabase();
		Cursor cur = mdb.query(PANCHAYAT_LIST_TABLE, columns, PANCHAYAT_PARENT_BLOCK_ID + "=?",
				new String[] { Block_Id }, null, null, null);

		if (!isDatabaseEmpty(cur)) {
			// When there exists form data
			try {
				if (cur.moveToFirst()) {
					Nodes panchayatObj;
					do {
						panchayatObj = new Nodes();
						panchayatObj.setNodeId(cur.getString(cur.getColumnIndex(PANCHAYAT_ID)));
						panchayatObj.setNodeName(cur.getString(cur.getColumnIndex(PANCHAYAT_NAME)));
						panchayatObj.setParentInfo(cur.getString(cur.getColumnIndex(PANCHAYAT_PARENT_BLOCK_ID)));
						
						panchayatList.add(panchayatObj);
					} while (cur.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(panchayatList, new NodesNameComparator());
		
		return panchayatList;
	}
	
	public class NodesNameComparator implements Comparator<Nodes> {
		public int compare(Nodes left, Nodes right) {
			return left.getNodeName().compareTo(right.getNodeName());
		}
	}
	
	public void deleteTableData(Context context) {
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(context).getWritableDatabase();
		mdb.delete(PANCHAYAT_LIST_TABLE, null, null);
	}
	
	public String getLastPanchayat (Context mContext) {
		String lastPanchayatID = "0";
		
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(mContext).getReadableDatabase();
		Cursor cur = mdb.rawQuery("SELECT * FROM" + PANCHAYAT_LIST_TABLE + " ORDER BY "+ _ID + " DESC LIMIT 1;", null);
		
		if (!isDatabaseEmpty(cur)) {
			// When there exists form data
			try {
				if (cur.moveToFirst()) {
					do {
						lastPanchayatID = cur.getString(cur.getColumnIndex(PANCHAYAT_ID));
					} while (cur.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lastPanchayatID;
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
