package com.graviballs.game.levels;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;
import com.graviballs.TimeUtils;
import com.graviballs.game.BallBag;
import com.graviballs.game.Ballable;
import com.graviballs.game.Deflector;
import com.graviballs.game.Goal;
import com.graviballs.game.manager.CollisionManager;
import com.graviballs.game.manager.RenderingManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;


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
	private CollisionManager collisionManager;
	private RenderingManager renderingManager;
	
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
	abstract int getBallReleaseTiming();
	abstract int getTimeLimit();

	public void setBounds(final float mHorizontalBound, final float mVerticalBound) {
		this.mHorizontalBound = mHorizontalBound;
		this.mVerticalBound = mVerticalBound;
		ballBag.setBounds(mHorizontalBound, mVerticalBound);

		if (ballBag.getAttackBallLaunchPoints().isEmpty()) {
			ballBag.setAttackBallLaunchPoints(getAttackBallLaunchPoints());
		}
		if (collisionManager == null) {
			collisionManager = new CollisionManager(mHorizontalBound, mVerticalBound);
		}
	}
	
	List<Goal> getGoals() {
		return goals;
	}
	
	List<Deflector> getDeflectors() {
		return deflectors;
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
		if (renderingManager == null) {
			renderingManager = new RenderingManager(canvas, mMetersToPixelsX, mMetersToPixelsY, mHorizontalBound, mVerticalBound, mXOrigin, mYOrigin, resources);
		}
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

		renderingManager.renderScreenItem(mainBall);

		drawIncidentals(mainBall);

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

	private void drawIncidentals(final Ballable mainBall) {
		for (final Goal goal : getGoals()) {
        	if (collisionManager.circularScreenItemCollision(goal, mainBall)) {
	        	passLevel();
        	}
			renderingManager.renderScreenItem(goal);
        }
        
        for (final Deflector deflector : getDeflectors()) {
			renderingManager.renderScreenItem(deflector);
		}
	}

	private void processDeflectors(final Ballable mainBall) {
		for (final Deflector deflector : getDeflectors()) {
        	if (collisionManager.circularScreenItemCollision(deflector, mainBall)) {
        		deflect(deflector, mainBall);
        	}
        }
	}

	private void drawTheBallBag(Canvas canvas, final float mXOrigin, final float mYOrigin, final Ballable mainBall) {

		final Iterator<Ballable> ballBagIterator = ballBag.getIterator();
        
        while(ballBagIterator.hasNext()) {
        	final Ballable ball = ballBagIterator.next();
            final float x1 = mXOrigin + (ball.getXProportion() * mHorizontalBound - ball.getRadius(mHorizontalBound)) * mMetersToPixelsX;
            final float y1 = mYOrigin - (ball.getYProportion() * mVerticalBound + ball.getRadius(mHorizontalBound)) * mMetersToPixelsY;
            
            if (collisionManager.circularScreenItemCollision(mainBall, ball)) {
            	failLevel();
            }
            
            for (final Goal goal : getGoals()) {
            	if (collisionManager.circularScreenItemCollision(goal, ball)) {
            		++totalBallsScored;
            		ballBagIterator.remove();
            	}
            }
            
            processDeflectors(ball);

            canvas.drawBitmap(ball.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY, mHorizontalBound), x1, y1, null);
        }
	}

	private void timeToAddNewBall(final long now) {
		final long scaledNow = now / 1000;
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
	
	private void failLevel() {
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
		final double xDist = (ball.getXProportion() - deflector.getXProportion()) * mHorizontalBound;
		final double yDist = (ball.getYProportion() - deflector.getYProportion()) * mVerticalBound;
		
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

}
