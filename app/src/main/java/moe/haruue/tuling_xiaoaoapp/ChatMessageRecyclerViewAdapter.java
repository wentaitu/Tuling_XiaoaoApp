package moe.haruue.tuling_xiaoaoapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 红岩-移动学员-文泰 on 2017/3/14.
 */

public class ChatMessageRecyclerViewAdapter extends RecyclerView.Adapter<ChatMessageRecyclerViewAdapter.ViewHolder>{
    private  List<ChatMessage> mDatas;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    public ChatMessageRecyclerViewAdapter(List<ChatMessage> datas) {
        mDatas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = mDatas.get(position);
        return msg.getType() == true ? 1 : 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_chat_from_msg, parent, false);
            viewHolder = new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_chat_send_msg, parent, false);
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ChatMessage chatMessage = mDatas.get(position);
        if (chatMessage.getType() == true) {
            ((TextView) viewHolder.itemView.findViewById(R.id.chat_from_createDate)).setText(mDatas.get(position).getDateStr());
            ((TextView) viewHolder.itemView.findViewById(R.id.chat_from_content)).setText(mDatas.get(position).getMsg());
        } else {
            ((TextView) viewHolder.itemView.findViewById(R.id.chat_send_createDate)).setText(mDatas.get(position).getDateStr());
            ((TextView) viewHolder.itemView.findViewById(R.id.chat_send_content)).setText(mDatas.get(position).getMsg());
        }
        chatMessage.save();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}



