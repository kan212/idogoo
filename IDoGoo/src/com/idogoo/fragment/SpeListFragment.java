package com.idogoo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.idogoo.R;
import com.idogoo.adapter.SpeListAdapter;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.BaseParser;
import com.idogoo.paser.SpeListItem;
import com.idogoo.paser.SpeListParser;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;
import com.idogoo.utils.Config;
import com.idogoo.utils.JumpUtils;
import com.idogoo.widget.PullRefreshLayout;

/**
 * 
 * @author kan 专家列表
 * 
 * 
 */
public class SpeListFragment extends BaseFragmentHasFooter {

	private View mView;
	private PullRefreshLayout mPullRefreshLayout;
	IDoGooRequest request ;
	private SpeListAdapter mAdapter;
	private int start = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_spelist, container, false);
		mPullRefreshLayout = (PullRefreshLayout) mView.findViewById(R.id.pull_to_refresh_View);
		mListView = (ListView) mView.findViewById(R.id.pull_list);
		mListView.addFooterView(onCreateFooterView(inflater));
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		requetData(false);
	}

	/**
	 * 初始化数据和视图
	 */
	private void initData() {
		mAdapter = new SpeListAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(mOnScrollListener);
		mListView.setOnItemClickListener(mOnItemClickListener);
	}

	private void requetData(final boolean isLoadMore) {
		SpeListParser parser = new SpeListParser();
		if (null != request && !request.hasHadResponseDelivered()) {
			request.cancel();
		}
		if(isLoadMore) {
			start += 5;
		}
		if(start > 95) {
			setLoadMoreState(isLoadMore, BaseParser.EMPTY);
			return;
		}
		
		OnProtocolTaskListener callBack = new OnProtocolTaskListener() {

			@Override
			public void onProgressUpdate(BaseParser parser) {
				refreshData(isLoadMore, (SpeListParser) parser);
			}
		};
		request = new IDoGooRequest(RequestUrl.getSpeListReqeust(start), parser, callBack);
		
		HttpUtil.addRequest(request,true);
	}

	/**
	 * 刷新视图
	 * @param parser
	 */
	protected void refreshData(boolean isLoadMore,SpeListParser parser) {
		if (isDetached() || null == getActivity()) {
			return;
		}
		setLoadMoreState(isLoadMore, parser.getCode());
		
		if(parser.getCode() == BaseParser.SUCCESS) {
			if(!isLoadMore) {
				mAdapter.setList(parser.getList());
			}else {
				mAdapter.addList(parser.getList());
			}
		}
		isLoadedOver(isLoadMore);
	}
	
	private void isLoadedOver(final boolean isLoadMore) {
		mListView.post(new Runnable() {
			@Override
			public void run() {
				if (!isLoadMore && mAdapter != null
						&& mListView.getLastVisiblePosition() == mAdapter.getCount()) {
					setLoadMoreState(isLoadMore, BaseParser.EMPTY);
				}	
			}
		});
	}
	
	OnScrollListener mOnScrollListener = new OnScrollListener() {
		private boolean isScollToFoot;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE && isScollToFoot) {
				requetData(true);
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem + visibleItemCount == totalItemCount) {
				isScollToFoot = true;
			} else {
				isScollToFoot = false;
			}
		}
	};
	
	
	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(position < 0) {
				return;
			}
			if (position == mAdapter.getCount()) {
				setLoadMoreState(true, BaseParser.SUCCESS);
				requetData(true);
				return;
			}
			SpeListItem item = mAdapter.getItem(position);
			if(null != item) {
				JumpUtils.startExpertDetail(getActivity(), item.getUserInfo().getUser_id());;
			}
		}
	};
	
}
