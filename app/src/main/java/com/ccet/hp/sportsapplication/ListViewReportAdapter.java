package com.ccet.hp.sportsapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HP on 06-01-2018.
 */

public class ListViewReportAdapter extends ArrayAdapter<HashMap<String, String>> {
    ArrayList<HashMap<String, String>> dataList;

    public ListViewReportAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String, String>> dataList){
        super(context, textViewResourceId, dataList);
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater)getContext ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate (R.layout.position_table_faculty, null);
        }

        TextView player_position = v.findViewById (R.id.position_report);
        TextView player_name = v.findViewById (R.id.name_report);

        List<String> s = new ArrayList<String>(dataList.get(position).keySet ());

        try {
            player_position.setText (s.get (position));
            player_name.setText (dataList.get (position).get (s.get (position)));

            player_name.setTextColor (Color.BLACK);
            player_position.setTextColor (Color.BLACK);
        }catch (Exception e){
            e.printStackTrace ();
        }

        return v;
    }
}
