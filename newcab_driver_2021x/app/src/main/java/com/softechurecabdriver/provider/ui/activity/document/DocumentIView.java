package com.softechurecabdriver.provider.ui.activity.document;

import com.softechurecabdriver.provider.base.MvpView;
import com.softechurecabdriver.provider.data.network.model.DriverDocumentResponse;

public interface DocumentIView extends MvpView {

    void onSuccess(DriverDocumentResponse response);

    void onDocumentSuccess(DriverDocumentResponse response);

    void onError(Throwable e);

    void onSuccessLogout(Object object);

}
