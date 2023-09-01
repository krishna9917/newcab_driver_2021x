package com.softechurecabdriver.provider.ui.fragment.offline;

import com.softechurecabdriver.provider.base.MvpPresenter;

import java.util.HashMap;

public interface OfflineIPresenter<V extends OfflineIView> extends MvpPresenter<V> {

    void providerAvailable(HashMap<String, Object> obj);
}
