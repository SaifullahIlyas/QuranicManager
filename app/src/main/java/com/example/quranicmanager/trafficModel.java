package com.example.quranicmanager;

public class trafficModel {

    String Monthname;
    String numberofusers;
    int monthImg;
    public  trafficModel(String monthname,String numberofimage,int monthImg)
    {

        this.Monthname =  monthname;
        this.numberofusers =  numberofimage;
        this.monthImg =  monthImg;
    }

    public int getMonthImg() {
        return monthImg;
    }

    public String getMonthname() {
        return Monthname;
    }

    public String getNumberofusers() {
        return numberofusers;
    }
}
