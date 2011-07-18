package com.example.android.accelerometerplay;

public class BallBag {
	static final int NUM_PARTICLES = 3;
	private static final float sFriction = 0.1f;
	private long mLastT;
    private float mLastDeltaT;
    private Ballable mBalls[] = new Ballable[NUM_PARTICLES];
    private Ballable mainBall = new Ballocks(sFriction, 0.003f);
    
    public BallBag() {
        for (int i = 0; i < mBalls.length; i++) {
            mBalls[i] = new AttackingBallacks(generateRandomRadius(), mainBall);
            
            final float initialX = generateRandomPosition(mainBall.getmPosX());
            final float initialY = generateRandomPosition(mainBall.getmPosY());
            mBalls[i].setInitialPos(initialX, initialY);
        }
    }
    
    private float generateRandomRadius() {
    	return (float) (0.001f + Math.random() * 0.001f);
    }
    
    private float generateRandomPosition(float relativeFrom) {
    	final int posOrNeg = Math.random() > 0.5 ? 1 : -1;
    	return (float) (mainBall.getmPosX() + Math.random() * 0.05f * posOrNeg);
    }
    
    public Ballable getBall(final int i) {
    	return mBalls[i];
    }

    private void updatePositions(float sx, float sy, long timestamp) {
        final long t = timestamp;
        if (mLastT != 0) {
            final float dT = (float) (t - mLastT) * (1.0f / 5000000000.0f);
            if (mLastDeltaT != 0) {
                final float dTC = dT / mLastDeltaT;
                
                mainBall.computePhysics(sx, sy, dT, dTC);
                
                final int count = mBalls.length;
                for (int i = 0; i < count; i++) {
                    Ballable ball = mBalls[i];
                    ball.computePhysics(sx, sy, dT, dTC);
                }
            }
            mLastDeltaT = dT;
        }
        mLastT = t;
    }

    public void update(float sx, float sy, long now, float mHorizontalBound, float mVerticalBound) {
        updatePositions(sx, sy, now);

        mainBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);
        
        for (int i = 0; i < mBalls.length; ++i) {
        	mBalls[i].resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);
        }
    }

    public int getParticleCount() {
        return mBalls.length;
    }

    public float getPosX(int i) {
        return mBalls[i].getmPosX();
    }

    public float getPosY(int i) {
        return mBalls[i].getmPosY();
    }
    
    public Ballable getMainBall() {
    	return mainBall;
    }
}
