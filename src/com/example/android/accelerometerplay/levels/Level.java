package com.example.android.accelerometerplay.levels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.util.Pair;

import com.example.android.accelerometerplay.BallBag;
import com.example.android.accelerometerplay.Ballable;
import com.example.android.accelerometerplay.Goal;
import com.example.android.accelerometerplay.Deflector;
import com.example.android.accelerometerplay.R;
import com.example.android.accelerometerplay.ScreenItem;

public abstract class Level {
	private final List<Goal> goals = new ArrayList<Goal>();
	private final List<Deflector> deflectors = new ArrayList<Deflector>();
	private final BallBag ballBag = new BallBag();
	private final Resources resources;
	float mMetersToPixelsX = 0;
	float mMetersToPixelsY = 0;
	long lastBallRelease = 0;
	//TODO get the size earlier, so I don't have to add this boolean into the update logic
	private boolean initialBallsAdded = false;
	
	public Level(Resources resources) {
		setUpGoals();
		setUpDeflectors();
		this.resources = resources;
	}
	
	abstract int getInitialCount();
	abstract void setUpGoals();
	abstract void setUpDeflectors();
	
	List<Goal> getGoals() {
		return goals;
	}
	
	List<Deflector> getDeflectors() {
		return deflectors;
	}
	
	abstract int getBallReleaseTiming();
	
	public void setMetersToPixels(final float mMetersToPixelsX, final float mMetersToPixelsY) {
		this.mMetersToPixelsX = mMetersToPixelsX;
		this.mMetersToPixelsY = mMetersToPixelsY;
	}
	
	public void drawLevel(Canvas canvas, final long now, final float mSensorX, final float mSensorY,
			final float mXOrigin, final float mYOrigin,
			final float mHorizontalBound, final float mVerticalBound) {
		ballBag.updateBounds(mHorizontalBound, mVerticalBound);
		
		if (!initialBallsAdded) {
			initialBallsAdded = true;
			for (int i = 1; i < getInitialCount(); ++i) {
				ballBag.addBall();
			}
		}
		
		final long scaledNow = now/1000;
		if (scaledNow - lastBallRelease > getBallReleaseTiming() * 1000000 || ballBag.isEmpty()) {
			ballBag.addBall();
			lastBallRelease = scaledNow;
		}
		
        ballBag.update(mSensorX, mSensorY, now, mHorizontalBound, mVerticalBound);
        
        final Iterator<Ballable> iter = ballBag.getIterator();
        
        while(iter.hasNext()) {
        	final Ballable ball = iter.next();
            final float x1 = mXOrigin + (ball.getmPosX() - ball.getRadius()) * mMetersToPixelsX;
            final float y1 = mYOrigin - (ball.getmPosY() + ball.getRadius()) * mMetersToPixelsY;
            
            for (final Goal goal : getGoals()) {
            	if (goalBallCollision(goal, ball, mHorizontalBound, mVerticalBound)) {
            		iter.remove();
            	}
            }
            
            for (final Deflector deflector : getDeflectors()) {
            	if (goalBallCollision(deflector, ball, mHorizontalBound, mVerticalBound)) {
            		deflect(deflector, ball, mHorizontalBound, mVerticalBound);
            	}
            }
            
            canvas.drawBitmap(ball.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY), x1, y1, null);
        }
        
        final Ballable mainBall = ballBag.getMainBall();
        
        for (final Deflector deflector : getDeflectors()) {
        	if (goalBallCollision(deflector, mainBall, mHorizontalBound, mVerticalBound)) {
        		deflect(deflector, mainBall, mHorizontalBound, mVerticalBound);
        	}
        }
        
        final float x = mXOrigin + (mainBall.getmPosX() - mainBall.getRadius()) * mMetersToPixelsX;
        final float y = mYOrigin - (mainBall.getmPosY() + mainBall.getRadius()) * mMetersToPixelsY;
        
        canvas.drawBitmap(mainBall.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY), x, y, null);
        
        //TODO don't need to redraw the goals, they won't move
        for (final Goal goal : getGoals()) {
        	canvas.drawBitmap(goal.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY), mXOrigin - goal.getRadius() * mMetersToPixelsX + (goal.getXProportion() * mHorizontalBound)* mMetersToPixelsX, mYOrigin - goal.getRadius() * mMetersToPixelsY + (goal.getYProportion() * mVerticalBound) * mMetersToPixelsY, null);
        }
        
        for (final Deflector deflector : getDeflectors()) {
        	canvas.drawBitmap(deflector.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY), mXOrigin - deflector.getRadius() * mMetersToPixelsX + deflector.getXProportion() * mHorizontalBound * mMetersToPixelsX, mYOrigin - deflector.getRadius() * mMetersToPixelsY + deflector.getYProportion() * mVerticalBound * mMetersToPixelsY, null);
        }
	}
	
	private void deflect(final Deflector deflector, final Ballable ball, final float mHorizontalBound, final float mVerticalBound) {
		final double xDist = ball.getmPosX() - deflector.getXProportion() * mHorizontalBound;
		final double yDist = ball.getmPosY() - deflector.getYProportion() * mVerticalBound;
		
		//rotation (cos2a  sin2a)(v_x) = (v_x')
		//matrix   (-sin2a cos2a)(v_y)   (v_y')
		//We want v_x' and v_y'
		
		final double theta = Math.atan(yDist/xDist) + getPiAddition(xDist, yDist);
		
		final Pair<Float, Float> vel = ball.getVelocity();
		//Reverse velocity to get correct direction for angle
		final float vel_x_dir = -vel.first;
		final float vel_y_dir = -vel.second;
		final double psi = Math.atan(vel_y_dir/vel_x_dir) + getPiAddition(vel_x_dir, vel_y_dir);
		 
		double tot = psi - theta;
		final float newVelX = (float) (vel.first * Math.cos(2 * tot) + vel.second * Math.sin(2 * tot));
		final float newVelY = (float) (-1 * vel.first * Math.sin(2 * tot) + vel.second * Math.cos(2 * tot));
		
		final float realignedVelX = (float) (vel.first * Math.cos(tot) + vel.second * Math.sin(tot));

		if (realignedVelX <= 0 && xDist > 0 || realignedVelX >= 0 && xDist < 0) {
			ball.setVelocity(- 2 * newVelX, - 2 * newVelY);
		}
	}
	
	private double getPiAddition(final double x, final double y) {
		if (x < 0) {
			return Math.PI;
		}
		else if (y < 0) {
			return Math.PI * 2;
		}
		
		return 0;
	}
	
	//TODO common interface so we can compare any 2 rendered objects
	private boolean goalBallCollision(final ScreenItem screenItem, final Ballable ball, final float mHorizontalBound, final float mVerticalBound) {
		final double xDist = screenItem.getXProportion() * mHorizontalBound - ball.getmPosX();
		final double yDist = -screenItem.getYProportion() * mVerticalBound - ball.getmPosY();
		final double collisionDist = (screenItem.getRadius() + ball.getRadius());
		return (Math.pow(xDist, 2) + Math.pow(yDist, 2) < Math.pow(collisionDist, 2));
	}
}
