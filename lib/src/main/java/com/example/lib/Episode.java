package com.example.lib;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Episode{
    public List<String[]> episode_list=new ArrayList<>();;
    Episode(String URL){
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.99kubo.tv"+URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element ul=doc.selectFirst("body > div.main > div.topRow > div > ul > div > div");
        for(Element a:ul.select("a")){
            episode_list.add(new String[]{a.attr("href"), a.text()});
        }

    }
    public Child_list episode(int i){
        return new Child_list(episode_list.get(i));
    }
}