package com.jha.ayush.autotag;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nick on 4/20/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ResultViewHolder>{
    List<ResultCard> resultCardList;

    RVAdapter(List<ResultCard> resultCardList){
        this.resultCardList = resultCardList;
    }

    @Override
    public RVAdapter.ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,false);
        ResultViewHolder rvh = new ResultViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(RVAdapter.ResultViewHolder holder, int position) {
        holder.hashTags.setText(resultCardList.get(position).hashTags);
        holder.image.setImageBitmap(resultCardList.get(position).bitmap);
    }

    @Override
    public int getItemCount() {
        return resultCardList.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView hashTags;
        ImageView image;

        ResultViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            hashTags = (TextView)itemView.findViewById(R.id.TagOutPut);
            image = (ImageView)itemView.findViewById(R.id.imageView);
        }
    }

}

