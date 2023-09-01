package com.softechurecabdriver.provider.ui.activity.change_password;

import com.softechurecabdriver.provider.base.MvpView;

public interface ChangePasswordIView extends MvpView {


    void onSuccess(Object object);
    void onError(Throwable e);
}
