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

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.data.DataTransformer;
import com.app.minyaneto_android.location.LocationRepository;
import com.app.minyaneto_android.models.domain.MinyanScheduleDomain;
import com.app.minyaneto_android.models.domain.SynagogueCache;
import com.app.minyaneto_android.models.domain.SynagogueDomain;
import com.app.minyaneto_android.models.domain.SynagoguesSource;
import com.app.minyaneto_android.restApi.ResponseListener;
import com.app.minyaneto_android.restApi.RestAPIUtility;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;


public class AddSynagogueFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = AddSynagogueFragment.class.getSimpleName();
    private final static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
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
    private AddSynagogueListener mListener;

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

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).zzim(etAddressSynagogue.getText().toString()).build(getActivity());

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
        final SynagogueDomain s = new SynagogueDomain(
                etAddressSynagogue.getText().toString(),
                cbLessons.isChecked(),
                etCommentsSynagogue.getText().toString(),
                "ignored_id",
                Collections.<MinyanScheduleDomain>emptyList(),
                etNameSynagogue.getText().toString(),
                (String) spinnerNosachSynagogue.getSelectedItem(),
                cbParking.isChecked(),
                cbSefer_tora.isChecked(),
                cbWheelchair_accessible.isChecked(),
                LocationRepository.getInstance().getLastKnownLatLng()
        );

        SynagoguesSource source = new SynagoguesSource(RestAPIUtility.createSynagoguesRestAPI(), new DataTransformer(), SynagogueCache.getInstance());
        source.addSynagogue(s, new ResponseListener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.seccess_add_synagogue), Toast.LENGTH_SHORT).show();
                mListener.onAddSynagogue(response);
            }
        });
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
        void onAddSynagogue(String id);

        void onSetActionBarTitle(String title);

        void onUpdateMarker(Place place);

        void onGetTheSynagoguesAround(LatLng lng);
    }
}
