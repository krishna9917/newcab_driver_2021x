package com.softechurecabdriver.provider.ui.activity.past_detail;


import com.softechurecabdriver.provider.base.MvpPresenter;

public interface PastTripDetailIPresenter<V extends PastTripDetailIView> extends MvpPresenter<V> {

    void getPastTripDetail(String request_id);
}
