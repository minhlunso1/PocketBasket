package minhna.android.giodicho.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import minhna.android.giodicho.receiver.AfterBootCompletedReceiver;
import minhna.android.giodicho.receiver.ShowAnnouncementReceiver;

/**
 * Created by Administrator on 14-Nov-15.
 */
public class AlarmUtils {

    public void setAnnouncement(Context context, int type, String title, String content, String link, long showTime){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ShowAnnouncementReceiver.class);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("link", link);
        intent.putExtra("showTime", showTime);
        PendingIntent pi = PendingIntent.getBroadcast(context, NotificationUtils.notiId++, intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC_WAKEUP, showTime, pi);
        //Log.d("debug", "oneAnnouncement:" + content);
    }

    public void cancelAlarm(Context context)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = null;
        intent = new Intent(context, ShowAnnouncementReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.cancel(sender);
    }

    public void enableBootingBroadcastReceiver(Context context){
        ComponentName receiver = new ComponentName(context, AfterBootCompletedReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void disableBootingBroadcastReceiver(Context context){
        ComponentName receiver = new ComponentName(context, AfterBootCompletedReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
