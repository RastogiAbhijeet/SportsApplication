package com.ccet.hp.sportsapplication;

import android.os.AsyncTask;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class ResultFiltering extends AppCompatActivity {

    ListView genderListView, eventListView, positionView;
    ArrayAdapter<String> gender_Adapter, eventAdapter, positionAdapter;
    ArraySet<String> event_resultant, position_resultant;
    ArraySet<String> genderReq, eventReq, positionReq;

    Button getReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_result_filtering);

        position_resultant = new ArraySet<> ();
        position_resultant.add ("qualified");
        position_resultant.add("1");
        position_resultant.add("2");
        position_resultant.add("3");

        getReport = (Button)findViewById (R.id.getReport);

        genderReq = new ArraySet<>();
        eventReq = new ArraySet<>();
        positionReq = new ArraySet<> ();

        event_resultant = new ArraySet<> ();
        event_resultant.addAll (Arrays.asList (getResources ().getStringArray (R.array.event_list_male)));
        event_resultant.addAll (Arrays.asList (getResources ().getStringArray (R.array.event_list_male)));
        event_resultant.remove ("");

        final ArraySet<String> genderSet = new ArraySet<> ();
        genderSet.addAll (Arrays.asList (getResources ().getStringArray (R.array.gender_entries)));
        genderSet.remove("");

        gender_Adapter = new ArrayAdapter<> (getApplicationContext (), R.layout.candidate_layout, R.id.candidate_check,genderSet.toArray (new String[0]));
        genderListView = (ListView)findViewById (R.id.genderCheck);
        genderListView.setAdapter (gender_Adapter);
        setListViewHeightBasedOnChildren (genderListView);

        eventAdapter = new ArrayAdapter<> (getApplicationContext (), R.layout.candidate_layout, R.id.candidate_check,event_resultant.toArray (new String[0]));
        eventListView = (ListView)findViewById (R.id.event_selection_check);
        eventListView.setAdapter (eventAdapter);
        eventAdapter.notifyDataSetChanged ();

        positionAdapter = new ArrayAdapter<> (getApplicationContext (), R.layout.candidate_layout, R.id.candidate_check,position_resultant.toArray (new String[0]));
        positionView = (ListView)findViewById (R.id.position_selection_check);
        positionView.setAdapter (positionAdapter);
        positionAdapter.notifyDataSetChanged ();
        setListViewHeightBasedOnChildren (positionView);

        genderListView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView ctv = view.findViewById (R.id.candidate_check);
                if(ctv.isChecked ()){
                    ctv.setChecked (false);
                    genderReq.remove (ctv.getText ().toString ());

                }else{
                    ctv.setChecked(true);
                    genderReq.add (ctv.getText ().toString ());
                }
            }
        });

        eventListView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView ctv = view.findViewById (R.id.candidate_check);
                if(ctv.isChecked ()){
                    ctv.setChecked (false);
                    eventReq.remove (ctv.getText ().toString ());
                }else{
                    ctv.setChecked(true);
                    eventReq.add(ctv.getText ().toString ());
                }
            }
        });

        positionView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView ctv = view.findViewById (R.id.candidate_check);
                if(ctv.isChecked ()){
                    ctv.setChecked (false);
                    positionReq.remove (ctv.getText ().toString ());
                }else{
                    ctv.setChecked(true);
                    positionReq.add(ctv.getText ().toString ());
                }
            }
        });

        getReport.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject ();
                JSONArray gender = new JSONArray ();
                for(String s : genderReq){
                    gender.put (s);
                }

                JSONArray event = new JSONArray ();
                for(String s : eventReq){
                    event.put (s);
                }

                JSONArray position = new JSONArray ();
                for(String s : positionReq){
                    position.put (s);
                }

                try {
                    object.put("event", event);
                    object.put("gender", gender);
                    object.put("position", position);
                } catch (JSONException e) {
                    e.printStackTrace ();
                }

                getReport.setEnabled (false);
                new ReportAsync ().execute (object.toString ());
            }
        });
    }

    private class ReportAsync extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            WebServiceConnect service = new WebServiceConnect ();
            return service.makeServiceCall ("/emp/xls_generation/",strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            getReport.setEnabled (true);
            Toast.makeText (getApplicationContext (), s,Toast.LENGTH_LONG).show ();
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
