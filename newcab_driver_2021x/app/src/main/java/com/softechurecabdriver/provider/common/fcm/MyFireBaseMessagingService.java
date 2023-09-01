package com.softechurecabdriver.provider.common.fcm;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.softechurecabdriver.provider.MvpApplication;
import com.softechurecabdriver.provider.R;
import com.softechurecabdriver.provider.common.Constants;
import com.softechurecabdriver.provider.common.SharedHelper;
import com.softechurecabdriver.provider.ui.activity.main.MainActivity;

import java.util.List;

@SuppressLint("InvalidWakeLockTag")
public class MyFireBaseMessagingService extends FirebaseMessagingService {

    public static final String INTENT_FILTER = "INTENT_FILTER";
    private static final String TAG = "RRRR Notification: ";
    private int notificationId = 0;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        @SuppressLint("HardwareIds")
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("DEVICE_ID: ", deviceId);
        Log.d("FCM_TOKEN", s);

        SharedHelper.putKeyFCM(this, Constants.SharedPref.DEVICE_TOKEN, s);
        SharedHelper.putKeyFCM(this, Constants.SharedPref.DEVICE_ID, deviceId);

    }
    String reqid="";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {
            String chat = null;

            // System.out.println(TAG + "From: " + remoteMessage.getFrom());
            // System.out.println(TAG + "getData: " + remoteMessage.getData());

            String message = remoteMessage.getData().get("message");
            reqid = remoteMessage.getData().get("request_id");

            try {
                chat = remoteMessage.getData().get("custom");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (TextUtils.isEmpty(message)) message = remoteMessage.getData().toString();

            if (message != null) {
                if (isLocked(getApplicationContext()) && (message.contains("RRRR") || message.equalsIgnoreCase("Nova viagem"))) {
                    sendNotificationProlonged(message);
                    Log.e("MG_TAG", "LONG NOT");
                } else {

                    createNotification(message,!TextUtils.isEmpty(chat));
                    Log.e("MG_TAG", "NORMAL NOT");
                }
            }

            System.out.println(TAG
                    + " message = " + message
                    + " status = Background-> " + isBackground(getApplicationContext())
                    + " isLocked ->" + isLocked(getApplicationContext())
                    + " is CallActive -> " + isCallActive(getApplicationContext()));

            if ((message.equalsIgnoreCase("Nova viagem") || message.contains("RRRR"))
                    && isBackground(getApplicationContext())
                    && !isLocked(getApplicationContext())
                    && !isCallActive(getApplicationContext())) restartApp();

            sendBroadcast(new Intent(INTENT_FILTER));

        } else System.out.println(TAG + "FCM Notification failed");


    }


    private void restartApp() {
        //  System.out.println("RRR MyFireBaseMessagingService.restartApp");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    Uri soundUri;

    public void createNotification(String message,Boolean isChat){

        MvpApplication.canGoToChatScreen = isChat;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity
                (this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.alert_tone);


        Intent accept = new Intent(this, NotificationReceiver.class);
        accept.setAction("accept");
        accept.putExtra("100", 0);
        Bundle noBundle = new Bundle();
        noBundle.putString("id", reqid);
        accept.putExtras(noBundle);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this, 0, accept, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent reject = new Intent(this, NotificationReceiver.class);
        reject.setAction("reject");
        reject.putExtra("100", 0);
        Bundle noBundlee = new Bundle();
        noBundlee.putString("id", reqid+"");
        reject.putExtras(noBundlee);
        PendingIntent rejectPendingIntent = PendingIntent.getBroadcast(this, 0, reject, PendingIntent.FLAG_UPDATE_CURRENT);




        NotificationCompat.Builder     mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

        mBuilder.setSound(soundUri);
        mBuilder.setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setColor(Color.GREEN)

                .setStyle(new NotificationCompat.BigTextStyle());
        if(reqid!=null){

            mBuilder.addAction(R.mipmap.ic_launcher,"Accept",acceptPendingIntent)
                    .addAction(R.mipmap.ic_launcher,"Reject",rejectPendingIntent);
        }

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager  mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("100", "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.s

            if(soundUri != null){
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();
                notificationChannel.setSound(soundUri,audioAttributes);
            }

            assert mNotificationManager != null;
            mBuilder.setChannelId("100");
            mNotificationManager.createNotificationChannel(notificationChannel);

        }

        assert mNotificationManager != null;
        if(reqid!=null)
            mNotificationManager.notify(Integer.parseInt(reqid) /* Request Code */, mBuilder.build());
        else {
            mNotificationManager.notify(1245 /* Request Code */, mBuilder.build());
        }

    }


    private void sendNotificationProlonged(String messageBody) {

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) ((PowerManager) getSystemService(POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE, "TAG").acquire(3 * 60 * 1000L /*3 minutes*/);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        String channelId = "prologedChannelID_1";//getString(R.string.default_notification_channel_id);
        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.alert_tone);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setTicker("Hearty365")
                .setContentIntent(pIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setSound(alarmSound)
                .setPriority(NotificationCompat.PRIORITY_MAX);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Audio attributes
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelId, "New Trip Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setDescription(messageBody);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableVibration(true);

            channel.setSound(alarmSound,attributes);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(notificationId++, notificationBuilder.build());
        } else {
            notificationManagerCompat.notify(notificationId++, notificationBuilder.build());
        }
    }

    private boolean isBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses)
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                    for (String activeProcess : processInfo.pkgList)
                        if (activeProcess.equals(context.getPackageName())) isInBackground = false;
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName()))
                isInBackground = false;
        }

        return isInBackground;
    }

    public boolean isCallActive(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getMode() == AudioManager.MODE_IN_CALL;
    }

    public boolean isLocked(Context context) {
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return myKM.isKeyguardLocked();
    }
}
