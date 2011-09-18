package com.graviballs.menus;

import android.R;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class LevelAdapter extends BaseAdapter {
	private Context mContext;

    public LevelAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return levels.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            textView = new TextView(mContext);
            textView.setLayoutParams(new GridView.LayoutParams(85, 85));
            textView.setTextSize(25);
            textView.setPadding(4, 4, 4, 4);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.toast_frame);
            
            textView.setHapticFeedbackEnabled(true);
        } else {
        	textView = (TextView) convertView;
        }

        textView.setText(levels[position]);
        return textView;
    }

    private String[] levels = {
            "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "10", "11", "12",
            "13", "14", "15", "16", "17", "18"
    };

}
