package com.softechurecabdriver.provider.ui.activity.email;

import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.ForgotResponse;
import com.softechurecabdriver.provider.data.network.model.LoginUser;

public interface EmailIView extends MvpView {
    void onSuccess(LoginUser loginUser);
    void onSuccess(ForgotResponse forgotResponse);
    void onError(Throwable e);
}
