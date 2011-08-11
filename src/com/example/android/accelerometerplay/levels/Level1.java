package com.example.android.accelerometerplay.levels;

import java.util.List;

import com.example.android.accelerometerplay.Goal;

import android.content.res.Resources;

public class Level1 extends Level {

	public Level1(Resources resources) {
		super(resources);
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0, 0.33f, 0.001f));
	}

	@Override
	void setUpDeflectors() {}
	
	@Override
	int getBallReleaseTiming() {
		return 20;
	}

	@Override
	int getInitialCount() {
		return 1;
	}
}
