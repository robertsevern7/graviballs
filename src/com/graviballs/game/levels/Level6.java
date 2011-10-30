package com.graviballs.game.levels;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Pair;

import com.graviballs.game.Deflector;
import com.graviballs.game.Goal;

public class Level6 extends Level {

	public Level6(Resources resources, SharedPreferences scoreCard, SharedPreferences currentLevel) {
		super(resources, scoreCard, currentLevel);
	}

	@Override
	int getInitialCount() {
		return 0;
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0f, -0.4f, 0.003f));
	}

	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		
		float xPos = -0.7f;
		while(xPos <= 0.7f) {
			deflectors.add(new Deflector(xPos, 0f, 0.0015f));
			xPos += 0.15f;
		}	
	}

	@Override
	int getTotalBallCount() {
		return 2;
	}

	@Override
	String getLevelIdentifier() {
		return "6";
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
