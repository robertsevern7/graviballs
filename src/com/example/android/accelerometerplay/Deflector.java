package com.example.android.accelerometerplay;

public class Deflector extends ScreenItem {
	public Deflector(float mPosXProp, float mPosYProp, float radius) {
		super(mPosXProp, mPosYProp, radius);
	}
	
	protected int getDrawable() {
		return R.drawable.yellowball; 
	}
}
