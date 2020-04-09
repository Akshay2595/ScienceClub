package com.theneem.scienceclub.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClubListResponse {

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
	private ArrayList<ClubList> clublist = null;

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

	public ArrayList<ClubList> getClublist() {
		return clublist;
	}

	public void setClublist(ArrayList<ClubList> clublist) {
		this.clublist = clublist;
	}

	public class ClubList {

		@SerializedName("club_id")
		@Expose
		private String club_id;

		@SerializedName("club_name")
		@Expose
		private String club_name;

		@SerializedName("club_image")
		@Expose
		private String club_image;

		@SerializedName("club_address")
		@Expose
		private String club_address;

		@SerializedName("club_desc")
		@Expose
		private String club_desc;

		@SerializedName("club_member_number")
		@Expose
		private String club_member_number;

		@SerializedName("club_sponser_name")
		@Expose
		private String club_sponser_name;

		@SerializedName("club_contact_no")
		@Expose
		private String club_contact_no;

		@SerializedName("club_email_address")
		@Expose
		private String club_email_address;

		@SerializedName("channel_id")
		@Expose
		private String channel_id;

		public String getClub_id() {
			return club_id;
		}

		public void setClub_id(String club_id) {
			this.club_id = club_id;
		}

		public String getClub_name() {
			return club_name;
		}

		public void setClub_name(String club_name) {
			this.club_name = club_name;
		}

		public String getClub_image() {
			return club_image;
		}

		public void setClub_image(String club_image) {
			this.club_image = club_image;
		}

		public String getClub_address() {
			return club_address;
		}

		public void setClub_address(String club_address) {
			this.club_address = club_address;
		}

		public String getClub_desc() {
			return club_desc;
		}

		public void setClub_desc(String club_desc) {
			this.club_desc = club_desc;
		}

		public String getClub_member_number() {
			return club_member_number;
		}

		public void setClub_member_number(String club_member_number) {
			this.club_member_number = club_member_number;
		}

		public String getClub_sponser_name() {
			return club_sponser_name;
		}

		public void setClub_sponser_name(String club_sponser_name) {
			this.club_sponser_name = club_sponser_name;
		}

		public String getClub_contact_no() {
			return club_contact_no;
		}

		public void setClub_contact_no(String club_contact_no) {
			this.club_contact_no = club_contact_no;
		}

		public String getClub_email_address() {
			return club_email_address;
		}

		public void setClub_email_address(String club_email_address) {
			this.club_email_address = club_email_address;
		}

		public String getChannel_id() {
			return channel_id;
		}

		public void setChannel_id(String channel_id) {
			this.channel_id = channel_id;
		}
	}

}
