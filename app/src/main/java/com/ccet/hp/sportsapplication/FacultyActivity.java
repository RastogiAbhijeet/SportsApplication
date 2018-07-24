package com.ccet.hp.sportsapplication;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FacultyActivity extends AppCompatActivity {

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
        setContentView (R.layout.activity_faculty);



        try {
            getSupportActionBar ().setDisplayOptions (ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar ().setCustomView (R.layout.title);
        }catch(NullPointerException e){
            getSupportActionBar ().setCustomView (R.layout.title);
        }

        Button report = (Button) findViewById(R.id.report_faculty);
        report.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (FacultyActivity.this, EventReport.class);
                startActivity (i);
            }
        });

        Button shortListButton = (Button)findViewById (R.id.shortList);
        shortListButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FacultyActivity.this, ShortList.class);
                startActivity (i);
            }
        });

        Button reports = (Button)findViewById (R.id.buttondownloadresult);
        reports.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FacultyActivity.this, ResultFiltering.class);
                startActivity (i);
            }
        });

        Button notices = (Button)findViewById (R.id.faculty_download_notice);
        notices.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FacultyActivity.this, DownloadForms.class);
                startActivity (i);
            }
        });

    }
}
