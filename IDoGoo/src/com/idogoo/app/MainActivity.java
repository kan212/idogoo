package com.idogoo.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.idogoo.R;
import com.idogoo.fragment.SpeListFragment;
import com.idogoo.service.IdogooService;
import com.umeng.message.PushAgent;

public class MainActivity extends FragmentActivity {

	private DrawerLayout mDrawer;
	private LinearLayout leftDrawer;
	private FragmentManager mFragmentManager;
	private SpeListFragment mSpeListFragment;
	private PushAgent mPushAgent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PushAgent.getInstance(this).onAppStart();
		mPushAgent.enable();
		mPushAgent.setPushIntentServiceClass(IdogooService.class);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		initDrawLayout();
		initView();
	}

	private void initView() {
		mFragmentManager = getSupportFragmentManager();
		if (null == mSpeListFragment) {
			mSpeListFragment = new SpeListFragment();
		}  
		mFragmentManager.beginTransaction()
		.add(R.id.fl_layout, mSpeListFragment, "spe").commitAllowingStateLoss();
	}

	private void initDrawLayout() {
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		mDrawer.setDrawerTitle(Gravity.START,
				getResources().getString(R.string.app_name));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void finish() {
		if (mDrawer.isDrawerOpen(leftDrawer)) {
			mDrawer.closeDrawers();
			return;
		}
		super.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
