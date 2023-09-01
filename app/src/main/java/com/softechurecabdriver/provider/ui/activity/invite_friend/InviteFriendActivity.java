package com.softechurecabdriver.provider.ui.activity.invite_friend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.base.BaseActivity;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.SharedHelper;
import com.softechurecabdriver.provider.data.network.model.ReferralsData;
import com.softechurecabdriver.provider.data.network.model.UserResponse;
import com.softechurecabdriver.provider.ui.adapter.ReferralsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteFriendActivity extends BaseActivity implements InviteFriendIView {

    private InviteFriendPresenter<InviteFriendActivity> inviteFriendPresenter = new InviteFriendPresenter<>();

    @BindView(R.id.invite_friend)
    TextView invite_friend;
    @BindView(R.id.referral_code)
    TextView referral_code;
    @BindView(R.id.rvReferrals)
    RecyclerView rvReferrals;
    @BindView(R.id.referralTxt)
    TextView txtReferral;
    String inviteText="";

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        inviteFriendPresenter.attachView(this);

        if (!SharedHelper.getKey(this, Constants.ReferalKey.REFERRAL_CODE).equalsIgnoreCase(""))
            updateUI(Constants.referrals);
        else inviteFriendPresenter.profile();
    }

    private void updateUI(ArrayList<ReferralsData> referrals) {
        referral_code.setText(SharedHelper.getKey(this, Constants.ReferalKey.REFERRAL_CODE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            invite_friend.setText(Html.fromHtml(SharedHelper.getKey(this, Constants.ReferalKey.REFERRAL_TEXT), Html.FROM_HTML_MODE_COMPACT));
            inviteText= String.valueOf(Html.fromHtml(SharedHelper.getKey(this,  Constants.ReferalKey.REFERRAL_INVITE_TEXT), Html.FROM_HTML_MODE_COMPACT));
        } else {
            invite_friend.setText(Html.fromHtml(SharedHelper.getKey(this, Constants.ReferalKey.REFERRAL_TEXT)));
            inviteText= String.valueOf(Html.fromHtml(SharedHelper.getKey(this, Constants.ReferalKey.REFERRAL_INVITE_TEXT)));
        }


       showReferrals(referrals);
    }

    private void showReferrals(ArrayList<ReferralsData> referrals) {
        if(Constants.referrals.size()==0 && Constants.referrals==null)
        {
            txtReferral.setText(getString(R.string.referrals)+" : 0");
        }else
        {
            txtReferral.setText(getString(R.string.referrals));
            rvReferrals.setLayoutManager(new LinearLayoutManager(this));
            rvReferrals.setAdapter(new ReferralsAdapter(InviteFriendActivity.this,referrals));
        }
    }

    @Override
    public void onSuccess(UserResponse response) {
        SharedHelper.putKey(this, Constants.ReferalKey.REFERRAL_CODE, response.getReferral_unique_id());
        SharedHelper.putKey(this, Constants.ReferalKey.REFERRAL_COUNT, response.getReferral_count());
        SharedHelper.putKey(this, Constants.ReferalKey.REFERRAL_TEXT, response.getReferral_text());
        SharedHelper.putKey(this, Constants.ReferalKey.REFERRAL_INVITE_TEXT, response.getReferral_details());

        Log.d("Profile----->", "onSuccess: text---->"+ response.getReferral_text());
        Log.d("Profile----->", "onSuccess: total"+ response.getReferrals());
        Log.d("Profile----->", "onSuccess: count"+ response.getReferral_count());
        showReferrals(response.getReferrals());
        updateUI(response.getReferrals());
    }

    @Override
    public void onError(Throwable throwable) {
        onErrorBase(throwable);
    }

    @SuppressLint("StringFormatInvalid")
    @OnClick({R.id.share})
    public void onClickAction(View view) {
        switch (view.getId()) {
            case R.id.share:
                try {
                    String appName = getString(R.string.app_name);
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, appName);
                    i.putExtra(Intent.EXTRA_TEXT,inviteText);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
