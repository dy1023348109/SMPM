package com.example.filemanager.repositorymanager.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.filemanager.repositorymanager.Entity.Good;
import com.example.filemanager.repositorymanager.R;

import org.w3c.dom.Text;

public class GoodsActivity extends AppCompatActivity {
    public ImageView goods_img;
    public TextView goods_name;
    public TextView goods_id;
    public TextView goods_spec;
    public TextView goods_quan;
    public TextView goods_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        Intent intent=getIntent();
        Good good= (Good) intent.getSerializableExtra("good");


        goods_img= (ImageView) findViewById(R.id.goods_activity_img);
        goods_id= (TextView) findViewById(R.id.goods_activity_id);
        goods_name=(TextView)findViewById(R.id.goods_activity_name);
        goods_quan= (TextView) findViewById(R.id.goods_activity_quan);
        goods_price= (TextView) findViewById(R.id.goods_activity_price);
        goods_spec= (TextView) findViewById(R.id.goods_activity_spec);
        Log.d("-----------", goods_img.getResources().toString());
        goods_id.setText("商品编号:"+good.getGoodid());
        goods_name.setText("商品名称:"+good.getGoodname());
        goods_quan.setText("商品余量:"+good.getGoodquan());
        goods_price.setText("商品单价:"+good.getGoodprice()+"元");
        goods_spec.setText("商品规格:"+good.getGoodspec());
        goods_img.setBackgroundResource(0);
        Log.e("tag","http://169.254.54.60:8080/images/"+good.getGoodurl());
        Glide.with(getApplicationContext()).load("http://169.254.54.60:8080/images/"+good.getGoodurl()).into(goods_img);

       Log.d("-----------", goods_img.getResources().toString());
    }
    RequestListener<String,GlideDrawable> errorListener=new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

            Log.e("onException",e.toString()+"  model:"+model+" isFirstResource: "+isFirstResource);
            // imageView.setImageResource(R.mipmap.ic_launcher);
            return false;
        }
        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Log.e("onResourceReady","isFromMemoryCache:"+isFromMemoryCache+"  model:"+model+" isFirstResource: "+isFirstResource);
            return false;
        }
    } ;
}
