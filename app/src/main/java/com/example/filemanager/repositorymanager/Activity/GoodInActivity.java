package com.example.filemanager.repositorymanager.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.filemanager.repositorymanager.Entity.Good;
import com.example.filemanager.repositorymanager.Entity.Net;
import com.example.filemanager.repositorymanager.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoodInActivity extends AppCompatActivity {
    public EditText good_in_id;
    public EditText good_in_name;
    public EditText good_in_spec;
    public EditText good_in_price;
    public EditText good_in_quan;
    public TextView good_in_img;
    public Button good_in_verify;
    public Button good_in_save;
    public Button select_img;

  //  public TextView good_in_info;
    public int RESULT_LOAD_IMAGE = 1;
    public String imgpath=null;
    int type = -1;
    public Handler handler = new Handler() {
        public void handleMessage(Message message) {
            type = message.what;
            String info = "";
            switch (message.what) {
                case 0: //有这个id
                    info = "商品已存在 只更新数据";
                    Good good= (Good) message.obj;
                    good_in_name.setText(good.getGoodname());
                    good_in_price.setText(good.getGoodprice()+"元");
                    good_in_spec.setText(good.getGoodspec());
                    break;
                case 1:
                    info = "无此商品 将添加新商品";
                    good_in_name.setText("");
                    good_in_price.setText("");
                    good_in_spec.setText("");
                    break;
                case 3://更新添加成功
                    formUpload("http://"+Net.ip+":8080/WORK/servlet/SaveImageServlet", imgpath);
                    break;
                case 4:
                    //info="更新或添加失败";
                    Toast.makeText(GoodInActivity.this, "失败", Toast.LENGTH_SHORT).show();
                case 6:
                    //图片上传失败
                    Toast.makeText(GoodInActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    //数据库图片信息更新成功
                    Toast.makeText(GoodInActivity.this,"成功", Toast.LENGTH_SHORT).show();
                    Message message1=new Message();
                    message1.what=-3;
                    handler.sendMessageAtTime(message1,1000);
                    break;
                case 8:
                    //数据库图片信息更新失败
                    Toast.makeText(GoodInActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    break;
                case -3:
                    finish();
                    break;
                case -11:
                    Toast.makeText(getApplicationContext(),"连接超时",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            Toast.makeText(getApplicationContext(),info,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_in);
        good_in_id = (EditText) findViewById(R.id.good_in_input);
        good_in_name = (EditText) findViewById(R.id.good_in_name);
        good_in_spec = (EditText) findViewById(R.id.good_in_spec);
        good_in_price = (EditText) findViewById(R.id.good_in_price);
        good_in_quan = (EditText) findViewById(R.id.good_in_quan);
        good_in_img = (TextView) findViewById(R.id.img_path);
        good_in_verify = (Button) findViewById(R.id.good_in_verify);
        good_in_save = (Button) findViewById(R.id.good_in_save);
        select_img= (Button) findViewById(R.id.select_img);

        good_in_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //存至数据库
                if (imgpath!=null)
                {
                    String good_name = good_in_name.getText().toString();
                    int good_id = Integer.valueOf(good_in_id.getText().toString());
                    String good_spec = good_in_spec.getText().toString();
                    int good_price = Integer.valueOf(good_in_price.getText().toString());
                    int good_quan = Integer.valueOf(good_in_quan.getText().toString());
                    String url = "http://"+ Net.ip+":8080/WORK/servlet/AddGoodsServlet?good_name="
                            + good_name + "&good_id=" + good_id + "&good_spec=" + good_spec + "&good_price=" + good_price
                            + "&good_quan=" + good_quan + "&type=" + type;

                    saveData(url);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"请选择图片",Toast.LENGTH_SHORT).show();
                }

                // formUpload("http://169.254.186.190:8080/WORK/servlet/SaveImageServlet", imgpath);
            }
        });
        good_in_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final int good_id = Integer.valueOf(good_in_id.getText().toString());
                    //判断id是否已存在 在一个线程里查询数据库 然后发送给handler 由handler处理 显示
                    final String url = "http://"+ Net.ip+":8080/WORK/servlet/VerifyGoodIdServlet?good_id=" + good_id;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                URL Url = new URL(url);

                                HttpURLConnection httpURLConnection = (HttpURLConnection) Url.openConnection();
                                httpURLConnection.setRequestMethod("GET");
                                httpURLConnection.setConnectTimeout(8000);
                                httpURLConnection.setReadTimeout(8000);
                                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                                InputStream in = httpURLConnection.getInputStream();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                                StringBuilder sb = new StringBuilder();
                                String line;
                                while ((line = bufferedReader.readLine()) != null) {
                                    sb.append(line);
                                }
                                JSONObject json = new JSONObject(sb.toString());
                                Message message = new Message();
                                Good good=new Good();
                                Log.e("good",json.toString());
                                good.setGoodid(json.getInt("good_id"));
                                good.setGoodprice(json.getInt("good_price"));
                                good.setGoodquan(json.getInt("good_quan"));
                                good.setGoodname(json.getString("good_name"));
                                good.setGoodspec(json.getString("good_spec"));
                                good.setGoodurl(json.getString("good_url"));
                                message.obj=good;
                                if (good.getGoodid()==-1)
                                {
                                    message.what=1;
                                }
                                else
                                {
                                    message.what=0;
                                }
                                handler.sendMessage(message);

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"连接超时",Toast.LENGTH_SHORT).show();

                            }


                        }
                    }).start();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgpath = cursor.getString(columnIndex);
            good_in_img.setText(imgpath);
            Log.e("path------", imgpath);
            cursor.close();
        }


    }

    public void saveData(final String url) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    String str= URLEncoder.encode(url, "utf-8");
                    URL Url = new URL(url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) Url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    InputStream in = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    JSONObject json = new JSONObject(sb.toString());
                    Message message = new Message();
                    message.what = json.getInt("status");
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"连接超时",Toast.LENGTH_SHORT).show();

                }


            }
        }).start();


    }

    public String formUpload(final String urlStr, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String rsp = "";
                HttpURLConnection conn = null;
                String BOUNDARY = "|"; // request头和上传文件内容分隔符
                try {
                    URL url = new URL(urlStr);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(30000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=" + BOUNDARY);
                    OutputStream out = new DataOutputStream(conn.getOutputStream());
                    File file = new File(filePath);
                    String filename = file.getName();
                    Log.e("------",filename);
                    String contentType = "";
                    if (filename.endsWith(".png")) {
                        contentType = "image/png";

                    }
                    if (filename.endsWith(".jpg")) {
                        contentType = "image/jpg";

                    }
                    if (filename.endsWith(".gif")) {
                        contentType = "image/gif";

                    }
                    if (filename.endsWith(".bmp")) {
                        contentType = "image/bmp";

                    }
                    if (contentType == null || contentType.equals("")) {
                        contentType = "application/octet-stream";
                    }

                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + filePath
                            + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes());
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                    byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
                    out.write(endData);
                    out.flush();
                    out.close();
                    // 读取返回数据
                    StringBuffer buffer = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }
                    rsp = buffer.toString();
                    int good_id = Integer.valueOf(good_in_id.getText().toString());
                    updateImagePath(rsp, good_id);
                } catch (Exception e) {
                    Message message=new Message();
                    message.what=-11;
                    handler.sendMessage(message);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                        conn = null;
                    }
                }

            }
        }).start();

        return null;
    }

    RequestListener<String, GlideDrawable> errorListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

            Log.e("onException", e.toString() + "  model:" + model + " isFirstResource: " + isFirstResource);
            // imageView.setImageResource(R.mipmap.ic_launcher);
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Log.e("onResourceReady", "isFromMemoryCache:" + isFromMemoryCache + "  model:" + model + " isFirstResource: " + isFirstResource);
            return false;
        }
    };
    //更新服务端的图片名称
    public void updateImagePath(String rsp, int good_id) {
        try {
            JSONObject json = new JSONObject(rsp);

            json.getInt("status");
            String filename = json.getString("filename");
            if (json.getInt("status") == 5) {//成功
                String update = "http://"+ Net.ip+":8080/WORK/servlet/UpdateImagenameServlet?good_id="
                        + good_id + "&filename=" + filename;
                URL url = new URL(update);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(30000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                InputStream in = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    sb.append(s);
                }
                json = new JSONObject(sb.toString());
                Message message = new Message();
                message.what = json.getInt("status");
                handler.sendMessage(message);

            } else {
                Message message = new Message();
                message.what = 6;
                handler.sendMessage(message);
            }

        } catch (Exception e) {
            Message message=new Message();
            message.what=-11;
            handler.sendMessage(message);
        }

    }
}
