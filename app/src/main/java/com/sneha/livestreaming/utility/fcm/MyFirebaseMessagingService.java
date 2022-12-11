package com.sneha.livestreaming.utility.fcm;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.sneha.livestreaming.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    String value, type;
    int count = 0;
    NotificationManager notificationManager;
    String image, productId, title, message;
    String random_id;
    public static int NOTIFICATION_ID = 0;
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            handleDataMessage(remoteMessage.getData());
        }else{
            handleDataMessage(remoteMessage.getData());
        }
    }



    private void handleDataMessage(Map<String, String> data) {

        image = data.get("image");
        title = data.get("title");
        message = data.get("message");
        productId = data.get("channels_id");
        random_id = data.get("random_id");
        //Log.d("placeId", placeId);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationUtils = new NotificationUtils(getApplicationContext());
            Notification.Builder nb = notificationUtils
                    .getAndroidChannelNotification(title, message, image, productId);
            notificationUtils.getManager().notify(102, nb.build());
        }else {
            handleMessage(getApplicationContext());
        }
    }

    @SuppressWarnings("deprecation")
    private void handleMessage(Context mContext) {
        Bitmap remote_picture = null;
        int icon = R.mipmap.ic_launcher;
        //if message and image url
        if (message != null && image != null) {
            try {

                NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
                notiStyle.setSummaryText(message);
                try {
                    remote_picture = BitmapFactory.decodeStream((InputStream) new URL(image).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                notiStyle.bigPicture(remote_picture);
                notificationManager = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent contentIntent = null;

                Intent gotoIntent = new Intent();
                gotoIntent.putExtra("id", productId);
                gotoIntent.setClassName(mContext, getApplicationContext().getPackageName()+".ui.SplashActivity");//Start activity when user taps on notification.
                contentIntent = PendingIntent.getActivity(mContext,
                        (int) (Math.random() * 100), gotoIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        mContext);
                Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                        .setLargeIcon(((BitmapDrawable) getResources().getDrawable(icon)).getBitmap())
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setPriority(1)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentIntent(contentIntent)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                        .setContentText(message)
                        .setStyle(notiStyle).build();


                notification.flags = Notification.FLAG_AUTO_CANCEL;
                count++;
                notificationManager.notify(count, notification);//This will generate seperate notification each time server sends.

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}

