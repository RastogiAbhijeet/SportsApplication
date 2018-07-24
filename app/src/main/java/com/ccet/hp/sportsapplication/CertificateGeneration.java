package com.ccet.hp.sportsapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CertificateGeneration extends AppCompatActivity {
    JSONObject js_obj;
    Spinner eventspinner;
    ArrayList<String> events;
    ArrayList<HashMap<String ,String>> eventListMap;
    HashMap<String, String> hashEvent;
    ArrayAdapter<String> eventAdapter;
    TextView eventDetails;
    Button download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_certificate_generation);

        try {
            getSupportActionBar ().setDisplayOptions (ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar ().setCustomView (R.layout.title);
        } catch (NullPointerException e) {

        }

        initialise();

        Intent intent = getIntent ();
        String s = intent.getStringExtra ("key");


        try {
            js_obj = new JSONObject (s);
            new AsyncEventFetch().execute (js_obj.getString ("roll_no"));
        } catch (JSONException e) {
            e.printStackTrace ();
        }

        eventspinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                TextView value = (TextView) view;

                if(i!=0) {

                    String s = value.getText ().toString ().substring (0, value.getText ().toString ().indexOf ("|")-1);
                    String position = hashEvent.get (value.getText ().toString ());

                    String filler = null;
                    try {
                        filler = "Roll No : " + js_obj.getString ("roll_no")+
                                "\nName : "+js_obj.getString ("name")+
                                "\nBranch : "+js_obj.getString ("branch")+
                                "\nEvent Name : "+ s +
                                "\nPosition : "+ position ;
                        js_obj.put ("event", s);
                        js_obj.put ("position", position);
                        Log.i ("JsObj : ", js_obj.toString ());
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                    eventDetails.setText (filler);


                }else{
                    value.setTextColor (Color.GRAY);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        download.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                new AsyncCertiDownload().execute(js_obj.toString ());
            }
        });



    }

    void initialise(){

        eventDetails = (TextView)findViewById (R.id.certificate_event_detail);
        events = new ArrayList<> ();
        eventspinner = (Spinner)findViewById (R.id.certificate_spinner);
        eventListMap = new ArrayList<>();
        hashEvent = new HashMap<> ();
        eventAdapter = new ArrayAdapter<> (getApplicationContext (), android.R.layout.simple_spinner_item, events);
        eventspinner.setAdapter (eventAdapter);
        download = (Button)findViewById (R.id.certificate_download);

    }

    private class AsyncEventFetch extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            WebServiceConnect wsc = new WebServiceConnect ();
            return wsc.makeServiceCall ("/emp/event_profile/", strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute (s);

            try {

                JSONArray array = new JSONObject (s).getJSONArray ("event");
                JSONArray position = new JSONObject (s).getJSONArray ("position");

                events.add ("(Select the Event)");
                for(int i = 0; i < array.length (); i++){
                    Log.i ("Position", position.get (i).toString ());
                    if(position.get (i).toString ().equals ("1") || position.get (i).toString ().equals ("2") || position.get (i).toString ().equals ("3")) {
                        events.add (array.getString (i));
                        hashEvent.put (array.getString (i), position.getString (i));
                        eventListMap.add (hashEvent);
                    }
                }
                if(events.size () == 1){

                        Toast.makeText (getApplicationContext (), "You have not won in any event as of now", Toast.LENGTH_SHORT).show ();

                }

                eventAdapter.notifyDataSetChanged ();

            }catch (JSONException e){
                e.printStackTrace ();
            }
        }
    }

    private class AsyncCertiDownload extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {

            WebServiceConnect service = new WebServiceConnect ();
            return service.downloadFile ("/emp/certidownload/", strings[0], getApplicationContext ());

        }
    }


}
