package minhna.android.giodicho.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;

import minhna.android.giodicho.R;
import minhna.android.giodicho.resources.Constant;
import minhna.android.giodicho.utils.NotificationUtils;

/**
 * Created by Administrator on 19-Nov-15.
 */
public class ShowAnnouncementReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "one_announcement");
        wakeLock.acquire();
        //Log.d("debug", "ShowAnnouncementReceiver");

        Bundle bundle = intent.getExtras();
        if (bundle!=null) {
            int type = 0;
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREF_NAME, context.MODE_PRIVATE);
            String title = context.getString(R.string.Do_not_forget);
            String content = sharedPreferences.getString("reminderName", "PocketBasket");
            String link="";
            type = bundle.getInt("type");
            if (bundle.getString("title")!=null)
                title = bundle.getString("title");
            if (bundle.getString("content")!=null)
                content = bundle.getString("content");
            if (bundle.getString("link")!=null)
                link = bundle.getString("link");
            new NotificationUtils(context).sendNotification(type, title, content, link);
        }

        wakeLock.release();
    }
}
