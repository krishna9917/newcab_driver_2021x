package com.softechurecabdriver.provider.ui.activity.subscription;

import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.Subscriptions;
import com.softechurecabdriver.provider.data.network.model.SuccessResponse;

import java.util.List;

public interface SubscriptionView extends MvpView {
    void onSuccess(List<Subscriptions> subscriptions);

    void onError(Throwable e);

    void onPaySuccess(SuccessResponse successResponse);

}
