package com.idogoo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cardview.CardView;
import com.idogoo.R;
import com.idogoo.paser.SpeListItem;
import com.idogoo.paser.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author kan 专家列表适配器
 *
 */
public class SpeListAdapter extends BaseAdapter {

	private Context mContext;
	private List<SpeListItem> mList = new ArrayList<SpeListItem>();
	
	public SpeListAdapter(Context context) {
		this.mContext = context;
	}

	public void setList(List<SpeListItem> list) {
		this.mList = list;
		super.notifyDataSetChanged();
	}
	
	public void addList(List<SpeListItem> list) {
		this.mList.addAll(list);
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public SpeListItem getItem(int position) {
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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_spelist, parent, false);
			holder.iv_user = (ImageView) convertView.findViewById(R.id.iv_user);
			holder.tips_1 = (TextView) convertView.findViewById(R.id.tv_tips_1);
			holder.tips_2 = (TextView) convertView.findViewById(R.id.tv_tips_2);
			holder.tips_3 = (TextView) convertView.findViewById(R.id.tv_tips_3);
			holder.tv_Location = (TextView) convertView
					.findViewById(R.id.tv_location);
			holder.tv_Name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.mCardView = (CardView) convertView
					.findViewById(R.id.cardView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		bindData(holder, getItem(position));
		return convertView;
	}

	/**
	 * 绑定视图
	 * 
	 * @param holder
	 * @param object
	 */
	private void bindData(ViewHolder holder, SpeListItem item) {
		if (null == item) {
			return;
		}
		holder.tips_1.setVisibility(View.GONE);
		holder.tips_2.setVisibility(View.GONE);
		holder.tips_3.setVisibility(View.GONE);

		UserInfo info = item.getUserInfo();
		if (null == info) {
			return;
		}
		ImageLoader.getInstance().displayImage(info.getPhoto(), holder.iv_user);
		holder.tv_Location.setText(info.getYour_city());
		holder.tv_Name.setText(info.getTruename());
		if (null == item.getTipList()) {
			return;
		}
		for (int i = 0; i < item.getTipList().size(); i++) {
			switch (i) {
			case 0:
				holder.tips_1.setVisibility(View.VISIBLE);
				holder.tips_1.setText(item.getTipList().get(i).getSummary());
				continue;
			case 1:
				holder.tips_2.setVisibility(View.VISIBLE);
				holder.tips_2.setText(item.getTipList().get(i).getSummary());
				continue;
			case 2:
				holder.tips_3.setVisibility(View.VISIBLE);
				holder.tips_3.setText(item.getTipList().get(i).getSummary());
				continue;
			}
		}
	}

	public class ViewHolder {
		ImageView iv_user;
		TextView tips_1, tips_2, tips_3, tv_Name, tv_Location;
		CardView mCardView;
	}
	
}
