package com.zjmy.mvp.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 这个中间的base层用来做到viewHolder与adapter的解耦。
 */
public abstract class BaseViewHolder<M> extends RecyclerView.ViewHolder{
    public int mType;//item的类型
    public View view;
    protected final SparseArray<View> mViews = new SparseArray<View>();

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        initView();
    }

    //生成viewholder的构造方法。
    public BaseViewHolder(ViewGroup parent, int res , int type){
//        super(LayoutInflater.from(parent.getContext()).inflate(res,null));
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
        this.mType = type;
        view = itemView;
        initView();
    }

    private final <T extends View> T bindView(int id) {
        T v = (T) mViews.get(id);
        if (v == null) {
            v = (T) view.findViewById(id);
            mViews.put(id, v);
        }
        return v;
    }

    protected abstract void initView();

    public abstract void setData(M data);

    protected final <T extends View> T get(int id) {
        return (T) bindView(id);
    }

    public Context getContext(){
        return view.getContext();
    }
}

