package com.softechurecabdriver.provider.ui.activity.splash;

import com.softechurecabdriver.provider.base.MvpPresenter;

import java.util.HashMap;

public interface SplashIPresenter<V extends SplashIView> extends MvpPresenter<V> {

    void handlerCall();

    void getPlaces();

    void checkVersion(HashMap<String, Object> map);

    void changeLanguage(String languageID);
}
