package com.softechurecabdriver.provider.ui.fragment.incoming_request;

import com.softechurecabdriver.provider.base.MvpPresenter;

public interface IncomingRequestIPresenter<V extends IncomingRequestIView> extends MvpPresenter<V> {

    void accept(Integer id);
    void cancel(Integer id);
}
