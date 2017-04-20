package com.example.filemanager.repositorymanager.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.filemanager.repositorymanager.Activity.GoodInActivity;
import com.example.filemanager.repositorymanager.Activity.GoodOutActivity;
import com.example.filemanager.repositorymanager.Activity.GoodsActivity;
import com.example.filemanager.repositorymanager.Adapter.MyArrayAdapter;
import com.example.filemanager.repositorymanager.Entity.Good;
import com.example.filemanager.repositorymanager.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class GoodsFragment extends Fragment implements View.OnClickListener {
    @Nullable
    public List<Good> datalist= new ArrayList<Good>();
    public EditText input_area;
    public Button add;
    public Button search;
    public ListView good_list;
    public View mView;
    public MyArrayAdapter goodsAdapter;
    String [] item={"出库","入库"};

    public Handler handler=new Handler()
    {
        public void handleMessage(Message message)
        {

            if (message.what==1)
            {
                Log.d("------datalist------",datalist.size()+"");
                if (datalist.size()==0)
                {
                    Toast.makeText(getContext(),"没有这些商品",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    goodsAdapter=new MyArrayAdapter(getContext(),R.layout.goods_listview_layout,datalist);
                    good_list.setAdapter(goodsAdapter);
                }


            }


        }
    };
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.good_layout,null);
        initView();
        return mView;

    }
    public void initView()
    {

        input_area= (EditText) mView.findViewById(R.id.input_area);
        add= (Button) mView.findViewById(R.id.add);
        search= (Button) mView.findViewById(R.id.search);
        good_list= (ListView) mView.findViewById(R.id.goods_list);
        getFromServlet("");

        add.setOnClickListener(this);
        search.setOnClickListener(this);
        good_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Good good= datalist.get(i);
                        Intent intent=new Intent();
                        intent.setClass(getContext(), GoodsActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("good",good);
                        intent.putExtras(bundle);
                        startActivity(intent);

            }
        });
    }

    public void getFromServlet(String n)
    {
        final String name=n.replaceAll(" ","");
        Log.d("----s--",name);
        final String url="http://169.254.186.190:8080/WORK/servlet/SearchGoodServlet?good_name="+(name.equals("")?"empty":name);
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
                     Log.e("-----------",jsonstr);
                    // String jsonstr=new String(js.getBytes("iso-8859-1"),"utf-8");
                     ArrayList<Good> goodlist=new ArrayList<Good>();

                     ShowData(jsonstr,name,goodlist);
                     datalist=goodlist;
                     Log.d("----goodlist---",goodlist.size()+"");
                     Log.d("----datalist---",datalist.size()+"");
                     Message message=new Message();
                     message.what=1;
                     handler.sendMessage(message);

                 }
                 catch (Exception e)
                 {
                     e.printStackTrace();
                 }
             }
         }).start();
    }
    public  List<Good> ShowData(String jsonstr,String goodname,List<Good> dl)  {
        if(goodname.equals(""))
        {
            //显示全部数据  json Array
            try
            {
                //   data=getFromServlet("empty");
                JSONObject jsonObject=new JSONObject(jsonstr);
                JSONArray jsonArray=jsonObject.getJSONArray("data");

                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jo=jsonArray.getJSONObject(i);
                    Good good=new Good();
                    good.setGoodid(jo.getInt("good_id"));
                    good.setGoodprice(jo.getInt("good_price"));
                    good.setGoodquan(jo.getInt("good_quan"));
                    good.setGoodname(jo.getString("good_name"));
                    good.setGoodspec(jo.getString("good_spec"));
                    good.setGoodurl(jo.getString("good_url"));
                    dl.add(good);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            //显示 一条   jsonObject
            try{
                //  data=getFromServlet(goodname);
                JSONObject jsonObject=new JSONObject(jsonstr);
                Good good=new Good();
                good.setGoodid(jsonObject.getInt("good_id"));
                good.setGoodprice(jsonObject.getInt("good_price"));
                good.setGoodquan(jsonObject.getInt("good_quan"));
                good.setGoodname(jsonObject.getString("good_name"));
                good.setGoodspec(jsonObject.getString("good_spec"));
                good.setGoodurl(jsonObject.getString("good_url"));
                dl.add(good);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        return  dl;

    }
    @Override
    public void onClick(View v) {
         int id=v.getId();
        switch (id)
        {
            case R.id.add:  //货物入库或者出库
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("货物出库或入库");
                builder.setItems(item, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which)
                        {
                            case 0:
                                //出库
                                Intent intent1=new Intent();
                                intent1.setClass(getContext(),GoodOutActivity.class);
                                startActivity(intent1);

                                break;
                            case 1:
                                //入库
                                Intent intent2=new Intent();
                                intent2.setClass(getContext(),GoodInActivity.class);
                                startActivity(intent2);
                                break;
                        }
                       }
                    });
              builder.show();
            break;
            case R.id.search://查找货物
                String gname=input_area.getText().toString();
                getFromServlet(gname);
                break;
            default:
                break;
        }
    }
}
