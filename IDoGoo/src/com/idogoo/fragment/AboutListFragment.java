package com.idogoo.fragment;

import com.idogoo.R;
import com.idogoo.adapter.AboutListAdapter;
import com.idogoo.paser.AboutParser;
import com.idogoo.utils.Constant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 约我的人和我约的人共用的列表
 * @author kan212
 *
 */
public class AboutListFragment extends Fragment{

	public static final int ONGOING = 0;
	public static final int FINISH = 1;
	private View mView;
	private ListView mListView;
	private AboutParser parser;
	private int mType;
	private String uType;
	private AboutListAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if(null != args) {
			String json = args.getString(Constant.EXTRA_JSON);
			mType = args.getInt(Constant.EXTRA_FRAGMENT_TYPE);
			uType = args.getString(Constant.KEY_USER_UTYPE);
			parser = new AboutParser(json);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_about_list, container, false);
		mListView = (ListView) mView.findViewById(R.id.list);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	private void initData() {
		mAdapter = new AboutListAdapter(getActivity(),uType,mType);
		mListView.setAdapter(mAdapter);
		if(mType == ONGOING) {
			mAdapter.setData(parser.getOnGoingList());
		}else {
			mAdapter.setData(parser.getReadyList());
		}
	}
	
}
