package com.graviballs.menus;

import com.graviballs.R;
import com.graviballs.TimeUtils;
import com.graviballs.game.GameActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LevelCompleteActivity extends Activity {
	SharedPreferences CURRENT_LEVEL;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CURRENT_LEVEL = getSharedPreferences("CurrentLevel", 0);
		setContentView(R.layout.level_complete);
		
		//TODO get best time and last attempt
		final int bestTime = CURRENT_LEVEL.getInt("bestTime", -1);
		final int previousAttempt = CURRENT_LEVEL.getInt("previousAttempt", -1);
		
		TextView completeText = (TextView)findViewById(R.id.level_complete_text_view);
		if (previousAttempt == -1) {
			completeText.append("Level Failed\n");
		} else {
			completeText.append("Level Complete\n");
			completeText.append("You did it in " + TimeUtils.justParsingTheTime(previousAttempt) + "\n");
		}
		completeText.append("Best time: " + TimeUtils.justParsingTheTime(bestTime));
	
		Button startMenu = (Button)findViewById(R.id.start_menu_button);
		startMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showContent = new Intent(getApplicationContext(), StartPageActivity.class);
				startActivity(showContent);
			}
		});
		
		Button levelsMenu = (Button)findViewById(R.id.level_menu_button);
		levelsMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showContent = new Intent(getApplicationContext(), LevelsMenuActivity.class);
				startActivity(showContent);
			}
		});
		
		Button retry = (Button)findViewById(R.id.retry_button);
		retry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showContent = new Intent(getApplicationContext(), GameActivity.class);
				startActivity(showContent);
			}
		});
		
		Button play = (Button)findViewById(R.id.next_button);
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
