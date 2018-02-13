package com.app.minyaneto_android.zmanim;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.minyaneto_android.Injection;
import com.app.minyaneto_android.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ZmanimFragment extends Fragment implements ZmanimContract.View {

    public static final String TAG = ZmanimFragment.class.getSimpleName();

    private ZmanimContract.UserActionsListener listener;
    private SimpleDateFormat formatter;
    private ContentAdapter adapter;

    public ZmanimFragment() {
        formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
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
        return inflateAndSetupRecyclerView(inflater, container);
    }

    @NonNull
    private RecyclerView inflateAndSetupRecyclerView(LayoutInflater inflater, @Nullable ViewGroup container) {
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_zmanim, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContentAdapter(getContext());
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public void displayAlosHashahar(Date zman) {
        displayZmanInPosition(zman, 0);
    }

    @Override
    public void displayMisheyakir(Date zman) {
        displayZmanInPosition(zman, 1);
    }

    @Override
    public void displayHenezHahama(Date zman) {
        displayZmanInPosition(zman, 2);
    }

    @Override
    public void displayShkiaa(Date zman) {
        displayZmanInPosition(zman, 3);
    }

    @Override
    public void displayTzaisHakochavim(Date zman) {
        displayZmanInPosition(zman, 4);
    }

    private void displayZmanInPosition(Date zman, int position) {
        adapter.zmanim[position] = formatter.format(zman);
        adapter.notifyItemChanged(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView zman;
        public TextView title;
        public TextView description;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_zmanim_recycler_view, parent, false));
            zman = itemView.findViewById(R.id.item_zmanim_zman);
            title = itemView.findViewById(R.id.item_zmanim_title);
            description = itemView.findViewById(R.id.item_zmanim_description);
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private String[] zmanim;
        private String[] titles;
        private String[] descriptions;

        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            descriptions = resources.getStringArray(R.array.zmanim_descriptions);
            titles = resources.getStringArray(R.array.zmanim_titles);
            zmanim = new String[titles.length];
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.zman.setText(zmanim[position]);
            holder.title.setText(titles[position]);
            holder.description.setText(descriptions[position]);
        }

        @Override
        public int getItemCount() {
            return zmanim.length;
        }
    }

}
