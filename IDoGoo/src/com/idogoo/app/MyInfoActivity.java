package com.idogoo.app;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.idogoo.R;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.BaseParser;
import com.idogoo.paser.LoginParser;
import com.idogoo.paser.MyInfoParser;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;
import com.idogoo.utils.Config;
import com.idogoo.utils.Constant;
import com.idogoo.utils.PhotoDialog;
import com.idogoo.utils.Variable;
import com.idogoo.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

public class MyInfoActivity extends Activity implements OnClickListener {

	private String mUid;
	private Button logoutBtn;
	private TextView tv_Nick, tv_Pwd, tv_Tel;
	private CircleImageView iv_User;
	private static final int RESULT_REQUEST_CODE = 2;
	private String userIconFilePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);
		initView();
		mUid = getIntent().getStringExtra(Constant.KEY_USER_ID);
	}

	private void initView() {
		logoutBtn = (Button) findViewById(R.id.logout_btn);
		tv_Nick = (TextView) findViewById(R.id.tv_nick_name);
		tv_Pwd = (TextView) findViewById(R.id.tv_pwd);
		tv_Tel = (TextView) findViewById(R.id.tv_tel);
		iv_User = (CircleImageView) findViewById(R.id.iv_user);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		iv_User.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		reqestData();
	}

	private void reqestData() {
		MyInfoParser parser = new MyInfoParser();
		IDoGooRequest request = new IDoGooRequest(RequestUrl.getUserInfo(mUid),
				parser, new OnProtocolTaskListener() {

					@Override
					public void onProgressUpdate(BaseParser parser) {
						initData((MyInfoParser) parser);
					}
				});
		HttpUtil.addRequest(request, true);

	}

	protected void initData(MyInfoParser parser) {
		if (BaseParser.SUCCESS == parser.getCode()) {
			tv_Nick.setText(parser.getNick_name());
			tv_Tel.setText(parser.getPhone());
			ImageLoader.getInstance().displayImage(parser.getPic(), iv_User);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_user:
			new PhotoDialog(MyInfoActivity.this).show();
			break;
		case R.id.logout_btn:
			logoutUser();
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_CANCELED) {
			switch (requestCode) {
			case Constant.IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case Constant.CAMERA_REQUEST_CODE:
				Uri uri = data.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
				startPhotoZoom(uri);
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			}
		}

	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 480);
		intent.putExtra("outputY", 480);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", false);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		userIconFilePath = data.getDataString();
		userIconFilePath = getFileName(userIconFilePath);
		updatePhoto(userIconFilePath);
		handler.sendEmptyMessage(0);
	}

	private void updatePhoto(final String url) {

		BaseParser parser = new BaseParser();
		IDoGooRequest request = new IDoGooRequest(RequestUrl.getUpToken(),
				parser, new OnProtocolTaskListener() {

					@Override
					public void onProgressUpdate(BaseParser parser) {
						if (null != parser.getObj()) {
							String uptoken = parser.getObj().optString(
									"uptoken");
							initData(uptoken, url);
						}
					}
				});
		HttpUtil.addRequest(request, true);
	}

	protected void initData(final String uptoken, final String url) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				UploadManager uploadManager = new UploadManager();
				uploadManager.put(url,null, uptoken,
						new UpCompletionHandler() {

							@Override
							public void complete(String arg0,
									ResponseInfo arg1, JSONObject arg2) {
								Config.e(arg0);
								Config.e(arg1);
								Config.e(arg2);
								if(arg1.isOK()) {
									String pic = "http://7xjv9k.com2.z0.glb.qiniucdn.com/" + arg2.optString("key");
									Message msg = new Message();
									msg.obj = pic;
									msg.what = 0;
									handler.sendMessage(msg);
								}
							}
						}, null);
			}
		}).start();
	}

	private Handler handler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ImageLoader.getInstance().displayImage((String) msg.obj, iv_User);
				break;
			}
		};
	};

	private String getFileName(String url) {
		Uri uri = Uri.parse(url);
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String img_path = actualimagecursor
				.getString(actual_image_column_index);
		return img_path;
	}

	private void logoutUser() {
		new LoginParser().clearFile();
		Variable.getInstance().setSessionId("");
		BaseParser parser = new BaseParser();
		IDoGooRequest request = new IDoGooRequest(RequestUrl.getUserLogout(),
				parser, new OnProtocolTaskListener() {

					@Override
					public void onProgressUpdate(BaseParser parser) {
						finish();
					}
				});
		HttpUtil.addRequest(request, true);
	}

}
