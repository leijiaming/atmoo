package com.yysj.atmoo.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.yysj.atmoo.R;


public class LoadingCustom extends Dialog {
    private static LoadingCustom mLoadingProgress;

    public LoadingCustom(Context context) {
        super(context);

    }

    public LoadingCustom(Context context, int theme) {
        super(context, theme);
    }

    public static void showprogress(Context context,CharSequence message,boolean iscanCancel){
        mLoadingProgress=new LoadingCustom(context,R.style.loading_dialog);
                mLoadingProgress.setCanceledOnTouchOutside(false);

        mLoadingProgress.setTitle("");
        mLoadingProgress.setContentView(R.layout.loading_layout);
        mLoadingProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if(message==null|| TextUtils.isEmpty(message)){
            mLoadingProgress.findViewById(R.id.loading_tv).setVisibility(View.GONE);
        }else {
            TextView tv = (TextView) mLoadingProgress.findViewById(R.id.loading_tv);
            tv.setText(message);
        }
        //按返回键响应是否取消等待框的显示
        mLoadingProgress.setCancelable(iscanCancel);

        mLoadingProgress.show();

    }

    public static void dismissprogress(){
        if(mLoadingProgress!=null){

            mLoadingProgress.dismiss();
        }
    }
}
