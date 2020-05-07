package com.example.lib;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    public List<String[]> menu_list = new ArrayList<>();
    private List<List<String[]>> child_list = new ArrayList<>();
    public Menu(){
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.99kubo.tv").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element menu_elem = doc.selectFirst("body > div.top > div.menu > div.mainmenu_top");
        for(Element a:menu_elem.select("a[id~=mm\\d{0,3}][id!=mm35][id!=mm0][target!=_blank]")){
            this.menu_list.add(new String[]{a.attr("href"), a.text()});
        }
        Element child_elem = doc.selectFirst("body > div.top > div.menu > div.mainmenu_bottom");
        Elements child_elem_ul=child_elem.select("ul[id!=mb0]");

        for(Element ul:child_elem_ul){
            List<String[]> li = new ArrayList<>();
            Elements aa = ul.select("a:not([href$=html])");
            for(Element a:ul.select("a:not([href$=html])")) {
                li.add(new String[]{a.attr("href").replaceAll("^(?!/)","/"), a.text()});
            }
            this.child_list.add(li);
        }

    }
    public Menu_list menu(int i){
        return new Menu_list(this.menu_list.get(i),this.child_list.get(i));
    }
}

