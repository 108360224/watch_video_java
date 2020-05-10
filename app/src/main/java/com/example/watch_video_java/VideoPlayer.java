package com.example.watch_video_java;

import android.content.Context;
import android.net.Uri;
import android.widget.ProgressBar;

import com.example.lib.Episode;
import com.example.lib.Film;
import com.example.lib.M3u8;
import com.example.lib.Menu;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

public class VideoPlayer {
    private PlayerView playerView;
    private ProgressBar mProgressBar;
    private SimpleExoPlayer player;
    private ConcatenatingMediaSource video_list=new ConcatenatingMediaSource();
    private int video_index=0;
    VideoPlayer(PlayerView playerView,ProgressBar buffering){
        this.playerView=playerView;

        Context context = this.playerView.getContext();
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
        this.player=player;
        Listener listener=new Listener(player,buffering);
        player.addListener(listener);
        playerView.setPlayer(this.player);




    }
    void pause(){
        this.player.setPlayWhenReady(false);
        Log.d("on","pause");
    }
    void resume(){
        this.player.setPlayWhenReady(true);
        Log.d("on","play");
    }
    void addvideo(String url){
        DefaultHttpDataSourceFactory dataSourceFactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(this.playerView.getContext(), "Watch_video_java"));
        Uri vidUri = Uri.parse(url);
        HlsMediaSource hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(vidUri);

        this.video_list.addMediaSource(hlsMediaSource);

    }
    void start(){
        player.prepare(this.video_list);
    }
    void next_video(){
        Log.d("on","current"+Integer.toString(this.video_index));
        this.video_index++;
        player.seekTo(this.video_index,0);

    }
    void prev_video(){
        if(this.video_index !=0){
            Log.d("on","current"+Integer.toString(this.player.getCurrentWindowIndex()));
            Log.d("on","prev"+Integer.toString(this.player.getPreviousWindowIndex()));
            this.video_index--;
            player.seekTo(this.video_index,0);

        }

    }
    void shift_time(long shift){
        player.seekTo(player.getCurrentPosition()+shift);
    }

    void setspeed(float speed){
        PlaybackParameters sp=new PlaybackParameters(speed);

        this.player.setPlaybackParameters(sp);
    }
}
