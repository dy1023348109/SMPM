package com.example.filemanager.repositorymanager.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.filemanager.repositorymanager.Adapter.MyArrayAdapter;
import com.example.filemanager.repositorymanager.Adapter.MyNoteAdapter;
import com.example.filemanager.repositorymanager.Entity.Good;
import com.example.filemanager.repositorymanager.Entity.Note;
import com.example.filemanager.repositorymanager.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/4/6 0006.
 */

public class NotesFragment extends Fragment  implements View.OnClickListener{
    @Nullable
    DateFormat fmtDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            //修改日历控件的年，月，日
            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //将页面TextView的显示更新为最新时间
            note_input_area.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            dateAndTime=Calendar.getInstance(Locale.CHINA);

        }
    };
    public String TAG="NotesFragment";
    public View mView;
    public EditText note_input_area;
    public Button note_search;
    public Button note_date;
    public ListView note_list;
    public ArrayList<Note> noteList;
    public MyNoteAdapter myNoteAdapter;

    public Handler handler=new Handler()
    {
        public void handleMessage(Message message)
        {

            if (message.what==1)
            {
                Log.d("------datalist------",noteList.size()+"");
                if (noteList.size()==0)
                {
                    Toast.makeText(getContext(),"无此记录",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    myNoteAdapter=new MyNoteAdapter(getContext(),R.layout.note_listview_layout,noteList);
                    note_list.setAdapter(myNoteAdapter);
                }


            }


        }
    };


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.note_layout,null);
        initView();
        return mView;
    }
    public void initView()
    {
        note_input_area= (EditText) mView.findViewById(R.id.note_input_area);
        note_search= (Button) mView.findViewById(R.id.note_search);
        note_date= (Button) mView.findViewById(R.id.note_date);
        note_list= (ListView) mView.findViewById(R.id.note_list);
        getFromServlet("","");
        note_date.setOnClickListener(this);
        note_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.note_search:
                String input=note_input_area.getText().toString();
                String[] temp=input.split("-");
                Log.d("__temp__", temp.length+"");
                Log.d("__temp__", input);
                if (temp.length==1)
                {
                    getFromServlet(input,"");

                }
                else
                {
                    getFromServlet("",input);
                }
                break;
            case R.id.note_date:
                DatePickerDialog dateDlg = new DatePickerDialog(getContext(),
                        d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH));

                dateDlg.show();

                break;
            default:
                break;

        }
    }

    public void getFromServlet(String n,String date)
    {
        final String name=n.replaceAll("","");
        final String url="http://169.254.186.190:8080/WORK/servlet/SearchNoteServlet?good_name="+(name.equals("")?"empty":name)+"&note_date="+(date.equals("")?"empty":date);
        Log.d(TAG,url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

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
                    Log.e("---",jsonstr);
                    Log.d(TAG,jsonstr);
                    noteList=ShowData(jsonstr,name);
                    Log.e("-------------",noteList.toString());
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
    public ArrayList<Note> ShowData(String jsonstr, String goodname)  {
        ArrayList<Note> nl=new ArrayList<Note>();
        if(goodname.equals(""))
        {
            //显示多条数据  json Array
            try
            {
                JSONObject jsonObject=new JSONObject(jsonstr);
                JSONArray jsonArray=jsonObject.getJSONArray("data");

                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jo=jsonArray.getJSONObject(i);
                    Note note=new Note();
                    note.setNote_good_id(jo.getInt("note_good_id"));
                    note.setNote_good_name(jo.getString("note_good_name"));
                    note.setNote_id(jo.getString("note_id"));
                    note.setNote_quan(jo.getInt("note_quan"));
                    note.setNote_time(jo.getString("note_time"));
                    note.setNote_type(jo.getInt("note_type"));

                    nl.add(note);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        else
        {

            try{
                JSONObject jsonObject=new JSONObject(jsonstr);
                Note note=new Note();
                note.setNote_good_id(jsonObject.getInt("note_good_id"));
                note.setNote_good_name(jsonObject.getString("note_good_name"));
                note.setNote_id(jsonObject.getString("note_id"));
                note.setNote_quan(jsonObject.getInt("note_quan"));
                note.setNote_time(jsonObject.getString("note_time"));
                note.setNote_type(jsonObject.getInt("note_type"));
                nl.add(note);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        return  nl;


    }
}
