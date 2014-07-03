package com.example.closetstylist;

public class PairMatchingRecord {

	private long id;
	private String bottom;
	private String top;
	private int point;
	private String outer;
	
	public PairMatchingRecord(String bottom, String top, int point, String outer) {
		this.bottom = bottom;
		this.top = top;
		this.point = point;
		this.outer = outer;
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

	public String getOuter() {
		return outer;
	}

	public void setOuter(String outer) {
		this.outer = outer;
	}
}
