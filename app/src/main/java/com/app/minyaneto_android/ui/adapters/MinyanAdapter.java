package com.app.minyaneto_android.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.minyaneto_android.R;
import com.app.minyaneto_android.location.LocationRepository;
import com.app.minyaneto_android.models.domain.MinyanScheduleDomain;
import com.app.minyaneto_android.models.time.ExactTime;
import com.app.minyaneto_android.models.time.TimeUtility;
import com.app.minyaneto_android.utilities.SynagogeUtils;

import java.util.List;
import java.util.Locale;

public class MinyanAdapter extends RecyclerView.Adapter<MinyanAdapter.MinyanViewHolder> {

    private List<MinyanScheduleDomain> minyans;

    public MinyanAdapter(List<MinyanScheduleDomain> minyans) {
        this.minyans = minyans;
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
        MinyanScheduleDomain minyan = minyans.get(position);
        holder.prayTypeTextView.setText(SynagogeUtils.getTextFromEnum(holder.prayTypeTextView.getContext(),minyan.getPrayType()));
        ExactTime time = TimeUtility.extractSpecificTime(
                minyan.getPrayTime(),
                LocationRepository.getInstance().getLastKnownLocation());

        holder.prayerTimeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", time.getHour(), time.getMinutes()));
        holder.prayDayTypeTextView.setText(SynagogeUtils.getTextFromEnum(holder.prayDayTypeTextView.getContext(),minyan.getDayOfWeek()));
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
