package com.zjmy.mvp.presenter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zjmy.mvp.model.IListener;
import com.zjmy.mvp.model.IModel;
import com.zjmy.mvp.view.IView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

public abstract class AppCompatActivityPresenter<V extends IView, M extends IModel> extends AppCompatActivity implements IListener {

    private static final String TAG = "ActivityPresenter";

    private Reference<V> mViewRef;//弱引用解决内存泄露问题

    private V v;//View 层
    private M m;//Model 层

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mViewRef = new WeakReference<>(getRootViewClass().newInstance());

            v = getViewRef();
            m = getRootModelClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

//        AppManager.getAppManager().addActivity(this);

        v.createView(getLayoutInflater(), null, savedInstanceState);
        v.setActivityContext(getContext());

        setBarStyle();
        setContentView(v.getRootView());

        v.initView();
        m.setListener(this);
        inCreate(savedInstanceState);
    }

    public abstract void setBarStyle();

    //解除绑定
    @Override
    protected void onDestroy() {
        inDestroy();

//        AppManager.getAppManager().finishActivity(this);

        try {
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
            Log.e(TAG, "onDestroy not effective!");
        }
        super.onDestroy();
    }

    private boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     * 获取viwe的引用,若view 已被回收，则抛出异常
     * @return
     */
    public V getViewRef() throws RuntimeException{
        if (isViewAttached()) {
            return mViewRef.get();
        }else{
            throw new RuntimeException("rootview's id can't be null");
        }
    }

    public M getModelRef() throws RuntimeException{
        if (m != null) {
            return m;
        }else{
            throw new RuntimeException("rootmodel's id can't be null");
        }
    }

    public abstract Class<V> getRootViewClass();

    public abstract Class<M> getRootModelClass();

    public abstract void inCreate(Bundle savedInstanceState);

    public abstract void inDestroy();

    public abstract FragmentActivity getContext();

    public void onError(Throwable e){
        if (e == null || !isViewAttached()){
            return;
        }
    }

    public <T> void onSuccess(T result){
        if (!isViewAttached()){
            return;
        }
    }

    public <T> void onSuccess(int indexPage, int pageSize, List<T> result){
        if (result == null || !isViewAttached()){
            return;
        }
    }


}
