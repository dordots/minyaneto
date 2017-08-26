package com.app.minyaneto_android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.minyaneto_android.models.Synagogue;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class SynagogueAdapter extends RecyclerView.Adapter<SynagogueAdapter.SynagogueViewHolder> {

    private List<Synagogue> synagogues;
    private LatLng geo;
    private SynagogueClickListener myClickListener;

    public interface SynagogueClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position, View v);
    }

    public void setMyClickListener(SynagogueClickListener listener) {
        this.myClickListener = listener;
    }

    public SynagogueAdapter(List<Synagogue> synagogues, LatLng geo) {
        this.synagogues = synagogues;
        this.geo=geo;
    }

    @Override
    public int getItemCount() {
        return synagogues.size();
    }

    @Override
    public SynagogueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.minyan_cell_layout,parent,false);
        SynagogueViewHolder SynagogueViewHolder = new SynagogueViewHolder(v);
        return SynagogueViewHolder;
    }

    @Override
    public void onBindViewHolder(SynagogueViewHolder holder, int position) {
        Synagogue synagogue = synagogues.get(position);
        //holder.imageView.setImageResource(synagogue.getNosachResId());
        holder.nameTextView.setText(synagogue.getName());
        if(synagogue.getMinyans().size()>0)
            holder.prayerTimeTextView.setText("time");
        holder.distanceSynagogueTextView.setText(calculateDistance(synagogue.getGeo())+" מ'");
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

        ImageView imageView;
        TextView nameTextView;
        TextView prayerTimeTextView;
        TextView distanceSynagogueTextView;

        public SynagogueViewHolder(View itemView) {
            super(itemView);
            //imageView = (ImageView)itemView.findViewById(R.id.synagogue_nosach);
            nameTextView = (TextView)itemView.findViewById(R.id.synagogue_name);
            prayerTimeTextView = (TextView)itemView.findViewById(R.id.prayer_time);
            distanceSynagogueTextView=(TextView)itemView.findViewById(R.id.synagogue_distance);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onItemClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    myClickListener.onItemLongClick(getAdapterPosition(),v);
                    return false;
                }
            });
        }

    }
}
