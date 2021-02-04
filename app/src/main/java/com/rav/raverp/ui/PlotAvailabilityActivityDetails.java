package com.rav.raverp.ui;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rav.raverp.R;

import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.model.api.PlotAvailableModel;
import com.rav.raverp.databinding.ActivityPlotAvailabilityActivityDetailsBinding;

public class PlotAvailabilityActivityDetails extends BaseActivity  implements ArrowBackPressed {
    private static final String TAG = PlotAvailabilityActivityDetails.class.getSimpleName();
    private Context mContext = PlotAvailabilityActivityDetails.this;
    private ActivityPlotAvailabilityActivityDetailsBinding binding;

    PlotAvailableModel plotAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=putContentView(R.layout.activity_plot_availability_activity_details);
        setToolbarTitle("Plot Available");
        showBackArrow();
        setArrowBackPressed(this);
        binding.btnBookPlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PlotAvailabilityActivityDetails.this,PlotBookActivityDetails.class);
                intent.putExtra("ploatData", plotAvailable);
                startActivity(intent);
            }
        });


        getIntentData();
    }


    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null &&
                intent.hasExtra("ploatData") ){
            plotAvailable = (PlotAvailableModel) intent.getSerializableExtra("ploatData");
            binding.setPlotAvailable(plotAvailable);
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


