package com.softechurecabdriver.provider.ui.fragment.status_flow;

import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.TimerResponse;
import com.softechurecabdriver.provider.data.network.model.TripResponse;

public interface StatusFlowIView extends MvpView {

    void onSuccess(Object object);

    void onWaitingTimeSuccess(TimerResponse object);
    void onSuccess(TripResponse tripResponse);
    void onError(Throwable e);
}
