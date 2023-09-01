package com.softechurecabdriver.provider.ui.activity.email;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.softechurecabdriver.provider.BuildConfig;
import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.base.BaseActivity;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.SharedHelper;
import com.softechurecabdriver.provider.data.network.model.ForgotResponse;
import com.softechurecabdriver.provider.data.network.model.LoginUser;
import com.softechurecabdriver.provider.ui.activity.forgot_password.ForgotActivity;
import com.softechurecabdriver.provider.ui.activity.main.MainActivity;
import com.softechurecabdriver.provider.ui.activity.regsiter.RegisterActivity;

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
            ((EditText)findViewById(R.id.email)).setInputType(InputType.TYPE_CLASS_PHONE);
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
                 startActivity(new Intent(this, ForgotActivity.class));
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
    @SuppressLint("HardwareIds")
    private void login() {
        if (pass.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (email.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
            return;
        }

        if (SharedHelper.getKey(EmailActivity.this,  Constants.SharedPref.DEVICE_ID).isEmpty()) {
            @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.d("DATA", "validate: device_id-->" + deviceId);
            SharedHelper.putKey(EmailActivity.this,  Constants.SharedPref.DEVICE_ID, deviceId);
        }
        if (SharedHelper.getKey(EmailActivity.this, Constants.SharedPref.DEVICE_TOKEN).isEmpty()) {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if (s != null) {
                        SharedHelper.putKey(EmailActivity.this, Constants.SharedPref.DEVICE_TOKEN, s);
                        Log.d("DATA", "validate: device_token-->" + s);
                    }
                }
            });
        }


        deviceToken = SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN);
        deviceId = SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID);


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
    public void onSuccess(LoginUser loginUser)
    {
        hideLoading();
        if(loginUser.getStatus())
        {
            SharedHelper.putKey(this, Constants.SharedPref.ACCESS_TOKEN,  loginUser.getUser().getAccessToken());
            SharedHelper.putKey(this, Constants.SharedPref.USER_ID, String.valueOf( loginUser.getUser().getId()));
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
    public void onSuccess(ForgotResponse forgotResponse) {
        hideLoading();
    }

    @Override
    public void onError(Throwable e) {
//        TAG = "PasswordActivity";
        hideLoading();
        Log.d("dsfj3","joijdfg " +  "ghkfghl");
        onErrorBase(e);
    }
}
