package com.example.android.accelerometerplay;

public class Goal extends ScreenItem {
	public Goal(float mPosXProp, float mPosYProp, float radius) {
		super(mPosXProp, mPosYProp, radius);
	}
	
	protected int getDrawable() {
		return R.drawable.whitehole; 
	}
}
