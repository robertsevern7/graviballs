package com.graviballs.game;

import com.graviballs.R;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class GameLayout extends RelativeLayout {
	private SimulationView mSimulationView;
	private View inLevelMenu;
	public GameLayout(final GameActivity gameActivity, Context context) {
		super(context);
		
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 
	            LayoutParams.FILL_PARENT));
		
		mSimulationView = new SimulationView(gameActivity, context);
		addView(mSimulationView);
		
		inLevelMenu = View.inflate(context, R.layout.inlevelmenu, null);
		
		addView(inLevelMenu);
		inLevelMenu.setVisibility(INVISIBLE);
		Button resume = (Button)findViewById(R.id.resume_button);
		resume.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				inLevelMenu.setVisibility(INVISIBLE);
	    		mSimulationView.pause();
			}
		});
		
		Button quit = (Button)findViewById(R.id.quit_button);
		quit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gameActivity.finish();
			}
		});		
	}
	
	public SimulationView getSimulationView() {
		return mSimulationView;
	}
	
	public View getInLevelMenu() {
		return inLevelMenu;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
    	super.onTouchEvent(e);
    	
    	if (inLevelMenu.getVisibility() == INVISIBLE) {
    		inLevelMenu.setVisibility(VISIBLE);
    		mSimulationView.pause();
    	}
    	
		return false;
	}
}
