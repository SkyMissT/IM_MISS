package com.miss.meet.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.miss.meet.R;
import com.miss.meet.constant.Constant;
import com.miss.meet.model.bean.Friend;
import com.miss.meet.model.bean.FriendsGroup;
import com.miss.meet.ui.activity.ChangeGroup;
import com.miss.meet.ui.activity.FriendInformation;
import com.miss.meet.util.ImageTools;
import com.miss.meet.widget.CircleImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by linmu on 2017/6/6.
 */

public class ExpandFriendAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<FriendsGroup> groupArray;
    private List<List<Friend>> childArray;

    public ExpandFriendAdapter(Context context,List<FriendsGroup> groupArray,List<List<Friend>> childArray){
        mContext = context;
        this.groupArray = groupArray;
        this.childArray = childArray;
    }

    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        GroupHolder holder = null;
        if (view == null){
            holder = new GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_friends_group, null);
            holder.tv_groupName = (TextView) view.findViewById(R.id.group_name);
            holder.iv_select = (ImageView) view.findViewById(R.id.group_ic);
            view.setTag(holder);
        }else {
            holder = (GroupHolder) view.getTag();
        }
        if (isExpanded){
            holder.iv_select.setBackgroundResource(R.mipmap.expand_bottom);
        }else {
            holder.iv_select.setBackgroundResource(R.mipmap.expand_right);
        }
        holder.tv_groupName.setText(groupArray.get(groupPosition).getGroupName());
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ChildHolder holder = null;
        final Friend friend = childArray.get(groupPosition).get(childPosition);
        if (view == null){
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_friends_child, null);
            holder.tv_name = (TextView) view.findViewById(R.id.child_name);
            holder.tv_signature = (TextView) view.findViewById(R.id.child_signature);
            holder.iv_ic = (CircleImage) view.findViewById(R.id.child_ic);
            String ic = friend.getIc() == null ? "ic" : friend.getIc();
            ImageLoader.getInstance().displayImage(Constant.BASE_URL + "/img/" + ic + ".jpg",
                    holder.iv_ic, ImageTools.options, ImageTools.animateFirstListener);
            holder.item = view.findViewById(R.id.item);
            view.setTag(holder);
        }else {
            holder = (ChildHolder) view.getTag();
        }
        String friendAccount = friend.getAccount();
        String nickname = friend.getNickname();
        if (TextUtils.isEmpty(nickname)){
            nickname = friendAccount;
        }
        holder.tv_name.setText(nickname);
        holder.tv_signature.setText(friend.getSignature());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  跳转到详情界面
                Intent friendInformation = new Intent(mContext, FriendInformation.class);
                String account = childArray.get(groupPosition).get(childPosition).getAccount();
                Gson gson = new Gson();
                String aa = gson.toJson(friend);
                friendInformation.putExtra("account",account);
                friendInformation.putExtra("gson",aa);
                mContext.startActivity(friendInformation);
            }
        });
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, ChangeGroup.class);
                intent.putExtra("friendAccount", friend.getAccount());
                mContext.startActivity(intent);
                return true;
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder{
        TextView tv_groupName;
        ImageView iv_select;
    }

    class ChildHolder{
        TextView tv_name;
        TextView tv_signature;
        CircleImage iv_ic;
        View item;
    }


    public void refreshData(List<FriendsGroup> groupArray,List<List<Friend>> childArray){
        groupArray.clear();
        childArray.clear();
        this.groupArray = groupArray;
        this.childArray = childArray;
        notifyDataSetChanged();
    }

}
