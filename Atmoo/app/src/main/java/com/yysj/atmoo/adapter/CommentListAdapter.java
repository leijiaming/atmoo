package com.yysj.atmoo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yysj.atmoo.R;
import com.yysj.atmoo.bean.Channel;
import com.yysj.atmoo.bean.Comment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by asus on 2018/3/25.
 */

public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Comment.Data> data;
    public CommentListAdapter(Context context){
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return new CommentListViewHolder(inflater.inflate(R.layout.item_cmmentlist,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentListViewHolder commentListViewHolder = (CommentListViewHolder) holder;
        Comment.Data  d = data.get(position);
        commentListViewHolder.user.setText(d.commentUserName);
        commentListViewHolder.time.setText(d.createTime);
        commentListViewHolder.content.setText(d.content);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Comment.Data> data) {
        this.data = data;
    }

    class CommentListViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.user)
        TextView user;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.content)
        TextView content;
        public CommentListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
