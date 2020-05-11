package com.example.watch_video_java;

import androidx.appcompat.app.AppCompatActivity;
import com.example.lib.*;
import com.google.android.exoplayer2.ExoPlayer;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.VideoView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "net";
    private VideoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ImageButton pause=(ImageButton)findViewById(R.id.exo_pause);
        ImageButton resume=(ImageButton)findViewById(R.id.exo_play);
        ImageButton next_video=(ImageButton)findViewById(R.id.exo_next);
        ImageButton prev_video=(ImageButton)findViewById(R.id.exo_prev);
        ProgressBar buffering = (ProgressBar) findViewById(R.id.progressBar);
        TextView title=(TextView)findViewById(R.id.title);
        Space space=(Space)findViewById(R.id.space);
        space.setMinimumHeight(10);
        final String url="http://iqiyi.cdn9-okzy.com/20200425/9496_b7642f6c/index.m3u8";
        PlayerView playerView = findViewById(R.id.PlayerView);

        player=new VideoPlayer(playerView);
        player.add_buffering(buffering);
        player.add_TextView(title);
        player.add_video(url,"title1");
        player.add_video("https://iqiyi.cdn9-okzy.com/20200412/8695_d2f1e49b/index.m3u8","title2");
        player.start();
        player.add_video(url,"title3");

        pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                player.pause();
            }
        });
        resume.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                player.resume();
            }
        });
        next_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                player.next_video();
            }
        });
        prev_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                player.prev_video();
            }
        });

        /*
        next_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                player.addvideo(url);

            }
        });*/
        /*
        Thread thread = new Thread(mutiThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


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
