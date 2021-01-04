package com.example.myshoppingapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    private String Title = "Shopping Application";
    private String Text = "You have planed shopping list, don't forget!!";
    private int Id = 200;
    @Override
    public void onReceive(Context context, Intent intent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ShoppingApp")
                .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                .setContentTitle(Title)
                .setContentText(Text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(Id,builder.build());
    }
}
