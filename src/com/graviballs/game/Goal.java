package com.graviballs.game;

import com.graviballs.R;

public class Goal extends CircularScreenItem {
	private static final float DEFAULT_RADIUS = 0.06f;
	
	public Goal(float mPosXProp, float mPosYProp, float radius) {
		super(mPosXProp, mPosYProp, radius);
	}
	
	public Goal(float mPosXProp, float mPosYProp) {
		super(mPosXProp, mPosYProp, DEFAULT_RADIUS);
	}

	@Override public void executeCollision(Ballable ball, double mHorizontalBound, double mVerticalBound) {
		// TODO pass level
	}

	public int getDrawable() {
		return R.drawable.whitehole; 
	}
}
