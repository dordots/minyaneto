package com.app.minyaneto_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
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
        //sy.getAddress()+" "+sy.getNosach()+" "+sy.isSefer_tora()+" "+sy.isClasses()+" "+sy.isParking()+" "+sy.isWheelchair_accessible())

        TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.info_synagogue_title));
        tvTitle.setText(marker.getTitle());

        TextView tvNosach = ((TextView)myContentsView.findViewById(R.id.info_synagogue_nosach));
        CheckBox cbIsWheelchairAccessible=((CheckBox)myContentsView.findViewById(R.id.info_synagogue_wheelchair_accessible));
        CheckBox cbIsSeferTora=((CheckBox)myContentsView.findViewById(R.id.info_synagogue_sefer_tora));
        CheckBox cbIsClasses=((CheckBox)myContentsView.findViewById(R.id.info_synagogue_classes));

        CheckBox cbIsParking=((CheckBox)myContentsView.findViewById(R.id.info_synagogue_parking));
        String[] separated = marker.getSnippet().split(" ");


        return myContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        return null;
    }

}