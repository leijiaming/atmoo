package com.yysj.atmoo.bean;

import java.util.List;

/**
 *
 * Created by asus on 2018/4/7.
 */

public class VideoDetail {
    public String errmsg;
    public int errno;
    public Data data;
    public static class Data{
            //自定义字段
            public int isLike =2;
            public int isSubscribe =2;
            public String shareUrl;
            public PlayInfo playInfo;
            public static class PlayInfo {
                public Wifi wifi;

                public static class Wifi {
                    public String url;
                    public String bitrate;
                    public String formate;
                    public String size;
                    public String ratio;
                    public String expire;
                    public String uri;

                }
            }
            public CoverInfo coverInfo;
            public static class CoverInfo{
                public String ratio;
                public String cover;
            }
            public String id;
            public String description;
            public String title;
            public String duration;
        public Counts counts;
        public static class Counts{
            public String playCnt;
            public String commentCnt;
            public String collectCnt;
        }
    }
}
