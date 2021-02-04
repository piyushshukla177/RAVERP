package com.rav.raverp.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rav.raverp.R;

import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.model.api.CustomerPlotBookingAccountDetailsModel;
import com.rav.raverp.data.model.api.CustomerPlotDetailsModel;
import com.rav.raverp.data.model.api.PlotAvailableModel;
import com.rav.raverp.databinding.ActivityCustomerPlotBookingAccountDetailsBinding;
import com.rav.raverp.databinding.ActivityCustomerPlotBookingDetailsBinding;
import com.rav.raverp.databinding.ActivityPlotAvailabilityActivityDetailsBinding;

public class CustomerPlotBookingAccountActivityDetails extends BaseActivity  implements ArrowBackPressed {
    private static final String TAG = CustomerPlotBookingAccountActivityDetails.class.getSimpleName();
    private Context mContext = CustomerPlotBookingAccountActivityDetails.this;
    private ActivityCustomerPlotBookingAccountDetailsBinding binding;
    CustomerPlotBookingAccountDetailsModel customerPlotBookingAccountDetailsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=putContentView(R.layout.activity_customer_plot_booking_account_details);
        setToolbarTitle("Plot Booking Account Details");
        showBackArrow();
        setArrowBackPressed(this);
        getIntentData();
    }


    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null &&
                intent.hasExtra("customerplotdetailsaccountdeta") ){
            customerPlotBookingAccountDetailsModel =
                    (CustomerPlotBookingAccountDetailsModel) intent.getSerializableExtra("customerplotdetailsaccountdeta");
            binding.setCustomerPlotBookingAccountDetailsModel(customerPlotBookingAccountDetailsModel);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public void arrowBackPressed() {
        onBackPressed();
    }

}


