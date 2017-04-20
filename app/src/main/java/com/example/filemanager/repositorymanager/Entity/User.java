package com.example.filemanager.repositorymanager.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public class User implements Serializable {
    public String username;
    public String password;
    public int userType;//1 是系统管理员 2是仓库管理员 3是普通用户

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }



}
