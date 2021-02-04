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
import com.rav.raverp.data.model.api.GetWalletListModel;
import com.rav.raverp.data.model.api.LeadListModel;
import com.rav.raverp.data.model.api.SiteVisitRequestModel;
import com.rav.raverp.databinding.ItemLeadListBinding;
import com.rav.raverp.databinding.ItemSiteVisitRequestBinding;
import com.rav.raverp.databinding.ItemWalletListBinding;

import java.util.List;


public class SiteVisitRequestAdapter extends RecyclerView.Adapter<SiteVisitRequestAdapter.MyViewHolder> {

    private static final String TAG = SiteVisitRequestAdapter.class.getSimpleName();
    private ListItemClickListener listItemClickListener;
    private List<SiteVisitRequestModel> siteVisitRequestModels;
    private Context context;

    public SiteVisitRequestAdapter(Context context,
                                   ListItemClickListener listItemClickListener, List<SiteVisitRequestModel> siteVisitRequestModels) {
        this.context = context;
        this.siteVisitRequestModels = siteVisitRequestModels;
        this.listItemClickListener = listItemClickListener;
        if (null != siteVisitRequestModels && siteVisitRequestModels.size() > 0) {
            Log.d(TAG, "No. of Lead List : " + siteVisitRequestModels.size());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSiteVisitRequestBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_site_visit_request, parent, false);
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (null != siteVisitRequestModels && siteVisitRequestModels.size()+1 > position) {

            MyViewHolder rowViewHolder = (MyViewHolder) holder;
            final int rowPos = rowViewHolder.getAdapterPosition();
            if (rowPos==0){
               holder.getBinding().headerLayout.setVisibility(View.GONE);
               holder.getBinding().contentLayout.setVisibility(View.GONE);
            }else{
                holder.getBinding().headerLayout.setVisibility(View.GONE);
                holder.getBinding().contentLayout.setVisibility(View.VISIBLE);
                holder.getBinding().setSiteVisitRequestModel(siteVisitRequestModels.get(rowPos-1));
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
        if (null != siteVisitRequestModels && siteVisitRequestModels.size() > 0) {
            return siteVisitRequestModels.size()+1;
        } else {
            return 1;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemSiteVisitRequestBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ItemSiteVisitRequestBinding getBinding() {
            return binding;
        }
    }

    public List<SiteVisitRequestModel> getSiteVisitRequestModel() {

        return siteVisitRequestModels;
    }

}

