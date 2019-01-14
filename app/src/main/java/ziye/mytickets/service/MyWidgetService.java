package ziye.mytickets.service;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ziye.mytickets.R;
import ziye.mytickets.dao.SmsBean;
import ziye.mytickets.util.MUtils;

public class MyWidgetService extends RemoteViewsService {

    private static final boolean DB = true;
    private static final String TAG = "MyWidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        log("onGetViewFactory, intent=" + intent);
        MyWidgetFactory factory = new MyWidgetFactory(getApplicationContext(), intent);
        return factory;
    }

    public static class MyWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private ContentResolver resolver;
        private ArrayList<SmsBean> mData;
        Uri uri = Uri.parse("content://sms");
        //[3]查询表，获得sms表游标结果集
        String[] projection = {"address", "date", "body", "type", "person", "thread_id"};
        //访问表的字段
        Cursor cursor;

        // 构造
        public MyWidgetFactory(Context context, Intent intent) {
            log("MyWidgetFactory");
            mContext = context;
            resolver = context.getContentResolver();
            cursor = resolver.query(uri, projection, null, null, null);
            intData();
        }

        private void intData() {
            List<SmsBean> smsBeans = new ArrayList<>();
            //[1.]获得ContentResolver对象
//            ContentResolver resolver = context.getContentResolver();
            //[2.1]得到Uri :访问raw_contacts的url
            while (cursor.moveToNext()) {
                //遍历游标，获取数据，储存在bean中
                SmsBean smsBean = new SmsBean();
                smsBean.address = cursor.getString(0);
                if (!smsBean.address.contains("12306")) continue;
                smsBean.date = cursor.getString(1);
                smsBean.body = cursor.getString(2);
                smsBean.type = cursor.getInt(cursor.getColumnIndex("type"));
                smsBean.name = cursor.getString(cursor.getColumnIndex("person"));
                smsBean.thread_id = cursor.getInt(cursor.getColumnIndex("thread_id"));
                smsBeans.add(smsBean);
            }
            mData = new ArrayList<>(smsBeans);
            Log.e(TAG, "intData: " + mData.size());
        }

        @Override
        public int getCount() {
            log("getCount" + mData.size());
            return mData.size();
        }

        @Override
        public long getItemId(int position) {
            log("getItemId");
            return position;
        }

        // 在调用getViewAt的过程中，显示一个LoadingView。
        // 如果return null，那么将会有一个默认的loadingView
        @Override
        public RemoteViews getLoadingView() {
            log("getLoadingView");
            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            log("getViewAt, position=" + position);
            if (position < 0 || position >= getCount()) {
                return null;
            }
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_sms);
            String body = mData.get(position).body;
            Pattern passNumber = Pattern.compile("E.*?,"); //订单号
            Pattern username = Pattern.compile(",.*?您");//订单人
            Pattern date = Pattern.compile("(\\d{1,2})月(\\d{1,2})日");//乘车日期
            Pattern trainSeat = Pattern.compile("([a-zA-Z0-9]*)次(\\d*)车(\\d*)号");//座位号

            String[] split = body.split(",");
            String clock = split[1];

            Pattern startPlace = Pattern.compile(".*站");//xx站发车
            Pattern startTime = Pattern.compile("[:\\d{1,2}]*开");//xx站发车时间

            Log.e(TAG, "getViewAt: " + MUtils.getRexString(clock, startPlace));
//            views.setTextViewText(R.id.tv_content, mData.get(position).body);
            views.setTextViewText(R.id.tv_train, MUtils.getRexString(body, trainSeat));
            views.setTextViewText(R.id.tv_start, MUtils.getRexString(clock, startPlace));
            views.setTextViewText(R.id.tv_tickets_number, MUtils.getRexString(body, passNumber).replace(",",""));
            views.setTextViewText(R.id.tv_start_time,
                    MUtils.getRexString(body,date)+"  "+
                    MUtils.getRexString(clock, startTime));
            return views;
        }

        @Override
        public int getViewTypeCount() {
            log("getViewTypeCount");
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            log("hasStableIds");
            return true;
        }

        @Override
        public void onCreate() {
            log("onCreate");
        }

        @Override
        public void onDataSetChanged() {

        }


        @Override
        public void onDestroy() {
            log("onDestroy");
            mData.clear();
        }
    }

    private static void log(String log) {
        if (DB)
            Log.d(TAG, log);
    }
}
