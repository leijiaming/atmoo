package com.yysj.atmoo.bean;

/**
 *
 * Created by asus on 2018/4/7.
 */

public class MessageEvent{
    public static final String MSG_L_A_P ="MSG_L_A_P";
    public Object message;
    public String type;
    public  MessageEvent(String type,Object message){
        this.message=message;
        this.type  =type;
    }

}

