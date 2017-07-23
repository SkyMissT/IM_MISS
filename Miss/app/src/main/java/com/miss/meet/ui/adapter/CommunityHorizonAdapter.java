package com.miss.meet.ui.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.miss.meet.ui.activity.FriendInformation;
import com.miss.meet.util.ImageTools;
import com.miss.meet.widget.CircleImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dell on 2017/6/18.
 */

public class CommunityHorizonAdapter extends RecyclerView.Adapter<CommunityHorizonAdapter.HorizonHolder> {

    private Context mContext;
    private List<Friend> mData;

    public CommunityHorizonAdapter(Context mContext){
        this.mContext = mContext;
        mData = new ArrayList<>();
    }
    public void addData(List<Friend> data){
        Log.e("Community", "横向的加载111");
        mData.addAll(data);
        notifyDataSetChanged();
        Log.e("Community", "横向的加载222");
    }

    @Override
    public HorizonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_community_horizontal,parent,false);
        HorizonHolder holder = new HorizonHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HorizonHolder holder, int position) {
        final Friend friend = mData.get(position);
        final String account = friend.getAccount();
        final String nickname = friend.getNickname();
        holder.name.setText(nickname);
        ImageLoader.getInstance().displayImage(Constant.BASE_URL + "/img/" + friend.getIc() + ".jpg",
                holder.ic, ImageTools.options, ImageTools.animateFirstListener);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendInformation.class);
                Gson gson = new Gson();
                String aa = gson.toJson(friend);
                intent.putExtra("gson",aa);
                intent.putExtra("account",account);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class HorizonHolder extends RecyclerView.ViewHolder{

        View item;
        CircleImageView ic;
        TextView name;

        public HorizonHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            name = (TextView) itemView.findViewById(R.id.item_name);
            ic = (CircleImageView) itemView.findViewById(R.id.item_ic);
        }
    }

}
