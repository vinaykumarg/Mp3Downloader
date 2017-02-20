package com.example.vinayg.mp3downloader;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.URLUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.id;

public class DownloadService extends IntentService {
    private NotificationManager mNotifyManager;
    private  NotificationCompat.Builder mBuilder;
    private static final String TAG = DownloadService.class.getName();


    public DownloadService() {
        super("downloader");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onCreate() {
        super.onCreate();


    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"onHandleIntent()");
        String link = "http://www.atozmp3.lol/1/Singles/Winner/320/01%20-%20Sitara%20%5bwww.AtoZmp3.in%5d.mp3";
        String name = URLUtil.guessFileName(link, null, null);
        mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.putExtra("Filename",name);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                myIntent,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Mp3 Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_arrow_downward);

        int count;
        if(isNetworkConnected()) {
            try {

                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                // getting file length
                int lenghtOfFile = urlConnection.getContentLength();
                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard, name);
                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    int incr = (int) ((total * 100) / lenghtOfFile);
                    mBuilder.setProgress(100, incr, false);
                    mNotifyManager.notify(id, mBuilder.build());
                    fileOutput.write(data, 0, count);
                }
                mBuilder.setContentText("Download complete")
                        // Removes the progress bar
                        .setContentIntent(pendingIntent)
                        .setProgress(0, 0, false)
                        .setVibrate(new long[] { 1000,1000});
                mNotifyManager.notify(id, mBuilder.build());
                fileOutput.close();
                inputStream.close();
                stopSelf();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
