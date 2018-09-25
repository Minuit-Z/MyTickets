package ziye.mytickets.mainAty;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.PermissionRequest;

import com.zjmy.mvp.presenter.ActivityPresenter;

import java.util.List;

import ziye.mytickets.dao.SmsBean;

public class MainActivity extends ActivityPresenter<MainView, MainModel> {


    @Override
    public int setStatusColor() {
        return -1;
    }

    @Override
    public Class<MainView> getRootViewClass() {
        return MainView.class;
    }

    @Override
    public Class<MainModel> getRootModelClass() {
        return MainModel.class;
    }

    @Override
    public void inCreate(Bundle savedInstanceState) {

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_SMS
        },100);

        getModelRef().getSmsData(this);
    }

    @Override
    public void inDestroy() {

    }

    @Override
    public FragmentActivity getContext() {
        return this;
    }

    @Override
    public <T> void onSuccess(T result) {
        super.onSuccess(result);
        List<SmsBean> s = (List<SmsBean>) result;
        Log.e("onSuccess: ", "sss");

    }
}
