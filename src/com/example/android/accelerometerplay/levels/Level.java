package com.example.android.accelerometerplay.levels;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.android.accelerometerplay.BallBag;
import com.example.android.accelerometerplay.Ballable;
import com.example.android.accelerometerplay.Goal;
import com.example.android.accelerometerplay.R;

public abstract class Level {
	private final List<Goal> goals = new ArrayList<Goal>();
	private final BallBag ballBag = new BallBag();
	private final Resources resources;
	float mMetersToPixelsX = 0;
	float mMetersToPixelsY = 0;
	
	public Level(Resources resources) {
		setUpGoals();
		this.resources = resources;
	}
	
	abstract void setUpGoals();
	
	abstract int getBallReleaseTiming();
	
	public void setMetersToPixels(final float mMetersToPixelsX, final float mMetersToPixelsY) {
		this.mMetersToPixelsX = mMetersToPixelsX;
		this.mMetersToPixelsY = mMetersToPixelsY;
	}
	
	public void drawLevel(Canvas canvas, final long now, final float mSensorX, final float mSensorY,
			final float mXOrigin, final float mYOrigin,
			final float mHorizontalBound, final float mVerticalBound) {

        ballBag.update(mSensorX, mSensorY, now, mHorizontalBound, mVerticalBound);
        
        final Ballable mainBall = ballBag.getMainBall();
        final float x = mXOrigin + (mainBall.getmPosX() - mainBall.getRadius()) * mMetersToPixelsX;
        final float y = mYOrigin - (mainBall.getmPosY() + mainBall.getRadius()) * mMetersToPixelsY;
        
        canvas.drawBitmap(createBitmap(mainBall.getRadius()), x, y, null);
        
        final int count = ballBag.getParticleCount();
        for (int i = 0; i < count; i++) {
            final float x1 = mXOrigin + (ballBag.getPosX(i) - ballBag.getBall(i).getRadius()) * mMetersToPixelsX;
            final float y1 = mYOrigin - (ballBag.getPosY(i) + ballBag.getBall(i).getRadius()) * mMetersToPixelsY;
            canvas.drawBitmap(createBitmap(ballBag.getBall(i).getRadius()), x1, y1, null);
        }
	}
	
	private Bitmap createBitmap(final float radius) {
		Bitmap ball = BitmapFactory.decodeResource(resources, R.drawable.ball);
        final int dstWidth = (int) (radius * 2 * mMetersToPixelsX + 0.5f);
        final int dstHeight = (int) (radius * 2 * mMetersToPixelsY + 0.5f);
        return Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
	}
}
