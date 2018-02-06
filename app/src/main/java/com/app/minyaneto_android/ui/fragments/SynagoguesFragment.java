package com.app.minyaneto_android.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.app.minyaneto_android.ui.adapters.SynagogueAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * create by david vardi
 */
public class SynagoguesFragment extends Fragment {

    public static final String TAG = SynagoguesFragment.class.getSimpleName();

    private OnSynagoguesListener mListener;

    private ArrayList<Synagogue> mSynagogues;

    private RecyclerView mSynagoguesView;

    private SynagogueAdapter mAdapter;

    private View mProgress;


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

        mAdapter = new SynagogueAdapter(mSynagogues, getContext());

        mSynagoguesView.setAdapter(mAdapter);

        mProgress = view.findViewById(R.id.FS_progress);


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

        mListener.onSetActionBarTitle(getContext().getResources().getString(R.string.main_screen_fragment));

    }

    public void updateSynagogues(ArrayList<Synagogue> synagogues) {

        mSynagogues.clear();

        mSynagogues.addAll(synagogues);

        mProgress.setVisibility(View.GONE);

        updateAdapter();

    }




    public void scrollToSynagoguePosition(int position) {

        mSynagoguesView.scrollToPosition(position);

    }

    private void updateAdapter() {

        sortSynagoguesByLocation();

        mListener.onUpdateMarkers(mSynagogues);

        mAdapter.setMyClickListener(new SynagogueAdapter.SynagogueClickListener() {
            @Override
            public void onItemClick(int position) {

                if (position == -1) return;

                mListener.onMoveCamera(mSynagogues.get(position).getGeo(), position);
            }

            @Override
            public void onRouteClick(int position) {
                if (position == -1) return;

                mListener.onOpenRoute(mSynagogues.get(position).getGeo());
            }

            @Override
            public void onShowDetailsClick(int position) {
                if (position == -1) return;

                mListener.onShowSynagogueDetails(mSynagogues.get(position).getId());

            }
        });

        mAdapter.notifyDataSetChanged();

    }

    private void sortSynagoguesByLocation() {

        Collections.sort(mSynagogues, new Comparator<Synagogue>() {
            public int compare(Synagogue o1, Synagogue o2) {
                if (o1.getDistanceFromLocation() == o2.getDistanceFromLocation())
                    return 0;
                return o1.getDistanceFromLocation() < o2.getDistanceFromLocation() ? -1 : 1;
            }
        });
    }


    public interface OnSynagoguesListener {

        void onSetActionBarTitle(String title);

        void onUpdateMarkers(ArrayList<Synagogue> mSynagogues);

        void onMoveCamera(LatLng latLng, int position);

        void onOpenRoute(LatLng geo);

        void onShowSynagogueDetails(String id);
    }
}
