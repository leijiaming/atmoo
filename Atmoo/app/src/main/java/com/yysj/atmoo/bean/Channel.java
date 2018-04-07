package com.yysj.atmoo.bean;

import com.yysj.atmoo.utils.ToastUtils;

import java.util.List;

/**
 *
 * Created by asus on 2018/4/7.
 */

public class Channel {
    public int errno;
    public String errmsg;
    public Data data;
    public static class Data{
        public List<ChannelList> channelList;
        public int selectedIndex;
        public static class ChannelList{
            public String id;
            public String title;
        }
    }


}
