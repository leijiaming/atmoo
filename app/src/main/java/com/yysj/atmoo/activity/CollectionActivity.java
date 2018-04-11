package com.yysj.atmoo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.yysj.atmoo.R;
import com.yysj.atmoo.adapter.ChannelContentAdapter;
import com.yysj.atmoo.adapter.PlayRecordAdapter;
import com.yysj.atmoo.bean.HomeList;
import com.yysj.atmoo.core.MainFactory;
import com.yysj.atmoo.utils.SPUtils;
import com.yysj.atmoo.utils.ToastUtils;
import com.yysj.atmoo.widget.LoadingCustom;

import java.util.List;

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

public class CollectionActivity extends AppCompatActivity{


    @BindView(R.id.collectionlist)
    RecyclerView collectionlist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        collectionlist.setLayoutManager(layoutManager);
        collectionlist.setHasFixedSize(true);
        collectionlist.setItemAnimator(new DefaultItemAnimator());
        findCollectList();
    }

    private void findCollectList() {
        String userId = (String) SPUtils.get(this,"userId","");
        //去服务器下载数据
        LoadingCustom.showprogress(this,"获取视频中..",true);
        Observable<HomeList> observable = MainFactory.getApiInstance().atmooUserCollectlistData(userId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HomeList>() {

            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {
                LoadingCustom.dismissprogress();
                ToastUtils.showShort("服务器开小差了...");
            }

            @Override
            public void onNext(HomeList homeList) {
                Gson gson = new Gson();
                Log.i("GSON",gson.toJson(homeList));
                LoadingCustom.dismissprogress();
                int errno = homeList.errno;
                String errmsg = homeList.errmsg;
                if(errno == 1){
                    HomeList.Data data = homeList.data;
                    if(data!=null){
                        List<HomeList.Data.VideoList> videoList = data.videoList;
                        if(videoList !=null && !videoList.isEmpty()){
                            PlayRecordAdapter playRecordAdapter = new PlayRecordAdapter(CollectionActivity.this);
                            playRecordAdapter.setData(videoList);
                            collectionlist.setAdapter(playRecordAdapter);
                        }
                    }
                }
                ToastUtils.showShort(errmsg);
            }
        });
    }
    @OnClick(R.id.back)
    public void action(){
        finish();
    }
}
