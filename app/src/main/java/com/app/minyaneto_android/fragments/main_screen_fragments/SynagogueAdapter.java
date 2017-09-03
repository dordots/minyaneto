package com.app.minyaneto_android.fragments.main_screen_fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class SynagogueAdapter extends RecyclerView.Adapter<SynagogueAdapter.SynagogueViewHolder> {

    private List<Synagogue> synagogues;
    private LatLng geo;
    private SynagogueClickListener myClickListener;

    public interface SynagogueClickListener {
        void onItemClick(int position);

        void onGoToWazeClick(int position);

        void onShowDetailsClick(int position);

        void onItemLongClick(int position, View v);

    }

    public void setMyClickListener(SynagogueClickListener listener) {
        this.myClickListener = listener;
    }

    public SynagogueAdapter(List<Synagogue> synagogues, LatLng geo) {
        this.synagogues = synagogues;
        this.geo = geo;
    }

    @Override
    public int getItemCount() {
        return synagogues.size();
    }

    @Override
    public SynagogueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tfila_cell_layout_main_screen, parent, false);
        SynagogueViewHolder SynagogueViewHolder = new SynagogueViewHolder(v);
        return SynagogueViewHolder;
    }

    @Override
    public void onBindViewHolder(SynagogueViewHolder holder, int position) {
        Synagogue synagogue = synagogues.get(position);
        //holder.imageView.setImageResource(synagogue.getNosachResId());
        holder.nameTextView.setText(synagogue.getName());
        if (synagogue.getMinyans().size() > 0)
            holder.prayerTimeTextView.setText(calcTime(position));
                     holder.distanceSynagogueTextView.setText(calculateDistance(synagogue.getGeo()) + " מ'");
    }
    private String calcTime(int position){
        //TODO calculate real time
        Synagogue synagogue = synagogues.get(position);
        return synagogue.getMinyans().get(0).getTime().getHour() + ":" + synagogue.getMinyans().get(0).getTime().getMinutes();

    }

    private long calculateDistance(LatLng location) {

        double dLat = Math.toRadians(location.latitude - geo.latitude);
        double dLon = Math.toRadians(location.longitude - geo.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(geo.latitude))
                * Math.cos(Math.toRadians(location.latitude)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        long distanceInMeters = Math.round(6371000 * c);
        return distanceInMeters;
    }

    public class SynagogueViewHolder extends RecyclerView.ViewHolder {

        ImageView goWazeImageView;
        ImageView showDetailsImageView;
        TextView nameTextView;
        TextView prayerTimeTextView;
        TextView distanceSynagogueTextView;

        public SynagogueViewHolder(View itemView) {
            super(itemView);
            goWazeImageView = (ImageView) itemView.findViewById(R.id.go_waze);
            showDetailsImageView = (ImageView) itemView.findViewById(R.id.synagogue_details);
            nameTextView = (TextView) itemView.findViewById(R.id.synagogue_name);
            prayerTimeTextView = (TextView) itemView.findViewById(R.id.prayer_time);
            distanceSynagogueTextView = (TextView) itemView.findViewById(R.id.synagogue_distance);
            goWazeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myClickListener != null)
                        myClickListener.onGoToWazeClick(getAdapterPosition());
                }
            });
            showDetailsImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myClickListener != null)
                        myClickListener.onShowDetailsClick(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myClickListener != null)
                        myClickListener.onItemClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myClickListener != null)
                        myClickListener.onItemLongClick(getAdapterPosition(), v);
                    return false;
                }
            });
        }

    }
}
