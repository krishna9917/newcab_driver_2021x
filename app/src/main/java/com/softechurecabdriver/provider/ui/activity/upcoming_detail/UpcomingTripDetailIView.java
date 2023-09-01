package com.softechurecabdriver.provider.ui.activity.upcoming_detail;


import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.HistoryDetail;

public interface UpcomingTripDetailIView extends MvpView {

    void onSuccess(HistoryDetail historyDetail);
    void onError(Throwable e);
}
