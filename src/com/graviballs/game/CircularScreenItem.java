package com.graviballs.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class CircularScreenItem implements ScreenItem {
	private float mPosXProp;
	private float mPosYProp;
	protected final float radius;
	private Bitmap bitmap;

	public CircularScreenItem(final float mPosXProp, final float mPosYProp, final float radius) {
		this.mPosXProp = mPosXProp;
		this.mPosYProp = mPosYProp;
		this.radius = radius;
	}

	@Override
	public float getXProportion() {
		return mPosXProp;
	}

	@Override
	public float getYProportion() {
		return mPosYProp;
	}
	
	public float setXProportion(float mPosXProp) {
		return this.mPosXProp = mPosXProp;
	}

	public float setYProportion(float mPosYProp) {
		return this.mPosYProp = mPosYProp;
	}

	public void setmPosXProp(float mPosXProp) {
		this.mPosXProp = mPosXProp;
	}

	public void setmPosYProp(float mPosYProp) {
		this.mPosYProp = mPosYProp;
	}

	// TODO NORMALIZE
	public float getRadius(final float scaling) {
		return radius * scaling;
	}

	public abstract int getDrawable();

	public Bitmap getBitmap(final Resources resources, final float mMetersToPixelsX, final float mMetersToPixelsY, final float scaling) {
		if (bitmap == null) {
			final Bitmap ball = BitmapFactory.decodeResource(resources, getDrawable());
			final int dstWidth = (int) (getRadius(scaling) * 2 * mMetersToPixelsX + 0.5f);
			final int dstHeight = (int) (getRadius(scaling) * 2 * mMetersToPixelsY + 0.5f);
			bitmap = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
		}
		return bitmap;
	}


}
