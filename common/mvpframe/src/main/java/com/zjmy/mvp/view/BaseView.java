package com.zjmy.mvp.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 将view加载的过程写在抽象类，做到代码复用。
 * View delegate base class
 * 视图层代理的基类
 */
public abstract class BaseView implements IView{

    protected View rootView;
    protected SparseArray<View> mViews = new SparseArray<>();

    protected Activity mActivity;

    private Unbinder binder;

    public void createView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        int resourceId = getRootViewId();
        if (resourceId == 0) {
            throw new RuntimeException("rootview's id can't be null");
        }
        rootView = inflater.inflate(resourceId, parent, false);
    }

    public final View getRootView() {
        return rootView;
    }

    public void initView() {
        binder = ButterKnife.bind(this, rootView);
    }

    public final void removeView() {
        if (binder != null) {
            binder.unbind();
        }
        if (mViews != null) {
            mViews.clear();
        }
        mViews = null;
        rootView = null;
    }

    private final <T extends View> T bindView(int id) {
        T view2 = (T) mViews.get(id);
        if (view2 == null) {
            view2 = (T) rootView.findViewById(id);
            mViews.put(id, view2);
        }
        return view2;
    }

    @Override
    public <T> void notifyDataSetChanged(int currentPage, int pageCount, List<T> result) {

    }

    @Override
    public <T> void notifyDataSetChanged(T result) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void notifyError(Throwable e) {

    }

    public abstract int getRootViewId();

    @Override
    public void setActivityContext(FragmentActivity activity) {
        mActivity = activity;
    }

    public <T extends Activity> T getActivity() {
        return (T) rootView.getContext();
    }

    public final <T extends View> T get(int id) {
        return (T) bindView(id);
    }
}
