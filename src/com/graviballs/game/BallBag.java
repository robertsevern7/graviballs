package com.graviballs.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Pair;

public class BallBag {
	private static final float sFriction = 0.07f;
	private long mLastT;
    private float mLastDeltaT;
    private List<Ballable> mBalls = new ArrayList<Ballable>();
    private final Ballable mainBall;
    private float mHorizontalBound = 0;
    private float mVerticalBound = 0;
    List<Pair<Float, Float>> launchPoints = new ArrayList<Pair<Float, Float>>();
    
    public BallBag() {
    	mainBall = new Ballocks(sFriction, 0.002f);
    }
    
    public void setBounds(final float mHorizontalBound, final float mVerticalBound) {
		this.mHorizontalBound = mHorizontalBound;
		this.mVerticalBound = mVerticalBound;
	}
    
    public void setAttackBallLaunchPoints(List<Pair<Float, Float>> launchPoints) {
    	this.launchPoints = launchPoints;
	}
    
    public List<Pair<Float, Float>> getAttackBallLaunchPoints() {
    	return launchPoints;
	}
    
    private Ballable generateRandomBall() {
    	final Ballable ball = new AttackingBallacks(generateRandomRadius(), mainBall);
    	
    	final int launchOptions = launchPoints.size();
    	final double scaledRandom = Math.random() * launchOptions;
    	
    	Pair<Float, Float> startPoint = launchPoints.get(0);
    	for (int i = 0; i < launchOptions; ++i) {
    		if (scaledRandom > i && scaledRandom < i + 1) {
    			startPoint = launchPoints.get(i);
    			break;
    		}
    	}
    	
        ball.setInitialPos(startPoint.first - mHorizontalBound, -startPoint.second + mVerticalBound);
        return ball;
    }
    
    public boolean isEmpty() {
    	return mBalls.isEmpty();
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

    public void update(float sx, float sy, long now) {
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
