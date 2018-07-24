package com.ccet.hp.sportsapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ProfileStudent extends AppCompatActivity {

    ListView ls;
//    , ls_events;
    JSONObject js_obj;
    HashMap<String, String> hm;
    HashMap<String, String> hm_events;
    StudentAdapter stu_adt;
//    , event_adt;
    ArrayList<HashMap<String, String>> param_adapter;
    ArrayList<HashMap<String, String>> param_adapter_events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_profile_student);

            try {
                getSupportActionBar ().setDisplayOptions (ActionBar.DISPLAY_SHOW_CUSTOM);
                getSupportActionBar ().setCustomView (R.layout.title);
            }catch(NullPointerException e){

            }

        Intent intent = getIntent ();
        String s = intent.getStringExtra ("key");

        param_adapter= new ArrayList<> ();
        param_adapter_events=new ArrayList<> ();

        try {
            js_obj = new JSONObject (s);
        } catch (JSONException e) {
            e.printStackTrace ();
        }

        hm = new HashMap<> ();
        hm_events = new HashMap<> ();

        Iterator<String> iterator = js_obj.keys ();

        while (iterator.hasNext ()) {
            try {
                String iter = iterator.next ();
                hm.put (iter.toUpperCase (), js_obj.getString (iter));
                param_adapter.add (hm);
            } catch (JSONException e) {
                e.printStackTrace ();
            }
        }

        stu_adt= new StudentAdapter (this, R.layout.student_adapter, param_adapter);
        ls = (ListView)findViewById (R.id.list);
        ls.setAdapter (stu_adt);
        setListViewHeightBasedOnChildren (ls);

//        event_adt = new StudentAdapter (this, R.layout.student_adapter, param_adapter_events);
//        ls_events = (ListView)findViewById (R.id.list_events);
//        ls_events.setAdapter (event_adt);
//        setListViewHeightBasedOnChildren (ls_events);



//        try {
//            new AsyncEventFetch().execute(js_obj.getString ("roll_no"));
//        } catch (JSONException e) {
//            e.printStackTrace ();
//        }
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

