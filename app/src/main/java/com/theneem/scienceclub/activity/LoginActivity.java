package com.theneem.scienceclub.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.theneem.scienceclub.R;
import com.theneem.scienceclub.response.RegisterResponse;
import com.theneem.scienceclub.response.SocialLoginResponse;
import com.theneem.scienceclub.retrofit.ApiClient;
import com.theneem.scienceclub.retrofit.ApiInterface;
import com.theneem.scienceclub.utils.CustomProgressBar;
import com.theneem.scienceclub.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

	TextView txt_register, txt_forgotpass;
	MaterialButton btn_login;
	PreferenceManager preferenceManager;
	private LoginButton loginButton;
	private CallbackManager callbackManager;
	TextInputEditText edt_login_email, edt_login_password;
	private ProgressDialog progressDialog;
	private GoogleSignInClient mGoogleSignInClient;
	private static final String TAG = "SignInActivity";
	private static final int RC_SIGN_IN = 9001;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Objects.requireNonNull(this).setTheme(R.style.OPAppTheme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
		SignInButton signInButton = findViewById(R.id.sign_in_button);

		signInButton.setOnClickListener(v -> {
			signIn();
		});
		edt_login_email = findViewById(R.id.edt_login_email);
		edt_login_password = findViewById(R.id.edt_login_password);

		loginButton = findViewById(R.id.facebook_login);
		preferenceManager = new PreferenceManager(LoginActivity.this);
		txt_register = findViewById(R.id.txt_register);
		btn_login = findViewById(R.id.btn_login);
		txt_forgotpass = findViewById(R.id.txt_forgotpass);

		callbackManager = CallbackManager.Factory.create();
		loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

		checkLoginStatus();

		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {


			}

			@Override
			public void onCancel() {

			}

			@Override
			public void onError(FacebookException error) {
				Log.e("DBERROR", "" + error);
				Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
			}
		});

		txt_forgotpass.setOnClickListener(v -> {
			startActivity(new Intent(LoginActivity.this, ForgotPasswordAcivity.class));
		});

		txt_register.setOnClickListener(v -> {

			Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

		});

		btn_login.setOnClickListener(v -> {

			String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
			if (edt_login_email.getText().toString().isEmpty()) {
				Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
			} else if (!edt_login_email.getText().toString().matches(emailPattern)) {
				Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
			} else if (edt_login_password.getText().toString().isEmpty()) {
				Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
			} else if (edt_login_password.getText().toString().length() < 6) {
				Toast.makeText(this, "Please enter min 6 digit passoword", Toast.LENGTH_SHORT).show();
			} else {
				preferenceManager.setLoginSession(true);
				login(edt_login_email.getText().toString(), edt_login_password.getText().toString());
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			// The Task returned from this call is always completed, no need to attach
			// a listener.
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			handleSignInResult(task);
		}
	}

	AccessTokenTracker tokenTracker = new AccessTokenTracker() {
		@Override
		protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
			if (currentAccessToken == null) {
				/*txtName.setText("");
				txtEmail.setText("");
				circleImageView.setImageResource(0);
				Toast.makeText(MainActivity.this, "User Logged out", Toast.LENGTH_LONG).show();*/
			} else
				loadUserProfile(currentAccessToken);
		}
	};

	private void loadUserProfile(AccessToken newAccessToken) {
		GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
			@Override
			public void onCompleted(JSONObject object, GraphResponse response) {
				try {
					String first_name = object.getString("first_name");
					String last_name = object.getString("last_name");
					String email = object.getString("email");
					String id = object.getString("id");
					String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

					// FB
					SocialLogin("2", first_name + "" + last_name, email, id);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

		Bundle parameters = new Bundle();
		parameters.putString("fields", "first_name,last_name,email,id");
		request.setParameters(parameters);
		request.executeAsync();

	}

	private void checkLoginStatus() {
		if (AccessToken.getCurrentAccessToken() != null) {
			loadUserProfile(AccessToken.getCurrentAccessToken());
		}
	}


	public void login(String email, String password) {

		progressDialog = CustomProgressBar.createProgressDialog(LoginActivity.this);
		final ApiInterface api = ApiClient.getApiService();
		Call<RegisterResponse> call = api.getLogin(email, password);

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

							Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						} else {
							Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(LoginActivity.this, "response body() is null", Toast.LENGTH_SHORT).show();
					}

				} else if (response.code() == 401) {
					progressDialog.dismiss();
					// Handle unauthorized
				} else if (response.code() == 404) {
					progressDialog.dismiss();
					Toast.makeText(LoginActivity.this, "Not Found (From Serverside Error)", Toast.LENGTH_SHORT).show();
				} else if (response.code() == 500) {
					progressDialog.dismiss();
					Toast.makeText(LoginActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
				} else {
					progressDialog.dismiss();
					Toast.makeText(LoginActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<RegisterResponse> call, Throwable t) {
				progressDialog.dismiss();
				Toast.makeText(LoginActivity.this, "onFailer:  " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void signIn() {
		Intent signInIntent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
		try {
			GoogleSignInAccount account = completedTask.getResult(ApiException.class);

			// Signed in successfully, show authenticated UI.
			updateUI(account);
		} catch (ApiException e) {
			// The ApiException status code indicates the detailed failure reason.
			// Please refer to the GoogleSignInStatusCodes class reference for more information.
			Log.e(TAG, "signInResult:failed code=" + e.toString());
			updateUI(null);
		}
	}

	private void updateUI(@Nullable GoogleSignInAccount account) {
		if (account != null) {
			String personName = account.getDisplayName();
			String personGivenName = account.getGivenName();
			String personFamilyName = account.getFamilyName();
			String personEmail = account.getEmail();
			String personId = account.getId();
			Uri personPhoto = account.getPhotoUrl();


			// Gmail
			SocialLogin("1", personName, personEmail, personId);


		}
	}

	public void SocialLogin(String isSocial, String username, String email, String userId) {

		final ApiInterface api = ApiClient.getApiService();
		Call<SocialLoginResponse> call = api.getSocialLogin(isSocial, username, email, userId);

		call.enqueue(new Callback<SocialLoginResponse>() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onResponse(@NonNull Call<SocialLoginResponse> call, @NonNull Response<SocialLoginResponse> response) {
				if (response.code() == 200) {
					if (response.body() != null) {
						if (response.body().isSuccess()) {

							preferenceManager.setuserName(response.body().getSocialLogin().getUsername());
							preferenceManager.setemailAddress(response.body().getSocialLogin().getEmail());
							preferenceManager.setUserId(response.body().getSocialLogin().getUserId());
							preferenceManager.setLoginSession(true);

							Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						} else {
							Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(LoginActivity.this, "response body() is null", Toast.LENGTH_SHORT).show();
					}

				} else if (response.code() == 401) {
					// Handle unauthorized
				} else if (response.code() == 404) {
					Toast.makeText(LoginActivity.this, "Not Found (From Serverside Error)", Toast.LENGTH_SHORT).show();
				} else if (response.code() == 500) {
					Toast.makeText(LoginActivity.this, "Server Broken", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(LoginActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<SocialLoginResponse> call, Throwable t) {
				Toast.makeText(LoginActivity.this, "onFailer:  " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

	}
}
