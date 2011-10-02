package com.graviballs.game.levels;


import java.util.List;

import com.graviballs.game.Deflector;
import com.graviballs.game.Goal;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Pair;


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
		goals.add(new Goal(0, 0, 0.003f));

	}

	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		deflectors.add(new Deflector(0.33f, 0.33f, 0.002f));
		
		//TODO anything offcenter in the y direction doesn't work for deflection
		deflectors.add(new Deflector(-0.33f, 0.33f, 0.002f));
		deflectors.add(new Deflector(0.33f, -0.33f, 0.002f));
		deflectors.add(new Deflector(-0.33f, -0.33f, 0.002f));
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
		return 5;
	}
	
	@Override
	Pair<Float, Float> getInitialMainBallPosition() {
		return new Pair<Float, Float>(0.006f, 0.006f);
	}
	
	@Override
	public String getLevelIdentifier() {
		return "3";
	}
}
