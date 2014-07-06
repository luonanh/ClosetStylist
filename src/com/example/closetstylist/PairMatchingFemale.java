package com.example.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PairMatchingFemale extends PairMatching {
	private ArrayList<String> topStyle = new ArrayList<String>(Arrays.asList(
			"Collared_And_Button-down",
			"Blouse_Short_Sleeve",
			"Blouse_Long_Sleeve",
			"Blouse_Sleeveless",
			"Tee_Long_Sleeve",
			"Tee_Short_Sleeve",
			"Tank_Camisoles",
			"Party_Top",
			"Tunic",
			"Pull-over"));

	private ArrayList<String> outerStyle = new ArrayList<String>(); 

	public PairMatchingFemale(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		this.dbHelper = dbHelper;
		this.wi = wi;
		this.pairMatchingRecordTable = this.dbHelper.getPairMatchingRecordFemale();
		setupPerWeatherInfo();
	}
	
	/*
	@Override
	protected Outfit getOutfitFromTopBottomOuter(ItemDataOccasion top, ItemDataOccasion bottom,
			ItemDataOccasion outer) {
		Outfit outfit = null;
		String topStyle = top.getItemData().getStyle();
		String bottomStyle = bottom.getItemData().getStyle();
		if (null == outer) {
			for (PairMatchingRecord pmr: pairMatchingRecordTable) {
				if ((pmr.getTop().equalsIgnoreCase(topStyle))
						&& (pmr.getBottom().equalsIgnoreCase(bottomStyle))) {
					int totalScore = top.getScore() + bottom.getScore() + pmr.getPoint();
					outfit = new Outfit.OutfitBuilder(top.getItemData())
							.bottom(bottom.getItemData())
							.score(totalScore)
							.build();
				}
			}
		} else {
			for (PairMatchingRecord pmr: pairMatchingRecordTable) {
				if ((pmr.getTop().equalsIgnoreCase(topStyle))
						&& (pmr.getBottom().equalsIgnoreCase(bottomStyle))) {
					int totalScore = 0;
					if (pmr.getOuter().equalsIgnoreCase("No")) {
						totalScore = top.getScore() + bottom.getScore() + pmr.getPoint();						
					} else {
						totalScore = top.getScore() + bottom.getScore() 
								+ outer.getScore() + pmr.getPoint();
					}
					outfit = new Outfit.OutfitBuilder(top.getItemData())
							.bottom(bottom.getItemData())
							.outer(outer.getItemData())
							.score(totalScore)
							.build(); 
				}
			}			
		}
		return outfit;
	}
	*/

	@Override
	protected List<ItemDataOccasion> getOuterList(List<ItemDataOccasion> top) {
		ArrayList<ItemDataOccasion> result = new ArrayList<ItemDataOccasion>();
		for (ItemDataOccasion ido: top) {
			if ((ido.getItemData().getCategory().equalsIgnoreCase("Top"))
					&& (isOuter(ido.getItemData().getStyle()))) {
				result.add(ido);
			}
		}
		return result;
	}

	@Override
	protected List<ItemDataOccasion> getTopList(List<ItemDataOccasion> top) {
		ArrayList<ItemDataOccasion> result = new ArrayList<ItemDataOccasion>();
		for (ItemDataOccasion ido: top) {
			if ((ido.getItemData().getCategory().equalsIgnoreCase("Top"))
					&& (isTop(ido.getItemData().getStyle()))) {
				result.add(ido);
			}
		}
		return result;
	}
	
	private boolean isOuter(String style) {
		for (String temp: outerStyle) {
			if (temp.equalsIgnoreCase(style)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isTop(String style) {
		for (String temp: topStyle) {
			if (temp.equalsIgnoreCase(style)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * This function sets up outerStyle and pairMatchingRecordTable based on 
	 * WeatherInfo as followings:
	 * 	- tempMax > 70: no need Outer -> Outer set to "No" for all entries
	 * 	- 70 > tempMax > 40: use default Outer value, outerStyle includes some
	 * 	- tempMax < 40: use default Outer value, outerStyle includes only one
	 * 
	 */
	private void setupPerWeatherInfo() {
		if (wi.getTempMax() > 70) {
			for (PairMatchingRecord pmr: pairMatchingRecordTable) {
				pmr.setOuter("No");
			}
		} else if (wi.getTempMax() > 40) {
			outerStyle.add("Cardigan");
			outerStyle.add("Sweater_And_Sweatshirt");
			outerStyle.add("Vest");
			outerStyle.add("Coat_And_Jacket_Light");
		} else {
			outerStyle.add("Coat_And_Jacket_Heavy");
		}
	}
}
