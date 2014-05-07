package com.example.closetstylist;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDatabaseHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "closetStylist.db";
	private static final String TABLE_NAME = "itemData_db";
	
	final private Context mContext; // used to mContext.deleteDatabase(DATABASE_NAME);
	private SQLiteDatabase database;
	private ItemDataOpenHelper mItemDataOpenHelper;
	
	public ItemDatabaseHelper(Context context) {
		mContext = context;
		mItemDataOpenHelper = new ItemDataOpenHelper(context);
		database = mItemDataOpenHelper.getWritableDatabase();
	}
	
	public void saveRecord(ItemData item) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Schema.Item.Cols.NAME, item.getName());
		contentValues.put(Schema.Item.Cols.DESCRIPTION, item.getDescription());
		contentValues.put(Schema.Item.Cols.IMAGE_LINK, item.getImageLink());
		contentValues.put(Schema.Item.Cols.COLOR, item.getColor());
		contentValues.put(Schema.Item.Cols.TEMPERATUTRE_MIN, item.getTempMin());
		contentValues.put(Schema.Item.Cols.TEMPERATUTRE_MAX, item.getTempMax());
		contentValues.put(Schema.Item.Cols.CATEGORY, item.getCategory());
		contentValues.put(Schema.Item.Cols.AGE, item.getAge());
		contentValues.put(Schema.Item.Cols.MATERIAL, item.getMaterial());
		database.insert(TABLE_NAME, null, contentValues);
	}
	
	public Cursor getAllTimeRecords() {
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
	
	/*
	 * Get the first ItemData from the passed in cursor.
	 */
	public static ItemData getItemDataFromCursor(Cursor cursor) {
		String name = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.NAME));
		String description = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.DESCRIPTION));
		String imageLink = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.IMAGE_LINK));
		String color = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.COLOR));
		int tempMin = cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.TEMPERATUTRE_MIN));
		int tempMax = cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.TEMPERATUTRE_MAX));
		String category = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.CATEGORY));
		double age = cursor.getDouble(cursor.getColumnIndex(Schema.Item.Cols.AGE));
		String material = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.MATERIAL));
		/*
		 * 		public ItemDataBuilder(String imageLink, String color, int tempMin, 
				int tempMax, String category) {
		 */
		return new ItemData.ItemDataBuilder(imageLink, color, tempMin, tempMax, category)
			.age(age)
			.description(description)
			.material(material)
			.name(name)
			.build();
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
					+ Schema.Item.Cols.AGE + " REAL, "
					+ Schema.Item.Cols.MATERIAL + " TEXT)");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + "");
			onCreate(database);
		}

	}
}
