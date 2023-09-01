package com.softechurecabdriver.provider.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscriptions {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("no_of_days")
    @Expose
    private int no_of_days;

    @SerializedName("amount")
    @Expose
    private String amount;


    @SerializedName("is_subscribe")
    @Expose
    private boolean is_subscribe;


    public Subscriptions(String id, String name, int no_of_days, String amount, boolean is_subscribe) {
        this.id = id;
        this.name = name;
        this.no_of_days = no_of_days;
        this.amount = amount;
        this.is_subscribe = is_subscribe;
    }

    public Subscriptions() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getNo_of_days() {
        return no_of_days;
    }

    public void setNo_of_days(int no_of_days) {
        this.no_of_days = no_of_days;
    }

    public boolean isIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(boolean is_subscribe) {
        this.is_subscribe = is_subscribe;
    }
}
