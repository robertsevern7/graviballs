package com.graviballs.game;

import com.graviballs.R;

public class Gun extends RectangularScreenItem {

	private static final float PROPORTION = .1f;
	private final Direction direction;

	public Gun(Direction direction, float xProportion, float yProportion) {
		super(PROPORTION, PROPORTION, xProportion, yProportion);
		this.direction = direction;
	}

	@Override public int getDrawable() {
		return R.drawable.wood;
	}

	@Override public void executeCollision(Ballable ball, double mHorizontalBound, double mVerticalBound) {
		ball.setVelocity(direction.getVelocity());
	}

}
