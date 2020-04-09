package com.theneem.scienceclub.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.theneem.scienceclub.R;
import com.theneem.scienceclub.fragments.MemberFragment;
import com.theneem.scienceclub.fragments.ScienceClubsFragment;
import com.theneem.scienceclub.fragments.VideoLogsFragment;
import com.theneem.scienceclub.utils.PreferenceManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

	public DrawerLayout drawer;
	NavigationView navigationView;
	BottomNavigationView navigation;
	PreferenceManager preferenceManager;
	private GoogleSignInClient mGoogleSignInClient;

	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

		navigationView = findViewById(R.id.nav_view);
		drawer = findViewById(R.id.drawer_layout);
		preferenceManager = new PreferenceManager(MainActivity.this);
		View opHeader = navigationView.getHeaderView(0);
		LinearLayout opLinearLayoutHeader = opHeader.findViewById(R.id.header_ll_user);
		TextView opName = opLinearLayoutHeader.findViewById(R.id.header_user_name);
		TextView opEmail = opLinearLayoutHeader.findViewById(R.id.header_user_email);

		if (preferenceManager.getLoginSession()) {
			opEmail.setVisibility(View.VISIBLE);
			opName.setText(preferenceManager.getuserName());
			opEmail.setText(preferenceManager.getemailAddress());

		}

		opName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!preferenceManager.getLoginSession()) {
					Intent intent = new Intent(MainActivity.this, LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				} else {
					opEmail.setVisibility(View.VISIBLE);
					opName.setText(preferenceManager.getuserName());
					opEmail.setText(preferenceManager.getemailAddress());
				}
			}
		});

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, 0, 0);
		drawer.addDrawerListener(toggle);
		toggle.syncState();
		navigationView.setNavigationItemSelectedListener(this);

		navigation = findViewById(R.id.bottom_navigation);
		navigation.setOnNavigationItemSelectedListener(this);

		loadFragment(new ScienceClubsFragment());

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if (preferenceManager.getLoginSession()) {
			navigationView.inflateMenu(R.menu.nav_drawer_menu_withlogout);
		} else {
			navigationView.inflateMenu(R.menu.nav_drawer_menu);
		}

		return true;
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		Fragment fragment = null;
		int id = menuItem.getItemId();

		if (id == R.id.bottom_club) {
			fragment = new ScienceClubsFragment();

		} else if (id == R.id.bottom_video_logs) {
			if (preferenceManager.getLoginSession()) {
				fragment = new VideoLogsFragment();
			} else {
				goLogin();
			}


		} else if (id == R.id.bottom_member) {
			if (preferenceManager.getLoginSession()) {
				fragment = new MemberFragment();
			} else {
				goLogin();
			}
		} else if (id == R.id.video_logs) {

			if (preferenceManager.getLoginSession()) {
				navigation.setSelectedItemId(R.id.bottom_video_logs);
				fragment = new VideoLogsFragment();
				drawer.closeDrawers();
			} else {
				goLogin();
			}


		} else if (id == R.id.science_club) {
			navigation.setSelectedItemId(R.id.bottom_club);
			drawer.closeDrawers();
			fragment = new ScienceClubsFragment();

		} else if (id == R.id.members) {
			if (preferenceManager.getLoginSession()) {
				navigation.setSelectedItemId(R.id.bottom_member);
				fragment = new MemberFragment();
				drawer.closeDrawers();
			} else {
				goLogin();
			}
		} else if (id == R.id.how_work) {
			startActivity(new Intent(MainActivity.this, HowItWorksActivity.class));
		} else if (id == R.id.about_club) {
			startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
		} else if (id == R.id.logout) {

			preferenceManager.setLoginSession(false);
			preferenceManager.clearPreferences();

			// Facebook Logout
			LoginManager.getInstance().logOut();

			// Google
			signOut();


			Intent intent = new Intent(MainActivity.this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

		}

		loadFragment(fragment);


		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!preferenceManager.getLoginSession()) {
			navigation.setSelectedItemId(R.id.bottom_club);
		}
	}

	private void loadFragment(Fragment fragment) {
		//switching fragment
		if (fragment != null) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container, fragment)
					.commit();
		}
	}

	public void setActionBarTitle(String title) {
		Objects.requireNonNull(getSupportActionBar()).setTitle(title);
	}

	public void goLogin() {
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void disconnectFromFacebook() {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if (accessToken != null) {

			LoginManager.getInstance().logOut();
			GraphRequest delPermRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/{user-id}/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
				@Override
				public void onCompleted(GraphResponse graphResponse) {
					if (graphResponse != null) {
						FacebookRequestError error = graphResponse.getError();
						if (error != null) {

						} else {
							Intent intent = new Intent(MainActivity.this, MainActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();
						}
					}
				}
			});
			delPermRequest.executeAsync();
		}


	}

	private void signOut() {
		mGoogleSignInClient.signOut()
				.addOnCompleteListener(this, new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						// [START_EXCLUDE]

						// [END_EXCLUDE]
					}
				});
	}


}
