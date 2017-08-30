package com.app.minyaneto_android.fragments.add_synagogue_fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.Minyan;
import com.app.minyaneto_android.models.Synagogue;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddSynagogueFragment extends Fragment implements OnMapReadyCallback {

    private static AddSynagogueFragment _instance;
    public void setOnSeccessAdd(OnSeccessAdd onSeccessAdd) {
        this.onSeccessAdd = onSeccessAdd;
    }

    private OnSeccessAdd onSeccessAdd;

    public interface OnSeccessAdd {
        void OnSeccess(Synagogue synagogue);
    }

    private GoogleMap mMap;

    final Handler handler = new Handler();

    EditText etNameSynagogue;
    AutoCompleteTextView actvAddressSynagogue;
    EditText etCommentsSynagogue;
    Spinner spinnerNosachSynagogue;
    CheckBox cbParking;
    CheckBox cbSefer_tora;
    CheckBox cbWheelchair_accessible;
    CheckBox cbLessons;
    Button btnAddSynagogue;
    Marker marker;

    public AddSynagogueFragment() {
        // Required empty public constructor
    }

    public static AddSynagogueFragment theInstance() {
        if (_instance == null) {
            _instance = new AddSynagogueFragment();
        }
        return _instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_synagogue, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.add_synagogue_map);
        mapFragment.getMapAsync(this);

        etNameSynagogue = (EditText) view.findViewById(R.id.add_synagogoe_name);
        actvAddressSynagogue = (AutoCompleteTextView) view.findViewById(R.id.add_synagogoe_address);
        etCommentsSynagogue = (EditText) view.findViewById(R.id.add_synagogue_comments);
        spinnerNosachSynagogue = (Spinner) view.findViewById(R.id.add_synagogoe_nosach);
        cbParking = (CheckBox) view.findViewById(R.id.add_synagogoe_parking);
        cbSefer_tora = (CheckBox) view.findViewById(R.id.add_synagogoe_sefer_tora);
        cbWheelchair_accessible = (CheckBox) view.findViewById(R.id.add_synagogoe_accessible);
        cbLessons = (CheckBox) view.findViewById(R.id.add_synagogoe_lessons);
        btnAddSynagogue = (Button) view.findViewById(R.id.add_synagogoe_btn_add);
        btnAddSynagogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSynagogue();
            }
        });
        String[] address = {"בני נצרים", "יבול", "ירושלים"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_list_item_1, address);
        actvAddressSynagogue.setAdapter(adapter);
        actvAddressSynagogue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) return;
                new Runnable(){

                    @Override
                    public void run() {
                        Geocoder mGeocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> myAddresses = null;
                        try {
                            myAddresses = mGeocoder.getFromLocationName(actvAddressSynagogue.getText().toString(), 20);
                            if (myAddresses.size() > 0) {
                                mMap.clear();
                                LatLng loc = new LatLng(myAddresses.get(0).getLatitude(), myAddresses.get(0).getLongitude());
                                marker = mMap.addMarker(new MarkerOptions()
                                        .position(loc)
                                        .draggable(true)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.run();

            }
        });
    }

    private void addSynagogue() {
        if (etNameSynagogue.getText().toString().equals("") || actvAddressSynagogue.getText().toString().equals("")) {
            Toast.makeText(getContext(), getResources().getString(R.string.check_synagogue), Toast.LENGTH_SHORT).show();
            return;
        }
        Synagogue s = new Synagogue();
        s.setName(etNameSynagogue.getText().toString());
        s.setAddress(actvAddressSynagogue.getText().toString());
        s.setComments(etCommentsSynagogue.getText().toString());
        s.setNosach((String) spinnerNosachSynagogue.getSelectedItem());
        s.setClasses(cbLessons.isChecked());
        s.setParking(cbParking.isChecked());
        s.setSefer_tora(cbSefer_tora.isChecked());
        s.setWheelchair_accessible(cbWheelchair_accessible.isChecked());
        if (marker != null)
            s.setGeo(marker.getPosition());
        //TODO add synagogue to server
        if (onSeccessAdd != null)
            onSeccessAdd.OnSeccess(s);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker _marker) {
                marker = _marker;
                new Runnable() {
                    @Override
                    public void run() {
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addresses = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        try {
                            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                            actvAddressSynagogue.setText(addresses.get(0).getAddressLine(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.run();
            }
        });
    }


}
