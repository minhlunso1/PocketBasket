package minhna.android.giodicho;

import android.app.Application;
import android.content.SharedPreferences;

import minhna.android.giodicho.resources.Constant;
import minhna.android.giodicho.utils.AlarmUtils;

/**
 * Created by Administrator on 20-Feb-16.
 */
public class PocketBasketApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        AlarmUtils alarmUtils = new AlarmUtils();
//        alarmUtils.cancelAlarm(this);
//        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREF_NAME, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        if (sharedPreferences.getLong("reminder", 0)!=0)
//            alarmUtils.setAnnouncement(this, 1, getString(R.string.Do_not_forget), sharedPreferences.getString("reminderName", "PocketBasket"), Constant.PLAY_APP, sharedPreferences.getLong("reminder", 0));
    }

}
