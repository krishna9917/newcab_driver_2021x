package com.softechurecabdriver.provider.ui.activity.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.LocaleHelper;
import com.softechurecabdriver.provider.data.network.model.CheckVersion;
import com.softechurecabdriver.provider.data.network.model.User;
import com.softechurecabdriver.provider.ui.activity.email.EmailActivity;
import com.softechurecabdriver.provider.ui.activity.regsiter.RegisterActivity;
import com.softechurecabdriver.provider.ui.activity.splash.SplashIView;
import com.softechurecabdriver.provider.ui.activity.splash.SplashPresenter;
import com.yariksoffice.lingver.Lingver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivityNew extends AppCompatActivity implements SplashIView
{
    private SplashPresenter presenter;
    @BindView(R.id.tv_loginn)
    TextView login;
    @BindView(R.id.tv_registerry)
    TextView regsuter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_welcomeactivity);
        ButterKnife.bind(this);

        presenter = new SplashPresenter();
        presenter.attachView(this);

        chooseLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            showLoading();
            switch (checkedId) {
                case R.id.english:
                    language = Constants.LANGUAGE_ENGLISH;
                    break;
                case R.id.french:
                    language = Constants.LANGUAGE_FRANCE;
                    break;
                case R.id.arabic:
                    language = Constants.LANGUAGE_ARABIC;
                    break;
            }
            Lingver.getInstance().setLocale(this, language);
            Intent intent = new Intent(this, WelcomeActivityNew.class);
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK));
            this.overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
            //LocaleHelper.setLocale(getApplicationContext(), language);
            findViewById(R.id.grplanguages).setVisibility(View.GONE);
            //presenter.changeLanguage(language);

        });

        findViewById(R.id.tv_languages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.grplanguages).setVisibility(View.VISIBLE);
            }
        });
    }


    @OnClick({R.id.tv_loginn, R.id.tv_registerry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_loginn:
                startActivity(new Intent(this, EmailActivity.class));
                break;
            case R.id.tv_registerry:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

        }
    }
    @BindView(R.id.choose_language)
    RadioGroup chooseLanguage;
    @BindView(R.id.english)
    RadioButton english;
    @BindView(R.id.arabic)
    RadioButton arabic;
    private String language;

    @Override
    public void verifyAppInstalled() {

    }

    @Override
    public void onSuccess(Object user) {

    }

    @Override
    public void onSuccess(CheckVersion user) {

    }

    @Override
    public Activity activity() {
        return null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onErrorRefreshToken(Throwable throwable) {

    }

    @Override
    public void onSuccessRefreshToken(User user) {

    }

    @Override
    public void onSuccessLogout(Object object) {

    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        findViewById(R.id.grplanguages).setVisibility(View.GONE);

    }

    @Override
    public void onLanguageChanged(Object object) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        LocaleHelper.setLocale(getApplicationContext(), language);
        findViewById(R.id.grplanguages).setVisibility(View.GONE);

    }

    @Override
    public void onCheckVersionError(Throwable e) {

    }
}
