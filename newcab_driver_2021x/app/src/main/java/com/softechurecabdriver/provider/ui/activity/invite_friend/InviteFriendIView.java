package com.softechurecabdriver.provider.ui.activity.invite_friend;

import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.UserResponse;

public interface InviteFriendIView extends MvpView {

    void onSuccess(UserResponse response);
    void onError(Throwable e);

}
