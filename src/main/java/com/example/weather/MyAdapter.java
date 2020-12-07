package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;


class MyAdapter extends BaseAdapter {
    private List<Map<String,String>> mlist;
    private Context mContext;
    private LayoutInflater mlayoutInflater;

    public MyAdapter(Context mContext, List<Map<String, String>> list) {
        mlayoutInflater=LayoutInflater.from(mContext);
        this.mContext=mContext;
        this.mlist=list;
    }


    @Override
    public int getCount() {
        return mlist == null ? 0: mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //显示item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mlayoutInflater.inflate(R.layout.item, parent, false);

        //获取集合中的元素
        TextView city = view.findViewById(R.id.tv_city);
        TextView temperature = view.findViewById(R.id.tv_temperature);
        TextView weather = view.findViewById(R.id.tv_weather);

        city.setText((String) mlist.get(position).get("city_").toString());
        temperature.setText((String) mlist.get(position).get("temperature_").toString());
        weather.setText((String) mlist.get(position).get("weather_").toString());
        return view;
    }
}
