package com.graviballs.game;

import android.util.Pair;
import com.graviballs.R;

public class Deflector extends CircularScreenItem {
	private static final float DEFAULT_DEFLECTOR_RADIUS = 0.05f;
	public static final float VELOCITY_MULTIPLIER = 1.1f;

	public Deflector(float mPosXProp, float mPosYProp) {
		super(mPosXProp, mPosYProp, DEFAULT_DEFLECTOR_RADIUS);
	}

	public void executeCollision(final Ballable ball, double mHorizontalBound, double mVerticalBound) {
		if (ball.isPreviousCollision()) {
			return;
		}
		ball.setPreviousCollision(true);
		final double xDist = (ball.getXProportion() - getXProportion()) * mHorizontalBound;
		final double yDist = (ball.getYProportion() - getYProportion()) * mVerticalBound;

		//rotation (cos2a  sin2a)(v_x) = (v_x')
		//matrix   (-sin2a cos2a)(v_y)   (v_y')
		//We want v_x' and v_y'
		final double theta = Math.atan(yDist / xDist) + getPiAddition(xDist);

		final Pair<Float, Float> vel = ball.getVelocity();
		//Reverse velocity to get correct direction for angle
		final float vel_x_dir = -vel.first;
		final float vel_y_dir = -vel.second;
		final double psi = Math.atan(vel_y_dir / vel_x_dir) + getPiAddition(vel_x_dir);

		double tot = psi - theta;
		// footnote :1
		final float newVelX = (float) (vel.first * Math.cos(2 * tot) + vel.second * Math.sin(2 * tot));
		final float newVelY = (float) (-1 * vel.first * Math.sin(2 * tot) + vel.second * Math.cos(2 * tot));

		final float realignedVelX = (float) (vel.first * Math.cos(tot) + vel.second * Math.sin(tot));

		if (realignedVelX <= 0 && xDist > 0 || realignedVelX >= 0 && xDist < 0) {
			ball.setVelocity(-VELOCITY_MULTIPLIER * newVelX, -VELOCITY_MULTIPLIER * newVelY);
		}

	}

	private double getPiAddition(final double x) {
		if (x < 0) {
			return Math.PI;
		}
		return 0;
	}

	@Override
	public int getDrawable() {
		return R.drawable.yellowball; 
	}
}
