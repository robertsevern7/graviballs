package com.graviballs.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class CircularScreenItem extends ScreenItem {
	protected final float radius;
	private Bitmap bitmap;

	public CircularScreenItem(final float mPosXProp, final float mPosYProp, final float radius) {
		super(mPosXProp, mPosYProp);
		this.radius = radius;
	}

	public float getRadius(final float scaling) {
		return radius * scaling;
	}

	public abstract int getDrawable();

	// TODO WTF is the .5f?
	public final Bitmap getBitmap(final Resources resources, final float mMetersToPixelsX, final float mMetersToPixelsY, final float scaling) {
		if (bitmap == null) {
			final Bitmap ball = BitmapFactory.decodeResource(resources, getDrawable());
			final int dstWidth = (int) (getRadius(scaling) * 2 * mMetersToPixelsX + 0.5f);
			final int dstHeight = (int) (getRadius(scaling) * 2 * mMetersToPixelsY + 0.5f);
			bitmap = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
		}
		return bitmap;
	}


}
