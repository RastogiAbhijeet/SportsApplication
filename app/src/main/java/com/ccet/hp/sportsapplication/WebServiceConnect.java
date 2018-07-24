package com.ccet.hp.sportsapplication;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


import static android.content.Context.NOTIFICATION_SERVICE;


class WebServiceConnect {

    public URL url;
    public HttpURLConnection conn;
    long fileSizeDownloaded;
    long fileSize;

//    String IP = "http://192.168.178.35:8000";
  String IP = "http://54.209.85.6:8000";
//  String IP  = "http://192.168.4.20:8000";
//  String IP = "http://192.168.43.35:8000";


    public String makeServiceCall(String directory,String data){
        String dataPacket = "";
        try {
            url = new URL(IP+directory);
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            sendData(data);

            try{
                Log.i("Data Type", conn.getHeaderField ("filename"));
            }catch(Exception e){

            }


            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader (conn.getInputStream()));

                while((line = br.readLine())!= null){
                    dataPacket = line + dataPacket;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return dataPacket;
    }

    public String downloadFile(String directory, String data, Context applicationContext){

        NotificationCompat.Builder mbuilder;
        NotificationManager mNotifyMgr;
        int mNotification_id = 1;

        mbuilder = (NotificationCompat.Builder) new NotificationCompat.Builder (applicationContext)
                .setSmallIcon (R.drawable.collegelogo)
                .setContentTitle ("Downloads")
                .setContentText ("Downloading ...");

        mNotifyMgr = (NotificationManager)applicationContext.getSystemService (NOTIFICATION_SERVICE);


        try{
            url = new URL (IP+directory);
            conn = (HttpURLConnection)url.openConnection ();
            conn.setRequestMethod ("POST");
            conn.setConnectTimeout (15000);
            conn.setReadTimeout (15000);

            sendData (data);

            String file_name = conn.getHeaderField ("File-Name");

            Log.i("file Name : ", file_name);

            String folder = "";
            switch (directory){
                case "/emp/noticeList/":
                    folder = "/NOTICES/";
                    break;
                case "/emp/certidownload/":
                    folder = "/CERTIFICATES/";
                    break;
            }

            File file = new File(Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_DOWNLOADS)+folder+file_name);

            if(!file.exists ()){
                File temp = new File(Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_DOWNLOADS)+folder);
                temp.mkdirs ();
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;

            byte[] filereader = new byte[1024];
            fileSize = conn.getContentLength ();
            fileSizeDownloaded = 0;

            inputStream = conn.getInputStream ();
            outputStream = new FileOutputStream (file);

            while(true){
                int read = inputStream.read(filereader);
                if(read == -1){
                    break;
                }

                    mbuilder.setProgress (100, (int)((fileSizeDownloaded/fileSize)*100), true);
                    mNotifyMgr.notify (mNotification_id, mbuilder.build ());
                    outputStream.write(filereader, 0, read);
                    fileSizeDownloaded += read;
                    Log.i("Data Downloaded : ","File downloaded " + fileSizeDownloaded + " of "+fileSize);
            }

                mbuilder.setContentTitle ("DOWNLOAD COMPLETE");
                mbuilder.setContentText ("File Downloaded at : " + Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_DOWNLOADS)+"folder"+file_name);
                mbuilder.setProgress (0,0,false);
                mNotifyMgr.notify (mNotification_id, mbuilder.build ());

                switch (directory){
                    case "/emp/noticeList/":
                        Log.i("MESSAGE : ", "NOTICE DOWNLOADED");
                        break;
                    case "/emp/certidownload/":
                        Log.i ("MESSAGE : ", "CERTIFICATE DOWNLOAD");
                        break;


                }

                outputStream.flush ();
                return "File Downloaded";
            } catch (FileNotFoundException e1) {
                    e1.printStackTrace ();
                } catch (ProtocolException e1) {
                    e1.printStackTrace ();
                } catch (MalformedURLException e1) {
                    e1.printStackTrace ();
                } catch (IOException e1) {
                    e1.printStackTrace ();
                }

        return null;
    }

    public void sendData(String Data){
        byte[] b_data = Data.getBytes();
        OutputStream os;
        try {
            os = conn.getOutputStream();
            os.write(b_data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
