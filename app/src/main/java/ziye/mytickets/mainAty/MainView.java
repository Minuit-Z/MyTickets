package ziye.mytickets.mainAty;

import com.zjmy.mvp.view.BaseView;

import ziye.mytickets.R;

/**
 * Created by Administrator on 2018/9/20 0020.
 */

public class MainView extends BaseView{
    @Override
    public void onPresenterDestroy() {

    }

    @Override
    public int getRootViewId() {
        return R.layout.activity_main;
    }
}
