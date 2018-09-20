package ziye.mytickets.mainAty;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.zjmy.mvp.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

import ziye.mytickets.dao.SmsBean;

/**
 * Created by Administrator on 2018/9/20 0020.
 */

public class MainModel extends BaseModel {

    public void getSmsData(Context context) {
        List<SmsBean> smsBeans = new ArrayList<>();
        //[1.]获得ContentResolver对象
        ContentResolver resolver = context.getContentResolver();
        //[2.1]得到Uri :访问raw_contacts的url
        Uri uri = Uri.parse("content://sms");
        //[3]查询表，获得sms表游标结果集
        String[] projection = {"address", "date", "body", "type", "person", "thread_id"};

        //访问表的字段
        Cursor cursor = resolver.query(uri, projection, null, null, null);
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
        cursor.close();
        notifySuccess(smsBeans);
    }
}
