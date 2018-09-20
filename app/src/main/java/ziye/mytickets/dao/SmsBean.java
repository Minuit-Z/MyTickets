package ziye.mytickets.dao;

/**
 * Created by Administrator on 2018/9/20 0020.
 */

public class SmsBean {
    /**
     * 短信发送方
     */
    public String address;
    /**
     * 号码在通讯录中的姓名：无为null
     */
    public String name;
    /**
     * 短信时间
     */
    public String date;
    /**
     * 短信内容
     */
    public String body;
    /**
     * 1 接收短信 2 发送短信
     */
    public int type;
    /**
     * 同一个手机号互发的短信，其序号是相同的
     */
    public int thread_id;

}
