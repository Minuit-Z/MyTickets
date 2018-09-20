package com.zjmy.mvp.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjmy.mvp.model.BaseModel;
import com.zjmy.mvp.model.IListener;
import com.zjmy.mvp.view.BaseView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * onAttach -> onCreate -> onCreateView ->onViewCreated -> onActivityCreated-> onViewStateRestored -> onStart -> onResume
 *
 * @param <T>
 */
public abstract class FragmentPresenter<T extends BaseView, M extends BaseModel> extends Fragment implements IListener {
    private Reference<T> mViewRef;//弱引用解决内存泄露问题

    private T v;//View 层
    private M m;//Model 层

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mViewRef = new WeakReference<>(getRootViewClass().newInstance());
            v = getViewRef();
            m = getRootModelClass().newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        v.setActivityContext(getActivity());
        m.setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        v.createView(inflater, container, savedInstanceState);
        return v.getRootView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v.initView();
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            if (mViewRef != null) {
                mViewRef.clear();
                mViewRef = null;
            }

            if (v != null) {
                v.removeView();
                v.onPresenterDestroy();
                v = null;
            }

            if (m != null) {
                m.onPresenterDestory();
                m.removeListener();
                m = null;
            }
        } catch (Exception e) {
            Log.e("test", "onDestroy not effective!");
        }
    }

    private boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     * 获取viwe的引用,若view 已被回收，则抛出异常
     * @return
     */
    public T getViewRef() throws RuntimeException {
        if (isViewAttached()) {
            return mViewRef.get();
        }else{
            throw new RuntimeException("rootview can't be null");
        }
    }

    public M getModelRef() throws RuntimeException {
        if (m != null) {
            return m;
        }else{
            throw new RuntimeException("rootmodel's id can't be null");
        }
    }

    public abstract Class<T> getRootViewClass();

    public abstract Class<M> getRootModelClass();

    public void onError(Throwable e){
        if (e == null || !isViewAttached()){
            return;
        }
    }

    public  <T> void onSuccess(T result){
        if (!isViewAttached()){
            return;
        }
    }

    public  <T> void onSuccess(int indexPage, int pageSize, List<T> result){
        if (result == null || !isViewAttached()){
            return;
        }
    }

}
