package com.graviballs.game;

import com.graviballs.R;

public class Deflector extends CircularScreenItem {
	public Deflector(float mPosXProp, float mPosYProp, float radius) {
		super(mPosXProp, mPosYProp, radius);
	}
	
	public int getDrawable() {
		return R.drawable.yellowball; 
	}
}
