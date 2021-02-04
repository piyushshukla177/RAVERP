package com.rav.raverp.data.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.ListItemClickListener;
import com.rav.raverp.data.model.api.BookingPlotListModal;
import com.rav.raverp.data.model.api.CustomerPlotDetails;


import java.util.List;


public class CustomerPlotBookingListAdapter extends RecyclerView.Adapter<CustomerPlotBookingListAdapter.MyViewHolder> {

    private static final String TAG = CustomerPlotDetails.class.getSimpleName();
    private ListItemClickListener listItemClickListener;
    private List<BookingPlotListModal.Body> customerPlotDetails;
    private Context context;


    public CustomerPlotBookingListAdapter(Context context, ListItemClickListener listItemClickListener, List<BookingPlotListModal.Body> customerPlotDetailsList) {
        this.context = context;
        this.customerPlotDetails = customerPlotDetailsList;
        this.listItemClickListener = listItemClickListener;
        if (null != customerPlotDetailsList && customerPlotDetailsList.size() > 0) {
            Log.d(TAG, "No. of booked List : " + customerPlotDetailsList.size());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_booking, parent, false));

        /*LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMyBookingBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_my_booking, parent, false);
        return new MyViewHolder(binding.getRoot());*/
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.sno.setText(position + 1 + "");
        holder.tvAccountNumber.setText(customerPlotDetails.get(position).getStrcustaccno());
        holder.tvPlotNo.setText(customerPlotDetails.get(position).getStrplotno());
        holder.tvPayAmount.setText(customerPlotDetails.get(position).getTotalpaid() + "");
        holder.show_more_text_view.setPaintFlags(holder.show_more_text_view.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.show_more_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemClickListener.onItemClicked(position);
            }
        });

/*
        if (null != customerPlotDetails && customerPlotDetails.size()+1 > position) {

            CustomerPlotBookingListAdapter.MyViewHolder rowViewHolder = (CustomerPlotBookingListAdapter.MyViewHolder) holder;
            final int rowPos = rowViewHolder.getAdapterPosition();
            if (rowPos==0){
                holder.getBinding().headerLayout.setVisibility(View.GONE);
                holder.getBinding().contentLayout.setVisibility(View.GONE);
            }else{
                holder.getBinding().headerLayout.setVisibility(View.GONE);
                holder.getBinding().contentLayout.setVisibility(View.VISIBLE);
                holder.getBinding().setCustomerPlotDetails(customerPlotDetails.get(rowPos-1));
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
*/
    }


    @Override
    public int getItemCount() {
        return customerPlotDetails.size();
      /*  if (null != customerPlotDetails && customerPlotDetails.size() > 0) {
            return customerPlotDetails.size()+1;
        } else {
            return 1;
        }*/
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sno,
                tvAccountNumber,
                tvPlotNo,
                tvPayAmount,
                show_more_text_view;

        // private ItemMyBookingBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sno = itemView.findViewById(R.id.sno);
            tvAccountNumber = itemView.findViewById(R.id.tvAccountNumber);
            tvPlotNo = itemView.findViewById(R.id.tvPlotNo);
            tvPayAmount = itemView.findViewById(R.id.tvPayAmount);
            show_more_text_view = itemView.findViewById(R.id.show_more_text_view);
            //  binding = DataBindingUtil.bind(itemView);
        }

/*
        public ItemMyBookingBinding getBinding() {
            return binding;
        }
*/
    }

    public List<BookingPlotListModal.Body> getCustomerPlotDetails() {
        return customerPlotDetails;
    }

}

