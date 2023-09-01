package com.softechurecabdriver.provider.ui.activity.subscription;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.base.BaseActivity;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.SharedHelper;
import com.softechurecabdriver.provider.data.network.model.Subscriptions;
import com.softechurecabdriver.provider.data.network.model.SuccessResponse;
import com.softechurecabdriver.provider.ui.adapter.SubscriptionAdapter;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscriptionActivity extends BaseActivity implements SubscriptionView, PaymentResultListener {

    @BindView(R.id.rvSubscriptions)
    RecyclerView rvSubscriptions;
    private SubscriptionsPresenter<SubscriptionActivity> presenter = new SubscriptionsPresenter<>();
    private String subscription_id = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_subscription;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setTitle(getString(R.string.subscription));
        presenter.getSubscriptions(SharedHelper.getKey(this, Constants.SharedPref.ACCESS_TOKEN));

    }

    @Override
    public void onSuccess(List<Subscriptions> subscriptions) {
        SubscriptionAdapter adapter=new SubscriptionAdapter(this, subscriptions, new SubscribeClickListener() {
            @Override
            public void clickEvent(String subscriptionID, String amount) {
                subscription_id=subscriptionID;
                startPaymentRaz(subscriptionID,amount);
            }
        });
        rvSubscriptions.setLayoutManager(new LinearLayoutManager(this));
        rvSubscriptions.setAdapter(adapter);
    }
    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }


    @Override
    public void onPaySuccess(SuccessResponse successResponse) {
       presenter.getSubscriptions(SharedHelper.getKey(this, Constants.SharedPref.ACCESS_TOKEN));
    }

    public void startPaymentRaz(String subscriptionID, String amount) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID(SharedHelper.getKey(SubscriptionActivity.this, Constants.SharedPref.razorpay_key_id));
        try {
            JSONObject options = new JSONObject();
            options.put("name", Constants.userName);
            options.put("description", "Subscription Plan");
            options.put("currency", "INR");
            options.put("amount", Double.parseDouble(amount) * 100);
            JSONObject preFill = new JSONObject();
            preFill.put("email", Constants.userEmail);
            preFill.put("contact", Constants.userPhone);
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Log.d("Error---->", "startPaymentRaz: " + e.getMessage());
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

        @Override
        public void onPaymentSuccess (String s){
        presenter.subscribeSuccessfully(SharedHelper.getKey(this, Constants.SharedPref.ACCESS_TOKEN), subscription_id);
    }

        @Override
        public void onPaymentError ( int i, String s){
        Log.d("Error:", "onPaymentError: " + s);
    }

}