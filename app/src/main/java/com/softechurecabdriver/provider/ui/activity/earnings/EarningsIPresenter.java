package com.softechurecabdriver.provider.ui.activity.earnings;


import com.softechurecabdriver.provider.base.MvpPresenter;

public interface EarningsIPresenter<V extends EarningsIView> extends MvpPresenter<V> {

    void getEarnings();
}
