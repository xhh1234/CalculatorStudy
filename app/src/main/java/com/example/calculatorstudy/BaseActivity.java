package com.example.calculatorstudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Time:2020/3/14/014
 * <p>
 * Author:Administrator
 * <p>
 * Description:
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ViewHolder viewHolder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewHolder=new ViewHolder(LayoutInflater.from(this),LayoutId(),null);
        setContentView(viewHolder.getRootView());
        initView(viewHolder.getRootView());
    }

    public abstract int LayoutId();

    protected void initView(View view) {
    }

}
