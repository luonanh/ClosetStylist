package com.example.closetstylist;

import java.util.ArrayList;
import java.util.List;

public abstract class PairMatching {

	// All of the below instance variables must be initialize in constructor. 
	// Otherwise all of the functions will fail.
	protected ArrayList<PairMatchingRecord> pairMatchingRecordTable;
	protected ItemDatabaseHelper dbHelper;
	protected WeatherInfo wi;
	
	// The following instance variables need not to be initialized in constructor.
	protected List<ItemDataOccasion> topPairList;		// created by setInternalList
	protected List<ItemDataOccasion> bottomPairList;	// created by setInternalList
	protected List<ItemDataOccasion> outerPairList;	// created by setInternalList
	protected ArrayList<Outfit> pairMatchingList;		// created by getPairScoreList
	
	// calculate points for a particular combination of top, bottom, outer 
	// (optional) and return a new Outfit.
	// null is returned if nothing is found
	protected Outfit getOutfitFromTopBottomOuter(ItemDataOccasion top, 
			ItemDataOccasion bottom, ItemDataOccasion outer) {
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
	
	protected abstract List<ItemDataOccasion> getOuterList(List<ItemDataOccasion> top);
	
	protected abstract List<ItemDataOccasion> getTopList(List<ItemDataOccasion> top);
	
	private void setInternalLists(List<ItemDataOccasion> topList, 
			List<ItemDataOccasion> bottomList) {
		topPairList = getTopList(topList);
		outerPairList = getOuterList(topList);
		bottomPairList = bottomList;
	}
	
	public List<Outfit> getPairScoreList(List<ItemDataOccasion> topList, 
			List<ItemDataOccasion> bottomList) {
		if (null != pairMatchingList) {
			pairMatchingList.clear();
		} else {
			pairMatchingList = new ArrayList<Outfit>();
		}
		
		setInternalLists(topList, bottomList);
		
		for (ItemDataOccasion top: topPairList) {
			for (ItemDataOccasion bottom: bottomPairList) {
				Outfit o = null;
				
				// For outfit with add-on
				for (ItemDataOccasion outer: outerPairList) {
					o = getOutfitFromTopBottomOuter(top, bottom, outer);
					if (null != o) {
						pairMatchingList.add(o);
					}
				}
				
				// For outfit with no add-on
				o = getOutfitFromTopBottomOuter(top, bottom, null);
				if (null != o) {
					pairMatchingList.add(o);
				}
				
			}
		}
		
		return pairMatchingList;
	}
}
