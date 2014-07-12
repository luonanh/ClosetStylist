package com.example.closetstylist;

public class ClothesMatchingFactoryMale extends ClothesMatchingFactory {

	@Override
	public ClothesMatching newInstance(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		ClothesMatching cm = null;
		ClothesMatchingComponentFactory cmcf = new ClothesMatchingComponentFactoryMale(dbHelper, wi, up, oe); 
		cm = new ClothesMatchingMale(cmcf);
		return cm;
	}
}
