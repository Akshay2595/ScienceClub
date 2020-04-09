package com.theneem.scienceclub.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

	private static PreferenceManager preferenceManager;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;

	public PreferenceManager(Context context) {
		preferenceManager = this;
		mSharedPreferences = context.getSharedPreferences(VariableBag.PREF_NAME, Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public static PreferenceManager getInstance() {
		return preferenceManager;
	}

	public void clearPreferences() {
		mEditor.clear();
		mEditor.commit();
	}

	public void removePref(Context context, String keyToRemove) {
		mSharedPreferences = context.getSharedPreferences(VariableBag.PREF_NAME, Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		mEditor.remove(keyToRemove);
		mEditor.commit();
	}


	/* LOGIN SESSION*/

	public boolean getLoginSession() {
		boolean login = mSharedPreferences.getBoolean(VariableBag.isLogin, false);
		return login;
	}

	public void setLoginSession(boolean loginStatus) {
		mEditor.putBoolean(VariableBag.isLogin, loginStatus);
		mEditor.commit();
	}

	/* FB LOGIN*/

	public boolean getFbLogin() {
		boolean login = mSharedPreferences.getBoolean(VariableBag.fromFbLogin, false);
		return login;
	}

	public void setFbLogin(boolean loginStatus) {
		mEditor.putBoolean(VariableBag.fromFbLogin, loginStatus);
		mEditor.commit();
	}

	/* UserID*/

	public String getUserId() {
		return mSharedPreferences.getString(VariableBag.userId, "");
	}

	public void setUserId(String userId) {
		mEditor.putString(VariableBag.userId, userId).commit();
	}

	/* userName*/

	public String getuserName() {
		return mSharedPreferences.getString(VariableBag.userName, "");
	}

	public void setuserName(String userName) {
		mEditor.putString(VariableBag.userName, userName).commit();
	}

	/* emailAddress*/

	public String getemailAddress() {
		return mSharedPreferences.getString(VariableBag.emailAddress, "");
	}

	public void setemailAddress(String emailAddress) {
		mEditor.putString(VariableBag.emailAddress, emailAddress).commit();
	}

	/* MobileNum*/

	public String getMobileNum() {
		return mSharedPreferences.getString(VariableBag.MobileNum, "");
	}

	public void setMobileNum(String MobileNum) {
		mEditor.putString(VariableBag.MobileNum, MobileNum).commit();
	}

	/* Club ID*/

	public String getClubid() { return mSharedPreferences.getString(VariableBag.clubId,"");}

	public void setClubId(String ClubId) {
		mEditor.putString(VariableBag.clubId,ClubId).commit();
	}

}
