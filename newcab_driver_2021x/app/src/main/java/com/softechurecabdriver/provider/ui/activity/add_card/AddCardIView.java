package com.softechurecabdriver.provider.ui.activity.add_card;

import com.softechurecabdriver.provider.base.MvpView;

public interface AddCardIView extends MvpView {

    void onSuccess(Object card);

    void onError(Throwable e);
}
