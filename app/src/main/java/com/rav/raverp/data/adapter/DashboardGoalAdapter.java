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

import com.bumptech.glide.Glide;
import com.rav.raverp.R;
import com.rav.raverp.data.model.api.MyGoalListModel;

import java.util.List;

public class DashboardGoalAdapter extends RecyclerView.Adapter<DashboardGoalAdapter.ViewHolder> {
    Context context;
    List<MyGoalListModel> dataList;

    public DashboardGoalAdapter(FragmentActivity activity, List<MyGoalListModel> myGoalListModels) {
        this.context = activity;
        this.dataList = myGoalListModels;
    }

    @NonNull
    @Override
    public DashboardGoalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_goal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardGoalAdapter.ViewHolder holder, int position) {
        holder.tvSno.setText(position + 1 + "");
        holder.tvMyGoal.setText(dataList.get(position).getStrMyGoal());
        holder.tvStartDate.setText(dataList.get(position).getDtGoalStartDate());
        holder.tvEndDate.setText(dataList.get(position).getDtGoalEndDate());
        holder.tvDaysLeft.setText(dataList.get(position).getDaysLeft());
        holder.tvAction.setText(dataList.get(position).getGoalStatus());
        Glide.with(holder.ivImage).load("https://ravgroup.org" + dataList.get(position).getStrMyGoaIImage())
                .placeholder(R.drawable.account)
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSno, tvMyGoal, tvStartDate, tvEndDate, tvDaysLeft, tvAction;
        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSno = itemView.findViewById(R.id.tvSno);
            tvMyGoal = itemView.findViewById(R.id.tvMyGoal);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvDaysLeft = itemView.findViewById(R.id.tvDaysLeft);
            tvAction = itemView.findViewById(R.id.tvAction);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
