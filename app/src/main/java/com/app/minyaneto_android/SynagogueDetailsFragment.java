package com.app.minyaneto_android;

import android.content.Context;
import android.database.DataSetObserver;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.app.minyaneto_android.entities.Synagogue;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SynagogueDetailsFragment extends Fragment {
    private static SynagogueDetailsFragment _instance;

    EditText etNameSynagogue;
    EditText etAddressSynagogue;
    Spinner spinnerNosachSynagogue;
    CheckBox cbParking;
    CheckBox cbSefer_tora;
    CheckBox cbWheelchair_accessible;
    CheckBox cbLessons;
    Button btnAddSynagogue;

    public SynagogueDetailsFragment() {
        // Required empty public constructor
    }

    private OnFragmentInteractionListener mListener;

    public static SynagogueDetailsFragment theInstance() {
        if (_instance == null) {
            _instance = new SynagogueDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_synagogue_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etNameSynagogue=(EditText)view.findViewById(R.id.synagogoe_details_name);
        etAddressSynagogue=(EditText)view.findViewById(R.id.synagogoe_details_address);
        spinnerNosachSynagogue=(Spinner)view.findViewById(R.id.synagogoe_details_nosach);
        cbParking=(CheckBox)view.findViewById(R.id.synagogoe_details_parking);
        cbSefer_tora=(CheckBox)view.findViewById(R.id.synagogoe_details_sefer_tora);
        cbWheelchair_accessible=(CheckBox)view.findViewById(R.id.synagogoe_details_accessible);
        cbLessons=(CheckBox)view.findViewById(R.id.synagogoe_details_lessons);
        btnAddSynagogue=(Button)view.findViewById(R.id.synagogoe_details_btn_add);
        btnAddSynagogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSynagogue();
            }
        });
    }
    final Handler handler=new Handler();

    private void addSynagogue() {
        if(etNameSynagogue.getText().toString().equals("") || etAddressSynagogue.getText().toString().equals("") ) {
            Toast.makeText(getContext(), getResources().getString(R.string.check_synagogue), Toast.LENGTH_SHORT).show();
            return;
        }
        Synagogue s=new Synagogue();
        s.setName(etNameSynagogue.getText().toString());
        s.setAddress(etAddressSynagogue.getText().toString());
        s.setNosach((String)spinnerNosachSynagogue.getSelectedItem());
        s.setClasses(cbLessons.isChecked());
        s.setParking(cbParking.isChecked());
        s.setSefer_tora(cbSefer_tora.isChecked());
        s.setWheelchair_accessible(cbWheelchair_accessible.isChecked());

        Geocoder mGeocoder = new Geocoder(getActivity(), Locale.getDefault());
        String myCity = etAddressSynagogue.getText().toString();
        List<Address> myAddresses = null;
        try {
            myAddresses = mGeocoder.getFromLocationName(myCity, 20);
            if (myAddresses.size() <= 0) {

                return;
            }
            s.setGeo(new LatLng(myAddresses.get(0).getLatitude(),myAddresses.get(0).getLongitude()));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
