package com.example.closetstylist;

public abstract class ClothesMatchingFactory {

	public abstract ClothesMatching newInstance(ItemDatabaseHelper dbHelper, 
			WeatherInfo wi, UserProfile up, OccasionEnum oe);
}
