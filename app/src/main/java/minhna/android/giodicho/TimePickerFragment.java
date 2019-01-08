package minhna.android.giodicho;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import minhna.android.giodicho.resources.Constant;
import minhna.android.giodicho.utils.AlarmUtils;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private DatePickerFragment datePickerFragment;

    public TimePickerFragment(DatePickerFragment datePickerFragment) {
        this.datePickerFragment = datePickerFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (datePickerFragment != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            String dateInString = datePickerFragment.getDay() + "-" + datePickerFragment.getMonth()
                    + "-" + datePickerFragment.getYear() + " " + hourOfDay + ":" + minute;
            Date date;
            try {
                date = sdf.parse(dateInString);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                long time = calendar.getTimeInMillis();
                Context context = getContext();
                if (time < System.currentTimeMillis())
                    Toast.makeText(context, context.getString(R.string.Not_allowed_past), Toast.LENGTH_SHORT).show();
                else {
                    if (datePickerFragment.getFlag() == 0) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREF_NAME, context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("reminder", time);
                        editor.putString("reminderName", datePickerFragment.getListName());
                        editor.apply();
                        new AlarmUtils().setAnnouncement(context.getApplicationContext(), 1, context.getString(R.string.Do_not_forget), datePickerFragment.getListName(), Constant.PLAY_APP, time);
                        datePickerFragment.setFlag(1);
                        Toast.makeText(context, context.getString(R.string.Setup_reminder_done), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
