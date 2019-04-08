package com.example.quranicmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class feedBackAdapter extends BaseAdapter {
    Context context;
    ArrayList<feedBackAdapterModel> list;
  public   feedBackAdapter(Context c,ArrayList<feedBackAdapterModel> b)
  {
     this.context = c;
      list = b;
  }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
      return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


          convertView = LayoutInflater.from(context).inflate(R.layout.feedbacklayout,parent,false);

     final feedBackAdapterModel model =list.get(position);
        ImageView view =(ImageView) convertView.findViewById(R.id.mpicon);
        TextView userid =(TextView)convertView.findViewById(R.id.feedusername);
        //TextView status =(TextView) convertView.findViewById(R.id.messageStatus);
        view.setImageResource(model.getImage());
        userid.setText(model.getUsername());
        userid.setAutoLinkMask(14);
       // status.setText(model.getStatus().toString());
        return convertView;
    }
}
