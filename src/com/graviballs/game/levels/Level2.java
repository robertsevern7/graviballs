package com.graviballs.game.levels;


import android.content.SharedPreferences;
import android.content.res.Resources;
import com.graviballs.game.Goal;

import java.util.List;

public class Level2 extends Level {

	public Level2(Resources resources, SharedPreferences scoreCard, SharedPreferences currentLevel) {
		super(resources, scoreCard, currentLevel);
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0, -0.5f));
	}
	
	@Override
	void setUpDeflectors() {
	}

	@Override
	void setUpMud() {

	}

	@Override
	int getBallReleaseTiming() {
		return 15;
	}

	@Override
	int getInitialCount() {
		return 2;
	}

	@Override
	int getTimeLimit() {
		return 60;
	}
	
	@Override
	int getTotalBallCount() {
		return 4;
	}
	
	@Override
	public String getLevelIdentifier() {
		return "2";
	}
}
