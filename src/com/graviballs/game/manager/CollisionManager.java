package com.graviballs.game.manager;

import com.graviballs.game.Ballable;
import com.graviballs.game.CircularScreenItem;
import com.graviballs.game.RectangularScreenItem;
import com.graviballs.game.ScreenItem;

public class CollisionManager {

	private float mHorizontalBound;
	private float mVerticalBound;

	public CollisionManager(float mHorizontalBound, float mVerticalBound) {
		this.mHorizontalBound = mHorizontalBound;
		this.mVerticalBound = mVerticalBound;
	}

	public boolean screenItemCollision(final ScreenItem someScreenItem, final Ballable ball) {
		// TODO: never say i wrote this HORRIBLE
		if (someScreenItem instanceof CircularScreenItem) {
			return screenItemCollision((CircularScreenItem) someScreenItem, ball);
		}
		else {
			return screenItemCollision((RectangularScreenItem) someScreenItem, ball);
		}
	}

	private boolean screenItemCollision(final CircularScreenItem circularScreenItem, final Ballable ball) {
		final double xDist = (circularScreenItem.getXProportion() - ball.getXProportion()) * mHorizontalBound;
		final double yDist = (circularScreenItem.getYProportion() - ball.getYProportion()) * mVerticalBound;
		final double collisionDist = (circularScreenItem.getRadius(mHorizontalBound) + ball.getRadius(mHorizontalBound));
		return Math.pow(xDist, 2) + Math.pow(yDist, 2) < Math.pow(collisionDist, 2);
	}

	private boolean screenItemCollision(final RectangularScreenItem screenItem, final Ballable ball) {
		final float xMin = screenItem.getXProportion()*mHorizontalBound - ball.getRadius(mHorizontalBound);
		final float yMin = screenItem.getYProportion()*mVerticalBound - screenItem.getHeightProportion()*mVerticalBound - ball.getRadius(mHorizontalBound);
		final float xMax = screenItem.getXProportion()*mHorizontalBound + screenItem.getWidthProportion()*mHorizontalBound + ball.getRadius(mHorizontalBound);
		final float yMax = screenItem.getYProportion()*mVerticalBound + ball.getRadius(mHorizontalBound);
		final float centerX = ball.getXProportion()*mHorizontalBound;
		final float centerY = ball.getYProportion()*mVerticalBound;
		return centerX > xMin && centerX < xMax && centerY > yMin && centerY < yMax;
	}


}

/*
footnote 1:
//Log.i("dd",ball.getmPosX() + ", " + deflector.getXProportion() * mHorizontalBound);
//Log.i("dd",ball.getmPosY() + ", " + deflector.getYProportion() * mVerticalBound);

//Log.i("dd", xDist + ", " + yDist + ", " + yDist/xDist + ", " + Math.atan(yDist/xDist));
//Log.i("tots = ", xDist + ", " + yDist + ", " + tot + ", " + psi + ", " + theta);
//Log.i("tot = ", vel_x_dir + ", " + vel_y_dir + ", " + tot + ", " + psi + ", " + theta);
*/