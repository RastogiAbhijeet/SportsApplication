package com.ccet.hp.sportsapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    EditText user_name, pass_word;
    Button login, register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
        getSupportActionBar ().setDisplayOptions (ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar ().setCustomView (R.layout.title);
        }catch (NullPointerException e){
            e.printStackTrace ();
        }

        user_name = (EditText)findViewById(R.id.username_field);
        pass_word = (EditText)findViewById(R.id.password_field);
        login = (Button)findViewById(R.id.loginButton);
        register = (Button)findViewById(R.id.register);

        login.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                new LoginVerify ().execute (user_name.getText ().toString (), pass_word.getText ().toString ());
            }
        });

        register.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (MainActivity.this, RegisterationStudent.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity (i);
                finish();
            }
        });
    }



    public class LoginVerify extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            String username = strings[0];
            String password = strings[1];
            try {
                JSONObject js_obj = new JSONObject ();
                js_obj.put ("Username", username);
                js_obj.put ("Password", password);
                String json_string = js_obj.toString ();
//                Log.i (json_string, "Packet Sent : ");

                WebServiceConnect ws_connect = new WebServiceConnect ();
                response = ws_connect.makeServiceCall ("/emp/",json_string);

            }catch (JSONException e) {
                e.printStackTrace ();
            }

//            Log.i (response, "Packet Sent : ");
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute (response);

            try {
                if( !response.equals ("")){
                    JSONObject temp_obj = new JSONObject(response);
                    Toast.makeText (getApplicationContext (), temp_obj.get("role").toString (), Toast.LENGTH_SHORT).show ();
                    if( temp_obj.get("role").toString ().equals("student")){
                        Intent intent = new Intent (MainActivity.this, StudentActivity.class);
                        intent.putExtra ("key", response);
                        startActivity (intent);
                    }else{
                        Intent intent = new Intent (MainActivity.this, FacultyActivity.class);
                        intent.putExtra ("json_data", response);
                        startActivity (intent);
                    }

                    finish();
                }else {
                    Toast.makeText (getApplicationContext ()," Hey! Try to enter some details in fields meh!\n or Atleast Enter the Correct Password \n Hint : Its Your ROLL NUMBER", Toast.LENGTH_SHORT).show ();
                }
            } catch (JSONException e) {
                e.printStackTrace ();
            }
        }
    }
}
