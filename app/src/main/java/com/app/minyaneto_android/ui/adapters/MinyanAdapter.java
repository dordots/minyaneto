package com.app.minyaneto_android.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.location.AndroidLocationProvider;
import com.app.minyaneto_android.models.minyan.Minyan;
import com.app.minyaneto_android.models.time.ExactTime;
import com.app.minyaneto_android.models.time.TimeUtility;

import java.util.List;
import java.util.Locale;

public class MinyanAdapter extends RecyclerView.Adapter<MinyanAdapter.MinyanViewHolder> {

    private List<Minyan> minyans;
    private Context context;

    public MinyanAdapter(List<Minyan> minyans, Context context) {
        this.minyans = minyans;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return minyans.size();
    }

    @Override
    public MinyanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.minyan_cell_layout_synagogue_details, parent, false);
        MinyanViewHolder minyanViewHolder = new MinyanViewHolder(v);
        return minyanViewHolder;
    }

    @Override
    public void onBindViewHolder(MinyanViewHolder holder, int position) {
        Minyan minyan = minyans.get(position);
        holder.prayTypeTextView.setText(minyan.getPrayType().name());
        ExactTime time = TimeUtility.extractSpecificTime(
                minyan.getPrayTime(),
                new AndroidLocationProvider(context).getLocation().getValue());
        holder.prayerTimeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", time.getHour(), time.getMinutes()));
        holder.prayDayTypeTextView.setText(minyan.getPrayDayType().name());
    }


    public class MinyanViewHolder extends RecyclerView.ViewHolder {

        TextView prayTypeTextView;
        TextView prayerTimeTextView;
        TextView prayDayTypeTextView;

        public MinyanViewHolder(View itemView) {
            super(itemView);
            prayTypeTextView = itemView.findViewById(R.id.minyan_cell_pray_type);
            prayerTimeTextView = itemView.findViewById(R.id.minyan_cell_prayer_time);
            prayDayTypeTextView = itemView.findViewById(R.id.minyan_cell_pray_day_type);
        }

    }
}
