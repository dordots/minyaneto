package com.app.minyaneto_android.ui.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.location.LocationUtility;
import com.app.minyaneto_android.models.domain.SynagogueDomain;
import com.app.minyaneto_android.models.time.TimeUtility;

import java.util.Date;
import java.util.List;

public class SynagogueAdapter extends RecyclerView.Adapter<SynagogueAdapter.SynagogueViewHolder> {

    private List<SynagogueDomain> synagogues;
    private SynagogueClickListener myClickListener;
    private int rowIndex = -1;

    public SynagogueAdapter(List<SynagogueDomain> synagogues) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tfila_main_screen, parent, false);
        SynagogueViewHolder SynagogueViewHolder = new SynagogueViewHolder(v);
        return SynagogueViewHolder;
    }

    @Override
    public void onBindViewHolder(SynagogueViewHolder holder, final int position) {
        SynagogueDomain synagogue = synagogues.get(position);
        holder.nameTextView.setText(synagogue.getName());
        // holder.walkingTime.setText(synagogue.getWalking_time()+"");
        // holder.drivigTime.setText(synagogue.getDriving_time()+"");


        if (synagogue.getMinyans().size() > 0) {
            //TODO real time from minyans
            holder.prayerTimeTextView.setText(TimeUtility.getTimes(synagogue.getMinyans(), new Date()));
        }
        double distance = LocationUtility.getDistance(synagogue);
        if (distance < 1000)
            holder.distanceSynagogueTextView.setText(String.format("%.2f ", distance) + "מ'");
        else
            holder.distanceSynagogueTextView.setText(String.format("%.2f ", distance / 1000) + "ק'מ");


        if (rowIndex == position)
            holder.rowLinearlayout.setBackgroundColor(Color.LTGRAY);
        else
            holder.rowLinearlayout.setBackgroundColor(Color.WHITE);
    }

    public void setSelectedListPosition(int position) {
        int prevPosition = rowIndex;
        if (prevPosition != -1) {
            notifyItemChanged(prevPosition);
        }

        notifyItemChanged(position);
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
        LinearLayout rowLinearlayout;
        ImageView go;
        ImageView details;


        public SynagogueViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.synagogue_name);
            prayerTimeTextView = itemView.findViewById(R.id.prayer_time);
            distanceSynagogueTextView = itemView.findViewById(R.id.synagogue_distance);
            rowLinearlayout = itemView.findViewById(R.id.row_linrLayout);
            go = itemView.findViewById(R.id.go_waze);
            details = itemView.findViewById(R.id.synagogue_details);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rowIndex = getAdapterPosition();
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
