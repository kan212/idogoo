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

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.v7.recyclerview.R;
import android.util.Log;

/**
 * A rounded rectangle drawable which also includes a shadow around.
 */
class RoundRectDrawableWithShadow extends Drawable {
	// used to calculate content padding
	final static double COS_45 = Math.cos(Math.toRadians(45));

	final static float SHADOW_MULTIPLIER = 1.5f;

	final float mInsetShadow; // extra shadow to avoid gaps between card and shadow

	/*
	* This helper is set by CardView implementations.
	* <p>
	* Prior to API 17, canvas.drawRoundRect is expensive; which is why we need this interface
	* to draw efficient rounded rectangles before 17.
	* */
	static RoundRectHelper sRoundRectHelper;

	Paint mPaint;

	Paint mCornerShadowPaint;

	Paint mEdgeShadowPaint;

	final RectF mCardBounds;

	float mCornerRadius;

	Path mCornerShadowPath;

	// updated value with inset
	float mMaxShadowSize;

	// actual value set by developer
	float mRawMaxShadowSize;

	// multiplied value to account for shadow offset
	float mShadowSize;

	// actual value set by developer
	float mRawShadowSize;

	private boolean mDirty = true;

	private final int mShadowStartColor;

	private final int mShadowEndColor;

	private boolean mAddPaddingForCorners = true;

	private boolean mIsTopEnabled;

	private boolean mIsBottomEnabled;

	/**
	 * If shadow size is set to a value above max shadow, we print a warning
	 */
	private boolean mPrintedShadowClipWarning = false;

	RoundRectDrawableWithShadow(Resources resources, int backgroundColor, float radius,
	                            float shadowSize, float maxShadowSize, boolean isTopEnabled, boolean isBottomEnabled) {
		mShadowStartColor = resources.getColor(R.color.cardview_shadow_start_color);
		mShadowEndColor = resources.getColor(R.color.cardview_shadow_end_color);
		mInsetShadow = resources.getDimension(R.dimen.cardview_compat_inset_shadow);
		setShadowSize(shadowSize, maxShadowSize);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mPaint.setColor(backgroundColor);
		mCornerShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mCornerShadowPaint.setStyle(Paint.Style.FILL);
		mCornerShadowPaint.setDither(true);
		mCornerRadius = radius;
		mCardBounds = new RectF();
		mEdgeShadowPaint = new Paint(mCornerShadowPaint);
		mIsTopEnabled = isTopEnabled;
		mIsBottomEnabled = isBottomEnabled;
	}

	public void setAddPaddingForCorners(boolean addPaddingForCorners) {
		mAddPaddingForCorners = addPaddingForCorners;
		invalidateSelf();
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
		mCornerShadowPaint.setAlpha(alpha);
		mEdgeShadowPaint.setAlpha(alpha);
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		mDirty = true;
	}

	public void setEnabledSides(boolean isTopEnabled, boolean isBottomEnabled) {
		if (isTopEnabled == mIsTopEnabled && isBottomEnabled == mIsBottomEnabled) {
			return;
		}
		mIsTopEnabled = isTopEnabled;
		mIsBottomEnabled = isBottomEnabled;
		mDirty = true;
		invalidateSelf();
	}

	void setShadowSize(float shadowSize, float maxShadowSize) {
		if (shadowSize < 0 || maxShadowSize < 0) {
			throw new IllegalArgumentException("invalid shadow size");
		}
		if (shadowSize > maxShadowSize) {
			shadowSize = maxShadowSize;
			if (!mPrintedShadowClipWarning) {
				Log.w("CardView", "Shadow size is being clipped by the max shadow size. See "
						+ "{CardView#setMaxCardElevation}.");
				mPrintedShadowClipWarning = true;
			}
		}
		if (mRawShadowSize == shadowSize && mRawMaxShadowSize == maxShadowSize) {
			return;
		}
		mRawShadowSize = shadowSize;
		mRawMaxShadowSize = maxShadowSize;
		mShadowSize = shadowSize * SHADOW_MULTIPLIER + mInsetShadow;
		mMaxShadowSize = maxShadowSize + mInsetShadow;
		mDirty = true;
		invalidateSelf();
	}

	@Override
	public boolean getPadding(Rect padding) {
		int vOffset = (int) Math.ceil(calculateVerticalPadding(mRawMaxShadowSize, mCornerRadius,
				mAddPaddingForCorners));
		int hOffset = (int) Math.ceil(calculateHorizontalPadding(mRawMaxShadowSize, mCornerRadius,
				mAddPaddingForCorners));
		padding.set(hOffset, mIsTopEnabled ? vOffset : 0, hOffset, mIsBottomEnabled ? vOffset : 0);
		return true;
	}

	static float calculateVerticalPadding(float maxShadowSize, float cornerRadius,
	                                      boolean addPaddingForCorners) {
		if (addPaddingForCorners) {
			return (float) (maxShadowSize * SHADOW_MULTIPLIER + (1 - COS_45) * cornerRadius);
		} else {
			return maxShadowSize * SHADOW_MULTIPLIER;
		}
	}

	static float calculateHorizontalPadding(float maxShadowSize, float cornerRadius,
	                                        boolean addPaddingForCorners) {
		if (addPaddingForCorners) {
			return (float) (maxShadowSize + (1 - COS_45) * cornerRadius);
		} else {
			return maxShadowSize;
		}
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
		mCornerShadowPaint.setColorFilter(cf);
		mEdgeShadowPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	void setCornerRadius(float radius) {
		if (mCornerRadius == radius) {
			return;
		}
		mCornerRadius = radius;
		mDirty = true;
		invalidateSelf();
	}

	@Override
	public void draw(Canvas canvas) {
		if (mDirty) {
			buildComponents(getBounds());
			mDirty = false;
		}
		if (mIsTopEnabled) {
			canvas.translate(0, mRawShadowSize / 2);
		}
		drawShadow(canvas);
		if (mIsTopEnabled) {
			canvas.translate(0, -mRawShadowSize / 2);
		}
		if (!mIsTopEnabled && !mIsBottomEnabled) {
			canvas.drawRect(mCardBounds, mPaint);
			return;
		}
		if (mIsTopEnabled && mIsBottomEnabled) {
			sRoundRectHelper.drawRoundRect(canvas, mCardBounds, mCornerRadius, mPaint);
			return;
		}
		if (!mIsTopEnabled) {
			sRoundRectHelper.drawBottomRoundRect(canvas, mCardBounds, mCornerRadius, mPaint);
		}
		if (!mIsBottomEnabled) {
			sRoundRectHelper.drawTopRoundRect(canvas, mCardBounds, mCornerRadius, mPaint);
		}
	}

	private void drawShadow(Canvas canvas) {
		final float edgeShadowTop = -mCornerRadius - mShadowSize;
		final float inset = mCornerRadius + mInsetShadow + mRawShadowSize / 2;
		final boolean drawHorizontalEdges = mCardBounds.width() - 2 * inset > 0;
		final boolean drawVerticalEdges = mCardBounds.height() - 2 * inset > 0;
		// LT
		int saved = canvas.save();
		canvas.translate(mCardBounds.left + inset, mCardBounds.top + inset);
		if (mIsTopEnabled) {
			canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
		}
		if (drawHorizontalEdges && mIsTopEnabled) {
			canvas.drawRect(0, edgeShadowTop,
					mCardBounds.width() - 2 * inset, -mCornerRadius,
					mEdgeShadowPaint);
		}
		canvas.restoreToCount(saved);
		// RB
		saved = canvas.save();
		canvas.translate(mCardBounds.right - inset, mCardBounds.bottom - (mIsBottomEnabled ? inset : 0));
		canvas.rotate(180f);
		if (mIsBottomEnabled) {
			canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
		}
		if (drawHorizontalEdges && mIsBottomEnabled) {
			canvas.drawRect(0, edgeShadowTop,
					mCardBounds.width() - 2 * inset, -mCornerRadius + mShadowSize,
					mEdgeShadowPaint);
		}
		canvas.restoreToCount(saved);
		// LB
		saved = canvas.save();
		canvas.translate(mCardBounds.left + inset, mCardBounds.bottom - (mIsBottomEnabled ? inset : 0));
		canvas.rotate(270f);
		if (mIsBottomEnabled) {
			canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
		}
		if (drawVerticalEdges) {
			canvas.drawRect(0, edgeShadowTop,
					mCardBounds.height() - (mIsBottomEnabled ? inset : 0) - (mIsTopEnabled ? inset : 0),
					-mCornerRadius, mEdgeShadowPaint);
		}
		canvas.restoreToCount(saved);
		// RT
		saved = canvas.save();
		canvas.translate(mCardBounds.right - inset, mCardBounds.top + (mIsTopEnabled ? inset : 0));
		canvas.rotate(90f);
		if (mIsTopEnabled) {
			canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
		}
		if (drawVerticalEdges) {
			canvas.drawRect(0, edgeShadowTop,
					mCardBounds.height() - (mIsBottomEnabled ? inset : 0) - (mIsTopEnabled ? inset : 0),
					-mCornerRadius, mEdgeShadowPaint);
		}
		canvas.restoreToCount(saved);
	}

	private void buildShadowCorners() {
		RectF innerBounds = new RectF(-mCornerRadius, -mCornerRadius, mCornerRadius, mCornerRadius);
		RectF outerBounds = new RectF(innerBounds);
		outerBounds.inset(-mShadowSize, -mShadowSize);

		if (mCornerShadowPath == null) {
			mCornerShadowPath = new Path();
		} else {
			mCornerShadowPath.reset();
		}
		mCornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
		mCornerShadowPath.moveTo(-mCornerRadius, 0);
		mCornerShadowPath.rLineTo(-mShadowSize, 0);
		// outer arc
		mCornerShadowPath.arcTo(outerBounds, 180f, 90f, false);
		// inner arc
		mCornerShadowPath.arcTo(innerBounds, 270f, -90f, false);
		mCornerShadowPath.close();

		float startRatio = mCornerRadius / (mCornerRadius + mShadowSize);
		mCornerShadowPaint.setShader(new RadialGradient(0, 0, mCornerRadius + mShadowSize,
				new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor},
				new float[]{0f, startRatio, 1f}
				, Shader.TileMode.CLAMP));

		// we offset the content shadowSize/2 pixels up to make it more realistic.
		// this is why edge shadow shader has some extra space
		// When drawing bottom edge shadow, we use that extra space.
		mEdgeShadowPaint.setShader(new LinearGradient(0, -mCornerRadius + mShadowSize, 0,
				-mCornerRadius - mShadowSize,
				new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor},
				new float[]{0f, .5f, 1f}, Shader.TileMode.CLAMP));
	}

	private void buildComponents(Rect bounds) {
		// Card is offset SHADOW_MULTIPLIER * maxShadowSize to account for the shadow shift.
		// We could have different top-bottom offsets to avoid extra gap above but in that case
		// center aligning Views inside the CardView would be problematic.
		final float verticalOffset = mMaxShadowSize * SHADOW_MULTIPLIER;
		mCardBounds.set(bounds.left + mMaxShadowSize, bounds.top + (mIsTopEnabled ? verticalOffset : 0),
				bounds.right - mMaxShadowSize, bounds.bottom - (mIsBottomEnabled ? verticalOffset : 0));
		buildShadowCorners();
	}

	boolean isTopSideEnabled() {
		return mIsTopEnabled;
	}

	boolean isBottomSideEnabled() {
		return mIsBottomEnabled;
	}

	float getCornerRadius() {
		return mCornerRadius;
	}

	void getMaxShadowAndCornerPadding(Rect into) {
		getPadding(into);
	}

	void setShadowSize(float size) {
		setShadowSize(size, mRawMaxShadowSize);
	}

	void setMaxShadowSize(float size) {
		setShadowSize(mRawShadowSize, size);
	}

	float getShadowSize() {
		return mRawShadowSize;
	}

	float getMaxShadowSize() {
		return mRawMaxShadowSize;
	}

	float getMinWidth() {
		final float content = 2 *
				Math.max(mRawMaxShadowSize, mCornerRadius + mInsetShadow + mRawMaxShadowSize / 2);
		return content + (mRawMaxShadowSize + mInsetShadow) * 2;
	}

	float getMinHeight() {
		final float content = 2 * Math.max(mRawMaxShadowSize, mCornerRadius + mInsetShadow
				+ mRawMaxShadowSize * SHADOW_MULTIPLIER / 2);
		return content + (mRawMaxShadowSize * SHADOW_MULTIPLIER + mInsetShadow) * 2;
	}

	static interface RoundRectHelper {
		void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint);

		void drawTopRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint);

		void drawBottomRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint);
	}
}