package com.app.minyaneto_android.ui.fragments;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.synagogue.Geo;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.restApi.RequestHelper;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import ravtech.co.il.httpclient.ErrorResponse;
import ravtech.co.il.httpclient.model.ErrorData;
import ravtech.co.il.httpclient.model.Result;


public class AddSynagogueFragment extends Fragment implements View.OnClickListener {

    private final static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public static final String TAG = AddSynagogueFragment.class.getSimpleName();

    private AddSynagogueListener mListener;

    EditText etNameSynagogue;

    EditText etAddressSynagogue;

    EditText etCommentsSynagogue;

    Spinner spinnerNosachSynagogue;

    CheckBox cbParking;

    CheckBox cbSefer_tora;

    CheckBox cbWheelchair_accessible;

    CheckBox cbLessons;

    Button btnAddSynagogue;

    LatLng mLatLng;


    public static AddSynagogueFragment getInstance() {

        return new AddSynagogueFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_synagogue, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNameSynagogue = view.findViewById(R.id.add_synagogoe_name);

        etAddressSynagogue = view.findViewById(R.id.add_synagogoe_address);

        etCommentsSynagogue = view.findViewById(R.id.add_synagogue_comments);

        spinnerNosachSynagogue = view.findViewById(R.id.add_synagogoe_nosach);

        cbParking = view.findViewById(R.id.add_synagogoe_parking);

        cbSefer_tora = view.findViewById(R.id.add_synagogoe_sefer_tora);

        cbWheelchair_accessible = view.findViewById(R.id.add_synagogoe_accessible);

        cbLessons = view.findViewById(R.id.add_synagogoe_lessons);

        btnAddSynagogue = view.findViewById(R.id.add_synagogoe_btn_add);

        etAddressSynagogue.setOnClickListener(this);

        btnAddSynagogue.setOnClickListener(this);

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

    private void addSynagogue() {

        if (etNameSynagogue.getText().toString().equals("") || etAddressSynagogue.getText().toString().equals("")) {
            Toast.makeText(getContext(), getResources().getString(R.string.check), Toast.LENGTH_SHORT).show();
            return;
        }
        final Synagogue s = new Synagogue();

        s.setName(etNameSynagogue.getText().toString());

        s.setAddress(etAddressSynagogue.getText().toString());

        s.setComments(etCommentsSynagogue.getText().toString());

        s.setNosach((String) spinnerNosachSynagogue.getSelectedItem());

        s.setClasses(cbLessons.isChecked());

        s.setParking(cbParking.isChecked());

        s.setSeferTora(cbSefer_tora.isChecked());

        s.setWheelchairAccessible(cbWheelchair_accessible.isChecked());

        if (mLatLng != null)
            s.setGeo(new Geo(mLatLng.latitude, mLatLng.longitude));
        else
            return; //TODO - what do if there is not latlng?

        RequestHelper.addSynagogue(getContext(), s, new Response.Listener<Synagogue>() {
            @Override
            public void onResponse(Synagogue response) {
                s.setId(response.getId());
                mListener.onAddSynagogue(s);
            }
        }, new ErrorResponse(new ErrorResponse.ErrorListener() {
            @Override
            public void onErrorResponse(Result<ErrorData> error) {
                error.getData().getMessage();
            }
        }));
    }


    public void updateSynagogueAddress(String address) {

        etAddressSynagogue.setText(address);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                Place place = PlacePicker.getPlace(getActivity(), data);

                updateSynagogueAddress(place.getAddress().toString());

                mListener.onUpdateMarker(place);

                mListener.onGetTheSynagoguesAround(place.getLatLng());

                updateLatLng(place.getLatLng());

            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        mListener.onSetActionBarTitle(getResources().getString(R.string.add_synagogue_fragment));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddSynagogueListener) {
            mListener = (AddSynagogueListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSynagoguesListener");
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.add_synagogoe_btn_add:

                addSynagogue();

                break;

            case R.id.add_synagogoe_address:

                getAddress();

                break;

        }
    }


    public interface AddSynagogueListener {

        void onAddSynagogue(Synagogue synagogue);

        void onSetActionBarTitle(String title);

        void onUpdateMarker(Place place);

        void onGetTheSynagoguesAround(LatLng lng);
    }
}
