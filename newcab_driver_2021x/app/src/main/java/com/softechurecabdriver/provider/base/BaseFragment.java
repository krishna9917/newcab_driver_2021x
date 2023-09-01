package com.softechurecabdriver.provider.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softechurecabdriver.provider.common.SharedHelper;
import com.softechurecabdriver.provider.data.network.model.Name;
import com.softechurecabdriver.provider.data.network.model.User;
import com.softechurecabdriver.provider.ui.bottomsheetdialog.invoice_flow.InvoiceDialogFragment;
import com.softechurecabdriver.provider.ui.fragment.incoming_request.IncomingRequestFragment;
import com.softechurecabdriver.provider.ui.fragment.status_flow.StatusFlowFragment;
import com.softechurecabdriver.provider.ui.fragment.upcoming.UpcomingTripFragment;
import com.softechurecabdriver.provider.ui.fragment.past.PastTripFragment;
import com.softechurecabdriver.provider.ui.fragment.offline.OfflineFragment;
import com.softechurecabdriver.provider.ui.fragment.dispute_status.DisputeStatusFragment;
import com.softechurecabdriver.provider.ui.bottomsheetdialog.rating.RatingDialogFragment;

public abstract class BaseFragment extends Fragment implements MvpView {

    protected View view;
    protected Lottiedialogfragment progressDialog;

    private BaseActivity mActivity;


    public abstract int getLayoutId();

    public abstract View initView(View view);

    public abstract Fragment fragmentInstance();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(getLayoutId(), container, false);
            if (fragmentInstance() instanceof IncomingRequestFragment ||
                    fragmentInstance() instanceof StatusFlowFragment ||
                    fragmentInstance() instanceof UpcomingTripFragment ||
                    fragmentInstance() instanceof PastTripFragment ||
                    fragmentInstance() instanceof OfflineFragment ||
                    fragmentInstance() instanceof DisputeStatusFragment ||
                    fragmentInstance() instanceof RatingDialogFragment){
                initView(view);
            }
        }

//        progressDialog = new ProgressDialog(activity());
        progressDialog = new Lottiedialogfragment(getActivity());
    //    progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // atualiza os dados apos a view ter sido criada
        if (fragmentInstance() instanceof InvoiceDialogFragment){
            showLoading();
            // Delay de 5 segs até exibir o valor para pagamento no fragment
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    initView(view);
                }
            }, 5000);
        }
    }
    public String getNameResult(Name value){

        String language=  SharedHelper.getKey(getContext(),"lang_code","English");
        if (language.equalsIgnoreCase("English")){
            return value.getEn();
        }else if (language.equalsIgnoreCase("Russian")){
            return value.getRu();
        }else if (language.equalsIgnoreCase("Azerbaijan")){
            return value.getAz();
        }
        return value.getEn();
    }
    @Override
    public FragmentActivity activity() {
        return getActivity();
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    public void onErrorBase(Throwable e) {
        if (mActivity != null) {
            mActivity.onErrorBase(e);
        }
    }

    @Override
    public void onSuccessRefreshToken(User user) {
        if (mActivity != null) {
            mActivity.onSuccessRefreshToken(user);
        }
    }

    @Override
    public void onErrorRefreshToken(Throwable throwable) {
        if (mActivity != null) {
            mActivity.onErrorRefreshToken(throwable);
        }
    }

    @Override
    public void onSuccessLogout(Object object) {
        if (mActivity != null) {
            mActivity.onSuccessLogout(object);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (mActivity != null) {
            mActivity.onError(throwable);
        }
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }
}
