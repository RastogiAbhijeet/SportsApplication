package com.ccet.hp.sportsapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HP on 25-12-2017.
 */

public class StudentAdapter extends ArrayAdapter<HashMap<String, String>> {

    ArrayList<HashMap<String, String>> dataList;

    public StudentAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String, String>> dataList) {
        super(context, textViewResourceId, dataList);
        this.dataList  = dataList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        try{
            View v = convertView;

            if(v == null){
                LayoutInflater inflater = (LayoutInflater)getContext ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate (R.layout.student_adapter,null);
            }

            TextView label = v.findViewById(R.id.key);
            TextView value = v.findViewById (R.id.value);


            List<String> s = new ArrayList<> (dataList.get (position).keySet ());

//            Log.i("Position :" + Integer.toString (position), " Size of Array" + Integer.toString (s.size ()));
            label.setText (s.get(position));
            value.setText (dataList.get(position).get (s.get(position)));

            if(position%2 == 0) {
                v.setBackgroundColor (Color.LTGRAY);
            }

            return v;
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace ();
            return null;
        }
    }
}


