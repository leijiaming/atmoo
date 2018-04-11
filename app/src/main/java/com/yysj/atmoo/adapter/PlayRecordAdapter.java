package com.yysj.atmoo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yysj.atmoo.R;
import com.yysj.atmoo.activity.VideoActivity;
import com.yysj.atmoo.bean.HomeList;
import com.yysj.atmoo.bean.VideoRelated;
import com.yysj.atmoo.utils.GlideUtils;
import com.yysj.atmoo.utils.IntentUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by asus on 2018/3/31.
 */

public class PlayRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<HomeList.Data.VideoList> videoList;
    public PlayRecordAdapter(Context context){
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new PlayRecordAdapter.PlayRecordViewHolder(inflater.inflate(R.layout.item_playrecord,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PlayRecordAdapter.PlayRecordViewHolder playRecordViewHolder= (PlayRecordAdapter.PlayRecordViewHolder) holder;
        HomeList.Data.VideoList video = videoList.get(position);
        GlideUtils.loadImageView(context,video.coverInfo.cover,playRecordViewHolder.videoimage);
        playRecordViewHolder.videocounts.setText("2500次播放");
        playRecordViewHolder.videotime.setText(video.duration);
        playRecordViewHolder.videotitle.setText(video.title);
        playRecordViewHolder.videocounts.setText("115次热评");
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void setData(List<HomeList.Data.VideoList> videoList) {
        this.videoList = videoList;
    }

    class PlayRecordViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.videoimage)
        ImageView videoimage;
        @BindView(R.id.videotime)
        TextView videotime;
        @BindView(R.id.videotitle)
        TextView videotitle;
        @BindView(R.id.videocounts)
        TextView videocounts;
        @BindView(R.id.videocurrent)
        TextView videocurrent;
        public PlayRecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String,Object> param= new HashMap<>();
                    param.put("position",-1);
                    param.put("videoId",videoList.get(getAdapterPosition()).id);
                    param.put("flag",0);
                    IntentUtils.startActivity(context, VideoActivity.class,param);
                }
            });
        }
    }
}
