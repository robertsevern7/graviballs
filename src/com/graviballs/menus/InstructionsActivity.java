package com.graviballs.menus;

import com.graviballs.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InstructionsActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructions);
		
		TextView tv1 = (TextView)findViewById(R.id.instructions_text);
		
		tv1.setMovementMethod(new ScrollingMovementMethod());
		
		Button play = (Button)findViewById(R.id.main_menu_button);
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
