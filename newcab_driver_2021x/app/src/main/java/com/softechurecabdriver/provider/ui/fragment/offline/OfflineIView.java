package com.softechurecabdriver.provider.ui.fragment.offline;

import com.softechurecabdriver.provider.base.MvpView;

public interface OfflineIView extends MvpView {

    void onSuccess(Object object);
    void onError(Throwable e);
}
