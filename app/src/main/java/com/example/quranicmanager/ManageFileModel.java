package com.example.quranicmanager;

public class ManageFileModel {
    String name;
    int mp3;
    int conmenu;
    public  ManageFileModel(String a,int b,int c)
    {
        this.name = a;
        this.mp3 = b;
        this.conmenu = c;
    }

    public String getName() {
        return name;
    }

    public int getConmenu() {
        return conmenu;
    }

    public int getMp3() {
        return mp3;
    }
}
