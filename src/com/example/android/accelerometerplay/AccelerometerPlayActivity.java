/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.accelerometerplay;

import com.example.android.accelerometerplay.levels.Level;
import com.example.android.accelerometerplay.levels.Level1;
import com.example.android.accelerometerplay.levels.Level2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.BitmapFactory.Options;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public class AccelerometerPlayActivity extends Activity {

    private SimulationView mSimulationView;
    private SensorManager mSensorManager;
    private PowerManager mPowerManager;
    private WindowManager mWindowManager;
    private Display mDisplay;
    private WakeLock mWakeLock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass()
                .getName());

        mSimulationView = new SimulationView(this);
        setContentView(mSimulationView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
        mSimulationView.startSimulation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSimulationView.stopSimulation();
        mWakeLock.release();
    }

    class SimulationView extends View implements SensorEventListener {
        private Sensor mAccelerometer;
        private float mXDpi;
        private float mYDpi;
        private float mMetersToPixelsX;
        private float mMetersToPixelsY;
        private Bitmap mWood;
        private float mXOrigin;
        private float mYOrigin;
        private float mSensorX;
        private float mSensorY;
        private long mSensorTimeStamp;
        private long mCpuTimeStamp;
        private float mHorizontalBound;
        private float mVerticalBound;
        private final Level level = new Level2(getResources());

        public void startSimulation() {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }

        public void stopSimulation() {
            mSensorManager.unregisterListener(this);
        }

        public SimulationView(Context context) {
            super(context);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            mXDpi = metrics.xdpi;
            mYDpi = metrics.ydpi;
            mMetersToPixelsX = mXDpi / 0.0254f;
            mMetersToPixelsY = mYDpi / 0.0254f;
            
            level.setMetersToPixels(mMetersToPixelsX, mMetersToPixelsY);

            // rescale the ball so it's about 0.5 cm on screen
            Options opts = new Options();
            opts.inDither = true;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            mWood = BitmapFactory.decodeResource(getResources(), R.drawable.wood, opts);
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

            switch (mDisplay.getOrientation()) {
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
        	//canvas.drawBitmap(mWood, 0, 0, null);
            final long now = mSensorTimeStamp + (System.nanoTime() - mCpuTimeStamp);
        	level.drawLevel(canvas, now, mSensorX, mSensorY, mXOrigin, mYOrigin, mHorizontalBound, mVerticalBound);

            invalidate();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }
}
