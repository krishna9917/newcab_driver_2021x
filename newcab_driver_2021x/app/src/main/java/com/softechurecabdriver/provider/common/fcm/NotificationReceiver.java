package com.softechurecabdriver.provider.common.fcm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.softechurecabdriver.provider.data.network.APIClient;
import com.softechurecabdriver.provider.ui.activity.main.MainActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equalsIgnoreCase("accept"))
        {
            if(intent.getExtras().getString("id")==null)
                return;
            APIClient.getAPIClient().acceptRequest("",Integer.parseInt(intent.getExtras().getString("id"))).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

                    .subscribe(new Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Object o) {

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancel(Integer.parseInt(intent.getExtras().getString("id")));
                        }
                    });
        }
        else {

            if(intent.getExtras().getString("id")==null)
                return;
            APIClient.getAPIClient().rejectRequest(Integer.parseInt(intent.getExtras().getString("id"))).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

                    .subscribe(new Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Object o) {


                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                          NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancel(Integer.parseInt(intent.getExtras().getString("id")));
                        }
                    });

        }

    }
}