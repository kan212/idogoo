package com.idogoo.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.idogoo.R;
import com.idogoo.utils.Constant;

public class SubActivity extends FragmentActivity {

	private TextView mTitleView;
	protected ImageView mLeftView, mRightView;
	private Fragment mFragment;
	private FragmentTransaction mTransaction;
	private static float SWIPE_MIN_DISTANCE_X;
	protected OnFinishListener mOnFinishListener;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_sub);
		mTitleView = (TextView) findViewById(R.id.tv_title);
		mLeftView = (ImageView) findViewById(R.id.iv_title_left);
		mRightView = (ImageView) findViewById(R.id.iv_title_right);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		String className = getIntent().getStringExtra(
				Constant.EXTRA_FRAGMENT_TYPE);
		String title = getIntent().getStringExtra(Constant.EXTRA_TITLE);
		mTitleView.setText(title);
		SWIPE_MIN_DISTANCE_X = getResources().getDisplayMetrics().density * 100;
		mFragment = getFragment(className);
		mTransaction = this.getSupportFragmentManager().beginTransaction();
		mTransaction.replace(R.id.content_frame, mFragment);
		mTransaction.commit();

		mFinishDetector = new GestureDetector(this, mFinishListener);
	}

	protected Fragment getFragment(String className) {
		Bundle args = getIntent().getExtras();
		Fragment fragment = Fragment.instantiate(this, className, args);
		return fragment;
	}

	private GestureDetector mFinishDetector;
	private SimpleOnGestureListener mFinishListener = new SimpleOnGestureListener() {
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (null != e1 && null != e2) {
				float distX = e2.getX() - e1.getX();
				float distY = e2.getY() - e1.getY();

				if (Math.abs(distX) > Math.abs(distY) * 1.732f) {
					if (distX > SWIPE_MIN_DISTANCE_X) {
						onExitBySlide();
					}
				}
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		};
	};

	protected void onExitBySlide() {
		if (mOnFinishListener != null && !mOnFinishListener.canFinish()) {
			return;
		}
		finish();
	}

	public void setOnFinishListener(OnFinishListener l) {
		this.mOnFinishListener = l;
	}

	public interface OnFinishListener {
		public boolean canFinish();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public boolean dispatchTouchEvent(MotionEvent ev) {
		mFinishDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

}
