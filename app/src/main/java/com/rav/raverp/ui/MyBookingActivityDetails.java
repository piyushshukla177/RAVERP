package com.rav.raverp.ui;

import android.content.Context;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.adapter.ReceiptAdapter;
import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.model.api.BookingPlotListModal;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.ReceiptModel;
import com.rav.raverp.databinding.ActivityMyBookingDetailsBinding;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyBookingActivityDetails extends BaseActivity implements ArrowBackPressed {
    private static final String TAG = MyBookingActivityDetails.class.getSimpleName();
    private Context mContext = MyBookingActivityDetails.this;
    private ActivityMyBookingDetailsBinding binding;


    RecyclerView rvReceiptList;

    private ApiHelper apiHelper;
    private LoginModel login;
    String loginId;
    int roleId;
    GridLayoutManager gridLayoutManager;
    String bookingId;
    BookingPlotListModal.Body bookingPlotListModal;

    TextView tvAccountNumber, tvPlotNumber, tvBlockName, tvProjectName, tvArea, tvPlanType, tvPlanCost, tvPaidAmount, tvBalancedAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = putContentView(R.layout.activity_my_booking_details);
        setToolbarTitle("Associate Plot Booking Receipts");
        showBackArrow();
        setArrowBackPressed(this);

        if (getIntent() != null) {
            bookingPlotListModal = (BookingPlotListModal.Body) getIntent().getSerializableExtra("detail");
            bookingId = String.valueOf(bookingPlotListModal.getBigintbookingid());
        }

        findViewById();
    }

    void findViewById() {
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        login = MyApplication.getLoginModel();
        rvReceiptList = findViewById(R.id.rvReceiptList);
        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        tvPlotNumber = findViewById(R.id.tvPlotNumber);
        tvBlockName = findViewById(R.id.tvBlockName);
        tvProjectName = findViewById(R.id.tvProjectName);
        tvArea = findViewById(R.id.tvArea);
        tvPlanType = findViewById(R.id.tvPlanType);
        tvPlanCost = findViewById(R.id.tvPlanCost);
        tvPaidAmount = findViewById(R.id.tvPaidAmount);
        tvBalancedAmount = findViewById(R.id.tvBalancedAmount);
        gridLayoutManager = new GridLayoutManager(mContext, 1);


        loginId = login.getStrLoginID();
        roleId = login.getIntRoleID();


        tvAccountNumber.setText(bookingPlotListModal.getStrcustaccno());
        tvProjectName.setText(bookingPlotListModal.getStrprojectname());
        tvBlockName.setText(bookingPlotListModal.getStrblockname());
        tvPlotNumber.setText(bookingPlotListModal.getStrplotno());
        tvArea.setText(bookingPlotListModal.getFloatplotarea()+" SQFT.");
        tvPlanType.setText(bookingPlotListModal.getStrplan());
        tvPlanCost.setText(bookingPlotListModal.getNumericplotamount());
        tvPaidAmount.setText(bookingPlotListModal.getTotalpaid()+"");
        tvBalancedAmount.setText(bookingPlotListModal.getTotaldue()+"");


        checkNetwork();
    }

    public void checkNetwork() {
        if (NetworkUtils.isNetworkConnected()) {
            getReceiptList();
        } else {
            ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                @Override
                public void okAction() {
                    checkNetwork();
                }
            });
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

    void getReceiptList() {
        ViewUtils.startProgressDialog(mContext);
        final Call<ReceiptModel> getAddCustomerEditCustomerCall = apiHelper.getReceiptList(loginId, roleId, bookingId);

        getAddCustomerEditCustomerCall.enqueue(new Callback<ReceiptModel>() {
            @Override
            public void onResponse(Call<ReceiptModel> call, Response<ReceiptModel> response) {
                ViewUtils.endProgressDialog();
                ReceiptModel receiptModel = response.body();
                ReceiptAdapter receiptAdapter = new ReceiptAdapter(mContext, receiptModel.getBody());
                rvReceiptList.setLayoutManager(gridLayoutManager);
                rvReceiptList.setAdapter(receiptAdapter);
            }


            @Override
            public void onFailure(Call<ReceiptModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });

    }
}


