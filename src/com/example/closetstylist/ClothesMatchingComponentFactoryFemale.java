package com.example.closetstylist;

public class ClothesMatchingComponentFactoryFemale implements ClothesMatchingComponentFactory {
	private ItemDatabaseHelper dbHelper;
	private WeatherInfo wi;
	private UserProfile up;
	private OccasionEnum oe;

	public ClothesMatchingComponentFactoryFemale(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		this.dbHelper = dbHelper;
		this.wi = wi;
		this.up = up;
		this.oe = oe;
	}
	
	@Override
	public OccasionMatching createOccasionMatching() {
		return new OccasionMatchingFemale(dbHelper, wi, up, oe);
	}

	@Override
	public PairMatching createPairMatching() {
		return new PairMatchingFemale(dbHelper, wi, up, oe);
	}

	@Override
	public ColorMatching createColorMatching() {
		return new ColorMatchingDefault(dbHelper, wi, up, oe);
	}
}
