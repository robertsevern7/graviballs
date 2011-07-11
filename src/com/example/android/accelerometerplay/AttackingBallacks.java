package com.example.android.accelerometerplay;

public class AttackingBallacks extends Ballable {

	private final Ballable bigBoy;
	private final float G = 0.00007f;
	private boolean computed = false;

	AttackingBallacks(final float radius, final Ballable bigBoy) {
		super(1, radius);
		this.bigBoy = bigBoy;
	}
	
	@Override
	public void computePhysics(float sx, float sy, float dT, float dTC) {
		final float lastDt = (dTC > 0) ? dT/dTC : 1;
		//new BigDecimal(Float.valueOf(mPosX).toString()).divide(divisor, 20, RoundingMode.HALF_UP )
		System.out.println("Hi mPosX " + mPosX + ", mLastPosX " + mLastPosX + ", lastDt " + lastDt);
	    float currentSpeedX = (mPosX - mLastPosX) / lastDt;				
		float currentSpeedY = (mPosY - mLastPosY) / lastDt;
		
		if (!computed) {
			computed = true;
			//TODO randomise some initial speed
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
        
        mPosX = (float) (mLastPosX + currentSpeedX * dT - a * Math.pow(dT, 2) * Math.sin(theta) * 0.5);
        mPosY = (float) (mLastPosY + currentSpeedY * dT - a * Math.pow(dT, 2) * Math.cos(theta) * 0.5);
	}
}
