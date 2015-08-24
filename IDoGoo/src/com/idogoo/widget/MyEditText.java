package com.idogoo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditText extends EditText {
	
	private int type = 1;

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setType(int type) {
		this.type = type;
		invalidate();
	}

	private Paint getPaint(Paint paint) {
		switch (type) {
		case 0:
			paint.setColor(Color.parseColor("#FFFFFF"));
			break;
		case 1:
			paint.setColor(Color.parseColor("#00ffff"));
			break;
		case 2:
			paint.setColor(Color.parseColor("#122e29"));
			break;
		}
		return paint;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(6);
		getPaint(paint);
	/*	if (this.isFocused() == true)
			paint.setColor(Color.parseColor("#122e29"));
		else
			paint.setColor(Color.rgb(0, 173, 173));*/
		canvas.drawRoundRect(
				new RectF(2 + this.getScrollX(), 2 + this.getScrollY(), this
						.getWidth() - 3 + this.getScrollX(), this.getHeight()
						+ this.getScrollY() - 1), 15, 15, paint);
		super.onDraw(canvas);
	}

}
