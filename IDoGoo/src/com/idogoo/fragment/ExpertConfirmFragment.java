package com.idogoo.fragment;

import com.idogoo.R;
import com.idogoo.paser.TopicParser;
import com.idogoo.widget.CircleImageView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 专家已经确认
 * @author kan212
 *
 */
public class ExpertConfirmFragment extends Fragment implements View.OnClickListener{

	private View mView;
	private TextView tv_expert_name, tv_expert_intro, tv_expert_price,tv_order_title;
	private CircleImageView iv_expert;
	private Button btn_pay;
	TopicParser parser;
	private String mJson;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if(null != args) {
			
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_expert_confirm, container, false);
		tv_order_title = (TextView) mView.findViewById(R.id.tv_order_title);
		iv_expert = (CircleImageView) mView.findViewById(R.id.iv_expert);
		tv_expert_name = (TextView) mView.findViewById(R.id.tv_expert_name);
		tv_expert_intro = (TextView) mView.findViewById(R.id.tv_expert_intro);
		tv_expert_price = (TextView) mView.findViewById(R.id.tv_expert_price);
		btn_pay = (Button) mView.findViewById(R.id.btn_submit);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btn_pay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
	}
	
}
