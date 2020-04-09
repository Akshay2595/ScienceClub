package com.theneem.scienceclub.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPassResponse {
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
	private ForgotPassword forgotpass;

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

	public ForgotPassword getForgotpass() {
		return forgotpass;
	}

	public void setForgotpass(ForgotPassword forgotpass) {
		this.forgotpass = forgotpass;
	}

	public class ForgotPassword {
		@SerializedName("otp")
		@Expose
		private String otp;

		public String getOtp() {
			return otp;
		}

		public void setOtp(String otp) {
			this.otp = otp;
		}
	}

}
