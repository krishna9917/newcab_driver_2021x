package com.softechurecabdriver.provider.ui.activity.splash;

import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.CheckVersion;

public interface SplashIView extends MvpView {

    void verifyAppInstalled();

    void onSuccess(Object user);

    void onSuccess(CheckVersion user);

    void onError(Throwable e);

    void onLanguageChanged(Object object);
    void onCheckVersionError(Throwable e);
}
