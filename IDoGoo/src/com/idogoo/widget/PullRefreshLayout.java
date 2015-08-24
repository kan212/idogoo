/**
 * Project Name:custom_android
 * File Name:PullRefreshLayout.java
 * Package Name:custom.android.widget
 *
 */

package com.idogoo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * ClassName:PullRefreshLayout <br/>
 * Date: 2014-3-7 下午12:25:46 <br/>
 * 
 * @author hushuan
 * @version
 * @since JDK 1.6
 * @see
 */
public class PullRefreshLayout extends FrameLayout {
	// 三种刷新方式
	public static final int MODE_PULL_DOWN = 0x1;
	public static final int MODE_PULL_UP = 0x2;
	private static final int MODE_PULL_BOTH = 0x3;
	private static final int MODE_NONE = 0x0;
	private int mMode = MODE_NONE;
	private int mCurrentMode = MODE_PULL_DOWN;

	private static final int STATE_NONE = 0x0;
	private static final int STATE_PULL_TO_REFRESH = STATE_NONE + 1;
	private static final int STATE_REFRESHING = STATE_NONE + 2;
	private static final int STATE_RELEASE_TO_REFRESH = STATE_NONE + 3;
	private static final int STATE_MANUAL_REFRESHING = STATE_NONE + 4;
	private int mState = STATE_NONE;

	// 滑动状�?

	private final Scroller mScroller;
	private final int mTouchSlop;
	private static float FRICTION = 2.0f;

	private PullToRefreshLoadingView mHeadView;
	private PullToRefreshLoadingView mFootView;
	private View mContentView;

	private int mHeadViewHeight;
	private int mFootViewHeight;

	private OnRefreshListener onRefreshListener;
	private boolean isPullToRefreshEnabled = true;

	public static interface OnRefreshListener {
		public void onRefresh(int mode);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * A mutator to enable/disable Pull-to-Refresh for the current View
	 * 
	 * @param enable
	 *            Whether Pull-To-Refresh should be used
	 */
	public final void setPullToRefreshEnabled(boolean enable) {
		this.isPullToRefreshEnabled = enable;
	}

	/**
	 * Whether Pull-to-Refresh is enabled
	 * 
	 * @return enabled
	 */
	public final boolean isPullToRefreshEnabled() {
		return isPullToRefreshEnabled;
	}

	public final boolean isRefreshing() {
		return mState == STATE_REFRESHING || mState == STATE_MANUAL_REFRESHING;
	}

	public final void setRefreshing() {
		this.setRefreshing(true);
	}

	/**
	 * Sets the Widget to be in the refresh state. The UI will be updated to
	 * show the 'Refreshing' view.
	 * 
	 * @param doScroll
	 *            - true if you want to force a scroll to the Refreshing view.
	 */
	public final void setRefreshing(boolean doScroll) {
		if (!isRefreshing()) {
			smoothScrollTo(mCurrentMode == MODE_PULL_DOWN ? -mHeadViewHeight
					: mFootViewHeight);
			mState = STATE_MANUAL_REFRESHING;
			scrollStateChanged();
		}
	}

	public void onRefreshComplete() {
		if (mState == STATE_NONE) {
			return;
		}
		smoothScrollTo(0);
		mState = STATE_NONE;
		scrollStateChanged();
	}

	/**
	 * Set OnRefreshListener for the Widget
	 * 
	 * @param listener
	 *            - Listener to be used when the Widget is set to Refresh
	 */
	public final void setOnRefreshListener(OnRefreshListener listener) {
		onRefreshListener = listener;
	}

	public PullRefreshLayout(Context context) {
		super(context);
		mScroller = new Scroller(context);
		ViewConfiguration cfg = ViewConfiguration.get(context);
		mTouchSlop = cfg.getScaledTouchSlop();
	}

	public PullRefreshLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PullRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScroller = new Scroller(context);
		ViewConfiguration cfg = ViewConfiguration.get(context);
		mTouchSlop = cfg.getScaledTouchSlop();
	}

	private void checkView(View view) {
		if (null == view) {
			return;
		}
		if (view instanceof PullToRefreshLoadingView) {
			if (null == mHeadView) {
				mHeadView = (PullToRefreshLoadingView) view;
				mMode = MODE_PULL_DOWN;
			} else {
				mFootView = (PullToRefreshLoadingView) view;
				mMode = MODE_PULL_BOTH;
			}
		} else {
			mContentView = view;
		}
	}

	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		checkView(child);
		super.addView(child, index, params);
	}

	@Override
	protected boolean addViewInLayout(View child, int index,
			android.view.ViewGroup.LayoutParams params,
			boolean preventRequestLayout) {
		checkView(child);
		return super
				.addViewInLayout(child, index, params, preventRequestLayout);
	}

	@Override
	protected void attachViewToParent(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		checkView(child);
		super.attachViewToParent(child, index, params);
	}

	@Override
	protected void detachViewFromParent(View child) {
		destroyChild(child);
		super.detachViewFromParent(child);
	}

	@Override
	protected void detachViewFromParent(int index) {
		destroyChild(getChildAt(index));
		super.detachViewFromParent(index);
	}

	@Override
	protected void detachViewsFromParent(int start, int count) {
		for (int i = start; i < start + count; i++) {
			destroyChild(getChildAt(i));
		}
		super.detachViewsFromParent(start, count);
	}

	@Override
	protected void detachAllViewsFromParent() {
		destroyChild(mHeadView);
		destroyChild(mFootView);
		super.detachAllViewsFromParent();
	}

	private void destroyChild(View view) {
		if (mHeadView == view) {
			mHeadView = null;
		}
		if (mFootView == view) {
			mFootView = null;
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (mHeadView != null) {
			View v = mHeadView;
			int b = 0;
			int t = b - (v.getBottom() - v.getTop());
			v.layout(v.getLeft(), t, v.getRight(), b);
			mHeadViewHeight = ((v.getVisibility() == View.VISIBLE) ? b - t : 0);
		}
		if (mFootView != null) {
			View v = mFootView;
			int t = bottom;
			int b = t + (v.getBottom() - v.getTop());
			v.layout(v.getLeft(), t, v.getRight(), b);
			mFootViewHeight = ((v.getVisibility() == View.VISIBLE) ? b - t : 0);
		}
	}

	private float mLastMotionX, mLastMotionY;
	private float mInitialMotionY;
	private boolean mIsDraged = false;
	private boolean mCancelSended = false;

	private boolean dispatchTouchEventOfRefreshing(MotionEvent ev) {
		float y = ev.getY();
		int action = ev.getAction();
		final int scrollY = getScrollY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mIsDraged = scrollY != 0;
			mCancelSended = scrollY != 0;
			mLastMotionY = mInitialMotionY = y;
			mLastMotionX = ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int dy = (int) (mLastMotionY - y);
			int newscrollY = scrollY + dy;
			if (mCurrentMode == MODE_PULL_DOWN) {
				// 刚开始Drag
				if (scrollY == 0 && isReadyForPullDown() && newscrollY < 0) {
					newscrollY = -1;
					scrollTo(0, newscrollY);
					mLastMotionY = y;
				}
				if (scrollY < 0) {
					mIsDraged = true;
					mCancelSended = true;
					if (newscrollY > 0) {// 刚结�?
						newscrollY = 0;
						mIsDraged = false;
					}
					if (newscrollY < -mHeadViewHeight) {
						newscrollY = -mHeadViewHeight;
						mCancelSended = false;
					}
					scrollTo(0, newscrollY);
					mLastMotionY = y;
				} else {
					mIsDraged = false;
				}
			}
			if (mCurrentMode == MODE_PULL_UP) {
				if (scrollY == 0 && isReadyForPullUp() && newscrollY > 0) {
					newscrollY = 1;
					scrollTo(0, newscrollY);
					mLastMotionY = y;
				}
				if (scrollY > 0) {
					mIsDraged = true;
					mCancelSended = true;
					if (newscrollY < 0) {
						newscrollY = 0;
						mIsDraged = false;
					}
					if (newscrollY > mFootViewHeight) {
						newscrollY = mFootViewHeight;
						mCancelSended = false;
					}
					scrollTo(0, newscrollY);
					mLastMotionY = y;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mIsDraged = false;
			mCancelSended = false;
			break;
		}
		if (mCancelSended && !mIsDraged) {
			mCancelSended = false;
			float xf = ev.getX(), yf = ev.getY();
			ev.setLocation(xf, yf - scrollY);
			ev.setAction(MotionEvent.ACTION_DOWN);
			super.dispatchTouchEvent(ev);
			ev.setLocation(xf, yf);
			ev.setAction(action);
		}
		if (!mCancelSended)
			return super.dispatchTouchEvent(ev);
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (!isPullToRefreshEnabled) {
			return super.dispatchTouchEvent(ev);
		}
		if (STATE_REFRESHING == mState) {
			return dispatchTouchEventOfRefreshing(ev);
		}
		float y = ev.getY();
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mIsDraged = false;
			mCancelSended = false;
			mLastMotionY = mInitialMotionY = ev.getY();
			mLastMotionX = ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			if (!mIsDraged) {
				final float dy = y - mLastMotionY;
				final float yDiff = Math.abs(dy);
				final float xDiff = Math.abs(ev.getX() - mLastMotionX);
				if (yDiff > mTouchSlop && yDiff > xDiff) {
					if ((mMode == MODE_PULL_BOTH || mMode == MODE_PULL_DOWN)
							&& dy >= 0.0001f && isReadyForPullDown()) {
						mLastMotionY = y;
						mIsDraged = true;
						mCurrentMode = MODE_PULL_DOWN;
					} else if ((mMode == MODE_PULL_BOTH || mMode == MODE_PULL_UP)
							&& dy <= -0.0001f && isReadyForPullUp()) {
						mLastMotionY = y;
						mIsDraged = true;
						mCurrentMode = MODE_PULL_UP;
					}
				}
			} else {
				mLastMotionY = y;
				pullEvent();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mIsDraged = false;
			completeTouch();
			break;
		}
		if (mIsDraged && !mCancelSended) {
			mCancelSended = true;
			ev.setAction(MotionEvent.ACTION_CANCEL);
			super.dispatchTouchEvent(ev);
		}
		if (!mCancelSended)
			return super.dispatchTouchEvent(ev);
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	public void setPullDownMaxHeight(int height) {
		iPullDownMaxHeight = height;
	}

	private int iPullDownMaxHeight;

	private boolean pullEvent() {
		final int oldHeight = this.getScrollY();
		int newHeight;
		switch (mCurrentMode) {
		case MODE_PULL_UP:
			newHeight = Math.round(Math.max(mInitialMotionY - mLastMotionY, 0)
					/ FRICTION);
			if (null != mFootView) {
				mFootView.pullDistanceHeight(newHeight);
			}
			break;
		case MODE_PULL_DOWN:
		default:
			newHeight = Math.round(Math.min(mInitialMotionY - mLastMotionY, 0)
					/ FRICTION);
			if (iPullDownMaxHeight > 0 && newHeight < -iPullDownMaxHeight) {
				newHeight = -iPullDownMaxHeight;
			}
			if (null != mHeadView) {
				mHeadView.pullDistanceHeight(newHeight);
			}
			break;
		}
		scrollTo(0, newHeight);
		checkScrollState(newHeight);
		return oldHeight != newHeight;
	}

	private void checkScrollState(int srollY) {
		int distance = mCurrentMode == MODE_PULL_DOWN ? mHeadViewHeight
				: mFootViewHeight;
		switch (mState) {
		case STATE_NONE:
			scrollStateChanged();
			mState = STATE_PULL_TO_REFRESH;
			break;
		case STATE_PULL_TO_REFRESH:
			if (Math.abs(srollY) > distance) {
				mState = STATE_RELEASE_TO_REFRESH;
				scrollStateChanged();
			}
			break;
		case STATE_RELEASE_TO_REFRESH:
			if (Math.abs(srollY) <= distance) {
				mState = STATE_PULL_TO_REFRESH;
				scrollStateChanged();
			}
			break;
		}
	}

	private void scrollStateChanged() {
		PullToRefreshLoadingView actionView = mCurrentMode == MODE_PULL_DOWN ? mHeadView
				: mFootView;
		if (null == actionView) {
			return;
		}
		switch (mState) {
		case STATE_NONE:
			actionView.reset();
			break;
		case STATE_PULL_TO_REFRESH:
			actionView.pullToRefresh();
			break;
		case STATE_RELEASE_TO_REFRESH:
			actionView.releaseToRefresh();
			break;
		case STATE_REFRESHING:
		case STATE_MANUAL_REFRESHING:
			actionView.refresh();
			if (null != onRefreshListener) {
				onRefreshListener.onRefresh(mCurrentMode);
			}
			break;
		}
	}

	private void completeTouch() {
		if (mState == STATE_NONE) {
			return;
		}
		if (STATE_RELEASE_TO_REFRESH == mState && onRefreshListener != null) {
			smoothScrollTo(mCurrentMode == MODE_PULL_DOWN ? -mHeadViewHeight
					: mFootViewHeight);
			mState = STATE_REFRESHING;
			scrollStateChanged();
		} else {
			mState = STATE_NONE;
			smoothScrollTo(0);
			scrollStateChanged();
		}
	}

	public final void smoothScrollTo(int y) {
		smoothScrollBy(y - getScrollY());
	}

	private long mLastScroll;
	private static final int ANIMATED_SCROLL_GAP = 250;

	public final void smoothScrollBy(int dy) {
		long duration = AnimationUtils.currentAnimationTimeMillis()
				- mLastScroll;
		if (duration > ANIMATED_SCROLL_GAP) {
			mScroller.startScroll(0, getScrollY(), 0, dy);
			invalidate();
		} else {
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			scrollBy(0, dy);
		}
		mLastScroll = AnimationUtils.currentAnimationTimeMillis();
	}

	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int oldY = getScrollY();
			int scrollY = mScroller.getCurrY();

			scrollTo(0, scrollY);

			if (oldY != scrollY) {
				onScrollChanged(0, scrollY, 0, oldY);
			}
			if (scrollY == 0) {
				mScroller.abortAnimation();
			}
			postInvalidate();
		}
	}

	/**
	 * isReadyForPullDown:是否可以下拉. <br/>
	 * 
	 * @author hushuan
	 * @return
	 */
	private boolean isReadyForPullDown() {
		if (mContentView == null)
			return false;
		if (mContentView instanceof AbsListView) {
			return isFirstItemVisible((AbsListView) mContentView);
		} else {
			return mContentView.getScrollY() == 0;
		}
	}

	/**
	 * isReadyForPullUp:是否可以上拉. <br/>
	 * 
	 * @author hushuan
	 * @return
	 */
	private boolean isReadyForPullUp() {
		if (mContentView == null)
			return false;
		if (mContentView instanceof AbsListView) {
			return isLastItemVisible((AbsListView) mContentView);
		} else if (mContentView instanceof ScrollView) {
			return mContentView.getScrollY() + mContentView.getHeight() == ((ScrollView) mContentView)
					.getChildAt(0).getHeight();
		} else if (mContentView instanceof WebView) {
			return mContentView.getScrollY() >= (((WebView) mContentView)
					.getContentHeight() - mContentView.getHeight());
		} else {
			return false;
		}
	}

	private boolean isFirstItemVisible(AbsListView listview) {
		if (listview.getCount() == 0) {
			return true;
		} else if (listview.getFirstVisiblePosition() == 0) {
			final View firstVisibleChild = listview.getChildAt(0);
			if (firstVisibleChild != null) {
				return firstVisibleChild.getTop() >= listview.getTop();
			}
		}
		return false;
	}

	private boolean isLastItemVisible(AbsListView listview) {
		final int count = listview.getCount();
		final int lastVisiblePosition = listview.getLastVisiblePosition();

		if (count == 0) {
			return true;
		} else if (lastVisiblePosition == count - 1) {

			final int childIndex = lastVisiblePosition
					- listview.getFirstVisiblePosition();
			final View lastVisibleChild = listview.getChildAt(childIndex);

			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() <= listview.getBottom();
			}
		}
		return false;
	}

}
