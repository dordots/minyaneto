package com.app.minyaneto_android.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.location.LocationUtility;
import com.app.minyaneto_android.models.domain.SynagogueDomain;
import com.app.minyaneto_android.ui.adapters.SynagogueAdapter;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SynagoguesFragment extends Fragment implements View.OnClickListener {

  public static final String TAG = SynagoguesFragment.class.getSimpleName();
  private OnSynagoguesListener mListener;
  private List<SynagogueDomain> mSynagogues;
  private RecyclerView mSynagoguesView;
  private SynagogueAdapter mAdapter;
  private View mProgress;
  private TextView mError;
  private FloatingActionButton searchMinyan;


  public static SynagoguesFragment newInstance() {
    SynagoguesFragment fragment = new SynagoguesFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_synagogues, container, false);

    init(view);

    return view;
  }

  private void init(View view) {
    mSynagogues = new ArrayList<>();
    mSynagoguesView = view.findViewById(R.id.FS_synagogues);
    mSynagoguesView.setHasFixedSize(true);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mSynagoguesView.setLayoutManager(linearLayoutManager);
    mAdapter = new SynagogueAdapter(mSynagogues);
    mSynagoguesView.setAdapter(mAdapter);
    mProgress = view.findViewById(R.id.FS_progress);
    mError = view.findViewById(R.id.FS_error);
    searchMinyan = view.findViewById(R.id.search_minyan);
    searchMinyan.setOnClickListener(this);
  }


  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnSynagoguesListener) {
      mListener = (OnSynagoguesListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnSynagoguesListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public void onResume() {
    super.onResume();

    mListener
        .onSetActionBarTitle(getContext().getResources().getString(R.string.main_screen_fragment));

  }

  public void updateSynagogues(List<SynagogueDomain> synagogues, String msg) {
    mSynagogues.clear();
    if (synagogues.size() == 0) {
      mError.setVisibility(View.VISIBLE);
      mError.setText(msg);
    } else {
      mError.setVisibility(View.GONE);
      mSynagogues.addAll(synagogues);
    }
    mProgress.setVisibility(View.GONE);
    updateAdapter();
  }


  public void scrollToSynagoguePosition(int position) {

    mSynagoguesView.scrollToPosition(position);
    mAdapter.setSelectedListPosition(position);
  }

  private void updateAdapter() {

    sortSynagoguesByLocation();

    if (mListener != null) {

      mListener.onUpdateMarkers(mSynagogues);
    }
    mAdapter.setMyClickListener(new SynagogueAdapter.SynagogueClickListener() {
      @Override
      public void onItemClick(int position) {

          if (position == -1) {
              return;
          }

        mListener.onMoveCamera(mSynagogues.get(position).getLocation(), position);
      }

      @Override
      public void onRouteClick(int position) {
          if (position == -1) {
              return;
          }

        mListener.onOpenRoute(mSynagogues.get(position).getLocation());
      }

      @Override
      public void onShowDetailsClick(int position) {
          if (position == -1) {
              return;
          }

        mListener.onShowSynagogueDetails(mSynagogues.get(position).getId());

      }
    });

    mAdapter.notifyDataSetChanged();
  }

  private void sortSynagoguesByLocation() {
    Collections.sort(mSynagogues, new Comparator<SynagogueDomain>() {
      public int compare(SynagogueDomain o1, SynagogueDomain o2) {
        long distance1 = LocationUtility.getDistance(o1);
        long distance2 = LocationUtility.getDistance(o2);
        return Long.compare(distance1, distance2);
      }
    });
  }

  @Override
  public void onClick(View view) {
    if (mListener != null) {

      mListener.onSearchMinyan();
    }
  }


  public interface OnSynagoguesListener {

    void onSetActionBarTitle(String title);

    void onUpdateMarkers(List<SynagogueDomain> mSynagogues);

    void onMoveCamera(LatLng latLng, int position);

    void onOpenRoute(LatLng geo);

    void onShowSynagogueDetails(String id);

    void onSearchMinyan();
  }
}
