package com.example.quranicmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class trafficAdapter extends BaseAdapter {
Context context;
ArrayList<trafficModel> models;

public  trafficAdapter(Context context,ArrayList<trafficModel> trafficModels)
{
   this.context =  context;
   this.models =  trafficModels;
}


    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    convertView = LayoutInflater.from(context).inflate(R.layout.trafficcustomlayout,parent,false);

    trafficModel  modelobject = models.get(position);
        ImageView img =  convertView.findViewById(R.id.calendeimg);
        TextView monthnsme =   convertView.findViewById(R.id.monthname);
        TextView numberofusers = convertView.findViewById(R.id.numerofUser);
        TextView users = convertView.findViewById(R.id.userintrafficlis);
        img.setImageResource(modelobject.getMonthImg());
        monthnsme.setText(modelobject.getMonthname());
        numberofusers.setText(modelobject.getNumberofusers());
        users.setText("Users");

        return  convertView;
    }
}
