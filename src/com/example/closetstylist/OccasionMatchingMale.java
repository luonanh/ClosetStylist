package com.example.closetstylist;

public class OccasionMatchingMale extends OccasionMatching {
	public OccasionMatchingMale(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		this.dbHelper = dbHelper;
		this.oe = oe;
		this.occasionMatchingRecordTable = this.dbHelper
				.getOccasionMatchingRecordMale(this.oe);
	}
}