package com.softechurecabdriver.provider.ui.adapter;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softechurecabdriver.provider.MvpApplication;
import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.data.network.model.Ride;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


public class EarningsTripAdapter extends RecyclerView.Adapter<EarningsTripAdapter.MyViewHolder> {

    private List<Ride> list;

    public EarningsTripAdapter(List<Ride> list) {
        this.list = list;
    }

    public void setList(List<Ride> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_earnings, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Ride ride = list.get(position);
        holder.lblDistance.setText(ride.getDistance() + " Km");
        if (ride.getPayment() != null)
            holder.lblAmount.setText(Constants.Currency +
                    MvpApplication.getInstance().getNewNumberFormat(ride.getPayment().getProviderPay()));
        else
            holder.lblAmount.setText(Constants.Currency + "0.00");
        StringTokenizer tk = new StringTokenizer(ride.getAssignedAt());
        String date = tk.nextToken();
        String time = tk.nextToken();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt;
        try {
            dt = sdf.parse(time);
            holder.lblTime.setText(sdfs.format(dt));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView lblTime, lblDistance, lblAmount;

        private MyViewHolder(View view) {
            super(view);

            lblTime = (TextView) view.findViewById(R.id.lblTime);
            lblDistance = (TextView) view.findViewById(R.id.lblDistance);
            lblAmount = (TextView) view.findViewById(R.id.lblAmount);
        }
    }
}