package com.rav.raverp.data.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.R;
import com.rav.raverp.data.model.api.ChatAttachmentModel;
import com.rav.raverp.data.model.api.ChatModel;
import com.rav.raverp.ui.ConversationActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHlder> {
    Context context;
    ChatModel chatModel;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
    String attachment;
    List<String> as;

    public ChatListAdapter(ConversationActivity conversationActivity, ChatModel chatModel) {
        this.context = conversationActivity;
        this.chatModel = chatModel;
    }

    @NonNull
    @Override
    public ViewHlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHlder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHlder holder, int position) {
        holder.tvName.setText(chatModel.getBody().get(position).getName());
        holder.tvDateTime.setText(chatModel.getBody().get(position).getCreateddate());
        holder.tvMessage.setText(chatModel.getBody().get(position).getMessage());

        if (chatModel.getBody().get(position).getMessageby().equalsIgnoreCase("Admin")) {
            holder.tvType.setText("Staff");
            holder.rlMain.setBackgroundColor(context.getResources().getColor(R.color.light));
        } else {
            holder.rlMain.setBackgroundColor(context.getResources().getColor(R.color.card_bg_color));
            holder.tvType.setText("Customer");
        }

        if (chatModel.getBody().get(position).getAttachment().equalsIgnoreCase("")) {
            holder.tvAttachment.setVisibility(View.GONE);
        } else {

            hashMapArrayList.clear();
            holder.tvAttachment.setVisibility(View.VISIBLE);
            holder.rvAttachment.setVisibility(View.VISIBLE);
            attachment = chatModel.getBody().get(position).getAttachment();

            as = Arrays.asList(attachment.split(":"));

            for (int i = 0; i < as.size(); i++) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("attachment", as.get(i));
                hashMapArrayList.add(hashMap);

                Log.v("ListAttach", as.get(i));
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
            ChatAttachmentAdapter chatAttachmentAdapter = new ChatAttachmentAdapter(context, hashMapArrayList, "chat");
            holder.rvAttachment.setLayoutManager(gridLayoutManager);
            holder.rvAttachment.setAdapter(chatAttachmentAdapter);
            holder.tvAttachment.setText("Attachments(" + as.size() + ")");

        }
    }

    @Override
    public int getItemCount() {
        return chatModel.getBody().size();
    }

    public class ViewHlder extends RecyclerView.ViewHolder {
        RelativeLayout rlMain;
        CircleImageView civUserIcon;
        TextView tvDateTime, tvName, tvType, tvMessage, tvAttachment;

        RecyclerView rvAttachment;

        public ViewHlder(@NonNull View itemView) {
            super(itemView);
            rlMain = itemView.findViewById(R.id.rlMain);
            civUserIcon = itemView.findViewById(R.id.civUserIcon);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvAttachment = itemView.findViewById(R.id.tvAttachment);
            rvAttachment = itemView.findViewById(R.id.rvAttachment);
        }
    }


}
