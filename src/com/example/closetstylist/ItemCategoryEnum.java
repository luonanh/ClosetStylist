package com.example.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;

public enum ItemCategoryEnum {

	Top, Bottom;
	
	public static ArrayList<String> getAllItemCategoryEnumString() {
		ArrayList<ItemCategoryEnum> enumList = new ArrayList<ItemCategoryEnum>(Arrays.asList(ItemCategoryEnum.values()));
		ArrayList<String> result = new ArrayList<String>();
		for (ItemCategoryEnum enumType: enumList) {
			result.add(enumType.toString());
		}
		return result;
	}
}
