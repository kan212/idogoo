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
import com.alipay.PayActivity.PayResultLisenter;
import com.idogoo.R;
import com.idogoo.paser.TopicParser;
import com.idogoo.utils.Constant;
import com.idogoo.utils.JumpUtils;
import com.idogoo.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 支付
 * 
 * @author kan212
 *
 */
public class PayFragment extends Fragment implements OnClickListener,PayResultLisenter{

	private View mView;
	private TextView tv_expert_name, tv_expert_intro, tv_expert_price,tv_order_title;
	private CircleImageView iv_expert;
	private Button btn_pay;
	TopicParser parser;
	private String mJson;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getActivity().getIntent().getExtras();
		if (null != args) {
			mJson = args.getString(Constant.EXTRA_JSON);
			if (!TextUtils.isEmpty(mJson)) {
				try {
					parser = new TopicParser();
					parser.parseData(new JSONObject(mJson).optJSONObject("data"));
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
		initData();
		((PayActivity) getActivity()).setPayResultLisenter(this);
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
//			((PayActivity) getActivity()).pay();
			break;
		}
	}

	@Override
	public void success() {
		tv_order_title.setText("嗨~你已经完成支付。我们会尽快联系专家，若专家未确认，我们将退还全部费用，请耐心等待");
		btn_pay.setVisibility(View.GONE);
	}

	@Override
	public void fail() {
		tv_order_title.setText("支付失败");
		btn_pay.setVisibility(View.VISIBLE);
	}

}
