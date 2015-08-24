package com.idogoo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.idogoo.R;

public class CircleImageView extends ImageView {

	private boolean isDrawBorder;
	private int colorValue;
	private float value;
	private Bitmap roundBitmap;
	private Paint paint = new Paint();
	int w,h;
	public CircleImageView(Context context) {
		super(context);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//TypedArray是一个用来存放由context.obtainStyledAttributes获得的属性的数组   
        //在使用完成后，一定要调用recycle方法   
        //属性的名称是styleable中的名称+“_”+属性名称 
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);   
		isDrawBorder = array.getBoolean(R.styleable.CircleImageView_drawBorder, true);
		colorValue = array.getColor(R.styleable.CircleImageView_borderColor, R.color.circle_image_border_color);
		value = array.getFloat(R.styleable.CircleImageView_circleRadius, 6f);
		array.recycle(); //一定要调用，否则这次的设定会对下次的使用造成影响   
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		w = getWidth();
		h = getHeight();
		initBitmap();
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		initBitmap();
	}
	private void initBitmap(){
		if (getDrawable() == null) {
			return;
		}
		if (getWidth() == 0 || getHeight() == 0) {
			return; 
		}
		Bitmap b =  ((BitmapDrawable)getDrawable()).getBitmap();
		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
		roundBitmap =  getCroppedBitmap(bitmap, w);
		bitmap.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		custom.android.util.Config.e("=========ondraw");
		if(getDrawable()!=null){
			paint.setAntiAlias(true);
			if(isDrawBorder){
				//			paint.setColor(Color.parseColor(colorValue));
				//			canvas.drawCircle(w / 2+value, h / 2+value, w / 2f-8, paint);
				paint.setColor(colorValue);
				canvas.drawCircle(w / 2+0.7f, h / 2+0.7f, w / 2f-value, paint);
			}
			canvas.drawBitmap(roundBitmap, 0,0, null);
		}
	}

	public Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		Bitmap sbmp;
		if(bmp.getWidth() != radius || bmp.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
				sbmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 255, 255, 255);
		paint.setColor(Color.parseColor("#7f97d2"));
		canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
				sbmp.getWidth() / 2-9f, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}

	public void setDrawBorder(boolean isDrawBorder) {
		this.isDrawBorder = isDrawBorder;
		invalidate();
	}
	public void setBorderColor(int colorValue){
		this.colorValue = colorValue;
		invalidate();
	}
	public void setCircleRadius(float value){
		this.value = value;
	}






}