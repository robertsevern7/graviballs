package com.example.android.accelerometerplay.levels;

import android.content.res.Resources;

public class Level1 extends Level {

	public Level1(Resources resources) {
		super(resources);
	}

	@Override
	void setUpGoals() {
	}

	@Override
	int getBallReleaseTiming() {
		return 10;
	}
}
