package com.idogoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.idogoo.R;
import com.idogoo.widget.CircleImageView;

/**
 * 专家设置时间和地点
 * @author kan212
 *
 */
public class ExpertSetFragment extends Fragment implements OnClickListener{

	private View mView;
	private TextView tv_expert_name,tv_expert_intro,tv_expert_price;
	private CircleImageView iv_expert;
	private Button btn_submit;
	private EditText editPosition,editTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_expert_set, container, false);
		iv_expert = (CircleImageView) mView.findViewById(R.id.iv_expert);
		tv_expert_name = (TextView) mView.findViewById(R.id.tv_expert_name);
		tv_expert_intro = (TextView) mView.findViewById(R.id.tv_expert_intro);
		tv_expert_price = (TextView) mView.findViewById(R.id.tv_expert_price);
		btn_submit = (Button) mView.findViewById(R.id.btn_submit);
		editPosition = (EditText) mView.findViewById(R.id.edit_position);
		editTime = (EditText) mView.findViewById(R.id.edit_time);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btn_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			
			break;
		}
	}

	
}
