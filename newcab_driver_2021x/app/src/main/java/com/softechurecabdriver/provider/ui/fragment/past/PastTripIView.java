package com.softechurecabdriver.provider.ui.fragment.past;


import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.HistoryList;

import java.util.List;

public interface PastTripIView extends MvpView {

    void onSuccess(List<HistoryList> historyList);
    void onError(Throwable e);
}
