package com.example.quranicmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ManageFileAdapter extends BaseAdapter {
    Context context;
    ArrayList<ManageFileModel> modelObject;
    public  ManageFileAdapter(Context context,ArrayList<ManageFileModel> models)
    {
        this.context = context;
        this.modelObject = models;
    }
    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    @Override
    public int getCount() {
        return modelObject.size();
    }

    @Override
    public Object getItem(int position) {
        return modelObject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(context).inflate(R.layout.managefilelayout, parent, false);
        ManageFileModel model = modelObject.get(position);
        ImageView mp3View =row.findViewById(R.id.managemp3);
        TextView surahname = row.findViewById(R.id.managename);
        mp3View.setImageResource(model.getMp3());
        surahname.setText(model.getName());
        return  row;
    }
}
