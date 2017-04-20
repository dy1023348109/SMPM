package com.example.filemanager.repositorymanager.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.filemanager.repositorymanager.Entity.User;
import com.example.filemanager.repositorymanager.Fragment.GoodsFragment;
import com.example.filemanager.repositorymanager.Fragment.NotesFragment;
import com.example.filemanager.repositorymanager.Fragment.UserFragment;
import com.example.filemanager.repositorymanager.R;

public class MainActivity extends AppCompatActivity {
    public FrameLayout frameLayout;
    public FragmentManager fragmentManager;
    public android.support.v4.app.Fragment currentfragment;
    public GoodsFragment goodsFragment;
    public NotesFragment notesFragment;
    public UserFragment userFragment;
    public User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        //当前登录的用户
        user= (User) getIntent().getSerializableExtra("user");

        fragmentManager=getSupportFragmentManager();
        frameLayout= (FrameLayout) findViewById(R.id.frame_area);
        BottomNavigationView bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNV);

        goodsFragment=new GoodsFragment();

        currentfragment=goodsFragment;
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.frame_area,goodsFragment);
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.goods:

                        if (currentfragment!=goodsFragment)
                        {


                            if (goodsFragment == null) {
                                goodsFragment = new GoodsFragment();

                            }
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
//                    transaction.hide(currentfragment);
//                    transaction.add(R.id.frame_area, userFragment);
                            transaction.replace(R.id.frame_area,goodsFragment);
                            currentfragment = goodsFragment;
                            transaction.commit();
                        }

                        break;
                    case  R.id.record:
                        if (currentfragment!=notesFragment)
                        {


                            if (notesFragment == null) {
                                notesFragment = new NotesFragment();

                            }
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.frame_area,notesFragment);
                            currentfragment = notesFragment;
                            transaction.commit();
                        }


                        break;
                    case R.id.user:
                        if (currentfragment!=userFragment)
                        {


                            if (userFragment == null) {
                                userFragment = new UserFragment();

                            }
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("user",user);
                            userFragment.setArguments(bundle);
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.frame_area,userFragment);
                            currentfragment = userFragment;
                            transaction.commit();
                        }

                        break;
                }

               return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
