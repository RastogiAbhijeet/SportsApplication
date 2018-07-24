package com.ccet.hp.sportsapplication;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterationStudent extends AppCompatActivity {

    Button register, verifyButton;
    EditText name, roll_no,mobile, email;
    Spinner branch, semester,gender, type;
    JSONObject data_map;
    LinearLayout linearLayout;
    EditText verificationCode;
    String code;
    CheckedTextView checkedText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_registeration_student);

        initialise ();
        try{
        getSupportActionBar ().setDisplayOptions (ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar ().setDisplayUseLogoEnabled (true);
        getSupportActionBar ().setCustomView (R.layout.title);
        }catch(Exception e){

        }



        checkedText.setMovementMethod (new ScrollingMovementMethod ());
        linearLayout.setVisibility (View.INVISIBLE);
        verifyButton.setEnabled (false);
        data_map = new JSONObject ();

        register = (Button)findViewById (R.id.register_final);
        register.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {

                String s = email.getText ().toString ();
                if(s.contains ("@")) {
                    linearLayout.setVisibility (View.VISIBLE);
                    sendMail (RegisterationStudent.this);
                }else{
                    Toast.makeText (getApplicationContext (), "Please Enter Valid email Address", Toast.LENGTH_SHORT).show ();
                    email.setText ("");
                }


            }
        });

        verifyButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if(verificationCode.getText ().toString ().equals (code)){
                    register.setEnabled (false);
                    sendData();
                }else{
                    verificationCode.setText("");
                    Toast.makeText (getApplicationContext (),"Code Incorrect", Toast.LENGTH_SHORT).show ();
                }
            }
        });

    }

    private class RegisterAsync extends AsyncTask<JSONObject, Void, String> {

        @Override
        protected String doInBackground(JSONObject... jsonObjects) {
            WebServiceConnect serviceCall = new WebServiceConnect ();
            String dataPacket = jsonObjects[0].toString ();
            return serviceCall.makeServiceCall ("/emp/register/",dataPacket);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute (s);
            Intent i ;
            if(s.equals ("success")){
                Toast.makeText (getApplicationContext (),"You Have Successfully Registered \n Please Login to see Profile\nUsername and password are your roll no ",Toast.LENGTH_SHORT ).show ();
                i = new Intent (RegisterationStudent.this, MainActivity.class);
                startActivity(i);
            }else if(s.equals ("Record Exists")){
                Toast.makeText (getApplicationContext (), s,Toast.LENGTH_SHORT ).show ();
                i = new Intent (RegisterationStudent.this, MainActivity.class);
                startActivity(i);
            }else if(s.equals("Can't find Roll No in the records \nContact Admin")){
                Toast.makeText (getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText (getApplicationContext (), s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class SendMail extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            WebServiceConnect serviceCall = new WebServiceConnect ();
            return serviceCall.makeServiceCall ("/emp/sendVerification/",strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute (s);
            register.setEnabled (false);
            Toast.makeText (getApplicationContext (), "Code Sent\n Please check mail and enter the code", Toast.LENGTH_SHORT).show();
            verifyButton.setEnabled (true);
            code = s;
        }
    }

    public boolean data_validation(){

        if(mobile.getText ().length () == 10){
            return true;
        }else{
            return false;
        }
    }

    public void sendMail(Context context){
        new SendMail().execute(email.getText ().toString ());
    }

    public void sendData(){

                        if(data_validation()){
                    try{
                        data_map.put ("name", name.getText ().toString ());
                        data_map.put ("roll_no", roll_no.getText ().toString ().toLowerCase ());
                        data_map.put("mobile", mobile.getText ().toString ());
                        data_map.put ("email",email.getText ().toString ());
                        data_map.put ("semester",semester.getSelectedItem ().toString ());
                        data_map.put ("branch", branch.getSelectedItem ().toString ());
                        data_map.put("role", "student");
                        data_map.put("gender", gender.getSelectedItem ().toString ());
                        data_map.put("college", type.getSelectedItem ().toString ());
                        if((Integer.parseInt (semester.getSelectedItem ().toString ()))%2 == 0){
                            data_map.put ("year", String.valueOf ((Integer.parseInt (semester.getSelectedItem ().toString ()))/2));
                        }else{
                            data_map.put ("year", String.valueOf (((Integer.parseInt (semester.getSelectedItem ().toString ()))/2) + 1));
                        }

                        new RegisterAsync ().execute (data_map);

                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                }else{
                    Toast.makeText (getApplicationContext (), "Please enter Correct Info", Toast.LENGTH_SHORT).show ();
                    roll_no.setText ("");
                    mobile.setText ("");
                }

    }

    public void initialise(){
        linearLayout = (LinearLayout)findViewById (R.id.verificatioLayout);
        checkedText = (CheckedTextView)findViewById (R.id.oathCheck);

        name = (EditText) findViewById (R.id.name);
        roll_no = (EditText) findViewById(R.id.roll_no);

        mobile = (EditText) findViewById (R.id.mobile);
        email = (EditText) findViewById (R.id.email);
        branch = (Spinner)findViewById (R.id.branch_spinner);
        semester = (Spinner)findViewById (R.id.semester);
        gender = (Spinner)findViewById(R.id.gender_spinner);
        type = (Spinner)findViewById (R.id.collegeTypeSpinner);

        verificationCode =(EditText)findViewById (R.id.verficationCode);
        verifyButton = (Button)findViewById (R.id.verificationButton);

    }

}
