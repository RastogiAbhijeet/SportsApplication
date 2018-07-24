package com.ccet.hp.sportsapplication;

import android.app.NotificationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DownloadForms extends AppCompatActivity {

    List<String> noticeList;
    DownloadAdapter downloadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_download_forms);

        noticeList = new ArrayList<> ();

        downloadAdapter = new DownloadAdapter (getApplicationContext (), noticeList);
        RecyclerView rv = (RecyclerView)findViewById (R.id.noticeRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager (getApplicationContext ());
        rv.setLayoutManager (layoutManager);
        rv.setItemAnimator (new DefaultItemAnimator ());
        rv.setAdapter (downloadAdapter);

        new AsyncNoticeList().execute();
    }

    public class AsyncNoticeList extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                WebServiceConnect webServiceConnect = new WebServiceConnect ();

                return webServiceConnect.makeServiceCall ("/emp/noticeList/", "data");
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute (s);
            try {
                JSONObject jsonObject = new JSONObject (s);
                JSONArray jsonArray = jsonObject.getJSONArray ("files");
                for(int i = 0; i<jsonArray.length ();i++){
                    noticeList.add(jsonArray.getString (i));
                }
                downloadAdapter.notifyDataSetChanged ();
            } catch (JSONException e) {
                e.printStackTrace ();
            }

        }
    }
}
