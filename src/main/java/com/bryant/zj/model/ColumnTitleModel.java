package com.bryant.zj.model;

public class ColumnTitleModel {
	private String col;
	private String title;
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "ColumnTitleModel [col=" + col + ", title=" + title + "]";
	}
}
