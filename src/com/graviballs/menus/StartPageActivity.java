package com.graviballs.menus;

import com.graviballs.R;
import com.graviballs.game.GameActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartPageActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_page);
		
		Button play = (Button)findViewById(R.id.play_button);
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showContent = new Intent(getApplicationContext(), LevelsMenuActivity.class);
				startActivity(showContent);
			}
		});
		
		Button continuePlay = (Button)findViewById(R.id.quick_play_button);
		continuePlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showContent = new Intent(getApplicationContext(), GameActivity.class);
				startActivity(showContent);
			}
		});
		
		Button instructions = (Button)findViewById(R.id.instructions_button);
		instructions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showContent = new Intent(getApplicationContext(), InstructionsActivity.class);
				startActivity(showContent);
			}
		});
		
		Button exit = (Button)findViewById(R.id.exit_button);
		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
