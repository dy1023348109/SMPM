package com.example.filemanager.repositorymanager.Entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Administrator on 2017/4/7 0007.
 */

public class Note  implements Serializable{

    String  note_id;
    int   note_type;
    int   note_good_id;
    int  note_quan;
    String note_time;
    String  note_good_name;

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getNote_time() {
        return note_time;
    }

    public void setNote_time(String note_time) {
        this.note_time = note_time;
    }

    public int getNote_type() {
        return note_type;
    }

    public void setNote_type(int note_type) {
        this.note_type = note_type;
    }

    public String getNote_good_name() {
        return note_good_name;
    }

    public void setNote_good_name(String note_good_name) {
        this.note_good_name = note_good_name;
    }

    public int getNote_good_id() {
        return note_good_id;
    }

    public void setNote_good_id(int note_good_id) {
        this.note_good_id = note_good_id;
    }

    public int getNote_quan() {
        return note_quan;
    }

    public void setNote_quan(int note_quan) {
        this.note_quan = note_quan;
    }

}
