package com.rav.raverp.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.rav.raverp.MyApplication;
import com.rav.raverp.R;

import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.local.prefs.PrefsHelper;
import com.rav.raverp.data.model.api.CustomerListModel;
import com.rav.raverp.data.model.api.DeleteCustomerModel;
import com.rav.raverp.data.model.api.EditEmailModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.databinding.ActivityCustomerListDetailsBinding;
import com.rav.raverp.databinding.DialogConfirmationBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.AppConstants;
import com.rav.raverp.utils.ViewUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerListActivityDetails extends BaseActivity  implements ArrowBackPressed {
    private static final String TAG = CustomerListActivityDetails.class.getSimpleName();
    private Context mContext = CustomerListActivityDetails.this;
    private ActivityCustomerListDetailsBinding binding;
    CustomerListModel customerListModel;
    ImageView DeleteBtn;
    private ApiHelper apiHelper;
    private LoginModel login;
    int customerId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=putContentView(R.layout.activity_customer_list_details);
        setToolbarTitle("Customer List");
        showBackArrow();
        setArrowBackPressed(this);
        DeleteBtn=(ImageView)findViewById(R.id.delete_btn);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);

        login = MyApplication.getLoginModel();

        getIntentData();

    }


    @SuppressLint("ResourceAsColor")
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null &&
                intent.hasExtra("Customerlist")) {
            customerListModel =
                    (CustomerListModel) intent.getSerializableExtra("Customerlist");
            binding.setCustomerListModel(customerListModel);
            customerId = customerListModel.getBigIntCustomerId();

            if (customerListModel.getIntIsDelated() == 0) {
                binding.status.setText("Active");
                binding.status.setTextColor(Color.parseColor("#008000"));


            } else if (customerListModel.getIntIsDelated() == 1) {

                binding.status.setText(" IN Active");
                binding.status.setTextColor(Color.parseColor("#FF0000"));
                binding.deleteBtn.setVisibility(View.GONE);

            }

            binding.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CustomerListActivityDetails.this, AddCustomerActivity.class);
                    intent.putExtra("Customerlist", customerListModel);
                    startActivity(intent);
                }
            });
            binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showPopup();
                   // CustomerDelete();
                }
            });
        }

    }

    private void showPopup() {
        ViewUtils.showConfirmationDialog(this, getString(R.string.msg_dialog),
                new DialogActionCallback() {
                    @Override
                    public void okAction() {
                        CustomerDelete();
                    }
                });


    }


    private void CustomerDelete() {
                 String id=login.getStrLoginID();

                 Call<DeleteCustomerModel> getDeleteCustomerModelCall = apiHelper.getDeleteCustomerModel(id,customerId);
                 getDeleteCustomerModelCall.enqueue(new Callback<DeleteCustomerModel>() {
                     @Override
                     public void onResponse(@NonNull Call<DeleteCustomerModel> call,
                                            @NonNull Response<DeleteCustomerModel> response) {
                         showProgress(false);
                         if (response.isSuccessful()) {
                             if (response.body().getResponse().equalsIgnoreCase("Success")) {

                                 ViewUtils.showSuccessDialog(mContext, response.body().getMessage(),
                                         new DialogActionCallback() {
                                             @Override
                                             public void okAction() {

                                                 Intent intent = new Intent(CustomerListActivityDetails.this, MainActivity.class);

                                                 startActivity(intent);

                                             }
                                         });

                             }else{

                             }

                         }

                     }




                     @Override
                     public void onFailure(@NonNull Call<DeleteCustomerModel> call,
                                           @NonNull Throwable t) {
                         if (!call.isCanceled()) {
                             showProgress(false);
                             ViewUtils.showToast(t.getLocalizedMessage());
                         }
                         t.printStackTrace();
                     }
                 });
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


