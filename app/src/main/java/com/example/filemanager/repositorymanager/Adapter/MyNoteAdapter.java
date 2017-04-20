package com.example.filemanager.repositorymanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
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
import com.example.filemanager.repositorymanager.Entity.Note;
import com.example.filemanager.repositorymanager.Entity.NoteView;
import com.example.filemanager.repositorymanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/7 0007.
 */

public class MyNoteAdapter extends ArrayAdapter {
    private int resource;
    private Context context;
    public MyNoteAdapter(Context context, int resource, ArrayList<Note> Objects) {
        super(context, resource, Objects);
        this.resource=resource;
        this.context=context;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteView noteView;
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.note_listview_layout,parent,false);
            noteView=new NoteView();
            noteView.note_good_name= (TextView) convertView.findViewById(R.id.note_goods_name);
            noteView.note_quan= (TextView) convertView.findViewById(R.id.note_quan);
            noteView.note_time= (TextView) convertView.findViewById(R.id.note_time);
            noteView.note_type= (TextView) convertView.findViewById(R.id.note_type);
            noteView.type=(ImageView)convertView.findViewById(R.id.type);
            convertView.setTag(noteView);
        }
        else
        {
            noteView= (NoteView) convertView.getTag();
        }
        final Note note= (Note) getItem(position);
        Log.d("--adapter--",note.getNote_good_name());
        noteView.note_good_name.setText(note.getNote_good_name());
        noteView.note_quan.setText("数量:"+note.getNote_quan()+"");
        noteView.note_time.setText("时间:"+note.getNote_time());
        int type=note.getNote_type();
        noteView.note_type.setText(type==1?"入库":"出库");
        noteView.type.setImageResource(type==1?R.drawable.in:R.drawable.out);

//
//        goodView.more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setClass(context, GoodsActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("good",good);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }
}
