package com.app.minyaneto_android.fragments.main_screen_fragments;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.minyan.Minyan;
import com.app.minyaneto_android.models.minyan.Time;
import com.app.minyaneto_android.models.minyan.WeekDay;
import com.app.minyaneto_android.models.synagogue.Synagogue;
import com.google.android.gms.maps.model.LatLng;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SynagogueAdapter extends RecyclerView.Adapter<SynagogueAdapter.SynagogueViewHolder> {

    private List<Synagogue> synagogues;
    private SynagogueClickListener myClickListener;
    private int idShowDetails;
    private int idWazw;
    private  int row_index=-1;

    public interface SynagogueClickListener {
        void onItemClick(int position);

        void onGoToWazeClick(int position);

        void onShowDetailsClick(int position);
    }

    public void setMyClickListener(SynagogueClickListener listener) {
        this.myClickListener = listener;
        return;
    }

    public SynagogueAdapter(List<Synagogue> synagogues,int idWazw,int idShowDetails) {
        this.synagogues = synagogues;
        this.idWazw=idWazw;
        this.idShowDetails=idShowDetails;
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
    public void onBindViewHolder(SynagogueViewHolder holder, final int position) {
        Synagogue synagogue = synagogues.get(position);
        //holder.imageView.setImageResource(synagogue.getNosachResId());
        holder.goWazeImageView.setImageResource(idWazw);
        holder.showDetailsImageView.setImageResource(idShowDetails);
        holder.nameTextView.setText(synagogue.getName());
        if (synagogue.getMinyans().size() > 0) {
            //TODO real time from minyans
            SimpleDateFormat format =
                    new SimpleDateFormat("HH:mm");
            holder.prayerTimeTextView.setText(format.format(calcTime(position)));
            holder.prayTypeTextView.setText(synagogue.getMinyans().get(0).getPrayType().toString().charAt(0)+"");
        }
        holder.distanceSynagogueTextView.setText(synagogue.getDistanceFromLocation()+"");

        if(row_index==position)
            holder.row_linearlayout.setBackgroundColor(Color.LTGRAY);
        else
            holder.row_linearlayout.setBackgroundColor(Color.WHITE);
    }

    private Date calcTime(int position) {
        //TODO calculate real time
        Synagogue synagogue = synagogues.get(position);
        //TODO return time in good format
        //TODO choose the best time from all minyans
        Minyan minyan=synagogue.getMinyans().get(0);
        return minyan.getTime().toDate(WeekDay.values()[new Date().getDay()]);
    }


    public class SynagogueViewHolder extends RecyclerView.ViewHolder {

        ImageView goWazeImageView;
        ImageView showDetailsImageView;
        TextView nameTextView;
        TextView prayTypeTextView;
        TextView prayerTimeTextView;
        TextView distanceSynagogueTextView;
        LinearLayout row_linearlayout;


        public SynagogueViewHolder(View itemView) {
            super(itemView);
            goWazeImageView = (ImageView) itemView.findViewById(R.id.go_waze);
            showDetailsImageView = (ImageView) itemView.findViewById(R.id.synagogue_details);
            nameTextView = (TextView) itemView.findViewById(R.id.synagogue_name);
            prayTypeTextView = (TextView) itemView.findViewById(R.id.pray_type_minyan);
            prayerTimeTextView = (TextView) itemView.findViewById(R.id.prayer_time);
            distanceSynagogueTextView = (TextView) itemView.findViewById(R.id.synagogue_distance);
            row_linearlayout=(LinearLayout)itemView.findViewById(R.id.row_linrLayout);
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
                    row_index=getAdapterPosition();
                    if (myClickListener != null)
                        myClickListener.onItemClick(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
