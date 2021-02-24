package com.rav.raverp.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.R;
import com.rav.raverp.data.model.api.PendingClosedTicketModel;
import com.rav.raverp.ui.ConversationActivity;

public class PendingClosedTicketAdapter extends RecyclerView.Adapter<PendingClosedTicketAdapter.ViewHolder> {
    Context context;
    PendingClosedTicketModel pendingClosedTicketModel;
    String status;

    public PendingClosedTicketAdapter(FragmentActivity activity, PendingClosedTicketModel pendingClosedTicketModel, String pending) {
        this.context = activity;
        this.pendingClosedTicketModel = pendingClosedTicketModel;
        this.status = pending;

    }

    @NonNull
    @Override
    public PendingClosedTicketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_closed_pending_ticket, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PendingClosedTicketAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return pendingClosedTicketModel.getBody().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSupportFor, tvSupportType, tvSubject, tvStatus, tvLastUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSupportFor = itemView.findViewById(R.id.tvSupportFor);
            tvSupportType = itemView.findViewById(R.id.tvSupportType);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLastUpdate = itemView.findViewById(R.id.tvLastUpdate);
        }
    }
}
