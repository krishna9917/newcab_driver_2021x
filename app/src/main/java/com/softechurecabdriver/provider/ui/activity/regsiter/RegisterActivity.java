package com.softechurecabdriver.provider.ui.activity.regsiter;

import static com.softechurecabdriver.provider.common.Constants.APP_REQUEST_CODE;
import static com.softechurecabdriver.provider.common.Constants.MULTIPLE_PERMISSION;
import static com.softechurecabdriver.provider.common.Constants.RC_MULTIPLE_PERMISSION_CODE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.softechurecabdriver.provider.BuildConfig;
import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.base.BaseActivity;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.SharedHelper;
import com.softechurecabdriver.provider.common.Utilities;
import com.softechurecabdriver.provider.data.network.model.Cites;
import com.softechurecabdriver.provider.data.network.model.City;
import com.softechurecabdriver.provider.data.network.model.RegisterCity;
import com.softechurecabdriver.provider.data.network.model.ServiceType;
import com.softechurecabdriver.provider.data.network.model.SettingsResponse;
import com.softechurecabdriver.provider.data.network.model.State;
import com.softechurecabdriver.provider.data.network.model.User;
import com.softechurecabdriver.provider.ui.activity.main.MainActivity;
import com.softechurecabdriver.provider.ui.countrypicker.Country;
import com.softechurecabdriver.provider.ui.countrypicker.CountryPicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class RegisterActivity extends BaseActivity implements RegisterIView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;
    @BindView(R.id.txtLastName)
    EditText txtLastName;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;
    @BindView(R.id.chkTerms)
    CheckBox chkTerms;
    @BindView(R.id.spinnerServiceType)
    AppCompatSpinner spinnerServiceType;
    @BindView(R.id.txtVehicleModel)
    EditText txtVehicleModel;
    @BindView(R.id.txtVehicleNumber)
    EditText txtVehicleNumber;
    @BindView(R.id.lnrReferralCode)
    LinearLayout lnrReferralCode;
    @BindView(R.id.txtReferalCode)
    EditText txtReferalCode;
    @BindView(R.id.countryImage)
    ImageView countryImage;
    @BindView(R.id.countryNumber)
    TextView countryNumber;
    @BindView(R.id.phoneNumber)
    EditText phoneNumber;
    @BindView(R.id.sexy_spinner)
    AppCompatSpinner sexy_Spinner;
    @BindView(R.id.city_spinner)
    AppCompatSpinner city_Spinner;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;

    private String countryDialCode = "+91";
    private String countryCode = "in";
    private CountryPicker mCountryPicker;

    private RegisterPresenter presenter;
    private int selected_pos = -1;
    private List<ServiceType> lstServiceTypes = new ArrayList<>();
    private ArrayList<RegisterCity> lstCities = new ArrayList<RegisterCity>();

    private ArrayAdapter<State> stateArrayAdapter;
    private ArrayAdapter<City> cityArrayAdapter;
    private ArrayAdapter<String> dataAdapter;

    private boolean isEmailAvailable = true;
    private boolean isPhoneNumberAvailable = true;
    private int city_id=1;

    private File imgFile = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        TextView termsLink = findViewById(R.id.tv_terms_link);
        termsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = "https://jibouti.yaan.com/privacy";
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(myIntent);
            }
        });

        ButterKnife.bind(this);
        presenter = new RegisterPresenter();
        presenter.attachView(this);
        if (Utilities.isConnected())
        {
            showLoading();
            presenter.getSettings();
            showLoading();
            presenter.getCity();

        }else
        {
            showAToast(getString(R.string.no_internet_connection));
            finish();
        }
        setupSpinnerCities();
        spinnerServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        clickFunctions();

        setCountryList();

//        if (BuildConfig.DEBUG) {
//            txtEmail.setText("netopvh.dev@hotmail.com");
//            txtFirstName.setText("Angelo");
//            txtLastName.setText("Neto");
//            txtVehicleModel.setText("Golf");
//            txtVehicleNumber.setText("NBC5214");
//            phoneNumber.setText("69993148701");
//            txtPassword.setText("123456");
//            txtConfirmPassword.setText("123456");
//        }

        if (SharedHelper.getKey(this, Constants.SharedPref.DEVICE_TOKEN).isEmpty()) {
//            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
//                if (!task.isSuccessful()) {
//                    Log.w("PasswordActivity", "getInstanceId failed", task.getException());
//                    return;
//                }
//                Log.d("FCM_TOKEN", task.getResult().getToken());
//
//                SharedHelper.putKey(RegisterActivity.this, Constants.SharedPref.DEVICE_TOKEN, task.getResult().getToken());
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
                    SharedHelper.putKey(RegisterActivity.this, Constants.SharedPref.DEVICE_TOKEN, task.getResult());
                }
            });
        }

        @SuppressLint("HardwareIds")
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        SharedHelper.putKeyFCM(RegisterActivity.this, Constants.SharedPref.DEVICE_ID, deviceId);
    }

    private void setCountryList() {
        mCountryPicker = CountryPicker.newInstance("Select Country");
        List<Country> countryList = Country.getAllCountries();
        countryDialCode = countryList.get(0).getDialCode();
        countryImage.setImageResource(countryList.get(0).getFlag());
        //Collections.sort(countryList, (s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        mCountryPicker.setCountriesList(countryList);

        setListener();
    }

    private void setListener() {
        mCountryPicker.setListener((name, code, dialCode, flagDrawableResID) -> {
            countryNumber.setText(dialCode);
            countryDialCode = dialCode;
            countryImage.setImageResource(flagDrawableResID);
            mCountryPicker.dismiss();
        });

        countryImage.setOnClickListener(v -> mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER"));

        countryNumber.setOnClickListener(v -> mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER"));

        getUserCountryInfo();
    }

    private void getUserCountryInfo() {
        Country country = getDeviceCountry(RegisterActivity.this);
        countryImage.setImageResource(country.getFlag());
        countryNumber.setText(country.getDialCode());
        countryDialCode = country.getDialCode();
        countryCode = country.getCode();
    }

    private boolean validation() {
        if (imgFile == null)
        {
            Toasty.error(this, R.string.upload_image, Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if (txtEmail.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtFirstName.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_first_name), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtLastName.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_last_name), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (selected_pos == -1) {
            Toasty.error(this, getString(R.string.invalid_service_type), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtVehicleModel.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_car_model), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtVehicleNumber.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_car_number), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (phoneNumber.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_phone_number), Toast.LENGTH_SHORT).show();
            return false;
        } else if (txtPassword.getText().toString().length() < 6) {
            Toasty.error(this, getString(R.string.invalid_password_length), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtConfirmPassword.getText().toString().trim().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_confirm_password), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
            Toasty.error(this, getString(R.string.password_should_be_same), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (!chkTerms.isChecked()) {
            Toasty.error(this, getString(R.string.please_accept_terms_conditions), Toast.LENGTH_SHORT, true).show();
            return false;
        }  else return true;
    }

    private void showErrorMessage(EditText view, String message) {
        Toasty.error(this, message, Toast.LENGTH_SHORT).show();
        view.requestFocus();
        view.setText(null);
    }

    private void register(String countryCode, String phoneNumber)
    {
        if (SharedHelper.getKey(RegisterActivity.this,  Constants.SharedPref.DEVICE_ID).isEmpty()) {
            @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.d("DATA", "validate: device_id-->" + deviceId);
            SharedHelper.putKey(RegisterActivity.this,  Constants.SharedPref.DEVICE_ID, deviceId);
        }
        if (SharedHelper.getKey(RegisterActivity.this, Constants.SharedPref.DEVICE_TOKEN).isEmpty()) {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if (s != null) {
                        SharedHelper.putKey(RegisterActivity.this, Constants.SharedPref.DEVICE_TOKEN, s);
                        Log.d("DATA", "validate: device_token-->" + s);
                    }
                }
            });
        }


        Map<String, RequestBody> map = new HashMap<>();
        map.put("first_name", toRequestBody(txtFirstName.getText().toString()));
        map.put("last_name", toRequestBody(txtLastName.getText().toString()));
        map.put("email", toRequestBody(txtEmail.getText().toString()));
        map.put("mobile", toRequestBody(phoneNumber));
        map.put("gender", toRequestBody(sexy_Spinner.getSelectedItem().toString()));
        map.put("city_id",toRequestBody(String.valueOf(city_id)));
        map.put("country_code", toRequestBody(countryCode));
        map.put("password", toRequestBody(txtPassword.getText().toString()));
        map.put("password_confirmation", toRequestBody(txtConfirmPassword.getText().toString()));
        map.put("device_token", toRequestBody(SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN)));
        map.put("device_id", toRequestBody(SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID)));
        map.put("service_type", toRequestBody(lstServiceTypes.get(selected_pos).getId() + ""));
        map.put("service_model", toRequestBody(txtVehicleModel.getText().toString()));
        map.put("service_number", toRequestBody(txtVehicleNumber.getText().toString()));
        map.put("device_type", toRequestBody(BuildConfig.DEVICE_TYPE));
        map.put("referral_code", toRequestBody(txtReferalCode.getText().toString()));

        Log.d("REGISTER---->", "register: DEVICE_TOKEN "+SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN));
        Log.d("REGISTER---->", "register: DEVICE_ID "+SharedHelper.getKeyFCM(this, Constants.SharedPref.DEVICE_ID));

//        String parameters = "";
//        for (Map.Entry<String, RequestBody> entry : map.entrySet()) {
//            parameters += entry.getKey() + ":" + entry.getValue() + "\n";
//
//        }
//        Log.d("Tag", "API Call test_register parameters:\n" + parameters + "\n");

        List<MultipartBody.Part> parts = new ArrayList<>();
        MultipartBody.Part filePart = null;
        if (imgFile != null)
            try {
                File compressedImageFile = new Compressor(this).compressToFile(imgFile);
                filePart = MultipartBody.Part.createFormData("avatar", compressedImageFile.getName(),
                        RequestBody.create(MediaType.parse("image*//*"), compressedImageFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        showLoading();
        presenter.register(map, parts,filePart);
    }

    @OnClick({R.id.next, R.id.back, R.id.lblTerms,R.id.imgProfile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.next:
                if (validation())
                    if (Utilities.isConnected())
                       // countryNumber.getText().toString()
                    register("",phoneNumber.getText().toString());
                    //fbPhoneLogin(countryCode, countryDialCode, phoneNumber.getText().toString());
                else showAToast(getString(R.string.no_internet_connection));
                break;
            case R.id.lblTerms:
                showTermsConditionsDialog();
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.imgProfile:
                MultiplePermissionTask();
                break;
        }
    }

    private void clickFunctions() {
        txtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            isEmailAvailable = true;
            if (!hasFocus && !TextUtils.isEmpty(txtEmail.getText().toString()))
                if (Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches())
                    presenter.verifyEmail(txtEmail.getText().toString().trim());
        });

        phoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            isPhoneNumberAvailable = true;
            if (!hasFocus && !TextUtils.isEmpty(phoneNumber.getText().toString()))
                presenter.verifyCredentials(countryDialCode, phoneNumber.getText().toString());
        });
    }

    private void showTermsConditionsDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this,R.style.AlertDialogCustom);
        alert.setTitle(getText(R.string.terms_and_conditions));
        WebView wv = new WebView(this);
        wv.loadUrl(BuildConfig.TERMS_CONDITIONS);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton("Close", (dialog, id) -> dialog.dismiss());
        alert.show();
    }

    @Override
    public void onSuccess(User user) {
        hideLoading();
        SharedHelper.putKey(this, Constants.SharedPref.USER_ID, String.valueOf(user.getId()));
        SharedHelper.putKey(this, Constants.SharedPref.ACCESS_TOKEN, user.getAccessToken());
        SharedHelper.putKey(this, Constants.SharedPref.LOGGGED_IN, "true");
        Toasty.success(this, getString(R.string.register_success), Toast.LENGTH_SHORT, true).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onSuccess(Object verifyEmail) {
        hideLoading();
        isEmailAvailable = false;
    }

    @Override
    public void onVerifyEmailError(Throwable e) {
        isEmailAvailable = true;
        showErrorMessage(txtEmail, getString(R.string.email_already_exist));
    }

    @Override
    public void onSuccessCityList(Cites cites) {
        hideLoading();
        lstCities=cites.getCities();
        ArrayList<String> list=new ArrayList<>();
        for(RegisterCity citesList:lstCities)
        {
            list.add(citesList.getName());
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner,list);
        adapter.setDropDownViewResource(R.layout.spinner);
        city_Spinner.setAdapter(adapter);
        city_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for(RegisterCity city :lstCities)
                {
                    if(city.getName().equalsIgnoreCase(city_Spinner.getSelectedItem().toString()))
                    {
                        city_id=city.getId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                city_id=1;
            }
        });
    }


    @Override
    public void onSuccess(SettingsResponse response) {
         hideLoading();
        lstServiceTypes = response.getServiceTypes();
        lnrReferralCode.setVisibility(response.getReferral().getReferral().equalsIgnoreCase("1") ? View.VISIBLE : View.GONE);
        setupSpinner(response);
    }

    @AfterPermissionGranted(RC_MULTIPLE_PERMISSION_CODE)
    void MultiplePermissionTask() {
        if (hasMultiplePermission()) pickImage();
        else EasyPermissions.requestPermissions(this, getString(R.string.please_accept_permission), RC_MULTIPLE_PERMISSION_CODE, MULTIPLE_PERMISSION);
    }
    private boolean hasMultiplePermission() {
        return EasyPermissions.hasPermissions(this, MULTIPLE_PERMISSION);
    }


    private void setupSpinner(@Nullable SettingsResponse response) {

        ArrayList<String> lstNames = new ArrayList<>(response != null ? response.getServiceTypes().size() : 0);
        if (response != null) for (ServiceType serviceType : response.getServiceTypes())
            lstNames.add(getNameResult(serviceType.getName()));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lstNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServiceType.setAdapter(dataAdapter);
    }

    private void setupSpinnerCities(){

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                 R.array.gender_list, R.layout.spinner);
        adapter.setDropDownViewResource(R.layout.spinner);
        sexy_Spinner.setAdapter(adapter);
    }
    @Override
    public void onError(Throwable e) {
        Log.d("sdnfds","sdbsbd: " + e.getMessage());

        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    public void fbPhoneLogin(String strCountryCode, String strCountryISOCode, String strPhoneNumber) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder mBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN);
        mBuilder.setReadPhoneStateEnabled(true);
        mBuilder.setReceiveSMS(true);
        PhoneNumber phoneNumber = new PhoneNumber(strCountryISOCode, strPhoneNumber, strCountryCode);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                mBuilder.setInitialPhoneNumber(phoneNumber).
                        build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE && data != null) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (!loginResult.wasCancelled()) {
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        Log.d("AccountKit", "onSuccess: Account Kit" + AccountKit.getCurrentAccessToken().getToken());
                        if (AccountKit.getCurrentAccessToken().getToken() != null) {
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            SharedHelper.putKey(RegisterActivity.this, Constants.SharedPref.DIAL_CODE,
                                    "+" + phoneNumber.getCountryCode());
                            SharedHelper.putKey(RegisterActivity.this, Constants.SharedPref.MOBILE,
                                    phoneNumber.getPhoneNumber());
                            register(SharedHelper.getKey(RegisterActivity.this, Constants.SharedPref.DIAL_CODE),
                                    SharedHelper.getKey(RegisterActivity.this, Constants.SharedPref.MOBILE));
                        }
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Log.e("AccountKit", "onError: Account Kit" + accountKitError);
                    }
                });
            }
        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, RegisterActivity.this,
                new DefaultCallback() {
                    @Override
                    public void onImagesPicked(@NonNull List<File> imageFiles,
                                               EasyImage.ImageSource source, int type) {
                        imgFile = imageFiles.get(0);
                        Glide.with(activity())
                                .load(Uri.fromFile(imgFile))
                                .apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.ic_user_placeholder))
                                .into(imgProfile);
                    }
                });
    }

    @Override
    public void onSuccessPhoneNumber(Object object) {
        isPhoneNumberAvailable = false;
    }

    @Override
    public void onVerifyPhoneNumberError(Throwable e) {
        isPhoneNumberAvailable = true;
        showErrorMessage(phoneNumber, getString(R.string.mobile_number_already_exist));
    }
}