package com.graviballs.game;

import com.graviballs.R;

public class AttackingBallacks extends Ballable {

	private static final Double TAIL_OFF_EFFECT = 1.5d;
	private final Ballable bigBoy;
	private final float G = 800f;
	private boolean computed = false;

	// TODO define friction in ballable
	public AttackingBallacks(final Ballable bigBoy, final float sFriction, final float mPosXProp, final float mPosYProp, final float radius) {
		super(sFriction, mPosXProp, mPosYProp, radius);
		this.bigBoy = bigBoy;
	}

	@Override
	public void computePhysics(float sx, float sy, float dT, float dTC) {	
		if (!computed) {
			computed = true;
			setVelocity(initialSpeedX, initialSpeedY);
		}
		
		lastXProportion = getXProportion();
		lastYProportion = getYProportion();

        final double relX = getXProportion() - bigBoy.getXProportion();
        final double relY = getYProportion() - bigBoy.getYProportion();
        final double a = G * bigBoy.getMass() / (Math.pow(Math.abs(relX), TAIL_OFF_EFFECT) + Math.pow(Math.abs(relY), TAIL_OFF_EFFECT));
        double theta = Math.atan(relX / relY);
        
        if (relY < 0) {
        	theta += Math.PI;
        }
        
        setVelocity((float) (getVelocity().first - a * Math.sin(theta) * dT), (float) (getVelocity().second - a * Math.cos(theta) * dT));
        
        setxProportion((float) (lastXProportion + getVelocity().first * dT - a * Math.pow(dT, 2) * Math.sin(theta) * 0.5));
        setyProportion((float) (lastYProportion + getVelocity().second * dT - a * Math.pow(dT, 2) * Math.cos(theta) * 0.5));
	}
	
	public int getDrawable() {
		return R.drawable.spikyball; 
	}

	@Override public void executeCollision(Ballable ball, double mHorizontalBound, double mVerticalBound) {
		// finish level
	}
}
