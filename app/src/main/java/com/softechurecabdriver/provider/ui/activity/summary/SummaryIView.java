package com.softechurecabdriver.provider.ui.activity.summary;


import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.Summary;

public interface SummaryIView extends MvpView {

    void onSuccess(Summary object);

    void onError(Throwable e);
}
