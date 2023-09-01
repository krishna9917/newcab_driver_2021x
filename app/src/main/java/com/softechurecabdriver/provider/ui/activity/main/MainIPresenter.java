package com.softechurecabdriver.provider.ui.activity.main;

import com.softechurecabdriver.provider.base.MvpPresenter;

import java.util.HashMap;

public interface MainIPresenter<V extends MainIView> extends MvpPresenter<V> {

    void getProfile();

    void getDirectionResult(String slat, String slang, String dlat, String dlang);

    void logout(HashMap<String, Object> obj);

    void getTrip(HashMap<String, Object> params);

    void providerAvailable(HashMap<String, Object> obj);

//    void sendFCM(JsonObject jsonObject);

    void getTripLocationUpdate(HashMap<String, Object> params);

    void getSettings();


}
