package com.remoteyourcam.usb.ptp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat.Builder;
import com.remoteyourcam.usb.R;
import com.remoteyourcam.usb.util.NotificationIds;

public class NotifierStop {
    private final Notification notification;
    private final NotificationManager notificationManager;
    private final int uniqueId = NotificationIds.getInstance().getUniqueIdentifier(NotifierStop.class.getName() + ":running");

    public NotifierStop(Context context) {
        this.notificationManager = (NotificationManager) context.getSystemService("notification");
        this.notification = new Builder(context).setContentText(context.getString(R.string.worker_stop)).setWhen(System.currentTimeMillis()).setVibrate(new long[]{100, 200, 300, 400}).setContentTitle(context.getString(R.string.worker_content_title)).setOngoing(false).setSmallIcon(R.drawable.icon).build();
        this.notificationManager.notify(this.uniqueId, this.notification);
    }
}
