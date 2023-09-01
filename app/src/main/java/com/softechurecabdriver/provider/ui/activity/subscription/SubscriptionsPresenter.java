package com.softechurecabdriver.provider.ui.activity.subscription;

import com.softechurecabdriver.provider.base.BasePresenter;
import com.softechurecabdriver.provider.data.network.APIClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SubscriptionsPresenter<V extends SubscriptionView> extends BasePresenter<V> implements SubscriptionPresenter<V> {

    @Override
    public void getSubscriptions(String accessToken) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getSubscriptions(accessToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }


    @Override
    public void subscribeSuccessfully(String accessToken,String subscription_id) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .paymentSuccess(accessToken,subscription_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onPaySuccess, getMvpView()::onError));
    }


}
