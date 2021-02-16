package com.rav.raverp.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.R;
import com.rav.raverp.data.model.api.ChatModel;
import com.rav.raverp.ui.ConversationActivity;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHlder> {
    Context context;
    ChatModel chatModel;

    public ChatListAdapter(ConversationActivity conversationActivity, ChatModel chatModel) {
        this.context = conversationActivity;
        this.chatModel = chatModel;
    }

    @NonNull
    @Override
    public ViewHlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHlder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHlder holder, int position) {

        if (chatModel.getBody().get(position).getMessageBy().equalsIgnoreCase("associate")) {
            holder.llAdmin.setVisibility(View.GONE);
            holder.llAssociate.setVisibility(View.VISIBLE);
            holder.tvAssociateMsg.setText(chatModel.getBody().get(position).getMessage());
            holder.tvAssociateDateTime.setText(chatModel.getBody().get(position).getCreateddate());
        } else {

            holder.llAdmin.setVisibility(View.VISIBLE);
            holder.llAssociate.setVisibility(View.GONE);
            holder.tvAdminMsg.setText(chatModel.getBody().get(position).getMessage());
            holder.tvAdminDateTime.setText(chatModel.getBody().get(position).getCreateddate());
        }

    }

    @Override
    public int getItemCount() {
        return chatModel.getBody().size();
    }

    public class ViewHlder extends RecyclerView.ViewHolder {
        LinearLayout llAdmin, llAssociate;
        TextView tvAdminMsg, tvAdminDateTime, tvAssociateMsg, tvAssociateDateTime;

        public ViewHlder(@NonNull View itemView) {
            super(itemView);
            llAdmin = itemView.findViewById(R.id.llAdmin);
            llAssociate = itemView.findViewById(R.id.llAssociate);
            tvAdminMsg = itemView.findViewById(R.id.tvAdminMsg);
            tvAdminDateTime = itemView.findViewById(R.id.tvAdminDateTime);
            tvAssociateMsg = itemView.findViewById(R.id.tvAssociateMsg);
            tvAssociateDateTime = itemView.findViewById(R.id.tvAssociateDateTime);

        }
    }
}
