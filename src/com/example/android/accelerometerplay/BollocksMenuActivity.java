package com.example.android.accelerometerplay;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BollocksMenuActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(),
			R.array.graviballs_menu, R.layout.list_item));
	}

	@Override protected void onListItemClick(ListView l, View v, int position, long id) {
		SharedPreferences CURRENT_LEVEL = getSharedPreferences("CurrentLevel", 0);
		SharedPreferences.Editor editor = CURRENT_LEVEL.edit();
		editor.putInt("level", position);
		editor.commit();
		
		Intent showContent = new Intent(getApplicationContext(), AccelerometerPlayActivity.class);
		startActivity(showContent);
	}
}
