package ziye.mytickets.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ziye.mytickets.R;
import ziye.mytickets.dao.SmsBean;

/**
 * Created by Administrator on 2018/9/25 0025.
 */

public class SmsAdapter extends BaseAdapter {

    private ArrayList<SmsBean> lists;
    private Context context;
    public SmsAdapter(ArrayList<SmsBean> lists,Context context) {
        this.lists=lists;
        this.context=context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SmsBean smsBean = lists.get(position);

        View v=View.inflate(context, R.layout.item_sms,parent);
        TextView tvContent=v.findViewById(R.id.tv_content);
        tvContent.setText(smsBean.body);
        return v;
    }
}
