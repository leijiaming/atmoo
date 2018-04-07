package com.yysj.atmoo.core;

public class MainFactory {
    /**
     * 数据主机地址
     */
    public static final String HOST = "http://www.atmoo.net:8080";

    private static Api mApi;

    protected static final Object monitor = new Object();

    public static Api getApiInstance(){
        synchronized (monitor){
            if(mApi==null){
                mApi = new MainRetrofit().getService();
            }
            return mApi;
        }
    }
}
