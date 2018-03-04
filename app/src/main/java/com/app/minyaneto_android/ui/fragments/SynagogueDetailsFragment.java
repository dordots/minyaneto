package com.app.minyaneto_android.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.domain.SynagogueCache;
import com.app.minyaneto_android.models.domain.SynagogueDomain;
import com.app.minyaneto_android.ui.adapters.MinyanAdapter;


public class SynagogueDetailsFragment extends DialogFragment {

    public static final String TAG = SynagogueDetailsFragment.class.getSimpleName();
    TextView tvNameSynagogue;
    TextView tvAddressSynagogue;
    TextView tvCommentsSynagogue;
    TextView tvNosachSynagogue;
    CheckBox cbParking;
    CheckBox cbSefer_tora;
    CheckBox cbWheelchair_accessible;
    CheckBox cbLessons;
    FloatingActionButton btnAddMinyan;
    private SynagogueDomain mSynagogue;
    private WantCahngeFragmentListener mListener;
    private RecyclerView mRecyclerViewMinyans;

    public static SynagogueDetailsFragment newInstance(String id) {
        SynagogueDetailsFragment fragment = new SynagogueDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(TAG)) {

            String id = getArguments().getString(TAG);
            mSynagogue = SynagogueCache.getInstance().getSynagogue(id);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_synagogue_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNameSynagogue = view.findViewById(R.id.synagogoe_details_name);
        tvAddressSynagogue = view.findViewById(R.id.synagogoe_details_address);
        tvCommentsSynagogue = view.findViewById(R.id.add_synagogue_comments);
        tvNosachSynagogue = view.findViewById(R.id.synagogoe_details_nosach);
        cbParking = view.findViewById(R.id.synagogoe_details_parking);
        cbSefer_tora = view.findViewById(R.id.synagogoe_details_sefer_tora);
        cbWheelchair_accessible = view.findViewById(R.id.synagogoe_details_accessible);
        cbLessons = view.findViewById(R.id.synagogoe_details_lessons);
        btnAddMinyan = view.findViewById(R.id.synagogoe_details_add_minyan);
        mRecyclerViewMinyans = view.findViewById(R.id.synagogoe_details_recycler_minyans);
        mRecyclerViewMinyans.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewMinyans.setLayoutManager(linearLayoutManager);
        if (mSynagogue == null)
            return;
        tvNameSynagogue.setText(mSynagogue.getName());
        tvAddressSynagogue.setText(mSynagogue.getAddress());
        tvCommentsSynagogue.setText(mSynagogue.getComments());
        tvNosachSynagogue.setText(getResources().getString(R.string.nosach) + " " + mSynagogue.getNosach());
        cbLessons.setChecked(mSynagogue.getClasses() != null ? mSynagogue.getClasses() : false);
        cbParking.setChecked(mSynagogue.getParking() != null ? mSynagogue.getParking() : false);
        cbSefer_tora.setChecked(mSynagogue.getSeferTora() != null ? mSynagogue.getSeferTora() : false);
        cbWheelchair_accessible.setChecked(mSynagogue.getWheelchairAccessible() != null ? mSynagogue.getWheelchairAccessible() : false);
        mRecyclerViewMinyans.setAdapter(new MinyanAdapter(mSynagogue.getMinyans()));
        btnAddMinyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onWantToAddAMinyan(mSynagogue.getId());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WantCahngeFragmentListener) {
            mListener = (WantCahngeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSynagoguesListener");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        try {
            super.onDetach();

            mListener.onSetActionBarTitle(null);

            mListener = null;
        } catch (Exception x) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mListener.onSetActionBarTitle(getResources().getString(R.string.synagogue_details_fragment));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }


    public interface WantCahngeFragmentListener {

        void onWantToAddAMinyan(String id);

        void onSetActionBarTitle(String string);
    }

}
