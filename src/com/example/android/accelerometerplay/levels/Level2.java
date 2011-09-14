package com.example.android.accelerometerplay.levels;

import java.util.List;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Pair;
import com.example.android.accelerometerplay.Goal;

public class Level2 extends Level {

	public Level2(Resources resources, SharedPreferences scoreCard) {
		super(resources, scoreCard);
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0, -0.5f, 0.003f));
	}
	
	@Override
	void setUpDeflectors() {
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
	Pair<Float, Float> getInitialMainBallPosition() {
		return new Pair<Float, Float>(0f, 0.01f);
	}
	
	@Override
	public String getLevelIdentifier() {
		return "2";
	}
}
