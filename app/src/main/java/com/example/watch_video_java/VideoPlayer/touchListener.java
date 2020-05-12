package com.example.watch_video_java.VideoPlayer;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;

public class touchListener implements View.OnTouchListener {

    private VideoPlayer player;
    private VelocityTracker mVelocityTracker = null;
    private Boolean b=false;
    private Boolean isbeclick=false,moveleft=false,moveup=false,issetspeed=false;
    private LinearLayout playback_control;
    private Button button;
    float y=0,x=0;
    float speed=0;
    @Override
    public boolean onTouch(View view,MotionEvent event) {
        try {
            int index = event.getActionIndex();
            int action = event.getActionMasked();
            int pointerId = event.getPointerId(index);

            switch(action) {
                case MotionEvent.ACTION_DOWN:
                    if(mVelocityTracker == null) {
                        // Retrieve a new VelocityTracker object to watch the
                        // velocity of a motion.
                        mVelocityTracker = VelocityTracker.obtain();
                    }
                    else {
                        // Reset the velocity tracker back to its initial state.
                        mVelocityTracker.clear();
                    }
                    //playback_control.setVisibility(View.VISIBLE);
                    button.setX(event.getX()-200);
                    x=event.getX();
                    y=event.getY();
                    isbeclick=true;
                    issetspeed=false;
                    // Add a user's movement to the tracker.
                    mVelocityTracker.addMovement(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mVelocityTracker.addMovement(event);
                    // When you want to determine the velocity, call
                    // computeCurrentVelocity(). Then call getXVelocity()
                    // and getYVelocity() to retrieve the velocity for each pointer ID.
                    mVelocityTracker.computeCurrentVelocity(100);
                    // Log velocity of pixels per second
                    // Best practice to use VelocityTrackerCompat where possible.
                    float X_velocity=mVelocityTracker.getXVelocity(pointerId);
                    float Y_velocity=mVelocityTracker.getYVelocity(pointerId);
                    if(Math.abs(X_velocity)>Math.abs(Y_velocity)&&moveup==false){
                        isbeclick=false;
                        player.shift_time(0,(long)(X_velocity*250));
                        moveleft=true;
                    }else if(moveleft==false){
                        isbeclick=false;
                        speed=(y-event.getY())/50;
                        player.setspeed(speed);
                        button.setVisibility(View.VISIBLE);
                        button.setText("x"+String.valueOf(Math.round(speed*10.)/10.));
                        button.setY(event.getY()-50);
                        if(Math.abs(x-event.getX())>130){
                            b=false;
                            issetspeed=true;
                        }else{
                            b=true;
                        }
                        moveup=true;
                    }







                    break;
                case MotionEvent.ACTION_UP:
                    if(b){
                        player.play();
                        if(issetspeed==false){
                            player.setspeed(1);
                        }

                        b=false;
                    }
                    moveleft=false;
                    moveup=false;
                    button.setVisibility(View.INVISIBLE);

                    if(isbeclick){
                        if(playback_control.getVisibility()==View.VISIBLE){
                            playback_control.setVisibility(View.INVISIBLE);
                        }else if(playback_control.getVisibility()==View.INVISIBLE){
                            playback_control.setVisibility(View.VISIBLE);
                        }

                        isbeclick=false;
                    }

                case MotionEvent.ACTION_CANCEL:
                    // Return a VelocityTracker object back to be re-used by others.
                    mVelocityTracker.recycle();
                    break;
            }

        }catch (Exception e){}
        return true;
    }
    public void add_player(VideoPlayer player){
        this.player=player;
    }
    public void add_playback_controll(LinearLayout playback_control){
        this.playback_control=playback_control;
    }
    public void add_lock_speed_button(Button button){
        this.button=button;
    }
    private boolean isMotionEventInsideView(View view, MotionEvent event) {
        Rect viewRect = new Rect(
                view.getLeft(),
                view.getTop(),
                view.getRight(),
                view.getBottom()
        );

        return viewRect.contains(
                view.getLeft() + (int) event.getX(),
                view.getTop() + (int) event.getY()
        );
    }
}

