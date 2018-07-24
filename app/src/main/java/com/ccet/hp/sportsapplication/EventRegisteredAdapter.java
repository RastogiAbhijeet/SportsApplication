package com.ccet.hp.sportsapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by HP on 25-02-2018.
 */

public class EventRegisteredAdapter extends RecyclerView.Adapter<EventRegisteredAdapter.MyViewHolder>{
    private List<String> eventList;
    public Context applicationContext;
    public String roll;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView events;
        public Button deleteButton;

        MyViewHolder(View view){
            super(view);
            events = view.findViewById (R.id.eventName);
            deleteButton = view.findViewById (R.id.eventDelete);

        }
    }

    public EventRegisteredAdapter(Context applicationContext, List<String> events, String roll){
        this.eventList = events;
        this.applicationContext = applicationContext;
        this.roll = roll;
    }

    @Override
    public EventRegisteredAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from (parent.getContext ()).inflate (R.layout.event_updation, parent, false);
        return new EventRegisteredAdapter.MyViewHolder (itemView);
    }

    @Override
    public void onBindViewHolder(final EventRegisteredAdapter.MyViewHolder holder,final int position) {
        Log.i("TAg : : ", String.valueOf (eventList.get(position)));
        holder.events.setText (String.valueOf (position+1) + ".     " + eventList.get (position));
        holder.deleteButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                JSONObject js = new JSONObject ();
                try {
                    js.put ("roll_no", roll);
                    js.put ("event", eventList.get (position));
                    eventList.remove (position);
                    notifyDataSetChanged ();
                    new AsyncDelete().execute(js.toString ());
                } catch (JSONException e) {
                    e.printStackTrace ();
                }

            }
        });

    }

    public class AsyncDelete extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            WebServiceConnect wsc = new WebServiceConnect ();
            return wsc.makeServiceCall ("/emp/deleteEvent/", strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute (s);
                Toast.makeText (applicationContext, s, Toast.LENGTH_LONG).show ();

        }
    }

    @Override
    public int getItemCount() {
        return eventList.size ();
    }

}
