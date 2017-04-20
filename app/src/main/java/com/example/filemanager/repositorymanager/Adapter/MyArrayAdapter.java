package com.example.filemanager.repositorymanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filemanager.repositorymanager.Activity.GoodsActivity;
import com.example.filemanager.repositorymanager.Entity.Good;
import com.example.filemanager.repositorymanager.Entity.GoodView;
import com.example.filemanager.repositorymanager.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class MyArrayAdapter  extends ArrayAdapter {
    private int resource;
    private Context context;
    public MyArrayAdapter(Context context, int resource, List<Good> Objects) {
        super(context, resource, Objects);
        this.resource=resource;
        this.context=context;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoodView goodView;
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.goods_listview_layout,parent,false);
            goodView=new GoodView();
            goodView.good_quan=(TextView)convertView.findViewById(R.id.goods_quan);
            goodView.good_spec=(TextView)convertView.findViewById(R.id.goods_spec);
            goodView.good_name=(TextView)convertView.findViewById(R.id.goods_name);
            goodView.more=(ImageView)convertView.findViewById(R.id.more);
            convertView.setTag(goodView);
        }
        else
        {
            goodView= (GoodView) convertView.getTag();
        }
        final Good good= (Good) getItem(position);

        goodView.good_spec.setText(good.getGoodspec());
        goodView.good_name.setText(good.getGoodname());
        goodView.good_quan.setText(good.getGoodquan()+"");
        return convertView;
    }
}
