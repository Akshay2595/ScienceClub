package com.theneem.scienceclub.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoiningClubResponse {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("isGmail")
        @Expose
        private Object isGmail;
        @SerializedName("gmail_email")
        @Expose
        private Object gmailEmail;
        @SerializedName("gmail_username")
        @Expose
        private Object gmailUsername;
        @SerializedName("gmail_id")
        @Expose
        private Object gmailId;
        @SerializedName("isFb")
        @Expose
        private Object isFb;
        @SerializedName("fb_email")
        @Expose
        private Object fbEmail;
        @SerializedName("fb_username")
        @Expose
        private Object fbUsername;
        @SerializedName("fb_id")
        @Expose
        private Object fbId;
        @SerializedName("club_id")
        @Expose
        private String clubId;

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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Object getIsGmail() {
            return isGmail;
        }

        public void setIsGmail(Object isGmail) {
            this.isGmail = isGmail;
        }

        public Object getGmailEmail() {
            return gmailEmail;
        }

        public void setGmailEmail(Object gmailEmail) {
            this.gmailEmail = gmailEmail;
        }

        public Object getGmailUsername() {
            return gmailUsername;
        }

        public void setGmailUsername(Object gmailUsername) {
            this.gmailUsername = gmailUsername;
        }

        public Object getGmailId() {
            return gmailId;
        }

        public void setGmailId(Object gmailId) {
            this.gmailId = gmailId;
        }

        public Object getIsFb() {
            return isFb;
        }

        public void setIsFb(Object isFb) {
            this.isFb = isFb;
        }

        public Object getFbEmail() {
            return fbEmail;
        }

        public void setFbEmail(Object fbEmail) {
            this.fbEmail = fbEmail;
        }

        public Object getFbUsername() {
            return fbUsername;
        }

        public void setFbUsername(Object fbUsername) {
            this.fbUsername = fbUsername;
        }

        public Object getFbId() {
            return fbId;
        }

        public void setFbId(Object fbId) {
            this.fbId = fbId;
        }

        public String getClubId() {
            return clubId;
        }

        public void setClubId(String clubId) {
            this.clubId = clubId;
        }

    }
}
