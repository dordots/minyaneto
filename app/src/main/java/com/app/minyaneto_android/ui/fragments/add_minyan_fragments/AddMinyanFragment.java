package com.app.minyaneto_android.ui.fragments.add_minyan_fragments;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.ui.acivities.MainActivity;
import com.app.minyaneto_android.models.minyan.ExactTime;
import com.app.minyaneto_android.models.minyan.Minyan;
import com.app.minyaneto_android.models.minyan.PrayDayType;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.minyan.RelativeTime;
import com.app.minyaneto_android.models.minyan.RelativeTimeType;
import com.app.minyaneto_android.models.minyan.Time;

import java.util.ArrayList;

public class AddMinyanFragment extends Fragment {

    private Spinner spinnerPrayType;
    private EditText etMinutes;
    private Spinner spinnerRelativeTimeType;
    private TimePicker timePicker;
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
        timePicker.setIs24HourView(true);
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
        ((RadioButton) view.findViewById(R.id.add_minyan_exact_time)).setChecked(true);
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
            if (etMinutes.getText().toString().equals("")) {
                Toast.makeText(getContext(), getResources().getString(R.string.check), Toast.LENGTH_SHORT).show();
                return;
            }
            time = new RelativeTime((RelativeTimeType) spinnerRelativeTimeType.getSelectedItem(), Integer.parseInt(etMinutes.getText().toString()));
        } else {
            time = new ExactTime(timePicker.getHour(), timePicker.getMinute());
        }
        ArrayList<PrayDayType> days = new ArrayList<>();

        if (cbSunday.isChecked()) {
            days.add(PrayDayType.SUNDAY);
        }
        if (cbMonday.isChecked()) {
            days.add(PrayDayType.MONDAY);
        }
        if (cbTuesday.isChecked()) {
            days.add(PrayDayType.TUESDAY);
        }
        if (cbWednesday.isChecked()) {
            days.add(PrayDayType.WEDNESDAY);
        }
        if (cbThursday.isChecked()) {
            days.add(PrayDayType.THURSDAY);
        }
        if (cbFriday.isChecked()) {
            days.add(PrayDayType.FRIDAY);
        }
        if (cbSaterday.isChecked()) {
            days.add(PrayDayType.SATURDAY);
        }


        Minyan minyan = new Minyan();
        minyan.setPrayType((PrayType) spinnerPrayType.getSelectedItem());
        minyan.setTime(time);
        //TODO sent new minyan to server for all the days
        minyan.setPrayDayType(days.get(0));

        //TODO add  minyan to server

        //TODO return to back fragment
        // getActivity().onBackPressed();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity())
                    .setActionBarTitle(getResources().getString(R.string.add_minyan_fragment));
        }
        return inflater.inflate(R.layout.fragment_add_minyan, container, false);
    }
}
