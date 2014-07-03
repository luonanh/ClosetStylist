package com.example.closetstylist;

public class ColorMatchingRecord {

	private long id;
	private String bottom;
	private String top;
	private int point;
	
	public ColorMatchingRecord(String bottom, String top, int point) {
		this.bottom = bottom;
		this.top = top;
		this.point = point;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBottom() {
		return bottom;
	}

	public void setBottom(String bottom) {
		this.bottom = bottom;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
}
