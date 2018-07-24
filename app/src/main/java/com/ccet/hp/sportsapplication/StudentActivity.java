package com.ccet.hp.sportsapplication;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StudentActivity extends AppCompatActivity {

    JSONObject userDetails;
    TextView welcome;
    ImageView profile, event_register, event_list;
    Button download, eventReg;

    String s;


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_student);

        getSupportActionBar ().setDisplayOptions (ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar ().setCustomView (R.layout.title);

        verifyStoragePermissions (this);

        welcome = (TextView)findViewById (R.id.welcome_message);

        Intent i = getIntent ();
        s = i.getStringExtra ("key");

        if(!s.equals ("")){
            try {
                userDetails = new JSONObject (s);
                welcome.setText ("Welcome : "+userDetails.getString ("name")+"\n"+"(Click to view Profile)" );
            } catch (JSONException e) {
                e.printStackTrace ();
            }
        }

        profile = (ImageView)findViewById (R.id.profile);
        profile.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StudentActivity.this, ProfileStudent.class);
//                i.addFlags(Intent.);
                i.putExtra ("key", s);
                startActivity (i);
            }
        });

        event_register = (ImageView)findViewById (R.id.registration);
        event_register.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (StudentActivity.this, Student_Event_Reg.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra ("key", s);
                startActivity (i);

            }
        });

        event_list = (ImageView)findViewById (R.id.eventlist);
        event_list.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (StudentActivity.this,Main2Activity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity (i);
            }
        });

        download = (Button)findViewById(R.id.download);
        download.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (StudentActivity.this,CertificateGeneration.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra ("key", s);
                startActivity (i);
            }
        });

        eventReg = (Button)findViewById(R.id.eventReg);
        eventReg.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (StudentActivity.this,EventRegistered.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.putExtra ("key", s);
                startActivity (i);
            }
        });
    }

    public class Download extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... voids) {

            try {
//                URL url = new URL ("http://192.168.178.34:8000"+"/emp/pdf_download/");
                URL url = new URL ("http://54.209.85.6:8000"+"/emp/pdf_download/");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection ();
                conn.setRequestMethod ("GET");
                conn.setConnectTimeout (15000);
                conn.setReadTimeout (15000);
                conn.connect ();

                Log.i("Data Type", conn.getContentType ().toString ());

                String content_type = conn.getContentType ();
                String type = content_type.substring (content_type.indexOf ('/'), content_type.length ());

                switch (type){
                    case "vnd.openxmlformats-officedocument.wordprocessingml.document":

                }

                String filename=  "Heheh" + ".pdf";

                File file = new File (Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_DOWNLOADS)+"/"+filename);

                InputStream inputStream = null;
                OutputStream outputStream = null;

                try{

                    byte[] fileReader = new byte[1024];
                    long fileSize = conn.getContentLength ();
                    long fileSizeDownloaded = 0;

                    inputStream = conn.getInputStream ();
                    outputStream = new FileOutputStream (file);
//                    Log.i("User Download Tag : ", Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_DOWNLOADS).toString ());
                    while(true){
                        int read = inputStream.read(fileReader);
                        if(read == -1){
                            break;
                        }

                        outputStream.write(fileReader, 0, read);
                        fileSizeDownloaded += read;

//                        Log.i("Data Downloaded : ","File downloaded " + fileSizeDownloaded + " of "+fileSize);
                    }

                    outputStream.flush ();
                    return null;

                }catch(IOException e) {
                    return e.toString ();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText (getApplicationContext (), s, Toast.LENGTH_SHORT).show();
        }
    }
}
