package com.softechurecabdriver.provider.ui.activity.email;

import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.ForgotResponse;
import com.softechurecabdriver.provider.data.network.model.User;

public interface EmailIView extends MvpView {
    void onSuccess(User object);
    void onSuccess(ForgotResponse forgotResponse);



    void onError(Throwable e);
}
