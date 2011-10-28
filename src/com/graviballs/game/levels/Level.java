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
import android.util.Log;
import android.util.Pair;


public abstract class Level extends Observable {
	private final List<Goal> goals = new ArrayList<Goal>();
	private final List<Deflector> deflectors = new ArrayList<Deflector>();
	private final BallBag ballBag = new BallBag();
	private final Resources resources;
	float mMetersToPixelsX = 0;
	float mMetersToPixelsY = 0;
	float mHorizontalBound = 0; 
	float mVerticalBound = 0;
	long lastBallRelease = 0;
	//TODO get the size earlier, so I don't have to add this boolean into the update logic
	private boolean initialBallsAdded = false;
	private Long startTime;
	private final Paint textPaint = new Paint();
	private final Paint cornerPaint = new Paint();
	private int totalBallsScored = 0;
	private int elapsedTime = 0;
	private final SharedPreferences scoreCard;
	private final SharedPreferences currentLevel;
	private int bestTime;
	private boolean ended = false;
	private boolean paused = false;
	private long timePaused = 0;
	private long totalTimePaused = 0;
	private long pauseTime = 0;
	
	public Level(Resources resources, SharedPreferences scoreCard, SharedPreferences currentLevel) {
		this.scoreCard = scoreCard;
		this.currentLevel = currentLevel;
		setUpGoals();
		setUpDeflectors();
		this.resources = resources;
		
		textPaint.setTextSize(20);

		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(Color.WHITE);
		
		cornerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		cornerPaint.setColor(Color.DKGRAY);
	
		bestTime = scoreCard.getInt(getLevelIdentifier(), -1);
	}
	
	abstract int getInitialCount();
	abstract void setUpGoals();
	abstract void setUpDeflectors();
	abstract int getTotalBallCount();
	abstract String getLevelIdentifier();
	
	public void setBounds(final float mHorizontalBound, final float mVerticalBound) {
		this.mHorizontalBound = mHorizontalBound;
		this.mVerticalBound = mVerticalBound;
		ballBag.setBounds(mHorizontalBound, mVerticalBound);
		ballBag.getMainBall().setInitialPos(getInitialMainBallPosition().first, getInitialMainBallPosition().second);
		
		if (ballBag.getAttackBallLaunchPoints().isEmpty()) {
			ballBag.setAttackBallLaunchPoints(getAttackBallLaunchPoints());
		}
	}
	
	List<Goal> getGoals() {
		return goals;
	}
	
	List<Deflector> getDeflectors() {
		return deflectors;
	}
	
	abstract int getBallReleaseTiming();
	abstract int getTimeLimit();
	public Pair<Float, Float> getInitialMainBallPosition() {
		final float widthInPixels = (2 * mMetersToPixelsX * mHorizontalBound);
		final float heightInPixels = (2 * mMetersToPixelsY * mVerticalBound);
		return new Pair<Float, Float>(widthInPixels, heightInPixels);
	}
	
	public List<Pair<Float, Float>> getAttackBallLaunchPoints() {
		List<Pair<Float, Float>> launchPoints = new ArrayList<Pair<Float, Float>>();
		launchPoints.add(new Pair<Float, Float>(2*mMetersToPixelsX * mHorizontalBound, 2*mMetersToPixelsY * mVerticalBound));
		launchPoints.add(new Pair<Float, Float>(0f, 2*mMetersToPixelsY * mVerticalBound));
		
		return launchPoints;
	}
	
	public void setMetersToPixels(final float mMetersToPixelsX, final float mMetersToPixelsY) {
		this.mMetersToPixelsX = mMetersToPixelsX;
		this.mMetersToPixelsY = mMetersToPixelsY;
	}
	
	public void pause() {
		pauseTime = System.nanoTime();
		if (!paused) {
			totalTimePaused += timePaused;
		}
		
		paused = !paused;
	}
	
	public void drawMainBallStartPoint(Canvas canvas) {
		cornerPaint.setColor(Color.argb(200, 255, 255, 255));
		final int widthInPixels = (int) (2 * mMetersToPixelsX * mHorizontalBound);
		canvas.drawCircle(widthInPixels, 0, 30, cornerPaint);
	}
	
	public void drawAttackBallLaunchPoints(Canvas canvas) {
		cornerPaint.setColor(Color.argb(100, 255, 20, 20));
		for (Pair<Float, Float> launchPoints : getAttackBallLaunchPoints()) {
			canvas.drawCircle(launchPoints.first, launchPoints.second, 30, cornerPaint);
		}
	}
	
	public void drawLevel(Canvas canvas, final long now, final float mSensorX, final float mSensorY,
			final float mXOrigin, final float mYOrigin) {
		if (paused) {
			timePaused = (now - pauseTime);
		}
		
		final long timeToUse = now - timePaused - totalTimePaused;
		
		if (ended) {
			return;
		}
		
		drawMainBallStartPoint(canvas);
		drawAttackBallLaunchPoints(canvas);
		
		addBalls();
		
		timeToAddNewBall(timeToUse);
		
		if (!paused) {
            ballBag.update(mSensorX, mSensorY, timeToUse);
		}
        
        final Ballable mainBall = ballBag.getMainBall();
        drawTheBallBag(canvas, mXOrigin, mYOrigin, mainBall);
        
        processDeflectors(mainBall);
        	
        drawMainBall(canvas, mXOrigin, mYOrigin, mainBall);
        
        drawIncidentals(canvas, mXOrigin, mYOrigin, mainBall);
        
        if (startTime == null) {
			startTime = timeToUse;
		}
		
		drawText(canvas, timeToUse);
	}

	private void drawText(Canvas canvas, final long now) {
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

	private void drawIncidentals(Canvas canvas, final float mXOrigin,
			final float mYOrigin, final Ballable mainBall) {
		for (final Goal goal : getGoals()) {
        	if (goalBallCollision(goal, mainBall)) {
	        	failLevel();
        	}
        	canvas.drawBitmap(goal.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY), mXOrigin - goal.getRadius() * mMetersToPixelsX + (goal.getXProportion() * mHorizontalBound)* mMetersToPixelsX, mYOrigin - goal.getRadius() * mMetersToPixelsY + (goal.getYProportion() * mVerticalBound) * mMetersToPixelsY, null);
        }
        
        for (final Deflector deflector : getDeflectors()) {
        	canvas.drawBitmap(deflector.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY), mXOrigin - deflector.getRadius() * mMetersToPixelsX + deflector.getXProportion() * mHorizontalBound * mMetersToPixelsX, mYOrigin - deflector.getRadius() * mMetersToPixelsY + deflector.getYProportion() * mVerticalBound * mMetersToPixelsY, null);
        }
	}

	private void drawMainBall(Canvas canvas, final float mXOrigin,
			final float mYOrigin, final Ballable mainBall) {
		final float x = mXOrigin + (mainBall.getmPosX() - mainBall.getRadius()) * mMetersToPixelsX;
        final float y = mYOrigin - (mainBall.getmPosY() + mainBall.getRadius()) * mMetersToPixelsY;
        
        canvas.drawBitmap(mainBall.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY), x, y, null);
	}

	private void processDeflectors(final Ballable mainBall) {
		for (final Deflector deflector : getDeflectors()) {
        	if (goalBallCollision(deflector, mainBall)) {
        		deflect(deflector, mainBall);
        	}
        }
	}

	private void drawTheBallBag(Canvas canvas, final float mXOrigin,
			final float mYOrigin, final Ballable mainBall) {
		final Iterator<Ballable> iter = ballBag.getIterator();
        
        while(iter.hasNext()) {
        	final Ballable ball = iter.next();
            final float x1 = mXOrigin + (ball.getmPosX() - ball.getRadius()) * mMetersToPixelsX;
            final float y1 = mYOrigin - (ball.getmPosY() + ball.getRadius()) * mMetersToPixelsY;
            
            if (ballBallCollision(mainBall, ball)) {
            	failLevel();
            }
            
            for (final Goal goal : getGoals()) {
            	if (goalBallCollision(goal, ball)) {
            		++totalBallsScored;
            		iter.remove();
            		
            		if (totalBallsScored >= getTotalBallCount()) {
            			passLevel();
            		}
            	}
            }
            
            processDeflectors(ball);
            
            canvas.drawBitmap(ball.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY), x1, y1, null);
        }
	}

	private void timeToAddNewBall(final long now) {
		final long scaledNow = now/1000;
		if (scaledNow - lastBallRelease > getBallReleaseTiming() * 1000000 || ballBag.isEmpty()) {
			ballBag.addBall();
			lastBallRelease = scaledNow;
		}
	}

	private void addBalls() {
		if (!initialBallsAdded) {
			initialBallsAdded = true;
			for (int i = 1; i < getInitialCount(); ++i) {
				ballBag.addBall();
			}
		}
	}
	
	public void failLevel() {
		SharedPreferences.Editor editor = currentLevel.edit();
		editor.putInt("previousAttempt", -1);
		editor.putInt("bestTime", bestTime);
		editor.commit();
		ended = true;
		setChanged();
		notifyObservers();
	}
	
	public void passLevel() {
		ended = true;
		if (bestTime < 0 || elapsedTime < bestTime) {
			bestTime = elapsedTime;
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
	
	private void deflect(final Deflector deflector, final Ballable ball) {
		final double xDist = ball.getmPosX() - deflector.getXProportion() * mHorizontalBound;
		final double yDist = ball.getmPosY() + deflector.getYProportion() * mVerticalBound;
		
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
		//Log.i("dd",ball.getmPosX() + ", " + deflector.getXProportion() * mHorizontalBound);
		//Log.i("dd",ball.getmPosY() + ", " + deflector.getYProportion() * mVerticalBound);
				
		//Log.i("dd", xDist + ", " + yDist + ", " + yDist/xDist + ", " + Math.atan(yDist/xDist));
		//Log.i("tots = ", xDist + ", " + yDist + ", " + tot + ", " + psi + ", " + theta);
		//Log.i("tot = ", vel_x_dir + ", " + vel_y_dir + ", " + tot + ", " + psi + ", " + theta);
		final float newVelX = (float) (vel.first * Math.cos(2 * tot) + vel.second * Math.sin(2 * tot));
		final float newVelY = (float) (-1 * vel.first * Math.sin(2 * tot) + vel.second * Math.cos(2 * tot));
		
		final float realignedVelX = (float) (vel.first * Math.cos(tot) + vel.second * Math.sin(tot));

		if (realignedVelX <= 0 && xDist > 0 || realignedVelX >= 0 && xDist < 0) {
			ball.setVelocity( - 2 * newVelX, - 2 * newVelY);
		}
	}
	
	private double getPiAddition(final double x, final double y) {
		if (x < 0) {
			return Math.PI;
		}
		
		return 0;
	}
	
	//TODO common interface so we can compare any 2 rendered objects
	private boolean goalBallCollision(final ScreenItem screenItem, final Ballable ball) {
		final double xDist = screenItem.getXProportion() * mHorizontalBound - ball.getmPosX();
		final double yDist = -screenItem.getYProportion() * mVerticalBound - ball.getmPosY();
		final double collisionDist = (screenItem.getRadius() + ball.getRadius());
		return (Math.pow(xDist, 2) + Math.pow(yDist, 2) < Math.pow(collisionDist, 2));
	}
	
	private boolean ballBallCollision(final Ballable ball1, final Ballable ball2) {
		final double xDist = ball1.getmPosX() - ball2.getmPosX();
		final double yDist = ball1.getmPosY() - ball2.getmPosY();
		final double collisionDist = (ball1.getRadius() + ball2.getRadius());
		return (Math.pow(xDist, 2) + Math.pow(yDist, 2) < Math.pow(collisionDist, 2));
	}
}
