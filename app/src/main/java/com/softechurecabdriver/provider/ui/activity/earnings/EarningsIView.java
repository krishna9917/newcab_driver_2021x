package com.softechurecabdriver.provider.ui.activity.earnings;


import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.EarningsList;

public interface EarningsIView extends MvpView {

    void onSuccess(EarningsList earningsLists);

    void onError(Throwable e);
}
