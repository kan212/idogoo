package com.idogoo.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.idogoo.R;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.AboutItem;
import com.idogoo.paser.BaseParser;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;
import com.idogoo.utils.Config;
import com.idogoo.utils.Constant;
import com.idogoo.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 取消订单
 * @author kan212
 *
 */
public class CancelOrderFragment extends Fragment implements OnClickListener {

	private View mView;
	private AboutItem item;
	private String order_id;

	private TextView tv_1, tv_2, tv_3;
	private TextView tv_expert_name, tv_expert_intro, tv_expert_price;
	private CircleImageView iv_expert;
	private Button btn_submit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (null != args) {
			order_id = args.getString(Constant.KEY_ORDER_ID);
			try {
				item = new AboutItem(new JSONObject(
						args.getString(Constant.EXTRA_JSON)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_cancel_order, container,
				false);
		iv_expert = (CircleImageView) mView.findViewById(R.id.iv_expert);
		tv_expert_name = (TextView) mView.findViewById(R.id.tv_expert_name);
		tv_expert_intro = (TextView) mView.findViewById(R.id.tv_expert_intro);
		tv_expert_price = (TextView) mView.findViewById(R.id.tv_expert_price);
		tv_1 = (TextView) mView.findViewById(R.id.tv_1);
		tv_2 = (TextView) mView.findViewById(R.id.tv_2);
		tv_3 = (TextView) mView.findViewById(R.id.tv_3);
		btn_submit = (Button) mView.findViewById(R.id.btn_submit);
		initView();
		return mView;
	}

	/**
	 * 传递进来的数据直接布局
	 */
	private void initView() {
		ImageLoader.getInstance().displayImage(item.getPic(), iv_expert);
		tv_expert_name.setText(item.getSpecialist_name());
		tv_expert_intro.setText(item.getTopic_title());
		tv_expert_price.setText(item.getPrice() + "元/次(1小时左右)");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btn_submit.setOnClickListener(this);
		tv_1.setOnClickListener(this);
		tv_2.setOnClickListener(this);
		tv_3.setOnClickListener(this);
	}

	private void requestData() {
		BaseParser parser = new BaseParser();
		IDoGooRequest request = new IDoGooRequest(
				RequestUrl.UrlCancelOrderMeetByUser,
				RequestUrl.postCancelOrderMeetByUser(order_id, "124"), parser,
				new OnProtocolTaskListener() {

					@Override
					public void onProgressUpdate(BaseParser parser) {
						initData(parser);
					}
				});
		HttpUtil.addRequest(request, true);
	}

	protected void initData(BaseParser parser) {
		Config.e(parser.getMsg());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			requestData();
			break;
		case R.id.tv_1:
			tv_1.setBackgroundColor(getResources().getColor(
					R.color.bg_concel_order_reason));
			break;
		case R.id.tv_2:
			tv_2.setBackgroundColor(getResources().getColor(
					R.color.bg_concel_order_reason));
			break;
		case R.id.tv_3:
			tv_3.setBackgroundColor(getResources().getColor(
					R.color.bg_concel_order_reason));
			break;
		}
	}
}
