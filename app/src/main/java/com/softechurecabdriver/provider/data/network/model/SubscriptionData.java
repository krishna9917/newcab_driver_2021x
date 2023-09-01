package com.softechurecabdriver.provider.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscriptionData {
    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("data")
    @Expose
    private List<Subscriptions> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Subscriptions> getData() {
        return data;
    }

    public void setData(List<Subscriptions> data) {
        this.data = data;
    }
}
