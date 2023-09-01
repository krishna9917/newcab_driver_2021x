package com.softechurecabdriver.provider.ui.activity.help;


import com.softechurecabdriver.provider.base.MvpPresenter;

public interface HelpIPresenter<V extends HelpIView> extends MvpPresenter<V> {

    void getHelp();
}
