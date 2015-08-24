package com.idogoo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.idogoo.R;
import com.idogoo.fragment.AboutListFragment;
import com.idogoo.paser.AboutItem;
import com.idogoo.utils.JumpUtils;
import com.idogoo.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AboutListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	private List<AboutItem> mList = new ArrayList<AboutItem>();
	private String uType;
	private int mType;

	public AboutListAdapter(Context context, String uType, int mType) {
		this.mContext = context;
		this.uType = uType;
		this.mType = mType;
		this.inflater = LayoutInflater.from(context);
	}

	public void setData(List<AboutItem> list) {
		this.mList = list;
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return null == mList ? 0 : mList.size();
	}

	@Override
	public AboutItem getItem(int position) {
		return null == mList ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_about_list, parent,
					false);
			holder.iv_user = (CircleImageView) convertView
					.findViewById(R.id.iv_user);
			holder.tv_user = (TextView) convertView.findViewById(R.id.tv_user);
			holder.tv_topic = (TextView) convertView
					.findViewById(R.id.tv_topic);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.btn_detail = (Button) convertView
					.findViewById(R.id.btn_detail);
			holder.btn_cancel_order = (Button) convertView
					.findViewById(R.id.btn_cancel_order);
			holder.btn_ready_detail = (Button) convertView
					.findViewById(R.id.btn_ready_detail);
			holder.btn_layout = convertView.findViewById(R.id.btn_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		bindData(holder, getItem(position));
		return convertView;
	}

	private void bindData(ViewHolder holder, final AboutItem item) {

		if ("user".equals(uType) || mType == AboutListFragment.ONGOING) {
			holder.btn_layout.setVisibility(View.VISIBLE);
			holder.btn_ready_detail.setVisibility(View.GONE);
		} else {
			holder.btn_layout.setVisibility(View.GONE);
			holder.btn_ready_detail.setVisibility(View.VISIBLE);
		}

		ImageLoader.getInstance().displayImage(item.getPic(), holder.iv_user);
		holder.tv_user.setText(item.getSpecialist_name());
		holder.tv_topic.setText(item.getTopic_title());
		holder.tv_time.setText(item.getCreateTimestamp());
		holder.btn_cancel_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				JumpUtils.startCancelOrderFragment(mContext,item.getId(),item.toString());
			}
		});
		holder.btn_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				JumpUtils.startOrderDetailFragment(
						mContext,
						"订单详情",
						"user".equals(uType) ? item.getUser_id() : item
								.getSpecialist_id(), uType, item.getId());
			}
		});
		holder.btn_ready_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				JumpUtils.startOrderDetailFragment(
						mContext,
						"订单详情",
						"user".equals(uType) ? item.getUser_id() : item
								.getSpecialist_id(), uType, item.getId());
			}
		});
	}

	private class ViewHolder {

		private CircleImageView iv_user;
		private TextView tv_user, tv_topic, tv_time;
		private Button btn_detail, btn_cancel_order, btn_ready_detail;
		private View btn_layout;

	}

}
