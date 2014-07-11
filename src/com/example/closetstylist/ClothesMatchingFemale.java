package com.example.closetstylist;

public class ClothesMatchingFemale extends ClothesMatching {
	public ClothesMatchingFemale(ClothesMatchingComponentFactory cmcf) {		
		this.cmcf = cmcf;
		this.om = cmcf.createOccasionMatching();
		this.pm = cmcf.createPairMatching();
		this.cm = cmcf.createColorMatching();
	}
}
