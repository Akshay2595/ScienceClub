package com.theneem.scienceclub.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.theneem.scienceclub.R;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

public class EnterOTPActivity extends AppCompatActivity {

	MaterialButton btn_verify;
	private OtpView otpView;
	String enteredOTP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EnterOTPActivity.this.setTheme(R.style.OPAppTheme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_enter_otp);
		otpView = findViewById(R.id.otp_view);
		String otp = getIntent().getExtras().getString("otp", "");
		String email = getIntent().getExtras().getString("email", "");
		btn_verify = findViewById(R.id.btn_verify);

		otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
			@Override
			public void onOtpCompleted(String otp) {
				enteredOTP = otp;
			}
		});

		btn_verify.setOnClickListener(v -> {
			if (enteredOTP.equalsIgnoreCase(otp)) {
				Intent intent = new Intent(EnterOTPActivity.this, ResetPasswordActivity.class);
				intent.putExtra("email", email);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			} else {
				Toast.makeText(this, "Please Enter Valid OTP.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
	}

}
