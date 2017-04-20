package com.example.filemanager.repositorymanager.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filemanager.repositorymanager.Entity.User;
import com.example.filemanager.repositorymanager.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    TextView register;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        Button login=(Button)findViewById(R.id.login);
        register=(TextView)findViewById(R.id.login_register);
        username= (EditText) findViewById(R.id.user_name);
        password= (EditText) findViewById(R.id.password);
        final Handler handler=new Handler()
        {
            public void  handleMessage(Message message)
            {
                switch (message.what)
                {   //处理登录情况

                    case 0:     //密码错误
                        Toast.makeText(LoginActivity.this,"password error",Toast.LENGTH_SHORT).show();
                        break;
                    case -1:     //无此用户
                        Toast.makeText(LoginActivity.this,"no this user",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        //成功登陆
//                        Toast.makeText(LoginActivity.this,"successful",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        User user=new User();
                        user.setUsername(username.getText().toString());
                        user.setPassword(password.getText().toString());
                        user.setUserType(message.what);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("user",user);
                        intent.setClass(LoginActivity.this,MainActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;

                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取 输入的账号和密码
                 String  user_name=username.getText().toString();
                 String  user_password=password.getText().toString();
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
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

}
