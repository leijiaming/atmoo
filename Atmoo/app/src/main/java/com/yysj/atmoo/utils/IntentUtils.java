package com.yysj.atmoo.utils;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by asus on 2018/3/24.
 */

public class IntentUtils {
    public static void startActivity(Context context,Class clazz,HashMap<String,Object> param){
        Intent intent = new Intent(context,clazz);
        if(param !=null)
            intent.putExtra("param",param);
        context.startActivity(intent);
    }
}
