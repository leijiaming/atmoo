package com.yysj.atmoo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yysj.atmoo.R;
import com.yysj.atmoo.adapter.ChannelContentAdapter;
import com.yysj.atmoo.bean.HomeList;
import com.yysj.atmoo.core.MainFactory;
import com.yysj.atmoo.utils.ToastUtils;
import com.yysj.atmoo.widget.LoadingCustom;

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
public class ChannelFragment extends Fragment {

    private String channelId;
    public ChannelFragment(String channelId){
        this.channelId = channelId;
    }
    @BindView(R.id.content)
    RecyclerView content;
    private boolean isFirstVisible = true;
    private View rootView;
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        content.setLayoutManager(layoutManager);
        content.setHasFixedSize(true);
        content.setItemAnimator(new DefaultItemAnimator());

    }

    private void onFragmentFirstVisible() {
        Log.i("ID",channelId);
        //去服务器下载数据
        LoadingCustom.showprogress(getActivity(),"获取视频中..",true);
        Observable<HomeList> observable = MainFactory.getApiInstance().atmooHomeListData(channelId);
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
                LoadingCustom.dismissprogress();
                int errno = homeList.errno;
                String errmsg = homeList.errmsg;
                if(errno == 1){
                    HomeList.Data data = homeList.data;
                    if(data!=null){
                        List<HomeList.Data.VideoList> videoList = data.videoList;
                        if(videoList !=null && !videoList.isEmpty()){
                            ChannelContentAdapter channelContentAdapter = new ChannelContentAdapter(getActivity());
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

}
