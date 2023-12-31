package com.softechurecabdriver.provider.ui.activity.past_detail;

import static com.softechurecabdriver.provider.MvpApplication.DATUM_history;
import static com.softechurecabdriver.provider.MvpApplication.DATUM_history_detail;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.softechurecabdriver.provider.BuildConfig;
import com.softechurecabdriver.provider.MvpApplication;
import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.base.BaseActivity;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.Utilities;
import com.softechurecabdriver.provider.data.network.model.HistoryDetail;
import com.softechurecabdriver.provider.data.network.model.Payment;
import com.softechurecabdriver.provider.data.network.model.Rating;
import com.softechurecabdriver.provider.data.network.model.User_Past;
import com.softechurecabdriver.provider.ui.bottomsheetdialog.invoice_show.InvoiceShowDialogFragment;
import com.softechurecabdriver.provider.ui.fragment.dispute.DisputeCallBack;
import com.softechurecabdriver.provider.ui.fragment.dispute.DisputeFragment;
import com.softechurecabdriver.provider.ui.fragment.dispute_status.DisputeStatusFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PastTripDetailActivity extends BaseActivity implements PastTripDetailIView, DisputeCallBack {

    @BindView(R.id.static_map)
    ImageView staticMap;
    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.first_name)
    TextView firstName;
    @BindView(R.id.rating)
    AppCompatRatingBar ratingBar;
    @BindView(R.id.finished_at)
    TextView finishedAt;
    @BindView(R.id.booking_id)
    TextView bookingId;
    @BindView(R.id.payment_mode)
    TextView paymentMode;
    @BindView(R.id.payable)
    TextView payable;
    @BindView(R.id.user_comment)
    TextView userComment;
    @BindView(R.id.view_receipt)
    Button viewReceipt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lblSource)
    TextView lblSource;
    @BindView(R.id.lblDestination)
    TextView lblDestination;
    @BindView(R.id.payment_image)
    ImageView paymentImage;
    @BindView(R.id.finished_at_time)
    TextView finishedAtTime;

    @BindView(R.id.textView2)
    TextView textView2;

    private PastTripDetailPresenter presenter = new PastTripDetailPresenter();
    private boolean isDisputeCreated;
    private HistoryDetail datum;

    @Override
    public int getLayoutId() {
        return R.layout.activity_past_trip_detail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.past_trip_detail));
        if (DATUM_history != null) {
            Utilities.printV("ID===>", DATUM_history.getId() + "");
            presenter.getPastTripDetail(String.valueOf(DATUM_history.getId()));
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof DisputeFragment) ((DisputeFragment) fragment).setCallBack(this);
    }

    void initPayment(String paymentMode) {

        switch (paymentMode) {
            case Constants.PaymentMode.CASH:
                paymentImage.setImageResource(R.drawable.ic_money);
                this.paymentMode.setText(getString(R.string.cash));
                break;
            case Constants.PaymentMode.CARD:
                paymentImage.setImageResource(R.drawable.ic_card);
                this.paymentMode.setText(getString(R.string.card));
                break;
            case Constants.PaymentMode.ONLINE:
                paymentImage.setImageResource(R.drawable.ic__online);
                this.paymentMode.setText(getString(R.string.online));
                break;
            case Constants.PaymentMode.PAYPAL:
                this.paymentMode.setText(getString(R.string.paypal));
                break;
            case Constants.PaymentMode.WALLET:
                this.paymentMode.setText(getString(R.string.wallet));
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.view_receipt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_receipt:
                InvoiceShowDialogFragment invoiceDialogFragment = new InvoiceShowDialogFragment();
                invoiceDialogFragment.show(getSupportFragmentManager(), invoiceDialogFragment.getTag());
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSuccess(HistoryDetail historyDetail) {

        Utilities.printV("onSuccess==>", historyDetail.getBookingId());
        DATUM_history_detail = historyDetail;

        datum = historyDetail;

        bookingId.setText(historyDetail.getBookingId());

        textView2.setText("Type :"+getNameResult(historyDetail.getServiceType().getName()));


        String strCurrentDate = historyDetail.getFinishedAt();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat timeFormat;
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
            format = new SimpleDateFormat("dd MMM yyyy");
            timeFormat = new SimpleDateFormat("hh:mm a");
            String date = format.format(newDate);
            String time = timeFormat.format(newDate);
            finishedAt.setText(date);
            finishedAtTime.setText(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        lblSource.setText(historyDetail.getSAddress());
        lblDestination.setText(historyDetail.getDAddress());
        Glide.with(activity())
                .load(historyDetail.getStaticMap())
                .apply(RequestOptions
                        .placeholderOf(R.drawable.backride)
                        .dontAnimate()
                        .error(R.drawable.backride))
                .into(staticMap);

        isDisputeCreated = historyDetail.getDispute() != null;

        initPayment(historyDetail.getPaymentMode());

        User_Past user = historyDetail.getUser();
        if (user != null) {
            firstName.setText(user.getFirstName() + " " + user.getLastName());
            Glide.with(activity()).load(BuildConfig.BASE_IMAGE_URL +
                    user.getPicture()).apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder).
                    dontAnimate().error(R.drawable.ic_user_placeholder)).into(avatar);
        }
        Payment payment = historyDetail.getPayment();
        if (payment != null) if (payment.getTotal() == 0 || payment.getTotal() == 0.0)
            payable.setVisibility(View.GONE);
        else {
            payable.setVisibility(View.VISIBLE);
            payable.setText(Constants.Currency +
                    MvpApplication.getInstance().getNewNumberFormat(payment.getTotal()));
        }

        Utilities.printV("===>", historyDetail.getRating().getUserComment() + "");

        Rating rating = historyDetail.getRating();
        if (rating != null) {
            userComment.setText(rating.getUserComment());
            ratingBar.setRating(Float.parseFloat(String.valueOf(rating.getUserRating())));
        }
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e != null)
            onErrorBase(e);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trip_help_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem disputeMenuItem = menu.findItem(R.id.action_dispute);
        if (isDisputeCreated) disputeMenuItem.setTitle(getString(R.string.dispute_status));
        else disputeMenuItem.setTitle(getString(R.string.dispute));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_dispute:
                if (isDisputeCreated) {
                    DisputeStatusFragment disputeDetailFragment = DisputeStatusFragment.newInstance(datum);
                    disputeDetailFragment.show(getSupportFragmentManager(), disputeDetailFragment.getTag());
                } else {
                    DisputeFragment disputeFragment = new DisputeFragment();
                    disputeFragment.show(getSupportFragmentManager(), disputeFragment.getTag());
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDisputeCreated() {
        presenter.getPastTripDetail(String.valueOf(DATUM_history.getId()));
    }
}
