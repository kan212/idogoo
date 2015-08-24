package com.idogoo.utils;

import java.io.File;
import java.util.Date;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Config {

	public static boolean isDebug = true;
	public static boolean isLogSave = true;
	public static final String TAG = "Config";

	public static void e(Object msg) {
		if (isDebug)
			Log.e(getTag(4), msg == null ? "null" : msg.toString());
	}

	public static void d(Object msg) {
		if (isDebug)
			Log.d(getTag(4), msg == null ? "null" : msg.toString());
	}

	public static void i(Object msg) {
		if (isDebug)
			Log.i(getTag(4), msg == null ? "null" : msg.toString());
	}

	private static File logFile;
	private static Date logDate;

	public static void s(Object msg) {
		if (isLogSave) {
			if (null == logFile) {
				File dir = FileUtil.getSdcardDirectory("log");
				logFile = new File(dir.getPath(), "log.txt");
				logDate = new Date();
			}
			logDate.setTime(System.currentTimeMillis());
			StringBuilder sb = new StringBuilder();
			sb.append(logDate.toString()).append(' ');
			sb.append(getTag(4)).append(' ');
			sb.append(msg == null ? "null" : msg.toString()).append('\n');
			FileUtil.saveBytesToFile(sb.toString().getBytes(), logFile, true);
		}
	}

	public static void showTip(Context context, String msg) {
		if (null == context) {
			return;
		}
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void showTip(Context context, int msg) {
		if (null == context) {
			return;
		}
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static String getTag(int level) {
		StackTraceElement ste = Thread.currentThread().getStackTrace()[level];
		StringBuilder sb = new StringBuilder();
		sb.append(getSimpleClassName(ste.getClassName()));
		sb.append('.');
		sb.append(ste.getMethodName());
		sb.append('(');
		sb.append(ste.getLineNumber());
		sb.append(')');
		return sb.toString();
	}

	public static String getSimpleClassName(String path) {
		int index = path.lastIndexOf('.');
		if (index < 0) {
			return path;
		} else {
			return path.substring(index + 1);
		}
	}
}
