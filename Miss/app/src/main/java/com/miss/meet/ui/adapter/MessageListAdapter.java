package com.miss.meet.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by linmu on 2017/6/5.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListHolder> {

    @Override
    public MessageListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MessageListHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MessageListHolder extends RecyclerView.ViewHolder{

        public MessageListHolder(View itemView) {
            super(itemView);
        }
    }

}
