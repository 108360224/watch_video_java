package com.example.lib;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Film {
    Document doc = null;
    String URL;
    public List<String[]> film_list = new ArrayList<>();
    public Film(String URL){
        try {
            this.doc = Jsoup.connect("http://www.99kubo.tv"+URL).get();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Element a = this.doc.selectFirst("body > div.main > div.list > div.listlf > dl > span > a:nth-child(3)");
        this.URL="http://www.99kubo.tv/"+a.attr("href");
        this.film_list=load(this.URL);
    }
    public Film_list film(int i){
        return new Film_list(film_list.get(i));
    }
    public void sort_by(String sort){
        this.URL=this.URL.replaceAll("order.+%20desc","order-"+sort+"%20desc");
        this.film_list=load(this.URL);
    }
    public void goto_area(String area){
        this.URL=this.URL.replaceAll("area.+tag","area-"+area+"-tag");
        this.film_list=load(this.URL);
    }
    public void load_new_film(){
        Element tag=this.doc.selectFirst("body > div.main > div.list > div.listlf > div");
        Element a=tag.select("a").last();
        this.film_list=load("http://www.99kubo.tv/"+a.attr("href"));
    }

    List<String[]> load(String url){

        List<String[]> list=new ArrayList<>();
        try {
            this.doc = Jsoup.connect(url).get();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Element ul=this.doc.selectFirst("body > div.main > div.list > div.listlf > ul");
        for(Element li:ul.select("li")){
           Element a=li.selectFirst("a");
           String href=a.attr("href");
           Element img=a.selectFirst("img");
           String title=img.attr("alt");
           String imsrc=img.attr("data-original");
           list.add(new String[]{href, title, imsrc});
        }
        return list;
    }
}

