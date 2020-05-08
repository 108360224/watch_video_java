package com.example.lib;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
}
