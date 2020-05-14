package com.example.watch_video_java.VideoPlayer;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.Uri;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lib.Episode;
import com.example.lib.Film;
import com.example.lib.M3u8;
import com.example.lib.Menu;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayer {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ProgressBar buffering;
    private TextView title;
    private ConcatenatingMediaSource video_list=new ConcatenatingMediaSource();
    private int video_index=0;
    private DefaultTrackSelector trackSelector;
    private DefaultHttpDataSourceFactory dataSourceFactory;
    private List<String> title_list=new ArrayList<>();
    private Episode episode;
    private Thread thread;
    private Listener listener;
    private Boolean m3u8_exit=false;
    VideoPlayer(PlayerView playerView){

        this.playerView=playerView;
        this.dataSourceFactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(this.playerView.getContext(), "Watch_video_java"));
        Context context = this.playerView.getContext();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(context).build();
        this.trackSelector =  new DefaultTrackSelector(context);
        SimpleExoPlayer player = new SimpleExoPlayer
                                        .Builder(context)
                                        .build();

        this.player=player;





    }
    public void get_episode(Episode episode){
        this.episode=episode;
    }
    void retry(){
        player.retry();
    }
    void setPlayWhenReady(boolean play){
        player.setPlayWhenReady(play);
    }
    void add_TextView(TextView title){
        this.title=title;
    }
    void pause(){
        this.player.setPlayWhenReady(false);
        Log.d("on","pause");
        setspeed(1);
    }
    void play(){
        this.player.setPlayWhenReady(true);
        Log.d("on","play");
    }
    void set_quality(){
        this.trackSelector.setParameters(
                trackSelector.buildUponParameters()
                        .setMaxVideoBitrate(800)
                        .setForceHighestSupportedBitrate(true)
                        .build()
        );
    }
    private void add_video(int i){
        HlsMediaSource hlsMediaSource =
                new HlsMediaSource.Factory(this.dataSourceFactory).createMediaSource(Uri.parse(M3u8.get_m3u8(episode.episode(i).url)));

        this.video_list.addMediaSource(hlsMediaSource);
        this.title_list.add(episode.episode(i).text);
    }
    private Runnable add_video_thread = new Runnable() {
        public void run() {
            video_list.clear();
            if(video_index>0){
                add_video(video_index-1);
            }

            add_video(video_index);
            if(video_index<episode.episode_list.size()-1){
                add_video(video_index+1);
            }

        }
    };
    private Runnable check_m3u8 = new Runnable() {
        public void run() {
            if(M3u8.get_m3u8(episode.episode(0).url)=="not exit"){
                m3u8_exit=false;
            }else{
                m3u8_exit=true;
            }

        }
    };
    void add_listener(Listener listener){
        player.addListener(listener);
    }
    void start(){
        video_list=new ConcatenatingMediaSource();
        this.playerView.setPlayer(this.player);
        this.player.prepare(this.video_list);
        Thread check_exit=new Thread(check_m3u8);
        check_exit.start();
        try {
            check_exit.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if(m3u8_exit==false){
            Toast.makeText(playerView.getContext(), "video not exit", Toast.LENGTH_SHORT).show();
        }else{
            thread = new Thread(add_video_thread);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.title.setText(episode.episode(video_index).text);
        }



    }
    void next_video() {
        if(video_index==0||video_index>episode.episode_list.size()-2){
            try {
                player.seekTo(1,0);
            }catch (Exception e){
                Toast.makeText(playerView.getContext(), "loading... tap too fast", Toast.LENGTH_SHORT).show();
            }

        }else{
            try {
                player.seekTo(2,0);
            }catch (Exception e){
                Toast.makeText(playerView.getContext(), "tap too fast", Toast.LENGTH_SHORT).show();
            }
        }

        if(video_index<episode.episode_list.size()-1){
            this.video_index++;
            this.title.setText(episode.episode(video_index).text);
        }




        thread = new Thread(add_video_thread);
        thread.start();





    }
    void jump_to_video(int index){
        video_index=index;
        thread = new Thread(add_video_thread);
        thread.start();
        this.title.setText(episode.episode(video_index).text);
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(video_index>episode.episode_list.size()-2){
            try {
                player.seekTo(0,0);
            }catch (Exception e){
                Toast.makeText(playerView.getContext(), "loading... tap too fast", Toast.LENGTH_SHORT).show();
            }

        }else{
            try {
                player.seekTo(1,0);
            }catch (Exception e){
                Toast.makeText(playerView.getContext(), "tap too fast", Toast.LENGTH_SHORT).show();
            }
        }

    }
    void prev_video(){

        try {
            player.seekTo(0,0);
        }catch (Exception e){
            Toast.makeText(playerView.getContext(), "loading... tap too fast", Toast.LENGTH_SHORT).show();
        }


        if(video_index>0){
            this.video_index--;
            this.title.setText(episode.episode(video_index).text);
        }



        thread = new Thread(add_video_thread);
        thread.start();

    }
    void shift_time(int index,long shift){
        player.seekTo(index,player.getCurrentPosition()+shift);
    }

    void setspeed(float speed){
        PlaybackParameters sp=new PlaybackParameters(speed);

        this.player.setPlaybackParameters(sp);
    }
}
