/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cardview;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Very simple drawable that draws a rounded rectangle background with arbitrary corners and also
 * reports proper outline for L.
 * <p/>
 * Simpler and uses less resources compared to GradientDrawable or ShapeDrawable.
 */
@TargetApi(21)
class RoundRectDrawable extends Drawable {
	private float mRadius;
	private final Paint mPaint;
	private final RectF mBoundsF;
	private final Rect mBoundsI;
	private float mPadding;
	private boolean mInsetForPadding = false;
	private boolean mInsetForRadius = true;

	private boolean mIsTopEnabled;
	private boolean mIsBottomEnabled;

	public RoundRectDrawable(int backgroundColor, float radius, boolean isTopEnabled, boolean isBottomEnabled) {
		mRadius = radius;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mPaint.setColor(backgroundColor);
		mBoundsF = new RectF();
		mBoundsI = new Rect();

		mIsBottomEnabled = isBottomEnabled;
		mIsTopEnabled = isTopEnabled;
	}

	public void setEnabledSides(boolean isTopEnabled, boolean isBottomEnabled) {
		mIsTopEnabled = isTopEnabled;
		mIsBottomEnabled = isBottomEnabled;
		updateBounds(null);
		invalidateSelf();
	}

	void setPadding(float padding, boolean insetForPadding, boolean insetForRadius) {
		if (padding == mPadding && mInsetForPadding == insetForPadding &&
				mInsetForRadius == insetForRadius) {
			return;
		}
		mPadding = padding;
		mInsetForPadding = insetForPadding;
		mInsetForRadius = insetForRadius;
		updateBounds(null);
		invalidateSelf();
	}

	float getPadding() {
		return mPadding;
	}

	@Override
	public void draw(Canvas canvas) {
		if (!mIsTopEnabled && !mIsBottomEnabled) {
			canvas.drawRect(mBoundsF, mPaint);
			return;
		}
		if (mIsTopEnabled && mIsBottomEnabled) {
			RoundRectDrawableWithShadow.sRoundRectHelper.drawRoundRect(canvas, mBoundsF, mRadius, mPaint);
			return;
		}
		if (!mIsTopEnabled) {
			RoundRectDrawableWithShadow.sRoundRectHelper.drawBottomRoundRect(canvas, mBoundsF, mRadius, mPaint);
		}
		if (!mIsBottomEnabled) {
			RoundRectDrawableWithShadow.sRoundRectHelper.drawTopRoundRect(canvas, mBoundsF, mRadius, mPaint);
		}
	}

	private void updateBounds(Rect bounds) {
		if (bounds == null) {
			bounds = getBounds();
		}
		mBoundsF.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
		mBoundsI.set(bounds);
		if (mInsetForPadding) {
			int vInset = (int) Math.ceil(RoundRectDrawableWithShadow.calculateVerticalPadding(mPadding, mRadius, mInsetForRadius));
			int hInset = (int) Math.ceil(RoundRectDrawableWithShadow.calculateHorizontalPadding(mPadding, mRadius, mInsetForRadius));
			mBoundsI.left += hInset;
			mBoundsI.top += mIsTopEnabled ? vInset : -mRadius;
			mBoundsI.right -= hInset;
			mBoundsI.bottom -= mIsBottomEnabled ? vInset : -mRadius;
			// to make sure they have same bounds.
			mBoundsF.set(mBoundsI);
		}
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		updateBounds(bounds);
	}

	@Override
	public void getOutline(Outline outline) {
		outline.setRoundRect(mBoundsI, mRadius);
	}

	void setRadius(float radius) {
		if (radius == mRadius) {
			return;
		}
		mRadius = radius;
		updateBounds(null);
		invalidateSelf();
	}

	@Override
	public void setAlpha(int alpha) {
		// not supported because older versions do not support
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// not supported because older versions do not support
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	public float getRadius() {
		return mRadius;
	}

	public boolean isTopSideEnabled() {
		return mIsTopEnabled;
	}

	public boolean isBottomSideEnabled() {
		return mIsBottomEnabled;
	}
}
