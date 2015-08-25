package com.idogoo.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract.Constants;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.idogoo.R;


public class PhotoDialog extends Dialog implements android.view.View.OnClickListener{
	private Button mCancelBtn,mCameraBtn,mPhotoBtn;
	// 创建一个以当前时间为名称的文件
	private File tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
	private Context mContext;
	public PhotoDialog(Context context, int theme) {
		super(context, theme);
	}

	public PhotoDialog(Context context) {
		super(context,R.style.TimeDialog);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_choice_photo_layout);
		mCancelBtn = (Button) findViewById(R.id.btn_cancel);
		mCameraBtn = (Button) findViewById(R.id.btn_camera);
		mPhotoBtn = (Button) findViewById(R.id.btn_photolib);
		mCancelBtn.setOnClickListener(this);
		mCameraBtn.setOnClickListener(this);
		mPhotoBtn.setOnClickListener(this);
		Window dialogWindow = getWindow();
		dialogWindow.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_camera:
			dismiss();
			Intent intentFromCapture = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			// 判断存储卡是否可以用，可用进行存储
			if (FileUtil.haveSDCard()) {
				intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
			}
			((Activity)this.mContext).startActivityForResult(intentFromCapture,
					Constant.CAMERA_REQUEST_CODE);
			break;
		case R.id.btn_photolib:
			dismiss();
			Intent intentFromGallery = new Intent();
			intentFromGallery.setType("image/*"); // 设置文件类型
			intentFromGallery
			.setAction(Intent.ACTION_PICK);
			intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
			((Activity)this.mContext).startActivityForResult(intentFromGallery,
					Constant.IMAGE_REQUEST_CODE);
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		}
	}

	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss",Locale.CHINA);
		return dateFormat.format(date) + ".jpg";

	}

}
