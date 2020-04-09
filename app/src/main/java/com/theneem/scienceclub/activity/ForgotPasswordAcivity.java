package com.theneem.scienceclub.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.theneem.scienceclub.R;
import com.theneem.scienceclub.response.ForgotPassResponse;
import com.theneem.scienceclub.retrofit.ApiClient;
import com.theneem.scienceclub.retrofit.ApiInterface;
import com.theneem.scienceclub.utils.CustomProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordAcivity extends AppCompatActivity {

	MaterialButton btn_generateOTP;
	TextInputEditText edt_forgot_email;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ForgotPasswordAcivity.this.setTheme(R.style.OPAppTheme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_forgot_password_acivity);

		edt_forgot_email = findViewById(R.id.edt_forgot_email);

		btn_generateOTP = findViewById(R.id.btn_generateOTP);

		btn_generateOTP.setOnClickListener(v -> {

			String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

			if (edt_forgot_email.getText().toString().isEmpty()) {
				Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
			} else if (!edt_forgot_email.getText().toString().matches(emailPattern)) {
				Toast.makeText(this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
			} else {
				forgotPass(edt_forgot_email.getText().toString());
			}

		});
	}

	public void forgotPass(String email) {

		progressDialog = CustomProgressBar.createProgressDialog(ForgotPasswordAcivity.this);
		final ApiInterface api = ApiClient.getApiService();
		Call<ForgotPassResponse> call = api.getForgotPassword(email);

		call.enqueue(new Callback<ForgotPassResponse>() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onResponse(@NonNull Call<ForgotPassResponse> call, @NonNull Response<ForgotPassResponse> response) {
				if (response.code() == 200) {
					if (response.body() != null) {
						progressDialog.dismiss();
						if (response.body().isSuccess()){
							Toast.makeText(ForgotPasswordAcivity.this, "" + response.body().getForgotpass().getOtp(), Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(ForgotPasswordAcivity.this, EnterOTPActivity.class);
							intent.putExtra("otp", response.body().getForgotpass().getOtp());
							intent.putExtra("email", email);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}else {
							Toast.makeText(ForgotPasswordAcivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
						}


					} else {
						Toast.makeText(ForgotPasswordAcivity.this, "response body() is null", Toast.LENGTH_SHORT).show();
					}

				} else if (response.code() == 401) {
					progressDialog.dismiss();
					// Handle unauthorized
				} else if (response.code() == 404) {
					progressDialog.dismiss();
					Toast.makeText(ForgotPasswordAcivity.this, "Not Found (From Serverside Error)", Toast.LENGTH_SHORT).show();
				} else if (response.code() == 500) {
					progressDialog.dismiss();
					Toast.makeText(ForgotPasswordAcivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
				} else {
					progressDialog.dismiss();
					Toast.makeText(ForgotPasswordAcivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<ForgotPassResponse> call, Throwable t) {
				progressDialog.dismiss();
				Toast.makeText(ForgotPasswordAcivity.this, "onFailer:  " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

	}

}
