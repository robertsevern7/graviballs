package com.example.android.accelerometerplay.levels;

import java.util.List;

import android.content.res.Resources;

import com.example.android.accelerometerplay.Deflector;
import com.example.android.accelerometerplay.Goal;

public class Level3 extends Level {

	public Level3(Resources resources) {
		super(resources);
	}

	@Override
	int getInitialCount() {
		return 0;
	}

	@Override
	void setUpGoals() {
		final List<Goal> goals = getGoals();
		goals.add(new Goal(0, 0, 0.003f));

	}

	@Override
	void setUpDeflectors() {
		final List<Deflector> deflectors = getDeflectors();
		deflectors.add(new Deflector(0.33f, 0, 0.003f));
		
		//TODO anything offcenter in the y direction doesn't work for deflection
		deflectors.add(new Deflector(0, 0.33f, 0.003f));
		//deflectors.add(new Deflector(0.33f, -0.33f, 0.003f));
		//deflectors.add(new Deflector(-0.33f, -0.33f, 0.003f));
	}

	@Override
	int getBallReleaseTiming() {
		return 30;
	}

}
