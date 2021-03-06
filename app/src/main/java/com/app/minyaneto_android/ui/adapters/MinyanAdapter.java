package com.app.minyaneto_android.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.minyaneto_android.R;
import com.app.minyaneto_android.location.LocationRepository;
import com.app.minyaneto_android.models.domain.MinyanSchedule;
import com.app.minyaneto_android.models.time.ExactTime;
import com.app.minyaneto_android.models.time.TimeUtility;
import com.app.minyaneto_android.utilities.SynagogueUtils;
import java.util.List;
import java.util.Locale;

public class MinyanAdapter extends RecyclerView.Adapter<MinyanAdapter.MinyanViewHolder> {

  private List<MinyanSchedule> minyans;

  public MinyanAdapter(List<MinyanSchedule> minyans) {
    this.minyans = minyans;
  }

  @Override
  public int getItemCount() {
    return minyans.size();
  }

  @Override
  public MinyanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_minyan_synagogue_details, parent, false);
    MinyanViewHolder minyanViewHolder = new MinyanViewHolder(v);
    return minyanViewHolder;
  }

  @Override
  public void onBindViewHolder(MinyanViewHolder holder, int position) {
    MinyanSchedule minyan = minyans.get(position);
    holder.prayTypeTextView.setText(
        SynagogueUtils.getTextFromEnum(holder.prayTypeTextView.getContext(), minyan.getPrayType()));
    ExactTime time = TimeUtility.extractSpecificTime(
        minyan.getPrayTime(),
        LocationRepository.getInstance().getLastKnownLocation());

    holder.prayerTimeTextView.setText(
        String.format(Locale.getDefault(), "%02d:%02d", time.getHour(), time.getMinutes()));
    holder.prayDayTypeTextView.setText(SynagogueUtils
        .getTextFromEnum(holder.prayDayTypeTextView.getContext(), minyan.getWeekDay()));
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
