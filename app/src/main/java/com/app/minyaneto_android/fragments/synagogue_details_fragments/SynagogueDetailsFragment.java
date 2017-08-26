package com.app.minyaneto_android.fragments.synagogue_details_fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.Synagogue;

public class SynagogueDetailsFragment extends DialogFragment {
    private static Synagogue mSynagogue;

    TextView tvNameSynagogue;
    TextView tvAddressSynagogue;
    TextView tvCommentsSynagogue;
    TextView tvNosachSynagogue;
    CheckBox cbParking;
    CheckBox cbSefer_tora;
    CheckBox cbWheelchair_accessible;
    CheckBox cbLessons;

    public SynagogueDetailsFragment() {
        // Required empty public constructor
    }


    public static SynagogueDetailsFragment newInstance(Synagogue synagogue) {
        SynagogueDetailsFragment fragment = new SynagogueDetailsFragment();
        Bundle args = new Bundle();
        mSynagogue = synagogue;
        return fragment;
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
        tvNameSynagogue = (TextView) view.findViewById(R.id.synagogoe_details_name);
        tvAddressSynagogue = (TextView) view.findViewById(R.id.synagogoe_details_address);
        tvCommentsSynagogue = (TextView) view.findViewById(R.id.add_synagogue_comments);
        tvNosachSynagogue = (TextView) view.findViewById(R.id.synagogoe_details_nosach);
        cbParking = (CheckBox) view.findViewById(R.id.synagogoe_details_parking);
        cbSefer_tora = (CheckBox) view.findViewById(R.id.synagogoe_details_sefer_tora);
        cbWheelchair_accessible = (CheckBox) view.findViewById(R.id.synagogoe_details_accessible);
        cbLessons = (CheckBox) view.findViewById(R.id.synagogoe_details_lessons);

        tvNameSynagogue.setText(mSynagogue.getName());
        tvAddressSynagogue.setText(mSynagogue.getAddress());
        tvCommentsSynagogue.setText(mSynagogue.getComments());
        tvNosachSynagogue.setText(mSynagogue.getNosach());
        cbLessons.setChecked(mSynagogue.isClasses());
        cbParking.setChecked(mSynagogue.isParking());
        cbSefer_tora.setChecked(mSynagogue.isSefer_tora());
        cbWheelchair_accessible.setChecked(mSynagogue.isWheelchair_accessible());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }

}
