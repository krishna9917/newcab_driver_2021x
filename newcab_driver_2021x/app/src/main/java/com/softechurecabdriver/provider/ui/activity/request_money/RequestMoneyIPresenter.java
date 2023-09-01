package com.softechurecabdriver.provider.ui.activity.request_money;

import com.softechurecabdriver.provider.base.MvpPresenter;

public interface RequestMoneyIPresenter<V extends RequestMoneyIView> extends MvpPresenter<V> {

    void getRequestedData();
    void requestMoney(Double requestedAmt);
    void removeRequestMoney(int id);

}
