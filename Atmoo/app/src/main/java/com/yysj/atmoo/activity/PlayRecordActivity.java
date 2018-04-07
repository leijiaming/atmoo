package com.yysj.atmoo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yysj.atmoo.R;
import com.yysj.atmoo.adapter.ChannelContentAdapter;
import com.yysj.atmoo.adapter.PlayRecordAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by asus on 2018/3/31.
 */

public class PlayRecordActivity extends AppCompatActivity{


    @BindView(R.id.playlist)
    RecyclerView playlist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playrecord);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        playlist.setLayoutManager(layoutManager);
        playlist.setHasFixedSize(true);
        playlist.setItemAnimator(new DefaultItemAnimator());
        PlayRecordAdapter playRecordAdapter = new PlayRecordAdapter(this);
        playlist.setAdapter(playRecordAdapter);
    }
}
