package com.zjmy.mvp.model;

import java.util.List;

public class BaseModel implements IModel {

    private IListener listener = null;
    protected Object lock = new Object();

    @Override
    public void setListener(IListener listener) {
        synchronized (lock) {
            this.listener = listener;
        }
    }

    @Override
    public void removeListener() {
        synchronized (lock) {
            this.listener = null;
        }
    }

    @Override
    public void notifyError(Throwable msg) {
        synchronized (lock) {
            if (listener != null) {
                listener.onError(msg);
            }
        }
    }

    @Override
    public <T> void notifySuccess(T result) {
        synchronized (lock) {
            if (listener != null) {
                listener.onSuccess(result);
            }
        }
    }

    @Override
    public <T> void notifySuccess(int currentPage, int pageCount, List<T> result) {
        synchronized (lock) {
            if (listener != null) {
                listener.onSuccess(currentPage, pageCount, result);
            }
        }
    }

    @Override
    public void onPresenterDestory() {
        removeListener();
    }

}
