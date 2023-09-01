package com.softechurecabdriver.provider.ui.activity.forgot_password;

import com.softechurecabdriver.provider.base.MvpPresenter;

import java.util.HashMap;

public interface ForgotIPresenter<V extends ForgotIView> extends MvpPresenter<V> {

    void forgot(HashMap<String, Object> obj);

}
