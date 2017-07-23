package com.miss.meet.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.miss.meet.R;
import com.miss.meet.constant.Constant;
import com.miss.meet.model.bean.Friend;
import com.miss.meet.model.bean.Person;
import com.miss.meet.ui.activity.FriendInformation;
import com.miss.meet.util.ImageTools;
import com.miss.meet.widget.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.sf.json.JSON;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2017/5/16.
 *
 *         每次刷新都是8个
 */

public class CommunityVerticalAdapter extends RecyclerView.Adapter<CommunityVerticalAdapter.VerticalHolder> {

    public Context mContext;
    public List<Friend> mData;

    public CommunityVerticalAdapter(Context mContext){
        this.mContext = mContext;
        mData = new ArrayList<>();
    }

    @Override
    public VerticalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_community_vertical,parent,false);
        VerticalHolder holder = new VerticalHolder(view);
        return holder;
    }

    public void addData(List<Friend> list){
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(VerticalHolder holder, int position) {
        final Friend item = mData.get(position);
        String name = item.getNickname();
        int age = item.getAge();
        String signature = item.getSignature();
        String labels = item.getLabels();
        final String ic = item.getIc();

        holder.name.setText(name+","+age);
        holder.signature.setText(signature);
        holder.labels.setText(labels);
        ImageLoader.getInstance().displayImage(Constant.BASE_URL + "/img/" + ic + ".jpg",
                holder.ic, ImageTools.options, ImageTools.animateFirstListener);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendInformation.class);
                Gson gson = new Gson();
                String aa = gson.toJson(item);
                intent.putExtra("gson",aa);
                intent.putExtra("account",item.getAccount());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class VerticalHolder extends RecyclerView.ViewHolder{
        CardView item;
        RoundImageView ic;
        TextView name;
        TextView signature;
        TextView labels;
        public VerticalHolder(View itemView) {
            super(itemView);

            item = (CardView) itemView.findViewById(R.id.item);
            ic = (RoundImageView) itemView.findViewById(R.id.item_ic);
            name = (TextView) itemView.findViewById(R.id.item_name);
            signature = (TextView) itemView.findViewById(R.id.item_signature);
            labels = (TextView) itemView.findViewById(R.id.item_labels);

        }
    }
}
