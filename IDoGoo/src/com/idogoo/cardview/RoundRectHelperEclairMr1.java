package com.idogoo.cardview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class RoundRectHelperEclairMr1 implements RoundRectDrawableWithShadow.RoundRectHelper {

	final RectF sCornerRect = new RectF();

	@Override
	public void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius,
	                          Paint paint) {
		final float twoRadius = cornerRadius * 2;
		final float innerWidth = bounds.width() - twoRadius;
		final float innerHeight = bounds.height() - twoRadius;
		sCornerRect.set(bounds.left, bounds.top,
				bounds.left + twoRadius, bounds.top + twoRadius);

		canvas.drawArc(sCornerRect, 180, 90, true, paint);
		sCornerRect.offset(innerWidth, 0);
		canvas.drawArc(sCornerRect, 270, 90, true, paint);
		sCornerRect.offset(0, innerHeight);
		canvas.drawArc(sCornerRect, 0, 90, true, paint);
		sCornerRect.offset(-innerWidth, 0);
		canvas.drawArc(sCornerRect, 90, 90, true, paint);

		//draw top and bottom pieces
		canvas.drawRect(bounds.left + cornerRadius, bounds.top,
				bounds.right - cornerRadius, bounds.top + cornerRadius,
				paint);
		canvas.drawRect(bounds.left + cornerRadius,
				bounds.bottom - cornerRadius, bounds.right - cornerRadius,
				bounds.bottom, paint);

		//center
		canvas.drawRect(bounds.left, bounds.top + cornerRadius,
				bounds.right, bounds.bottom - cornerRadius, paint);
	}

	@Override
	public void drawTopRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint) {
		final float twoRadius = cornerRadius * 2;
		final float innerWidth = bounds.width() - twoRadius;
		sCornerRect.set(bounds.left, bounds.top,
				bounds.left + twoRadius, bounds.top + twoRadius);

		canvas.drawArc(sCornerRect, 180, 90, true, paint);
		sCornerRect.offset(innerWidth, 0);
		canvas.drawArc(sCornerRect, 270, 90, true, paint);

		//draw top and bottom pieces
		canvas.drawRect(bounds.left + cornerRadius, bounds.top,
				bounds.right - cornerRadius, bounds.top + cornerRadius,
				paint);
		canvas.drawRect(bounds.left, bounds.top + cornerRadius, bounds.right, bounds.bottom, paint);
	}

	@Override
	public void drawBottomRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint) {
		final float twoRadius = cornerRadius * 2;
		final float innerWidth = bounds.width() - twoRadius;
		sCornerRect.set(bounds.left, bounds.bottom - twoRadius,
				bounds.left + twoRadius, bounds.bottom);

		canvas.drawArc(sCornerRect, 90, 90, true, paint);
		sCornerRect.offset(innerWidth, 0);
		canvas.drawArc(sCornerRect, 0, 90, true, paint);

		//draw top and bottom pieces
		canvas.drawRect(bounds.left, bounds.top, bounds.right, bounds.bottom - cornerRadius, paint);
		canvas.drawRect(bounds.left + cornerRadius,
				bounds.bottom - cornerRadius, bounds.right - cornerRadius,
				bounds.bottom, paint);
	}
}
