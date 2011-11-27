package com.graviballs.menus;

import com.graviballs.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class LevelAdapter extends BaseAdapter {
	private Context mContext;
	final SharedPreferences SCORE_CARD;

    public LevelAdapter(Context c, SharedPreferences SCORE_CARD) {
        mContext = c;
        this.SCORE_CARD = SCORE_CARD;
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

    public View getView(final int position, final View convertView, final ViewGroup parent) {    	
    	final TextView textView = new TextView(mContext) {
        	protected void onDraw(Canvas canvas) {
        		final int bestTime = SCORE_CARD.getInt(Integer.toString(position + 1), -1);
        		
        		if (bestTime != -1) {
                	setBackgroundResource(R.drawable.toast_frame_green);
                } else{
                	setBackgroundResource(R.drawable.toast_frame_red);
                }
            		
        		super.onDraw(canvas);
        	}
        };
        textView.setLayoutParams(new GridView.LayoutParams(85, 85));
        textView.setTextSize(25);
        textView.setPadding(4, 4, 4, 4);
        textView.setGravity(Gravity.CENTER);
        textView.setText(levels[position]);
        return textView;
    }

    private String[] levels = {
            "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "10"
    };

}
