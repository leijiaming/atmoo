package com.yysj.atmoo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yysj.atmoo.R;
import com.yysj.atmoo.adapter.ChannelContentAdapter;
import com.yysj.atmoo.bean.HomeList;
import com.yysj.atmoo.bean.MessageEvent;
import com.yysj.atmoo.core.MainFactory;
import com.yysj.atmoo.utils.SPUtils;
import com.yysj.atmoo.utils.ToastUtils;
import com.yysj.atmoo.widget.LoadingCustom;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by asus on 2018/3/24.
 */

@SuppressLint("ValidFragment")
public class ChannelFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private String channelId;
    private int flag;
    private int selectedIndex;
    private List<HomeList.Data.VideoList> videoList;
    private ChannelContentAdapter channelContentAdapter;
    private int pageNum = 0;
    private int pageSize = 10;
    public ChannelFragment(){

    }
    public ChannelFragment(String channelId,int flag,int selectedIndex){
        this.channelId = channelId;
        this.flag = flag;
        this.selectedIndex = selectedIndex;
        EventBus.getDefault().register(this);
    }
    @BindView(R.id.content)
    RecyclerView content;
    private boolean isFirstVisible = true;
    private View rootView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel,container,false);
        ButterKnife.bind( this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (rootView == null) {
            rootView = view;
            if (getUserVisibleHint()) {
                if (isFirstVisible) {
                    onFragmentFirstVisible();
                    isFirstVisible = false;
                }
            }
        }
        videoList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        content.setLayoutManager(layoutManager);
        content.setHasFixedSize(true);
        content.setItemAnimator(new DefaultItemAnimator());
        refreshLayout.setOnRefreshListener(this);

    }

    private void onFragmentFirstVisible() {
        //去服务器下载数据
        if(flag == selectedIndex){
            LoadingCustom.showprogress(getActivity(),"获取视频中..",true);
        }
        String userId = (String) SPUtils.get(getActivity(),"userId","");
        Observable<HomeList> observable = MainFactory.getApiInstance().atmooHomeListData(channelId,userId,pageNum,pageSize);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HomeList>() {

            @Override
            public void onCompleted() {
                    refreshLayout.setRefreshing(false);
            }
            @Override
            public void onError(Throwable e) {
                if(flag == selectedIndex) {
                    LoadingCustom.dismissprogress();
                }
                ToastUtils.showShort("服务器开小差了...");
            }

            @Override
            public void onNext(HomeList homeList) {
                if(flag == selectedIndex) {
                    LoadingCustom.dismissprogress();
                }
                int errno = homeList.errno;
                String errmsg = homeList.errmsg;
                if(errno == 1){
                    HomeList.Data data = homeList.data;
                    if(data!=null){
                        if(data.videoList !=null && !data.videoList.isEmpty()){
                            videoList.addAll(data.videoList);
                            channelContentAdapter = new ChannelContentAdapter(getActivity(),flag);
                            channelContentAdapter.setData(videoList);
                            content.setAdapter(channelContentAdapter);
                        }
                    }
                }
                ToastUtils.showShort(errmsg);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //setUserVisibleHint()有可能在fragment的生命周期外被调用
        if (rootView == null) {
            return;
        }
        if (isFirstVisible && isVisibleToUser) {
            onFragmentFirstVisible();
            isFirstVisible = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processMsg(MessageEvent messageEvent) {
        String message = (String) messageEvent.message;
        int flag = Integer.parseInt(message.split(",")[2]);
        if(this.flag == flag){
            if(messageEvent.type.equals(MessageEvent.MSG_V_M_L)){
                videoList.get(Integer.parseInt(message.split(",")[0])).isLiked = Integer.parseInt(message.split(",")[1]);
            }else if(messageEvent.type.equals(MessageEvent.MSG_V_M_C)){
                videoList.get(Integer.parseInt(message.split(",")[0])).isCollected = Integer.parseInt(message.split(",")[1]);
            }
            channelContentAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onRefresh() {
        pageNum++;
        onFragmentFirstVisible();
    }
}
