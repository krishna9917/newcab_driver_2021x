package com.softechurecabdriver.provider.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("provider")
    @Expose
    private Provider provider;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id ) {
        this.id = id;
    }
}
