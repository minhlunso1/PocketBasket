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
    private int flag;
    private String listName;
    private TimePickerFragment timeFragment;

    public static DatePickerFragment newInstance(String listName){
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("listName", listName);
        fragment.setArguments(bundle);
        return fragment;
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
            timeFragment = new TimePickerFragment(this);
            timeFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
        }
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getListName() {
        return listName;
    }
}