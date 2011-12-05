package com.graviballs.game.levels;

import java.util.List;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.graviballs.game.Deflector;
import com.graviballs.game.Goal;

public class Level10 extends Level {

	public Level10(Resources resources, SharedPreferences scoreCard, SharedPreferences currentLevel) {
		super(resources, scoreCard, currentLevel);
	}

	@Override
	int getInitialCount() {
		return 2;
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0f, 0f));
	}

	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		
		float yPos = -0.2f;
		while (yPos <= 0.8) {
			float xPos = -0.2f;
			while(xPos <= 0.8) {
				deflectors.add(new Deflector(xPos, yPos, 0.0015f));
				xPos += 0.4f;
			}
			yPos += 0.4f;
		}
	}

	@Override
	int getTotalBallCount() {
		return 3;
	}

	@Override
	String getLevelIdentifier() {
		return "10";
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
