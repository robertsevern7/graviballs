package com.example.android.accelerometerplay;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AttackingBallacks extends Ballable {

	private final Ballable bigBoy;
	private final float G = 0f;

	AttackingBallacks(final float radius, final Ballable bigBoy) {
		super(1, radius);
		this.bigBoy = bigBoy;
	}
	
	@Override
	public void computePhysics(float sx, float sy, float dT, float dTC) {
		final float lastDt = (dTC > 0) ? dT/dTC : 1;
		//new BigDecimal(Float.valueOf(mPosX).toString()).divide(divisor, 20, RoundingMode.HALF_UP )
		final float currentSpeedX = 0.11f;//(mPosX - mLastPosX) / lastDt;
		final float currentSpeedY = 0.11f;//(mPosY - mLastPosY) / lastDt;
		
		mLastPosX = mPosX;
        mLastPosY = mPosY;
        
        final double a = G * bigBoy.getMass() / (Math.pow(mPosX - bigBoy.mPosX, 2) + Math.pow(mPosY - bigBoy.mPosY, 2));
        final double theta = Math.atan((mPosX - bigBoy.mPosX) / (mPosY - bigBoy.mPosY));
        System.out.println("mPosX " + mPosX);
        System.out.println("mLastPosX " + mLastPosX);
        
        mPosX = (float) (mLastPosX - currentSpeedX * dT + a * Math.pow(dT, 2) * Math.sin(theta) * 0.5);
        mPosY = (float) (mLastPosY - currentSpeedY * dT + a * Math.pow(dT, 2) * Math.cos(theta) * 0.5);
	}
}
