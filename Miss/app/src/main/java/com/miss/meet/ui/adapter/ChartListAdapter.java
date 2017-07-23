package com.miss.meet.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miss.meet.R;
import com.miss.meet.constant.Constant;
import com.miss.meet.model.bean.ChartMessage;
import com.miss.meet.model.bean.Friend;
import com.miss.meet.model.bean.MessageItem;
import com.miss.meet.ui.activity.ChartActivity;
import com.miss.meet.ui.activity.TestChartActivity;
import com.miss.meet.util.ImageTools;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linmu on 2017/6/9.
 */

public class ChartListAdapter extends RecyclerView.Adapter<ChartListAdapter.ChartListHolder> {

    private Context mContext;
    private List<MessageItem> mData;

    public ChartListAdapter(Context context,List<MessageItem> mData){
        this.mContext = context;
        this.mData = descList(mData);
    }

    @Override
    public ChartListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        ChartListHolder holder = new ChartListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChartListHolder holder, int position) {

        final MessageItem item = mData.get(position);
        String account = item.getAccount();
        Friend friend = DataSupport.where("account=?", account).findLast(Friend.class);
        String ic = friend.getIc();
        Log.e("ss", "ic:" + ic);
        ImageLoader.getInstance().displayImage(Constant.BASE_URL + "/img/" + friend.getIc() + ".jpg",
                holder.ic, ImageTools.options, ImageTools.animateFirstListener);
        holder.tv_name.setText(item.getAccount());
        ChartMessage message = DataSupport.where("guestAccount = ?",item.getAccount()).findLast(ChartMessage.class);
        String content = "";
        if (message!=null){
            content = message.getContent();
        }
        holder.tv_content.setText(content);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:界面跳转
                Toast.makeText(mContext,"打开"+ item.getAccount()+"界面",Toast.LENGTH_SHORT).show();
                String account = item.getAccount();
                Intent intent = new Intent(mContext, TestChartActivity.class);
                intent.putExtra("account", account);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ChartListHolder extends RecyclerView.ViewHolder{

        View view;
        ImageView ic;
        TextView tv_name;
        TextView tv_content;

        public ChartListHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.message);
            ic = (ImageView) itemView.findViewById(R.id.ic);
            tv_name = (TextView) itemView.findViewById(R.id.name);
            tv_content = (TextView) itemView.findViewById(R.id.content);
        }
    }


    private List<MessageItem> descList(List<MessageItem> data){
        int count = data.size();
        List<MessageItem> list = new ArrayList<>();
        for (int i=0;i<count;i++){
            list.add(data.get(count - i - 1));
        }
        return list;
    }

    public void refreshData(List<MessageItem> data){
        mData.clear();
        mData = descList(data);
        notifyDataSetChanged();
    }

}
