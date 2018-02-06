package com.app.minyaneto_android.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.minyan.PrayType;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchFragment extends Fragment implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private final static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public static final String TAG = SearchFragment.class.getSimpleName();

    private SearchListener mListener;

    EditText etSearchAddress;

    Spinner spinnerNosachSynagogue;

    Spinner spinnerNameTfila;

    CheckBox cbSearchByNosach;

    LinearLayout choose_nosach;

    Button btnChooseADate;

    Button btnChooseATime;

    Button btnSearchSynagogue;

    LatLng mLatLng;

    Date date;

    public static SearchFragment getInstance() {

        return new SearchFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        date = new Date();

        etSearchAddress = view.findViewById(R.id.search_synagogoe_address);

        spinnerNosachSynagogue = view.findViewById(R.id.search_nosach);

        spinnerNameTfila = view.findViewById(R.id.search_synagogoe_name);

        cbSearchByNosach = view.findViewById(R.id.seach_by_nosach);

        choose_nosach = view.findViewById(R.id.search_by_nosach_true);

        btnChooseADate = view.findViewById(R.id.search_choose_date);

        btnChooseATime = view.findViewById(R.id.search_choose_time);

        btnSearchSynagogue = view.findViewById(R.id.search_synagogoe_btn_search);


        etSearchAddress.setOnClickListener(this);

        btnChooseADate.setOnClickListener(this);

        btnChooseATime.setOnClickListener(this);

        btnSearchSynagogue.setOnClickListener(this);

        cbSearchByNosach.setOnCheckedChangeListener(this);

    }


    private void chooseTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), this, mHour, mMinute, false);
        timePickerDialog.show();
    }


    private void chooseDate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void getAddress() {

        try {

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());

            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_address), Toast.LENGTH_SHORT).show();
        }

    }

    public void updateLatLng(LatLng latLng) {

        mLatLng = latLng;

    }

    private void searchSynagogues() {

        if (etSearchAddress.getText().toString().equals("") || null == mLatLng ||
                btnChooseADate.getText() == getString(R.string.choose_a_date) ||
                btnChooseATime.getText() == getString(R.string.choose_a_time)) {
            Toast.makeText(getContext(), getResources().getString(R.string.check_search), Toast.LENGTH_SHORT).show();
            return;
        }

        String nosach = null;

        if (cbSearchByNosach.isChecked())
            nosach = (String) spinnerNosachSynagogue.getSelectedItem();

        PrayType prayType = PrayType.values()[spinnerNameTfila.getSelectedItemPosition()];

        if (mListener != null) {

            mListener.onSearch(mLatLng, date, prayType, nosach);
        }
    }

    public void updateSynagogueAddress(String address) {

        etSearchAddress.setText(address);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(getActivity(), data);

                updateSynagogueAddress(place.getAddress().toString());

                mListener.onUpdateMarker(place);

                updateLatLng(place.getLatLng());

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mListener.onSetActionBarTitle(getResources().getString(R.string.search_synagogue_fragment));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchListener) {
            mListener = (SearchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSynagoguesListener");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.search_synagogoe_btn_search:

                searchSynagogues();

                break;

            case R.id.search_synagogoe_address:

                getAddress();

                break;

            case R.id.search_choose_date:

                chooseDate();

                break;

            case R.id.search_choose_time:

                chooseTime();

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {

        switch (v.getId()) {

            case R.id.seach_by_nosach:

                choose_nosach.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);

                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Date d = new Date(year, month, dayOfMonth);

        d.setHours(date.getHours());

        d.setMinutes(date.getMinutes());

        date = d;

        SimpleDateFormat format = new SimpleDateFormat("MM.dd.yy");

        btnChooseADate.setText(format.format(date));

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        date.setHours(hourOfDay);

        date.setMinutes(minute);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        btnChooseATime.setText(format.format(date));

    }


    public interface SearchListener {

        void onSearch(LatLng latLng, Date date, PrayType prayType, String nosach);

        void onSetActionBarTitle(String title);

        void onUpdateMarker(Place place);

    }

}