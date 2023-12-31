package com.softechurecabdriver.provider.ui.activity.document;

import com.softechurecabdriver.provider.base.BasePresenter;
import com.softechurecabdriver.provider.data.network.APIClient;
import com.softechurecabdriver.provider.data.network.model.Cites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public class DocumentPresenter<V extends DocumentIView> extends BasePresenter<V> implements DocumentIPresenter<V> {

    @Override
    public void getDocuments() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getDriverDocuments()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void postUploadDocuments(@PartMap Map<String, RequestBody> params,
                                    @Part List<MultipartBody.Part> files) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .postUploadDocuments(params, files)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onDocumentSuccess, getMvpView()::onError));
    }

    @Override
    public void logout(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .logout(obj)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccessLogout, getMvpView()::onError));
    }

    @Override
    public void getProfile() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getProfile()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

}
