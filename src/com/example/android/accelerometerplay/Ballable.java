package com.example.android.accelerometerplay;

import android.util.Pair;

public abstract class Ballable {
	float mPosX;
    float mPosY;
    private float mAccelX;
    private float mAccelY;
    float mLastPosX;
    float mLastPosY;
    private float mOneMinusFriction;
    private float radius;
    float initialSpeedX;
    float initialSpeedY;
    float currentSpeedX;
    float currentSpeedY;
    
    Ballable(final float sFriction, final float radius) {
        // make each particle a bit different by randomizing its
        // coefficient of friction
        final float r = ((float) Math.random() - 0.5f) * 0.2f;
        this.radius = radius;
        setmOneMinusFriction(1.0f - sFriction + r);
    }

	public void computePhysics(float sx, float sy, float dT, float dTC) {
        // Force of gravity applied to our virtual object
        final float m = 1000.0f; // mass of our virtual object
        final float gx = -sx * m;
        final float gy = -sy * m;


        final float invm = 1.0f / m;
        final float ax = gx * invm;
        final float ay = gy * invm;


        final float dTdT = dT * dT;
        final float x = mPosX + mOneMinusFriction * dTC * (mPosX - mLastPosX) + mAccelX
                * dTdT;
        final float y = mPosY + mOneMinusFriction * dTC * (mPosY - mLastPosY) + mAccelY
                * dTdT;
        mLastPosX = mPosX;
        mLastPosY = mPosY;
        mPosX = x;
        mPosY = y;
        mAccelX = ax;
        mAccelY = ay;
    }
	
	public void resolveCollisionWithBounds(final float mHorizontalBound, float mVerticalBound) {
        final float xmax = mHorizontalBound - radius;
        final float ymax = mVerticalBound - radius;
        final float x = mPosX;
        final float y = mPosY;
        final float slowingValue = 0.3f;
        if (x > xmax) {
            mPosX = xmax;
            currentSpeedX = -currentSpeedX * slowingValue;
        } else if (x < -xmax) {
            mPosX = -xmax;
            currentSpeedX = -currentSpeedX * slowingValue;
        }
        if (y > ymax) {
        	currentSpeedY = -currentSpeedY * slowingValue;
            mPosY = ymax;
        } else if (y < -ymax) {
        	currentSpeedY = -currentSpeedY * slowingValue;
            mPosY = -ymax;
        }
    }
	
	public Pair<Float, Float> getVelocity() {
		return new Pair<Float, Float>(currentSpeedX, currentSpeedY);
	}
	
	public void setVelocity(final float xVel, final float yVel) {
		currentSpeedX = xVel;
		currentSpeedY = yVel;
	}
	
	public void setInitialPos(float x, float y) {
		setmPosX(x);
		setmPosY(y);
		
		initialSpeedX = (float) (-Math.random() * 3 * x);
		initialSpeedY = (float) (-Math.random() * 3 * y);
	}

	public float getmPosX() {
		return mPosX;
	}

	public void setmPosX(float mPosX) {
		this.mPosX = mPosX;
	}

	public void setmPosY(float mPosY) {
		this.mPosY = mPosY;
	}

	public float getmPosY() {
		return mPosY;
	}
	
	public void setmOneMinusFriction(final float mOneMinusFriction) {
		this.mOneMinusFriction = mOneMinusFriction;
	}
	
	public float getMass() {
		return (float) (4 * Math.PI * Math.pow(radius, 3)/3f);
	}

	public float getRadius() {
		return radius;
	}
	
	public void revertToPreviousPosition() {
		mPosX = mLastPosX;
		mPosY = mLastPosY;
	}
}