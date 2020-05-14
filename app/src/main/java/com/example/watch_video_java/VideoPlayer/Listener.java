package com.example.watch_video_java.VideoPlayer;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import java.util.Formatter;
import java.util.Locale;

public class Listener implements Player.EventListener {
    private VideoPlayer mSimpleExoPlayer;
    private ProgressBar buffering;
    public Listener(VideoPlayer mSimpleExoPlayer){
        this.mSimpleExoPlayer=mSimpleExoPlayer;
    }
    void add_buffering(ProgressBar buffering){
        this.buffering=buffering;
    }
    @Override
    public void onTimelineChanged(Timeline timeline, int reason) {


    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.d("debug","onTracksChanged");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.d("debug","onLoadingChanged");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.d("debug","onPlayerStateChanged: playWhenReady = "+String.valueOf(playWhenReady)
                +" playbackState = "+playbackState);
        switch (playbackState){
            case ExoPlayer.STATE_ENDED:
                Log.d("end","Playback ended!");
                //Stop playback and return to start position
                //mSimpleExoPlayer.next_video();
                break;
            case ExoPlayer.STATE_READY:
                buffering.setVisibility(View.GONE);
                Log.d("debug","ExoPlayer ready! pos: ");
                break;
            case ExoPlayer.STATE_BUFFERING:
                Log.d("debug","Playback buffering!");
                this.buffering.setVisibility(View.VISIBLE);
                break;
            case ExoPlayer.STATE_IDLE:
                Log.d("debug","ExoPlayer idle!");
                mSimpleExoPlayer.retry();
                break;
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.d("debug","onPlaybackError: "+error.getMessage());
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        Log.d("debug","onPositionDiscontinuity");

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Log.d("debug","MainActivity.onPlaybackParametersChanged."+playbackParameters.toString());
    }
    private void setPlayPause(boolean play){
        mSimpleExoPlayer.setPlayWhenReady(play);
    }

    private String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds =  timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }



}
