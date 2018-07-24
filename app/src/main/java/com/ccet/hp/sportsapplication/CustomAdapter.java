package com.ccet.hp.sportsapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.ArrayList;

/**
 * Created by HP on 14-01-2018.
 */

public class CustomAdapter extends ArrayAdapter<String>  {

    public Context context;
    public ArrayList<String> candidates;
    static public ArrayList<String> selected_student_list;

    public CustomAdapter(Context context, ArrayList<String> candidates){
        super(context, R.layout.candidate_layout, candidates);
//        this.selected_student_list = new ArrayList<> ();
        this.context = context;
        this.candidates = candidates;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from (context);
        convertView = layoutInflater.inflate (R.layout.candidate_layout, null);

        final CheckedTextView name_check = convertView.findViewById (R.id.candidate_check);
        name_check.setText (candidates.get (position));

        name_check.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                name_check.toggle ();
            }
        });

        return convertView;
    }
}
