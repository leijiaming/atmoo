package com.yysj.atmoo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yysj.atmoo.R;
import com.yysj.atmoo.adapter.CommentListAdapter;
import com.yysj.atmoo.adapter.VideoRelatedAdapter;
import com.yysj.atmoo.bean.Comment;
import com.yysj.atmoo.bean.Result;
import com.yysj.atmoo.bean.VideoDetail;
import com.yysj.atmoo.bean.VideoRelated;
import com.yysj.atmoo.core.MainFactory;
import com.yysj.atmoo.utils.SPUtils;
import com.yysj.atmoo.utils.ToastUtils;
import com.yysj.atmoo.widget.LoadingCustom;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by asus on 2018/3/24.
 */

public class VideoActivity extends AppCompatActivity implements View.OnTouchListener,UMShareListener {
    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;
    @BindView(R.id.related)
    RecyclerView related;
    @BindView(R.id.content)
    ConstraintLayout content;
    @BindView(R.id.commentlist)
    RecyclerView commentlist;
    @BindView(R.id.inputcontent)
    EditText inputcontent;
    @BindView(R.id.commentcontent)
    ConstraintLayout commentcontent;
    @BindView(R.id.activityvideolayout)
    ConstraintLayout activityvideolayout;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.vediotitle)
    TextView vediotitle;
    @BindView(R.id.videocounts)
    TextView videocounts;
    @BindView(R.id.videocomment)
    TextView videocomment;
    private String videoUrl;
    private int position;
    private String videoId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#000000"));
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        activityvideolayout.setOnTouchListener(this);
        videoplayer.setOnTouchListener(this);
        close.setOnTouchListener(this);
        commentlist.setOnTouchListener(this);
        HashMap param = (HashMap) getIntent().getSerializableExtra("param");
        position = (int) param.get("position");
        videoId = (String) param.get("videoId");
        initVedio();
        initVedioList();
        initCommentList();

    }

    private void initCommentList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commentlist.setLayoutManager(layoutManager);
        commentlist.setHasFixedSize(true);
        commentlist.setItemAnimator(new DefaultItemAnimator());
        findCommentList();
    }

    private void findCommentList() {
        String userId = (String) SPUtils.get(this,"userId","");
        Observable<Comment> observable = MainFactory.getApiInstance().atmooCommentListData(videoId,userId,"1","10");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Comment>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoadingCustom.dismissprogress();
                ToastUtils.showShort("服务器开小差了...");
            }

            @Override
            public void onNext(Comment comment) {
                int errno = comment.errno;
                String errmsg = comment.errmsg;
                if(errno == 1){
                    List<Comment.Data> data = comment.data;
                    if(data!=null && !data.isEmpty()) {
                        CommentListAdapter commentlistAdapter = new CommentListAdapter(VideoActivity.this);
                        commentlistAdapter.setData(data);
                        commentlist.setAdapter(commentlistAdapter);
                    }
                }
                ToastUtils.showShort(errmsg);
            }
        });
    }

    private void initVedioList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        related.setLayoutManager(layoutManager);
        related.setHasFixedSize(true);
        related.setItemAnimator(new DefaultItemAnimator());
        findRelatedVideoList();
    }

    private void findRelatedVideoList() {
        Observable<VideoRelated> observable = MainFactory.getApiInstance().atmooDetailRelatedData(videoId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<VideoRelated>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoadingCustom.dismissprogress();
                ToastUtils.showShort("服务器开小差了...");
            }

            @Override
            public void onNext(VideoRelated videoRelated) {
                int errno = videoRelated.errno;
                String errmsg = videoRelated.errmsg;
                if(errno == 1){
                    List<VideoRelated.Data.VideoList>  videoList = videoRelated.data.videoList;
                    if(videoList!=null && !videoList.isEmpty()) {
                        VideoRelatedAdapter videoRelatedAdapter = new VideoRelatedAdapter(VideoActivity.this);
                        videoRelatedAdapter.setOnItemListener(new VideoRelatedAdapter.OnItemListener() {
                            @Override
                            public void onItemClick(String id) {
                                videoId = id;
                                findRelatedVideoList();
                                findVideoDetailById();
                            }
                        });
                        videoRelatedAdapter.setData(videoList);
                        related.setAdapter(videoRelatedAdapter);
                    }
                }
                ToastUtils.showShort(errmsg);
            }
        });
    }

    private void initVedio() {
        findVideoDetailById();
    }

    private void findVideoDetailById() {
        LoadingCustom.showprogress(this,"获取视频中...",true);
        Observable<VideoDetail> observable = MainFactory.getApiInstance().atmooDetailData(videoId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<VideoDetail>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoadingCustom.dismissprogress();
                ToastUtils.showShort("服务器开小差了...");
            }

            @Override
            public void onNext(VideoDetail videoDetail) {
                LoadingCustom.dismissprogress();
                int errno = videoDetail.errno;
                String errmsg = videoDetail.errmsg;
                if(errno == 1){
                   videoUrl = videoDetail.data.playInfo.wifi.url;
                    vediotitle.setText(videoDetail.data.title);
                    String playCnt = videoDetail.data.counts.playCnt;
                    String collectCnt = videoDetail.data.counts.collectCnt;
                    String commentCnt = videoDetail.data.counts.commentCnt;
                    videocounts.setText(playCnt+"次播放");
                    videocomment.setText(commentCnt+"次热评");
                    if(!TextUtils.isEmpty(videoUrl)){
                        videoplayer.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
                        videoplayer.startVideo();
                    }
                }
                ToastUtils.showShort(errmsg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(videoplayer !=null &&videoUrl!=null){
            videoplayer.startVideo();
        }
    }

    @Override
    public void onBackPressed() {
        if(commentcontent.getVisibility() == View.VISIBLE){
            commentcontent.setVisibility(View.GONE);
            return;
        }
        if(content.getVisibility() == View.VISIBLE){
            content.setVisibility(View.GONE);
            return;
        }
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
        //Change these two variables back
        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @OnClick({R.id.close,R.id.videocomment,R.id.comment,R.id.share,R.id.subscribe,R.id.like,R.id.publish})
    public void click(View v){
        switch (v.getId()){
            case R.id.close:
                closeOrShowContent(8);
                break;
            case R.id.videocomment:
                closeOrShowContent(0);
                break;
            case R.id.comment:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                inputcontent.requestFocus();
                commentcontent.setVisibility(View.VISIBLE);
                break;
            case R.id.share:
                share();
                break;
            case R.id.subscribe:
                break;
            case R.id.like:
                break;
            case R.id.publish:
                goPublish();
                break;
        }
    }

    private void goPublish() {
        String userId = (String) SPUtils.get(this,"userId","");
        String etContent = inputcontent.getText().toString().trim();
        if(TextUtils.isEmpty(etContent)){
            ToastUtils.showShort("评论内容不能为空");
            return;
        }
        Observable<Result> observable = MainFactory.getApiInstance().atmooCommentAddData(videoId,userId,etContent);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Result>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showShort("服务器开小差了...");
            }

            @Override
            public void onNext(Result result) {
                int errno = result.errno;
                String errmsg = result.errmsg;
                if(errno == 1){
                    int code = result.data.code;
                    String message = result.data.message;
                    if(code == 1){
                        if(commentcontent.getVisibility() ==View.VISIBLE){
                            commentcontent.setVisibility(View.GONE);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            if(imm.isActive()){
                                imm.hideSoftInputFromWindow(activityvideolayout.getWindowToken(), 0); //强制隐藏键盘
                            }
                        }
                    }
                    ToastUtils.showShort(errmsg);
                }
                ToastUtils.showShort(errmsg);
            }
        });
    }

    private void share() {
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this,mPermissionList,123);
        }

    }

    private void closeOrShowContent(int flag) {
        Animation animation = AnimationUtils.loadAnimation(this, flag==8?R.anim.anim_bottom_out:R.anim.anim_bottom_in);
        content.setVisibility(flag);
        content.startAnimation(animation);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(commentcontent.getVisibility() ==View.VISIBLE){
            commentcontent.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive()){
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
            }
            return true;
        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        new ShareAction(this).withText("hello").setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                .setCallback(this).open();
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {

    }
}
