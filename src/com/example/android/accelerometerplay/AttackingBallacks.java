package com.example.android.accelerometerplay;

public class AttackingBallacks extends Ballable {

	private final Ballable bigBoy;
	private final float G = 700f;
	private boolean computed = false;

	AttackingBallacks(final float radius, final Ballable bigBoy) {
		super(1, radius);
		this.bigBoy = bigBoy;
	}
	
	@Override
	public void computePhysics(float sx, float sy, float dT, float dTC) {	
		if (!computed) {
			computed = true;
			currentSpeedX = initialSpeedX;
			currentSpeedY = initialSpeedY;
		}
		
		mLastPosX = mPosX;
        mLastPosY = mPosY;
        
        final double relX = mPosX - bigBoy.mPosX;
        final double relY = mPosY - bigBoy.mPosY;
        final double a = G * bigBoy.getMass() / (Math.pow(relX, 2) + Math.pow(relY, 2));
        double theta = Math.atan(relX / relY);
        
        if (relY < 0) {
        	theta += Math.PI;
        }
        
        float newSpeedX = (float) (currentSpeedX - a * Math.sin(theta) * dT);
        float newSpeedY = (float) (currentSpeedY - a * Math.cos(theta) * dT);
        
        mPosX = (float) (mLastPosX + newSpeedX * dT - a * Math.pow(dT, 2) * Math.sin(theta) * 0.5);
        mPosY = (float) (mLastPosY + newSpeedY * dT - a * Math.pow(dT, 2) * Math.cos(theta) * 0.5);
        
        currentSpeedX = newSpeedX;
        currentSpeedY = newSpeedY;
	}
}
