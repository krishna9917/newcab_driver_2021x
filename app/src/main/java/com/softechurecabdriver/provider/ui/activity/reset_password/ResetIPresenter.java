package com.softechurecabdriver.provider.ui.activity.reset_password;

import com.softechurecabdriver.provider.base.MvpPresenter;

import java.util.HashMap;

public interface ResetIPresenter<V extends ResetIView> extends MvpPresenter<V> {

    void reset(HashMap<String, Object> obj);

}
