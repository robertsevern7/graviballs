package com.graviballs.game.levels;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import com.graviballs.TimeUtils;
import com.graviballs.game.BallBag;
import com.graviballs.game.Ballable;
import com.graviballs.game.Deflector;
import com.graviballs.game.Goal;
import com.graviballs.game.ScreenItem;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;


public abstract class Level extends Observable {
	private final List<Goal> goals = new ArrayList<Goal>();
	private final List<Deflector> deflectors = new ArrayList<Deflector>();
	private final BallBag ballBag = new BallBag(getInitialMainBallPosition());
	private final Resources resources;
	float mMetersToPixelsX = 0;
	float mMetersToPixelsY = 0;
	long lastBallRelease = 0;
	//TODO get the size earlier, so I don't have to add this boolean into the update logic
	private boolean initialBallsAdded = false;
	private Long startTime;
	private final Paint textPaint = new Paint();
	private int totalBallsScored = 0;
	private int elapsedTime = 0;
	private final SharedPreferences scoreCard;
	private final SharedPreferences currentLevel;
	private final int bestTime;
	private boolean ended = false;
	
	public Level(Resources resources, SharedPreferences scoreCard, SharedPreferences currentLevel) {
		this.scoreCard = scoreCard;
		this.currentLevel = currentLevel;
		setUpGoals();
		setUpDeflectors();
		this.resources = resources;
		
		textPaint.setTextSize(20);

		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(Color.WHITE);
	
		bestTime = scoreCard.getInt(getLevelIdentifier(), -1);
	}
	
	abstract int getInitialCount();
	abstract void setUpGoals();
	abstract void setUpDeflectors();
	abstract int getTotalBallCount();
	abstract String getLevelIdentifier();
	
	List<Goal> getGoals() {
		return goals;
	}
	
	List<Deflector> getDeflectors() {
		return deflectors;
	}
	
	abstract int getBallReleaseTiming();
	abstract int getTimeLimit();
	abstract Pair<Float, Float> getInitialMainBallPosition();
	
	public void setMetersToPixels(final float mMetersToPixelsX, final float mMetersToPixelsY) {
		this.mMetersToPixelsX = mMetersToPixelsX;
		this.mMetersToPixelsY = mMetersToPixelsY;
	}
	
	public void drawFailScreen(Canvas canvas) {
		textPaint.setTextSize(40);
		canvas.drawText("Level Failed", 5, 175, textPaint);
	}
	
	public void drawPassScreen(Canvas canvas) {
		textPaint.setTextSize(40);
		canvas.drawText("Level Passed", 5, 175, textPaint);
		textPaint.setTextSize(30);
		canvas.drawText("Time taken: " + TimeUtils.justParsingTheTime(elapsedTime), 10, 220, textPaint);
	}
	
	public void drawLevel(Canvas canvas, final long now, final float mSensorX, final float mSensorY,
			final float mXOrigin, final float mYOrigin,
			final float mHorizontalBound, final float mVerticalBound) {
		if (ended) {
			return;
		}
		
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
        
        final Ballable mainBall = ballBag.getMainBall();
        final Iterator<Ballable> iter = ballBag.getIterator();
        
        while(iter.hasNext()) {
        	final Ballable ball = iter.next();
            final float x1 = mXOrigin + (ball.getmPosX() - ball.getRadius()) * mMetersToPixelsX;
            final float y1 = mYOrigin - (ball.getmPosY() + ball.getRadius()) * mMetersToPixelsY;
            
            if (ballBallCollision(mainBall, ball, mHorizontalBound, mVerticalBound)) {
            	failLevel();
            }
            
            for (final Goal goal : getGoals()) {
            	if (goalBallCollision(goal, ball, mHorizontalBound, mVerticalBound)) {
            		++totalBallsScored;
            		iter.remove();
            		
            		if (totalBallsScored >= getTotalBallCount()) {
            			passLevel();
            		}
            	}
            }
            
            for (final Deflector deflector : getDeflectors()) {
            	if (goalBallCollision(deflector, ball, mHorizontalBound, mVerticalBound)) {
            		deflect(deflector, ball, mHorizontalBound, mVerticalBound);
            	}
            }
            
            canvas.drawBitmap(ball.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY), x1, y1, null);
        }
        
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
        	if (goalBallCollision(goal, mainBall, mHorizontalBound, mVerticalBound)) {
	        	failLevel();
        	}
        	canvas.drawBitmap(goal.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY), mXOrigin - goal.getRadius() * mMetersToPixelsX + (goal.getXProportion() * mHorizontalBound)* mMetersToPixelsX, mYOrigin - goal.getRadius() * mMetersToPixelsY + (goal.getYProportion() * mVerticalBound) * mMetersToPixelsY, null);
        }
        
        for (final Deflector deflector : getDeflectors()) {
        	canvas.drawBitmap(deflector.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY), mXOrigin - deflector.getRadius() * mMetersToPixelsX + deflector.getXProportion() * mHorizontalBound * mMetersToPixelsX, mYOrigin - deflector.getRadius() * mMetersToPixelsY + deflector.getYProportion() * mVerticalBound * mMetersToPixelsY, null);
        }
        
        if (startTime == null) {
			startTime = now;
		}
		
		elapsedTime = getTimeInSeconds(now - startTime);
		final int timeRemaining = getTimeLimit() - elapsedTime;
		
		if (timeRemaining <= 0 ) {
			failLevel();
		}
		
		canvas.drawText("Time Remaining: " + TimeUtils.justParsingTheTime(timeRemaining), 5, 25, textPaint);
		canvas.drawText("Balls removed: " + totalBallsScored + "/" + getTotalBallCount(), 5, 50, textPaint);
		if (bestTime > 0) {
			canvas.drawText("Best: " + TimeUtils.justParsingTheTime(bestTime), 5, 75, textPaint);
		}
	}
	
	public void failLevel() {
		SharedPreferences.Editor editor = currentLevel.edit();
		editor.putInt("previousAttempt", -1);
		editor.commit();
		ended = true;
		setChanged();
		notifyObservers();
	}
	
	public void passLevel() {
		ended = true;
		if (bestTime < 0 || elapsedTime < bestTime) {
			SharedPreferences.Editor editor = scoreCard.edit();
			editor.putInt(getLevelIdentifier(), elapsedTime);
			editor.commit();
		}
		
		SharedPreferences.Editor editor = currentLevel.edit();
		editor.putInt("bestTime", bestTime);
		editor.putInt("previousAttempt", elapsedTime);
		editor.commit();
		
		setChanged();
		notifyObservers();
	}
	
	public int getPassTime() {
		return bestTime;
	}
	
	private int getTimeInSeconds(long time) {
		return (int) (time / 1000000000);
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
			ball.setVelocity( - 2 * newVelX, - 2 * newVelY);
		}
	}
	
	private double getPiAddition(final double x, final double y) {
		//Log.i("PI addition", x + ", " + y);
		if (x < 0) {
			//Log.i("in", "IN HERE");
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
	
	private boolean ballBallCollision(final Ballable ball1, final Ballable ball2, final float mHorizontalBound, final float mVerticalBound) {
		final double xDist = ball1.getmPosX() - ball2.getmPosX();
		final double yDist = ball1.getmPosY() - ball2.getmPosY();
		final double collisionDist = (ball1.getRadius() + ball2.getRadius());
		return (Math.pow(xDist, 2) + Math.pow(yDist, 2) < Math.pow(collisionDist, 2));
	}
}
