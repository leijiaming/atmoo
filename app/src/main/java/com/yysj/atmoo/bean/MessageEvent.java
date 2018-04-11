package com.yysj.atmoo.bean;

/**
 *
 * Created by asus on 2018/4/7.
 */

public class MessageEvent{
    public static final String MSG_L_A_P ="MSG_L_A_P";
    public static final String MSG_V_M_C ="MSG_V_M_C";
    public static final String MSG_V_M_L ="MSG_V_M_L";
    public Object message;
    public String type;
    public  MessageEvent(String type,Object message){
        this.message=message;
        this.type  =type;
    }

}

