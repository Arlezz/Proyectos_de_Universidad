package com.example.marketplaceproyect.controller;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.marketplaceproyect.R;
import com.example.marketplaceproyect.activities.homeActivity;
import com.example.marketplaceproyect.modelos.MyNotification;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Mensaje recivido");
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        System.out.println("TITULO: " + remoteMessage.getData().get("title") + "\n" + "MENSAJE: " + remoteMessage.getData().get("body"));
        String title = remoteMessage.getData().get("title");
        String msg = remoteMessage.getData().get("body");
        sendNotification(title, msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendNotification(String title, String msg) {
        Intent intent = new Intent(this, homeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, MyNotification.NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);

        MyNotification notification = new MyNotification(this, MyNotification.CHANNEL_ID_NOTIFICATIONS);


        notification.build(R.drawable.money, title, msg, pendingIntent);
        notification.addChannel("Notificaciones", NotificationManager.IMPORTANCE_DEFAULT);
        notification.createChannelGroup(MyNotification.CHANNEL_GROUP_GENERAL, R.string.fcm_fallback_notification_channel_label);
        notification.show(MyNotification.NOTIFICATION_ID);
    }
}
