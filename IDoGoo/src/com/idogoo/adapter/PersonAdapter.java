package com.idogoo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.idogoo.R;
import com.idogoo.paser.PersonItem;

public class PersonAdapter extends BaseAdapter {

	private Context mContext;
	private List<PersonItem> mList = new ArrayList<PersonItem>();

	public PersonAdapter(Context context) {
		this.mContext = context;
	}

	public void setData(List<PersonItem> list) {
		this.mList = list;
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public PersonItem getItem(int position) {
		return null == mList ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_person, parent, false);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(getItem(position).getTitle());
		return convertView;
	}
	
	public class ViewHolder {
		private TextView tv;
	}

}
