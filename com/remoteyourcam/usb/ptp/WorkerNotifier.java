package com.remoteyourcam.usb.ptp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat.Builder;
import com.remoteyourcam.usb.R;
import com.remoteyourcam.usb.ptp.Camera.WorkerListener;
import com.remoteyourcam.usb.util.NotificationIds;

public class WorkerNotifier implements WorkerListener {
    private final Notification notification;
    private final NotificationManager notificationManager;
    private final int uniqueId = NotificationIds.getInstance().getUniqueIdentifier(WorkerNotifier.class.getName() + ":running");

    public WorkerNotifier(Context context) {
        this.notificationManager = (NotificationManager) context.getSystemService("notification");
        this.notification = new Builder(context).setContentText(context.getString(R.string.worker_ticker)).setWhen(System.currentTimeMillis()).setVibrate(new long[]{100, 200, 300, 400}).setContentTitle(context.getString(R.string.worker_content_title)).setSmallIcon(R.drawable.icon).build();
    }

    public void onWorkerEnded() {
        this.notificationManager.cancel(this.uniqueId);
    }

    public void onWorkerStarted() {
        Notification notification = this.notification;
        notification.flags |= 32;
        this.notificationManager.notify(this.uniqueId, this.notification);
    }
}
