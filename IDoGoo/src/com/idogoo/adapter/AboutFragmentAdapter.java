package com.idogoo.adapter;

import com.idogoo.fragment.AboutListFragment;
import com.idogoo.paser.AboutParser;
import com.idogoo.utils.Constant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AboutFragmentAdapter extends FragmentPagerAdapter {

	private AboutParser parser;
	private String uType;

	public AboutFragmentAdapter(FragmentManager fm, AboutParser parser) {
		super(fm);
		this.parser = parser;
	}

	public void setData(AboutParser parser,String uType) {
		this.parser = parser;
		this.uType = uType;
		super.notifyDataSetChanged();
	}
	
	@Override
	public Fragment getItem(int pos) {
		if(null == parser) {
			return null;
		}
		return getFragment(pos);
	}

	private Fragment getFragment(int pos) {
		AboutListFragment fragment = new AboutListFragment();
		Bundle bundle = new Bundle();
		bundle.putString(Constant.EXTRA_JSON, parser.getObj().toString());
		if(0 == pos) {
			bundle.putInt(Constant.EXTRA_FRAGMENT_TYPE, AboutListFragment.ONGOING);
		}else {
			bundle.putInt(Constant.EXTRA_FRAGMENT_TYPE, AboutListFragment.FINISH);
		}
		bundle.putString(Constant.KEY_USER_UTYPE, uType);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(null == parser) {
			return 0;
		}
		return 2;
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
