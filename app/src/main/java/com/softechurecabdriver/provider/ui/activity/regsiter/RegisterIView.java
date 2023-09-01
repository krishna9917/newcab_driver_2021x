package com.softechurecabdriver.provider.ui.activity.regsiter;

import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.Cites;
import com.softechurecabdriver.provider.data.network.model.SettingsResponse;
import com.softechurecabdriver.provider.data.network.model.User;

import java.util.ArrayList;

public interface RegisterIView extends MvpView {

    void onSuccess(User user);

    void onSuccess(Object verifyEmail);

    void onSuccess(SettingsResponse response);

    void onError(Throwable e);

    void onSuccessPhoneNumber(Object object);

    void onVerifyPhoneNumberError(Throwable e);

    void onVerifyEmailError(Throwable e);

    void onSuccessCityList(Cites cites);
}
