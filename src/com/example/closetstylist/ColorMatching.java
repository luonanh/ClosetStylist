package com.example.closetstylist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ColorMatching {

	// All of the below instance variables must be initialize in constructor. 
	// Otherwise all of the functions will fail.
	protected ArrayList<ColorMatchingRecord> colorMatchingRecordTable;
	protected ItemDatabaseHelper dbHelper;
	
	ArrayList<Outfit> colorMatchingList;
	
	protected abstract Outfit calculateColorScore(Outfit o);
	
	public List<Outfit> getColorScoreList(List<Outfit> pairMatchingList) {
		if (null != colorMatchingList) {
			colorMatchingList.clear();
		} else {
			colorMatchingList = new ArrayList<Outfit>();
		}
		
		for (Outfit o: pairMatchingList) {
			Outfit o2 = calculateColorScore(o);
			colorMatchingList.add(o2);
		}
		//Collections.sort(colorMatchingList); sort in ascending order
		Collections.sort(colorMatchingList, Collections.reverseOrder()); // sort in descending order
		return colorMatchingList;
	}
}
