package com.example.lib;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class M3u8{
    public String url;
    public static String get_m3u8(String URL){
        String url;
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.99kubo.tv"+URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element script = doc.selectFirst("body > div.main > div.playmar > div.play > div:nth-child(1) > script:nth-child(1)");
        Pattern pattern = Pattern.compile("http.{1,100}index\\.m3u8");
        String t=script.data();
        Matcher matcher = pattern.matcher(script.data());
        matcher.find();
        try{
            url=matcher.group(0);
            url=url.replaceAll("\\\\","");
        }
        catch (Exception e){
            url="not exit";
        }
        return url;
    }

    public static M3u8_list get_m3u8_list(String theUrl){
        Time time=new Time(0);
        M3u8_list m3u8_list=new M3u8_list();
        get_m3u8_content(theUrl,m3u8_list,time);
        return m3u8_list;
    }
    private static void get_m3u8_content(String theUrl, M3u8_list m3u8_list,Time time){


        try {
            URL url = new URL(theUrl);

            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null)
            {
                if(line.contains("m3u8")){
                    String s=theUrl.replaceAll("index\\.m3u8","")+line;
                    get_m3u8_content(s,m3u8_list,time);
                }
                else if(line.contains("RESOLUTION")){
                    String s=line.replaceAll(".+RESOLUTION=","");
                    m3u8_list.size=s;
                }
                else if(line.contains("#EXTINF")){
                    String s=line.replaceAll("#EXTINF:|,","");
                    time.i+=Float.parseFloat(s);
                    m3u8_list.time_list.add(time.i);
                }
                else if(!line.contains("#EXT")){
                    String s=theUrl.replaceAll("index\\.m3u8","")+line;
                    m3u8_list.url_list.add(s);
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
class Time{
    float i;
    Time(float i){
        this.i=i;
    }
}