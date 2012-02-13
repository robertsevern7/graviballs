package com.graviballs.game.levels;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;
import com.graviballs.TimeUtils;
import com.graviballs.game.*;
import com.graviballs.game.manager.CollisionManager;
import com.graviballs.game.manager.RenderingManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;


public abstract class Level extends Observable {
	private final List<Goal> goals = new ArrayList<Goal>();
	private final List<Deflector> deflectors = new ArrayList<Deflector>();
	private final List<MuddyScreenItem> mud = new ArrayList<MuddyScreenItem>();
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
		setUpMud();
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
	abstract void setUpMud();
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

	public List<MuddyScreenItem> getMud() {
		return mud;
	}

	public List<Pair<Float, Float>> getAttackBallLaunchPoints() {
		List<Pair<Float, Float>> launchPoints = new ArrayList<Pair<Float, Float>>();
		launchPoints.add(new Pair<Float, Float>(2 * mMetersToPixelsX * mHorizontalBound, 2 * mMetersToPixelsY * mVerticalBound));
		launchPoints.add(new Pair<Float, Float>(0f, 2 * mMetersToPixelsY * mVerticalBound));

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

		processDeflectors(mainBall);
		processMud(mainBall);
		drawIncidentals(mainBall);
		drawTheBallBag(mainBall);
		renderingManager.renderScreenItem(mainBall);

		if (startTime == null) {
			startTime = timeToUse;
		}

		drawText(canvas, timeToUse);
	}

	private void drawText(Canvas canvas, final long now) {
		elapsedTime = TimeUtils.getTimeInSeconds(now - startTime);
		final int timeRemaining = getTimeLimit() - elapsedTime;

		if (timeRemaining <= 0) {
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
		for (MuddyScreenItem aMudPatch : mud) {
			renderingManager.renderScreenItem(aMudPatch);
		}
	}

	private void processDeflectors(final Ballable mainBall) {
		for (final Deflector deflector : getDeflectors()) {
			if (collisionManager.circularScreenItemCollision(deflector, mainBall)) {
				collisionManager.deflect(deflector, mainBall);
			}
		}
	}

	private void processMud(final Ballable mainBall) {
		for (final MuddyScreenItem mud : getMud()) {
			if (collisionManager.rectangularItemCollision(mud, mainBall)) {
				collisionManager.slowDown(mainBall);
			}
		}
	}
	
	private void drawTheBallBag(final Ballable mainBall) {
		final Iterator<Ballable> ballBagIterator = ballBag.getIterator();

		while (ballBagIterator.hasNext()) {
			final Ballable ball = ballBagIterator.next();
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
			renderingManager.renderScreenItem(ball);
		}
	}

	private void timeToAddNewBall(final long now) {
		final long scaledNow = now / 1000;
		if (scaledNow - lastBallRelease > getBallReleaseTiming() * 1000000) {
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

}
