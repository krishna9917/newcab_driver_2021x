
package com.softechurecabdriver.provider.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SettingsResponse {

    @SerializedName("serviceTypes")
    @Expose
    private List<ServiceType> serviceTypes = null;
    @SerializedName("referral")
    @Expose
    private Referral referral;



    @SerializedName("app_share_link")
    @Expose
    private String appShareLink;

    public List<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(List<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public Referral getReferral() {
        return referral;
    }

    public void setReferral(Referral referral) {
        this.referral = referral;
    }

    public String getAppShareLink() {
        return appShareLink;
    }

    public void setAppShareLink(String appShareLink) {
        this.appShareLink = appShareLink;
    }
}
