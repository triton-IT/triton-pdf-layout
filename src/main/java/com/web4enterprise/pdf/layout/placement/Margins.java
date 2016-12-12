package com.web4enterprise.pdf.layout.placement;

public class Margins {
	/**
	 * 60 means about 2.12cm.
	 */
	public static Margins A0 = new Margins(960);
	public static Margins A1 = new Margins(480);
	public static Margins A2 = new Margins(240);
	public static Margins A3 = new Margins(120);
	public static Margins A4 = new Margins(60);
	public static Margins A5 = new Margins(30);
	public static Margins A6 = new Margins(15);
	public static Margins A7 = new Margins(8);
	public static Margins A8 = new Margins(4);
	
	protected int left;
	protected int right;
	protected int top;
	protected int bottom;
	
	public Margins(int allAround) {
		this.left = allAround;
		this.right = allAround;
		this.top = allAround;
		this.bottom = allAround;
	}
	
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
