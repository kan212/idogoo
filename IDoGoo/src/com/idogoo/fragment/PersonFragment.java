package com.idogoo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.idogoo.R;
import com.idogoo.adapter.PersonAdapter;
import com.idogoo.paser.BaseParser;
import com.idogoo.paser.LoginParser;
import com.idogoo.paser.PersonItem;
import com.idogoo.utils.JumpUtils;
import com.idogoo.utils.Variable;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PersonFragment extends Fragment {

	private View mView;
	private ListView mListView;
	private List<PersonItem> mList = new ArrayList<PersonItem>();
	private PersonAdapter mAdapter;
	private ImageView userIcon;
	private TextView userName;
	private LoginParser parser = new LoginParser();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_person, container, false);
		userIcon = (ImageView) mView.findViewById(R.id.iv_user);
		userName = (TextView) mView.findViewById(R.id.tv_user);
		mListView = (ListView) mView.findViewById(R.id.person_list);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(Variable.getInstance().getUserLogin()) {
			parser.parse(parser.readCache());
			if(BaseParser.SUCCESS == parser.getCode()) {
				userName.setText(parser.getUser_name());
				ImageLoader.getInstance().displayImage(parser.getPic(), userIcon);
			}
		}else {
			userName.setText("");
			ImageLoader.getInstance().displayImage("", userIcon);
		}
	}

	private void initData() {
		mList = new ArrayList<PersonItem>();
		mList.add(new PersonItem(PersonItem.COVENANT));
		mList.add(new PersonItem(PersonItem.ABOUT));
		mList.add(new PersonItem(PersonItem.MESSAGE));
		mList.add(new PersonItem(PersonItem.INFORMATION));
		mAdapter = new PersonAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		mAdapter.setData(mList);
		mListView.setOnItemClickListener(mOnItemClick);
	}

	private OnItemClickListener mOnItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position < 0) {
				return;
			}
			
			if(!Variable.getInstance().getUserLogin()) {
				JumpUtils.startLogin(getActivity());
				return;
			}
			
			PersonItem item = mAdapter.getItem(position);
			switch (item.getType()) {
			case PersonItem.ABOUT:
				JumpUtils.startAbout(getActivity(),"约我的人",parser.getId(),"specialist");
				break;
			case PersonItem.COVENANT:
				JumpUtils.startAbout(getActivity(),"我约的人",parser.getId(),"user");
				break;
			case PersonItem.INFORMATION:
				JumpUtils.startUserInfo(getActivity(),parser.getId());
				break;
			case PersonItem.MESSAGE:
				JumpUtils.startLogin(getActivity());
				break;
			default:
				break;
			}
		}
	};

}
