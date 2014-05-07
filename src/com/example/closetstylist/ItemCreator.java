package com.example.closetstylist;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

/*
 * This class is ported from StoryCreator from iRemember.
 * It's redundant because its functionalities overlap those of ItemData.
 */
public class ItemCreator {
	/*
	 * intent to port StoryCreator from iRemember
	 */
	
	public static ContentValues getCVfromItem(ItemData item) {
		ContentValues rValue = new ContentValues();
		rValue.put(Schema.Item.Cols.NAME, item.getName());
		rValue.put(Schema.Item.Cols.DESCRIPTION, item.getDescription());
		rValue.put(Schema.Item.Cols.IMAGE_LINK, item.getImageLink());
		rValue.put(Schema.Item.Cols.COLOR, item.getColor());
		rValue.put(Schema.Item.Cols.TEMPERATUTRE_MIN, item.getTempMin());
		rValue.put(Schema.Item.Cols.TEMPERATUTRE_MAX, item.getTempMax());
		rValue.put(Schema.Item.Cols.CATEGORY, item.getCategory());
		rValue.put(Schema.Item.Cols.AGE, item.getAge());
		rValue.put(Schema.Item.Cols.MATERIAL, item.getMaterial());
		return rValue;
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

}
