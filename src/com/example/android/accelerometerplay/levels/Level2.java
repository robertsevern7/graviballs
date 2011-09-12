package com.example.android.accelerometerplay.levels;

import java.util.List;

import android.content.res.Resources;

import com.example.android.accelerometerplay.Deflector;
import com.example.android.accelerometerplay.Goal;

public class Level2 extends Level {

	public Level2(Resources resources) {
		super(resources);
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0, -0.5f, 0.002f));
	}
	
	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		deflectors.add(new Deflector(0.33f, 0, 0.006f));
	}

	@Override
	int getBallReleaseTiming() {
		return 300;
	}

	@Override
	int getInitialCount() {
		return 1;
	}

	@Override
	int getTimeLimit() {
		return 0;
	}
	
	@Override
	int getTotalBallCount() {
		return 5;
	}
}
