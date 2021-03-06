package com.idogoo.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.idogoo.R;
import com.idogoo.fragment.SpeListFragment;
import com.idogoo.service.IdogooService;
import com.idogoo.utils.Config;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private DrawerLayout mDrawer;
	private LinearLayout leftDrawer;
	private FragmentManager mFragmentManager;
	private SpeListFragment mSpeListFragment;
	private PushAgent mPushAgent;
	private FrameLayout iv_left;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iv_left = (FrameLayout) findViewById(R.id.iv_title_left);
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.onAppStart();
		mPushAgent.enable();
		mPushAgent.setPushIntentServiceClass(IdogooService.class);
		String device_token = UmengRegistrar.getRegistrationId(this);
		Config.e("device_token == " + device_token);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		initDrawLayout();
		initView();
		iv_left.setOnClickListener(this);
	}

	private void initView() {
		mFragmentManager = getSupportFragmentManager();
		if (null == mSpeListFragment) {
			mSpeListFragment = new SpeListFragment();
		}
		mFragmentManager.beginTransaction()
				.add(R.id.fl_layout, mSpeListFragment, "spe")
				.commitAllowingStateLoss();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_title_left:
			if (mDrawer != null) {
				if (!mDrawer.isDrawerOpen(leftDrawer)) {
					mDrawer.openDrawer(leftDrawer);
				} else {
					mDrawer.closeDrawers();
				}
			}
			break;
		}
	}

}
