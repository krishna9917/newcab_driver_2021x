package com.softechurecabdriver.provider.ui.activity.setting;

import com.softechurecabdriver.provider.base.MvpPresenter;

public interface SettingsIPresenter<V extends SettingsIView> extends MvpPresenter<V> {
    void changeLanguage(String languageID);
}
