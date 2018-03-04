package com.app.minyaneto_android.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.app.minyaneto_android.data.DataTransformer;
import com.app.minyaneto_android.models.domain.MinyanScheduleDomain;
import com.app.minyaneto_android.models.domain.SynagogueCache;
import com.app.minyaneto_android.models.domain.SynagogueDomain;
import com.app.minyaneto_android.models.domain.SynagoguesSource;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.time.ExactTime;
import com.app.minyaneto_android.models.time.PrayTime;
import com.app.minyaneto_android.models.time.RelativeTime;
import com.app.minyaneto_android.models.time.RelativeTimeType;
import com.app.minyaneto_android.restApi.ResponseListener;
import com.app.minyaneto_android.restApi.RestAPIUtility;
import com.app.minyaneto_android.utilities.SynagogueUtils;

import java.util.ArrayList;

public class AddMinyanFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = AddMinyanFragment.class.getSimpleName();
    private static SynagogueDomain mSynagogue;
    private static DataTransformer transformer;
    boolean inRelativeTimeMode;
    private Spinner spinnerPrayType;
    private EditText etMinutes;
    private Spinner spinnerRelativeTimeType;
    private Button btnAddMinyan;
    private TimePicker timePicker;
    private CheckBox cbSunday;
    private CheckBox cbMonday;
    private CheckBox cbTuesday;
    private CheckBox cbWednesday;
    private CheckBox cbThursday;
    private CheckBox cbFriday;
    private CheckBox cbSaturday;
    private LinearLayout linearLayoutRelativeTime;
    private AddMinyanListener mListener;

    public AddMinyanFragment() {
    }

    public static AddMinyanFragment newInstance(String id) {
        mSynagogue = SynagogueCache.getInstance().getSynagogue(id);
        transformer = new DataTransformer();
        return new AddMinyanFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerPrayType = view.findViewById(R.id.add_minyan_pray_type_spinner);
        etMinutes = view.findViewById(R.id.add_minyan_minutes);
        spinnerRelativeTimeType = view.findViewById(R.id.add_minyan_day_times);
        cbSunday = view.findViewById(R.id.add_minyan_sunday);
        cbMonday = view.findViewById(R.id.add_minyan_monday);
        cbTuesday = view.findViewById(R.id.add_minyan_tuesday);
        cbWednesday = view.findViewById(R.id.add_minyan_wednesday);
        cbThursday = view.findViewById(R.id.add_minyan_thursday);
        cbFriday = view.findViewById(R.id.add_minyan_friday);
        cbSaturday = view.findViewById(R.id.add_minyan_saterday);
        btnAddMinyan = view.findViewById(R.id.add_minyan_btn);
        timePicker = view.findViewById(R.id.timePicker);
        linearLayoutRelativeTime = view.findViewById(R.id.liner_layout_relative_time);

        timePicker.setVisibility(View.INVISIBLE);
        timePicker.setIs24HourView(true);
        linearLayoutRelativeTime.setVisibility(View.INVISIBLE);
        RadioGroup f = view.findViewById(R.id.radio_group_add_minyan);
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
        btnAddMinyan.setOnClickListener(this);

        ArrayList<String> prayTypeNames = new ArrayList<>(PrayType.values().length);
        for (PrayType prayType : PrayType.values()) {
            prayTypeNames.add(SynagogueUtils.getTextFromEnum(getContext(), prayType));
        }
        spinnerPrayType.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, prayTypeNames));
        ArrayList<String> relativeTimeTypeNames = new ArrayList<>(RelativeTimeType.values().length);
        for (RelativeTimeType relativeTimeType : RelativeTimeType.values()) {
            relativeTimeTypeNames.add(SynagogueUtils.getTextFromEnum(getContext(), relativeTimeType));
        }
        spinnerRelativeTimeType.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, relativeTimeTypeNames));
    }


    private void addMinyan() {
        if (inRelativeTimeMode) {
            if (etMinutes.getText().toString().equals("")) {
                Toast.makeText(getContext(), getResources().getString(R.string.check), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        PrayType prayType = PrayType.values()[spinnerPrayType.getSelectedItemPosition()];
        PrayTime time = getPrayTime();

        try {
            if (cbSunday.isChecked()) {
                mSynagogue.addMinyan(new MinyanScheduleDomain(transformer.transformStringToWeekDay("SUNDAY"), prayType, time));
            }
            if (cbMonday.isChecked()) {
                mSynagogue.addMinyan(new MinyanScheduleDomain(transformer.transformStringToWeekDay("MONDAY"), prayType, time));
            }
            if (cbTuesday.isChecked()) {
                mSynagogue.addMinyan(new MinyanScheduleDomain(transformer.transformStringToWeekDay("TUESDAY"), prayType, time));
            }
            if (cbWednesday.isChecked()) {
                mSynagogue.addMinyan(new MinyanScheduleDomain(transformer.transformStringToWeekDay("WEDNESDAY"), prayType, time));
            }
            if (cbThursday.isChecked()) {
                mSynagogue.addMinyan(new MinyanScheduleDomain(transformer.transformStringToWeekDay("THURSDAY"), prayType, time));
            }
            if (cbFriday.isChecked()) {
                mSynagogue.addMinyan(new MinyanScheduleDomain(transformer.transformStringToWeekDay("FRIDAY"), prayType, time));
            }
            if (cbSaturday.isChecked()) {
                mSynagogue.addMinyan(new MinyanScheduleDomain(transformer.transformStringToWeekDay("SATURDAY"), prayType, time));
            }
            SynagoguesSource source = new SynagoguesSource(RestAPIUtility.createSynagoguesRestAPI(), transformer, SynagogueCache.getInstance());
            source.updateSynagogue(mSynagogue, new ResponseListener<Void>() {
                @Override
                public void onResponse(Void response) {
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.seccess_add_minyan), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            });
        } catch (Exception e) {
            Log.w(AddMinyanFragment.class.getSimpleName(),
                    "Couldn't add synagogue, an exception occurred:\n" + e.getMessage());
        }
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddMinyanListener) {
            mListener = (AddMinyanListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddMinyanListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onSetActionBarTitle(getResources().getString(R.string.sidebar_addMinyan));
    }

    @NonNull
    private PrayTime getPrayTime() {
        PrayTime time;
        if (inRelativeTimeMode) time = new PrayTime(new RelativeTime(
                (RelativeTimeType) spinnerRelativeTimeType.getSelectedItem(),
                Integer.parseInt(etMinutes.getText().toString())));
        else if (Build.VERSION.SDK_INT >= 23)
            time = new PrayTime(new ExactTime(timePicker.getHour(), timePicker.getMinute()));
        else
            time = new PrayTime(new ExactTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
        return time;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_minyan_btn:
                addMinyan();
                break;
        }
    }

    public interface AddMinyanListener {
        void onSetActionBarTitle(String title);
    }
}
