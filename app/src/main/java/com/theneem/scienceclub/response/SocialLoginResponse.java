package com.theneem.scienceclub.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialLoginResponse {

	@SerializedName("statusCode")
	@Expose
	private int statusCode;

	@SerializedName("success")
	@Expose
	private boolean success;

	@SerializedName("message")
	@Expose
	private String message;

	@SerializedName("data")
	@Expose
	private SocialLogin socialLogin;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public SocialLogin getSocialLogin() {
		return socialLogin;
	}

	public void setSocialLogin(SocialLogin socialLogin) {
		this.socialLogin = socialLogin;
	}

	public class SocialLogin {

		@SerializedName("id")
		@Expose
		private String id;

		@SerializedName("userName")
		@Expose
		private String username;

		@SerializedName("email")
		@Expose
		private String email;

		@SerializedName("userId")
		@Expose
		private String userId;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}
	}
}
