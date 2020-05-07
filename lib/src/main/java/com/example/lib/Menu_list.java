package com.example.lib;

import java.util.List;

public class Menu_list{
    public String url;
    public String text;
    public List<String[]> child_list;


    public Menu_list(String[] menu_list,List<String[]> child_list){
        this.url=menu_list[0];
        this.text=menu_list[0];
        this.child_list=child_list;
    }
    Child_list child(int i){
        return new Child_list(this.child_list.get(i));
    }
}
