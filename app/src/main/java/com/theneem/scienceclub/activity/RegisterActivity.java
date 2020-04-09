package com.theneem.scienceclub.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
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

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

	TextView txt_login;
	MaterialButton btn_register;
	PreferenceManager preferenceManager;
	TextInputEditText edt_userName, edt_email, edt_password, edt_mobile;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Objects.requireNonNull(this).setTheme(R.style.OPAppTheme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_register);
		btn_register = findViewById(R.id.btn_register);
		preferenceManager = new PreferenceManager(RegisterActivity.this);

		edt_userName = findViewById(R.id.edt_userName);
		edt_email = findViewById(R.id.edt_email);
		edt_password = findViewById(R.id.edt_password);
		edt_mobile = findViewById(R.id.edt_mobile);

		btn_register.setOnClickListener(v -> {

			String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

			if (edt_userName.getText().toString().isEmpty()) {
				Toast.makeText(this, "Please add UserName", Toast.LENGTH_SHORT).show();
			} else if (edt_email.getText().toString().isEmpty()) {
				Toast.makeText(this, "Please add Email Id", Toast.LENGTH_SHORT).show();
			} else if (!edt_email.getText().toString().matches(emailPattern)) {
				Toast.makeText(this, "Please enter valid email Id", Toast.LENGTH_SHORT).show();
			} else if (edt_password.getText().toString().isEmpty()) {
				Toast.makeText(this, "Please add Password ", Toast.LENGTH_SHORT).show();
			} else if (edt_password.getText().toString().length() < 6) {
				Toast.makeText(this, "Please enter min 6 digit passoword ", Toast.LENGTH_SHORT).show();
			} else if (edt_mobile.getText().toString().isEmpty()) {
				Toast.makeText(this, "Please add Mobile Number", Toast.LENGTH_SHORT).show();
			} else if (edt_mobile.getText().toString().length() != 10) {
				Toast.makeText(this, "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
			} else {
				preferenceManager.setLoginSession(true);
				retrofit(edt_userName.getText().toString(), edt_email.getText().toString(), edt_password.getText().toString(), edt_mobile.getText().toString());
			}
		});

		txt_login = findViewById(R.id.txt_login);

		txt_login.setOnClickListener(v -> {

			Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		});
	}

	private void retrofit(String userName, String email, String password, String mobile) {
		progressDialog = CustomProgressBar.createProgressDialog(RegisterActivity.this);
		final ApiInterface api = ApiClient.getApiService();
		Call<RegisterResponse> call = api.getRegister(userName, email, password, mobile);

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

							Toast.makeText(RegisterActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						} else {
							Toast.makeText(RegisterActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
						}

					} else {
						Toast.makeText(RegisterActivity.this, "response body() is null", Toast.LENGTH_SHORT).show();
					}

				} else if (response.code() == 401) {
					progressDialog.dismiss();
					// Handle unauthorized
				} else if (response.code() == 404) {
					progressDialog.dismiss();
					Toast.makeText(RegisterActivity.this, "Not Found (From Serverside Error)", Toast.LENGTH_SHORT).show();
				} else if (response.code() == 500) {
					progressDialog.dismiss();
					Toast.makeText(RegisterActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
				} else {
					progressDialog.dismiss();
					Toast.makeText(RegisterActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<RegisterResponse> call, Throwable t) {
				progressDialog.dismiss();
				Toast.makeText(RegisterActivity.this, "onFailer:  " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

	}
}
