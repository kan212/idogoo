package com.idogoo.fragment;

import android.animation.ObjectAnimator;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.idogoo.R;
import com.idogoo.adapter.AboutFragmentAdapter;
import com.idogoo.app.SubActivity;
import com.idogoo.app.SubActivity.OnFinishListener;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.AboutParser;
import com.idogoo.paser.BaseParser;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;
import com.idogoo.utils.Constant;
import com.idogoo.utils.ScreenUtils;
import com.idogoo.widget.ViewPager;

/**
 * 约我的人或者约我的人
 * 
 * @author kan
 * 
 */

public class AboutFragment extends Fragment {

	private View mView;
	private ViewPager mViewPager;
	private AboutFragmentAdapter mAdapter;
	private AboutParser mAboutParser = null;
	private RadioGroup mTabs;

	private String id;
	private String uType;
	private ImageView cursor_view;
	private int bmpW;// 动画View宽度
	private int screenW;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		screenW = ScreenUtils.getScreenWidth(getActivity());
		if (null != args) {
			id = args.getString(Constant.KEY_USER_ID);
			uType = args.getString(Constant.KEY_USER_UTYPE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_about, container, false);
		mViewPager = (ViewPager) mView.findViewById(R.id.viewPager);
		mTabs = (RadioGroup) mView.findViewById(R.id.about_rb_tab);
		cursor_view = (ImageView) mView.findViewById(R.id.cursor_view);
		mTabs.setOnCheckedChangeListener(mOnCheckedChangeListener);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter = new AboutFragmentAdapter(getChildFragmentManager(),
				mAboutParser);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setOnPageChangeListener(mOnPageChangeListener);
		((SubActivity) getActivity()).setOnFinishListener(mOnFinishListener);
		initCursorView();
		requestData();
	}

	/**
	 * 初始化下标
	 */
	private void initCursorView() {
		if (getActivity() == null) {
			return;
		}
		int count = 2;
		bmpW = screenW / count;
		cursor_view.getLayoutParams().width = bmpW;
		if (count == 1) {
			cursor_view.setPadding(136, 0, 136, 0);
		} else {
			cursor_view.setPadding(20, 0, 20, 0);
		}
		Matrix matrix = new Matrix();
		matrix.postTranslate(screenW, 0);
		cursor_view.setImageMatrix(matrix);
	}

	private void requestData() {
		AboutParser parser = new AboutParser();
		IDoGooRequest request = null;
		if ("user".equals(uType)) {
			request = new IDoGooRequest(RequestUrl.getAbout(id), parser,
					new OnProtocolTaskListener() {

						@Override
						public void onProgressUpdate(BaseParser parser) {
							initData((AboutParser) parser);
						}
					});
		} else if ("specialist".equals(uType)) {
			request = new IDoGooRequest(RequestUrl.getSpecialistOrderList(id,
					1, 5), parser, new OnProtocolTaskListener() {

				@Override
				public void onProgressUpdate(BaseParser parser) {
					initData((AboutParser) parser);
				}
			});
		}
		HttpUtil.addRequest(request, true);
	}

	protected void initData(AboutParser parser) {
		if (BaseParser.SUCCESS == parser.getCode()) {
		}
		mAdapter.setData(parser, uType);
		mAdapter.finishUpdate(mViewPager);
	}
	
	OnFinishListener mOnFinishListener = new OnFinishListener() {
		
		@Override
		public boolean canFinish() {
			if (0 == mViewPager.getCurrentItem()) {
				return true;
			}
			return false;
		}
	};

	OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.tab1_btn_view:
				mViewPager.setCurrentItem(0);
				break;
			case R.id.tab2_btn_view:
				mViewPager.setCurrentItem(1);
				break;
			}
		}
	};

	private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
		private int mCurrIndex;

		@Override
		public void onPageSelected(int pos) {
			ObjectAnimator
			.ofFloat(cursor_view, "translationX", bmpW * mCurrIndex,
					bmpW * pos).setDuration(200).start();
			mCurrIndex = pos;
			RadioButton rb;
			switch (pos) {
			case 0:
				rb = (RadioButton) mTabs.getChildAt(0);
				rb.setChecked(true);
				break;
			case 1:
				rb = (RadioButton) mTabs.getChildAt(1);
				rb.setChecked(true);
				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

}
