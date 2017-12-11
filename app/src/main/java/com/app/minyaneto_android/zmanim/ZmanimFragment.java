package com.app.minyaneto_android.zmanim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.minyaneto_android.Injection;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.acivities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ZmanimFragment extends Fragment implements ZmanimContract.View {

    private ZmanimContract.UserActionsListener listener;
    private TextView alosTv;
    private TextView misheyakirTv;
    private TextView tzaisTv;
    private SimpleDateFormat formatter;

    public ZmanimFragment() {
        formatter = new SimpleDateFormat("HH:mm");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listener = new ZmanimPresenter(Injection.getZmanimCalendarProvider(),
                Injection.getLocationProvider(getContext()),
                this);
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.showZmanim();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity())
                    .setActionBarTitle(getResources().getString(R.string.zmanim_fragment));
        }

        View root = inflater.inflate(R.layout.fragment_zmanim, container, false);
        alosTv = (TextView) root.findViewById(R.id.alos_hashahar);
        misheyakirTv = (TextView) root.findViewById(R.id.misheyakir);
        tzaisTv = (TextView) root.findViewById(R.id.tzais_hakochavim);
        return root;
    }

    @Override
    public void displayAlosHashahar(Date alos) {
        alosTv.setText(formatter.format(alos));
    }

    @Override
    public void displayMisheyakir(Date misheyakir) {
        misheyakirTv.setText(formatter.format(misheyakir));
    }

    @Override
    public void displayTzaisHakochavim(Date tzais) {
        tzaisTv.setText(formatter.format(tzais));
    }
}
