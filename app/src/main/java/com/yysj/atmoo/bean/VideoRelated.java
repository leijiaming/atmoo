package com.yysj.atmoo.bean;

import java.util.List;

/**
 *
 * Created by asus on 2018/4/7.
 */

public class VideoRelated {
    public String errmsg;
    public int errno;
    public Data data;
    public static class Data{
        public List<VideoList> videoList;
        public static class VideoList{
            //自定义字段
            public int isLike =2;
            public int isSubscribe =2;
            public String shareUrl;
            public CoverInfo coverInfo;
            public static class CoverInfo{
                public String ratio;
                public String cover;
            }
            public String id;
            public String description;
            public String title;
            public String duration;
        }
    }
}
