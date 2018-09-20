package com.zjmy.mvp.model;

import java.util.List;

/**
 * 用作双向绑定使用
 * Do Model-View two-way binding future use
 */
public interface IModel {

    void removeListener();

    void setListener(IListener listener);

    void notifyError(Throwable msg);

    <T> void notifySuccess(T result);

    <T> void notifySuccess(int currentPage, int pageCount, List<T> result);

    /**
     * 在presenter销毁的时候调用,生命周期同步一下,有时候需要在view释放什么
     */
    void onPresenterDestory();

}
