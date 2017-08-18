package com.app.minyaneto_android;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

class SynagogueInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;

    SynagogueInfoWindowAdapter(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        myContentsView = inflater.inflate(R.layout.info_synsgogue_window, null);
    }

    @Override
    public View getInfoContents(Marker marker) {

        TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.info_synagogue_title));
        TextView tvAddress=((TextView)myContentsView.findViewById(R.id.info_synagogue_address));
        ImageView ivParking=((ImageView)myContentsView.findViewById(R.id.info_synagogue_parking));
        ImageView ivSefer_tora=((ImageView)myContentsView.findViewById(R.id.info_synagogue_sefer_tora));
        ImageView ivWheelchair_accessible=((ImageView)myContentsView.findViewById(R.id.info_synagogue_wheelchair_accessible));
        ImageView ivLessons=((ImageView)myContentsView.findViewById(R.id.info_synagogue_classes));
        TextView tvNosach = ((TextView)myContentsView.findViewById(R.id.info_synagogue_nosach));
        String[] separated = marker.getSnippet().split(":");
        tvTitle.setText(marker.getTitle());
        tvAddress.setText(separated[0]);
        tvNosach.setText("("+separated[1]+")");
        if(separated[2].equals("true")){
            ivSefer_tora.setImageResource(R.drawable.ic_bible_on);
        }
        if(separated[3].equals("true")){
            ivLessons.setImageResource(R.drawable.ic_lesson_on);
        }
        if(separated[4].equals("true")){
            ivParking.setImageResource(R.drawable.ic_parking_on);
        }
        if(separated[5].equals("true")){
            ivWheelchair_accessible.setImageResource(R.drawable.ic_handycap_on);
        }
        return myContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
       return null;
    }

}