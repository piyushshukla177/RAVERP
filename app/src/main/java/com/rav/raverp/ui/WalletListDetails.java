package com.rav.raverp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.model.api.GetWalletListModel;
import com.rav.raverp.data.model.api.LeadListModel;
import com.rav.raverp.data.model.api.SiteVisitRequestModel;
import com.rav.raverp.databinding.ActivityLeadListDetailsBinding;
import com.rav.raverp.databinding.ActivitySiteVisitRequestDetailsBinding;
import com.rav.raverp.databinding.ActivityWalletListDetailsBinding;

public class WalletListDetails extends BaseActivity  implements ArrowBackPressed {
    private static final String TAG = WalletListDetails.class.getSimpleName();
    private Context mContext = WalletListDetails.this;
    private ActivityWalletListDetailsBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=putContentView(R.layout.activity_wallet_list_details);
        setToolbarTitle("Wallet");
        showBackArrow();
        setArrowBackPressed(this);



        getIntentData();
    }


    @SuppressLint("ResourceAsColor")
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null &&
                intent.hasExtra("walletdata")) {
            GetWalletListModel getWalletListModel =
                    (GetWalletListModel) intent.getSerializableExtra("walletdata");
            binding.setGetWalletListModel(getWalletListModel);

            if (getWalletListModel.getIntApproveStatus() == 1) {
                binding.status.setText("Pending");
                binding.status.setTextColor(Color.parseColor("#FFA500"));
            } else if (getWalletListModel.getIntApproveStatus() == 2) {

                binding.status.setText(" Declined");
                binding.status.setTextColor(Color.parseColor("#FF0000"));

            }
            if(getWalletListModel.getIntApproveStatus()==3)
            {
                binding.status.setText("Confirmed");
                binding.status.setTextColor(Color.parseColor("#008000"));
            }
            else if(getWalletListModel.getIntApproveStatus()==4)
            {
                binding.status.setText("Hold");
                binding.status.setTextColor(Color.parseColor("#800000"));
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


