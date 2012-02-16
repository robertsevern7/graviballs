package com.graviballs.game;

public class Ballocks extends Ballable {

	public Ballocks(float sFriction, float mPosXProp, float mPosYProp, float radius) {
		super(sFriction, mPosXProp, mPosYProp, radius);
	}

	@Override public void executeCollision(Ballable ball, double mHorizontalBound, double mVerticalBound) {
		// TODO is this big boy with itself, or attacking ballocks
		// TODO PAY RENT
	}
}
