package com.example.lib;

import java.io.IOException;
import java.net.URLDecoder;


public class MyClass {
    public static String toHex(byte b){
        return (""+"0123456789ABCDEF".charAt(0xf&b>>4)+"0123456789ABCDEF".charAt(b&0xf));}
    public static void main(String[] args) throws IOException {
        //System.out.println("開始");

        String url=M3u8.get_m3u8("/vod-play-id-148062-sid-0-pid-1-zuida.html");
        System.out.println(url);
        /*for(int i = 0;i<film.film_list.size();i++){

                System.out.println(film.film(i).url);

        }*/

    }
}
