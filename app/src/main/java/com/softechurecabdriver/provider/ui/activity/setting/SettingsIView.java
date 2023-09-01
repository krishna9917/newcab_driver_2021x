package com.softechurecabdriver.provider.ui.activity.setting;

import com.softechurecabdriver.provider.base.MvpView;

public interface SettingsIView extends MvpView {

    void onSuccess(Object o);

    void onError(Throwable e);

}
