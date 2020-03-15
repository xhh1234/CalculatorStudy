package com.example.calculatorstudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Time:2020/3/14/014
 * <p>
 * Author:Administrator
 * <p>
 * Description:
 */
public abstract class BaseFragment extends Fragment {
    private ViewHolder mViewHolder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder=new ViewHolder(inflater,LayoutId(),container);
        return mViewHolder.getRootView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews(mViewHolder.getRootView(),mViewHolder);
    }

    protected abstract int LayoutId();
    protected void initViews(View view,ViewHolder viewHolder){

    }

}
