package com.example.android.accelerometerplay;

public class AttackingBallacks extends Ballable {

	private static final Double TAIL_OFF_EFFECT = 1.5d;
	private final Ballable bigBoy;
	private final float G = 40000f;
	private boolean computed = false;

	AttackingBallacks(final float radius, final Ballable bigBoy) {
		super(1, radius);
		this.bigBoy = bigBoy;
	}
	
	@Override
	public void computePhysics(float sx, float sy, float dT, float dTC) {	
		if (!computed) {
			computed = true;
			setVelocity(initialSpeedX, initialSpeedY);
		}
		
		mLastPosX = mPosX;
        mLastPosY = mPosY;

        final double relX = mPosX - bigBoy.mPosX;
        final double relY = mPosY - bigBoy.mPosY;
        final double a = G * bigBoy.getMass() / (Math.pow(Math.abs(relX), TAIL_OFF_EFFECT) + Math.pow(Math.abs(relY), TAIL_OFF_EFFECT));
        double theta = Math.atan(relX / relY);
        
        if (relY < 0) {
        	theta += Math.PI;
        }
        
        setVelocity((float) (getVelocity().first - a * Math.sin(theta) * dT), (float) (getVelocity().second - a * Math.cos(theta) * dT));
        
        mPosX = (float) (mLastPosX + getVelocity().first * dT - a * Math.pow(dT, 2) * Math.sin(theta) * 0.5);
        mPosY = (float) (mLastPosY + getVelocity().second * dT - a * Math.pow(dT, 2) * Math.cos(theta) * 0.5);
	}
	
	protected int getDrawable() {
		return R.drawable.redball; 
	}
}
