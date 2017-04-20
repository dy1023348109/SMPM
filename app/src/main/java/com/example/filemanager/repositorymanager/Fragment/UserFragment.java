package com.example.filemanager.repositorymanager.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filemanager.repositorymanager.Activity.ChangePasswordActivity;
import com.example.filemanager.repositorymanager.Activity.ChangePermissionActivity;
import com.example.filemanager.repositorymanager.Entity.User;
import com.example.filemanager.repositorymanager.R;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public class UserFragment extends Fragment implements View.OnClickListener {
    public View mView;
    public User user;
    public TextView current_user;
    public TextView current_user_type;
    public Button change_password;
    public Button change_permission;
    public int permission;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.user_layout,null);
        //获取当前用户
        user= (User) getArguments().getSerializable("user");
        initView();
        return mView;
    }
    public void initView()
    {
        current_user= (TextView) mView.findViewById(R.id.current_user);
        current_user_type= (TextView) mView.findViewById(R.id.current_user_type);
        change_password= (Button) mView.findViewById(R.id.change_password);
        change_permission= (Button) mView.findViewById(R.id.change_permission);
        permission=user.getUserType();
        current_user.setText(user.getUsername());
        current_user_type.setText((permission==1?"系统管理员":permission==2?"仓库管理员":"普通用户"));
        change_password.setOnClickListener(this);
        change_permission.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (permission!=1)
        {
            Toast.makeText(getContext(), "权限不足,请联系系统管理员", Toast.LENGTH_SHORT).show();
        }
        else  if (view.getId()==R.id.change_password)
        {   //更改密码
            Intent intent=new Intent();
            intent.setClass(getContext(), ChangePasswordActivity.class);
            startActivity(intent);
        }else if (view.getId()==R.id.change_permission)
        {    //更改权限
            Intent intent=new Intent();
            intent.setClass(getContext(), ChangePermissionActivity.class);
            startActivity(intent);

        }

    }
}
