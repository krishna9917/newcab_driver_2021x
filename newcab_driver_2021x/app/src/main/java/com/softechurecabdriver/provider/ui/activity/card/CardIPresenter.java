package com.softechurecabdriver.provider.ui.activity.card;

import com.softechurecabdriver.provider.base.MvpPresenter;

public interface CardIPresenter<V extends CardIView> extends MvpPresenter<V> {

    void deleteCard(String cardId);

    void card();

    void changeCard(String cardId);
}
