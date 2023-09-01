package com.softechurecabdriver.provider.ui.activity.past_detail;


import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.HistoryDetail;

public interface PastTripDetailIView extends MvpView {

    void onSuccess(HistoryDetail historyDetail);
    void onError(Throwable e);
}
