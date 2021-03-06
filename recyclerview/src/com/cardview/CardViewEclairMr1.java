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

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

class CardViewEclairMr1 implements CardViewImpl {

    @Override
    public void initStatic() {
        // Draws a round rect using 7 draw operations. This is faster than using
        // canvas.drawRoundRect before JBMR1 because API 11-16 used alpha mask textures to draw
        // shapes.
        RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectHelperEclairMr1();
    }

    @Override
    public void initialize(CardViewDelegate cardView, Context context, int backgroundColor,
            float radius, float elevation, float maxElevation, boolean isTopSideEnabled, boolean isBottomSideEnabled) {
        RoundRectDrawableWithShadow background = createBackground(context, backgroundColor, radius,
                elevation, maxElevation, isTopSideEnabled, isBottomSideEnabled);
        background.setAddPaddingForCorners(cardView.getPreventCornerOverlap());
	    background.setEnabledSides(isTopSideEnabled, isBottomSideEnabled);
        cardView.setBackgroundDrawable(background);
        updatePadding(cardView);
    }

    RoundRectDrawableWithShadow createBackground(Context context, int backgroundColor,
            float radius, float elevation, float maxElevation, boolean isTopSideEnabled, boolean isBottomSideEnabled) {
        return new RoundRectDrawableWithShadow(context.getResources(), backgroundColor, radius,
                elevation, maxElevation, isTopSideEnabled, isBottomSideEnabled);
    }

    @Override
    public void updatePadding(CardViewDelegate cardView) {
        Rect shadowPadding = new Rect();
        getShadowBackground(cardView).getMaxShadowAndCornerPadding(shadowPadding);
        //((View)cardView).setMinimumHeight((int) Math.ceil(getMinHeight(cardView)));
        ((View)cardView).setMinimumWidth((int) Math.ceil(getMinWidth(cardView)));
	    cardView.setShadowPadding(shadowPadding.left, shadowPadding.top,
                shadowPadding.right, shadowPadding.bottom);
    }

    @Override
    public void onCompatPaddingChanged(CardViewDelegate cardView) {
        // NO OP
    }

    @Override
    public void onPreventCornerOverlapChanged(CardViewDelegate cardView) {
        getShadowBackground(cardView).setAddPaddingForCorners(cardView.getPreventCornerOverlap());
        updatePadding(cardView);
    }

	@Override
	public void setEnabledSides(CardViewDelegate cardView, boolean isTopEnabled, boolean isBottomEnabled) {
		getShadowBackground(cardView).setEnabledSides(isTopEnabled, isBottomEnabled);
		updatePadding(cardView);
	}

	@Override
	public boolean isTopSideEnabled(CardViewDelegate cardView) {
		return getShadowBackground(cardView).isTopSideEnabled();
	}

	@Override
	public boolean isBottomSideEnabled(CardViewDelegate cardView) {
		return getShadowBackground(cardView).isBottomSideEnabled();
	}

	@Override
    public void setRadius(CardViewDelegate cardView, float radius) {
        getShadowBackground(cardView).setCornerRadius(radius);
        updatePadding(cardView);
    }

    @Override
    public float getRadius(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getCornerRadius();
    }

    @Override
    public void setElevation(CardViewDelegate cardView, float elevation) {
        getShadowBackground(cardView).setShadowSize(elevation);
    }

    @Override
    public float getElevation(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getShadowSize();
    }

    @Override
    public void setMaxElevation(CardViewDelegate cardView, float maxElevation) {
        getShadowBackground(cardView).setMaxShadowSize(maxElevation);
        updatePadding(cardView);
    }

    @Override
    public float getMaxElevation(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getMaxShadowSize();
    }

    @Override
    public float getMinWidth(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getMinWidth();
    }

    @Override
    public float getMinHeight(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getMinHeight();
    }

    private RoundRectDrawableWithShadow getShadowBackground(CardViewDelegate cardView) {
        return ((RoundRectDrawableWithShadow) cardView.getBackground());
    }
}