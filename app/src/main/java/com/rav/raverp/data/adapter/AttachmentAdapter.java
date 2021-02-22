package com.rav.raverp.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.R;
import com.rav.raverp.data.model.api.AttachmentModel;
import com.rav.raverp.data.model.api.SendAttachmentModel;
import com.rav.raverp.ui.fragment.Associate.AddTicketFragment;

import java.util.ArrayList;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {
    Context context;
    ArrayList<AttachmentModel> attachmentModels;

    public AttachmentAdapter(FragmentActivity addTicketFragment, ArrayList<AttachmentModel> spacecrafts) {
        this.context = addTicketFragment;
        this.attachmentModels = spacecrafts;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attachment_name, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttachmentModel s = attachmentModels.get(position);
        holder.tvAttachmentName.setText(s.getName());

        holder.ivRemoveAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attachmentModels.size();
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
        attachmentModels.remove(actualPosition);
        notifyItemRemoved(actualPosition);
        notifyItemRangeChanged(actualPosition, attachmentModels.size());
    }

}
