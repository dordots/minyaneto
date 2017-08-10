package com.app.minyaneto_android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.minyaneto_android.entities.Synagogue;

import java.util.List;

public class SynagogueAdapter extends RecyclerView.Adapter<SynagogueAdapter.SynagogueViewHolder> {

    private List<Synagogue> synagogues;
    private SynagogueClickListener myClickListener;

    public interface SynagogueClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

    public void setMyClickListener(SynagogueClickListener listener) {
        this.myClickListener = listener;
    }

    public SynagogueAdapter(List<Synagogue> synagogues) {
        this.synagogues = synagogues;
    }

    @Override
    public int getItemCount() {
        return synagogues.size();
    }

    @Override
    public SynagogueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_layout,parent,false);
        SynagogueViewHolder SynagogueViewHolder = new SynagogueViewHolder(v);
        return SynagogueViewHolder;
    }

    @Override
    public void onBindViewHolder(SynagogueViewHolder holder, int position) {
        Synagogue synagogue = synagogues.get(position);
        //holder.imageView.setImageResource(synagogue.getNosachResId());
        holder.nameTextView.setText(synagogue.getName());
        holder.locationTextView.setText(synagogue.getLocation().latitude+"");
    }

    public class SynagogueViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTextView;
        TextView locationTextView;

        public SynagogueViewHolder(View itemView) {
            super(itemView);
            //imageView = (ImageView)itemView.findViewById(R.id.synagogue_nosach);
            nameTextView = (TextView)itemView.findViewById(R.id.synagogue_name);
            locationTextView = (TextView)itemView.findViewById(R.id.synagogue_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onItemClick(getAdapterPosition(),v);
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
