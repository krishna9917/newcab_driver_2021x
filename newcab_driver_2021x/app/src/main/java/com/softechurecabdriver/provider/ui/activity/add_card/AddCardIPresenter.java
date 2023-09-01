package com.softechurecabdriver.provider.ui.activity.add_card;

import com.softechurecabdriver.provider.base.MvpPresenter;

public interface AddCardIPresenter<V extends AddCardIView> extends MvpPresenter<V> {

    void addCard(String stripeToken);
}
