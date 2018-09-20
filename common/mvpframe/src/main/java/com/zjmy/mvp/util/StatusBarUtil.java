package com.zjmy.mvp.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class StatusBarUtil {

    private static final String TAG = "StatusBarUtil";

    public static int sStatusBarHeight = 0;
    private static StatusBarUtil mStatusBarUtil;
    private StatusBarUtil(){

    }

    public static StatusBarUtil instance(){
        if(mStatusBarUtil == null){
            mStatusBarUtil = new StatusBarUtil();
        }
        return mStatusBarUtil;
    }

    public int getStatusBarHeight(Activity activity){
        if(sStatusBarHeight == 0){
            //获取状态栏高度的资源id
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                sStatusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return sStatusBarHeight;
    }

    public void setStatusBar(Activity activity, int color){
        int height = getStatusBarHeight(activity);
        if(color == -1){
            return; //color为-1时，全屏显示, 布局顶到状态栏顶部，状态栏不隐藏
        }else if(color == -2){//color为-2时，全屏显示
            View decorView = activity.getWindow().getDecorView();
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiFlags |= 0x00001000;
            decorView.setSystemUiVisibility(uiFlags);
            if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.P){
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                activity.getWindow().setAttributes(lp);
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.0 以上直接设置状态栏颜色
                activity.getWindow().setStatusBarColor(color);
            } else {
                //根布局添加占位状态栏
                ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
                View statusBarView = new View(activity);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                statusBarView.setBackgroundColor(color);
                decorView.addView(statusBarView, lp);
            }
        }
    }

    public void setStatusBarTxtDark(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){//设置字体为深色
            activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
    
}
