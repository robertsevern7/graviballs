package com.graviballs.game.levels;


import java.util.List;

import com.graviballs.game.Deflector;
import com.graviballs.game.Goal;

import android.content.SharedPreferences;
import android.content.res.Resources;

public class Level3 extends Level {

	public Level3(Resources resources, SharedPreferences scoreCard, SharedPreferences currentLevel) {
		super(resources, scoreCard, currentLevel);
	}

	@Override
	int getInitialCount() {
		return 0;
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0, -0.33f, 0.002f));

	}

	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		deflectors.add(new Deflector(0, 0.33f, 0.002f));
	}

	@Override
	int getBallReleaseTiming() {
		return 15;
	}

	@Override
	int getTimeLimit() {
		return 120;
	}

	@Override
	int getTotalBallCount() {
		return 3;
	}
	
	@Override
	public String getLevelIdentifier() {
		return "3";
	}
}
