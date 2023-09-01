package com.softechurecabdriver.provider.ui.activity.request_money;

import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.RequestDataResponse;

public interface RequestMoneyIView extends MvpView {

    void onSuccess(RequestDataResponse response);
    void onSuccess(Object response);
    void onError(Throwable e);

}
