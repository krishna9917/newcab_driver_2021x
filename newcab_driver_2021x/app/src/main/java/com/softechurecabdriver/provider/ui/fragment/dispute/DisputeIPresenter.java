package com.softechurecabdriver.provider.ui.fragment.dispute;


import com.softechurecabdriver.provider.base.MvpPresenter;

import java.util.HashMap;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface DisputeIPresenter<V extends DisputeIView> extends MvpPresenter<V> {
    void dispute(HashMap<String, Object> obj);
    void getDispute();
}
