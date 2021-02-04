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
import com.rav.raverp.data.model.api.CustomerPlotDetails;
import com.rav.raverp.data.model.api.CustomerPlotDetailsModel;
import com.rav.raverp.data.model.api.PlotAvailableModel;
import com.rav.raverp.databinding.ItemPlotAvailableBinding;
import com.rav.raverp.databinding.ItemPlotBookingDetailsCustomerBinding;

import java.util.List;


public class CustomerPlotBookingDetailsAdapter extends RecyclerView.Adapter<CustomerPlotBookingDetailsAdapter.MyViewHolder> {

    private static final String TAG = CustomerPlotDetailsModel.class.getSimpleName();
    private ListItemClickListener listItemClickListener;
    private List<CustomerPlotDetailsModel> customerPlotDetailsModels;
    private Context context;


    public CustomerPlotBookingDetailsAdapter(Context context,
                                             ListItemClickListener listItemClickListener, List<CustomerPlotDetailsModel> customerPlotDetailsModelList) {
        this.context = context;
        this.customerPlotDetailsModels = customerPlotDetailsModelList;
        this.listItemClickListener = listItemClickListener;
        if (null != customerPlotDetailsModels && customerPlotDetailsModels.size() > 0) {
            Log.d(TAG, "No. of Customer Plot List : " + customerPlotDetailsModels.size());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPlotBookingDetailsCustomerBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_plot_booking_details_customer, parent, false);
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (null != customerPlotDetailsModels && customerPlotDetailsModels.size()+1 > position) {

            CustomerPlotBookingDetailsAdapter.MyViewHolder rowViewHolder = (CustomerPlotBookingDetailsAdapter.MyViewHolder) holder;
            final int rowPos = rowViewHolder.getAdapterPosition();
            if (rowPos==0){
                holder.getBinding().headerLayout.setVisibility(View.GONE);
                holder.getBinding().contentLayout.setVisibility(View.GONE);
            }else{
                holder.getBinding().headerLayout.setVisibility(View.GONE);
                holder.getBinding().contentLayout.setVisibility(View.VISIBLE);
                holder.getBinding().setCustomerPlotDetailsModel(customerPlotDetailsModels.get(rowPos-1));
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
        if (null != customerPlotDetailsModels && customerPlotDetailsModels.size() > 0) {
            return customerPlotDetailsModels.size()+1;
        } else {
            return 1;
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemPlotBookingDetailsCustomerBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ItemPlotBookingDetailsCustomerBinding getBinding() {
            return binding;
        }
    }

    public List<CustomerPlotDetailsModel> getCustomerPlotDetailsModel() {
        return customerPlotDetailsModels;
    }

}

