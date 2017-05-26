package com.example.filemanager.repositorymanager.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class Good  implements Serializable{
    public String good_id;//商品id
    public int good_price;//商品单价
    public int good_quan;//商品数量
    public String good_name;//商品名称
    public String good_spec;//商品规格
    public String good_url;//商品图片的地址

    public String getGoodid() {
        return good_id;
    }

    public void setGoodid(String good_id) {
        this.good_id = good_id;
    }

    public int getGoodprice() {
        return good_price;
    }

    public void setGoodprice(int good_price) {
        this.good_price = good_price;
    }

    public int getGoodquan() {
        return good_quan;
    }

    public void setGoodquan(int good_quan) {
        this.good_quan = good_quan;
    }

    public String getGoodname() {
        return good_name;
    }

    public void setGoodname(String good_name) {
        this.good_name = good_name;
    }

    public String getGoodspec() {
        return good_spec;
    }

    public void setGoodspec(String good_spec) {
        this.good_spec = good_spec;
    }

    public String getGoodurl() {
        return good_url;
    }

    public void setGoodurl(String good_url) {
        this.good_url = good_url;
    }


}
