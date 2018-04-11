package com.yysj.atmoo.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yysj.atmoo.R;
import com.yysj.atmoo.adapter.ChannelAdapter;
import com.yysj.atmoo.bean.Channel;
import com.yysj.atmoo.core.MainFactory;
import com.yysj.atmoo.fragment.ChannelFragment;
import com.yysj.atmoo.utils.IntentUtils;
import com.yysj.atmoo.utils.ToastUtils;
import com.yysj.atmoo.widget.LoadingCustom;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.channel)
    TabLayout channel;
    @BindView(R.id.channelpage)
    ViewPager channelpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind( this );
        initChannel();
        channel.setupWithViewPager(channelpage);
    }

    private void initChannel() {
        LoadingCustom.showprogress(this,"获取频道中..",true);
        Observable<Channel> observable = MainFactory.getApiInstance().getChannelData();
        observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Channel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoadingCustom.dismissprogress();
                ToastUtils.showShort("服务器开小差了...");
            }

            @Override
            public void onNext(Channel chann) {
                LoadingCustom.dismissprogress();
                int errno = chann.errno;
                String errmsg = chann.errmsg;
                if(errno == 1){
                    Channel.Data data = chann.data;
                    if(data!=null){
                        List<Channel.Data.ChannelList> channelList = data.channelList;
                        int selectedIndex = data.selectedIndex;
                        if(channelList!=null && !channelList.isEmpty()){
                            channelpage.setOffscreenPageLimit(channelList.size());
                            List<Fragment> fragments = new ArrayList<>();
                            List<String> titles = new ArrayList<>();
                            for (int i = 0;i<channelList.size();++i){
                                fragments.add(new ChannelFragment(channelList.get(i).id,i,selectedIndex));
                                titles.add(channelList.get(i).title);
                            }
                            ChannelAdapter channelAdapter = new ChannelAdapter(getSupportFragmentManager());
                            channelAdapter.setFragments(fragments,titles);
                            channelpage.setAdapter(channelAdapter);
                            channel.getTabAt(selectedIndex).select();
                        }
                    }
                }
                ToastUtils.showShort(errmsg);
            }
        });
    }
    @OnClick(R.id.user)
    public void click(){
        IntentUtils.startActivity(this,AccountActivity.class,null);
    }
}
