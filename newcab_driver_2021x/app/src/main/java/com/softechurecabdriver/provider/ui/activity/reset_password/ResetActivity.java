package com.softechurecabdriver.provider.ui.activity.reset_password;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.base.BaseActivity;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.SharedHelper;
import com.softechurecabdriver.provider.ui.activity.email.EmailActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ResetActivity extends BaseActivity implements ResetIView {

    @BindView(R.id.txtOTP)
    EditText txtOTP;
    @BindView(R.id.txtNewPassword)
    EditText txtNewPassword;
    @BindView(R.id.txtPassword)
    EditText txtPassword;

    String OTP;
    ResetPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter = new ResetPresenter();
        presenter.attachView(this);
        OTP = SharedHelper.getKey(this,Constants.SharedPref.OTP);
        Toast.makeText(this, OTP + "otp here", Toast.LENGTH_SHORT).show();
        //Log.e("testest otp : ",OTP);
    }

    @OnClick({R.id.back, R.id.btnDone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.btnDone:
                if (!txtOTP.getText().toString().equals(OTP)) {
                    Toasty.error(this, getString(R.string.please_correct_otp), Toast.LENGTH_SHORT, true).show();
                } else if (txtOTP.getText().toString().isEmpty()) {
                    Toasty.error(this, getString(R.string.please_correct_otp), Toast.LENGTH_SHORT, true).show();
                } else if (txtPassword.getText().toString().isEmpty()) {
                    Toasty.error(this, getString(R.string.invalid_old_password), Toast.LENGTH_SHORT, true).show();
                } else if (txtNewPassword.getText().toString().isEmpty()) {
                    Toasty.error(this, getString(R.string.invalid_new_password), Toast.LENGTH_SHORT, true).show();
                } else if (!txtPassword.getText().toString().equals(txtNewPassword.getText().toString())) {
                    Toasty.error(this, getString(R.string.password_should_be_same), Toast.LENGTH_SHORT, true).show();
                } else {
                    showLoading();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", SharedHelper.getKey(this, Constants.SharedPref.ID));
                    map.put("password", txtNewPassword.getText().toString());
                    map.put("password_confirmation", txtNewPassword.getText().toString());
                    presenter.reset(map);
                }
                break;
        }
    }

    @Override
    public void onSuccess(Object object) {
        hideLoading();
        Toasty.success(this, getString(R.string.password_updated), Toast.LENGTH_SHORT, true).show();
        Intent goToLogin = new Intent(activity(), EmailActivity.class);
        goToLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity().startActivity(goToLogin);
        activity().finish();
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if(e!= null)
        onErrorBase(e);
    }
}
