package com.softechurecabdriver.provider.ui.activity.regsiter;

import com.softechurecabdriver.provider.base.MvpPresenter;
import com.softechurecabdriver.provider.data.network.model.Cites;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RegisterIPresenter<V extends RegisterIView> extends MvpPresenter<V> {

    void register(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> file,@Part MultipartBody.Part filePart);

    void verifyEmail(String email);

    void getServices(int cityId);

    void getSettings();

    void verifyCredentials(String countryCode, String phoneNumber);

    void getCity();

}
