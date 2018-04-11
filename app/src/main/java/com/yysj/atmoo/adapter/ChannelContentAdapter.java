package com.yysj.atmoo.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yysj.atmoo.R;
import com.yysj.atmoo.activity.VideoActivity;
import com.yysj.atmoo.bean.HomeList;
import com.yysj.atmoo.bean.Result;
import com.yysj.atmoo.core.MainFactory;
import com.yysj.atmoo.utils.GlideUtils;
import com.yysj.atmoo.utils.IntentUtils;
import com.yysj.atmoo.utils.SPUtils;
import com.yysj.atmoo.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by asus on 2018/3/24.
 */

public class ChannelContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<HomeList.Data.VideoList> videoList;
    private int flag;
    public ChannelContentAdapter(Context context,int flag){
        this.context = context;
        this.flag = flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ChannelViewHolder(inflater.inflate(R.layout.item_channelcontent,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeList.Data.VideoList video = videoList.get(position);
        ChannelViewHolder channelViewHolder = (ChannelViewHolder) holder;
        GlideUtils.loadImageView(context,video.coverInfo.cover,channelViewHolder.bg);
        channelViewHolder.browsecount.setText(video.counts.playCnt);
        channelViewHolder.vediotime.setText(video.duration);
        channelViewHolder.title.setText(video.title);
        int isLiked = videoList.get(position).isLiked;
        if(isLiked==0){
            channelViewHolder.like.setImageResource(R.drawable.like);
        }else{
            channelViewHolder.like.setImageResource(R.drawable.likepress);
        }
        int isCollected = videoList.get(position).isCollected;
        if(isCollected==0){
            channelViewHolder.subscribe.setImageResource(R.drawable.subscribe);
        }else{
            channelViewHolder.subscribe.setImageResource(R.drawable.subscribepress);
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void setData(List<HomeList.Data.VideoList> videoList) {
        this.videoList = videoList;
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bg)
        public ImageView bg;
        @BindView(R.id.browsecount)
        public TextView browsecount;
        @BindView(R.id.vediotime)
        public TextView vediotime;
        @BindView(R.id.title)
        public TextView title;
        @BindView(R.id.like)
        ImageView like;
        @BindView(R.id.subscribe)
        ImageView subscribe;
        public ChannelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
        @OnClick({R.id.bg,R.id.like,R.id.subscribe})
        public void click(View v){
            String userId = (String) SPUtils.get(context,"userId","");
            switch (v.getId()){
                case R.id.bg:
                    HashMap<String,Object> param= new HashMap<>();
                    param.put("position",getAdapterPosition());
                    param.put("videoId",videoList.get(getAdapterPosition()).id);
                    param.put("flag",flag);
                    IntentUtils.startActivity(context, VideoActivity.class,param);
                    break;
                case R.id.like:
                    if(TextUtils.isEmpty(userId)){
                        ToastUtils.showShort("请登录后进行操作");
                        return;
                    }
                    //是否点赞
                    int isLiked = videoList.get(getAdapterPosition()).isLiked;
                    String lid = videoList.get(getAdapterPosition()).id;
                    if(isLiked==0){
                        like.setImageResource(R.drawable.likepress);
                        goClickLike(1,lid,userId);
                    }else{
                        like.setImageResource(R.drawable.like);
                        goClickLike(0,lid,userId);
                    }
                    break;
                case R.id.subscribe:
                    if(TextUtils.isEmpty(userId)){
                        ToastUtils.showShort("请登录后进行操作");
                        return;
                    }
                    //是否收藏
                    int isCollected = videoList.get(getAdapterPosition()).isCollected;
                    String sid = videoList.get(getAdapterPosition()).id;
                    if(isCollected==0){
                        subscribe.setImageResource(R.drawable.subscribepress);
                        goClickSubscribe(1,sid,"collect",userId);
                    }else{
                        subscribe.setImageResource(R.drawable.subscribe);
                        goClickSubscribe(0,sid,"uncollect",userId);
                    }
                    break;
            }

        }

        private void goClickSubscribe(final int isCollected, String sid, String path,String userId) {
            //收藏
            Observable<Result> observable = MainFactory.getApiInstance().atmooVideoCollectData(sid,userId,path);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if(isCollected==2){
                        subscribe.setImageResource(R.drawable.subscribepress);
                    }else{
                        subscribe.setImageResource(R.drawable.subscribe);
                    }
                    ToastUtils.showShort("服务器开小差了...");
                }

                @Override
                public void onNext(Result result) {
                    int errno = result.errno;
                    String errmsg = result.errmsg;
                    if(errno == 1){
                        int code = result.data.code;
                        String message = result.data.message;
                        if(code!=1){
                            if(isCollected==0){
                                subscribe.setImageResource(R.drawable.subscribepress);
                            }else{
                                subscribe.setImageResource(R.drawable.subscribe);
                            }
                        }else{
                            videoList.get(getAdapterPosition()).isCollected = 1;
                        }
                        ToastUtils.showShort(message);
                    }else{
                        if(isCollected==0){
                            subscribe.setImageResource(R.drawable.subscribepress);
                        }else{
                            subscribe.setImageResource(R.drawable.subscribe);
                        }
                    }
                    ToastUtils.showShort(errmsg);
                }
            });
        }
        private void goClickLike(final int isLiked, final String id,String userId) {
            //点赞
            Observable<Result> observable = MainFactory.getApiInstance().atmooVideoLikeData(id,isLiked,userId);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if(isLiked==2){
                        like.setImageResource(R.drawable.likepress);
                    }else{
                        like.setImageResource(R.drawable.like);
                    }
                    ToastUtils.showShort("服务器开小差了...");
                }

                @Override
                public void onNext(Result result) {
                    int errno = result.errno;
                    String errmsg = result.errmsg;
                    if(errno == 1){
                        int code = result.data.code;
                        String message = result.data.message;
                        if(code!=1){
                            if(isLiked==0){
                                like.setImageResource(R.drawable.likepress);
                            }else{
                                like.setImageResource(R.drawable.like);
                            }
                        }else {
                            videoList.get(getAdapterPosition()).isLiked = 1;
                        }
                        ToastUtils.showShort(message);
                    }else{
                        if(isLiked==0){
                            like.setImageResource(R.drawable.likepress);
                        }else{
                            like.setImageResource(R.drawable.like);
                        }
                    }
                    ToastUtils.showShort(errmsg);
                }
            });
        }
    }
}
