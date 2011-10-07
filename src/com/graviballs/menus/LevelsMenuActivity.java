package com.graviballs.menus;

import com.graviballs.DataTypes;
import com.graviballs.R;
import com.graviballs.game.GameActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

public class LevelsMenuActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.levelgridview);

		GridView gridview = (GridView) findViewById(R.id.levelgridview);
		SharedPreferences SCORE_CARD = getSharedPreferences(DataTypes.SCORE_HOLDER.name(), 0);
		gridview.setAdapter(new LevelAdapter(this, SCORE_CARD));
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	SharedPreferences CURRENT_LEVEL = getSharedPreferences(DataTypes.CURRENT_LEVEL.name(), 0);
	    		SharedPreferences.Editor editor = CURRENT_LEVEL.edit();
	    		editor.putInt("level", position);
	    		editor.commit();
	    		
	    		Intent showContent = new Intent(getApplicationContext(), GameActivity.class);
	    		startActivity(showContent);
	        }
	    });
		
		Button backToMenu = (Button)findViewById(R.id.back_to_main_button);
		backToMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showContent = new Intent(getApplicationContext(), StartPageActivity.class);
				startActivity(showContent);
			}
		});
	}
}
