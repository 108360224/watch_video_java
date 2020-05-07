package com.example.watch_video_java;

import androidx.appcompat.app.AppCompatActivity;
import com.example.lib.*;
import com.example.lib.Menu;
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
            Log.d(TAG,"url = "+m.menu(1).url);

        }
    };
}
