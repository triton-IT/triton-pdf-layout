package com.web4enterprise.pdf.layout.placement;

public class Stop {
	StopType type;
	float position;
	
	public Stop(StopType type, float position) {
		this.type = type;
		this.position = position;
	}

	public StopType getType() {
		return type;
	}

	public void setType(StopType type) {
		this.type = type;
	}

	public float getPosition() {
		return position;
	}

	public void setPosition(float position) {
		this.position = position;
	}
	
	
}
