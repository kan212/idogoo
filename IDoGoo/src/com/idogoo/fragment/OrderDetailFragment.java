package com.idogoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idogoo.R;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.AboutItem;
import com.idogoo.paser.BaseParser;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;
import com.idogoo.utils.Constant;
import com.idogoo.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 订单详情
 * 
 * @author kan212
 *
 */
public class OrderDetailFragment extends Fragment {

	private View mView;
	private String uType;
	private String uid;
	private String order_id;
	
	private TextView tv_expert_name, tv_expert_intro, tv_expert_price,tv_time_zone,tv_location;
	private CircleImageView iv_expert;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (null != args) {
			uType = args.getString(Constant.KEY_USER_UTYPE);
			uid = args.getString(Constant.KEY_USER_ID);
			order_id = args.getString(Constant.KEY_ORDER_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_order_detail, container,
				false);
		iv_expert = (CircleImageView) mView.findViewById(R.id.iv_expert);
		tv_expert_name = (TextView) mView.findViewById(R.id.tv_expert_name);
		tv_expert_intro = (TextView) mView.findViewById(R.id.tv_expert_intro);
		tv_expert_price = (TextView) mView.findViewById(R.id.tv_expert_price);
		tv_time_zone = (TextView) mView.findViewById(R.id.tv_time_zone);
		tv_location = (TextView) mView.findViewById(R.id.tv_location);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		requestData();
	}

	private void requestData() {
		AboutItem parser = new AboutItem();
		IDoGooRequest request = null;
		if ("user".equals(uType)) {
			request = new IDoGooRequest(RequestUrl.getOrderInfoByUser(order_id,
					uid), parser, new OnProtocolTaskListener() {

				@Override
				public void onProgressUpdate(BaseParser parser) {
					initData((AboutItem)parser);
				}
			});
		} else if ("specialist".equals(uType)) {
			request = new IDoGooRequest(RequestUrl.getOrderInfoBySpecialist(
					order_id, uid), parser, new OnProtocolTaskListener() {

				@Override
				public void onProgressUpdate(BaseParser parser) {
					initData((AboutItem)parser);
				}
			});
		}
		if (null != request) {
			HttpUtil.addRequest(request, true);
		}
	}

	protected void initData(AboutItem parser) {
		if(BaseParser.SUCCESS == parser.getCode()) {
			ImageLoader.getInstance().displayImage(parser.getSpecialist_pic(), iv_expert);
			tv_expert_name.setText(parser.getSpecialist_name());
			tv_expert_intro.setText(parser.getTopic_title());
			tv_expert_price.setText(parser.getCreateTimestamp());
			tv_time_zone.setText(parser.getTime_zone());
			tv_location.setText(parser.getLocation_by_user());
		}
	}

}
