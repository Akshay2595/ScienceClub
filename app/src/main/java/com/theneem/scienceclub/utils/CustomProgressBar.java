package com.theneem.scienceclub.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import com.theneem.scienceclub.R;

import java.util.Objects;

public class CustomProgressBar {

	public static ProgressDialog createProgressDialog(Context mContext) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		try {
			dialog.show();
		} catch (WindowManager.BadTokenException e) {

		}
		dialog.setCancelable(true);
		Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog_progressbar);
		return dialog;
	}
}
