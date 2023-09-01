package com.softechurecabdriver.provider.ui.activity.reset_password;

import com.softechurecabdriver.provider.base.MvpView;

public interface ResetIView extends MvpView{

    void onSuccess(Object object);
    void onError(Throwable e);
}
