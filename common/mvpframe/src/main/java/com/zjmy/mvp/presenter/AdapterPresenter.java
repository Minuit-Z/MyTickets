package com.zjmy.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zjmy.mvp.view.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有的数据管理都放在BaseAdapter里面。
 * 由于这个适配器并不提供添加header和foot的功能，所以getItemCount()最好还是要开发者自己实现
 */
public abstract class AdapterPresenter<M> extends BaseAdapter {

    protected Context mContext;
    //item数据的model集合
    protected List<M> mData;
    //ViewHolder集合
    private Map<Integer, BaseViewHolder> mViewHolders;
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
     *
     * @param mContext
     */
    public AdapterPresenter(Context mContext) {
        this(mContext, new ArrayList<M>());
    }

    /**
     * 传入数组类型的数据
     *
     * @param mContext
     * @param mDatas
     */
    public AdapterPresenter(Context mContext, M[] mDatas) {
        this(mContext, Arrays.asList(mDatas));
    }

    /**
     * 传入List类型的数据
     *
     * @param mContext
     * @param data
     */
    public AdapterPresenter(Context mContext, List<M> data) {
        this.mContext = mContext;
        this.mData = data;
        this.mViewHolders = new HashMap<>();
    }


//---------------------------对继承函数的重写--------------------------------

    /**
     * 将viewType返回,具体的实现由开发者去做。
     *
     * @param parent
     * @return
     */

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        BaseViewHolder holder;
        if (view == null) {
            holder = OnCreatViewHolder(parent, getItemViewType(position));
            view = holder.view;
            view.setTag(holder);
        } else {
            holder = (BaseViewHolder) view.getTag();
            if (holder.mType != getItemViewType(position)) {
                holder = OnCreatViewHolder(parent, getItemViewType(position));
                view = holder.view;
                view.setTag(holder);
            }
        }

        if (parent.getChildCount() == position) {//正常的用于界面展示的View对象
            onBindViewHolder(holder, position);
            mViewHolders.put(position, holder);
        }
        return holder.view;
    }

    public abstract BaseViewHolder OnCreatViewHolder(ViewGroup parent, int viewType);

    /**
     * 默认框架实现点击事件接口的回调
     *
     * @param holder
     * @param position
     */

    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        OnBindViewHloder(holder, position);
        //设置监听接口
        if (onItemClickListener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(holder, position);
                }
            });
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(holder, position);
                    return true;
                }
            });
        }
    }

    public abstract void OnBindViewHloder(BaseViewHolder holder, int position);

// ----------------------------对数据源的操作--------------------------------

    /**
     * 将数据添加到集合最末尾
     *
     * @param data
     */
    public void add(M data) {
        add(data, mData.size());
    }

    /**
     * 将数据添加到指定位置
     *
     * @param data
     * @param position
     */
    public void add(M data, int position) {
        if (data != null) {
            synchronized (mLock) {
                mData.add(position, data);
            }
        }
    }

    /**
     * 将一个集合添加到数据最尾端
     *
     * @param collection
     */
    public void addAll(Collection<? extends M> collection) {
        addAll(collection, mData.size());
    }

    public void addAll(Collection<? extends M> collection, int position) {
        if (collection != null && collection.size() != 0 && position >= 0) {
            synchronized (mLock) {
                mData.addAll(position, collection);
            }
        }
    }

    /**
     * 删除集合最尾部数据
     */
    public void remove() {
        synchronized (mLock) {
            remove(mData.size() - 1);
        }
    }

    /**
     * 删除指定位置的数据
     *
     * @param position
     */
    public void remove(int position) {
        if (position >= 0 && position <= (mData.size() - 1)) {
            synchronized (mLock) {
                mData.remove(position);
            }
        }
    }

    /**
     * 删除一个特定数据的元素
     *
     * @param data
     */
    public void remove(M data) {
        if (data != null) {
            synchronized (mLock) {
                mData.remove(data);
            }
        }
    }

    /**
     * 清空数据
     */
    public void clear() {
        synchronized (mLock) {
            mData.clear();
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public M getItem(int position) {
        return mData.get(position);
    }

    public BaseViewHolder getViewHolder(int position) {
        return mViewHolders.get(position);
    }

}
