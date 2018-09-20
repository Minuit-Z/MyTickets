package com.zjmy.mvp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zjmy.mvp.view.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 所有的数据管理都放在BaseAdapter里面。
 * 由于这个适配器并不提供添加header和foot的功能，所以getItemCount()最好还是要开发者自己实现
 */
public abstract class RecyclerAdapterPresenter<M> extends RecyclerView.Adapter<BaseViewHolder> {
    protected Context mContext;
    //item数据的model集合
    protected List<M> mData;
    //开发者添加的头数量
    private int headNum = 0;
    //开发者添加的尾的数量
    private int footNum = 0;
    //item的点击监听接口

    private OnItemClickListener onItemClickListener;
    //用来提供同步代码块的锁
    private final Object mLock = new Object();


//-------------------------------构造器-----------------------------------

    public interface OnItemClickListener {
        void onClick(BaseViewHolder holder, int position);

        void onLongClick(BaseViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    /**
     * 不传入数据
     * @param mContext
     */
    public RecyclerAdapterPresenter(@NonNull Context mContext){
        this(mContext, new ArrayList<M>());
    }

    /**
     * 传入数组类型的数据
     * @param mContext
     * @param mDatas
     */
    public RecyclerAdapterPresenter(@NonNull Context mContext, @NonNull M[] mDatas){
        this(mContext, Arrays.asList(mDatas));
    }

    /**
     * 传入List类型的数据
     * @param mContext
     * @param mDatas
     */
    public RecyclerAdapterPresenter(@NonNull Context mContext, @NonNull List<M> mDatas){
        this.mContext = mContext;
        this.mData = mDatas;
    }


//---------------------------对继承函数的重写--------------------------------

    /**
     * 将viewType返回,具体的实现由开发者去做。
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return OnCreatViewHolder(parent, viewType);
    }

    public abstract BaseViewHolder OnCreatViewHolder(ViewGroup parent, int viewType);

    /**
     * 默认框架实现点击事件接口的回调
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        OnBindViewHloder(holder, position);

        //设置监听接口
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(holder, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(holder, position);
                    return true;
                }
            });
        }
    }

    public abstract void OnBindViewHloder(BaseViewHolder holder, int position);

    /**
     * 将datas中的某个数据返回给viewholder
     * 前提是在个position不是head不是foot,但是这个判断应该是开发者去实现。
     * 所以这里默认开发者调用的时候已经不是head和foot，所以直接根据开发提供的position减去headNum给数据
     * @param position
     * @return
     */
    public M getItem(int position){
        return mData.get(position - headNum);
    }

    @Override
    public int getItemCount() {
        return mData.size() + setHeadNum() + setFootNum();
    }

    //开发者去实现，告诉BaseAdapter添加了几个head或者foot
    public  int setHeadNum(){
        return 0;
    }
    public  int setFootNum(){
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

// ----------------------------对数据源的操作--------------------------------

    /**
     * 将数据添加到集合最末尾
     * @param data
     */
    public void add(M data){
        add(data, mData.size());
    }

    /**
     * 将数据添加到指定位置
     * @param data
     * @param position
     */
    public void add(M data, int position){
        if (data != null){
            synchronized (mLock){
                mData.add(position, data);
            }
        }
    }

    /**
     * 将一个集合添加到数据最尾端
     * @param collection
     */
    public void addAll(Collection<? extends M> collection){
        addAll(collection, mData.size());
    }

    public void addAll(Collection<? extends M> collection, int position){
        if (collection != null && collection.size() != 0 && position >= 0){
            synchronized (mLock){
                mData.addAll(position, collection);
            }
        }
    }

    /**
     * 删除集合最尾部数据
     */
    public void remove(){
        synchronized (mLock){
            remove(mData.size() - 1);
        }
    }

    /**
     * 删除指定位置的数据
     * @param position
     */
    public void remove(int position){
        if (position >= 0 && position <= (mData.size() - 1)){
            synchronized (mLock){
                mData.remove(position);
            }
        }
    }

    /**
     * 删除一个特定数据的元素
     * @param data
     */
    public void remove(M data){
        if (data != null){
            synchronized (mLock){
                mData.remove(data);
            }
        }
    }

    /**
     * 清空数据
     */
    public void clear(){
        synchronized (mLock){
            mData.clear();
        }
    }

}
