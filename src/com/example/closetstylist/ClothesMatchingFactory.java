package com.example.closetstylist;

public abstract class ClothesMatchingFactory {

	public abstract ClothesMatching createClothesMatching(ItemDatabaseHelper dbHelper, 
			WeatherInfo wi, UserProfile up, OccasionEnum oe);
}
