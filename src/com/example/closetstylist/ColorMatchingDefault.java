package com.example.closetstylist;

public class ColorMatchingDefault extends ColorMatching {
	
	public ColorMatchingDefault(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		this.dbHelper = dbHelper;
		this.colorMatchingRecordTable = this.dbHelper.getColorMatchingRecordDefault();
	}

	@Override
	protected Outfit calculateColorScore(Outfit o) {
		String topColor = o.getTop().getColor();
		String bottomColor = o.getBottom().getColor();
		for (ColorMatchingRecord cmr: colorMatchingRecordTable) {
			if ((topColor.equalsIgnoreCase(cmr.getTop()))
					&& (bottomColor.equalsIgnoreCase(cmr.getBottom()))) {
				o.setScore(o.getScore() + cmr.getPoint());
			}
		}
		return o;
	}

}
