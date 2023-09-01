package com.softechurecabdriver.provider.ui.bottomsheetdialog.invoice_flow;

import com.softechurecabdriver.provider.base.MvpView;

public interface InvoiceDialogIView extends MvpView {

    void onSuccess(Object object);
    void onError(Throwable e);
}
