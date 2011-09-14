package com.graviballs.game;

import com.graviballs.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ScreenItem {
	private float mPosXProp;
    private float mPosYProp;
    private float radius;
    private Bitmap bitmap;
    
    public ScreenItem(final float mPosXProp, final float mPosYProp, final float radius) {
		this.mPosXProp = mPosXProp;
		this.mPosYProp = mPosYProp;
		this.radius = radius;
	}
    
    public float getRadius() {
    	return radius;
    }
    
    public float getXProportion() {
    	return mPosXProp;
    }
    
    public float getYProportion() {
    	return mPosYProp;
    }
    
    protected int getDrawable() {
		return R.drawable.ball; 
	}
    
    public Bitmap getBitmap(final Resources resources, final float mMetersToPixelsX, final float mMetersToPixelsY) {
		if (bitmap == null) {
			final Bitmap ball = BitmapFactory.decodeResource(resources, getDrawable());
			final int dstWidth = (int) (radius * 2 * mMetersToPixelsX + 0.5f);
	        final int dstHeight = (int) (radius * 2 * mMetersToPixelsY + 0.5f);
	        bitmap =  Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
		}
		
		return bitmap;		
	}
}
