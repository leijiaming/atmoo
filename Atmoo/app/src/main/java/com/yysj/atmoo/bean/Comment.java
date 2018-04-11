package com.yysj.atmoo.bean;

import java.util.List;

/**
 *
 * Created by asus on 2018/4/7.
 */

public class Comment {
    public int errno;
    public String errmsg;
    public List<Data> data;
    public static class Data{
        public String thumbsUpCnt;
        public String createTime;
        public String userCommentStatus;
        public String content;
        public String commentUserName;
        public String thumbsDownCnt;
        public String id;
    }
}
