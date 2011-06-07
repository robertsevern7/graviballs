package com.example.android.accelerometerplay;

public class AttackingBallacks extends Ballable {

	private final Ballable bigBoy;
	private final float G = 667f;

	AttackingBallacks(final float radius, final Ballable bigBoy) {
		super(1, radius);
		this.bigBoy = bigBoy;
	}
	
	@Override
	public void computePhysics(float sx, float sy, float dT, float dTC) {
		
		mLastPosX = mPosX;
        mLastPosY = mPosY;
        mPosX = (float)(mLastPosX +(mPosX - mLastPosX) + G * bigBoy.getMass() * Math.pow(mPosX - bigBoy.mPosX, 2)/2);
        mPosY = (float)(mLastPosY +(mPosY - mLastPosY) + G * bigBoy.getMass() * Math.pow(mPosY - bigBoy.mPosY, 2)/2);
	}
}
