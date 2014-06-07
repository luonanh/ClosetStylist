package com.example.closetstylist;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

public class ItemDatabaseHelper {
	private final static String LOG_TAG = ItemDatabaseHelper.class.getCanonicalName();
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "closetStylist.db";
	private static final String TABLE_NAME = "itemData_db";
	private static final String WHERE_CLAUSE = Schema.Item.Cols.ID + " = ? ";
	
	/*
	 * PREDEFINED_RESID and PREDEFINED_ITEMS must always match, otherwise, info
	 * will be wrong. If we change one later, MUST change the other one as well.
	 */
	private static final int[] PREDEFINED_RESID 
			= {
			R.drawable.jeans_blue_solid,
			R.drawable.polo_blue_solid,
			R.drawable.short_black_white_stripe,
			R.drawable.short_green_solid,
			R.drawable.tshirt_black_white_stripe,
			R.drawable.tshirt_yellow_solid
			};
	private static final ItemData[] PREDEFINED_ITEMS 
			= {
			// String imageLink (null), String color, int tempMin, int tempMax, String category, String cropImageLink (null)
			new ItemData.ItemDataBuilder(null, "blue", 15, 100, "jeans", null).name("Banana jean").description("Banana jean").brand("Banana").age(1).material("jeans").build(),
			new ItemData.ItemDataBuilder(null, "blue", 40, 120, "t-shirt", null).name("Express polo").description("Express polo").brand("Express").age(2).material("cotton").build(),
			new ItemData.ItemDataBuilder(null, "white", 70, 120, "short", null).name("DKNY short").description("DKNY short").brand("DKNY").age(1).material("cotton").build(),
			new ItemData.ItemDataBuilder(null, "green", 70, 120, "short", null).name("FrenchConnection short").description("FrenchConnection short").brand("FrenchConnection").age(3).material("cotton").build(),
			new ItemData.ItemDataBuilder(null, "black", 70, 120, "t-shirt", null).name("AE t-shirt").description("AE t-shirt").brand("AE").age(2).material("cotton").build(),
			new ItemData.ItemDataBuilder(null, "yellow", 70, 120, "t-shirt", null).name("JCrew t-shirt").description("JCrew t-shirt").brand("JCrew").age(1).material("cotton").build()
			};
	
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
		// Dump all the rows pointed to by cursor Log.i(LOG_TAG, DatabaseUtils.dumpCursorToString(cursor));
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
		/* Debug Log to dump all the fields read back from this row
		Log.i(LOG_TAG, "The new ItemData: " + " id - " + rowID + " name - " + name + "; description - "
				+ description + "; iamgeLink - " + imageLink + "; color - "
				+ color + "; tempMin - " + Integer.toString(tempMin)
				+ "; tempMax - " + Integer.toString(tempMax) + "; category - "
				+ category + "; brand - " + brand + "; age - " + age
				+ "; material - " + material + "; cropImageLink " + cropImageLink);
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

	/*
	 * Query "top" items in the item database
	 */
	public Cursor queryTop() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_NAME);
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?,?,?)");
		
		String[] whereArgs = {"jacket", "shirt", "t-shirt"}; // may change when more categories are added
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}

	/*
	 * Follow public ArrayList<StoryData> queryStoryData in MoocResolver.java
	 * Return a List of ItemData of category top
	 */
	public ArrayList<ItemData> getAllTop() {
		ArrayList<ItemData> tops = new ArrayList<ItemData>();
		Cursor c = queryTop();
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					tops.add(getItemDataFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return tops;
	}

	/*
	 * Query "bottom" items in the item database
	 */
	public Cursor queryBottom() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_NAME);
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?,?)");
		
		String[] whereArgs = {"jeans", "short"}; // may change when more categories are added
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}

	/*
	 * Follow public ArrayList<StoryData> queryStoryData in MoocResolver.java
	 * Return a List of ItemData of category bottom
	 */
	public ArrayList<ItemData> getAllBottom() {
		ArrayList<ItemData> bottoms = new ArrayList<ItemData>();
		Cursor c = queryBottom();
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					bottoms.add(getItemDataFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return bottoms;
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
	
	/*
	 * Create a default database based on the preloaded images in the app.
	 */
	public void createDefaultDatabase() {
		for (int i = 0; i < PREDEFINED_RESID.length; i++) {
			PREDEFINED_ITEMS[i].setImageLink(
					getUriFromResource(PREDEFINED_RESID[i]).toString());
			PREDEFINED_ITEMS[i].setCropImageLink(
					getUriFromResource(PREDEFINED_RESID[i]).toString());
			saveRecord(PREDEFINED_ITEMS[i]);
		}
	}
	
	/*
	 * Create Uri to assign fields in ItemData.
	 * http://stackoverflow.com/questions/4896223/how-to-get-an-uri-of-an-image-resource-in-android
	 */
	private Uri getUriFromResource(int resId) {
		Uri path = Uri.parse("android.resource://com.example.closetstylist/" + resId);
		return path;
	}
	

}
