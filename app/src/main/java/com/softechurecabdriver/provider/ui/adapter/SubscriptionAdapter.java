package com.softechurecabdriver.provider.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.data.network.model.Subscriptions;
import com.softechurecabdriver.provider.ui.activity.subscription.SubscribeClickListener;
import com.softechurecabdriver.provider.ui.activity.subscription.SubscriptionActivity;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder> {
    private List<Subscriptions> subscriptions;
    private Context mContext;
    private int selectedPosition=0;
    private int[] colors;
    private int ColorIndex=0;
    private SubscribeClickListener subscribeClickListener;

    public SubscriptionAdapter(SubscriptionActivity subscriptionActivity, List<Subscriptions> subscriptions,SubscribeClickListener subscribeClickListener) {
      mContext=subscriptionActivity;
      this.subscriptions=subscriptions;
        colors= mContext.getResources().getIntArray(R.array.colors);
        this.subscribeClickListener=subscribeClickListener;
    }

    @NonNull
    @Override
    public SubscriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubscriptionViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_subscriptions,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(selectedPosition==position)
        {
           holder.clSubscribePlan.setBackgroundResource(R.drawable.shape_selected_supcrition_box);
        }else
        {
            holder.clSubscribePlan.setBackgroundResource(R.drawable.shape_supcrition_box);
        }
        if(ColorIndex<colors.length)
        {
          holder.clLayout.setBackgroundColor(colors[ColorIndex]);
          ColorIndex++;
        }else
        {
            ColorIndex=0;
            holder.clLayout.setBackgroundColor(colors[ColorIndex]);
        }

        if(subscriptions.get(position).isIs_subscribe())
        {
            holder.txtSubscribeBtn.setText(mContext.getString(R.string.subscribed));
            holder.txtSubscribeBtn.setEnabled(false);
            holder.txtSubscribeBtn.setClickable(false);
            holder.txtSubscribeBtn.setBackgroundColor(mContext.getColor(R.color.quantum_yellowA700));
            holder.imgSubscribed.setVisibility(View.VISIBLE);
        }else
        {
            holder.txtSubscribeBtn.setEnabled(true);
            holder.txtSubscribeBtn.setClickable(true);

            holder.txtSubscribeBtn.setText(mContext.getString(R.string.subscribe));
            holder.imgSubscribed.setVisibility(View.GONE);
        }
        holder.txtSubscriptionPrice.setText("₹ "+subscriptions.get(position).getAmount());
        holder.txtValidity.setText(subscriptions.get(position).getNo_of_days()+"Days");
        holder.txtSubscriptionName.setText(subscriptions.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorIndex=0;
                selectedPosition=position;
                notifyDataSetChanged();
            }
        });
        holder.txtSubscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorIndex=0;
                selectedPosition=position;
              subscribeClickListener.clickEvent(subscriptions.get(position).getId(),subscriptions.get(position).getAmount());
             notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubscriptionName,txtSubscriptionPrice,txtValidity,txtSubscribeBtn;
        ConstraintLayout clLayout;
        ImageView imgSubscribed;
        CardView clSubscribePlan;
        public SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSubscriptionName=itemView.findViewById(R.id.txtSubscriptionName);
            txtSubscriptionPrice=itemView.findViewById(R.id.txtSubscriptionPrice);
            txtValidity=itemView.findViewById(R.id.txtValidity);
            clLayout=itemView.findViewById(R.id.clLayout);
            txtSubscribeBtn=itemView.findViewById(R.id.txtSubscribeBtn);
            imgSubscribed=itemView.findViewById(R.id.imgSubscribed);
            clSubscribePlan=itemView.findViewById(R.id.clSubscribePlan);
        }
    }

}
