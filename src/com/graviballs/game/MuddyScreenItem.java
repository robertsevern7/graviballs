package com.graviballs.game;

import com.graviballs.R;

public class MuddyScreenItem extends RectangularScreenItem {

	public MuddyScreenItem(float widthProportion, float heightProportion, float xProportion, float yProportion) {
		super(widthProportion, heightProportion, xProportion, yProportion);
	}

	@Override
	public int getDrawable() {
		return R.drawable.mud;
	}
}
