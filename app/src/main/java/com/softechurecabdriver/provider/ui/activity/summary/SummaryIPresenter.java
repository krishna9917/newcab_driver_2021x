package com.softechurecabdriver.provider.ui.activity.summary;


import com.softechurecabdriver.provider.base.MvpPresenter;

public interface SummaryIPresenter<V extends SummaryIView> extends MvpPresenter<V> {

    void getSummary(String data);
}
