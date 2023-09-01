package com.softechurecabdriver.provider.ui.activity.email;

import android.content.Intent;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.softechurecabdriver.provider.BuildConfig;
import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.base.BaseActivity;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.SharedHelper;
import com.softechurecabdriver.provider.data.network.model.ForgotResponse;
import com.softechurecabdriver.provider.data.network.model.User;
import com.softechurecabdriver.provider.ui.activity.main.MainActivity;

import com.softechurecabdriver.provider.ui.activity.regsiter.RegisterActivity;
import com.softechurecabdriver.provider.ui.activity.reset_password.ResetActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class EmailActivity extends BaseActivity implements EmailIView {

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.sign_up)
    TextView signUp;
    @BindView(R.id.next)
    FloatingActionButton next;
    @BindView(R.id.pass)
    EditText pass;

    EmailIPresenter presenter = new EmailPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_email;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        //((TextInputLayout)findViewById(R.id.textInputLayout)).setHint("Phone number");
        findViewById(R.id.imageView2).setOnClickListener(view -> {

            ((TextInputLayout)findViewById(R.id.textInputLayout)).setHint("Email");
            ((EditText)findViewById(R.id.email)).setText("");

            ((ImageView)findViewById(R.id.imageView)).setBackgroundResource(R.drawable.button_round_white);
            ((ImageView)findViewById(R.id.imageView2)).setBackgroundResource(R.drawable.button_round_accent);
            ((EditText)findViewById(R.id.email)).setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS); ;
        });
        findViewById(R.id.imageView).setOnClickListener(view -> {
            ((TextInputLayout)findViewById(R.id.textInputLayout)).setHint("Phone number");
            ((EditText)findViewById(R.id.email)).setText("");
            ((ImageView)findViewById(R.id.imageView)).setBackgroundResource(R.drawable.button_round_accent);
            ((ImageView)findViewById(R.id.imageView2)).setBackgroundResource(R.drawable.button_round_white);
            ((EditText)findViewById(R.id.email)).setInputType(InputType.TYPE_CLASS_PHONE); ;
        });


         findViewById(R.id.toolbar).setOnClickListener(view->{
             onBackPressed();
         });


        if (BuildConfig.DEBUG) {
//            email.setText("provider@dragon.com");
//            pass.setText("password");
        }
    }

    @OnClick({ R.id.sign_up, R.id.next,R.id.forgot_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {



            case R.id.forgot_password:
                showLoading();
                HashMap<String, Object> map = new HashMap<>();
                map.put("email", email.getText().toString());
                presenter.forgot(map);
                break;
            case R.id.sign_up:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.next:
//                if (email.getText().toString().isEmpty()) {
//                    Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
//                    return;
//                }
//                Intent i = new Intent(this, PasswordActivity.class);
//                i.putExtra(Constants.SharedPref.EMAIL, email.getText().toString());
//                SharedHelper.putKey(this, Constants.SharedPref.TXT_EMAIL, email.getText().toString());
//                startActivity(i);
                login();
               // break;
        }
    }
    private void login() {
        if (pass.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (email.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
            return;
        }

        deviceToken = SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN);
        deviceId = SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID);

        if (TextUtils.isEmpty(deviceToken))
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    deviceToken = instanceIdResult.getToken();
                    SharedHelper.putKeyFCM(EmailActivity.this, Constants.SharedPref.DEVICE_TOKEN, deviceToken);
                }
            });

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            SharedHelper.putKeyFCM(this, Constants.SharedPref.DEVICE_ID, deviceId);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", email.getText().toString());
        map.put("password", pass.getText().toString());
        map.put("device_id", deviceId);
        map.put("device_type", BuildConfig.DEVICE_TYPE);
        map.put("device_token", deviceToken);
        presenter.login(map);
        showLoading();
    }

    @Override
    public void onSuccess(User user)
    {
        hideLoading();
        SharedHelper.putKey(this, Constants.SharedPref.ACCESS_TOKEN, user.getAccessToken());
        SharedHelper.putKey(this, Constants.SharedPref.USER_ID,
                String.valueOf(user.getId()));
        SharedHelper.putKey(this, Constants.SharedPref.LOGGGED_IN, "true");
        Toasty.success(activity(), getString(R.string.login_out_success), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    @Override
    public void onSuccess(ForgotResponse forgotResponse) {
        hideLoading();
        Toasty.success(this, forgotResponse.getMessage(), Toast.LENGTH_SHORT, true).show();
        SharedHelper.putKey(this, Constants.SharedPref.TXT_EMAIL, email.getText().toString());
        SharedHelper.putKey(this, Constants.SharedPref.OTP, String.valueOf(forgotResponse.getProvider().getOtp()));
        SharedHelper.putKey(this, Constants.SharedPref.ID, String.valueOf(forgotResponse.getProvider().getId()));
        startActivity(new Intent(this, ResetActivity.class));
    }

    @Override
    public void onError(Throwable e) {
//        TAG = "PasswordActivity";
        hideLoading();
        Log.d("dsfj3","joijdfg " +  "ghkfghl");
        onErrorBase(e);
    }
}
