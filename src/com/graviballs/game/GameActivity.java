package com.graviballs.game;

import java.util.Observable;
import java.util.Observer;
import com.graviballs.menus.LevelCompleteActivity;
import android.view.*;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class GameActivity extends Activity implements Observer {

    private SimulationView mSimulationView;
    SensorManager mSensorManager;
    private PowerManager mPowerManager;
    private WindowManager mWindowManager;
    Display mDisplay;
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

        mSimulationView = new SimulationView(this, this);
        setContentView(mSimulationView);
        mSimulationView.getGameLevel().addObserver(this);
        
        registerForContextMenu(mSimulationView);
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

    @Override
	public void update(Observable arg0, Object arg1) {
    	mSimulationView.stopSimulation();
		Intent showContent = new Intent(getApplicationContext(), LevelCompleteActivity.class);
		startActivity(showContent);
		finish();
	}
}
