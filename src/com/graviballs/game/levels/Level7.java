package com.graviballs.game.levels;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Pair;
import com.graviballs.game.Deflector;
import com.graviballs.game.Goal;

import java.util.ArrayList;
import java.util.List;

public class Level7 extends Level {

	public Level7(Resources resources, SharedPreferences scoreCard, SharedPreferences currentLevel) {
		super(resources, scoreCard, currentLevel);
	}

	@Override
	int getInitialCount() {
		return 0;
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0f, -0f));
	}

	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		
		deflectors.add(new Deflector(0.3f, -0.2f));
		deflectors.add(new Deflector(0.15f, -0.2f));
		deflectors.add(new Deflector(0f, -0.2f));
		deflectors.add(new Deflector(-0.13f, -0.16f));
		deflectors.add(new Deflector(-0.23f, -0.1f));
		deflectors.add(new Deflector(0.3f, 0.2f));
		deflectors.add(new Deflector(0.15f, 0.2f));
		deflectors.add(new Deflector(0f, 0.2f));
		deflectors.add(new Deflector(-0.13f, 0.16f));
		deflectors.add(new Deflector(-0.23f, 0.1f));
		deflectors.add(new Deflector(-0.32f, 0f));
	}

	@Override
	void setUpMud() {

	}

	@Override
	int getTotalBallCount() {
		return 1;
	}

	@Override
	String getLevelIdentifier() {
		return "7";
	}

	@Override
	int getBallReleaseTiming() {
		return 30;
	}

	@Override
	int getTimeLimit() {
		return 150;
	}
	
	@Override
	public List<Pair<Float, Float>> getAttackBallLaunchPoints() {
		List<Pair<Float, Float>> launchPoints = new ArrayList<Pair<Float, Float>>();
		launchPoints.add(new Pair<Float, Float>(0f, 2*mMetersToPixelsY * mVerticalBound));
		
		return launchPoints;
	}
}
