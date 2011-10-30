package com.graviballs.game.levels;

import java.util.List;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.graviballs.game.Deflector;
import com.graviballs.game.Goal;

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
		goals.add(new Goal(0f, -0f, 0.003f));
	}

	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		
		deflectors.add(new Deflector(0.3f, -0.15f, 0.0015f));
		deflectors.add(new Deflector(0.15f, -0.15f, 0.0015f));
		deflectors.add(new Deflector(0f, -0.15f, 0.0015f));
		deflectors.add(new Deflector(-0.13f, -0.13f, 0.0015f));
		deflectors.add(new Deflector(-0.23f, -0.07f, 0.0015f));
		deflectors.add(new Deflector(0.3f, 0.15f, 0.0015f));
		deflectors.add(new Deflector(0.15f, 0.15f, 0.0015f));
		deflectors.add(new Deflector(0f, 0.15f, 0.0015f));
		deflectors.add(new Deflector(-0.13f, 0.13f, 0.0015f));
		deflectors.add(new Deflector(-0.23f, 0.07f, 0.0015f));
		deflectors.add(new Deflector(-0.32f, 0f, 0.0015f));
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
}
