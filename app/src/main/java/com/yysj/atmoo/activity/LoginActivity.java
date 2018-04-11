package com.yysj.atmoo.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yysj.atmoo.R;
import com.yysj.atmoo.bean.MessageEvent;
import com.yysj.atmoo.bean.Result;
import com.yysj.atmoo.core.MainFactory;
import com.yysj.atmoo.utils.MD5Utils;
import com.yysj.atmoo.utils.SPUtils;
import com.yysj.atmoo.utils.ToastUtils;
import com.yysj.atmoo.widget.LoadingCustom;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by asus on 2018/3/31.
 */

public class LoginActivity extends AppCompatActivity{
    //消息的处理：
    private static final int MSG_CODE = 0;
    private int limitTime = 31;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==MSG_CODE){
                limitTime--;
                if(limitTime>0){
                    mHandler.sendEmptyMessageDelayed(MSG_CODE,1000);
                    getcode.setText(limitTime+"s");

                }else{
                    getcode.setText("获取验证码");
                    getcode.setClickable(true);
                }
                return true;
            }
            return false;
        }
    });
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.getcode)
    TextView getcode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#ffffff"));
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }
    @OnClick({R.id.getcode,R.id.login})
    public void click(View v){
        switch (v.getId()){
            case R.id.getcode:
                String etPhone = phone.getText().toString().trim();
                if(etPhone.length() !=11){
                    ToastUtils.showShort("非法手机号");
                    return;
                }
                getcode.setClickable(false);
                mHandler.obtainMessage();
                mHandler.sendEmptyMessage(MSG_CODE);
                getCode(etPhone);
                break;
            case R.id.login:
                String etLPhone = phone.getText().toString().trim();
                if(etLPhone.length() !=11){
                    ToastUtils.showShort("非法手机号");
                    return;
                }
                String etLCode = code.getText().toString().trim();
                if(etLCode.length() !=6){
                    ToastUtils.showShort("非法验证码");
                    return;
                }
                goLogin(etLPhone,etLCode);
                break;
        }
    }

    private void goLogin(String etLPhone, String etLCode) {
        LoadingCustom.showprogress(this,"获取验证码中...",true);
        //获取验证码
        Observable<Result> observable = MainFactory.getApiInstance().atmooUserLoginData(etLPhone, MD5Utils.encode(etLCode),"1");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoadingCustom.dismissprogress();
                ToastUtils.showShort("服务器开小差了...");
            }

            @Override
            public void onNext(Result result) {
                LoadingCustom.dismissprogress();
                int errno = result.errno;
                String errmsg = result.errmsg;
                if(errno == 1){
                    int code = result.data.code;
                    String message = result.data.message;
                    if(code==1){
                        String userId = result.data.userId;
                        SPUtils.put(getApplicationContext(),"userId",userId);
                        String phone = result.data.phone;
                        SPUtils.put(getApplicationContext(),"phone",phone);
                        finish();
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_L_A_P,null));
                    }
                    ToastUtils.showShort(message);
                }
                ToastUtils.showShort(errmsg);
            }
        });
    }

    private void getCode(String etPhone) {
        LoadingCustom.showprogress(this,"获取验证码中...",true);
        //获取验证码
        Observable<Result> observable = MainFactory.getApiInstance().atmooUserLoginCodeData(etPhone);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoadingCustom.dismissprogress();
                getcode.setClickable(true);
                getcode.setText("获取验证码");
                ToastUtils.showShort("服务器开小差了...");
            }

            @Override
            public void onNext(Result result) {
                LoadingCustom.dismissprogress();
                int errno = result.errno;
                String errmsg = result.errmsg;
                if(errno == 1){
                    int code = result.data.code;
                    String message = result.data.message;
                    if(code!=1){
                        getcode.setClickable(true);
                        getcode.setText("获取验证码");
                    }
                    ToastUtils.showShort(message);
                }else{
                    getcode.setClickable(true);
                    getcode.setText("获取验证码");
                }
                ToastUtils.showShort(errmsg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
