package com.cyberswift.phe.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyberswift.phe.dto.Nodes;

public class BlockDataDB implements DBConstants {

	private static BlockDataDB obj = null;

	public synchronized static BlockDataDB obj() {

		if (obj == null)
			obj = new BlockDataDB();
		return obj;

	}

	@SuppressWarnings("finally")
	public Boolean saveBlockData(Context context, ContentValues cv) {
		System.out.println(" ----------  ADD BLOCK DATA IN FORM DATA TABLE  --------- ");
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(context).getWritableDatabase();
		mdb.beginTransaction();
		try {
			mdb.insert(BLOCK_LIST_TABLE, null, cv);
			mdb.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			mdb.endTransaction();
			return true;
		}
	}
	
	public ArrayList<Nodes> getBlockList(Context mContext, String District_Id) {
		
		ArrayList<Nodes> blockList = new ArrayList<Nodes>();
		String[] columns = { _ID, BLOCK_ID, BLOCK_NAME, BLOCK_PARENT_DISTRICT_ID };
		
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(mContext).getReadableDatabase();
		Cursor cur = mdb.query(BLOCK_LIST_TABLE, columns, BLOCK_PARENT_DISTRICT_ID + "=?",
				new String[] { District_Id }, null, null, null);

		if (!isDatabaseEmpty(cur)) {
			// When there exists form data
			try {
				if (cur.moveToFirst()) {
					Nodes blockObj;
					do {
						blockObj = new Nodes();
						blockObj.setNodeId(cur.getString(cur.getColumnIndex(BLOCK_ID)));
						blockObj.setNodeName(cur.getString(cur.getColumnIndex(BLOCK_NAME)));
						blockObj.setParentInfo(cur.getString(cur.getColumnIndex(BLOCK_PARENT_DISTRICT_ID)));
						
						blockList.add(blockObj);
					} while (cur.moveToNext());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(blockList, new NodesNameComparator());
		
		return blockList;
	}
	
	public class NodesNameComparator implements Comparator<Nodes> {
		public int compare(Nodes left, Nodes right) {
			return left.getNodeName().compareTo(right.getNodeName());
		}
	}
	
	public void deleteTableData(Context context) {
		SQLiteDatabase mdb = PHEApolloDatabase.getInstance(context).getWritableDatabase();
		mdb.delete(BLOCK_LIST_TABLE, null, null);
	}
	
//	public String getLastBlock (Context mContext) {
//		String lastBlockID = "0";
//		
//		SQLiteDatabase mdb = IMISDatabase.getInstance(mContext).getReadableDatabase();
//		Cursor cur = mdb.rawQuery("SELECT * FROM " + BLOCK_LIST_TABLE +" ORDER BY column DESC LIMIT 1;", null);
//		
//		if (!isDatabaseEmpty(cur)) {
//			// When there exists form data
//			try {
//				if (cur.moveToFirst()) {
//					do {
//						lastBlockID = cur.getString(cur.getColumnIndex(BLOCK_ID));
//					} while (cur.moveToNext());
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return lastBlockID;
//	}

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
