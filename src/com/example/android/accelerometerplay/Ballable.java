package com.example.android.accelerometerplay;

public abstract class Ballable {
	float mPosX;
    float mPosY;
    private float mAccelX;
    private float mAccelY;
    float mLastPosX;
    float mLastPosY;
    private float mOneMinusFriction;
    private float radius;
    
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

        /*
         * �F = mA <=> A = �F / m We could simplify the code by
         * completely eliminating "m" (the mass) from all the equations,
         * but it would hide the concepts from this sample code.
         */
        final float invm = 1.0f / m;
        final float ax = gx * invm;
        final float ay = gy * invm;

        /*
         * Time-corrected Verlet integration The position Verlet
         * integrator is defined as x(t+�t) = x(t) + x(t) - x(t-�t) +
         * a(t)�t�2 However, the above equation doesn't handle variable
         * �t very well, a time-corrected version is needed: x(t+�t) =
         * x(t) + (x(t) - x(t-�t)) * (�t/�t_prev) + a(t)�t�2 We also add
         * a simple friction term (f) to the equation: x(t+�t) = x(t) +
         * (1-f) * (x(t) - x(t-�t)) * (�t/�t_prev) + a(t)�t�2
         */
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
	
	/*
     * Resolving constraints and collisions with the Verlet integrator
     * can be very simple, we simply need to move a colliding or
     * constrained particle in such way that the constraint is
     * satisfied.
     */
    /* (non-Javadoc)
	 * @see com.example.android.accelerometerplay.Ballable#resolveCollisionWithBounds(float, float)
	 */
	public void resolveCollisionWithBounds(final float mHorizontalBound, float mVerticalBound) {
        final float xmax = mHorizontalBound;
        final float ymax = mVerticalBound;
        final float x = mPosX;
        final float y = mPosY;
        if (x > xmax) {
            mPosX = xmax;
        } else if (x < -xmax) {
            mPosX = -xmax;
        }
        if (y > ymax) {
            mPosY = ymax;
        } else if (y < -ymax) {
            mPosY = -ymax;
        }
    }
	
	public void setInitialPos(float x, float y) {
		setmPosX(x);
		setmPosY(y);
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
		System.out.print(Math.pow(radius, 3));
		System.out.print(Math.pow(radius, 3)/3f);
		System.out.print((float) (4 * Math.PI * Math.pow(radius, 3)/3f));
		return 1f;//(float) (4 * Math.PI * Math.pow(radius, 3)/3f);
	}

	public float getRadius() {
		return radius;
	}
}