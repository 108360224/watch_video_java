package com.example.watch_video_java.VideoPlayer;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
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
import java.util.logging.LogRecord;

public class VideoPlayer {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private TextView title;
    private ConcatenatingMediaSource video_list=new ConcatenatingMediaSource();
    private int video_index=0;
    private DefaultTrackSelector trackSelector;
    private DefaultHttpDataSourceFactory dataSourceFactory;
    private Episode episode;
    private Boolean m3u8_exit=false;
    private Handler workHandler;
    private long last_time;
    private HandlerThread mHandlerThread;
    private Thread quickClick_thread;
    VideoPlayer(PlayerView playerView){
        mHandlerThread = new HandlerThread("handlerThread");
        mHandlerThread.start();
        workHandler = new Handler(mHandlerThread.getLooper()) ;
        last_time=Calendar.getInstance().getTimeInMillis();
        this.playerView=playerView;
        this.dataSourceFactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(this.playerView.getContext(), "Watch_video_java"));
        Context context = this.playerView.getContext();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(context).build();
        this.trackSelector =  new DefaultTrackSelector(context);
        SimpleExoPlayer player = new SimpleExoPlayer
                                        .Builder(context)
                                        .build();
        playerView.setControllerShowTimeoutMs(0);
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
        video_list.clear();
        HlsMediaSource hlsMediaSource=null;
        while (hlsMediaSource==null){
            try {
                hlsMediaSource =
                        new HlsMediaSource.Factory(this.dataSourceFactory).createMediaSource(Uri.parse(M3u8.get_m3u8(episode.episode(i).url)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.video_list.addMediaSource(hlsMediaSource);

    }
    private Runnable add_video_thread = new Runnable() {
        public void run() {

            add_video(video_index);
        }
    };
    private Runnable detect_quickClick = new Runnable() {
        public void run() {
            while (true){
                if(Calendar.getInstance().getTimeInMillis()-last_time>2000){
                    workHandler.post(add_video_thread);
                    break;
                }
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
        }
    }
    void next_video() {
        if(video_index+1<=episode.episode_list.size()-1){
            this.video_index++;
            jump_to_video(this.video_index);

            this.title.setText(episode.episode(video_index).text);
        }else{
            Toast.makeText(playerView.getContext(), "final", Toast.LENGTH_SHORT).show();
        }
    }
    private Boolean isthread_start=false;
    void jump_to_video(int index){
        video_index=index;
        this.title.setText(episode.episode(video_index).text);

        if(Calendar.getInstance().getTimeInMillis()-last_time<2000) {
            if(isthread_start==false){
                quickClick_thread.start();
                isthread_start=true;
            }

        }else {
            workHandler.post(add_video_thread);
            quickClick_thread=new Thread(detect_quickClick);
            isthread_start=false;
        }

        last_time=Calendar.getInstance().getTimeInMillis();

    }
    void prev_video(){
        if(video_index>0){
            this.video_index--;
            jump_to_video(this.video_index);

            this.title.setText(episode.episode(video_index).text);
        }else {
            Toast.makeText(playerView.getContext(), "start", Toast.LENGTH_SHORT).show();
        }

    }
    void shift_time(int index,long shift){
        player.seekTo(index,player.getCurrentPosition()+shift);
    }

    void setspeed(float speed){
        PlaybackParameters sp=new PlaybackParameters(speed);

        this.player.setPlaybackParameters(sp);
    }
}
