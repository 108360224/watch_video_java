package com.example.lib;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Search{
    public List<String[]> film_list = new ArrayList<>();
    public Search(String keyword){
        String word=keyword.replaceAll(" ","%20");
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.99kubo.tv/index.php?s=home-vod-innersearch&q="+word).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element ires=doc.selectFirst("#ires");
        for(Element li:ires.select("li")){
            Element a=li.selectFirst("a");
            String href=a.attr("href");
            Element img =a.selectFirst("img");
            String imsrc=img.attr("src");
            String text=li.selectFirst("h3").text();
            text=text.replaceAll("-.+","");
            this.film_list.add(new String[]{href, text, imsrc});
        }
    }
    public Film_list film(int i){
        return new Film_list(this.film_list.get(i));
    }
}