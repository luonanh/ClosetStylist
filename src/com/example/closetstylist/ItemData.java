package com.example.closetstylist;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * Make this a little bit different from Builder Pattern described in 
 * http://www.javacodegeeks.com/2013/01/the-builder-pattern-in-practice.html
 * to have get and set for all fields in ItemData.
 */
public class ItemData implements Parcelable {
	public static final String INTENT = "ItemDataIntent";
	public static final long INVALID_ID = -1;
	
	private long id; // once added to database, this field will be populated
	private String name; // optional
	private String description; // optional
	private String imageLink; // required
	private String cropImageLink; // required
	private String color; // required
	private int tempMin; // required
	private int tempMax; // required
	private String category; // required
	private String brand; // optional
	private double age; // optional
	private String material; // optional
	
	/*
	 * To create new ItemData, do
	 * return new ItemData.ItemDataBuilder(...).name().description().age().material()
	 */
	private ItemData(ItemDataBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.description = builder.description;
		this.imageLink = builder.imageLink;
		this.cropImageLink = builder.cropImageLink;		
		this.color = builder.color;
		this.tempMin = builder.tempMin;
		this.tempMax = builder.tempMax;
		this.category = builder.category;
		this.brand = builder.brand;
		this.age = builder.age;
		this.material = builder.material;
	}

	public String getCropImageLink() {
		return cropImageLink;
	}

	public void setCropImageLink(String cropImageLink) {
		this.cropImageLink = cropImageLink;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getTempMin() {
		return tempMin;
	}

	public void setTempMin(int tempMin) {
		this.tempMin = tempMin;
	}

	public int getTempMax() {
		return tempMax;
	}

	public void setTempMax(int tempMax) {
		this.tempMax = tempMax;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getAge() {
		return age;
	}

	public void setAge(double age) {
		this.age = age;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}
	
	public String toString() {
		return "ItemData toString: id - " + id + " name - " + name + "; description - "
				+ description + "; iamgeLink - " + imageLink + "; color - "
				+ color + "; tempMin - " + Integer.toString(tempMin)
				+ "; tempMax - " + Integer.toString(tempMax) + "; category - "
				+ category + "; brand - " + brand + "; age - " + age
				+ "; material - " + material + "; cropImageLink " + cropImageLink;
	}

	public static class ItemDataBuilder {
		private long id; // once added to database, this field will be populated
		private String name = ""; // optional
		private String description = ""; // optional
		private final String imageLink; // required cropImageLink
		private final String cropImageLink; // required
		private final String color; // required
		private final int tempMin; // required
		private final int tempMax; // required
		private final String category; // required
		private String brand; // optional
		private double age = 0; // optional
		private String material = ""; // optional
		
		public ItemDataBuilder(String imageLink, String color, int tempMin, 
				int tempMax, String category, String cropImageLink) {
			this.imageLink = imageLink;
			this.color = color;
			this.tempMin = tempMin;
			this.tempMax = tempMax;
			this.category = category;
			this.id = INVALID_ID; // default in case of insertion, there's no valid id yet
			this.cropImageLink = cropImageLink;
		}

		public ItemDataBuilder() {
			this.imageLink = "";
			this.color = "";
			this.tempMin = 10;
			this.tempMax = 25;
			this.category = "";
			this.cropImageLink = "";
		}

		public String getCropImageLink() {
			return cropImageLink;
		}

		public ItemDataBuilder name(String name) {
			this.name = name;
			return this;
		}

		public ItemDataBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		public ItemDataBuilder brand(String brand) {
			this.brand = brand;
			return this;
		}

		public ItemDataBuilder age(double age) {
			this.age = age;
			return this;
		}

		public ItemDataBuilder material(String material) {
			this.material = material;
			return this;
		}
		
		public ItemDataBuilder id(long id) {
			this.id = id;
			return this;
		}
		
		public ItemData build() {
			ItemData itemData = new ItemData(this);
			if (age > 50) {
				throw new IllegalStateException("Age out of range");
			}
			return itemData;
		}
	}
	
	/*
	 * Parcelling part 
	 */

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(imageLink);
		dest.writeString(color);
		dest.writeInt(tempMin);
		dest.writeInt(tempMax);
		dest.writeString(category);
		dest.writeString(brand);
		dest.writeDouble(age);
		dest.writeString(material);
		dest.writeString(cropImageLink);
	}
	
	/*
	 * Unparcelling part, ported from iRemember/StoryData 
	 */
	public static final Parcelable.Creator<ItemData> CREATOR = new Parcelable.Creator<ItemData>() {

		@Override
		public ItemData createFromParcel(Parcel source) {
			return new ItemData(source);
		}

		@Override
		public ItemData[] newArray(int size) {
			return new ItemData[size];
		}
		
	};
	
	private ItemData(Parcel source) {
		id = source.readLong();
		name = source.readString();
		description = source.readString();
		imageLink = source.readString();
		color = source.readString();
		tempMin = source.readInt();
		tempMax = source.readInt();
		category = source.readString();
		brand = source.readString();
		age = source.readDouble();
		material = source.readString();
		cropImageLink = source.readString();
	}

}
