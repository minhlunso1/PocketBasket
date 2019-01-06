package minhna.android.giodicho.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import minhna.android.giodicho.MainActivity;
import minhna.android.giodicho.R;

/**
 * Created by Administrator on 20-Feb-16.
 */
public class NotificationUtils {

    private Context context;
    private NotificationCompat.Builder builder;
    public static int notiId = 0;

    public NotificationUtils(Context context){
        this.context = context;
        this.builder = new NotificationCompat.Builder(context);
    }

    public void sendNotification(int type, String title, String content, String link){
        builder
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(createPendingIntent(1));
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notiId++, builder.build());
    }

    public PendingIntent createPendingIntent(int type){
        if (type==1) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            return PendingIntent.getActivity(context, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }
        return null;
    }

}
