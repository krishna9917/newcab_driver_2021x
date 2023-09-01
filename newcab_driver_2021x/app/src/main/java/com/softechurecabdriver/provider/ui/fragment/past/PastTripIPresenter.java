package com.softechurecabdriver.provider.ui.fragment.past;


import com.softechurecabdriver.provider.base.MvpPresenter;

public interface PastTripIPresenter<V extends PastTripIView> extends MvpPresenter<V> {

    void getHistory();

}
