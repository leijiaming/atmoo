package com.yysj.atmoo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yysj.atmoo.R;
import com.yysj.atmoo.utils.SPUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * Created by asus on 2018/3/31.
 */

public class SettingActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.logout)
    public void click(){
        SPUtils.clear(this);
        Intent intent = new Intent(this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
