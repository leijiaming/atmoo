package com.yysj.atmoo.core;

import com.yysj.atmoo.bean.Channel;
import com.yysj.atmoo.bean.Comment;
import com.yysj.atmoo.bean.HomeList;
import com.yysj.atmoo.bean.Result;
import com.yysj.atmoo.bean.VideoDetail;
import com.yysj.atmoo.bean.VideoRelated;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface Api {

    @GET("/atmoo/channel/")
    Observable<Channel> getChannelData();
    @GET("/atmoo/homeList/")
    Observable<HomeList> atmooHomeListData(@Query("id")String channelId,@Query("userId")String userId,@Query("pageNum")int pageNum, @Query("pageSize")int pageSize);
    @GET("/atmoo/video/like")
    Observable<Result> atmooVideoLikeData(@Query("videoId")String videoId, @Query("flag")int flag, @Query("userId")String userId);
    @GET("/atmoo/video/{path}")
    Observable<Result> atmooVideoCollectData(@Query("videoId")String videoId, @Query("userId")String userId, @Path("path")String path);
    @GET("/atmoo/user/login/code")
    Observable<Result> atmooUserLoginCodeData(@Query("phone")String phone);
    @GET("/atmoo/user/login")
    Observable<Result> atmooUserLoginData(@Query("phone")String phone,@Query("code")String code,@Query("flag")String flag);
    @GET("/atmoo/detail")
    Observable<VideoDetail> atmooDetailData(@Query("id")String videoId,@Query("userId")String userId);
    @GET("/atmoo/detail/related")
    Observable<VideoRelated> atmooDetailRelatedData(@Query("id")String videoId);
    @GET("/atmoo/comment/list")
    Observable<Comment> atmooCommentListData(@Query("userId")String userId, @Query("id")String id, @Query("pageNum")int pageNum, @Query("pageSize")int pageSize);
    @GET("/atmoo/comment/add")
    Observable<Result> atmooCommentAddData(@Query("videoId")String videoId,@Query("userId")String userId,@Query("content")String content);
    @GET("/atmoo/user/collectlist")
    Observable<HomeList> atmooUserCollectlistData(@Query("userId")String userId);
}
