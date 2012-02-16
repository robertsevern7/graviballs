package com.graviballs.game;

import android.util.Pair;

public enum Direction {
	NORTH {
		@Override public Pair<Float, Float> getVelocity() {
			return new Pair<Float, Float>(-DEFAULT_SPEED, NO_SPEED);
		}
	},
	SOUTH {
		@Override public Pair<Float, Float> getVelocity() {
			return new Pair<Float, Float>(DEFAULT_SPEED, NO_SPEED);
		}
	},
	EAST {
		@Override public Pair<Float, Float> getVelocity() {
			return new Pair<Float, Float>(NO_SPEED, DEFAULT_SPEED);
		}
	},
	WEST {
		@Override public Pair<Float, Float> getVelocity() {
			return new Pair<Float, Float>(NO_SPEED, -DEFAULT_SPEED);
		}
	};

	private static final Float DEFAULT_SPEED = 3f;
	private static final Float NO_SPEED = 0f;

	public abstract Pair<Float, Float> getVelocity();
}
