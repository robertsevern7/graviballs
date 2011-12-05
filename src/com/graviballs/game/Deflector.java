package com.graviballs.game;

import com.graviballs.R;

public class Deflector extends CircularScreenItem {
	private static final float DEFAULT_DEFLECTOR_RADIUS = 0.05f;
	public Deflector(float mPosXProp, float mPosYProp) {
		super(mPosXProp, mPosYProp, DEFAULT_DEFLECTOR_RADIUS);
	}
	
	public int getDrawable() {
		return R.drawable.yellowball; 
	}
}
