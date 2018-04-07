package com.yysj.atmoo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yysj.atmoo.R;

import butterknife.ButterKnife;

/**
 *
 * Created by asus on 2018/3/31.
 */

public class CollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    public CollectionAdapter(Context context){
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new CollectionAdapter.CollectionViewHolder(inflater.inflate(R.layout.item_collection,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class CollectionViewHolder extends RecyclerView.ViewHolder{

        public CollectionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
