package com.example.closetstylist;

public class OccasionMatchingFemale extends OccasionMatching {
	public OccasionMatchingFemale(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		this.dbHelper = dbHelper;
		this.oe = oe;
		this.occasionMatchingRecordTable = this.dbHelper
				.getOccasionMatchingRecordFemale(this.oe);
	}
}
