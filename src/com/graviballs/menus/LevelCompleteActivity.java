package com.graviballs.menus;

import com.graviballs.R;
import com.graviballs.game.GameActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LevelCompleteActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_complete);
	
		Button startMenu = (Button)findViewById(R.id.start_menu_button);
		startMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showContent = new Intent(getApplicationContext(), StartPageActivity.class);
				startActivity(showContent);
			}
		});
		
		Button levelsMenu = (Button)findViewById(R.id.menu_button);
		levelsMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showContent = new Intent(getApplicationContext(), LevelsMenuActivity.class);
				startActivity(showContent);
			}
		});
		
		Button play = (Button)findViewById(R.id.next_button);
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences CURRENT_LEVEL = getSharedPreferences("CurrentLevel", 0);
				final int currentLevel = CURRENT_LEVEL.getInt("level", 0);
				SharedPreferences.Editor editor = CURRENT_LEVEL.edit();
				editor.putInt("level", currentLevel + 1);
				editor.commit();
				Intent showContent = new Intent(getApplicationContext(), GameActivity.class);
				startActivity(showContent);
			}
		});
	}
}
