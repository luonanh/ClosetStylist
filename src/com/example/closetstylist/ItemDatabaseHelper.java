package com.example.closetstylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDatabaseHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "closetStylist.db";
	private static final String TABLE_NAME = "itemData_db";
	
	static final String ITEMDATA_COLUMN_ID = "_id";
	static final String ITEMDATA_COLUMN_NAME = "name";
	static final String ITEMDATA_COLUMN_DESCRIPTION = "description";
	static final String ITEMDATA_COLUMN_IMAGE_LINK = "imageLink";
	static final String ITEMDATA_COLUMN_COLOR = "color";	
	static final String ITEMDATA_COLUMN_TEMPERATUTRE_MIN = "TempMin";
	static final String ITEMDATA_COLUMN_TEMPERATUTRE_MAX = "TempMax";
	static final String ITEMDATA_COLUMN_CATEGORY = "category";
	static final String ITEMDATA_COLUMN_AGE = "age";
	static final String ITEMDATA_COLUMN_MATERIAL = "material";
	
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
		contentValues.put(ITEMDATA_COLUMN_NAME, item.getName());
		contentValues.put(ITEMDATA_COLUMN_DESCRIPTION, item.getDescription());
		contentValues.put(ITEMDATA_COLUMN_IMAGE_LINK, item.getImageLink());
		contentValues.put(ITEMDATA_COLUMN_COLOR, item.getColor());
		contentValues.put(ITEMDATA_COLUMN_TEMPERATUTRE_MIN, item.getTempMin());
		contentValues.put(ITEMDATA_COLUMN_TEMPERATUTRE_MAX, item.getTempMax());
		contentValues.put(ITEMDATA_COLUMN_CATEGORY, item.getDescription());
		contentValues.put(ITEMDATA_COLUMN_AGE, item.getAge());
		contentValues.put(ITEMDATA_COLUMN_MATERIAL, item.getMaterial());
		database.insert(TABLE_NAME, null, contentValues);
	}
	
	public Cursor getAllTimeRecords() {
		return database.rawQuery(
				"SELECT * FROM " + TABLE_NAME, 
				null);
	}
	
	private class ItemDataOpenHelper extends SQLiteOpenHelper {
		public ItemDataOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_NAME + "("
					+ ITEMDATA_COLUMN_ID + " INTEGER PRIMARY KEY, "
					+ ITEMDATA_COLUMN_NAME + " TEXT, "
					+ ITEMDATA_COLUMN_DESCRIPTION + " TEXT, "
					+ ITEMDATA_COLUMN_IMAGE_LINK + " TEXT, "
					+ ITEMDATA_COLUMN_COLOR + " TEXT, "
					+ ITEMDATA_COLUMN_TEMPERATUTRE_MIN + " INTEGER, "
					+ ITEMDATA_COLUMN_TEMPERATUTRE_MAX + " INTEGER, "
					+ ITEMDATA_COLUMN_CATEGORY + " TEXT, "
					+ ITEMDATA_COLUMN_AGE + " REAL, "
					+ ITEMDATA_COLUMN_MATERIAL + " TEXT)");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + "");
			onCreate(database);
		}

	}
}
