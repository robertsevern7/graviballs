package com.example.android.accelerometerplay;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Author: Rahul Gidwani
 * Copyright 2011 - Viglink Inc.
 */
public class BollocksMenuActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(),
			R.array.graviballs_menu, R.layout.list_item));
	}

	@Override protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent showContent = new Intent(getApplicationContext(), AccelerometerPlayActivity.class);
		startActivity(showContent);
	}
}
