package com.app.minyaneto_android.fragments.add_minyan_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.Minyan;
import com.app.minyaneto_android.models.minyan.ExactTime;
import com.app.minyaneto_android.models.minyan.PrayDayType;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.minyan.RelativeTime;
import com.app.minyaneto_android.models.minyan.RelativeTimeType;
import com.app.minyaneto_android.models.minyan.Time;

public class AddMinyanFragment extends Fragment {

    private Spinner spinnerPrayType;
    private EditText etMinutes;
    private Spinner spinnerRelativeTimeType;
    private TimePicker timePicker;
    private CheckBox cbEveryDay;
    private CheckBox cbSunday;
    private CheckBox cbMonday;
    private CheckBox cbTuesday;
    private CheckBox cbWednesday;
    private CheckBox cbThursday;
    private CheckBox cbFriday;
    private CheckBox cbSaterday;
    private Button btnAddMinyn;
    private LinearLayout linearLayoutRelativeTime;
    boolean inRelativeTimeMode;

    public AddMinyanFragment() {
        // Required empty public constructor
    }

    public static AddMinyanFragment newInstance() {
        AddMinyanFragment fragment = new AddMinyanFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerPrayType = (Spinner) view.findViewById(R.id.add_minyan_pray_type_spinner);
        etMinutes = (EditText) view.findViewById(R.id.add_minyan_minutes);
        spinnerRelativeTimeType = (Spinner) view.findViewById(R.id.add_minyan_day_times);
        cbEveryDay = (CheckBox) view.findViewById(R.id.add_minyan_every_day);
        cbSunday = (CheckBox) view.findViewById(R.id.add_minyan_sunday);
        cbMonday = (CheckBox) view.findViewById(R.id.add_minyan_monday);
        cbTuesday = (CheckBox) view.findViewById(R.id.add_minyan_tuesday);
        cbWednesday = (CheckBox) view.findViewById(R.id.add_minyan_wednesday);
        cbThursday = (CheckBox) view.findViewById(R.id.add_minyan_thursday);
        cbFriday = (CheckBox) view.findViewById(R.id.add_minyan_friday);
        cbSaterday = (CheckBox) view.findViewById(R.id.add_minyan_saterday);
        btnAddMinyn = (Button) view.findViewById(R.id.add_minyan_btn);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        linearLayoutRelativeTime = (LinearLayout) view.findViewById(R.id.liner_layout_relative_time);

        timePicker.setVisibility(View.INVISIBLE);
        linearLayoutRelativeTime.setVisibility(View.INVISIBLE);
        RadioGroup f = (RadioGroup) view.findViewById(R.id.radio_group_add_minyan);
        f.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.add_minyan_exact_time:
                        timePicker.setVisibility(View.VISIBLE);
                        linearLayoutRelativeTime.setVisibility(View.INVISIBLE);
                        inRelativeTimeMode = false;
                        break;
                    case R.id.add_minyan_relative_time:
                        timePicker.setVisibility(View.INVISIBLE);
                        linearLayoutRelativeTime.setVisibility(View.VISIBLE);
                        inRelativeTimeMode = true;
                        break;
                    default:
                        break;
                }
            }
        });

        btnAddMinyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMinyan();
            }
        });

        spinnerRelativeTimeType.setAdapter(new ArrayAdapter<RelativeTimeType>(getContext(), R.layout.support_simple_spinner_dropdown_item, RelativeTimeType.values()));
        spinnerPrayType.setAdapter(new ArrayAdapter<PrayType>(getContext(), R.layout.support_simple_spinner_dropdown_item, PrayType.values()));
    }


    private void addMinyan() {
        Time time = null;
        if (inRelativeTimeMode) {
            time = new RelativeTime((RelativeTimeType)spinnerRelativeTimeType.getSelectedItem() ,Integer.parseInt(etMinutes.getText().toString()));
        }
        else {
            time= new ExactTime(timePicker.getHour(), timePicker.getMinute());
        }

        Minyan minyan=new Minyan((PrayType)spinnerPrayType.getSelectedItem(),time,PrayDayType.SUNDAY);
        //TODO add  mintan to server
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_minyan, container, false);
    }
}
