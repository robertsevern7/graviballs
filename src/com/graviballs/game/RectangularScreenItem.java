package com.graviballs.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class RectangularScreenItem extends ScreenItem {

	private final float widthProportion;
	private final float heightProportion;
	private Bitmap bitmap;

	protected RectangularScreenItem(float widthProportion, float heightProportion, float xProportion, float yProportion) {
		super(xProportion, yProportion);
		this.widthProportion = widthProportion;
		this.heightProportion = heightProportion;
	}

	public abstract int getDrawable();

	public final Bitmap getBitmap(final Resources resources, final float mMetersToPixelsX, final float mMetersToPixelsY, final float scalingX, final float scalingY) {
		if (bitmap == null) {
			final Bitmap ball = BitmapFactory.decodeResource(resources, getDrawable());
			final int dstWidth = (int) (widthProportion * mMetersToPixelsX * scalingX + .5f) ;
			final int dstHeight = (int) (heightProportion * mMetersToPixelsY * scalingY + .5f);
			bitmap = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
		}
		return bitmap;
	}

	public float getWidthProportion() {
		return widthProportion;
	}
	
	public float getHeightProportion() {
		return heightProportion;
	}
}
