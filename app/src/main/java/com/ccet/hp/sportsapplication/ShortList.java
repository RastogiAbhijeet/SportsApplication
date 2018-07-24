package com.ccet.hp.sportsapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShortList extends AppCompatActivity {


    public ListView candidateList;
    Button updateshortlist;
    ArrayAdapter<String> ad;
    public Spinner gender_select, event_select;
    JSONObject packet;
    CheckedTextView cc;
    ArrayList<String> candidateNames;
    ArrayList<String> roll_no;
    ArrayList<String> rollPacket;

    Button changeEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_short_list);

        rollPacket = new ArrayList<> ();

        updateshortlist = (Button)findViewById (R.id.update_shortlist);
        updateshortlist.setVisibility (View.INVISIBLE);

        packet = new JSONObject ();

        gender_select = (Spinner)findViewById(R.id.spinner_short_list);
        event_select = (Spinner)findViewById (R.id.spinner_event_shortlist);
        event_select.setEnabled (false);

        candidateNames = new ArrayList<> ();

        roll_no = new ArrayList<> ();
        ad = new ArrayAdapter<String> (getApplicationContext (),R.layout.candidate_layout, R.id.candidate_check, candidateNames);
        candidateList = (ListView)findViewById (R.id.candidateList);
        candidateList.setAdapter (ad);
        candidateList.setChoiceMode (ListView.CHOICE_MODE_MULTIPLE);

        gender_select.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                candidateNames.clear ();
                String s = gender_select.getSelectedItem ().toString ();
                if(!s.equals ("")) {

                    event_select.setEnabled (true);
                    try {
                        packet.put ("gender", s);
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }

                    ArrayAdapter<CharSequence> adapter;
                    int resource = R.array.event_list_male;

                    if (s.equals ("Boys")) {
                        resource = R.array.event_list_male;
                    } else if (s.equals ("Girls")) {
                        resource = R.array.event_list_female;
                    }

                    adapter = ArrayAdapter.createFromResource (getApplicationContext (), resource, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
                    event_select.setAdapter (adapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        event_select.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!event_select.getSelectedItem ().toString ().equals ("")) {
                    try {
                        packet.put ("event", event_select.getSelectedItem ().toString ());
                        Toast.makeText (getApplicationContext (), packet.toString (), Toast.LENGTH_LONG).show ();
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }

                    event_select.setEnabled (false);
                    updateshortlist.setVisibility (View.VISIBLE);
                    new NameFetchAsync ().execute (packet.toString ());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        candidateList.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,int i, long l) {

                cc = view.findViewById (R.id.candidate_check);

                if(!cc.isChecked ()){
                    cc.setChecked (true);
                    rollPacket.add (roll_no.get (i));
                }else{
                    cc.setChecked (false);
                    try{
                        rollPacket.remove(i);
                    }catch(Exception e){
                        e.printStackTrace ();
                    }
                }
            }
        });



        changeEvent = (Button)findViewById (R.id.shortList_get_names);
        changeEvent.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                candidateNames.clear ();
                roll_no.clear ();
                rollPacket.clear ();

                ad.notifyDataSetChanged ();
                if(gender_select.isEnabled ()){
                    event_select.setEnabled (true);
                }
            }
        });

        updateshortlist.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                JSONArray dataArray = new JSONArray ();
                for(String roll: rollPacket){

                    JSONObject js = new JSONObject ();
                    try {
                        js.put("roll_no", roll);
                        js.put("position", "qualified");
                        js.put("event", event_select.getSelectedItem ());
                        dataArray.put (js);
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                }

                new UpdatePositionAsync().execute(dataArray.toString());
            }
        });
    }

    private class NameFetchAsync extends AsyncTask<String,Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            WebServiceConnect service = new WebServiceConnect ();
            return service.makeServiceCall ("/emp/getnames", strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute (s);

            try {
                JSONArray jsonArray = new JSONObject(s).getJSONArray ("student");

                for(int i = 0;i< jsonArray.length ();i++){
                    JSONObject js = jsonArray.getJSONObject (i);
                    candidateNames.add ("Name : "+js.getString ("name")+"\n"+"Roll no: "+js.getString ("roll_no")+"\n"+"Branch : "+js.getString ("branch")+"\n"+"Year : "+js.getString ("year")+"\n"+"Position : "+js.getString ("position"));
                    roll_no.add (js.getString ("roll_no"));
                    ad.notifyDataSetChanged ();
                }

            } catch (JSONException e) {
                e.printStackTrace ();
            }

        }
    }

    public class UpdatePositionAsync extends AsyncTask<String,Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            WebServiceConnect service = new WebServiceConnect ();
            return service.makeServiceCall ("/emp/shortlist", strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute (s);
            Toast.makeText (getApplicationContext (), s, Toast.LENGTH_SHORT).show ();
        }
    }



}


