package com.softechurecabdriver.provider.ui.activity.email;

import com.softechurecabdriver.provider.base.BasePresenter;
import com.softechurecabdriver.provider.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EmailPresenter<V extends EmailIView> extends BasePresenter<V> implements EmailIPresenter<V> {

    @Override
    public void login(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .login(obj)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void forgot(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .forgotPassword(obj)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

}