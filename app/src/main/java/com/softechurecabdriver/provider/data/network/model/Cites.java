package com.softechurecabdriver.provider.data.network.model;

import java.util.ArrayList;
public class Cites {
    private String status;
    private ArrayList<RegisterCity> cities;

    public Cites(String status, ArrayList<RegisterCity> cities) {
        this.status = status;
        this.cities = cities;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<RegisterCity> getCities() {
        return cities;
    }

    public void setCities(ArrayList<RegisterCity> cities) {
        this.cities = cities;
    }
}
