package com.softechurecabdriver.provider.ui.activity.upcoming_detail;


import com.softechurecabdriver.provider.base.MvpPresenter;

public interface UpcomingTripDetailIPresenter<V extends UpcomingTripDetailIView> extends MvpPresenter<V> {

    void getUpcomingDetail(String request_id);

}
