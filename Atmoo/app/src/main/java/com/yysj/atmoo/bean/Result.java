package com.yysj.atmoo.bean;

/**
 *
 * Created by asus on 2018/4/7.
 */

public class Result {
    public int errno;
    public String errmsg;
    public Data data;
    public static class Data{
        public String message;
        public int code;
        public String userId;
        public String phone;
    }
}
