package com.im.dictionary.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.im.dictionary.App;
import com.im.dictionary.R;
import com.im.dictionary.view.activity.MainActivity;

public class Notification {

    public static void showNotification(String title, String content) {

        Intent resultIntent = new Intent(App.getAppContext(), MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(App.getAppContext());
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        android.support.v4.app.NotificationCompat.Builder nB = new NotificationCompat
                .Builder(App.getAppContext())
                .setPriority(android.app.Notification.PRIORITY_MIN)
                .setSmallIcon(R.drawable.ic_menu)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.InboxStyle())
                .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) App.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify(7231032, nB.build());

    }
}
