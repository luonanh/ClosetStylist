package com.example.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;

public enum ItemStyleEnum {

	Collared_And_Button_Down, Blouse_Short_Sleeve, Blouse_Long_Sleeve, Blouse_Sleeveless, Tank_Camisoles, Party_Top, Tunic, Pull_Over, Sweater_And_Sweatshirt, Coat_And_Jacket_Light, Cardigan, Coat_And_Jacket_Heavy, Vest, Casual_Button_Down_Shirt, Dress_Shirt, Polo, T_Shirt_Long_Sleeve, T_Shirt_Short_Sleeve, Jeans, Legging_Skinny, Pants, Shorts, Skirts, No, Yes, NA;

	public static ArrayList<String> getAllItemStyleEnumString() {
		ArrayList<ItemStyleEnum> enumList = new ArrayList<ItemStyleEnum>(Arrays.asList(ItemStyleEnum.values()));
		ArrayList<String> result = new ArrayList<String>();
		for (ItemStyleEnum enumType: enumList) {
			result.add(enumType.toString());
		}
		return result;
	}
	
	public static ArrayList<String> getAllTopItemStyleEnumPerGender(Gender g) {
		ArrayList<String> result = new ArrayList<String>();
		switch(g) {
		case FEMALE:
			result.add(Collared_And_Button_Down.toString());
			result.add(Blouse_Short_Sleeve.toString());
			result.add(Blouse_Long_Sleeve.toString());
			result.add(Blouse_Sleeveless.toString());
			result.add(Party_Top.toString());
			result.add(Pull_Over.toString());
			result.add(T_Shirt_Long_Sleeve.toString());
			result.add(T_Shirt_Short_Sleeve.toString());
			result.add(Tank_Camisoles.toString());
			result.add(Tunic.toString());
			result.add(Sweater_And_Sweatshirt.toString());
			result.add(Coat_And_Jacket_Light.toString());
			result.add(Cardigan.toString());
			result.add(Coat_And_Jacket_Heavy.toString());
			result.add(Vest.toString());
			break;
		default:
			result.add(Casual_Button_Down_Shirt.toString());
			result.add(Coat_And_Jacket_Heavy.toString());
			result.add(Coat_And_Jacket_Light.toString());
			result.add(Dress_Shirt.toString());
			result.add(Polo.toString());
			result.add(Sweater_And_Sweatshirt.toString());
			result.add(T_Shirt_Long_Sleeve.toString());
			result.add(T_Shirt_Short_Sleeve.toString());
			break;
		}
		return result;		
	}
	
	public static ArrayList<String> getAllBottomItemStyleEnumPerGender(Gender g) {
		ArrayList<String> result = new ArrayList<String>();
		switch(g) {
		case FEMALE:
			result.add(Jeans.toString());
			result.add(Legging_Skinny.toString());
			result.add(Pants.toString());
			result.add(Shorts.toString());
			result.add(Skirts.toString());			
			break;
		default:
			result.add(Jeans.toString());
			result.add(Pants.toString());
			result.add(Shorts.toString());
			break;
		}
		return result;		
	}
}
