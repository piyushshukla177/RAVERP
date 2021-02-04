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
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.GetWalletListModel;
import com.rav.raverp.data.model.api.LeadListModel;
import com.rav.raverp.data.model.api.SiteVisitRequestModel;
import com.rav.raverp.data.model.api.WalletAmountListModel;
import com.rav.raverp.databinding.ItemSiteVisitRequestBinding;
import com.rav.raverp.databinding.ItemWalletListBinding;
import com.rav.raverp.utils.ViewUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.MyViewHolder> {

    private static final String TAG = LeadListModel.class.getSimpleName();
    private ListItemClickListener listItemClickListener;
    private List<GetWalletListModel> getWalletListModels;
    private Context context;

    public WalletListAdapter(Context context,
                             ListItemClickListener listItemClickListener, List<GetWalletListModel> getWalletListModelList) {
        this.context = context;
        this.getWalletListModels = getWalletListModelList;
        this.listItemClickListener = listItemClickListener;
        if (null != getWalletListModelList && getWalletListModelList.size() > 0) {
            Log.d(TAG, "No. of Lead List : " + getWalletListModelList.size());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemWalletListBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_wallet_list, parent, false);
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (null != getWalletListModels && getWalletListModels.size()+1 > position) {

            MyViewHolder rowViewHolder = (MyViewHolder) holder;
            final int rowPos = rowViewHolder.getAdapterPosition();
            if (rowPos==0){
               holder.getBinding().headerLayout.setVisibility(View.GONE);
               holder.getBinding().contentLayout.setVisibility(View.GONE);
            }else{
                holder.getBinding().headerLayout.setVisibility(View.GONE);
                holder.getBinding().contentLayout.setVisibility(View.VISIBLE);
                holder.getBinding().setGetWalletListModel(getWalletListModels.get(rowPos-1));
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
        if (null != getWalletListModels && getWalletListModels.size() > 0) {
            return getWalletListModels.size()+1;
        } else {
            return 1;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemWalletListBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ItemWalletListBinding getBinding() {
            return binding;
        }
    }

    public List<GetWalletListModel> getGetWalletListModel() {

        return getWalletListModels;
    }



}

