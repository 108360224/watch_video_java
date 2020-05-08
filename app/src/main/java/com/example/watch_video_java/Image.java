package com.example.watch_video_java;

import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.*;
public class Image {
    public static Drawable image_from_url(String url){
        try{
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable draw = Drawable.createFromStream(is, "src");
            return draw;
        }catch (Exception e) {
            //TODO handle error
            return null;
        }
    }
}
