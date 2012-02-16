package com.graviballs.game;

import android.util.Pair;
import com.graviballs.R;

public class MuddyScreenItem extends RectangularScreenItem {

	public MuddyScreenItem(float widthProportion, float heightProportion, float xProportion, float yProportion) {
		super(widthProportion, heightProportion, xProportion, yProportion);
	}

	public void executeCollision(final Ballable ball, double mHorizontalBound, double mVerticalBound) {
		final Pair<Float, Float> velocity = ball.getVelocity();
		ball.setVelocity(velocity.first/2, velocity.second/2);
	}

	@Override
	public int getDrawable() {
		return R.drawable.mud;
	}
}
