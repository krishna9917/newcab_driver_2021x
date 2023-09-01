package com.softechurecabdriver.provider.ui.activity.password;

import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.ForgotResponse;
import com.softechurecabdriver.provider.data.network.model.LoginUser;

public interface PasswordIView extends MvpView {

    void onSuccess(ForgotResponse forgotResponse);

    void onSuccess(LoginUser loginUser);

    void onError(Throwable e);
}
