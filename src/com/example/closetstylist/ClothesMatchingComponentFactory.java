package com.example.closetstylist;

public interface ClothesMatchingComponentFactory {

	/*
	public OccasionMatching createOccasionMatching(ItemDatabaseHelper dbHelper, OccasionEnum oe);
	public PairMatching createPairMatching(ItemDatabaseHelper dbHelper, WeatherInfo wi);
	public ColorMatching createColorMatching(ItemDatabaseHelper dbHelper);
	*/
	public OccasionMatching createOccasionMatching();
	public PairMatching createPairMatching();
	public ColorMatching createColorMatching();
}
