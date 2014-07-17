package com.example.closetstylist;

import java.util.ArrayList;
import java.util.List;

public abstract class OccasionMatching {
	
	// All of the below instance variables must be initialize in constructor. 
	// Otherwise all of the functions will fail.
	protected ArrayList<OccasionMatchingRecord> occasionMatchingRecordTable;
	protected ItemDatabaseHelper dbHelper;
	protected OccasionEnum oe;
	
	// Calculate score of a particular ItemData and returns ItemDataOccasion object 
	private ItemDataOccasion getItemDataOccasionFromItemData(ItemData item) {
		ItemCategoryEnum category = item.getCategory();
		String style = item.getStyle();
		for (OccasionMatchingRecord omr: occasionMatchingRecordTable) {
			if ((item.getCategory() == category)
					&& item.getStyle().equalsIgnoreCase(style)) {
				ItemDataOccasion ido = new ItemDataOccasion(item, omr.getPoint(oe));
				return ido;
			}
		}
		return null;
	}
	
	// This will be called 3 times, 1 for top, 1 for bottom, 1 for outer
	public List<ItemDataOccasion> getOccasionScoreList(List<ItemData> itemList) {
		ArrayList<ItemDataOccasion> temp = new ArrayList<ItemDataOccasion>();
		for (ItemData item: itemList) {
			ItemDataOccasion ido = getItemDataOccasionFromItemData(item);
			temp.add(ido);
		}
		return temp;
	}
}
