package com.example.closetstylist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	private static final String TABLE_OCCASION_MATCHING_MALE = "occasionMatchingMale_db";
	private static final String TABLE_OCCASION_MATCHING_FEMALE = "occasionMatchingFemale_db";
	private static final String TABLE_PAIR_MATCHING_MALE = "pairMatchingMale_db";
	private static final String TABLE_PAIR_MATCHING_FEMALE = "pairMatchingFemale_db";
	private static final String TABLE_COLOR_MATCHING_DEFAULT = "colorMatchingDefault_db";
	
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
		new ItemData.ItemDataBuilder(null, "Blue", 40, 120, "Top", null).name("Express polo").description("Express polo").brand("Express").age(2).material("Cotton_Or_Cotton_Blend").style("Polo").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Multicolor_Pattern", 70, 120, "Bottom", null).name("DKNY short").description("DKNY short").brand("DKNY").age(1).material("Polyester").style("Shorts").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Green", 70, 120, "Bottom", null).name("FrenchConnection short").description("FrenchConnection short").brand("FrenchConnection").age(3).material("Cotton_Or_Cotton_Blend").style("Shorts").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Multicolor_Pattern", 70, 120, "Top", null).name("AE t-shirt").description("AE t-shirt").brand("AE").age(2).material("Cotton_Or_Cotton_Blend").style("T-Shirt_Short_Sleeve").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Yellow", 70, 120, "Top",null).name("JCrew t-shirt").description("JCrew t-shirt").brand("JCrew").age(1).material("Cotton_Or_Cotton_Blend").style("T-Shirt_Short_Sleeve").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Blue", 15, 100, "Bottom", null).name("Arizona jean").description("Arizona jean").brand("Arizona").age(5).material("Denim").style("Jeans").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Brown", 15, 100, "Bottom", null).name("Aeropostale pants").description("Aeropostale pants").brand("Aeropostale").age(4).material("Cotton_Or_Cotton_Blend").style("Pants").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Beige", 15, 100, "Bottom", null).name("Dockers pants").description("Dockers pants").brand("Dockers").age(3).material("Cotton_Or_Cotton_Blend").style("Pants").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Multicolor_Pattern", 15, 100, "Top", null).name("Adidas jacket").description("Adidas jacket").brand("Adidas").age(1).material("Polyester").style("Coat_And_Jacket_Light").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Green", 15, 100, "Top", null).name("AE jacket").description("AE jacket").brand("AE").age(1).material("Nylon").style("Coat_And_Jacket_Light").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Yellow", 15, 100, "Top", null).name("Rei jacket").description("Rei jacket").brand("Rei").age(1).material("Down").style("Coat_And_Jacket_Heavy").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Blue", 70, 120, "Bottom", null).name("GordonCooper short").description("GordonCooper short").brand("GordonCooper").age(0).material("Polyester").style("Shorts").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Beige", 70, 120, "Bottom", null).name("AE short").description("AE short").brand("AE").age(7).material("Cotton_Or_Cotton_Blend").style("Shorts").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Green", 40, 120, "Top", null).name("Adidas t-shirt").description("Adidas t-shirt").brand("Adidas").age(0).material("Nylon").style("T-Shirt_Long_Sleeve").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Blue", 40, 120, "Top", null).name("Express polo").description("Express polo").brand("Express").age(3).material("Cotton_Or_Cotton_Blend").style("Polo").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Blue", 70, 120, "Top", null).name("KennethCole shirt").description("KennethCole shirt").brand("KennethCole").age(4).material("Cotton_Or_Cotton_Blend").style("Dress_Shirt").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Multicolor_Pattern", 70, 120, "Top", null).name("RalphLauren shirt").description("RalphLauren shirt").brand("RalphLauren").age(9).material("Cotton_Or_Cotton_Blend").style("Dress_Shirt").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Multicolor_Pattern", 70, 120, "Top", null).name("Diesel shirt").description("Diesel shirt").brand("Diesel").age(7).material("Cotton_Or_Cotton_Blend").style("Casual_Button_Down_Shirt").dirty(false).wornTime(0).maxWornTime(1).build(),
		new ItemData.ItemDataBuilder(null, "Multicolor_Pattern", 70, 120, "Top", null).name("AE shirt").description("AE shirt").brand("AE").age(6).material("Cotton_Or_Cotton_Blend").style("Casual_Button_Down_Shirt").dirty(false).wornTime(0).maxWornTime(1).build()
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
	 * ************************************************************************
	 * OccasionMatchingRecord
	 * ************************************************************************	 * 
	 */
	// As of 7/1/2014, occasionMatchingRecordTable can be either
	// TABLE_OCCASION_MATCHING_MALE or TABLE_OCCASION_MATCHING_FEMALE
	public void saveOccasionMatchingRecord(OccasionMatchingRecord omr, String occasionMatchingRecordTable) {
		ContentValues c = new ContentValues();
		c.put(Schema.OccasionMatching.Cols.CATEGORY, omr.getCategory());
		c.put(Schema.OccasionMatching.Cols.STYLE, omr.getStyle());
		c.put(Schema.OccasionMatching.Cols.FORMAL, omr.getFormal());
		c.put(Schema.OccasionMatching.Cols.SEMI_FORMAL, omr.getSemiFormal());
		c.put(Schema.OccasionMatching.Cols.CASUAL, omr.getCasual());
		c.put(Schema.OccasionMatching.Cols.DAY_OUT, omr.getDayOut());
		c.put(Schema.OccasionMatching.Cols.NIGHT_OUT, omr.getNightOut());
		database.insert(occasionMatchingRecordTable, null, c);
	}
	
	public static OccasionMatchingRecord getOccasionMatchingRecordFromCursor(Cursor cursor) {
		long id = cursor.getLong(cursor
				.getColumnIndex(Schema.OccasionMatching.Cols.ID));
		String category = cursor.getString(cursor.getColumnIndex(Schema.OccasionMatching.Cols.CATEGORY));
		String style = cursor.getString(cursor.getColumnIndex(Schema.OccasionMatching.Cols.STYLE));
		int formal = cursor.getInt(cursor.getColumnIndex(Schema.OccasionMatching.Cols.FORMAL));
		int semiFormal = cursor.getInt(cursor.getColumnIndex(Schema.OccasionMatching.Cols.SEMI_FORMAL));
		int casual = cursor.getInt(cursor.getColumnIndex(Schema.OccasionMatching.Cols.CASUAL));
		int dayOut = cursor.getInt(cursor.getColumnIndex(Schema.OccasionMatching.Cols.DAY_OUT));
		int nightOut = cursor.getInt(cursor.getColumnIndex(Schema.OccasionMatching.Cols.NIGHT_OUT));
		OccasionMatchingRecord result = new OccasionMatchingRecord(category, style, formal, semiFormal, casual, dayOut, nightOut);
		result.setId(id);
		return result;
	}
	
	public void deleteOccasionMatchingRecordTable() {
		database.delete(TABLE_OCCASION_MATCHING_MALE, null, null);
		database.delete(TABLE_OCCASION_MATCHING_FEMALE, null, null);
	}
	
	private Cursor queryOccasionMatchingRecord(OccasionEnum oe, String occasionMatchingRecordTable) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		//String[] columns = {Schema.OccasionMatching.Cols.ID, Schema.OccasionMatching.Cols.CATEGORY, Schema.OccasionMatching.Cols.STYLE, oe.toString()};
		qb.setTables(occasionMatchingRecordTable);
		String orderBy = Schema.Item.Cols.ID + " DESC";
		/*
		Cursor c = qb.query(database, columns, null, null, null, null,
				orderBy);
				*/
		Cursor c = qb.query(database, null, null, null, null, null, orderBy);
		return c;
	}

	public ArrayList<OccasionMatchingRecord> getOccasionMatchingRecordMale(OccasionEnum oe) {
		ArrayList<OccasionMatchingRecord> result = new ArrayList<OccasionMatchingRecord>();
		Cursor c = queryOccasionMatchingRecord(oe, TABLE_OCCASION_MATCHING_MALE);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					result.add(getOccasionMatchingRecordFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return result;
	}

	public ArrayList<OccasionMatchingRecord> getOccasionMatchingRecordFemale(OccasionEnum oe) {
		ArrayList<OccasionMatchingRecord> result = new ArrayList<OccasionMatchingRecord>();
		Cursor c = queryOccasionMatchingRecord(oe, TABLE_OCCASION_MATCHING_FEMALE);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					result.add(getOccasionMatchingRecordFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return result;
	}

	/*
	 * ************************************************************************
	 * PairnMatchingRecord
	 * ************************************************************************ 
	 */
	
	// As of 7/1/2014, pairMatchingRecordTable can be either
	// TABLE_PAIR_MATCHING_MALE or TABLE_PAIR_MATCHING_FEMALE
	public void savePairMatchingRecord(PairMatchingRecord pmr, String pairMatchingRecordTable) {
		ContentValues c = new ContentValues();
		c.put(Schema.PairMatching.Cols.BOTTOM, pmr.getBottom());
		c.put(Schema.PairMatching.Cols.TOP, pmr.getTop());
		c.put(Schema.PairMatching.Cols.POINT, pmr.getPoint());
		c.put(Schema.PairMatching.Cols.OUTER, pmr.getOuter());
		database.insert(pairMatchingRecordTable, null, c);
	}

	public void deletePairMatchingRecordTable() {
		database.delete(TABLE_PAIR_MATCHING_MALE, null, null);
		database.delete(TABLE_PAIR_MATCHING_FEMALE, null, null);
	}
	
	public static PairMatchingRecord getPairMatchingRecordFromCursor(Cursor cursor) {
		long id = cursor.getLong(cursor
				.getColumnIndex(Schema.PairMatching.Cols.ID));
		String bottom = cursor.getString(cursor.getColumnIndex(Schema.PairMatching.Cols.BOTTOM));
		String top = cursor.getString(cursor.getColumnIndex(Schema.PairMatching.Cols.TOP));
		int point = cursor.getInt(cursor.getColumnIndex(Schema.PairMatching.Cols.POINT));
		String outer = cursor.getString(cursor.getColumnIndex(Schema.PairMatching.Cols.OUTER));
		PairMatchingRecord result = new PairMatchingRecord(bottom, top, point, outer);
		result.setId(id);
		return result;
	}
	
	private Cursor queryPairMatchingRecord(String pairRecordTable) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(pairRecordTable);
		String orderBy = Schema.Item.Cols.ID + " DESC";
		Cursor c = qb.query(database, null, null, null, null, null, orderBy);
		return c;
	}
	
	public ArrayList<PairMatchingRecord> getPairMatchingRecordMale() {
		ArrayList<PairMatchingRecord> result = new ArrayList<PairMatchingRecord>();
		Cursor c = queryPairMatchingRecord(TABLE_PAIR_MATCHING_MALE);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					result.add(getPairMatchingRecordFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return result;
	}
	
	public ArrayList<PairMatchingRecord> getPairMatchingRecordFemale() {
		ArrayList<PairMatchingRecord> result = new ArrayList<PairMatchingRecord>();
		Cursor c = queryPairMatchingRecord(TABLE_PAIR_MATCHING_FEMALE);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					result.add(getPairMatchingRecordFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return result;
	}
	
	/*
	 * ************************************************************************
	 * ColorMatchingRecord
	 * ************************************************************************ 
	 */
	public void saveColorMatchingRecord(ColorMatchingRecord cmr) {
		ContentValues c = new ContentValues();
		c.put(Schema.ColorMatching.Cols.BOTTOM, cmr.getBottom());
		c.put(Schema.ColorMatching.Cols.TOP, cmr.getTop());
		c.put(Schema.ColorMatching.Cols.POINT, cmr.getPoint());
		database.insert(TABLE_COLOR_MATCHING_DEFAULT, null, c);
	}

	public void deleteColorMatchingRecordTable() {
		database.delete(TABLE_COLOR_MATCHING_DEFAULT, null, null);
	}
	
	public static ColorMatchingRecord getColorMatchingRecordFromCursor(Cursor cursor) {
		long id = cursor.getLong(cursor
				.getColumnIndex(Schema.ColorMatching.Cols.ID));
		String bottom = cursor.getString(cursor.getColumnIndex(Schema.ColorMatching.Cols.BOTTOM));
		String top = cursor.getString(cursor.getColumnIndex(Schema.ColorMatching.Cols.TOP));
		int point = cursor.getInt(cursor.getColumnIndex(Schema.ColorMatching.Cols.POINT));
		ColorMatchingRecord result = new ColorMatchingRecord(bottom, top, point);
		result.setId(id);
		return result;
	}
	
	private Cursor queryColorMatchingRecord(String colorRecordTable) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(colorRecordTable);
		String orderBy = Schema.Item.Cols.ID + " DESC";
		Cursor c = qb.query(database, null, null, null, null, null, orderBy);
		return c;
	}
	
	public ArrayList<ColorMatchingRecord> getColorMatchingRecordDefault() {
		ArrayList<ColorMatchingRecord> result = new ArrayList<ColorMatchingRecord>();
		Cursor c = queryColorMatchingRecord(TABLE_COLOR_MATCHING_DEFAULT);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					result.add(getColorMatchingRecordFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return result;
	}

	/*
	 * ************************************************************************
	 * UserProfile
	 * ************************************************************************ 
	 */
	public void saveUserProfileRecord(UserProfile usr) {
		ContentValues c = new ContentValues();
		c.put(Schema.UserProfile.Cols.USR, usr.getUsr());
		c.put(Schema.UserProfile.Cols.PWD, usr.getPwd());
		c.put(Schema.UserProfile.Cols.GENDER, usr.getGender());
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
		String gender = cursor.getString(cursor.getColumnIndex(Schema.UserProfile.Cols.GENDER));
		int zip = cursor.getInt(cursor.getColumnIndex(Schema.UserProfile.Cols.ZIP));
		int laundrySchedule = cursor.getInt(cursor.getColumnIndex(Schema.UserProfile.Cols.LAUNDRY_SCHEDULE));
		String laundryDay = cursor.getString(cursor.getColumnIndex(Schema.UserProfile.Cols.LAUNDRY_DAY));
		return new UserProfile.UserProfileBuilder(usr, pwd, gender, zip)
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
		c.put(Schema.UserProfile.Cols.GENDER, usr.getGender());
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
	 * ************************************************************************ 
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


	/*
	 * Query "top" items in the item database that are clean and within the 
	 * range of temperature.
	 * SELECT * FROM itemData_db WHERE dirty = 'false' AND TempMax > 70 AND TempMin < 60;
	 * category is either "Top" or "Bottom"
	 */
	public Cursor queryItemCleanTemperature(String category, int tempMax, int tempMin) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_NAME);
		//qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?,?,?)");
		//String[] whereArgs = {"jacket", "shirt", "t-shirt"};
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?) AND "
				+ Schema.Item.Cols.DIRTY + " IN (?) AND " 
				+ Schema.Item.Cols.TEMPERATUTRE_MAX + " >= (?) AND "
				+ Schema.Item.Cols.TEMPERATUTRE_MIN + " <= (?)");
		String[] whereArgs = {category, "false", String.valueOf(tempMax), String.valueOf(tempMin)};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
		/*
		return database.rawQuery(
				"SELECT * FROM " + TABLE_NAME + " WHERE " + Schema.Item.Cols.DIRTY 
				+ " = false AND " + Schema.Item.Cols.CATEGORY + " = Top AND "
				+ Schema.Item.Cols.TEMPERATUTRE_MAX + " > " + tempMax
				+ Schema.Item.Cols.TEMPERATUTRE_MIN + " < " + tempMin, 
				null);
				*/
	}
	
	public ArrayList<ItemData> getTopCleanTemperature(int tempMax, int tempMin) {
		ArrayList<ItemData> tops = new ArrayList<ItemData>();
		Cursor c = queryItemCleanTemperature("Top", tempMax, tempMin);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					tops.add(getItemDataFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return tops;
	}
	
	public ArrayList<ItemData> getBottomCleanTemperature(int tempMax, int tempMin) {
		ArrayList<ItemData> tops = new ArrayList<ItemData>();
		Cursor c = queryItemCleanTemperature("Bottom", tempMax, tempMin);
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
	 * ************************************************************************
	 * ItemDataOpenHelper
	 * ************************************************************************ 
	 */
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
					+ Schema.UserProfile.Cols.GENDER + " TEXT, "
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
			
			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_OCCASION_MATCHING_MALE);
			db.execSQL("CREATE TABLE " + TABLE_OCCASION_MATCHING_MALE + "("
					+ Schema.OccasionMatching.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.OccasionMatching.Cols.CATEGORY + " TEXT, "
					+ Schema.OccasionMatching.Cols.STYLE + " TEXT, "
					+ Schema.OccasionMatching.Cols.FORMAL + " INTEGER, "
					+ Schema.OccasionMatching.Cols.SEMI_FORMAL + " INTEGER, "
					+ Schema.OccasionMatching.Cols.CASUAL + " INTEGER, "
					+ Schema.OccasionMatching.Cols.DAY_OUT + " INTEGER, "
					+ Schema.OccasionMatching.Cols.NIGHT_OUT + " INTEGER)");

			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_OCCASION_MATCHING_FEMALE);
			db.execSQL("CREATE TABLE " + TABLE_OCCASION_MATCHING_FEMALE + "("
					+ Schema.OccasionMatching.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.OccasionMatching.Cols.CATEGORY + " TEXT, "
					+ Schema.OccasionMatching.Cols.STYLE + " TEXT, "
					+ Schema.OccasionMatching.Cols.FORMAL + " INTEGER, "
					+ Schema.OccasionMatching.Cols.SEMI_FORMAL + " INTEGER, "
					+ Schema.OccasionMatching.Cols.CASUAL + " INTEGER, "
					+ Schema.OccasionMatching.Cols.DAY_OUT + " INTEGER, "
					+ Schema.OccasionMatching.Cols.NIGHT_OUT + " INTEGER)");

			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_PAIR_MATCHING_MALE);
			db.execSQL("CREATE TABLE " + TABLE_PAIR_MATCHING_MALE + "("
					+ Schema.PairMatching.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.PairMatching.Cols.BOTTOM + " TEXT, "
					+ Schema.PairMatching.Cols.TOP + " TEXT, "
					+ Schema.PairMatching.Cols.POINT + " TEXT, "
					+ Schema.PairMatching.Cols.OUTER + " INTEGER)");

			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_PAIR_MATCHING_FEMALE);
			db.execSQL("CREATE TABLE " + TABLE_PAIR_MATCHING_FEMALE + "("
					+ Schema.PairMatching.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.PairMatching.Cols.BOTTOM + " TEXT, "
					+ Schema.PairMatching.Cols.TOP + " TEXT, "
					+ Schema.PairMatching.Cols.POINT + " TEXT, "
					+ Schema.PairMatching.Cols.OUTER + " INTEGER)");

			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_COLOR_MATCHING_DEFAULT);
			db.execSQL("CREATE TABLE " + TABLE_COLOR_MATCHING_DEFAULT + "("
					+ Schema.ColorMatching.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.ColorMatching.Cols.BOTTOM + " TEXT, "
					+ Schema.ColorMatching.Cols.TOP + " TEXT, "
					+ Schema.ColorMatching.Cols.POINT + " INTEGER)");

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
	public void createDefaultDatabaseForMale() {
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
		
		createOccasionMatchingMaleDatabase();
		createPairMatchingMaleDatabase();
		createColorMatchingDefaultDatabase();
	}

	/*
	 * Create a default database based on the preloaded images in the app.
	 */
	public void createDefaultDatabaseForFemale() {
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
				= new UserProfile.UserProfileBuilder("an", "pwd", "F", 78717)
						.laundrySchedule(1)
						.laundryDay("Sunday");
		saveUserProfileRecord(usrBuilder.build());
		
		createOccasionMatchingFemaleDatabase();
		createPairMatchingFemaleDatabase();
		createColorMatchingDefaultDatabase();
	}

	public void deleteDatabase() {
		deleteMyCloset();
		deleteUserProfile();
		deleteOccasionMatchingRecordTable();
		deletePairMatchingRecordTable();
		deleteColorMatchingRecordTable();
	}
	
	/*
	 * Create Uri to assign fields in ItemData.
	 * http://stackoverflow.com/questions/4896223/how-to-get-an-uri-of-an-image-resource-in-android
	 */
	private Uri getUriFromResource(int resId) {
		Uri path = Uri.parse("android.resource://com.example.closetstylist/" + resId);
		return path;
	}
	
	private void createOccasionMatchingMaleDatabase() {
		try {
			InputStream is = mContext.getResources().openRawResource(R.raw.ocassionmatchingmalestripped);
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(is));
			String line = null;
			int i = 0;
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				OccasionMatchingRecord omr = new OccasionMatchingRecord(
						parts[0], parts[1], Integer.parseInt(parts[2]), 
						Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), 
						Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
				saveOccasionMatchingRecord(omr, TABLE_OCCASION_MATCHING_MALE);
				i++;
			}
			Log.i(LOG_TAG, "Number of entries in the TABLE_OCCASION_MATCHING_MALE: " + i);
			reader.close();
		} catch (IOException e) {
			Log.i(LOG_TAG, "Number of entries found: " + e);
		}
	}
	
	private void createOccasionMatchingFemaleDatabase() {
		try {
			InputStream is = mContext.getResources().openRawResource(R.raw.ocassionmatchingfemalestripped);
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(is));
			String line = null;
			int i = 0;
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				OccasionMatchingRecord omr = new OccasionMatchingRecord(
						parts[0], parts[1], Integer.parseInt(parts[2]), 
						Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), 
						Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
				saveOccasionMatchingRecord(omr, TABLE_OCCASION_MATCHING_FEMALE);
				i++;
			}
			Log.i(LOG_TAG, "Number of entries in the TABLE_OCCASION_MATCHING_FEMALE: " + i);
			reader.close();
		} catch (IOException e) {
			Log.i(LOG_TAG, "Number of entries found: " + e);
		}
	}
	
	private void createPairMatchingMaleDatabase() {
		try {
			InputStream is = mContext.getResources().openRawResource(R.raw.pairmatchingmalestripped);
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(is));
			String line = null;
			int i = 0;
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				PairMatchingRecord pmr = new PairMatchingRecord(
						parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]);
				savePairMatchingRecord(pmr, TABLE_PAIR_MATCHING_MALE);
				i++;
			}
			Log.i(LOG_TAG, "Number of entries in the TABLE_PAIR_MATCHING_MALE: " + i);
			reader.close();
		} catch (IOException e) {
			Log.i(LOG_TAG, "Number of entries found: " + e);
		}
	}

	private void createPairMatchingFemaleDatabase() {
		try {
			InputStream is = mContext.getResources().openRawResource(R.raw.pairmatchingfemalestripped);
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(is));
			String line = null;
			int i = 0;
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				PairMatchingRecord pmr = new PairMatchingRecord(
						parts[0], parts[1], Integer.parseInt(parts[2]), parts[3]);
				savePairMatchingRecord(pmr, TABLE_PAIR_MATCHING_FEMALE);
				i++;
			}
			Log.i(LOG_TAG, "Number of entries in the TABLE_PAIR_MATCHING_FEMALE: " + i);
			reader.close();
		} catch (IOException e) {
			Log.i(LOG_TAG, "Number of entries found: " + e);
		}
	}

	private void createColorMatchingDefaultDatabase() {
		try {
			InputStream is = mContext.getResources().openRawResource(R.raw.colormatchingdefaultstripped);
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(is));
			String line = null;
			int i = 0;
			line = reader.readLine();
			//Log.i(LOG_TAG, "Entries in TABLE_COLOR_MATCHING_DEFAULT: " + line);
			while ((line = reader.readLine()) != null) {
				//Log.i(LOG_TAG, "Entries in TABLE_COLOR_MATCHING_DEFAULT: " + line);
				String[] parts = line.split(",");
				ColorMatchingRecord cmr = new ColorMatchingRecord(
						parts[0], parts[1], Integer.parseInt(parts[2]));
				saveColorMatchingRecord(cmr);
				i++;
			}
			Log.i(LOG_TAG, "Number of entries in the TABLE_COLOR_MATCHING_DEFAULT: " + i);
			reader.close();
		} catch (IOException e) {
			Log.i(LOG_TAG, "Number of entries found: " + e);
		}
	}

}
