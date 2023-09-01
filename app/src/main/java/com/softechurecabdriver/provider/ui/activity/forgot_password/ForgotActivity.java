package com.softechurecabdriver.provider.ui.activity.forgot_password;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.base.BaseActivity;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.SharedHelper;
import com.softechurecabdriver.provider.data.network.model.ForgotResponse;
import com.softechurecabdriver.provider.ui.activity.reset_password.ResetActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ForgotActivity extends BaseActivity implements ForgotIView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.next)
    FloatingActionButton next;

    ForgotPresenter presenter = new ForgotPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgot;
    }

    @Override
    public void initView() {

        ButterKnife.bind(this);
        presenter.attachView(this);

    }

    @OnClick({R.id.back, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                activity().onBackPressed();
                break;
            case R.id.next:

                if (txtEmail.getText().toString().isEmpty()) {
                    Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
                }else if(txtEmail.getText().toString().length()<10)
                 {
                Toasty.error(this, getString(R.string.invalid_mobile), Toast.LENGTH_SHORT, true).show();
               }
                else {
                    showLoading();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("mobile",txtEmail.getText().toString());
                    presenter.forgot(map);
                }
                break;
        }
    }

    @Override
    public void onSuccess(ForgotResponse forgotResponse) {
        hideLoading();
        if(forgotResponse.getStatus())
        {
            SharedHelper.putKey(this, Constants.SharedPref.MOBILE, txtEmail.getText().toString());
//        SharedHelper.putKey(this, Constants.SharedPref.ID, String.valueOf(forgotResponse.getProvider().getId()));
            Toasty.success(this, forgotResponse.getMessage(), Toast.LENGTH_SHORT, true).show();
            startActivity(new Intent(this, ResetActivity.class));
        }else
        {
            Toasty.error(this, forgotResponse.getMessage(), Toast.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if(e!= null)
        onErrorBase(e);
    }
}
