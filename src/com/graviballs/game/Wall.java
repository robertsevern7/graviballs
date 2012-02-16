package com.graviballs.game;

import android.util.Pair;
import com.graviballs.R;

public class Wall extends RectangularScreenItem {

	public Wall(float widthProportion, float heightProportion, float xProportion, float yProportion) {
		super(widthProportion, heightProportion, xProportion, yProportion);
	}

	@Override
	public void executeCollision(final Ballable ball, double mHorizontalBound, double mVerticalBound) {
		// TODO: WRONG_CODE
		// Change of direction code is the problem not with the detecting collisions
		// when you hit a corner flip both bits.

		if (ball.isPreviousCollision()) {
			return;
		}
		ball.setPreviousCollision(true);
		final float ballX = ball.getXProportion();
		final float ballY = ball.getYProportion();
		final float wallEast = Math.abs(getXProportion() - ballX);
		final float wallWest = Math.abs(getXProportion() + getWidthProportion() - ballX);
		final float wallNorth = Math.abs(getYProportion() - getHeightProportion() - ballY);
		final float wallSouth = Math.abs(getYProportion() - ballY);

		final float minDist = Math.min(Math.min(wallEast, wallWest), Math.min(wallNorth, wallSouth));
		final Pair<Float, Float> vel = ball.getVelocity();
		if (minDist == wallEast || minDist == wallWest) {
			ball.setVelocity(-vel.first, vel.second);
		} else {
			ball.setVelocity(vel.first, -vel.second);
		}
	}




	@Override public int getDrawable() {
		return R.drawable.wall;
	}
}
