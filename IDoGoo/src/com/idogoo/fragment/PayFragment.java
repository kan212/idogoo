package com.idogoo.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alipay.PayActivity;
import com.idogoo.R;
import com.idogoo.paser.TopicParser;
import com.idogoo.utils.Constant;
import com.idogoo.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 支付
 * 
 * @author kan212
 *
 */
public class PayFragment extends Fragment implements OnClickListener{

	private View mView;
	private TextView tv_expert_name, tv_expert_intro, tv_expert_price;
	private CircleImageView iv_expert;
	private Button btn_pay;
	TopicParser parser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getActivity().getIntent().getExtras();
		if (null != args) {
			String json = args.getString(Constant.EXTRA_JSON);
			if (!TextUtils.isEmpty(json)) {
				try {
					parser = new TopicParser();
					parser.parseData(new JSONObject(json).optJSONObject("data"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_pay, container, false);
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
		initData();
		btn_pay.setOnClickListener(this);
	}

	private void initData() {
		if(!TextUtils.isEmpty(parser.getPhoto())) {
			ImageLoader.getInstance().displayImage(parser.getPhoto(), iv_expert);
		}
		tv_expert_name.setText(parser.getTruename());
		tv_expert_price.setText(parser.getPrice());
		tv_expert_intro.setText(parser.getTitle());		
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			((PayActivity) getActivity()).pay();
			break;
		}
	}

}
