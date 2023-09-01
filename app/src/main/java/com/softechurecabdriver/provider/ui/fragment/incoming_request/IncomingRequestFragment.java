package com.softechurecabdriver.provider.ui.fragment.incoming_request;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import androidx.appcompat.widget.AppCompatRatingBar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.softechurecabdriver.provider.BuildConfig;
import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.base.BaseFragment;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.Utilities;
import com.softechurecabdriver.provider.data.network.model.Request_;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.softechurecabdriver.provider.MvpApplication.DATUM;
import static com.softechurecabdriver.provider.MvpApplication.mLastKnownLocation;
import static com.softechurecabdriver.provider.MvpApplication.time_to_left;

public class IncomingRequestFragment extends BaseFragment implements IncomingRequestIView {

    @BindView(R.id.btnReject)
    Button btnReject;
    @BindView(R.id.btnAccept)
    Button btnAccept;
    Unbinder unbinder;
    @BindView(R.id.lblCount)
    TextView lblCount;
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.lblUserName)
    TextView lblUserName;
    @BindView(R.id.ratingUser)
    AppCompatRatingBar ratingUser;
    @BindView(R.id.lblLocationName)
    TextView lblLocationName;
    @BindView(R.id.DestLocationName)
    TextView DestLocationName;
    @BindView(R.id.lblDrop)
    TextView lblDrop;
    @BindView(R.id.lblScheduleDate)
    TextView lblScheduleDate;
    @BindView(R.id.Distance)
    TextView Distance;
    @BindView(R.id.PaymentType)
    TextView PaymentType;
    @BindView(R.id.DistanceUser)
    TextView DistanceUser;
    @BindView(R.id.service_type)
    TextView serviceType;

    @BindView(R.id.dropp)
    LinearLayout drop_box;

    @BindView(R.id.non_rental)
    LinearLayout non_rental;
    @BindView(R.id.rental)
    LinearLayout rental;


    @BindView(R.id.rental_payment_mode)
    TextView rental_payment_mode;

    @BindView(R.id.rental_plan)
    TextView rental_plan;

    @BindView(R.id.EstimatedFare)
    TextView EstimatedFare;

    @BindView(R.id.rental_DistanceUser)
    TextView rental_DistanceUser;

    private IncomingRequestPresenter presenter = new IncomingRequestPresenter();
    private Context context;
    public static CountDownTimer countDownTimer;
    public static MediaPlayer mPlayer;
//    private Double estimatedFare;

    @Override
    public Fragment fragmentInstance() {
        return IncomingRequestFragment.this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_incoming_request;
    }

    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        context = getContext();
        presenter.attachView(this);
        mPlayer = MediaPlayer.create(context, R.raw.alert_tone);
        init();
        return view;
    }

    @SuppressLint("SetTextI18n")
    void init() {
        Request_ data = DATUM;
        if (data != null) {
            lblUserName.setText(String.format("%s %s", data.getUser().getFirstName(),
                    data.getUser().getLastName()));
            ratingUser.setRating(Float.parseFloat(data.getUser().getRating()));
            if (data.getSAddress() != null && !data.getSAddress().isEmpty()
                    || data.getDAddress() != null && !data.getDAddress().isEmpty()) {
                lblLocationName.setText(data.getSAddress());
                DestLocationName.setText(data.getDAddress());
                Distance.setText(String.valueOf(data.getDistance()) + "Km");
                Double e = data.getEstimatedFare();
                int i = e.intValue();
                EstimatedFare.setText(String.format("%s%s", Constants.Currency, i));
                lblDrop.setText(data.getDAddress());

                if(data.getService_required().equals("none"))
                    serviceType.setText("Daily Ride");
                else
                    serviceType.setText(data.getService_required());

                if(data.getService_required().equals("rental"))
                {
                    drop_box.setVisibility(View.GONE);
                    non_rental.setVisibility(View.GONE);
                    rental.setVisibility(View.VISIBLE);
                    serviceType.setText("Rental");
                    //TODO ALLAN - Alterações débito na máquina e voucher
                    if(data.getPaymentMode().equals("CASH")){
                        rental_payment_mode.setText("CASH");
                    }else if(data.getPaymentMode().equals("CARD")){
                        rental_payment_mode.setText("CARD");
                    }else if(data.getPaymentMode().equals("DEBIT_MACHINE")){
                        rental_payment_mode.setText("DEBIT MACHINE");
                    }else{
                        rental_payment_mode.setText(data.getPaymentMode());
                    }
                    if(data.getRentalPackage()!=null){
                    String plan = String.format("%s Hr / %sKm", data.getRentalPackage().getHour(), data.getRentalPackage().getKm());
                    rental_plan.setText(plan);}
                }else if(data.getService_required().equals("rental"))
                    serviceType.setText("Out Station");
                //TODO ALLAN - Alterações débito na máquina e voucher
                if(data.getPaymentMode().equals("CASH")){
                    PaymentType.setText("CASH");
                }else if(data.getPaymentMode().equals("CARD")){
                    PaymentType.setText("CARD");
                }else if(data.getPaymentMode().equals("DEBIT_MACHINE")){
                    PaymentType.setText("DEBIT MACHINE");
                }else{
                    PaymentType.setText(data.getPaymentMode());
                }

                //Seta distância entre motorista e passageiro
                Double distUser = getDistancia(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), data.getSLatitude(), data.getSLongitude());
                DistanceUser.setText(String.format(Locale.getDefault(), "%.2f", distUser) + "Km");
                rental_DistanceUser.setText(String.format(Locale.getDefault(), "%.2f", distUser) + "Km");


            }
            if (data.getUser().getPicture() != null)
                Glide.with(activity()).load(BuildConfig.BASE_IMAGE_URL + data.getUser()
                        .getPicture()).apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder)
                        .dontAnimate().error(R.drawable.ic_user_placeholder)).into(imgUser);
        }

        String isScheduled = data.getIsScheduled();
        String scheduledAt = data.getScheduleAt();
        if (isScheduled != null && isScheduled.equalsIgnoreCase("NO")) {
            lblScheduleDate.setVisibility(View.INVISIBLE);
        } else {
            if (scheduledAt != null && isScheduled.equalsIgnoreCase("YES")) {
                StringTokenizer tk = new StringTokenizer(scheduledAt);
                String date = tk.nextToken();
                String time = tk.nextToken();
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
                Date dt;
                try {
                    dt = sdf.parse(time);
                    lblScheduleDate.setVisibility(View.VISIBLE);
                    lblScheduleDate.setText(getString(R.string.schedule_for) + " " +
                            Utilities.convertDate(scheduledAt) + " " + sdfs.format(dt));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        countDownTimer = new CountDownTimer(time_to_left * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                lblCount.setText(String.valueOf(millisUntilFinished / 1000));
                setTvZoomInOutAnimation(lblCount);
            }

            public void onFinish() {
                try {
                    context.sendBroadcast(new Intent("INTENT_FILTER"));
                    mPlayer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        countDownTimer.start();
    }


    /**
     * CALCULA DISTÂNCIA ENTRE 2 PONTOS
     */
    public static double getDistancia(Double lat1, Double long1, Double lat2, Double long2) {
        // Conversão de graus pra radianos das latitudes
        double firstLatToRad = Math.toRadians(lat1);
        double secondLatToRad = Math.toRadians(lat2);

        // Diferença das longitudes
        double deltaLongitudeInRad = Math.toRadians(long2 - long1);

        // Cálcula da distância entre os pontos
        return Math.acos(Math.cos(firstLatToRad) * Math.cos(secondLatToRad)
                * Math.cos(deltaLongitudeInRad) + Math.sin(firstLatToRad)
                * Math.sin(secondLatToRad))
                * 6371;
    }


    private void setTvZoomInOutAnimation(final TextView textView) {
        final float startSize = 20;
        final float endSize = 13;
        final int animationDuration = 900; // Animation duration in ms

        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(animationDuration);

        animator.addUpdateListener(valueAnimator -> {
            float animatedValue = (Float) valueAnimator.getAnimatedValue();
            textView.setTextSize(animatedValue);
        });
        //animator.setRepeatCount(ValueAnimator.INFINITE);  // Use this line for infinite animations
        animator.setRepeatCount(2);
        animator.start();
    }


    @OnClick({R.id.btnReject, R.id.btnAccept})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnReject:
                if (DATUM != null) {
                    Request_ datum = DATUM;
                    showLoading();
                    presenter.cancel(datum.getId());
                    time_to_left = 60;
                }
                break;
            case R.id.btnAccept:
                if (DATUM != null) {
                    Request_ datum = DATUM;
                    showLoading();
                    presenter.accept(datum.getId());
                    time_to_left = 60;
                }
                break;
        }
    }

    @Override
    public void onSuccessAccept(Object message) {
        countDownTimer.cancel();
        hideLoading();

        if(message.toString().equals("{erreur=Fin de délai!}")){
            Toasty.error(getContext(), "Fin du délai. Appel terminé!", Toast.LENGTH_LONG).show();
        }else if(message.toString().equals("{erreur=Trajet accepté par un autre chauffeur!}")){
            Toasty.error(getContext(), "Trajet accepté par un autre chauffeur!", Toast.LENGTH_LONG).show();
        }else{
            Toasty.success(getContext(), getString(R.string.ride_accepted), Toast.LENGTH_LONG).show();
        }

        getContext().sendBroadcast(new Intent("INTENT_FILTER"));
        try {
            getActivity().getSupportFragmentManager().beginTransaction().remove(IncomingRequestFragment.this).commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessCancel(Object object) {
        countDownTimer.cancel();
        hideLoading();
        getActivity().getSupportFragmentManager().beginTransaction().remove(IncomingRequestFragment.this).commitAllowingStateLoss();
        Toasty.success(context, getString(R.string.ride_cancelled), Toast.LENGTH_SHORT, true).show();
        context.sendBroadcast(new Intent("INTENT_FILTER"));
    }

    @Override
    public void onError(Throwable e) {
        try {
            hideLoading();
            if (mPlayer.isPlaying()) mPlayer.stop();
            if (e != null)
                onErrorBase(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mPlayer.isPlaying())
            mPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer.isPlaying())
            mPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying())
            mPlayer.stop();
    }
}
