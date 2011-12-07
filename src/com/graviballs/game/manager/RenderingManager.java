package com.graviballs.game.manager;

import android.content.res.Resources;
import android.graphics.Canvas;
import com.graviballs.game.CircularScreenItem;

public class RenderingManager {
	private final Canvas canvas;
	private final float mMetersToPixelsX;
	private final float mMetersToPixelsY;
	private final float mHorizontalBound;
	private final float mVerticalBound;
	private final float mXOrigin;
	private final float mYOrigin;
	private final Resources resources;

	public RenderingManager(Canvas canvas, float mMetersToPixelsX, float mMetersToPixelsY, float mHorizontalBound, float mVerticalBound, float mXOrigin, float mYOrigin, Resources resources) {
		this.canvas = canvas;
		this.mMetersToPixelsX = mMetersToPixelsX;
		this.mMetersToPixelsY = mMetersToPixelsY;
		this.mHorizontalBound = mHorizontalBound;
		this.mVerticalBound = mVerticalBound;
		this.mXOrigin = mXOrigin;
		this.mYOrigin = mYOrigin;
		this.resources = resources;
	}

	public final void renderScreenItem(CircularScreenItem someCircle) {
		final float x = mXOrigin + (someCircle.getXProportion() * mHorizontalBound - someCircle.getRadius(mHorizontalBound)) * mMetersToPixelsX;
		final float y = mYOrigin - (someCircle.getYProportion() * mVerticalBound + someCircle.getRadius(mHorizontalBound)) * mMetersToPixelsY;
		canvas.drawBitmap(someCircle.getBitmap(resources, mMetersToPixelsX, mMetersToPixelsY, mHorizontalBound), x, y, null);
	}

}
