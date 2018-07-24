package com.ccet.hp.sportsapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Student_Event_Reg extends AppCompatActivity {

    ArraySet<String> list_events;
    JSONArray events;
    JSONObject profile;
    String intentData;
    List<String> event_array;
    Spinner event_type_spinner;
    ArrayAdapter<String> tempAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_student__event__reg);

        try{
            getSupportActionBar ().setDisplayOptions (ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar ().setCustomView (R.layout.title);
        }catch (NullPointerException e){
            e.printStackTrace ();
        }

        event_array = new ArrayList<> ();

        Intent dataIntent = getIntent();
        intentData = dataIntent.getStringExtra ("key");
        try {
           profile = new JSONObject (intentData);
        } catch (JSONException e) {
            e.printStackTrace ();
        }

        event_type_spinner = (Spinner)findViewById (R.id.inter_athletic_spinner);

        // Check Gender of the Student then Proceed.
        Button register = (Button)findViewById(R.id.button_registration);

        list_events = new ArraySet<> ();

        Spinner sp_one = (Spinner)findViewById (R.id.eventlist_one);
        Spinner sp_two = (Spinner)findViewById (R.id.eventlist_two);
        Spinner sp_three = (Spinner)findViewById (R.id.eventlist_three);
        Spinner sp_four = (Spinner)findViewById (R.id.eventlist_four);
        Spinner sp_five = (Spinner)findViewById (R.id.eventlist_five);

        tempAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,event_array);
        tempAdapter.notifyDataSetChanged ();

        sp_one.setAdapter (tempAdapter);
        sp_two.setAdapter (tempAdapter);
        sp_three.setAdapter (tempAdapter);
        sp_four.setAdapter (tempAdapter);
        sp_five.setAdapter (tempAdapter);


        sp_one.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                list_events.add (event_array.get (i));
//                Toast.makeText (getApplicationContext(),list_events.toString (), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_two.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                list_events.add (event_array.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_three.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                list_events.add (event_array.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_four.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                list_events.add (event_array.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_five.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                list_events.add (event_array.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        event_type_spinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(event_type_spinner.getSelectedItem ().toString ().equals ("Athletic Meet")){

                    try {
                        if(profile.getString ("gender").equals ("Boy")) {
                            event_array.clear ();
                            String[] tempArr = getResources().getStringArray (R.array.event_list_male);

                            for( String s : tempArr){
                                event_array.add (s);
                            }

                            tempAdapter.notifyDataSetChanged ();
                        }else{
                            event_array.clear ();
                            String[] tempArr = getResources().getStringArray (R.array.event_list_female);

                            for( String s : tempArr){
                                event_array.add (s);
                            }
                            tempAdapter.notifyDataSetChanged ();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }

                }else if(event_type_spinner.getSelectedItem ().toString ().equals ("Inter Year")){
                    event_array.clear ();
                    String[] tempArr = getResources().getStringArray (R.array.event_list_inter_year);

                    for( String s : tempArr) {
                        event_array.add (s);
                    }
                    tempAdapter.notifyDataSetChanged ();
                }


//                    Toast.makeText (getApplicationContext (),event_type_spinner.getSelectedItem ().toString (), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        register.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                list_events.remove ("");
                events = new JSONArray (list_events);

                JSONObject data = new JSONObject ();
                try {
                    data.put ("roll_no", profile.getString("roll_no"));
                    data.put ("event_type",event_type_spinner.getSelectedItem ().toString ());
                    data.put ("event", events);
                } catch (JSONException e) {
                    e.printStackTrace ();
                }

                new AsyncReg ().execute (data.toString ());
//                Toast.makeText (getApplicationContext (),events.toString (), Toast.LENGTH_SHORT).show();
            }
        });
     }

    public class AsyncReg extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            WebServiceConnect wsc = new WebServiceConnect ();
            return wsc.makeServiceCall ("/emp/events/", strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute (s);
            Toast.makeText (getApplicationContext (),s, Toast.LENGTH_LONG).show ();
            Intent i = new Intent (Student_Event_Reg.this, StudentActivity.class);
            i.putExtra ("key", profile.toString ());
            startActivity (i);
        }
    }

}
