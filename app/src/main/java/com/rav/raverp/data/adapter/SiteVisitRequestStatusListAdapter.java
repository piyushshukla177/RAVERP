package com.rav.raverp.data.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.model.api.PlotAvailableModel;
import com.rav.raverp.data.model.api.SiteVisitRequestStatusModel;
import com.rav.raverp.databinding.ItemPlotAvailableBinding;
import com.rav.raverp.databinding.ItemSiteVisitRequestStatusBinding;

import java.util.List;


public class SiteVisitRequestStatusListAdapter extends RecyclerView.Adapter<SiteVisitRequestStatusListAdapter.MyViewHolder> {

    private static final String TAG = SiteVisitRequestStatusModel.class.getSimpleName();
    private ListItemClickListener listItemClickListener;
    private List<SiteVisitRequestStatusModel> siteVisitRequestStatusModels;
    private Context context;

    public SiteVisitRequestStatusListAdapter(Context context,
                                             ListItemClickListener listItemClickListener, List<SiteVisitRequestStatusModel> siteVisitRequestStatusModelList) {
        this.context = context;
        this.siteVisitRequestStatusModels = siteVisitRequestStatusModelList;
        this.listItemClickListener = listItemClickListener;
        if (null != siteVisitRequestStatusModelList && siteVisitRequestStatusModelList.size() > 0) {
            Log.d(TAG, "No. of Site List : " + siteVisitRequestStatusModelList.size());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSiteVisitRequestStatusBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_site_visit_request_status, parent, false);
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (null != siteVisitRequestStatusModels && siteVisitRequestStatusModels.size()+1 > position) {

            SiteVisitRequestStatusListAdapter.MyViewHolder rowViewHolder = (SiteVisitRequestStatusListAdapter.MyViewHolder) holder;
            final int rowPos = rowViewHolder.getAdapterPosition();
            if (rowPos==0){
                holder.getBinding().headerLayout.setVisibility(View.GONE);
                holder.getBinding().contentLayout.setVisibility(View.GONE);
            }else{
                holder.getBinding().headerLayout.setVisibility(View.GONE);
                holder.getBinding().contentLayout.setVisibility(View.VISIBLE);
                holder.getBinding().setSiteVisitRequestStatusModel(siteVisitRequestStatusModels.get(rowPos-1));
                holder.getBinding().sno.setText(String.valueOf(rowPos));
                holder.getBinding().showMoreTextView.setPaintFlags(
                        holder.getBinding().showMoreTextView.getPaintFlags() |
                                Paint.UNDERLINE_TEXT_FLAG);
            }

            holder.getBinding().showMoreTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listItemClickListener.onItemClicked(rowPos-1);
                }
            });

        }
    }





    @Override
    public int getItemCount() {
        if (null != siteVisitRequestStatusModels && siteVisitRequestStatusModels.size() > 0) {
            return siteVisitRequestStatusModels.size()+1;
        } else {
            return 1;
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemSiteVisitRequestStatusBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ItemSiteVisitRequestStatusBinding getBinding() {
            return binding;
        }
    }

    public List<SiteVisitRequestStatusModel> getSiteVisitRequestStatusModel() {
        return siteVisitRequestStatusModels;
    }

}

