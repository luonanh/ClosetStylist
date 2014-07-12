package com.example.closetstylist;

public class ClothesMatchingFemale extends ClothesMatching {
	public ClothesMatchingFemale(ClothesMatchingComponentFactory cmcf) {		
		this.cmcf = cmcf;
		this.om = cmcf.newOccasionMatching();
		this.pm = cmcf.newPairMatching();
		this.cm = cmcf.newColorMatching();
	}
}
