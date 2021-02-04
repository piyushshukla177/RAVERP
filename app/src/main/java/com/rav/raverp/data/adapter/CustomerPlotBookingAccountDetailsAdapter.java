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
import com.rav.raverp.data.model.api.CustomerPlotBookingAccountDetailsModel;
import com.rav.raverp.data.model.api.CustomerPlotDetailsModel;
import com.rav.raverp.databinding.ItemPlotBookingAccountDetailsCustomerBinding;
import com.rav.raverp.databinding.ItemPlotBookingDetailsCustomerBinding;

import java.util.List;


public class CustomerPlotBookingAccountDetailsAdapter extends RecyclerView.Adapter<CustomerPlotBookingAccountDetailsAdapter.MyViewHolder> {

    private static final String TAG = CustomerPlotBookingAccountDetailsModel.class.getSimpleName();
    private ListItemClickListener listItemClickListener;
    private List<CustomerPlotBookingAccountDetailsModel> customerPlotBookingAccountDetailsModels;
    private Context context;


    public CustomerPlotBookingAccountDetailsAdapter(Context context,
                                                    ListItemClickListener listItemClickListener, List<CustomerPlotBookingAccountDetailsModel> customerPlotBookingAccountDetailsModelList) {
        this.context = context;
        this.customerPlotBookingAccountDetailsModels = customerPlotBookingAccountDetailsModelList;
        this.listItemClickListener = listItemClickListener;
        if (null != customerPlotBookingAccountDetailsModels && customerPlotBookingAccountDetailsModels.size() > 0) {
            Log.d(TAG, "No. of Customer Account List : " + customerPlotBookingAccountDetailsModels.size());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPlotBookingAccountDetailsCustomerBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_plot_booking_account_details_customer, parent, false);
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (null != customerPlotBookingAccountDetailsModels && customerPlotBookingAccountDetailsModels.size()+1 > position) {

            CustomerPlotBookingAccountDetailsAdapter.MyViewHolder rowViewHolder = (CustomerPlotBookingAccountDetailsAdapter.MyViewHolder) holder;
            final int rowPos = rowViewHolder.getAdapterPosition();
            if (rowPos==0){
                holder.getBinding().headerLayout.setVisibility(View.GONE);
                holder.getBinding().contentLayout.setVisibility(View.GONE);
            }else{
                holder.getBinding().headerLayout.setVisibility(View.GONE);
                holder.getBinding().contentLayout.setVisibility(View.VISIBLE);
                holder.getBinding().setCustomerPlotBookingAccountDetailsModel(customerPlotBookingAccountDetailsModels.get(rowPos-1));
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
        if (null != customerPlotBookingAccountDetailsModels && customerPlotBookingAccountDetailsModels.size() > 0) {
            return customerPlotBookingAccountDetailsModels.size()+1;
        } else {
            return 1;
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemPlotBookingAccountDetailsCustomerBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ItemPlotBookingAccountDetailsCustomerBinding getBinding() {
            return binding;
        }
    }

    public List<CustomerPlotBookingAccountDetailsModel> getCustomerPlotBookingAccountDetailsModel() {
        return customerPlotBookingAccountDetailsModels;
    }

}

