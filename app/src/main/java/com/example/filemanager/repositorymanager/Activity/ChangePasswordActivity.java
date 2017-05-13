package com.example.filemanager.repositorymanager.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.filemanager.repositorymanager.Entity.Net;
import com.example.filemanager.repositorymanager.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChangePasswordActivity extends AppCompatActivity {
    public EditText change_username;
    public EditText change_old_password;
    public EditText change_password;
    public EditText verify_change_password;
    public Button change;
    public boolean canChange=false;
    public Handler handler=new Handler()
    {
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case 1:
                case 2:
                case 3:
                    canChange=true;
                    changePassword();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                    Message message1=new Message();
                        message1.what=-3;
                    handler.sendMessageAtTime(message1,1000);
                case 0:
                    Toast.makeText(getApplicationContext(),"账号密码不匹配",Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(),"无此用户",Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
                    break;
                case  -3:
                    finish();
                    break;
                default:
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        change_username= (EditText) findViewById(R.id.change_password_username);
        change_old_password= (EditText) findViewById(R.id.change_old_password);

        change_password= (EditText) findViewById(R.id.change_password);
        verify_change_password= (EditText) findViewById(R.id.verify_change_password);
        change= (Button) findViewById(R.id.change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 verify();
            }
        });

    }
    public void verify()
    {
        String  user_name=change_username.getText().toString();
        String  user_password=change_old_password.getText().toString();
        final String url="http://"+ Net.ip+":8080/WORK/servlet/LoginServlet?username="+
                user_name+"&password="+ user_password;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
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
                    JSONObject json=new JSONObject(sb.toString());
                    Message message=new Message();
                    message.what=json.getInt("status");
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void changePassword()
    {
        String  user_name=change_username.getText().toString();
        String  user_password=change_password.getText().toString();
        final String url="http://"+ Net.ip+":8080/WORK/servlet/ChangeServlet?username="+
                user_name+"&newpassword="+ user_password+"&newpermission=empty";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
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
                    JSONObject json=new JSONObject(sb.toString());
                    Message message=new Message();
                    message.what=json.getInt("status");
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
