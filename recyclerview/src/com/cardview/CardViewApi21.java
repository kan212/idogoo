package com.cardview;

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

import android.annotation.TargetApi;
import android.content.Context;
import android.view.View;

@TargetApi(21)
class CardViewApi21 implements CardViewImpl {

	@Override
	public void initialize(CardViewDelegate cardView, Context context, int backgroundColor,
	                       float radius, float elevation, float maxElevation, boolean isTopSideEnabled, boolean isBottomSideEnabled) {
		final RoundRectDrawable backgroundDrawable = new RoundRectDrawable(backgroundColor, radius,
				isTopSideEnabled, isBottomSideEnabled);
		cardView.setBackgroundDrawable(backgroundDrawable);
		View view = (View) cardView;
		view.setClipToOutline(true);
		view.setElevation(elevation);
		setMaxElevation(cardView, maxElevation);
	}

	@Override
	public void setRadius(CardViewDelegate cardView, float radius) {
		((RoundRectDrawable) (cardView.getBackground())).setRadius(radius);
	}

	@Override
	public void initStatic() {
		RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectHelperJellybeanMr1();
	}

	@Override
	public void setMaxElevation(CardViewDelegate cardView, float maxElevation) {
		((RoundRectDrawable) (cardView.getBackground())).setPadding(maxElevation,
				cardView.getUseCompatPadding(), cardView.getPreventCornerOverlap());
		updatePadding(cardView);
	}

	@Override
	public float getMaxElevation(CardViewDelegate cardView) {
		return ((RoundRectDrawable) (cardView.getBackground())).getPadding();
	}

	@Override
	public float getMinWidth(CardViewDelegate cardView) {
		return getRadius(cardView) * 2;
	}

	@Override
	public float getMinHeight(CardViewDelegate cardView) {
		return getRadius(cardView) * 2;
	}

	@Override
	public float getRadius(CardViewDelegate cardView) {
		return ((RoundRectDrawable) (cardView.getBackground())).getRadius();
	}

	@Override
	public void setElevation(CardViewDelegate cardView, float elevation) {
		((View) cardView).setElevation(elevation);
	}

	@Override
	public float getElevation(CardViewDelegate cardView) {
		return ((View) cardView).getElevation();
	}

	@Override
	public void updatePadding(CardViewDelegate cardView) {
		if (!cardView.getUseCompatPadding()) {
			cardView.setShadowPadding(0, 0, 0, 0);
			return;
		}
		float elevation = getMaxElevation(cardView);
		final float radius = getRadius(cardView);
		int hPadding = (int) Math.ceil(RoundRectDrawableWithShadow
				.calculateHorizontalPadding(elevation, radius, cardView.getPreventCornerOverlap()));
		int vPadding = (int) Math.ceil(RoundRectDrawableWithShadow
				.calculateVerticalPadding(elevation, radius, cardView.getPreventCornerOverlap()));
		cardView.setShadowPadding(hPadding, isTopSideEnabled(cardView) ? vPadding : 0,
				hPadding, isBottomSideEnabled(cardView) ? vPadding : 0);
	}

	@Override
	public void onCompatPaddingChanged(CardViewDelegate cardView) {
		setMaxElevation(cardView, getMaxElevation(cardView));
	}

	@Override
	public void onPreventCornerOverlapChanged(CardViewDelegate cardView) {
		setMaxElevation(cardView, getMaxElevation(cardView));
	}

	@Override
	public void setEnabledSides(CardViewDelegate cardView, boolean isTopEnabled, boolean isBottomEnabled) {
		((RoundRectDrawable) (cardView.getBackground())).setEnabledSides(isTopEnabled, isBottomEnabled);
		updatePadding(cardView);
	}

	@Override
	public boolean isTopSideEnabled(CardViewDelegate cardView) {
		return ((RoundRectDrawable) (cardView.getBackground())).isTopSideEnabled();
	}

	@Override
	public boolean isBottomSideEnabled(CardViewDelegate cardView) {
		return ((RoundRectDrawable) (cardView.getBackground())).isBottomSideEnabled();
	}
}