package com.graviballs.game.levels;

import java.util.List;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.graviballs.game.Deflector;
import com.graviballs.game.Goal;

public class Level9 extends Level {

	public Level9(Resources resources, SharedPreferences scoreCard, SharedPreferences currentLevel) {
		super(resources, scoreCard, currentLevel);
	}

	@Override
	int getInitialCount() {
		return 2;
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0f, 0f, 0.003f));
	}

	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		
		float xPos = -0.6f;
		while(xPos <= 0.6) {
			deflectors.add(new Deflector(xPos, 0.2f, 0.0015f));
			deflectors.add(new Deflector(xPos, -0.35f, 0.0015f));
			xPos += 0.15f;
		}
	}

	@Override
	int getTotalBallCount() {
		return 3;
	}

	@Override
	String getLevelIdentifier() {
		return "9";
	}

	@Override
	int getBallReleaseTiming() {
		return 30;
	}

	@Override
	int getTimeLimit() {
		return 120;
	}
}
