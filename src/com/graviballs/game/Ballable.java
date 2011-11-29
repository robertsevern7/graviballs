package com.graviballs.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;
import com.graviballs.R;

public abstract class Ballable extends CircularScreenItem {
    private float mAccelX;
    private float mAccelY;
    float mLastPosX;
    float mLastPosY;
    private float mOneMinusFriction;
    float initialSpeedX;
    float initialSpeedY;
    private float currentSpeedX;
    private float currentSpeedY;
    private Bitmap bitmap;
    
    final private float SPEED_LIMIT = 0.3f;
    
	public Ballable(final float sFriction, final float mPosXProp, final float mPosYProp, final float radius) {
		super(mPosXProp, mPosYProp, radius);
		float randomFriction = ((float) Math.random() - 0.5f) * 0.2f;
		setmOneMinusFriction(1.0f - sFriction + randomFriction);
		initialSpeedX = 0f;
		initialSpeedY = 0f;
	}

	public void computePhysics(float sx, float sy, float dT, float dTC) {
        // Force of gravity applied to our virtual object
        final float m = 1000.0f; // mass of our virtual object
        final float gx = -sx * m;
        final float gy = -sy * m;


        final float invm = 1.0f / m;
        final float ax = gx * invm;
        final float ay = gy * invm;

        setVelocity(getVelocity().first + mAccelX * dT, (float) getVelocity().second + mAccelY * dT);
       
        final float dTdT = dT * dT;
        final float x = getXProportion() + mOneMinusFriction * dT * currentSpeedX + mAccelX
                * dTdT;
        final float y = getYProportion() + mOneMinusFriction * dT * currentSpeedY + mAccelY
                * dTdT;
        mLastPosX = getXProportion();
        mLastPosY = getYProportion();
		setmPosXProp(x);
        setmPosYProp(y);
        mAccelX = ax;
        mAccelY = ay;
    }
	
	public void resolveCollisionWithBounds(final float mHorizontalBound, float mVerticalBound) {
        final float xmax = mHorizontalBound - getRadius();
        final float ymax = mVerticalBound - getRadius();
        final float x = getXProportion();
        final float y = getYProportion();
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
		final double speed = Math.sqrt(Math.pow(xVel, 2) + Math.pow(yVel, 2)); 
		final double speedRatio = Math.max(speed / SPEED_LIMIT, 1);
		currentSpeedX = (float) (xVel/speedRatio);
		currentSpeedY = (float) (yVel/speedRatio);
	}

	public void setmOneMinusFriction(final float mOneMinusFriction) {
		this.mOneMinusFriction = mOneMinusFriction;
	}
	
	public float getMass() {
		return (float) (4 * Math.PI * Math.pow(getRadius(), 3)/3f);
	}

	public int getDrawable() {
		return R.drawable.ball; 
	}
	
	public Bitmap getBitmap(final Resources resources, final float mMetersToPixelsX, final float mMetersToPixelsY) {
		if (bitmap == null) {
			final Bitmap ball = BitmapFactory.decodeResource(resources, getDrawable());
			final int dstWidth = (int) (getRadius() * 2 * mMetersToPixelsX + 0.5f);
	        final int dstHeight = (int) (getRadius() * 2 * mMetersToPixelsY + 0.5f);
	        bitmap =  Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
		}
		
		return bitmap;		
	}
}