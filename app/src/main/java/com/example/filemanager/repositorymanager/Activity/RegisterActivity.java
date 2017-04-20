package com.example.filemanager.repositorymanager.Activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filemanager.repositorymanager.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    public EditText register_username;
    public EditText register_password;
    public EditText register_verify_password;
    public TextView info;
    public Button register;
    boolean f=false;
    public Handler handler=new Handler()
    {
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case -1:
                    f=true;//可以注册
                    Register(register_username.getText().toString(),register_password.getText().toString());
                    break;
                case 0:case 1:case 2:case 3:
                   Toast.makeText(getApplicationContext(),"用户名已存在",Toast.LENGTH_SHORT).show();
                   break;
                case 10:
                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 11:
                    Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    f=false;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_username= (EditText) findViewById(R.id.register_username);
        register_password= (EditText) findViewById(R.id.register_password);
        register_verify_password= (EditText) findViewById(R.id.register_verify_password);
        info= (TextView) findViewById(R.id.info);
        register= (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=register_username.getText().toString();
                String password=register_password.getText().toString();
                String verify_password=register_verify_password.getText().toString();
                if (username.contains(" "))
                {
                    //info.setText("账号中含有空格");
                    Toast.makeText(getApplicationContext(),"账号中含有空格",Toast.LENGTH_SHORT).show();


                }
                else if (!password.equals(verify_password))
                {
//                    info.setText("两次输入的密码不同");
                    Toast.makeText(getApplicationContext(),"两次输入的密码不同",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    verifyUsername(username);

                }

            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void verifyUsername(String user_name)
    {
        String  user_password="abc";
        final String url="http://169.254.186.190:8080/WORK/servlet/LoginServlet?username="+
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
                    message.what =json.getInt("status");
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void Register(String username,String password)
    {
        final String url="http://169.254.186.190:8080/WORK/servlet/RegisterServlet?username="+
                username+"&password="+ password;
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
                    message.what =json.getInt("status");
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
