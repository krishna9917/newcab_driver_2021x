package com.softechurecabdriver.provider.ui.fragment.incoming_request;

import com.softechurecabdriver.provider.base.MvpView;

public interface IncomingRequestIView extends MvpView {

    void onSuccessAccept(Object responseBody);
    void onSuccessCancel(Object object);
    void onError(Throwable e);
}
