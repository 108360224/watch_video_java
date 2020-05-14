package com.example.watch_video_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lib.*;
import com.example.watch_video_java.VideoPlayer.VideoPlayer;

import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.os.Bundle;
import android.util.Log;

import com.example.watch_video_java.VideoPlayer.VideoPlayerActivity;
import com.google.android.exoplayer2.ui.PlayerView;

public class MainActivity extends FragmentActivity {
    static final String TAG = "net";
    private VideoPlayer player;
    private String ep_url="/vod-read-id-146948.html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// set Fragmentclass Arguments

        setContentView(R.layout.activity_main);

    }
    public String get_ep_url() {
        return ep_url;
    }
    @Override
    protected void onResume() {
        super.onResume();




    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // This overrides default action
    }

    private Runnable mutiThread = new Runnable() {
        public void run() {
            // 運行網路連線的程式
            Log.d(TAG,"start");
            Menu m=new Menu();
            Film f=new Film(m.menu(1).child(0).url);
            String s= f.film(0).url;
            Episode ep =new Episode(f.film(0).url);
            String ss =ep.episode(0).url;
            String indexm3u8=M3u8.get_m3u8(ss);

        }
    };
}
