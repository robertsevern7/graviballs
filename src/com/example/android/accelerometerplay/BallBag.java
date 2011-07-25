package com.example.android.accelerometerplay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BallBag {
	static final int NUM_PARTICLES = 0;
	private static final float sFriction = 0.3f;
	private long mLastT;
    private float mLastDeltaT;
    private List<Ballable> mBalls = new ArrayList<Ballable>(NUM_PARTICLES);
    private Ballable mainBall = new Ballocks(sFriction, 0.002f);
    private float mHorizontalBound = 0;
    private float mVerticalBound = 0;
    
    public BallBag() {
    }
    
    private Ballable generateRandomBall() {
    	final Ballable ball = new AttackingBallacks(generateRandomRadius(), mainBall);
    	final int xRandomSide = (Math.random() < 0.5) ? -1 : 1;
    	final int yRandomSide = (Math.random() < 0.5) ? -1 : 1;
    	
    	final float initialX = (float) (mHorizontalBound * Math.random() * xRandomSide);
    	final float initialY = (float) (mVerticalBound * Math.random() * yRandomSide);
        ball.setInitialPos(initialX, initialY);
        
        return ball;
    }
    
    private float generateRandomRadius() {
    	return (float) (0.001f + Math.random() * 0.001f);
    }
    
    public Ballable getBall(final int i) {
    	return mBalls.get(i);
    }

    private void updatePositions(float sx, float sy, long timestamp) {
        final long t = timestamp;
        if (mLastT != 0) {
            final float dT = (float) (t - mLastT) * (1.0f / 5000000000.0f);
            if (mLastDeltaT != 0) {
                final float dTC = dT / mLastDeltaT;
                
                mainBall.computePhysics(sx, sy, dT, dTC);
                
                for (final Ballable ball : mBalls) {
                    ball.computePhysics(sx, sy, dT, dTC);
                }
            }
            mLastDeltaT = dT;
        }
        mLastT = t;
    }
    
    public Iterator<Ballable> getIterator() {
    	return mBalls.iterator();
    }
    
    public void updateBounds(float mHorizontalBound, float mVerticalBound) {
    	this.mHorizontalBound = mHorizontalBound;
    	this.mVerticalBound = mVerticalBound;
    }

    public void update(float sx, float sy, long now, float mHorizontalBound, float mVerticalBound) {
    	updateBounds(mHorizontalBound, mVerticalBound);
    	updatePositions(sx, sy, now);

        mainBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);
        
        for (final Ballable ball : mBalls) {
        	ball.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);
        }
    }

    public int getParticleCount() {
        return mBalls.size();
    }

    public float getPosX(int i) {
        return mBalls.get(i).getmPosX();
        
    }

    public float getPosY(int i) {
        return mBalls.get(i).getmPosY();
    }
    
    public Ballable getMainBall() {
    	return mainBall;
    }
    
    public void addBall() {
    	mBalls.add(generateRandomBall());
    }
}
