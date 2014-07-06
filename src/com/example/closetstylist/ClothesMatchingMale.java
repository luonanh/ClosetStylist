package com.example.closetstylist;

public class ClothesMatchingMale extends ClothesMatching {

	/*
	public ClothesMatchingMale(ClothesMatchingComponentFactory cmcf, 
			ItemDatabaseHelper dbHelper, WeatherInfo wi, UserProfile up, OccasionEnum oe) {*/
	public ClothesMatchingMale(ClothesMatchingComponentFactory cmcf) {		
		this.cmcf = cmcf;
		this.om = cmcf.createOccasionMatching();
		this.pm = cmcf.createPairMatching();
		this.cm = cmcf.createColorMatching();
	}
}
