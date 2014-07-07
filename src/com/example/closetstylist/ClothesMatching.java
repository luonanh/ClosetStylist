package com.example.closetstylist;

import java.util.ArrayList;
import java.util.List;

public abstract class ClothesMatching {

	// All of the below instance variables must be initialize in constructor. 
	// Otherwise all of the functions will fail.
	protected OccasionMatching om;
	protected PairMatching pm;
	protected ColorMatching cm;
	protected ClothesMatchingComponentFactory cmcf;
	
	// The following instance variables need not to be initialized in constructor.
	/*
	UserProfile up;
	ItemDatabaseHelper itemDatabaseHelper;
	*/
	
	// Populated after running matching processes
	private List<ItemData> topLaundryTemperatureList;
	private List<ItemData> bottomLaundryTemperatureList;
	private List<ItemDataOccasion> topOccasionList;
	private List<ItemDataOccasion> bottomOccasionList;
	private List<Outfit> pairList;
	private List<Outfit> colorList;
	
	private void matchOccasion(List<ItemData> top, List<ItemData> bottom) {
		topOccasionList = om.getOccasionScoreList(top);
		bottomOccasionList = om.getOccasionScoreList(bottom);
	}
	
	private void matchPair() {
		pairList = pm.getPairScoreList(topOccasionList, bottomOccasionList);
	}
	
	private void matchColor() {
		colorList = cm.getColorScoreList(pairList);
	}
	
	public List<Outfit> match(WeatherInfo wi, ItemDatabaseHelper itemDatabaseHelper, UserProfile up) {
		topLaundryTemperatureList = itemDatabaseHelper.getTopCleanTemperature(
				wi.getTempMax(), wi.getTempMin());
		bottomLaundryTemperatureList = itemDatabaseHelper.getBottomCleanTemperature(
				wi.getTempMax(), wi.getTempMin());
		
		// Occasion Matching
		matchOccasion(topLaundryTemperatureList, bottomLaundryTemperatureList);
		
		// Pair Matching
		matchPair();
		
		// Color Matching
		matchColor();
		
		return colorList;
	}
}
