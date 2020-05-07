package com.example.lib;

import java.util.List;

public class Film_list {
    public String url;
    public String imsrc;
    public String text;
    Film_list(String[] film_list){
        this.url=film_list[0];
        this.text=film_list[1];
        this.imsrc=film_list[2];
    }

}
