package com.kth.seds.database;

import java.io.IOException;
import java.util.ArrayList;

import com.kth.seds.entity.Parking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ParkingDataSource {

	public final static String TB_NAME = "Parking";
	public final static String COLUMN_ID = "Id";
	public final static String COLUMN_NAME = "name";
	public final static String COLUMN_ADDRESS = "address";
	public final static String COLUMN_LATITUDE = "latitude";
	public final static String COLUMN_LONGITUDE = "longitude";
	public final static String COLUMN_TOTAL_SPACE = "totalSpace";
	public final static String COLUMN_AVAILABLE_SPACE = "availableSpace";
	public final static String COLUMN_OPENING_HOURS = "openingHours";
	public final static String COLUMN_PRICE = "price";
	public final static String COLUMN_DESCRIPTION = "description";
	public final static String COLUMN_FAVORITE = "favorite";

	public final static int NO_SORT = 0;
	public final static int SORT_BY_PRICE = 1;
	public final static int SORT_BY_SPACE = 2;

	private DBManager dbConnection;
	private SQLiteDatabase tempDB;

	public ParkingDataSource(Context context) {
		try {
			dbConnection = new DBManager(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void open() {
		try {
			tempDB = dbConnection.getWritableDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized void close() {
		dbConnection.close();
	}

	public Cursor queryAllCursor(int sortMode) {
		if (tempDB.isOpen()) {
			Cursor c;
			if (sortMode == 1) {
				c = tempDB.query(TB_NAME, null, "_id<10"
						, null, null, null,
						COLUMN_PRICE + " ASC");
			} else if (sortMode == 2) {
				c = tempDB.query(TB_NAME, null, "_id<10", null, null, null,
						COLUMN_AVAILABLE_SPACE + " DESC");
			} else {
				c = tempDB.query(TB_NAME, null, "_id<10", null, null, null, null);
			}

			c.moveToFirst();

			return c;
		} else {
			return null;
		}
	}

	public Cursor queryCursor(String queryValue, String inputColumn) {
		if (tempDB.isOpen()) {
			Cursor c = tempDB.query(TB_NAME, null, inputColumn + "='"
					+ queryValue + "'", null, null, null, null);
			c.moveToFirst();
			return c;
		} else {
			return null;
		}
	}

	public ArrayList<Parking> getAllParking() {
		Parking pInfoMap;
		ArrayList<Parking> dataList = null;
		if (tempDB.isOpen()) {
			Cursor c = tempDB
					.query(TB_NAME, null, null, null, null, null, null);
			c.moveToFirst();
			dataList = new ArrayList<Parking>();
			do {
				pInfoMap = new Parking();
				pInfoMap.setId(c.getString(c.getColumnIndex(COLUMN_ID)));
				pInfoMap.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
				pInfoMap.setAvailableSpace(c.getInt(c
						.getColumnIndex(COLUMN_AVAILABLE_SPACE)));
				pInfoMap.setLatitude(c.getDouble(c
						.getColumnIndex(COLUMN_LATITUDE)));
				pInfoMap.setLongitude(c.getDouble(c
						.getColumnIndex(COLUMN_LONGITUDE)));
				dataList.add(pInfoMap);
			} while (c.moveToNext());
		} else {
			return null;
		}
		return dataList;
	}

	public long updateRecord(String uniqueId, ContentValues updateValue) {
		long result = -1;
		try {
			if(tempDB.isOpen()){
				result = tempDB.update(TB_NAME, updateValue, "Id=" + "'" + uniqueId
						+ "'", null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
