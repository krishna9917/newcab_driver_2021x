package com.softechurecabdriver.provider.ui.activity.reset_password;

import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.UpdatePassword;

public interface ResetIView extends MvpView{

    void onSuccess(UpdatePassword object);
    void onError(Throwable e);
}
