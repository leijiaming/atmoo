package com.yysj.atmoo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yysj.atmoo.R;
import com.yysj.atmoo.bean.VideoRelated;
import com.yysj.atmoo.utils.GlideUtils;

import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by asus on 2018/3/25.
 */

public class VideoRelatedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<VideoRelated.Data.VideoList> videoList;
    private Context context;
    public VideoRelatedAdapter(Context context){
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return new VideoRelatedViewHolder(inflater.inflate(R.layout.item_videorelated,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoRelatedViewHolder  videoRelatedViewHolder= (VideoRelatedAdapter.VideoRelatedViewHolder) holder;
        VideoRelated.Data.VideoList video = videoList.get(position);
        GlideUtils.loadImageView(context,video.coverInfo.cover,videoRelatedViewHolder.videoimage);
        videoRelatedViewHolder.videocounts.setText("2500次播放");
        videoRelatedViewHolder.videotime.setText(video.duration);
        videoRelatedViewHolder.videotitle.setText(video.title);
        videoRelatedViewHolder.videocounts.setText("115次热评");
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void setData(List<VideoRelated.Data.VideoList> videoList) {
        this.videoList = videoList;
    }

    class VideoRelatedViewHolder extends RecyclerView.ViewHolder{

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
        public VideoRelatedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemListener!=null){
                        onItemListener.onItemClick(videoList.get(getAdapterPosition()).id);
                    }
                }
            });
        }
    }

    public interface OnItemListener{
        void onItemClick(String id);
    }
    public OnItemListener onItemListener;
    public void setOnItemListener(OnItemListener onItemListener){
        this.onItemListener = onItemListener;
    }
}
