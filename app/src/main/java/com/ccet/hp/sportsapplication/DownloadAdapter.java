package com.ccet.hp.sportsapplication;


import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.MyViewHolder>{

    private List<String> noticeList;
    public Context applicationContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView Sno, notice;
        public Button download;

        MyViewHolder(View view){
            super(view);

            Sno = view.findViewById (R.id.SNoNotices);
            notice = view.findViewById (R.id.NoticeNames);
            download = view.findViewById (R.id.downloadNotices);
        }
    }

    public DownloadAdapter(Context applicationContext, List<String> noticeList){
        this.noticeList = noticeList;
        this.applicationContext = applicationContext;
    }

    @Override
    public DownloadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from (parent.getContext ()).inflate (R.layout.noticedownload, parent, false);
        return new MyViewHolder (itemView);
    }

    @Override
    public void onBindViewHolder(final DownloadAdapter.MyViewHolder holder,final int position) {

        holder.Sno.setText (String.valueOf (position+1));
        holder.notice.setText(noticeList.get (position).substring (0, noticeList.get(position).indexOf ('.')));
        holder.download.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                downloadFile(noticeList.get (position));
                Toast.makeText (applicationContext, holder.notice.getText (), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size ();
    }

    public void downloadFile(String filename){
        new AsyncDownload ().execute(filename);
    }

    public class AsyncDownload extends AsyncTask<String, Integer, String>{



        @Override
        protected void onPreExecute() {
            super.onPreExecute ();


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate (values);
            Log.i ("Progress : ", String.valueOf (values[0]));
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                WebServiceConnect service = new WebServiceConnect ();

                return service.downloadFile ("/emp/downloadfile/", strings[0], applicationContext);

            }catch(Exception e){
                e.printStackTrace ();
                return "Problem Downloading the File.. Close the application and try again after sometime";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute (s);
            Toast.makeText (applicationContext, s, Toast.LENGTH_SHORT).show();
        }
    }
}
