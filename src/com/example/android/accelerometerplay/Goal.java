package com.example.android.accelerometerplay;

public class Goal {
	private float mPosXProp;
    private float mPosYProp;
    private float radius;
    
    public Goal(final float mPosXProp, final float mPosYProp, final float radius) {
		this.mPosXProp = mPosXProp;
		this.mPosYProp = mPosYProp;
		this.radius = radius;
	}
    
    public float getRadius() {
    	return radius;
    }
    
    public float getXProportion() {
    	return mPosXProp;
    }
    
    public float getYProportion() {
    	return mPosYProp;
    }
}
