package com.example.watch_video_java.VideoPlayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.lib.*;

import android.content.res.Configuration;
import android.os.StrictMode;
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
import android.widget.Space;
import android.widget.TextView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.watch_video_java.MainActivity;
import com.example.watch_video_java.R;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoPlayerActivity extends Fragment {
    private static final String TAG = "net";
    private VideoPlayer player;
    private Listener listener;
    private View root;
    private PlayerView playerView;
    String url;
    private Episode episode;
    private LinearLayout ep_list;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getActivity(). getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        url = activity.get_ep_url();
        root = inflater.inflate(R.layout.video_player_layout, container, false);


        return  root;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // 在這個方法中取得並定義Fragment的介面元件
        super.onActivityCreated(savedInstanceState);
        ep_list=(LinearLayout)root.findViewById(R.id.ep_list);
        ImageButton pause = (ImageButton) root.findViewById(R.id.exo_pause);
        ImageButton resume = (ImageButton) root.findViewById(R.id.exo_play);
        ImageButton next_video = (ImageButton) root.findViewById(R.id.next);
        ImageButton prev_video = (ImageButton) root.findViewById(R.id.prev);
        ProgressBar buffering = (ProgressBar) root.findViewById(R.id.progressBar);
        TextView title = (TextView) root.findViewById(R.id.title);
        Button button = (Button) root.findViewById(R.id.lock_speed);
        LinearLayout playback_control = (LinearLayout) root.findViewById(R.id.playback_control);
        playerView = root.findViewById(R.id.PlayerView);
        View space=root.findViewById(R.id.space);
        player = new VideoPlayer(playerView);
        Link_to_episode link_to_episode=new Link_to_episode(url);


        while (episode==null){
            try{
                Thread m=new Thread(link_to_episode);
                m.start();
                m.join();
                episode=link_to_episode.return_episode();
                Thread.sleep(5000);
            }catch (Exception e){}
        }

        player.get_episode(episode);
        listener=new Listener(player);
        listener.add_buffering(buffering);
        player.add_TextView(title);
        player.add_listener(listener);


        player.start();
        player.jump_to_video(0);

        set_ep_button();
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
        space.setOnTouchListener(touchListener);
        //...
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        }
        // This overrides default action
    }
    private void set_ep_button(){
        for(int i=0;i<episode.episode_list.size();i++){
            Button btn=new Button(root.getContext());
            btn.setId(i);
            btn.setText(episode.episode(i).text);
            btn.setOnClickListener(new ep_button_onclick(player,i));
            ep_list.addView(btn);
        }

    }

}
class ep_button_onclick implements Button.OnClickListener{
    private VideoPlayer player;
    private int i;
    public ep_button_onclick(VideoPlayer player,int i){
        this.player=player;
        this.i=i;
    }
    @Override
    public void onClick(View v) {
        player.jump_to_video(i);
    }
}
class Link_to_episode implements Runnable {
    private String url;
    private Episode episode;
    public Link_to_episode(String url){
        this.url=url;
    }


    public Episode return_episode(){
        return episode;
    }
    @Override
    public void run() {
        try {
            episode=new Episode(url);
        }catch (Exception e){}

    }
}