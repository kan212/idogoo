package com.idogoo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idogoo.R;
import com.idogoo.paser.Tags;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.ViewHolder> {

	private Context mContext;
	private List<Tags> list = new ArrayList<Tags>();

	public TipsAdapter(Context context, List<Tags> list) {
		this.mContext = context;
		this.list = list;
		super.notifyDataSetChanged();
	}

	@Override
	public TipsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		View v = LayoutInflater.from(mContext).inflate(R.layout.item_tips,
				parent, false);
		ViewHolder vh = new ViewHolder((TextView) v.findViewById(R.id.tv_tips));
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.mTextView.setText(list.get(position).getTag());
	}

	@Override
	public int getItemCount() {
		return null == list ? 0 : list.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView mTextView;

		public ViewHolder(TextView v) {
			super(v);
			mTextView = v;
		}
	}

}
