package com.idogoo.fragment;

import com.android.volley.Request;
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
import com.idogoo.widget.MyEditText;
import com.idogoo.widget.PullRefreshLayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * @author kan 专家列表
 * 
 * 
 */
public class SpeListFragment extends BaseFragmentHasFooter {

	private View mView;
	private ListView mListView;
	private PullRefreshLayout mPullRefreshLayout;
	 
	private SpeListAdapter mAdapter;
	
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
		requetData();
	}

	/**
	 * 初始化数据和视图
	 */
	private void initData() {
		mAdapter = new SpeListAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mOnItemClickListener);
	}

	private void requetData() {
		SpeListParser parser = new SpeListParser();
		Request<BaseParser> request = null;
		request = RequestUrl.getSpeListReqeust(parser,new OnProtocolTaskListener() {
			
			@Override
			public void onProgressUpdate(BaseParser parser) {
				refreshData((SpeListParser) parser);
			}
		},0,0,0);
		HttpUtil.addRequest(request,true);
	}

	/**
	 * 刷新视图
	 * @param parser
	 */
	protected void refreshData(SpeListParser parser) {
		Config.e(parser.getCode());
		if(parser.getCode() == BaseParser.SUCCESS) {
			mAdapter.setList(parser.getList());
		}
	}
	
	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(position < 0) {
				return;
			}
			SpeListItem item = mAdapter.getItem(position);
			if(null != item) {
				JumpUtils.startExpertDetail(getActivity(), item.getUserInfo().getUser_id());;
			}
		}
	};
	
}
