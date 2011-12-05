package com.graviballs.game.levels;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Pair;

import com.graviballs.game.Deflector;
import com.graviballs.game.Goal;

public class Level5 extends Level {

	public Level5(Resources resources, SharedPreferences scoreCard, SharedPreferences currentLevel) {
		super(resources, scoreCard, currentLevel);
	}

	@Override
	int getInitialCount() {
		return 0;
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(-0.4f, 0.4f));
	}

	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		//deflectors.add(new Deflector(0f, 0f, 0.0015f));
		
		float yPos = -1f;
		while(yPos <= -0.8) {
			deflectors.add(new Deflector(0f, yPos));
			yPos += 0.15f;
		}
		
		yPos = -0.3f;
		while(yPos <= 1) {
			deflectors.add(new Deflector(0f, yPos));
			yPos += 0.15f;
		}
		
		float xPos = -1f;
		while(xPos < -0.8) {
			deflectors.add(new Deflector(xPos, 0f));
			xPos += 0.1f;
		}
		
		xPos = -0.3f;
		while(xPos < -0.1) {
			deflectors.add(new Deflector(xPos, 0f));
			xPos += 0.1f;
		}
		xPos = 0.1f;
		while(xPos <= 0.3) {
			deflectors.add(new Deflector(xPos, 0f));
			xPos += 0.1f;
		}
		
		xPos = 0.7f;
		while(xPos < 1) {
			deflectors.add(new Deflector(xPos, 0f));
			xPos += 0.1f;
		}
	}

	@Override
	int getTotalBallCount() {
		return 1;
	}

	@Override
	String getLevelIdentifier() {
		return "5";
	}

	@Override
	int getBallReleaseTiming() {
		return 30;
	}

	@Override
	int getTimeLimit() {
		return 150;
	}

	public List<Pair<Float, Float>> getAttackBallLaunchPoints() {
		List<Pair<Float, Float>> launchPoints = new ArrayList<Pair<Float, Float>>();
		launchPoints.add(new Pair<Float, Float>(2*mMetersToPixelsX * mHorizontalBound, 2*mMetersToPixelsY * mVerticalBound));
		
		return launchPoints;
	}
}
