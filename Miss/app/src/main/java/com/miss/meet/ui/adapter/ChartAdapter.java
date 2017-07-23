package com.miss.meet.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miss.meet.R;
import com.miss.meet.model.bean.ChartMessage;

import java.util.List;

/**
 * Created by Dell on 2017/5/14.
 *      只有在adapter初始化的时候从数据库获取数据
 */

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ChartHolder> {

    private List<ChartMessage> mData;

    public ChartAdapter(List<ChartMessage> mData){
        this.mData = mData;
    }


    @Override
    public ChartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart, parent, false);
        ChartHolder holder = new ChartHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChartHolder holder, int position) {
        ChartMessage item = mData.get(position);
        String content = item.getContent();
        boolean said = item.isSaid();
        //  根据标志位判断，决定聊天信息出现位置
        if (said){
            holder.tv_right.setText(content);
            holder.tv_right.setVisibility(View.VISIBLE);
            holder.tv_left.setVisibility(View.GONE);
        }else {
            holder.tv_left.setText(content);
            holder.tv_left.setVisibility(View.VISIBLE);
            holder.tv_right.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ChartHolder extends RecyclerView.ViewHolder{
        RelativeLayout chart_item;
        TextView tv_left;
        TextView tv_right;
        public ChartHolder(View itemView) {
            super(itemView);
            chart_item = (RelativeLayout) itemView.findViewById(R.id.item_chart);
            tv_left = (TextView) itemView.findViewById(R.id.left_message);
            tv_right = (TextView) itemView.findViewById(R.id.right_message);
        }
    }

    public void refreshMessage(ChartMessage message){
        mData.add(message);
        notifyDataSetChanged();
    }


}
