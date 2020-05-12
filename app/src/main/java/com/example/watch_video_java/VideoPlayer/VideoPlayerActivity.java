package com.example.watch_video_java.VideoPlayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.lib.*;

import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.os.Bundle;
import android.util.Log;

import com.example.watch_video_java.R;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoPlayerActivity extends Fragment {
    static final String TAG = "net";
    private VideoPlayer player;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getActivity(). getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.video_player_layout, container, false);
        ImageButton pause = (ImageButton) root.findViewById(R.id.exo_pause);
        ImageButton resume = (ImageButton) root.findViewById(R.id.exo_play);
        ImageButton next_video = (ImageButton) root.findViewById(R.id.exo_next);
        ImageButton prev_video = (ImageButton) root.findViewById(R.id.exo_prev);
        ProgressBar buffering = (ProgressBar) root.findViewById(R.id.progressBar);
        TextView title = (TextView) root.findViewById(R.id.title);
        Button button = (Button) root.findViewById(R.id.lock_speed);
        LinearLayout playback_control = (LinearLayout) root.findViewById(R.id.playback_control);
        final String url = "http://iqiyi.cdn9-okzy.com/20200425/9496_b7642f6c/index.m3u8";
        PlayerView playerView = root.findViewById(R.id.PlayerView);
        player = new VideoPlayer(playerView);
        player.add_buffering(buffering);
        player.add_TextView(title);
        player.add_video(url, "title1");
        player.add_video("https://iqiyi.cdn9-okzy.com/20200412/8695_d2f1e49b/index.m3u8", "title2");
        player.start();
        player.add_video(url, "title3");

        pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                player.pause();
            }
        });
        resume.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                player.play();
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
        touchListener touchListener = new touchListener();
        touchListener.add_player(player);
        touchListener.add_playback_controll(playback_control);
        touchListener.add_lock_speed_button(button);
        playerView.setOnTouchListener(touchListener);
        return  root;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // 在這個方法中取得並定義Fragment的介面元件
        super.onActivityCreated(savedInstanceState);

        
        //...
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
