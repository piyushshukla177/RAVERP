package com.rav.raverp.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.R;
import com.rav.raverp.data.model.api.PlotAvailableModel;
import com.rav.raverp.data.model.api.SendAttachmentModel;
import com.rav.raverp.ui.ConversationActivity;

import java.util.ArrayList;

public class SendAttachmentAdapter extends RecyclerView.Adapter<SendAttachmentAdapter.ViewHolder> {
    Context context;
    ArrayList<SendAttachmentModel> sendAttachmentModels;

    public SendAttachmentAdapter(ConversationActivity conversationActivity, ArrayList<SendAttachmentModel> spacecrafts) {
        this.context = conversationActivity;
        this.sendAttachmentModels = spacecrafts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attachment_name, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SendAttachmentModel s = sendAttachmentModels.get(position);
        holder.tvAttachmentName.setText(s.getName());

        holder.ivRemoveAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position, holder);
             /*   int actualPosition = holder.getAdapterPosition();
                sendAttachmentModels.remove(actualPosition);
                notifyItemRemoved(actualPosition);
                notifyItemRangeChanged(actualPosition, sendAttachmentModels.size());*/
/*
                if (position > -1) {
                    sendAttachmentModels.remove(position);
                    notifyItemRemoved(position);
                }
*/

            }
        });

    }

    @Override
    public int getItemCount() {
        return sendAttachmentModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAttachmentName;
        ImageView ivRemoveAttachment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAttachmentName = itemView.findViewById(R.id.tvAttachmentName);
            ivRemoveAttachment = itemView.findViewById(R.id.ivRemoveAttachment);
        }
    }

    private void removeItem(int position, ViewHolder holder) {
        int actualPosition = holder.getAdapterPosition();
        sendAttachmentModels.remove(actualPosition);
        notifyItemRemoved(actualPosition);
        notifyItemRangeChanged(actualPosition, sendAttachmentModels.size());
    }
}
