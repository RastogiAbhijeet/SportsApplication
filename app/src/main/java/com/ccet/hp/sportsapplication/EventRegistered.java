package com.ccet.hp.sportsapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventRegistered extends AppCompatActivity {


    List<String> param_adapter;
    RecyclerView recyclerView;
    EventRegisteredAdapter adapter;
    JSONObject js_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_event_registered);

        getSupportActionBar ().setDisplayOptions (ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar ().setCustomView (R.layout.title);

        Intent intent = getIntent ();
        String s = intent.getStringExtra ("key");

        try {
            js_obj = new JSONObject (s);
        } catch (JSONException e) {
            e.printStackTrace ();
        }

        param_adapter = new ArrayList<>();
        try {
            adapter = new EventRegisteredAdapter (getApplicationContext (), param_adapter, js_obj.getString ("roll_no") );
        } catch (JSONException e) {
            e.printStackTrace ();
        }

        recyclerView = (RecyclerView)findViewById (R.id.recycleEvents);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager (getApplicationContext ());
        recyclerView.setLayoutManager (layoutManager);
        recyclerView.setItemAnimator (new DefaultItemAnimator ());
        recyclerView.setAdapter (adapter);


        try {

            new AsyncEventFetch ().execute (js_obj.getString ("roll_no"));
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace ();
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }

    private class AsyncEventFetch extends AsyncTask<String, Void, String> {
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

                for(int i = 0; i<array.length ();i++){
                    param_adapter.add (array.getString (i));
                }

                adapter.notifyDataSetChanged ();

            } catch (JSONException e) {
                e.printStackTrace ();
            }catch(ArrayIndexOutOfBoundsException e){
                e.printStackTrace ();
                Toast.makeText (getApplicationContext (),"Hey! Seems like we caught a bug on your Device :O !",Toast.LENGTH_SHORT).show ();
            }
        }
    }
}
