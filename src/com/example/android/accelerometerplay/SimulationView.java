package com.example.android.accelerometerplay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import com.example.android.accelerometerplay.levels.Level;
import com.example.android.accelerometerplay.levels.Level1;
import com.example.android.accelerometerplay.levels.Level2;

/**
* Author: Rahul Gidwani
* Copyright 2011 - Viglink Inc.
*/
public class SimulationView extends View implements SensorEventListener {
	private Sensor mAccelerometer;
	private float mXDpi;
	private float mYDpi;
	private float mMetersToPixelsX;
	private float mMetersToPixelsY;
	private float mXOrigin;
	private float mYOrigin;
	private float mSensorX;
	private float mSensorY;
	private long mSensorTimeStamp;
	private long mCpuTimeStamp;
	private float mHorizontalBound;
	private float mVerticalBound;
	private final Level level = new Level2(getResources());
	private AccelerometerPlayActivity accelerometerPlayActivity;

	public void startSimulation() {
		accelerometerPlayActivity.mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
	}

	public void stopSimulation() {
		accelerometerPlayActivity.mSensorManager.unregisterListener(this);
	}

	public SimulationView(AccelerometerPlayActivity accelerometerPlayActivity, Context context) {
		super(context);
		this.accelerometerPlayActivity = accelerometerPlayActivity;
		mAccelerometer = accelerometerPlayActivity.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		DisplayMetrics metrics = new DisplayMetrics();
		accelerometerPlayActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mXDpi = metrics.xdpi;
		mYDpi = metrics.ydpi;
		mMetersToPixelsX = mXDpi / 0.0254f;
		mMetersToPixelsY = mYDpi / 0.0254f;

		level.setMetersToPixels(mMetersToPixelsX, mMetersToPixelsY);

		// rescale the ball so it's about 0.5 cm on screen
		BitmapFactory.Options opts = new BitmapFactory.Options();
//            opts.inDither = true;
		opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// compute the origin of the screen relative to the origin of
		// the bitmap
		mXOrigin = w * 0.5f;
		mYOrigin = h * 0.5f;
		mHorizontalBound = (w / mMetersToPixelsX) * 0.5f;
		mVerticalBound = (h / mMetersToPixelsY) * 0.5f;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
			return;

		switch (accelerometerPlayActivity.mDisplay.getOrientation()) {
			case Surface.ROTATION_0:
				mSensorX = event.values[0];
				mSensorY = event.values[1];
				break;
			case Surface.ROTATION_90:
				mSensorX = -event.values[1];
				mSensorY = event.values[0];
				break;
			case Surface.ROTATION_180:
				mSensorX = -event.values[0];
				mSensorY = -event.values[1];
				break;
			case Surface.ROTATION_270:
				mSensorX = event.values[1];
				mSensorY = -event.values[0];
				break;
		}

		mSensorTimeStamp = event.timestamp;
		mCpuTimeStamp = System.nanoTime();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//TODO render this once. And make it not wood
		final long now = mSensorTimeStamp + (System.nanoTime() - mCpuTimeStamp);
		level.drawLevel(canvas, now, mSensorX, mSensorY, mXOrigin, mYOrigin, mHorizontalBound, mVerticalBound);
		invalidate();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
