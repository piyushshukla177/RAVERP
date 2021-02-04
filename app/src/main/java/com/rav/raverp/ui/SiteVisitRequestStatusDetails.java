package com.rav.raverp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.model.api.LeadListModel;
import com.rav.raverp.data.model.api.SiteVisitRequestModel;
import com.rav.raverp.data.model.api.SiteVisitRequestStatusModel;
import com.rav.raverp.databinding.ActivityLeadListDetailsBinding;
import com.rav.raverp.databinding.ActivitySiteVisitRequestDetailsBinding;
import com.rav.raverp.databinding.ActivitySiteVisitRequestStatusDetailsBinding;

public class SiteVisitRequestStatusDetails extends BaseActivity  implements ArrowBackPressed {
    private static final String TAG = SiteVisitRequestStatusDetails.class.getSimpleName();
    private Context mContext = SiteVisitRequestStatusDetails.this;
    private ActivitySiteVisitRequestStatusDetailsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=putContentView(R.layout.activity_site_visit_request_status_details);
        setToolbarTitle("Site Visit Request List");
        showBackArrow();
        setArrowBackPressed(this);


        getIntentData();
    }


    @SuppressLint("ResourceAsColor")
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null &&
                intent.hasExtra("sitevisitlist")) {
            SiteVisitRequestStatusModel siteVisitRequestStatusModel =
                    (SiteVisitRequestStatusModel) intent.getSerializableExtra("sitevisitlist");
            binding.setSiteVisitRequestStatusModel(siteVisitRequestStatusModel);
            if (siteVisitRequestStatusModel.getIntSiteVisitRequestStatus() == 1) {
                binding.status.setText("Request Success");
                binding.status.setTextColor(Color.parseColor("#008000"));

            }
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


