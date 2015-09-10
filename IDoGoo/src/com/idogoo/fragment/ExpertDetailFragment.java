package com.idogoo.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.idogoo.R;
import com.idogoo.adapter.CommentListAdapter;
import com.idogoo.adapter.TipsAdapter;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.BaseParser;
import com.idogoo.paser.CommentParser;
import com.idogoo.paser.ExpertDetailParser;
import com.idogoo.paser.TopicParser;
import com.idogoo.paser.UserInfo;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;
import com.idogoo.utils.Constant;
import com.idogoo.utils.JumpUtils;
import com.idogoo.widget.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ExpertDetailFragment extends Fragment {

	private View mView, mHeaderView;
	private String mUid;

	private RoundedImageView iv_user;
	private TextView tv_name, tv_location, tv_summary,tv_meet,tv_opinion;
	private RecyclerView userTip;
	private TipsAdapter mTipsAdapter;
	private LinearLayout topicLayout;
	private ListView commentList;
	private CommentListAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (null != args) {
			mUid = args.getString(Constant.KEY_USER_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_expert_detail, container,
				false);
		commentList = (ListView) mView.findViewById(R.id.comment_list);
		commentList.addHeaderView(getHeader(inflater, container));
		return mView;
	}

	private View getHeader(LayoutInflater inflater, ViewGroup container) {
		mHeaderView = LayoutInflater.from(getActivity()).inflate(
				R.layout.header_expert_detail, null, false);
		iv_user = (RoundedImageView) mHeaderView.findViewById(R.id.iv_user);
		tv_location = (TextView) mHeaderView.findViewById(R.id.user_location);
		tv_name = (TextView) mHeaderView.findViewById(R.id.user_name);
		tv_summary = (TextView) mHeaderView.findViewById(R.id.user_summary);
		tv_opinion = (TextView) mHeaderView.findViewById(R.id.tv_opinion);
		tv_meet = (TextView) mHeaderView.findViewById(R.id.tv_meet);
		userTip = (RecyclerView) mHeaderView.findViewById(R.id.user_tips);
		topicLayout = (LinearLayout) mHeaderView
				.findViewById(R.id.topic_layout);
		userTip.setHasFixedSize(true);
		userTip.setLayoutManager(new LinearLayoutManager(getActivity(),
				LinearLayout.HORIZONTAL, false));
		return mHeaderView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter = new CommentListAdapter(getActivity());
		commentList.setAdapter(mAdapter);
		requsetData();
	}

	/**
	 * 请求数据
	 */
	private void requsetData() {
		ExpertDetailParser parser = new ExpertDetailParser();
		CommentParser commentParser = new CommentParser();
		IDoGooRequest request = new IDoGooRequest(
				RequestUrl.getExpertDetail(mUid), parser,
				new OnProtocolTaskListener() {

					@Override
					public void onProgressUpdate(BaseParser parser) {
						initHeaderData((ExpertDetailParser) parser);
					}
				});
		HttpUtil.addRequest(request,true);
		request = new IDoGooRequest(RequestUrl.getEvaluateList(mUid, 1, 5),
				commentParser, new OnProtocolTaskListener() {

					@Override
					public void onProgressUpdate(BaseParser parser) {
						initCommentData((CommentParser) parser);
					}
				});
		HttpUtil.addRequest(request,true);
	}

	protected void initCommentData(CommentParser parser) {
		if (BaseParser.SUCCESS == parser.getCode()) {
			mAdapter.setData(parser.getList());
		}
	}

	/**
	 * 处理数据
	 * 
	 * @param parser
	 */
	protected void initHeaderData(ExpertDetailParser parser) {
		if (BaseParser.SUCCESS == parser.getCode()) {
			UserInfo userInfo = parser.getUserInfo();
			if (null != userInfo) {
				ImageLoader.getInstance().displayImage(userInfo.getPhoto(),
						iv_user);
				tv_location.setText(userInfo.getYour_city());
				tv_name.setText(userInfo.getTruename());
				tv_summary.setText(userInfo.getSummary());
			}
			tv_meet.setText("被约见  " + parser.getMeet_num() + "次");
			mTipsAdapter = new TipsAdapter(getActivity(), parser.getTagList());
			userTip.setAdapter(mTipsAdapter);
			addTopicLayout(parser.getTopicList());
		}
	}

	private void addTopicLayout(List<TopicParser> list) {
		if(null == getActivity()) {
			return ;
		}
		for (int i = 0; i < list.size(); i++) {
			View v = LayoutInflater.from(getActivity()).inflate(
					R.layout.item_topic, null, false);
			TextView tv_topic = (TextView) v.findViewById(R.id.tv_topic);
			TextView tv_content = (TextView) v.findViewById(R.id.tv_content);
			TextView tv_price = (TextView) v.findViewById(R.id.tv_price);
			TextView tv_appoint = (TextView) v.findViewById(R.id.tv_appoint);
			View divider_view = v.findViewById(R.id.divider_view);
			final TopicParser item = list.get(i);
			tv_price.setText(item.getPrice() + "元/小时");
			tv_topic.setText(item.getTitle());
			tv_content.setText(item.getSummary());
			if(i == list.size() - 1) {
				divider_view.setVisibility(View.GONE);
			}
			tv_appoint.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					JumpUtils.startAppoint(getActivity(),item.getTopic_id());
				}
			});
			topicLayout.addView(v);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
