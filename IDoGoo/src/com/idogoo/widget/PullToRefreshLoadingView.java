package com.idogoo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class PullToRefreshLoadingView extends FrameLayout {

	public PullToRefreshLoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected abstract void reset();

	protected abstract void refresh();

	protected abstract void pullToRefresh();

	protected abstract void releaseToRefresh();

	protected void pullDistanceHeight(int dy) {
	}

}
