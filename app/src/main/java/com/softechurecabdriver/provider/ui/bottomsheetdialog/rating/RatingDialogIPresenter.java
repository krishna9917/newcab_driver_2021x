package com.softechurecabdriver.provider.ui.bottomsheetdialog.rating;

import com.softechurecabdriver.provider.base.MvpPresenter;

import java.util.HashMap;

public interface RatingDialogIPresenter<V extends RatingDialogIView> extends MvpPresenter<V> {

    void rate(HashMap<String, Object> obj, Integer id);
}
