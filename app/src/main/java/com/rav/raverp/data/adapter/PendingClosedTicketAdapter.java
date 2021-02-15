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
        holder.tvTicketNo.setText(pendingClosedTicketModel.getBody().get(position).getPkTicketno());
        holder.tvDate.setText(pendingClosedTicketModel.getBody().get(position).getCreateddate());
        holder.tvSubject.setText(pendingClosedTicketModel.getBody().get(position).getStrsubjectname());
        String query = pendingClosedTicketModel.getBody().get(position).getQuery();
        String query1;
        if (query.length() >= 20) {
            query1 = query.substring(0, 18) + "..";
        } else {
            query1 = query;
        }
        holder.tvQuery.setText(query1);

        //   holder.tvQuery.setText(pendingClosedTicketModel.getBody().get(position).getQuery());

        holder.tvViewTicket.setPaintFlags(
                holder.tvViewTicket.getPaintFlags() |
                        Paint.UNDERLINE_TEXT_FLAG);

        holder.tvViewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ConversationActivity.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return pendingClosedTicketModel.getBody().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTicketNo, tvDate, tvSubject, tvQuery, tvViewTicket;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTicketNo = itemView.findViewById(R.id.tvTicketNo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvQuery = itemView.findViewById(R.id.tvQuery);
            tvViewTicket = itemView.findViewById(R.id.tvViewTicket);
        }
    }
}
