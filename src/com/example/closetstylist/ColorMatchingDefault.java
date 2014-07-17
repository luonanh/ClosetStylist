package com.example.closetstylist;

public class ColorMatchingDefault extends ColorMatching {
	
	public ColorMatchingDefault(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		this.dbHelper = dbHelper;
		this.colorMatchingRecordTable = this.dbHelper.getColorMatchingRecordDefault();
	}

	@Override
	protected Outfit calculateColorScore(Outfit o) {
		ItemColorEnum topColor = o.getTop().getColor();
		ItemColorEnum bottomColor = o.getBottom().getColor();
		for (ColorMatchingRecord cmr: colorMatchingRecordTable) {
			if ((topColor == cmr.getTop())
					&& (bottomColor == cmr.getBottom())) {
				o.setScore(o.getScore() + cmr.getPoint());
			}
		}
		return o;
	}

}
