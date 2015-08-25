package com.idogoo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cardview.CardView;
import com.idogoo.R;
import com.idogoo.paser.CommentParser.CommentItem;
import com.idogoo.paser.Tags;
import com.idogoo.utils.IDoGooUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class CommentListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<CommentItem> mList = new ArrayList<CommentItem>();

	public CommentListAdapter(Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
	}

	public void setData(List<CommentItem> list) {
		this.mList = list;
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return null == mList ? 0 : mList.size();
	}

	@Override
	public CommentItem getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.item_comment, parent,
					false);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.commnt_tv_title);
			holder.cardView = (CardView) convertView.findViewById(R.id.card);
			holder.tipsView = (RecyclerView) convertView.findViewById(R.id.user_tips);
			holder.tv_content = (TextView) convertView.findViewById(R.id.commnt_tv_content);
			holder.tv_name = (TextView) convertView.findViewById(R.id.commnt_tv_name);
			holder.mRatingBar = (RatingBar) convertView.findViewById(R.id.comment_rating_bar);
			holder.userIcon = (ImageView) convertView.findViewById(R.id.iv_user);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		bindData(holder, getItem(position));
		if (0 == position) {
			holder.cardView.setEnabledSides(true, false);
		} else if (0 < position && position < getCount() - 1) {
			holder.cardView.setEnabledSides(false, false);
		} else {
			holder.cardView.setEnabledSides(false, true);
		}
		return convertView;
	}

	private void bindData(ViewHolder holder, CommentItem item) {
		holder.tipsView.setHasFixedSize(true);
		holder.tipsView.setLayoutManager(new LinearLayoutManager(mContext,
				LinearLayout.HORIZONTAL, false));
		String tags = item.getTags();
		List<Tags> list = new ArrayList<Tags>();
		if(!TextUtils.isEmpty(tags)) {
			String[] tag = tags.split(",");
			for (int i = 0; i < tag.length; i++) {
				Tags mTag = new Tags(tag[i]);
				list.add(mTag);
			}
		}
		holder.tipsView.setAdapter(new TipsAdapter(mContext, list));
		holder.tv_content.setText(item.getContent());
		holder.mRatingBar.setNumStars(item.getScore());
		holder.tv_title.setText(item.getTitle());
		holder.tv_name.setText(item.getUser().getName());
		
		ImageLoader.getInstance().displayImage(item.getUser().getPicUrl(), holder.userIcon, IDoGooUtils.avatorOptions,new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				if (loadedImage != null) {
					loadedImage = IDoGooUtils
							.getRoundedCornerBitmap(
									loadedImage,
									loadedImage.getHeight() / 2);
				}
				((ImageView) view).setImageBitmap(loadedImage);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {

			}
		});
	}

	private class ViewHolder {
		public TextView tv_title,tv_name,tv_content;
		public CardView cardView;
		private RecyclerView tipsView;
		private ImageView userIcon;
		private RatingBar mRatingBar;
	}

}
