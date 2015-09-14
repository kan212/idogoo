package com.idogoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.idogoo.R;
import com.idogoo.paser.BaseParser;

public abstract class BaseFragmentHasFooter extends Fragment {

	protected ListView mListView;
	protected ProgressBar mFooterProgressBar;
	protected TextView mFooterMessage;
	
	private View FooterLoadView;
	
	/**
	 * LOADING:加载中.
	 */
	private final int LOADING = 1;
	/**
	 * LOADEND:加载结束，第一次加载且没有更多是使用.
	 */
	private final int LOADEND = 2;
	/**
	 * LOADFAILED:加载失败.
	 */
	private final int LOADERROR = 3;

	private int mLoadMoreState = LOADING;
	
	public View onCreateFooterView(LayoutInflater inflater) {
		FooterLoadView = inflater.inflate(R.layout.load_footer, mListView, false);
		mFooterProgressBar = (ProgressBar) FooterLoadView.findViewById(R.id.foot_progressbar);
		mFooterMessage = (TextView) FooterLoadView.findViewById(R.id.foot_message);
		return FooterLoadView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	protected void setLoadMoreState(boolean isLoadMore, int code) {
		switch (code) {
		case BaseParser.SUCCESS:
			setLoadingMore();
			break;
		case BaseParser.ERROR:
			setLoadMoreError(isLoadMore);
			break;
		case BaseParser.EMPTY:
			setLoadMoreEnd();
			break;
		default:
			break;
		}
	}

	private void setLoadingMore() {
		mLoadMoreState = LOADING;
		addFooterView();
		if (FooterLoadView != null) {
			FooterLoadView.setVisibility(View.VISIBLE);
		}
		if (mFooterProgressBar != null) {
			mFooterProgressBar.setVisibility(View.VISIBLE);
		}
		if (mFooterMessage != null) {
			mFooterMessage.setText(R.string.loading);
		}
	}

	private void setLoadMoreError(boolean isLoadMore) {
		if (!isLoadMore) {
			return;
		}
		mLoadMoreState = LOADERROR;
		mFooterMessage.setText("点击加载更多...");
		mFooterProgressBar.setVisibility(View.GONE);
	}

	protected void setLoadMoreEnd() {
		mLoadMoreState = LOADEND;
		removeFooterView();
	}

	private void addFooterView() {
		if (mListView.getFooterViewsCount() == 0) {
			mListView.addFooterView(FooterLoadView);
		}
	}
	public void removeFooterView() {
		if (mListView.getFooterViewsCount() > 0) {
			mListView.removeFooterView(FooterLoadView);
		}
	}
	
}
