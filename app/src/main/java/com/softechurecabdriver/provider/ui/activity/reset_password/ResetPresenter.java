package com.softechurecabdriver.provider.ui.activity.reset_password;

import com.softechurecabdriver.provider.base.BasePresenter;
import com.softechurecabdriver.provider.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ResetPresenter<V extends ResetIView> extends BasePresenter<V> implements ResetIPresenter<V> {

    @Override
    public void reset(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .resetPassword(obj)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }
}
