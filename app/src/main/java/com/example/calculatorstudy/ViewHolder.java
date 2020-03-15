package com.example.calculatorstudy;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

/**
 * Time:2020/3/14/014
 * <p>
 * Author:Administrator
 * <p>
 * Description:
 */
public class ViewHolder {
    private View rootView;
    private SparseArray<View> mViews;

    public ViewHolder(){

    }
    public ViewHolder(LayoutInflater layoutInflater,int resourId,ViewGroup parent){
        mViews=new SparseArray<View>();
        rootView=layoutInflater.inflate(resourId,parent,false);
    }

    /**
     * 获取view
     * @param viewId view的id
     * @param <T> 泛型
     * @return
     */
    public <T extends View> T get(int viewId){
        View view=mViews.get(viewId);
        if (view==null){
            view=rootView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }

    /**
     * 返还的是根view
     * @return
     */
    public View getRootView(){
        return rootView;
    }

    /**
     * @param l 监听
     * @param viewids 多个view 的id
     */
    public void setOnClickLinstener(View.OnClickListener l,int... viewids){
        if (viewids==null){
            return;
        }
        for (int id:viewids) {
            get(id).setOnClickListener(l);
        }
    }


}
