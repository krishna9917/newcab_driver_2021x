package com.softechurecabdriver.provider.ui.fragment.offline;

import com.softechurecabdriver.provider.base.BasePresenter;
import com.softechurecabdriver.provider.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OfflinePresenter<V extends OfflineIView> extends BasePresenter<V> implements OfflineIPresenter<V> {

    @Override
    public void providerAvailable(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .providerAvailable(obj)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }
}
