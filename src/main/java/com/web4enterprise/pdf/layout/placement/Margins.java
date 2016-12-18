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
	public static Margins Zero = new Margins(0);
	
	protected float left;
	protected float right;
	protected float top;
	protected float bottom;
	
	public Margins(float allAround) {
		this.left = allAround;
		this.right = allAround;
		this.top = allAround;
		this.bottom = allAround;
	}
	
	public Margins(float top, float left, float bottom, float right) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}
	
	public float getLeft() {
		return left;
	}
	
	public void setLeft(float left) {
		this.left = left;
	}
	
	public float getRight() {
		return right;
	}
	
	public void setRight(float right) {
		this.right = right;
	}
	
	public float getTop() {
		return top;
	}
	
	public void setTop(float top) {
		this.top = top;
	}
	
	public float getBottom() {
		return bottom;
	}
	
	public void setBottom(float bottom) {
		this.bottom = bottom;
	}
}
