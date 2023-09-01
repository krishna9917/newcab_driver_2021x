package com.softechurecabdriver.provider.ui.activity.wallet;

import com.softechurecabdriver.provider.base.MvpPresenter;

import java.util.HashMap;

public interface WalletIPresenter<V extends WalletIView> extends MvpPresenter<V> {

    void getWalletData();
    void addMoney(HashMap<String, Object> obj);

}
