package com.softechurecabdriver.provider.ui.activity.main;

import com.akexorcist.googledirection.model.Direction;
import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.SettingsResponse;
import com.softechurecabdriver.provider.data.network.model.TripResponse;
import com.softechurecabdriver.provider.data.network.model.UserResponse;

public interface MainIView extends MvpView {
    void onSuccess(Direction user);
    void onSuccess(UserResponse user);

    void onError(Throwable e);

    void onSuccessLogout(Object object);

    void onSuccess(TripResponse tripResponse);

    void onSuccess(SettingsResponse response);


    void onSettingError(Throwable e);

    void onSuccessProviderAvailable(Object object);

    void onSuccessFCM(Object object);

    void onSuccessLocationUpdate(TripResponse tripResponse);

}
