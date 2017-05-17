package com.example.filemanager.repositorymanager.Activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filemanager.repositorymanager.Entity.Net;
import com.example.filemanager.repositorymanager.Entity.User;
import com.example.filemanager.repositorymanager.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChangePermissionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener{
    public EditText change_permission_username;
    public EditText change_permission_password;
    public int permission=3;
    public Spinner spinner;
    public Button change_permission;
    public ArrayAdapter<String> arrayAdapter;
    public boolean canChange;
    public   String[] res={"1.系统管理员","2.仓库管理员","3.普通用户"};
    public Handler handler=new Handler()
    {
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case 1:
                case 2:
                case 3:
                    //canChange=true;
                    changePermission();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(),"修改权限成功",Toast.LENGTH_SHORT).show();
                    Message message1=new Message();
                    message1.what=-3;
                    handler.sendMessageAtTime(message1,1000);

                    break;
                case 0:
                    Toast.makeText(getApplicationContext(),"账号密码不匹配",Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(),"无此用户",Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
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
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_permission);
        initView();

    }
    public void initView()
    {
        change_permission_username= (EditText) findViewById(R.id.change_permission_username);
        change_permission_password= (EditText) findViewById(R.id.change_permission_password);
        spinner= (Spinner) findViewById(R.id.spinner);
        change_permission= (Button) findViewById(R.id.change_permission);
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,res);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setVisibility(View.VISIBLE);
        spinner.setSelection(2);
        change_permission.setOnClickListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


            permission=i+1;


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        verify();
    }

    private void changePermission() {
        {
            String  user_name=change_permission_username.getText().toString();
            String  user_password=change_permission_password.getText().toString();
            final String url="http://"+ Net.ip+":8080/WORK/servlet/ChangeServlet?username="+
                    user_name+"&newpassword=empty"+"&newpermission="+permission;




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

                        Message message=new Message();
                        message.what=-11;
                        handler.sendMessage(message);
                    }
                }
            }).start();
        }
    }

    public void verify() {
        String user_name = change_permission_username.getText().toString();
        String user_password = change_permission_password.getText().toString();
        final String url = "http://" + Net.ip + ":8080/WORK/servlet/LoginServlet?username=" +
                user_name + "&password=" + user_password;


        if (user_name.contains(" ") || user_password.contains(" ")) {
            Toast.makeText(getApplicationContext(), "输入有误，请重新输入", Toast.LENGTH_SHORT).show();
        } else if (user_name.length() < 5 || user_password.length() < 5) {
            Toast.makeText(getApplicationContext(), "至少输入5位，请重新输入", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL Url = new URL(url);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) Url.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setConnectTimeout(8000);
                        httpURLConnection.setReadTimeout(8000);
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
                        Message message = new Message();
                        message.what = -11;
                        handler.sendMessage(message);
                    }
                }
            }).start();
        }
    }
}
