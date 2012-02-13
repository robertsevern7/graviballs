package com.graviballs.game.levels;

import android.content.SharedPreferences;
import android.content.res.Resources;
import com.graviballs.game.Deflector;
import com.graviballs.game.Goal;
import com.graviballs.game.Wall;

import java.util.List;

public class Level4 extends Level {

	public Level4(Resources resources, SharedPreferences scoreCard, SharedPreferences currentLevel) {
		super(resources, scoreCard, currentLevel);
	}

	@Override
	int getInitialCount() {
		return 0;
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0, 0));

	}

	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		//deflectors.add(new Deflector(0.33f, 0.33f));
		//deflectors.add(new Deflector(-0.33f, -0.33f));
	}

	@Override
	void setUpMud() {}
	
	@Override
	void setUpWalls() {
		final List<Wall> walls = getWalls();
		walls.add(new Wall(.6f, .6f, .2f, .2f));
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
		return 4;
	}
	
	@Override
	public String getLevelIdentifier() {
		return "4";
	}
}
