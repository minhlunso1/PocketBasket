package minhna.android.giodicho.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import minhna.android.giodicho.R;
import minhna.android.giodicho.resources.Constant;
import minhna.android.giodicho.utils.AlarmUtils;

public class AfterBootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmUtils alarmUtils = new AlarmUtils();
            alarmUtils.cancelAlarm(context);
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREF_NAME, context.MODE_PRIVATE);
            if (sharedPreferences.getLong("reminder", 0) > System.currentTimeMillis())
                alarmUtils.setAnnouncement(context, 1, context.getString(R.string.Do_not_forget), sharedPreferences.getString("reminderName", "PocketBasket"), Constant.PLAY_APP, sharedPreferences.getLong("reminder", 0));
        }

    }
}
