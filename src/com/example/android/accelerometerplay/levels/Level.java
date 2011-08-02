package com.example.android.accelerometerplay.levels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.android.accelerometerplay.BallBag;
import com.example.android.accelerometerplay.Ballable;
import com.example.android.accelerometerplay.Goal;
import com.example.android.accelerometerplay.R;

public abstract class Level {
	private final List<Goal> goals = new ArrayList<Goal>();
	private final BallBag ballBag = new BallBag();
	private final Resources resources;
	float mMetersToPixelsX = 0;
	float mMetersToPixelsY = 0;
	long lastBallRelease = 0;
	//TODO get the size earlier, so I don't have to add this boolean into the update logic
	private boolean initialBallsAdded = false;
	
	public Level(Resources resources) {
		setUpGoals();
		this.resources = resources;
	}
	
	abstract int getInitialCount();
	abstract void setUpGoals();
	
	List<Goal> getGoals() {
		return goals;
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
		if (scaledNow - lastBallRelease > getBallReleaseTiming() * 1000000) {
			ballBag.addBall();
			lastBallRelease = scaledNow;
		}
		
        ballBag.update(mSensorX, mSensorY, now, mHorizontalBound, mVerticalBound);
        
        final Ballable mainBall = ballBag.getMainBall();
        final float x = mXOrigin + (mainBall.getmPosX() - mainBall.getRadius()) * mMetersToPixelsX;
        final float y = mYOrigin - (mainBall.getmPosY() + mainBall.getRadius()) * mMetersToPixelsY;
        
        canvas.drawBitmap(createBitmap(mainBall.getRadius()), x, y, null);
        
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
            
            canvas.drawBitmap(createBitmap(ball.getRadius()), x1, y1, null);
        }
        
        //TODO don't need to redraw the goals, they won't move
        for (final Goal goal : getGoals()) {
        	canvas.drawBitmap(createBitmap(goal.getRadius()), mXOrigin + goal.getXProportion() * mHorizontalBound * mMetersToPixelsX, mYOrigin + goal.getYProportion() * mVerticalBound * mMetersToPixelsY, null);
        }
	}
	
	//TODO common interface so we can compare any 2 rendered objects
	private boolean goalBallCollision(final Goal goal, final Ballable ball, final float mHorizontalBound, final float mVerticalBound) {
		final double xDist = goal.getXProportion() * mHorizontalBound - ball.getmPosX();
		final double yDist = -goal.getYProportion() * mVerticalBound - ball.getmPosY();
		final double collisionDist = (goal.getRadius() + ball.getRadius());
		return (Math.pow(xDist, 2) + Math.pow(yDist, 2) < Math.pow(collisionDist, 2));
	}
	
	private Bitmap createBitmap(final float radius) {
		Bitmap ball = BitmapFactory.decodeResource(resources, R.drawable.ball);
        final int dstWidth = (int) (radius * 2 * mMetersToPixelsX + 0.5f);
        final int dstHeight = (int) (radius * 2 * mMetersToPixelsY + 0.5f);
        return Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
	}
}
