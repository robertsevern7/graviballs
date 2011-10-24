package com.graviballs.game.levels;


import java.util.List;

import com.graviballs.game.Goal;


import android.content.SharedPreferences;
import android.content.res.Resources;

public class Level1 extends Level {

	public Level1(Resources resources, SharedPreferences scoreCard, SharedPreferences currentLevel) {
		super(resources, scoreCard, currentLevel);
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0, 0, 0.003f));
	}

	@Override
	void setUpDeflectors() {}
	
	@Override
	int getBallReleaseTiming() {
		return 15;
	}

	@Override
	int getInitialCount() {
		return 1;
	}

	@Override
	int getTimeLimit() {
		return 60;
	}

	@Override
	int getTotalBallCount() {
		return 1;
	}
	
	@Override
	public String getLevelIdentifier() {
		return "1";
	}
}
