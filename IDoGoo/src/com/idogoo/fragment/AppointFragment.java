package com.idogoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.idogoo.R;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.BaseParser;
import com.idogoo.paser.TopicParser;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;
import com.idogoo.utils.Constant;
import com.idogoo.utils.JumpUtils;
import com.idogoo.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 约见
 * 
 * @author kan212
 *
 */
public class AppointFragment extends Fragment implements OnClickListener{

	private View mView;
	private String topicId;
	
	private TextView tv_expert_name,tv_expert_intro,tv_expert_price;
	private CircleImageView iv_expert;
	private Button btn_submit;
	private EditText editQusetion,editInfo,editPosition,editTime;
	private TopicParser mTopicParser;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if(null != args) {
			topicId = args.getString(Constant.KEY_TOPIC_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_appoint, container, false);
		iv_expert = (CircleImageView) mView.findViewById(R.id.iv_expert);
		tv_expert_name = (TextView) mView.findViewById(R.id.tv_expert_name);
		tv_expert_intro = (TextView) mView.findViewById(R.id.tv_expert_intro);
		tv_expert_price = (TextView) mView.findViewById(R.id.tv_expert_price);
		btn_submit = (Button) mView.findViewById(R.id.btn_submit);
		editQusetion = (EditText) mView.findViewById(R.id.edit_qustion);
		editInfo = (EditText) mView.findViewById(R.id.edit_position);
		editPosition = (EditText) mView.findViewById(R.id.edit_position);
		editTime = (EditText) mView.findViewById(R.id.edit_time);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btn_submit.setOnClickListener(this);
		requestData();
	}

	private void requestData() {
		TopicParser parser = new TopicParser();
		IDoGooRequest request = new IDoGooRequest(RequestUrl.getTopicById(topicId), parser, new OnProtocolTaskListener() {
			
			@Override
			public void onProgressUpdate(BaseParser parser) {
				initData((TopicParser)parser);
			}
		});
		HttpUtil.addRequest(request,true);
	}

	protected void initData(TopicParser parser) {
		if(BaseParser.SUCCESS == parser.getCode()) {
			ImageLoader.getInstance().displayImage(parser.getPhoto(), iv_expert);
			tv_expert_name.setText(parser.getTruename());
			tv_expert_price.setText(parser.getPrice());
			tv_expert_intro.setText(parser.getTitle());
			mTopicParser = parser;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			submitInfo(editQusetion.getText().toString(),editInfo.getText().toString(),editPosition.getText().toString(),editTime.getText().toString());
			break;
		}
	}

	private void submitInfo(String s1, String s2, String s3,
			String s4) {
		BaseParser parser = new BaseParser();
		IDoGooRequest request = new IDoGooRequest(RequestUrl.UrlPostEditOrder, RequestUrl.postEditOrder(topicId, s1, s2, s3, s4), parser, new OnProtocolTaskListener() {
			
			@Override
			public void onProgressUpdate(BaseParser parser) {
				initData(parser);
			}
		});
		HttpUtil.addRequest(request, true);
	}

	private void initData(BaseParser parser) {
		JumpUtils.startPayFragment(getActivity(), mTopicParser.getObj().toString());
		if(BaseParser.SUCCESS == parser.getCode()) {
			if(null != mTopicParser) {
			}
		}
	}
	
}
