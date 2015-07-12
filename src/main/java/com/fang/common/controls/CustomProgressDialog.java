package com.fang.common.controls;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.fang.common.R;

/**
 * 自定义进度条
 * @author fang
 *
 */
public class CustomProgressDialog extends ProgressDialog {
	
	private static CustomProgressDialog mDialog;
	
	private CustomProgressDialog(Context context) {
		super(context);
	}

	private CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customprogressdialog); 
	}

	public static CustomProgressDialog show(Context context) {
		if (null == mDialog) {
			mDialog = new CustomProgressDialog(context,
					R.style.dialog);
			mDialog.setCanceledOnTouchOutside(false);
		}
		mDialog.show();
		return mDialog;
	}
	public static void cancel(Context context) {
		if (null != mDialog) {
			mDialog.cancel();
			mDialog = null;
		}
	}

}
