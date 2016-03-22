package com.cyberswift.phe.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyberswift.phe.dto.Nodes;

public class HabitationDataDB implements DBConstants {

	private static HabitationDataDB obj = null;

	public synchronized static HabitationDataDB obj() {

		if (obj == null)
			obj = new HabitationDataDB();
		return obj;

	}

	@SuppressWarnings("finally")
	public Boolean saveHabitationData(Context context, ContentValues cv) {
		System.out.println(" ----------  ADD HABITATION DATA IN FORM DATA TABLE  --------- ");
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(context).getWritableDatabase();
		mdb.beginTransaction();
		try {
			mdb.insert(HABITATION_LIST_TABLE, null, cv);
			mdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			mdb.endTransaction();
			return true;
		}
	}
	
	public ArrayList<Nodes> getHabitationList(Context mContext) {
		
		ArrayList<Nodes> habitationList = new ArrayList<Nodes>();
//		String[] columns = { _ID, HABITATION_ID, HABITATION_NAME };
		
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(mContext).getReadableDatabase();
		Cursor cur = mdb.query(HABITATION_LIST_TABLE, null, null, null, null, null, null);

		if (!isDatabaseEmpty(cur)) {
			// When there exists form data
			try {
				if (cur.moveToFirst()) {
					Nodes habitationObj;
					do {
						habitationObj = new Nodes();
						habitationObj.setNodeId(cur.getString(cur.getColumnIndex(HABITATION_ID)));
						habitationObj.setNodeName(cur.getString(cur.getColumnIndex(HABITATION_NAME)));
						
						habitationList.add(habitationObj);
					} while (cur.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(habitationList, new NodesNameComparator());
		
		return habitationList;
	}
	
	public class NodesNameComparator implements Comparator<Nodes> {
		public int compare(Nodes left, Nodes right) {
			return left.getNodeName().compareTo(right.getNodeName());
		}
	}
	
	public void deleteTableData(Context context) {
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(context).getWritableDatabase();
		mdb.delete(HABITATION_LIST_TABLE, null, null);
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
