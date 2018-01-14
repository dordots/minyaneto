package com.app.minyaneto_android.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.minyaneto_android.R;

public class AboutFragment extends Fragment {

    public static final String TAG = AboutFragment.class.getSimpleName();

    private AboutListener mListener;


    public static AboutFragment getInstance() {

        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AboutListener) {
            mListener = (AboutListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSynagoguesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener.onSetActionBarTitle(null);

        mListener =null;
    }

    @Override
    public void onResume() {
        super.onResume();

        mListener.onSetActionBarTitle(getResources().getString(R.string.about_fragment));
    }

    public interface AboutListener {

        void onSetActionBarTitle(String title);
    }
}
