package com.idogoo.app;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.android.volley.RequestQueue;
import com.idogoo.db.DatabaseHelper;
import com.idogoo.utils.FileManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class IDoGooApp extends Application {

	private static DatabaseHelper mDBHelper;
	private static Context mContext;
	public static int mVersionCode;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		mDBHelper = DatabaseHelper.getInstance(this);
		initImageLoader(getApplicationContext());
		AppUncaughtException.getIntance().init(getApplicationContext());
	}

	public static DatabaseHelper getDatabaseHelper() {
		return mDBHelper;
	}

	private static RequestQueue mRequestQueue;

	public static void setRequestQueue(RequestQueue requestQueue) {
		mRequestQueue = requestQueue;
	}

	public static RequestQueue getRequestQueue() {
		return mRequestQueue;
	}

	public static Context getContext() {
		return mContext;
	}

	private void initImageLoader(Context applicationContext) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(mContext,
				FileManager.PATH_IMG);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.resetViewBeforeLoading(true)
				// .displayer(new FadeInBitmapDisplayer(500))
				.imageScaleType(ImageScaleType.EXACTLY).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mContext).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(3)
				.denyCacheImageMultipleSizesInMemory()
				.discCache(new UnlimitedDiscCache(cacheDir))
				.memoryCache(new WeakMemoryCache())
				// default
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.defaultDisplayImageOptions(options).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public static int getVersionCode() {
		try {
			PackageInfo info = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0);
			mVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return mVersionCode;

	}

}
