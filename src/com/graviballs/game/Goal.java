package com.graviballs.game;

import com.graviballs.R;

public class Goal extends CircularScreenItem {
	public Goal(float mPosXProp, float mPosYProp, float radius) {
		super(mPosXProp, mPosYProp, radius);
	}
	
	public int getDrawable() {
		return R.drawable.whitehole; 
	}
}
