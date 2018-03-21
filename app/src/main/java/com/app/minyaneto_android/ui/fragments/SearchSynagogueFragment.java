package com.app.minyaneto_android.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.location.LocationRepository;
import com.app.minyaneto_android.utilities.LocationHelper;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

public class SearchSynagogueFragment extends Fragment implements
    View.OnClickListener {

  public static final String TAG = SearchSynagogueFragment.class.getSimpleName();
  private final static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
  EditText etSearchAddress;
  Button btnSearchSynagogue;
  private SearchListener mListener;
  private LatLng mLatLng;

  public static SearchSynagogueFragment getInstance() {

    return new SearchSynagogueFragment();
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    return inflater.inflate(R.layout.fragment_search_synagogue, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    etSearchAddress = view.findViewById(R.id.search_synagogue_address);
    btnSearchSynagogue = view.findViewById(R.id.search_synagogue_btn_search);
    etSearchAddress.setOnClickListener(this);
    btnSearchSynagogue.setOnClickListener(this);
  }

  private void getAddress() {

    try {

      Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
          .zzim(etSearchAddress.getText().toString()).build(getActivity());

      startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
      Toast
          .makeText(getContext(), getResources().getString(R.string.no_address), Toast.LENGTH_SHORT)
          .show();
    }

  }

  private void searchSynagogues() {
    if (etSearchAddress.getText().toString().equals("")) {
      Toast.makeText(getContext(), getResources().getString(R.string.check_search),
          Toast.LENGTH_SHORT).show();
      return;
    }

    if (mListener != null) {
      mListener.onUpdateMarker(mLatLng, etSearchAddress.getText().toString());
      mListener.onSearchSynagogue(etSearchAddress.getText().toString(), mLatLng);
    }
  }

  public void updateSynagogueAddress(String address) {
    etSearchAddress.setText(address);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      Place mPlace = PlacePicker.getPlace(getActivity(), data);
      mLatLng = mPlace.getLatLng();
      if (mListener != null) {
        mListener.onUpdateMarker(mLatLng, mPlace.getAddress().toString());
      }
      updateSynagogueAddress(mPlace.getAddress().toString());
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    Location location = LocationRepository.getInstance().getLastKnownLocation();
    mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
    new Runnable() {

      @Override
      public void run() {
        String address = LocationHelper.getAddressLineFromLatLng(getContext(), mLatLng);
        if (mListener != null) {
          mListener.onUpdateMarker(mLatLng, address);
        }
        updateSynagogueAddress(address);
      }

    }.run();
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
      case R.id.search_synagogue_btn_search:
        searchSynagogues();
        break;
      case R.id.search_synagogue_address:
        getAddress();
        break;
    }
  }

  public interface SearchListener {

    void onSearchSynagogue(String address, LatLng latLng);

    void onSetActionBarTitle(String title);

    void onUpdateMarker(LatLng latLng, String address);

  }

}