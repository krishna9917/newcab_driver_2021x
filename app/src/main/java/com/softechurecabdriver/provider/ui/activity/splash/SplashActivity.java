package com.softechurecabdriver.provider.ui.activity.splash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.softechurecabdriver.provider.BuildConfig;
import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.base.BaseActivity;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.LocaleHelper;
import com.softechurecabdriver.provider.common.SharedHelper;
import com.softechurecabdriver.provider.common.Utilities;
import com.softechurecabdriver.provider.data.network.model.CheckVersion;
import com.softechurecabdriver.provider.ui.activity.main.MainActivity;
import com.softechurecabdriver.provider.ui.activity.welcome.WelcomeActivityNew;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity implements SplashIView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SplashActivity";
    private SplashPresenter presenter;


    private GoogleApiClient mGoogleApiClient;

    @Override
    public int getLayoutId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_splash;
    }
    @BindView(R.id.choose_language)
    RadioGroup chooseLanguage;
    private String language;

    Executor executor;
    BiometricManager biometricManager;
    @Override
    public void initView()
    {
        ButterKnife.bind(this);
          executor = ContextCompat.getMainExecutor(this);

          //biometricManager = BiometricManager.from(this);



        moresetup();
    }

    public  void  moresetup()
    {
        presenter = new SplashPresenter();
        presenter.attachView(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        checkVersion();
        languageReset();
        Utilities.printV("FCM TOKEN ===> ", deviceToken);
        Utilities.printV("FCM TOKEN ID ===> ", deviceId);
        chooseLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            showLoading();
            switch (checkedId) {
                case R.id.english:
                    language = Constants.LANGUAGE_ENGLISH;
                    break;
                case R.id.arabic:
                    language = Constants.LANGUAGE_ARABIC;
                    break;
            }
            presenter.changeLanguage(language);

        });

    }


    private void checkVersion() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("version", BuildConfig.VERSION_CODE);
        map.put("device_type", BuildConfig.DEVICE_TYPE);
        map.put("sender", "provider");
        presenter.checkVersion(map);

        Utilities.printV("FCM TOKEN ID===>", SharedHelper.getKeyFCM(this, "device_id"));
    }
    public  void  authuser(Executor ee)
    {
        BiometricPrompt.PromptInfo promptInfo =new  BiometricPrompt.PromptInfo.Builder()
                // 2
                .setTitle("Yaan Cab Driver")
                // 3

                // 4
                .setDescription("You need to Verify Yourself!! use PIN,Pattern or Password")
                // 5
                .setDeviceCredentialAllowed(true)
                // 6
                .build();

        BiometricPrompt promt =  new  BiometricPrompt(this,ee,new BiometricPrompt.AuthenticationCallback(){

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivity(new Intent(activity(), MainActivity.class));
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                finish();
            }
        });

        promt.authenticate(promptInfo);
    }


    private void checkUserAppInstalled() {
        if (Utilities.isPackageExisted(SplashActivity.this, Constants.userAppPackageName))
            showWarningAlert(getString(R.string.user_provider_app_warning));
        else redirectToScreen();
    }

    private void showWarningAlert(String message) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this,R.style.AlertDialogCustom);
            alertDialogBuilder
                    .setTitle(getResources().getString(R.string.warning))
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.continue_app),
                            (dialog, id) -> redirectToScreen());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verifyAppInstalled() {
        checkUserAppInstalled();
    }

    private void redirectToScreen() {
        if (SharedHelper.getKey(this, Constants.SharedPref.LOGGGED_IN).equalsIgnoreCase("true"))
        {
//            if(executor!=null){
//                switch (biometricManager.canAuthenticate())
//                {
//                    case  BiometricManager.BIOMETRIC_SUCCESS: authuser(executor); break;
//                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE : break;
//
//                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE :  ;break;
//                    case  BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED :
//
//                }}
            startActivity(new Intent(activity(), MainActivity.class));
        }
        else
            startActivity(new Intent(activity(), WelcomeActivityNew.class));
    }

    @Override
    public void onSuccess(Object user) {

    }

    @Override
    public void onSuccess(CheckVersion version) {
        try {
            Utilities.printV("jsonObj===>", version.getForceUpdate() + "");
            SharedHelper.putKey(SplashActivity.this,Constants.SharedPref.razorpay_key_id,version.getRazorpay_key_id());
            Log.d("RazroPayId", "onSuccess: "+version.getRazorpay_key_id());
            Log.d("RazroPayId", "onSuccess: "+version.getForceUpdate());
            Log.d("RazroPayId", "onSuccess: "+version.getUrl());
            Log.d("Aurtherization", "onSuccess: "+SharedHelper.getKey(SplashActivity.this,Constants.SharedPref.ACCESS_TOKEN));
            if (!version.getForceUpdate()) presenter.handlerCall();
            else showAlertDialog(version.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlertDialog(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this,R.style.AlertDialogCustom);
        builder.setTitle(getString(R.string.new_version_update));
        builder.setMessage(getString(R.string.update_version_message));
        builder.setPositiveButton(R.string.update, (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
        builder.show();
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @Override
    public void onLanguageChanged(Object object) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        LocaleHelper.setLocale(getApplicationContext(), language);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK));
        this.overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
    }
    @BindView(R.id.english)
    RadioButton english;
    @BindView(R.id.arabic)
    RadioButton arabic;

    private void languageReset() {
        String dd = LocaleHelper.getLanguage(this);
        switch (dd) {
            case Constants.LANGUAGE_ARABIC:
                arabic.setChecked(true);
                break;
            default:
                english.setChecked(true);
                break;
        }
    }


    @Override
    public void onCheckVersionError(Throwable e) {
        hideLoading();
        presenter.handlerCall();
    }

    public void printHashKey() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String hashKey = new String(Base64.encode(md.digest(), 0));
//                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "printHashKey()", e);
//        }

        try {
            final MessageDigest md = MessageDigest.getInstance("SHA");
            if (Build.VERSION.SDK_INT >= 28) {
                PackageInfo packageInfo = getPackageManager().getPackageInfo
                        (getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
                Signature[] signatures = packageInfo.signingInfo.getApkContentsSigners();
                for (Signature signature : signatures) {
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", new String(Base64.encode(md.digest(), Base64.DEFAULT)));
                }
            } else {
                PackageInfo info = getPackageManager().getPackageInfo
                        (BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000 * 10);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, locationSettingsResponse -> runOnUiThread(this::checkVersion));

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) try {
                ResolvableApiException resolvable = (ResolvableApiException) e;
                resolvable.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException sendEx) {
                Toast.makeText(this, sendEx.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected())
            mGoogleApiClient.reconnect();
    }

    private static boolean isGooglePlayServiceOk(Context context, Activity activity) {
        final int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (status != ConnectionResult.SUCCESS) {
            if (GoogleApiAvailability.getInstance().isUserResolvableError(status)) {
                // o valor 300 é um codigo para o onActivityForResult
                Dialog erroDialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, status, 300);
                erroDialog.setCancelable(false);
                erroDialog.show();
            }
            return false;
        }
        return true;
    }
}
