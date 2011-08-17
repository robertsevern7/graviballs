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
		goals.add(new Goal(0, 0.33f, 0.002f));
		goals.add(new Goal(0, -0.33f, 0.002f));
	}
	
	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		deflectors.add(new Deflector(0.33f, 0.33f, 0.003f));
	}

	@Override
	int getBallReleaseTiming() {
		return 30;
	}

	@Override
	int getInitialCount() {
		return 1;
	}
}
