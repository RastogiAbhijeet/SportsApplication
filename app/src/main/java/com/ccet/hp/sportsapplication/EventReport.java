package com.ccet.hp.sportsapplication;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EventReport extends AppCompatActivity {

    Spinner gender_select, event_select, name_select,position_select;

    ArrayList<String> names; // this is the array list for student Name
    ArrayAdapter<String> adapter_names;
    HashMap<String, String> name_position_map;
    ArrayList<HashMap<String, String>> name_list;
    ArrayAdapter<CharSequence> adapter;
    ListViewReportAdapter adapter_table;

    Button updateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_event_report);

        /*       Algorithm        */
//        /*
//        *  this module will be divide into severals portions and
//        *  will have the following steps
//        *  Initial :  on Creation of login
//        *           - One Spinner will ask for Event Input
//        *           - Once the event is selected, then a server request should be made
//        *           in order to check that whether any positions have been previously alloted or not.
//        *           - If YES, the we display the result and give the faculty an opportunity to enter
//        *           a position that has not been filled
//        *           - If NO, then the Faculty has to select amongst the students that have participated
//        *           in the event till date.
//        *           {
//        *               1. Make a server request and get the details of the students that have participated
//        *               in the event.
//        *               2. fill the spinner for that particular position
//        *               3. select student.
//        *               4. give a short preview of details of the candidate.
//        *           }
//        *
//        *           - SUBMIT button will take the data from activity and then push that data to the
//        *           server and later to the database.
//        *           # On Submission also ask for confirmation.
//        *           - Cancel Button would simply undo all the changes made in that session.
//        *
//        *
//        */

        names = new ArrayList<> ();
        name_position_map = new HashMap<> ();
        name_list = new ArrayList<> ();

        updateButton = (Button)findViewById (R.id.submit);
        updateButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                JSONArray dataArray = new JSONArray ();
                for(int i = 0;i < 3;i++){
                    JSONObject js = new JSONObject ();
                    try {
                        js.put("roll_no", name_select.getSelectedItem ().toString ());
                        js.put("position", position_select.getSelectedItem ().toString ());
                        js.put("event", event_select.getSelectedItem ());
                        dataArray.put (js);
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                }

                Toast.makeText (getApplicationContext (), dataArray.toString (), Toast.LENGTH_SHORT).show ();
                new UpdatePositionAsync ().execute(dataArray.toString());
            }
        });

        try{
        getSupportActionBar ().setDisplayOptions (ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar ().setCustomView (R.layout.title);
        }catch(NullPointerException e){
            e.printStackTrace ();
        }

        gender_select = (Spinner)findViewById(R.id.gender_selection);
        event_select = (Spinner)findViewById (R.id.event_selection);
        name_select = (Spinner)findViewById (R.id.name_selection);
        position_select = (Spinner)findViewById(R.id.position_selection);

        event_select.setEnabled (false);
        position_select.setEnabled (false);
        name_select.setEnabled (false);

        adapter_names = new ArrayAdapter<> (getApplicationContext (),android.R.layout.simple_spinner_item,names);
        adapter_names.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
        name_select.setAdapter (adapter_names);

        // In the server request that is made on selecting the event should be like this:

//          {
//              "gender":"",
//              "event_name":"",
//          }

////       The above request will fetch the data in the following form
//
//        /*
//        *       {
//        *           "name":"xcs"
//        *           "roll_no:"co15302"
//        *           "events":[
//        *             "event_name":"100m",
//          *           "position":"ND",
//          *           "parameter":"40sec",
//          *           "event_score": 0 <Is subjected to change only in the case when a position is awarded>
//        *           ]
//        *           "players_score":< sum(events_participated*event_score)>
//        *       }
//        *
//        *       The position will be allocation here
//        * */

        adapter_table= new ListViewReportAdapter (getApplicationContext (), R.layout.position_table_faculty,name_list);


        gender_select.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String s = gender_select.getSelectedItem ().toString ();

                int resource = R.array.event_list_male;

                if(s.equals ("Boys")){
                    resource = R.array.event_list_male;
                }else if(s.equals ("Girls")){
                    resource = R.array.event_list_female;
                }

                adapter = ArrayAdapter.createFromResource (getApplicationContext (), resource, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
                adapter.notifyDataSetChanged ();
                event_select.setAdapter (adapter);

                if(!s.equals ("")) {
                    event_select.setEnabled (true);
                    position_select.setEnabled (true);
//                    name_select.setEnabled (true);
                }else {
                    event_select.setEnabled (false);
                    position_select.setEnabled (false);
                    name_select.setEnabled (false);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        position_select.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!position_select.getSelectedItem ().toString ().equals (""))
                    name_select.setEnabled (true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        event_select.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText (getApplicationContext (), event_select.getSelectedItem ().toString (), Toast.LENGTH_SHORT).show();
                ((TextView)view).setTextColor(Color.BLACK);
                if(!event_select.getSelectedItem ().toString ().equals ("")){
                    new NameFetchAsync().execute(gender_select.getSelectedItem ().toString (), event_select.getSelectedItem ().toString ());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        name_select.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)view).setTextColor (Color.BLACK);

                if(!name_select.getSelectedItem ().toString ().equals ("")) {
                    name_position_map.put (position_select.getSelectedItem ().toString (), name_select.getSelectedItem ().toString ());

                    if (!name_position_map.containsKey ("")){
                        name_list.add (name_position_map);
                        adapter_table.notifyDataSetChanged ();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ListView position_name_table = (ListView)findViewById (R.id.list_position_name);
        position_name_table.setAdapter (adapter_table);
    }

    public class NameFetchAsync extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {

            String gender = strings[0];
            String event = strings[1];

            JSONObject jsObj = new JSONObject ();
            try {
                jsObj.put ("gender", gender);
                jsObj.put ("event",event);
            } catch (JSONException e) {
                e.printStackTrace ();
            }

            WebServiceConnect service = new WebServiceConnect ();

            return service.makeServiceCall ("/emp/getnames", jsObj.toString ());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute (s);

            Toast.makeText (getApplicationContext (), s, Toast.LENGTH_SHORT).show ();

            try {
                names.clear ();
                JSONObject obj = new JSONObject (s);
                JSONArray array = obj.getJSONArray ("student");

                names.add ("");
                for(int i = 0;i<array.length ();i++){
                    JSONObject js_obj = array.getJSONObject (i);
                    names.add (js_obj.getString ("roll_no"));
                    adapter_names.notifyDataSetChanged ();
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
            return service.makeServiceCall ("/emp/position", strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute (s);
            Toast.makeText (getApplicationContext (), s, Toast.LENGTH_SHORT).show ();
        }
    }
}
