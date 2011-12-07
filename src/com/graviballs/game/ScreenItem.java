package com.graviballs.game;

public abstract class ScreenItem {

	private float xProportion;
	private float yProportion;

	protected ScreenItem(float xProportion, float yProportion) {
		this.xProportion = xProportion;
		this.yProportion = yProportion;
	}

	public float getXProportion() {
		return xProportion;
	}

	public float getYProportion() {
		return yProportion;
	}

	public void setxProportion(float xProportion) {
		this.xProportion = xProportion;
	}

	public void setyProportion(float yProportion) {
		this.yProportion = yProportion;
	}
}