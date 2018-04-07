package com.yysj.atmoo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yysj.atmoo.R;
import com.yysj.atmoo.adapter.PlayRecordAdapter;
import com.yysj.atmoo.bean.MessageEvent;
import com.yysj.atmoo.utils.IntentUtils;
import com.yysj.atmoo.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 *
 * Created by asus on 2018/3/24.
 */

public class AccountActivity extends AppCompatActivity{
    @BindView(R.id.set)
    TextView set;
    @BindView(R.id.account)
    TextView account;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
    }


    @OnClick({R.id.set,R.id.history,R.id.collect,R.id.account})
    public void click(View v){
        switch (v.getId()){
            case R.id.set:
                IntentUtils.startActivity(this,SettingActivity.class,null);
                break;
            case R.id.history:
                IntentUtils.startActivity(this,PlayRecordActivity.class,null);
                break;
            case R.id.collect:
                IntentUtils.startActivity(this,CollectionActivity.class,null);
                break;
            case R.id.account:
                IntentUtils.startActivity(this,LoginActivity.class,null);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processMsg(MessageEvent messageEvent) {
        if(messageEvent.type.equals(MessageEvent.MSG_L_A_P)){
            initData();
        }
    }

    private void initData() {
        String phone = (String) SPUtils.get(this,"phone","");
        String userId = (String) SPUtils.get(this,"userId","");
        if(TextUtils.isEmpty(userId)){
            account.setClickable(true);
            account.setText("点击登录");
        }else{
            account.setClickable(false);
            account.setText(phone);
        }
    }
}
