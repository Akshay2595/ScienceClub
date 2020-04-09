package com.theneem.scienceclub.model;

import java.io.Serializable;

public class Club implements Serializable {

	private String club_id;
	private String club_name;
	private int club_image;
	private String club_address;
	private String club_desc;
	private String club_member_number;
	private String club_sponser_name;
	private String club_contact_no;
	private String club_email_address;

	public Club(String club_id, String club_name, int club_image, String club_address, String club_desc, String club_member_number, String club_sponser_name, String club_contact_no, String club_email_address) {
		this.club_id = club_id;
		this.club_name = club_name;
		this.club_image = club_image;
		this.club_address = club_address;
		this.club_desc = club_desc;
		this.club_member_number = club_member_number;
		this.club_sponser_name = club_sponser_name;
		this.club_contact_no = club_contact_no;
		this.club_email_address = club_email_address;
	}

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

	public int getClub_image() {
		return club_image;
	}

	public void setClub_image(int club_image) {
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
}
