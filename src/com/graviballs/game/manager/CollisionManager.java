package com.graviballs.game.manager;

import android.util.Pair;
import com.graviballs.game.Ballable;
import com.graviballs.game.CircularScreenItem;
import com.graviballs.game.Deflector;
import com.graviballs.game.RectangularScreenItem;
import com.graviballs.game.Wall;

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
	
	public boolean rectangularItemCollision(final RectangularScreenItem screenItem, final CircularScreenItem ball) {
		final float xMin = screenItem.getXProportion()*mHorizontalBound - ball.getRadius(mHorizontalBound);
		final float yMin = screenItem.getYProportion()*mVerticalBound - screenItem.getHeightProportion()*mVerticalBound - ball.getRadius(mHorizontalBound);
		final float xMax = screenItem.getXProportion()*mHorizontalBound + screenItem.getWidthProportion()*mHorizontalBound + ball.getRadius(mHorizontalBound);
		final float yMax = screenItem.getYProportion()*mVerticalBound + ball.getRadius(mHorizontalBound);
		final float centerX = ball.getXProportion()*mHorizontalBound;
		final float centerY = ball.getYProportion()*mVerticalBound;
		return centerX > xMin && centerX < xMax && centerY > yMin && centerY < yMax;
	}

	public void deflect(final Deflector deflector, final Ballable ball) {
		final double xDist = (ball.getXProportion() - deflector.getXProportion()) * mHorizontalBound;
		final double yDist = (ball.getYProportion() - deflector.getYProportion()) * mVerticalBound;

		//rotation (cos2a  sin2a)(v_x) = (v_x')
		//matrix   (-sin2a cos2a)(v_y)   (v_y')
		//We want v_x' and v_y'
		final double theta = Math.atan(yDist/xDist) + getPiAddition(xDist);

		final Pair<Float, Float> vel = ball.getVelocity();
		//Reverse velocity to get correct direction for angle
		final float vel_x_dir = -vel.first;
		final float vel_y_dir = -vel.second;
		final double psi = Math.atan(vel_y_dir/vel_x_dir) + getPiAddition(vel_x_dir);

		double tot = psi - theta;
		// footnote :1
		final float newVelX = (float) (vel.first * Math.cos(2 * tot) + vel.second * Math.sin(2 * tot));
		final float newVelY = (float) (-1 * vel.first * Math.sin(2 * tot) + vel.second * Math.cos(2 * tot));

		final float realignedVelX = (float) (vel.first * Math.cos(tot) + vel.second * Math.sin(tot));

		if (realignedVelX <= 0 && xDist > 0 || realignedVelX >= 0 && xDist < 0) {
			ball.setVelocity( - 2 * newVelX, - 2 * newVelY);
		}
	}

	private double getPiAddition(final double x) {
		if (x < 0) {
			return Math.PI;
		}
		return 0;
	}

	public void slowDown(final Ballable ball) {
		final Pair<Float, Float> velocity = ball.getVelocity();
		ball.setVelocity(velocity.first/2, velocity.second/2);
	}
	
	public void deflect(final Wall wall, final Ballable ballable) {
		final float ballX = ballable.getXProportion();
		final float ballY = ballable.getYProportion();
		final float wallEast = Math.abs(wall.getXProportion() - ballX);
		final float wallWest = Math.abs(wall.getXProportion() + wall.getWidthProportion() - ballX);
		final float wallNorth = Math.abs(wall.getYProportion() - wall.getHeightProportion() - ballY);
		final float wallSouth = Math.abs(wall.getYProportion() - ballY);
		
		final float minDist = Math.min(Math.min(wallEast, wallWest), Math.min(wallNorth, wallSouth));
		final Pair<Float, Float> vel = ballable.getVelocity();
		if (minDist == wallEast || minDist == wallWest) {
			ballable.setVelocity(-vel.first, vel.second);
		} else {
			ballable.setVelocity(vel.first, -vel.second);
		}
	}
}

/*
footnote 1:
//Log.i("dd",ball.getmPosX() + ", " + deflector.getXProportion() * mHorizontalBound);
//Log.i("dd",ball.getmPosY() + ", " + deflector.getYProportion() * mVerticalBound);

//Log.i("dd", xDist + ", " + yDist + ", " + yDist/xDist + ", " + Math.atan(yDist/xDist));
//Log.i("tots = ", xDist + ", " + yDist + ", " + tot + ", " + psi + ", " + theta);
//Log.i("tot = ", vel_x_dir + ", " + vel_y_dir + ", " + tot + ", " + psi + ", " + theta);
*/