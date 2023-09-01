package com.softechurecabdriver.provider.ui.activity.subscription;

import com.softechurecabdriver.provider.base.MvpPresenter;

public interface SubscriptionPresenter<V extends SubscriptionView> extends MvpPresenter<V> {
    void getSubscriptions(String accessToken);
    void subscribeSuccessfully(String accessToken,String subscription_id);
}
