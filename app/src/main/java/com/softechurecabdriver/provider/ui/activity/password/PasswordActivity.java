package com.softechurecabdriver.provider.ui.activity.password;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.softechurecabdriver.provider.BuildConfig;
import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.base.BaseActivity;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.SharedHelper;
import com.softechurecabdriver.provider.common.Utilities;
import com.softechurecabdriver.provider.data.network.model.ForgotResponse;
import com.softechurecabdriver.provider.data.network.model.LoginUser;
import com.softechurecabdriver.provider.ui.activity.main.MainActivity;
import com.softechurecabdriver.provider.ui.activity.regsiter.RegisterActivity;
import com.softechurecabdriver.provider.ui.activity.reset_password.ResetActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class PasswordActivity extends BaseActivity implements PasswordIView {

    PasswordPresenter presenter = new PasswordPresenter();

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.sign_up)
    TextView signUp;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.next)
    FloatingActionButton next;
    String email = "";
    public static String TAG = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString(Constants.SharedPref.EMAIL);
            Utilities.printV("EMAIL===>", email);
            Utilities.printV("EMAIL===>", SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN));
            Utilities.printV("EMAIL===>", SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID));
        }
        //if (BuildConfig.DEBUG) password.setText("123456");


        if (SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN).isEmpty()) {
//            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
//                if (!task.isSuccessful()) {
//                    Log.w("PasswordActivity", "getInstanceId failed", task.getException());
//                    return;
//                }
//                Log.d("FCM_TOKEN", task.getResult().getToken());
//
//                SharedHelper.putKeyFCM(PasswordActivity.this, Constants.SharedPref.DEVICE_TOKEN, task.getResult().getToken());
//            });


            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.w("PasswordActivity", "getInstanceId failed", task.getException());
                                                return;
                    }
                    // Get new FCM registration token
                    deviceToken= task.getResult();
                    SharedHelper.putKeyFCM(PasswordActivity.this, Constants.SharedPref.DEVICE_TOKEN, task.getResult());
                }
            });
        }

        @SuppressLint("HardwareIds")
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("DEVICE_ID: ", deviceId);
        SharedHelper.putKeyFCM(this, Constants.SharedPref.DEVICE_ID, deviceId);


    }

    @OnClick({R.id.back, R.id.sign_up, R.id.forgot_password, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                activity().onBackPressed();
                break;
            case R.id.sign_up:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.forgot_password:
                showLoading();
                HashMap<String, Object> map = new HashMap<>();
                map.put("email", email);
                presenter.forgot(map);
//                startActivity(new Intent(this, ForgotActivity.class));
                break;
            case R.id.next:
                login();
                break;
            default:
                break;
        }
    }

    private void login() {
        if (password.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (email.isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
            return;
        }

        deviceToken = SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN);
        deviceId = SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID);

//        if (TextUtils.isEmpty(deviceToken))
//            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
//                @Override
//                public void onSuccess(InstanceIdResult instanceIdResult) {
//                    deviceToken = instanceIdResult.getToken();
//                    SharedHelper.putKeyFCM(PasswordActivity.this, Constants.SharedPref.DEVICE_TOKEN, deviceToken);
//                }
//            });
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                // Get new FCM registration token
                deviceToken= task.getResult();
                SharedHelper.putKeyFCM(PasswordActivity.this, Constants.SharedPref.DEVICE_TOKEN, deviceToken);
            }
        });

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            SharedHelper.putKeyFCM(this, Constants.SharedPref.DEVICE_ID, deviceId);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password.getText().toString());
        map.put("device_id", deviceId);
        map.put("device_type", BuildConfig.DEVICE_TYPE);
        map.put("device_token", deviceToken);
        presenter.login(map);
        showLoading();
    }

    @Override
    public void onSuccess(ForgotResponse forgotResponse) {
        Log.d("forgotResponse",forgotResponse.toString());
        hideLoading();
        SharedHelper.putKey(this, Constants.SharedPref.MOBILE, email);
//        SharedHelper.putKey(this, Constants.SharedPref.OTP, String.valueOf(forgotResponse.getOtp()));
//        SharedHelper.putKey(this, Constants.SharedPref.ID, String.valueOf(forgotResponse.getId()));
        Toasty.success(this, forgotResponse.getMessage(), Toast.LENGTH_SHORT, true).show();
        startActivity(new Intent(this, ResetActivity.class));
    }

    @Override
    public void onSuccess(LoginUser loginUser) {
        hideLoading();
        if(loginUser.getStatus())
        {
            Log.d("UserToken", "onSuccess: "+loginUser.getUser().getAccessToken());
            SharedHelper.putKey(this, Constants.SharedPref.ACCESS_TOKEN, loginUser.getUser().getAccessToken());
            SharedHelper.putKey(this, Constants.SharedPref.USER_ID, String.valueOf(loginUser.getUser().getId()));
            SharedHelper.putKey(this, Constants.SharedPref.LOGGGED_IN, "true");
            Toasty.success(activity(), getString(R.string.login_out_success), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }else
        {
            Toasty.error(activity(), loginUser.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            TAG = "PasswordActivity";
        onErrorBase(e);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
