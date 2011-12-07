package com.graviballs.game.manager;

import com.graviballs.game.CircularScreenItem;

public class CollisionManager {

	private float mHorizontalBound;
	private float mVerticalBound;

	public CollisionManager(float mHorizontalBound, float mVerticalBound) {
		this.mHorizontalBound = mHorizontalBound;
		this.mVerticalBound = mVerticalBound;
	}

	public boolean circularScreenItemCollision(final CircularScreenItem circularScreenItem, final CircularScreenItem ball) {
		final double xDist = (circularScreenItem.getXProportion() - ball.getXProportion()) * mHorizontalBound;
		final double yDist = (circularScreenItem.getYProportion() - ball.getYProportion()) * mVerticalBound;
		final double collisionDist = (circularScreenItem.getRadius(mHorizontalBound) + ball.getRadius(mHorizontalBound));
		return (Math.pow(xDist, 2) + Math.pow(yDist, 2) < Math.pow(collisionDist, 2));
	}
}
