package com.app.minyaneto_android.ui.fragments.synagogue_details_fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.models.minyan.Minyan;

import java.util.List;

public class MinyanAdapter extends RecyclerView.Adapter<MinyanAdapter.MinyanViewHolder> {

    private List<Minyan> minyans;

    public MinyanAdapter(List<Minyan> minyans) {
      this.minyans=minyans;
    }

    @Override
    public int getItemCount() {
        return minyans.size();
    }

    @Override
    public MinyanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.minyan_cell_layout_synagogue_details,parent,false);
        MinyanViewHolder minyanViewHolder = new MinyanViewHolder(v);
        return minyanViewHolder;
    }

    @Override
    public void onBindViewHolder(MinyanViewHolder holder, int position) {
        Minyan minyan = minyans.get(position);
        holder.prayTypeTextView.setText(minyan.getPrayType().toString());
        holder.prayerTimeTextView.setText(minyan.getTime().getHour()+":"+minyan.getTime().getMinutes());
        holder.prayDayTypeTextView.setText(minyan.getPrayDayType().toString());
    }


    public class MinyanViewHolder extends RecyclerView.ViewHolder {

        TextView prayTypeTextView;
        TextView prayerTimeTextView;
        TextView prayDayTypeTextView;

        public MinyanViewHolder(View itemView) {
            super(itemView);
            prayTypeTextView = (TextView)itemView.findViewById(R.id.minyan_cell_pray_type);
            prayerTimeTextView = (TextView)itemView.findViewById(R.id.minyan_cell_prayer_time);
            prayDayTypeTextView=(TextView)itemView.findViewById(R.id.minyan_cell_pray_day_type);
        }

    }
}
