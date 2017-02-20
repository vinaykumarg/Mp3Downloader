package com.example.vinayg.mp3downloader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();
    private TextView mTextView;
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String link = "http://www.atozmp3.lol/1/Singles/Winner/320/01%20-%20Sitara%20%5bwww.AtoZmp3.in%5d.mp3";
        mTextView = (TextView) findViewById(R.id.Text1);
        mButton = (Button) findViewById(R.id.b1);
        mButton.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent!=null) {
            String fname = intent.getStringExtra("Filename");
            if (fname!=null) {
                mTextView.setText(intent.getStringExtra("Filename"));
            } else {
                mTextView.setText(link);
            }
        }

    }

    @Override
    public void onClick(View v) {
        Intent sIntent = new Intent(this,DownloadService.class);
        startService(sIntent);
        mButton.setEnabled(false);
    }


}
