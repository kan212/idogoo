package com.idogoo.fragment;

import com.idogoo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 已经取消的订单
 * @author kan212
 *
 */
public class CanceledOrderFragment extends Fragment{

	private View mView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if(null != args) {
			
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView  = inflater.inflate(R.layout.fragment_canceled_order, container,false);
		
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		requestData();
	}

	private void requestData() {
		
	}
	
}
