package com.graviballs.game.levels;

import android.content.SharedPreferences;
import android.content.res.Resources;
import com.graviballs.game.Deflector;
import com.graviballs.game.Goal;

import java.util.List;

public class Level8 extends Level {

	public Level8(Resources resources, SharedPreferences scoreCard, SharedPreferences currentLevel) {
		super(resources, scoreCard, currentLevel);
	}

	@Override
	int getInitialCount() {
		return 0;
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(-0.85f, -0.85f));
	}

	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		
		deflectors.add(new Deflector(-0.7f, -0.93f));
		deflectors.add(new Deflector(-0.93f, -0.7f));
		deflectors.add(new Deflector(-0.83f, -0.6f));
		deflectors.add(new Deflector(-0.73f, -0.5f));
		deflectors.add(new Deflector(-0.4f, -0.63f));
		deflectors.add(new Deflector(-0.63f, -0.4f));
		deflectors.add(new Deflector(-0.3f, -0.53f));
		deflectors.add(new Deflector(-0.53f, -0.3f));
		deflectors.add(new Deflector(-0.2f, -0.43f));
		deflectors.add(new Deflector(-0.43f, -0.2f));
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
		return "8";
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
