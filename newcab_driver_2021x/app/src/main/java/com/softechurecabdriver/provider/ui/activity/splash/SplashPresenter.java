package com.softechurecabdriver.provider.ui.activity.splash;

import android.os.Handler;

import com.softechurecabdriver.provider.base.BasePresenter;
import com.softechurecabdriver.provider.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashPresenter<V extends SplashIView> extends BasePresenter<V> implements SplashIPresenter<V> {

    @Override
    public void handlerCall() {
        new Handler().postDelayed(() -> getMvpView().verifyAppInstalled(), 5000);
    }

    @Override
    public void getPlaces() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getPlaces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void checkVersion(HashMap<String, Object> map) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .checkversion(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onCheckVersionError));
    }
    @Override
    public void changeLanguage(String languageID) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .postChangeLanguage(languageID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onLanguageChanged, getMvpView()::onError));
    }
}
