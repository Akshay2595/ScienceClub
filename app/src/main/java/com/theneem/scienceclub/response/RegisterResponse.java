package com.theneem.scienceclub.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

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
	private Register register;

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

	public Register getRegister() {
		return register;
	}

	public void setRegister(Register register) {
		this.register = register;
	}

	public class Register {

		@SerializedName("id")
		@Expose
		private String id;

		@SerializedName("username")
		@Expose
		private String username;

		@SerializedName("email")
		@Expose
		private String email;



		@SerializedName("mobile")
		@Expose
		private String mobile;

		@SerializedName("club_id")
		@Expose
		private String clubid;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getClubid() { return clubid; 	}

		public void setClubid(String clubid) { this.clubid = clubid; }

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

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
	}
}
