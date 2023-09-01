package com.softechurecabdriver.provider.ui.fragment.upcoming;


import com.softechurecabdriver.provider.base.MvpPresenter;

public interface UpcomingTripIPresenter<V extends UpcomingTripIView> extends MvpPresenter<V> {

    void getUpcoming();

}
