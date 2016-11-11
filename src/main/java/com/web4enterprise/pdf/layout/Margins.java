package com.web4enterprise.pdf.layout;

public class Margins {
	/**
	 * 71 means about 2.5cm.
	 */
	public static Margins DEFAULT = new Margins(71, 71, 71, 71);
	
	protected int left;
	protected int right;
	protected int top;
	protected int bottom;
	
	public Margins(int left, int right, int top, int bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}
	
	public int getLeft() {
		return left;
	}
	
	public void setLeft(int left) {
		this.left = left;
	}
	
	public int getRight() {
		return right;
	}
	
	public void setRight(int right) {
		this.right = right;
	}
	
	public int getTop() {
		return top;
	}
	
	public void setTop(int top) {
		this.top = top;
	}
	
	public int getBottom() {
		return bottom;
	}
	
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
}
