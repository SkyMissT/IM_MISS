package com.miss.meet.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miss.meet.R;
import com.miss.meet.model.bean.ChartMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linmu on 2017/6/5.
 *
 */

public class ChartAdapterTest extends RecyclerView.Adapter<ChartAdapterTest.MyAdapterHolder> {

    private List<ChartMessage> mData = new ArrayList<>();
    private Context mContext;

    RelativeLayout.LayoutParams paramsLeft;
    RelativeLayout.LayoutParams paramsRight;

    public ChartAdapterTest(Context context){
        mContext = context;
    }

    @Override
    public MyAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_chart_item, parent, false);
        MyAdapterHolder holder = new MyAdapterHolder(view);

        paramsLeft = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsRight = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyAdapterHolder holder, int position) {
        ChartMessage item = mData.get(position);
        boolean said = item.isSaid();
        String content = item.getContent();
        if (said){
            holder.tv_message.setLayoutParams(paramsRight);
            holder.tv_message.setBackground(mContext.getResources().getDrawable(R.drawable.right_qipao));
        }else {
            holder.tv_message.setLayoutParams(paramsLeft);
            holder.tv_message.setBackground(mContext.getResources().getDrawable(R.drawable.left_qipao));

        }
        holder.tv_message.setText(content);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }




    static class MyAdapterHolder extends RecyclerView.ViewHolder{
        TextView tv_message;
        public MyAdapterHolder(View itemView){
            super(itemView);
            tv_message = (TextView) itemView.findViewById(R.id.message);
        }

    }


    //  数据初始化
    public void initData(List<ChartMessage> data){
        mData.addAll(data);
    }

    public void addMessage(ChartMessage item){
        mData.add(item);
        notifyDataSetChanged();
    }

    public void refreshData(List<ChartMessage> list){
        mData.addAll(0, descList(list));
        notifyDataSetChanged();
    }

    private List<ChartMessage> descList(List<ChartMessage> list){
        List<ChartMessage> data = new ArrayList<>();
        for (int i = 0 ; i < list.size() ; i ++ ){
            data.add(list.get(list.size() - i - 1));
        }
        return data;
    }



}
