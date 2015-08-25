package com.cardview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * @author andrey.pogrebnoy
 */
public class RoundRectHelperJellybeanMr1 extends RoundRectHelperEclairMr1 {
	@Override
	public void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius,
	                          Paint paint) {
		canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, paint);
	}
}
