package com.theneem.scienceclub.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class checkTheSubscription {

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

        @SerializedName("club_id")
        @Expose
        private String clubId;

        public String getClubId() {
            return clubId;
        }

        public void setClubId(String clubId) {
            this.clubId = clubId;
        }
    }

}
