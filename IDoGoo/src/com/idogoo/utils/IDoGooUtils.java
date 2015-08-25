package com.idogoo.utils;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;

import com.idogoo.R;
import com.idogoo.app.IDoGooApp;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class IDoGooUtils {

	/**
	 * getAvatorBitmap:获取正圆图标. <br/>
	 * 
	 * @param origBmp
	 * @return
	 */
	public static Bitmap getAvatorBitmap(Bitmap origBmp) {
		if (origBmp == null) {
			return null;
		}
		// 创建新的图片以及画布
		int size = Math.min(origBmp.getWidth(), origBmp.getHeight());
		float roundPx = size / 2;
		Bitmap output = Bitmap.createBitmap(size, size, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		int left = (origBmp.getWidth() - size) / 2;
		int top = (origBmp.getHeight() - size) / 2;
		final Rect src = new Rect(left, top, left + size, top + size);
		final RectF dst = new RectF(0, 0, size, size);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(dst, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(origBmp, src, dst, paint);

		return output;
	}
	/**
	 * 获取圆角图片
	 * 
	 * @param origBmp
	 *            原始bitmap
	 * @param roundPx
	 *            圆角弧度（像素）
	 * @return 圆角的bitmap
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap origBmp, float roundPx) {
		if (origBmp == null) {
			return null;
		}
		// 创建新的图片以及画布
		Bitmap output = Bitmap.createBitmap(origBmp.getWidth(),
				origBmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, origBmp.getWidth(),
				origBmp.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(origBmp, rect, rect, paint);

		return output;
	}

	/**
	 * 判断是不是有效的电话号码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isMobileNo(String phoneNumber) {
		Pattern pattern = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = pattern.matcher(phoneNumber);
		return m.matches();
	}
	
	/**
	 * 获取手机Token
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		if (null == context) {
			return "";
		}
		TelephonyManager telM = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telM == null ? "" : telM.getDeviceId();
	}
	

	/**
	 * client_ticket服务端有效期2小时，请客户端自行维护换过期并获取新的client_ticket机制(可以通过获取ticket有效期来辅助管理)。
	 * 判断文件2个小时有效
	 * @param fileName
	 * @return
	 */
	public boolean isCacheValid(String fileName) {
		long validTime = 60 * 60 * 1000 * 2;
		File file = new File(IDoGooApp.getContext().getFilesDir(), fileName);
		if (!file.exists()) {
			return false;
		}
		long durationTime = System.currentTimeMillis() - file.lastModified();
		durationTime /= 1000;
		if (durationTime > validTime) {
			return false;
		}
		byte[] buffer = FileUtil.readFile(IDoGooApp.getContext(), fileName);
		return null != buffer;
	}

	public static int getVersionCode(Context context) {
		int versionCode = 0;
		PackageManager manager = context.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
			versionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
	
	public static String format(String uri, List<NameValuePair> params,
			boolean isEncoded) {
		StringBuilder sb = new StringBuilder(uri);
		if (!uri.endsWith("?")) {
			sb.append('?');
		}
		if (isEncoded) {
			sb.append(URLEncodedUtils.format(params, "utf-8"));
		} else {
			String value = null;
			for (NameValuePair nvp : params) {
				value = nvp.getValue();
				if (value == null) {
					value = "";
				}
				sb.append(nvp.getName()).append('=');
				sb.append(value);
				sb.append('&');
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	public static Drawable createScaledBitmap(Bitmap src, int dstWidth,
			boolean filter) {
		Matrix m = new Matrix();
		final int width = src.getWidth();
		final int height = src.getHeight();
		float sx = dstWidth / (float) width;
		m.setScale(sx, sx);
		Bitmap b = Bitmap.createBitmap(src, 0, 0, width, height, m, filter);
		return new BitmapDrawable(b);
	}
	
	public static DisplayImageOptions avatorOptions = new DisplayImageOptions.Builder()
	.cacheInMemory(true).cacheOnDisc(true)
	.imageScaleType(ImageScaleType.EXACTLY)
	.resetViewBeforeLoading(true).considerExifParams(true)
	.showImageForEmptyUri(R.drawable.ic_launcher)
	.showImageOnFail(R.drawable.ic_launcher)
	.build();

}
