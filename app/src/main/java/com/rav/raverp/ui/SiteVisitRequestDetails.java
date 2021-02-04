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
import com.rav.raverp.databinding.ActivityLeadListDetailsBinding;
import com.rav.raverp.databinding.ActivitySiteVisitRequestDetailsBinding;

public class SiteVisitRequestDetails extends BaseActivity  implements ArrowBackPressed {
    private static final String TAG = SiteVisitRequestDetails.class.getSimpleName();
    private Context mContext = SiteVisitRequestDetails.this;
    private ActivitySiteVisitRequestDetailsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=putContentView(R.layout.activity_site_visit_request_details);
        setToolbarTitle("Site Visit");
        showBackArrow();
        setArrowBackPressed(this);


        getIntentData();
    }


    @SuppressLint("ResourceAsColor")
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null &&
                intent.hasExtra("sitevisit")) {
            SiteVisitRequestModel siteVisitRequestModel =
                    (SiteVisitRequestModel) intent.getSerializableExtra("sitevisit");
            binding.setSiteVisitRequestModel(siteVisitRequestModel);

            if (siteVisitRequestModel.getIntIsDelated() == 0) {
                binding.status.setText("Active");
                binding.status.setTextColor(Color.parseColor("#008000"));


            } else if (siteVisitRequestModel.getIntIsDelated() == 1) {

                binding.status.setText(" IN Active");
                binding.status.setTextColor(Color.parseColor("#FF0000"));

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


