package minhna.android.giodicho;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import minhna.android.giodicho.resources.Constant;
import minhna.android.giodicho.utils.AlarmUtils;

public class DatePickerFragment extends DialogFragment
                            implements DatePickerDialog.OnDateSetListener {

    private int day;
    private int month;
    private int year;
    private static int flag;//api 18 bug android 4.3
    private String listName;
    private TimePickerFragment timeFragment;

    public static DatePickerFragment newInstance(String listName){
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("listName", listName);
        fragment.setArguments(bundle);
        return fragment;
    }

    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

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
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            String dateInString = day+"-"+month+"-"+year+" "+hourOfDay+":"+minute;
            Date date;
            try {
                date = sdf.parse(dateInString);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                long time = calendar.getTimeInMillis();
                Context context = getContext();
                if (time<System.currentTimeMillis())
                    Toast.makeText(context, context.getString(R.string.Not_allowed_past), Toast.LENGTH_SHORT).show();
                else {
                    if (flag==0) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREF_NAME, context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("reminder", time);
                        editor.putString("reminderName", listName);
                        editor.apply();
                        new AlarmUtils().setAnnouncement(context.getApplicationContext(), 1, context.getString(R.string.Do_not_forget), listName, Constant.PLAY_APP, time);
                        flag = 1;
                        Toast.makeText(context, context.getString(R.string.Setup_reminder_done), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.day = day;
        this.month = month + 1;
        this.year = year;
        this.listName = getArguments().getString("listName");
        flag = 0;
        if (timeFragment==null) {
            timeFragment = new TimePickerFragment();
            timeFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
        }
    }

}