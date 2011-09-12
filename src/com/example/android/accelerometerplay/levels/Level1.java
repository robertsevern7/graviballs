package com.example.android.accelerometerplay.levels;

import java.util.List;

import com.example.android.accelerometerplay.Goal;

import android.content.res.Resources;
import android.util.Pair;

public class Level1 extends Level {

	public Level1(Resources resources) {
		super(resources);
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0, 0, 0.004f));
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

	@Override
	int getTimeLimit() {
		return 0;
	}

	@Override
	int getTotalBallCount() {
		return 5;
	}

	@Override
	Pair<Float, Float> getInitialMainBallPosition() {
		return new Pair<Float, Float>(0f, 0f);
	}
}
