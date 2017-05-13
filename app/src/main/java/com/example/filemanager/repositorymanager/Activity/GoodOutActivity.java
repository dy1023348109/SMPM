package com.example.filemanager.repositorymanager.Activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filemanager.repositorymanager.Entity.Good;
import com.example.filemanager.repositorymanager.Entity.Net;
import com.example.filemanager.repositorymanager.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GoodOutActivity extends AppCompatActivity implements View.OnClickListener {
    public EditText good_out_input,good_out_to;
    public TextView goods_out_id ,good_out_name,good_out_spec,good_out_price,good_out_quan;
    public Button good_out_verify,good_out_save;
    int remain;
    public Good good;
    Handler handler=new Handler()
    {
      public void handleMessage(Message message)
      {
          switch (message.what)
          {
              case 1:
                  good= (Good) message.obj;
                  Log.d("good",good.getGoodname());
                  if (good.getGoodid()==-1)
                  {
                      Toast.makeText(getApplicationContext(),"无此商品",Toast.LENGTH_SHORT).show();
                  }
                  else
                  {
                      goods_out_id.setText(good.getGoodid()+"");
                      good_out_name.setText(good.getGoodname());
                      good_out_spec.setText(good.getGoodspec());
                      good_out_price.setText(good.getGoodprice()+"元");
                      good_out_quan.setText(good.getGoodquan()+"");
                      remain=good.getGoodquan();
                  }

                  break;
              case 8:
                  //更新成功
                  good_out_quan.setText(""+remain);
                  Toast.makeText(GoodOutActivity.this,"更新数据成功",Toast.LENGTH_SHORT).show();

                  break;
              case 9:
                  //更新失败
                  Toast.makeText(GoodOutActivity.this,"更新数据失败",Toast.LENGTH_SHORT).show();

                  break;
              default:
                  break;


          }


      }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_out);
        good_out_input= (EditText) findViewById(R.id.good_out_input);
        good_out_to= (EditText) findViewById(R.id.good_out_to);
        goods_out_id= (TextView) findViewById(R.id.good_out_id);
        good_out_name= (TextView) findViewById(R.id.good_out_name);
        good_out_spec= (TextView) findViewById(R.id.good_out_spec);
        good_out_price= (TextView) findViewById(R.id.good_out_price);
        good_out_quan= (TextView) findViewById(R.id.good_out_quan);
        good_out_verify= (Button) findViewById(R.id.good_out_verify);
        good_out_save=(Button) findViewById(R.id.good_out_save);
        good_out_verify.setOnClickListener(this);
        good_out_save.setOnClickListener(this);

        good= (Good) getIntent().getSerializableExtra("good");
        if (good.getGoodid()!=-1)
        {
            goods_out_id.setText(good.getGoodid()+"");
            good_out_name.setText(good.getGoodname());
            good_out_spec.setText(good.getGoodspec());
            good_out_price.setText(good.getGoodprice()+"元");
            good_out_quan.setText(good.getGoodquan()+"");
            remain=good.getGoodquan();
            good_out_to.requestFocus();

        }

    }

    @Override
    public void onClick(View v) {
       switch (v.getId())
       {
           case R.id.good_out_verify:
               String gname=good_out_input.getText().toString();
               if (!gname.equals(""))
               {

                   boolean isNum = gname.matches("[0-9]+");
                   Log.d("isNum",isNum+"");
                   if (isNum)
                   {
                       int gid=Integer.valueOf(gname);
                       getFromServletById(gid);
                   }
                   else
                   {
                       getFromServletByname(gname);
                   }
                   break;
               }
               else
               {
                   Toast.makeText(getApplicationContext(),"请输入货物名称",Toast.LENGTH_SHORT).show();
               }
               break;
           case R.id.good_out_save:
               int out= Integer.valueOf(good_out_to.getText().toString());
               if(remain>=out)
               {

                   remain=remain-out;
                   //可以进行出货
                   UpdateRemain(good.getGoodid(),remain, out,good.getGoodname());
               }
               else
               {
                   Toast.makeText(getApplicationContext(),"输入数字不得大于剩余量",Toast.LENGTH_SHORT).show();
               }

               break;
           default:

               break;

       }
    }
    public boolean UpdateRemain(int good_id,int remain,int out,String good_name)
    {
        //更新数据库数据

        final String url="http://"+ Net.ip+":8080/WORK/servlet/UpdateRemainServlet?good_id="+good_id+"&remain="+remain+"&out="+out+"&good_name="+good_name;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
//                    http://localhost:8080/WORK/servlet/UpdateRemainServlet?good_id=1&remain=0
                    URL Url=new URL(url);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) Url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    InputStream in=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line=bufferedReader.readLine())!=null)
                    {
                        sb.append(line);
                    }
                    String jsonstr=sb.toString();

                    JSONObject jsonObject=new JSONObject(jsonstr);
                    int status=jsonObject.getInt("status");
                    Message message=new Message();
                    message.what=status;
                    handler.sendMessage(message);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();



        return true;
    }


    public void getFromServletById(final int id)
    {


        final String url="http://"+ Net.ip+":8080/WORK/servlet/SearchGoodServletById?good_id="+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    URL Url=new URL(url);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) Url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    InputStream in=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line=bufferedReader.readLine())!=null)
                    {
                        sb.append(line);
                    }
                    String jsonstr=sb.toString();
                    JSONObject jsonObject=new JSONObject(jsonstr);
                    Good good=new Good();
                    good.setGoodid(jsonObject.getInt("good_id"));
                    good.setGoodprice(jsonObject.getInt("good_price"));
                    good.setGoodquan(jsonObject.getInt("good_quan"));
                    good.setGoodname(jsonObject.getString("good_name"));
                    good.setGoodspec(jsonObject.getString("good_spec"));
                    good.setGoodurl(jsonObject.getString("good_url"));
                    Message message=new Message();
                    message.what=1;
                    message.obj=good;
                    handler.sendMessage(message);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getFromServletByname(String n)
    {
        final String name=n.replaceAll(" ","");
        Log.d("----s--",name);
        final String url="http://"+ Net.ip+":8080/WORK/servlet/SearchGoodServlet?good_name="+(name.equals("")?"empty":name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    URL Url=new URL(url);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) Url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    InputStream in=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line=bufferedReader.readLine())!=null)
                    {
                        sb.append(line);
                    }
                    String jsonstr=sb.toString();
                    JSONObject jsonObject=new JSONObject(jsonstr);
                    Good good=new Good();
                    good.setGoodid(jsonObject.getInt("good_id"));
                    good.setGoodprice(jsonObject.getInt("good_price"));
                    good.setGoodquan(jsonObject.getInt("good_quan"));
                    good.setGoodname(jsonObject.getString("good_name"));
                    good.setGoodspec(jsonObject.getString("good_spec"));
                    good.setGoodurl(jsonObject.getString("good_url"));
                    Message message=new Message();
                    message.what=1;
                    message.obj=good;
                    handler.sendMessage(message);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
