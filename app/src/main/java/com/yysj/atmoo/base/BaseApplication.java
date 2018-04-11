package com.yysj.atmoo.base;

import android.app.Application;
import android.content.Context;

import com.bulong.rudeness.RudenessScreenHelper;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 *
 * Created by asus on 2018/3/24.
 */

public class BaseApplication extends Application {
    public static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        //设计图标注的宽度
        int designWidth = 750;
        new RudenessScreenHelper(this, designWidth).activate();
        //PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //微信 appid appsecret
        //PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        //新浪微博 appkey appsecret
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        // QQ和Qzone appid appkey
        //PlatformConfig.setAlipay("2015111700822536");
        //支付宝 appid
        UMShareAPI.get(this);
        sContext = this;
    }

}
