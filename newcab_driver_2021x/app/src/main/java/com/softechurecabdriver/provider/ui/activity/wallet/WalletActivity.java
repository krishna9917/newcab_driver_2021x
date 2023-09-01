package com.softechurecabdriver.provider.ui.activity.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.base.BaseActivity;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.SharedHelper;
import com.softechurecabdriver.provider.data.network.model.WalletMoneyAddedResponse;
import com.softechurecabdriver.provider.data.network.model.WalletResponse;
import com.softechurecabdriver.provider.ui.activity.payment.PaymentActivity;
import com.softechurecabdriver.provider.ui.activity.request_money.RequestMoneyActivity;
import com.softechurecabdriver.provider.ui.adapter.WalletAdapter;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.softechurecabdriver.provider.ui.activity.payment.PaymentActivity.PICK_PAYMENT_METHOD;

public class WalletActivity extends BaseActivity implements WalletIView {

    String TAG = "WalletActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvWalletAmt)
    TextView tvWalletAmt;
    @BindView(R.id.rvWalletData)
    RecyclerView rvWalletData;
    @BindView(R.id.tvWalletPlaceholder)
    TextView tvWalletPlaceholder;
    @BindView(R.id.llWalletHistory)
    LinearLayout llWalletHistory;
    @BindView(R.id.ivRequestMoney)
    TextView ivRequestMoney;
    @BindView(R.id.addAmount)
    Button addAmount;
    @BindView(R.id.etRequestAmt)
    EditText etRequestAmt;
    private WalletPresenter mPresenter = new WalletPresenter();
    private double walletAmt;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        etRequestAmt.setKeyListener(new DigitsInputFilter());
        mPresenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.wallet));
        showLoading();
        if (SharedHelper.getIntKey(this, "card") == 0 || SharedHelper.getIntKey(this, "online") == 0) ivRequestMoney.setVisibility(View.GONE);
        else ivRequestMoney.setVisibility(View.VISIBLE);
        mPresenter.getWalletData();
        rvWalletData.setLayoutManager(new LinearLayoutManager(activity(), LinearLayoutManager.VERTICAL, false));
        rvWalletData.setItemAnimator(new DefaultItemAnimator());
        rvWalletData.setHasFixedSize(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.addAmount)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addAmount:
                if (etRequestAmt.getText().toString().trim().isEmpty()) {
                    Toast.makeText(activity(), getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show();
                    return;
                } else if (Float.parseFloat(etRequestAmt.getText().toString().trim()) == 0) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.valid_amount), Toast.LENGTH_SHORT)
                            .show();
                    return;
                } else {
                    if(SharedHelper.getIntKey(this, "online") != 0){
                        startPaymentRaz();
                    }else {
                        Intent intent = new Intent(activity(), PaymentActivity.class);
                        intent.putExtra("hideCash", true);
                        startActivityForResult(intent, PICK_PAYMENT_METHOD);
                    }
                }
                break;

            default:
                break;
        }
    }

    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            addMoney2();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    private void addMoney2() {

        showLoading();
        mPresenter.addMoneyRaz(etRequestAmt.getText().toString());
    }

    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    @Override
    public void onSuccess(WalletResponse response) {
        hideLoading();
        walletAmt = response.getWalletBalance();
        tvWalletAmt.setText(Constants.Currency + walletAmt);
        if (response.getWalletTransation() != null && response.getWalletTransation().size() > 0) {
            rvWalletData.setAdapter(new WalletAdapter(response.getWalletTransation()));
            llWalletHistory.setVisibility(View.VISIBLE);
            tvWalletPlaceholder.setVisibility(View.GONE);
        } else {
            llWalletHistory.setVisibility(View.GONE);
            tvWalletPlaceholder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(WalletMoneyAddedResponse response) {
        showLoading();
        etRequestAmt.setText(null);
        mPresenter.getWalletData();
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @OnClick(R.id.ivRequestMoney)
    public void onViewClicked() {
        startActivity(new Intent(this, RequestMoneyActivity.class).putExtra("WalletAmt", walletAmt));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PAYMENT_METHOD && resultCode == Activity.RESULT_OK && data != null)
            if (data.getStringExtra("payment_mode").equals("CARD")) {
                HashMap<String, Object> map = new HashMap<>();
                String cardId = data.getStringExtra("card_id");
                map.put("amount", etRequestAmt.getText().toString());
                map.put("card_id", cardId);
                map.put("payment_mode", "CARD");
                map.put("user_type", "provider");
                showLoading();
                mPresenter.addMoney(map);
            }
    }

    public class DigitsInputFilter extends DigitsKeyListener {

        private int decimalPlaces = 2;

        DigitsInputFilter() {
            super(false, true);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            CharSequence out = super.filter(source, start, end, dest, dstart, dend);
            if (out != null) {
                source = out;
                start = 0;
                end = out.length();
            }

            int length = end - start;
            if (length == 0)
                return source;

            if (dend == 0 && source.toString().equals("."))
                return "";

            int destLength = dest.length();
            for (int i = 0; i < dstart; i++) {
                if (dest.charAt(i) == '.')
                    return (destLength - (i + 1) + length > decimalPlaces) ?
                            "" : new SpannableStringBuilder(source, start, end);
            }

            for (int i = start; i < end; ++i) {
                if (source.charAt(i) == '.') {
                    if ((destLength - dend) + (end - (i + 1)) > decimalPlaces)
                        return "";
                    else
                        break;
                }
            }

            return new SpannableStringBuilder(source, start, end);
        }
    }

    String name;
    String email;
    String phone;
    public void startPaymentRaz() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();

                name=Constants.userName;
                email=Constants.userEmail;
                phone=Constants.userPhone;


            options.put("name", name);
            options.put("description", "Wallet ReCharges");
            //You can omit the image option to fetch the image from dashboard
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", Double.parseDouble(etRequestAmt.getText().toString())*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact",phone);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }
}
