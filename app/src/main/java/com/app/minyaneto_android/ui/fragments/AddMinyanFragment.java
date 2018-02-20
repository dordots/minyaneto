package com.app.minyaneto_android.ui.fragments;

import android.content.Context;
import android.os.Build;
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

import com.android.volley.Response;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.domain.MinyanScheduleDomain;
import com.app.minyaneto_android.models.domain.SynagogueCache;
import com.app.minyaneto_android.models.domain.SynagogueDomain;
import com.app.minyaneto_android.models.minyan.Minyan;
import com.app.minyaneto_android.models.minyan.PrayDayType;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.app.minyaneto_android.models.synagogue.Geo;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.models.time.ExactTime;
import com.app.minyaneto_android.models.time.PrayTime;
import com.app.minyaneto_android.models.time.RelativeTime;
import com.app.minyaneto_android.models.time.RelativeTimeType;
import com.app.minyaneto_android.restApi.RequestHelper;
import com.google.android.gms.maps.model.LatLng;
import com.app.minyaneto_android.utilities.SynagogeUtils;

import java.util.ArrayList;
import java.util.List;

import ravtech.co.il.httpclient.ErrorResponse;
import ravtech.co.il.httpclient.model.ErrorData;
import ravtech.co.il.httpclient.model.Result;

public class AddMinyanFragment extends Fragment {

    public static final String TAG = AddMinyanFragment.class.getSimpleName();
    private static Synagogue mSynagogue;
    boolean inRelativeTimeMode;
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
    private LinearLayout linearLayoutRelativeTime;
    private AddMinyanListener mListener;

    public AddMinyanFragment() {
        // Required empty public constructor
    }

    public static AddMinyanFragment newInstance(String id) {
        SynagogueDomain synagogue = SynagogueCache.getInstance().getSynagogue(id);
        mSynagogue = toOldModel(synagogue);
        return new AddMinyanFragment();
    }

    private static Synagogue toOldModel(SynagogueDomain synagogue) {
        Synagogue result = new Synagogue();
        result.setAddress(synagogue.getAddress());
        result.setClasses(synagogue.getClasses());
        result.setComments(synagogue.getComments());
        LatLng latLng = synagogue.getLocation();
        result.setGeo(new Geo(latLng.latitude, latLng.longitude));
        result.setId(synagogue.getId());
        result.setMinyans(toOldModel(synagogue.getMinyans()));
        result.setName(synagogue.getName());
        result.setNosach(synagogue.getNosach());
        result.setParking(synagogue.getParking());
        result.setSeferTora(synagogue.getSeferTora());
        result.setWheelchairAccessible(synagogue.getWheelchairAccessible());
        return result;
    }

    private static ArrayList<Minyan> toOldModel(List<MinyanScheduleDomain> minyans) {
        ArrayList<Minyan> list = new ArrayList<>();
        for (MinyanScheduleDomain minyan :
                minyans) {
            Minyan item = new Minyan();
            item.setDay(minyan.getDayOfWeek().name());
            item.setName(minyan.getPrayType().name());
            PrayTime time = minyan.getPrayTime();
            String strTime;
            if (time.isRelative()) {
                RelativeTime relativeTime = time.getRelativeTime();
                strTime = relativeTime.getRelativeTimeType().name() + ":" + relativeTime.getOffset();
            } else {
                ExactTime exactTime = time.getExactTime();
                strTime = exactTime.getHour() + ":" + exactTime.getMinutes();
            }
            item.setTime(strTime);
            list.add(item);
        }
        return list;
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
        cbSaterday = view.findViewById(R.id.add_minyan_saterday);
        Button btnAddMinyn = view.findViewById(R.id.add_minyan_btn);
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
        btnAddMinyn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMinyan();
            }
        });

        ArrayList<String> prayTypeNames=new ArrayList<>(PrayType.values().length);
        for(PrayType prayType :PrayType.values()){
            prayTypeNames.add(SynagogeUtils.getTextFromEnum(getContext(),prayType));
        }
        spinnerPrayType.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, prayTypeNames));
        ArrayList<String> relativeTimeTypeNames=new ArrayList<>(RelativeTimeType.values().length);
        for(RelativeTimeType relativeTimeType :RelativeTimeType.values()){
            relativeTimeTypeNames.add(SynagogeUtils.getTextFromEnum(getContext(),relativeTimeType));
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

        for (PrayDayType day : days) {
            Minyan minyan = new Minyan();
            if (inRelativeTimeMode) {
                minyan.setRelativeTime(new RelativeTime(
                        (RelativeTimeType) spinnerRelativeTimeType.getSelectedItem(),
                        Integer.parseInt(etMinutes.getText().toString())));
            } else {
                if (Build.VERSION.SDK_INT >= 23) {
                    minyan.setExactTime(new ExactTime(timePicker.getHour(), timePicker.getMinute()));
                } else {
                    minyan.setExactTime(new ExactTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
                }
            }
            minyan.setPrayType(PrayType.values()[spinnerPrayType.getSelectedItemPosition()]);
            minyan.setPrayDayType(day);
            mSynagogue.addMinyan(minyan);
        }

        RequestHelper.updateSynagogue(getContext(), mSynagogue.getId(), mSynagogue, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { //response = null
                //TODO some message to user?
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.seccess_add_minyan), Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        }, new ErrorResponse(new ErrorResponse.ErrorListener() {
            @Override
            public void onErrorResponse(Result<ErrorData> error) {
                //error.getData().getMessage();
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.no_seccess), Toast.LENGTH_SHORT).show();
            }
        }));
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

    public interface AddMinyanListener {

        void onSetActionBarTitle(String title);
    }
}
