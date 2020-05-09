package com.example.watch_video_java;

import androidx.appcompat.app.AppCompatActivity;
import com.example.lib.*;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "net";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private Runnable mutiThread = new Runnable() {
        public void run() {
            // 運行網路連線的程式
            Menu m=new Menu();
            Film f=new Film(m.menu(1).child(0).url);
            String s= f.film(0).url;
            f.sort_by("");
            Episode ep =new Episode(f.film(0).url);
            String ss =ep.episode(0).url;
            String indexm3u8=M3u8.get_m3u8(ss);
            Search sear=new Search("");

            Log.d(TAG,"url = "+m.menu(1).child(0));
        }
    };
}
