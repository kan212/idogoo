package com.idogoo.app;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.util.Log;

public class AppUncaughtException implements UncaughtExceptionHandler {

	private static AppUncaughtException appUncaughtException;

	public static AppUncaughtException getIntance() {
		if (appUncaughtException == null) {
			appUncaughtException = new AppUncaughtException();
		}
		return appUncaughtException;
	}

	private AppUncaughtException() {

	}
	
	public void init(Context context) {
		Thread.setDefaultUncaughtExceptionHandler(appUncaughtException);
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		ex.printStackTrace();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Log.e(getClass().getSimpleName(), e.getStackTrace().toString());
		}

		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
