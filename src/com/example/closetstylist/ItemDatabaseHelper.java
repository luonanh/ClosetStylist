package com.example.closetstylist;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;
import android.util.Log;

public class ItemDatabaseHelper {
	private final static String LOG_TAG = ItemDatabaseHelper.class.getCanonicalName();
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "closetStylist.db";
	private static final String TABLE_NAME = "itemData_db";
	private static final String WHERE_CLAUSE = Schema.Item.Cols.ID + " = ? ";
	
	final private Context mContext; // used to mContext.deleteDatabase(DATABASE_NAME);
	private SQLiteDatabase database;
	private ItemDataOpenHelper mItemDataOpenHelper;
	
	public ItemDatabaseHelper(Context context) {
		mContext = context;
		mItemDataOpenHelper = new ItemDataOpenHelper(context);
		database = mItemDataOpenHelper.getWritableDatabase();
	}
	
	/*
	 * no id
	 */
	public void saveRecord(ItemData item) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Schema.Item.Cols.NAME, item.getName());
		contentValues.put(Schema.Item.Cols.DESCRIPTION, item.getDescription());
		contentValues.put(Schema.Item.Cols.IMAGE_LINK, item.getImageLink());
		contentValues.put(Schema.Item.Cols.COLOR, item.getColor());
		contentValues.put(Schema.Item.Cols.TEMPERATUTRE_MIN, item.getTempMin());
		contentValues.put(Schema.Item.Cols.TEMPERATUTRE_MAX, item.getTempMax());
		contentValues.put(Schema.Item.Cols.CATEGORY, item.getCategory());
		contentValues.put(Schema.Item.Cols.BRAND, item.getBrand());
		contentValues.put(Schema.Item.Cols.AGE, item.getAge());
		contentValues.put(Schema.Item.Cols.MATERIAL, item.getMaterial());
		contentValues.put(Schema.Item.Cols.CROP_IMAGE_LINK, item.getCropImageLink());
		database.insert(TABLE_NAME, null, contentValues);
	}

	public void deleteRecord(ItemData item) {
		String[] whereArgs = { String.valueOf(item.getId()) };
		database.delete(TABLE_NAME, WHERE_CLAUSE, whereArgs);
	}

	/*
	 * param ItemData must have a valid id field 
	 */
	public void updateRecord(ItemData item) {
		Log.i(LOG_TAG, "updateRecord" + item.toString());
		String[] whereArgs = { String.valueOf(item.getId()) };
		Log.i(LOG_TAG, "Rows updated: " + database.update(TABLE_NAME, 
				getContentValuesFromItemData(item), WHERE_CLAUSE, whereArgs));
	}

	public Cursor getAllItemRecords() {
		return database.rawQuery(
				"SELECT * FROM " + TABLE_NAME, 
				null);
	}
	
	/*
	 * Get all of the ItemData from the passed in cursor.
	 */
	public static ArrayList<ItemData> getItemDataArrayListFromCursor(
			Cursor cursor) {
		ArrayList<ItemData> rValue = new ArrayList<ItemData>();
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					rValue.add(getItemDataFromCursor(cursor));
				} while (cursor.moveToNext() == true);
			}
		}
		return rValue;

	}
	
	void deleteMyCloset() {
		// thought this was working once but not anymore mContext.deleteDatabase(TABLE_NAME);
		database.delete(TABLE_NAME, null, null);
	}
	
	/*
	 * Get the first ItemData from the passed in cursor.
	 */
	public static ItemData getItemDataFromCursor(Cursor cursor) {
		long rowID = cursor.getLong(cursor
				.getColumnIndex(Schema.Item.Cols.ID));
		String name = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.NAME));
		String description = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.DESCRIPTION));
		String imageLink = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.IMAGE_LINK));
		String color = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.COLOR));
		int tempMin = cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.TEMPERATUTRE_MIN));
		int tempMax = cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.TEMPERATUTRE_MAX));
		String category = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.CATEGORY));
		String brand = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.BRAND));
		double age = cursor.getDouble(cursor.getColumnIndex(Schema.Item.Cols.AGE));
		String material = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.MATERIAL));
		String cropImageLink = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.CROP_IMAGE_LINK));
		/*
		 * 		public ItemDataBuilder(String imageLink, String color, int tempMin, 
				int tempMax, String category) {
		 */
		return new ItemData.ItemDataBuilder(imageLink, color, tempMin, tempMax, category, cropImageLink)
			.id(rowID)	
			.age(age)			
			.description(description)
			.material(material)
			.name(name)
			.brand(brand)
			.build();
	}
	
	/*
	 * valid id field in ItemData
	 */
	public static ContentValues getContentValuesFromItemData(ItemData item) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Schema.Item.Cols.ID, item.getId());
		contentValues.put(Schema.Item.Cols.NAME, item.getName());
		contentValues.put(Schema.Item.Cols.DESCRIPTION, item.getDescription());
		contentValues.put(Schema.Item.Cols.IMAGE_LINK, item.getImageLink());
		contentValues.put(Schema.Item.Cols.COLOR, item.getColor());
		contentValues.put(Schema.Item.Cols.TEMPERATUTRE_MIN, item.getTempMin());
		contentValues.put(Schema.Item.Cols.TEMPERATUTRE_MAX, item.getTempMax());
		contentValues.put(Schema.Item.Cols.CATEGORY, item.getCategory());
		contentValues.put(Schema.Item.Cols.BRAND, item.getBrand());
		contentValues.put(Schema.Item.Cols.AGE, item.getAge());
		contentValues.put(Schema.Item.Cols.MATERIAL, item.getMaterial());
		contentValues.put(Schema.Item.Cols.CROP_IMAGE_LINK, item.getCropImageLink());
		return contentValues;
	}
	
	private class ItemDataOpenHelper extends SQLiteOpenHelper {
		public ItemDataOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + "("
					+ Schema.Item.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.Item.Cols.NAME + " TEXT, "
					+ Schema.Item.Cols.DESCRIPTION + " TEXT, "
					+ Schema.Item.Cols.IMAGE_LINK + " TEXT, "
					+ Schema.Item.Cols.COLOR + " TEXT, "
					+ Schema.Item.Cols.TEMPERATUTRE_MIN + " INTEGER, "
					+ Schema.Item.Cols.TEMPERATUTRE_MAX + " INTEGER, "
					+ Schema.Item.Cols.CATEGORY + " TEXT, "
					+ Schema.Item.Cols.BRAND + " TEXT, "
					+ Schema.Item.Cols.AGE + " REAL, "
					+ Schema.Item.Cols.MATERIAL + " TEXT, "
					+ Schema.Item.Cols.CROP_IMAGE_LINK + " TEXT)");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + "");
			onCreate(database);
		}

	}
}
