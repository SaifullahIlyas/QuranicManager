package com.example.quranicmanager;

public class feedBackAdapterModel {
    String username;
    String status;
    int image;
    public  feedBackAdapterModel(String a,String b,int c)
    {
        username = a;
        status = b;
        image = c;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public int getImage() {
        return image;
    }

}
