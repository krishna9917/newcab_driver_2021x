package com.softechurecabdriver.provider.ui.activity.wallet_detail;

import com.softechurecabdriver.provider.base.MvpPresenter;
import com.softechurecabdriver.provider.data.network.model.Transaction;

import java.util.ArrayList;

public interface WalletDetailIPresenter<V extends WalletDetailIView> extends MvpPresenter<V> {
    void setAdapter(ArrayList<Transaction> myList);
}
