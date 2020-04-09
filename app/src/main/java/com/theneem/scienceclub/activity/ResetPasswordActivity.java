package com.theneem.scienceclub.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.theneem.scienceclub.R;
import com.theneem.scienceclub.response.RegisterResponse;
import com.theneem.scienceclub.retrofit.ApiClient;
import com.theneem.scienceclub.retrofit.ApiInterface;
import com.theneem.scienceclub.utils.CustomProgressBar;
import com.theneem.scienceclub.utils.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

	MaterialButton btn_submit;
	PreferenceManager preferenceManager;
	private ProgressDialog progressDialog;
	TextInputEditText edt_new_pass, edt_conf_pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ResetPasswordActivity.this.setTheme(R.style.OPAppTheme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_reset_password);
		btn_submit = findViewById(R.id.btn_submit);
		edt_new_pass = findViewById(R.id.edt_new_pass);
		edt_conf_pass = findViewById(R.id.edt_conf_pass);
		preferenceManager = new PreferenceManager(ResetPasswordActivity.this);
		String email = getIntent().getExtras().getString("email", "");
		btn_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (edt_new_pass.getText().toString().isEmpty()) {
					Toast.makeText(ResetPasswordActivity.this, "Please enter new Password", Toast.LENGTH_SHORT).show();
				} else if (edt_new_pass.getText().toString().length() < 6) {
					Toast.makeText(ResetPasswordActivity.this, "Please enter min 6 char in new Password", Toast.LENGTH_SHORT).show();
				} else if (edt_conf_pass.getText().toString().isEmpty()) {
					Toast.makeText(ResetPasswordActivity.this, "Please enter Confirm Password", Toast.LENGTH_SHORT).show();
				} else if (!edt_new_pass.getText().toString().equalsIgnoreCase(edt_conf_pass.getText().toString())) {
					Toast.makeText(ResetPasswordActivity.this, "Password Not Match !!", Toast.LENGTH_SHORT).show();
				} else {
					preferenceManager.setLoginSession(true);
					resetPass(email, edt_new_pass.getText().toString());
				}

			}
		});
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, "Please reset your Password first", Toast.LENGTH_SHORT).show();
	}

	private void resetPass(String email, String password) {
		progressDialog = CustomProgressBar.createProgressDialog(ResetPasswordActivity.this);
		final ApiInterface api = ApiClient.getApiService();
		Call<RegisterResponse> call = api.getResetPassword(email, password);

		call.enqueue(new Callback<RegisterResponse>() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
				if (response.code() == 200) {
					if (response.body() != null) {
						progressDialog.dismiss();

						if (response.body().isSuccess()) {
							preferenceManager.setUserId(response.body().getRegister().getId());
							preferenceManager.setuserName(response.body().getRegister().getUsername());
							preferenceManager.setemailAddress(response.body().getRegister().getEmail());
							preferenceManager.setMobileNum(response.body().getRegister().getMobile());
							preferenceManager.setClubId(response.body().getRegister().getClubid());

							Toast.makeText(ResetPasswordActivity.this, "Reset Password Sucessful.", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						} else {
							Toast.makeText(ResetPasswordActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
						}

					} else {
						Toast.makeText(ResetPasswordActivity.this, "response body() is null", Toast.LENGTH_SHORT).show();
					}

				} else if (response.code() == 401) {
					progressDialog.dismiss();
					// Handle unauthorized
				} else if (response.code() == 404) {
					progressDialog.dismiss();
					Toast.makeText(ResetPasswordActivity.this, "Not Found (From Serverside Error)", Toast.LENGTH_SHORT).show();
				} else if (response.code() == 500) {
					progressDialog.dismiss();
					Toast.makeText(ResetPasswordActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
				} else {
					progressDialog.dismiss();
					Toast.makeText(ResetPasswordActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<RegisterResponse> call, Throwable t) {
				progressDialog.dismiss();
				Toast.makeText(ResetPasswordActivity.this, "onFailer:  " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
