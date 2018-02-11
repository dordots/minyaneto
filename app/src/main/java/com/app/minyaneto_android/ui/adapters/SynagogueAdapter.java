package com.app.minyaneto_android.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.synagogue.Synagogue;

import java.util.List;

public class SynagogueAdapter extends RecyclerView.Adapter<SynagogueAdapter.SynagogueViewHolder> {

    private List<Synagogue> synagogues;
    private SynagogueClickListener myClickListener;
    private int row_index = -1;


    public SynagogueAdapter(List<Synagogue> synagogues, Context context) {
        this.synagogues = synagogues;
    }

    public void setMyClickListener(SynagogueClickListener listener) {
        this.myClickListener = listener;
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
        holder.nameTextView.setText(synagogue.getName());

        if (synagogue.getMinyans().size() > 0) {
            //TODO real time from minyans
            holder.prayerTimeTextView.setText(synagogue.getMinyansAsString());
        }
        if (synagogue.getDistanceFromLocation() < 1000)
            holder.distanceSynagogueTextView.setText(String.format("%.2f ", synagogue.getDistanceFromLocation()) + "מ'");
        else
            holder.distanceSynagogueTextView.setText(String.format("%.2f ", synagogue.getDistanceFromLocation() / 1000) + "ק'מ");


        if (row_index == position)
            holder.row_linearlayout.setBackgroundColor(Color.LTGRAY);
        else
            holder.row_linearlayout.setBackgroundColor(Color.WHITE);
    }

    public interface SynagogueClickListener {
        void onItemClick(int position);

        void onRouteClick(int position);

        void onShowDetailsClick(int position);
    }

    public class SynagogueViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView prayerTimeTextView;
        TextView distanceSynagogueTextView;
        LinearLayout row_linearlayout;
        ImageView go;
        ImageView details;


        public SynagogueViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.synagogue_name);
            prayerTimeTextView = itemView.findViewById(R.id.prayer_time);
            distanceSynagogueTextView = itemView.findViewById(R.id.synagogue_distance);
            row_linearlayout = itemView.findViewById(R.id.row_linrLayout);
            go = itemView.findViewById(R.id.go_waze);
            details = itemView.findViewById(R.id.synagogue_details);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    row_index = getAdapterPosition();
                    if (myClickListener != null)
                        myClickListener.onItemClick(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myClickListener != null)
                        myClickListener.onRouteClick(getAdapterPosition());
                }
            });

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myClickListener != null)
                        myClickListener.onShowDetailsClick(getAdapterPosition());
                }
            });
        }
    }


}
