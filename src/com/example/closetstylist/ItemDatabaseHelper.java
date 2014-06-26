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
	private static final String TABLE_USER_PROFILE = "userProfile_db";
	private static final String WHERE_USER_PROFILE_CLAUSE = Schema.UserProfile.Cols.ID + " = ? ";
	
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
			R.drawable.tshirt_yellow_solid,
			R.drawable.jean_light_blue_solid,
			R.drawable.pant_brown_solid,
			R.drawable.pant_white_solid,
			R.drawable.jacket_gray_blue_stripe,
			R.drawable.jacket_green_solid,
			R.drawable.jacket_yellow_solid,
			R.drawable.short_blue_white_stripe,
			R.drawable.short_brown_solid,
			R.drawable.polo_green_solid,
			R.drawable.polo_light_blue_solid,
			R.drawable.shirt_blue_solid,
			R.drawable.shirt_blue_white_stripe,
			R.drawable.shirt_brown_stripe,
			R.drawable.shirt_white_blue_stripe
			};
	private static final ItemData[] PREDEFINED_ITEMS 
	= {
		// String imageLink (null), String color, int tempMin, int tempMax, String category, String cropImageLink (null)
		new ItemData.ItemDataBuilder(null, "Blue", 15, 100, "Bottom", null).name("Banana jean").description("Banana jean").brand("Banana").age(1).material("Denim").style("Jeans").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Blue", 40, 120, "Top", null).name("Express polo").description("Express polo").brand("Express").age(2).material("Cotton or Cotton Blend").style("Polo").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "MulticolorOrPattern", 70, 120, "Bottom", null).name("DKNY short").description("DKNY short").brand("DKNY").age(1).material("Polyester").style("Shorts").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Green", 70, 120, "Bottom", null).name("FrenchConnection short").description("FrenchConnection short").brand("FrenchConnection").age(3).material("Cotton or Cotton Blend").style("Shorts").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "MulticolorOrPattern", 70, 120, "Top", null).name("AE t-shirt").description("AE t-shirt").brand("AE").age(2).material("Cotton or Cotton Blend").style("T-Shirt - Short Sleeve").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Yellow", 70, 120, "Top",null).name("JCrew t-shirt").description("JCrew t-shirt").brand("JCrew").age(1).material("Cotton or Cotton Blend").style("T-Shirt - Short Sleeve").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Blue", 15, 100, "Bottom", null).name("Arizona jean").description("Arizona jean").brand("Arizona").age(5).material("Denim").style("Jeans").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Brown", 15, 100, "Bottom", null).name("Aeropostale pants").description("Aeropostale pants").brand("Aeropostale").age(4).material("Cotton or Cotton Blend").style("Pants").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Beige", 15, 100, "Bottom", null).name("Dockers pants").description("Dockers pants").brand("Dockers").age(3).material("Cotton or Cotton Blend").style("Pants").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "MulticolorOrPattern", 15, 100, "Top", null).name("Adidas jacket").description("Adidas jacket").brand("Adidas").age(1).material("Polyester").style("Coat and Jacket - Light").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Green", 15, 100, "Top", null).name("AE jacket").description("AE jacket").brand("AE").age(1).material("Nylon").style("Coat and Jacket - Light").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Yellow", 15, 100, "Top", null).name("Rei jacket").description("Rei jacket").brand("Rei").age(1).material("Down").style("Coat and Jacket - Heavy").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Blue", 70, 120, "Bottom", null).name("GordonCooper short").description("GordonCooper short").brand("GordonCooper").age(0).material("Polyester").style("Shorts").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Beige", 70, 120, "Bottom", null).name("AE short").description("AE short").brand("AE").age(7).material("Cotton or Cotton Blend").style("Shorts").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Green", 40, 120, "Top", null).name("Adidas t-shirt").description("Adidas t-shirt").brand("Adidas").age(0).material("Nylon").style("T-Shirt - Long Sleeve").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Blue", 40, 120, "Top", null).name("Express polo").description("Express polo").brand("Express").age(3).material("Cotton or Cotton Blend").style("Polo").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Blue", 70, 120, "Top", null).name("KennethCole shirt").description("KennethCole shirt").brand("KennethCole").age(4).material("Cotton or Cotton Blend").style("Dress Shirt").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "MulticolorOrPattern", 70, 120, "Top", null).name("RalphLauren shirt").description("RalphLauren shirt").brand("RalphLauren").age(9).material("Cotton or Cotton Blend").style("Dress Shirt").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "MulticolorOrPattern", 70, 120, "Top", null).name("Diesel shirt").description("Diesel shirt").brand("Diesel").age(7).material("Cotton or Cotton Blend").style("Casual Button Down Shirt").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "MulticolorOrPattern", 70, 120, "Top", null).name("AE shirt").description("AE shirt").brand("AE").age(6).material("Cotton or Cotton Blend").style("Casual Button Down Shirt").dirty(false).wornTime(0).maxWornTime(1).build()
	};
	
	final private Context mContext; // used to mContext.deleteDatabase(DATABASE_NAME);
	private SQLiteDatabase database;
	private ItemDataOpenHelper mItemDataOpenHelper;
	
	public ItemDatabaseHelper(Context context) {
		mContext = context;
		mItemDataOpenHelper = new ItemDataOpenHelper(context);
		database = mItemDataOpenHelper.getWritableDatabase();
	}
	
	public void saveUserProfileRecord(UserProfile usr) {
		ContentValues c = new ContentValues();
		c.put(Schema.UserProfile.Cols.USR, usr.getUsr());
		c.put(Schema.UserProfile.Cols.PWD, usr.getPwd());
		c.put(Schema.UserProfile.Cols.SEX, usr.getSex());
		c.put(Schema.UserProfile.Cols.ZIP, usr.getZip());
		c.put(Schema.UserProfile.Cols.LAUNDRY_SCHEDULE, usr.getLaundrySchedule());
		c.put(Schema.UserProfile.Cols.LAUNDRY_DAY, usr.getLaundryDay());
		database.insert(TABLE_USER_PROFILE, null, c);
	}

	public void deleteUserProfileRecord(UserProfile usr) {
		String[] whereArgs = { String.valueOf(usr.getId()) };
		database.delete(TABLE_USER_PROFILE, WHERE_USER_PROFILE_CLAUSE, whereArgs);
	}

	/*
	 * param UserProfile must have a valid id field 
	 */
	public void updateUserProfileRecord(UserProfile usr) {
		Log.i(LOG_TAG, "updateRecord" + usr.toString());
		String[] whereArgs = { String.valueOf(usr.getId()) };
		Log.i(LOG_TAG, "Rows updated: " + database.update(TABLE_USER_PROFILE, 
				getContentValuesFromUserProfile(usr), WHERE_CLAUSE, whereArgs));
	}

	public Cursor getCursorToAllUserProfileRecord() {
		return database.rawQuery(
				"SELECT * FROM " + TABLE_USER_PROFILE, 
				null);
	}
	
	/*
	 * Get the first UserProfile from the passed in cursor.
	 */
	public static UserProfile getUserProfileFromCursor(Cursor cursor) {
		// Dump all the rows pointed to by cursor Log.i(LOG_TAG, DatabaseUtils.dumpCursorToString(cursor));
		long rowID = cursor.getLong(cursor
				.getColumnIndex(Schema.UserProfile.Cols.ID));
		String usr = cursor.getString(cursor.getColumnIndex(Schema.UserProfile.Cols.USR));
		String pwd = cursor.getString(cursor.getColumnIndex(Schema.UserProfile.Cols.PWD));
		String sex = cursor.getString(cursor.getColumnIndex(Schema.UserProfile.Cols.SEX));
		int zip = cursor.getInt(cursor.getColumnIndex(Schema.UserProfile.Cols.ZIP));
		int laundrySchedule = cursor.getInt(cursor.getColumnIndex(Schema.UserProfile.Cols.LAUNDRY_SCHEDULE));
		String laundryDay = cursor.getString(cursor.getColumnIndex(Schema.UserProfile.Cols.LAUNDRY_DAY));
		return new UserProfile.UserProfileBuilder(usr, pwd, sex, zip)
			.id(rowID)
			.laundrySchedule(laundrySchedule)
			.laundryDay(laundryDay)
			.build();
	}

	/*
	 * valid id field in ItemData
	 */
	public static ContentValues getContentValuesFromUserProfile(UserProfile usr) {
		ContentValues c = new ContentValues();
		c.put(Schema.UserProfile.Cols.USR, usr.getUsr());
		c.put(Schema.UserProfile.Cols.PWD, usr.getPwd());
		c.put(Schema.UserProfile.Cols.SEX, usr.getSex());
		c.put(Schema.UserProfile.Cols.ZIP, usr.getZip());
		c.put(Schema.UserProfile.Cols.LAUNDRY_SCHEDULE, usr.getLaundrySchedule());
		c.put(Schema.UserProfile.Cols.LAUNDRY_DAY, usr.getLaundryDay());
		return c;
	}

	public void deleteUserProfile() {
		database.delete(TABLE_USER_PROFILE, null, null);
	}

	/*
	 * Follow public ArrayList<StoryData> queryStoryData in MoocResolver.java
	 * Return a List of ItemData of category top
	 */
	public ArrayList<UserProfile> getAllUserProfile() {
		ArrayList<UserProfile> tops = new ArrayList<UserProfile>();
		Cursor c = getCursorToAllUserProfileRecord();
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					tops.add(getUserProfileFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return tops;
	}
	
	/*
	 * ************************************************************************
	 * ItemData
	 * ************************************************************************	 * 
	 */

	/*
	 * no id
	 */
	public void saveItemDataRecord(ItemData item) {
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
		contentValues.put(Schema.Item.Cols.STYLE, item.getStyle());
		contentValues.put(Schema.Item.Cols.DIRTY, item.getDirty().toString());
		contentValues.put(Schema.Item.Cols.WORN_TIME, item.getWornTime());
		contentValues.put(Schema.Item.Cols.MAX_WORN_TIME, item.getMaxWornTime());
		database.insert(TABLE_NAME, null, contentValues);
	}

	public void deleteItemDataRecord(ItemData item) {
		String[] whereArgs = { String.valueOf(item.getId()) };
		database.delete(TABLE_NAME, WHERE_CLAUSE, whereArgs);
	}

	/*
	 * param ItemData must have a valid id field 
	 */
	public void updateItemDataRecord(ItemData item) {
		// Update
		Log.i(LOG_TAG, "updateRecord" + item.toString());
		String[] whereArgs = { String.valueOf(item.getId()) };
		Log.i(LOG_TAG, "Rows updated: " + database.update(TABLE_NAME, 
				getContentValuesFromItemData(item), WHERE_CLAUSE, whereArgs));
		
		// Verify
		Cursor c = queryItemFromId(item.getId());
		ArrayList<ItemData> it = getItemDataArrayListFromCursor(c);
		if (it.size() > 0) {
			Log.i(LOG_TAG, "updateRecord" + it.get(0).toString());	
		}
	}
	
	/*
	 * Query "top" items in the item database
	 */
	public Cursor queryItemFromId(long id) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_NAME);
		qb.appendWhere(Schema.Item.Cols.ID + " IN (?)");
		String[] whereArgs = {String.valueOf(id)};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}

	/*
	 * Query dirty items in the item database
	 */
	public Cursor queryDirtyItem(boolean dirty) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_NAME);
		qb.appendWhere(Schema.Item.Cols.DIRTY + " IN (?)");
		String[] whereArgs = {String.valueOf(dirty)};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}

	public Cursor getCursorToAllItemDataRecord() {
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
	
	public void deleteMyCloset() {
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
		String style = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.STYLE));
		String dirty = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.DIRTY));
		int wornTime = cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.WORN_TIME));
		int maxWornTime = cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.MAX_WORN_TIME));
		/* Debug Log to dump all the fields read back from this row
		Log.i(LOG_TAG, "The new ItemData: " + " id - " + rowID + " name - " + name + "; description - "
				+ description + "; iamgeLink - " + imageLink + "; color - "
				+ color + "; tempMin - " + Integer.toString(tempMin)
				+ "; tempMax - " + Integer.toString(tempMax) + "; category - "
				+ category + "; brand - " + brand + "; age - " + age
				+ "; material - " + material + "; cropImageLink " + cropImageLink + "; style - "
				+ style + "; dirty - " + dirty + "; wornTime - "
				+ wornTime + "; maxWornTime - " + maxWornTime + "; List wornHistory - "
				+ wornHistory.toString() );
		*/
		return new ItemData.ItemDataBuilder(imageLink, color, tempMin, tempMax, category, cropImageLink)
			.id(rowID)	
			.age(age)			
			.description(description)
			.material(material)
			.name(name)
			.brand(brand)
			.style(style)
			.dirty(Boolean.parseBoolean(dirty))
			.wornTime(wornTime)
			.maxWornTime(maxWornTime)
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
		contentValues.put(Schema.Item.Cols.STYLE, item.getStyle());
		contentValues.put(Schema.Item.Cols.DIRTY, item.getDirty().toString());
		contentValues.put(Schema.Item.Cols.WORN_TIME, item.getWornTime());
		contentValues.put(Schema.Item.Cols.MAX_WORN_TIME, item.getMaxWornTime());
		return contentValues;
	}

	/*
	 * Query "top" items in the item database
	 */
	public Cursor queryTop() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_NAME);
		//qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?,?,?)");
		//String[] whereArgs = {"jacket", "shirt", "t-shirt"};
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?)");
		String[] whereArgs = {"Top"};
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
		//qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?,?)");
		//String[] whereArgs = {"jeans", "short"};
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?)");
		String[] whereArgs = {"Bottom"};
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
			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_USER_PROFILE);
			db.execSQL("CREATE TABLE " + TABLE_USER_PROFILE + "("
					+ Schema.UserProfile.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.UserProfile.Cols.USR + " TEXT, "
					+ Schema.UserProfile.Cols.PWD + " TEXT, "
					+ Schema.UserProfile.Cols.SEX + " TEXT, "
					+ Schema.UserProfile.Cols.ZIP + " INTEGER, "
					+ Schema.UserProfile.Cols.LAUNDRY_SCHEDULE + " INTEGER, "
					+ Schema.UserProfile.Cols.LAUNDRY_DAY + " TEXT)");
			
			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_NAME);
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
					+ Schema.Item.Cols.CROP_IMAGE_LINK + " TEXT, "
					+ Schema.Item.Cols.STYLE + " TEXT, "
					+ Schema.Item.Cols.DIRTY + " TEXT, "
					+ Schema.Item.Cols.WORN_TIME + " INTEGER, "
					+ Schema.Item.Cols.MAX_WORN_TIME + " INTEGER)");
			
			Log.i(LOG_TAG, "DONE CREATE TABLE");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + "");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PROFILE + "");
			onCreate(database);
		}
	}
	
	/*
	 * Create a default database based on the preloaded images in the app.
	 */
	public void createDefaultDatabase() {
		// ItemData database
		for (int i = 0; i < PREDEFINED_RESID.length; i++) {
			PREDEFINED_ITEMS[i].setImageLink(
					getUriFromResource(PREDEFINED_RESID[i]).toString());
			PREDEFINED_ITEMS[i].setCropImageLink(
					getUriFromResource(PREDEFINED_RESID[i]).toString());
			saveItemDataRecord(PREDEFINED_ITEMS[i]);
		}
		
		// UserProfile database
		UserProfile.UserProfileBuilder usrBuilder 
				= new UserProfile.UserProfileBuilder("anh", "pwd", "M", 78758)
						.laundrySchedule(0)
						.laundryDay("Saturday");
		saveUserProfileRecord(usrBuilder.build());		
	}
	
	public void deleteDatabase() {
		deleteMyCloset();
		deleteUserProfile();
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
