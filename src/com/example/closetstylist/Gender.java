package com.example.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;

public enum Gender {
	MALE, FEMALE;
	
	public static ArrayList<String> getAllGenderString() {
		ArrayList<Gender> enumList = new ArrayList<Gender>(Arrays.asList(Gender.values()));
		ArrayList<String> result = new ArrayList<String>();
		for (Gender enumType: enumList) {
			result.add(enumType.toString());
		}
		return result;
	}
}
