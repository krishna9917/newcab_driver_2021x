package com.softechurecabdriver.provider.ui.fragment.status_flow;

import com.softechurecabdriver.provider.base.MvpPresenter;

import java.util.HashMap;

public interface StatusFlowIPresenter<V extends StatusFlowIView> extends MvpPresenter<V> {

    void statusUpdate(HashMap<String, Object> obj, Integer id);

    void waitingTime(String time, String requestId);

    void getTrip(HashMap<String, Object> params);

    void checkWaitingTime(String requestId);
}
