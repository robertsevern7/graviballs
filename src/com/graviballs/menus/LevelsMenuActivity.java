package com.graviballs.menus;

import com.graviballs.R;
import com.graviballs.game.GameActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

public class LevelsMenuActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.levelgridview);

		GridView gridview = (GridView) findViewById(R.id.levelgridview);
		SharedPreferences SCORE_CARD = getSharedPreferences("ScoreHolder", 0);
		gridview.setAdapter(new LevelAdapter(this, SCORE_CARD));
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	SharedPreferences CURRENT_LEVEL = getSharedPreferences("CurrentLevel", 0);
	    		SharedPreferences.Editor editor = CURRENT_LEVEL.edit();
	    		editor.putInt("level", position);
	    		editor.commit();
	    		
	    		Intent showContent = new Intent(getApplicationContext(), GameActivity.class);
	    		startActivity(showContent);
	        }
	    });
	}
}
