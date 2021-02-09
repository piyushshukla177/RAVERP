package com.rav.raverp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;

import com.rav.raverp.MyApplication;
import com.rav.raverp.R;
import com.rav.raverp.data.interfaces.ArrowBackPressed;
import com.rav.raverp.data.interfaces.DialogActionCallback;
import com.rav.raverp.data.model.api.ApiResponse;
import com.rav.raverp.data.model.api.AssociateWalletAmount;
import com.rav.raverp.data.model.api.CustomerAmount;
import com.rav.raverp.data.model.api.CustomerPlotBookModel;
import com.rav.raverp.data.model.api.LoginModel;
import com.rav.raverp.data.model.api.PlotAvailableModel;
import com.rav.raverp.data.model.api.PlotBooking;
import com.rav.raverp.data.model.api.PlotBookingPlan;
import com.rav.raverp.data.model.api.PlotBookingStatus;
import com.rav.raverp.data.model.api.PlotCostModal;
import com.rav.raverp.data.model.api.SendOtpForPlotBookingModel;
import com.rav.raverp.databinding.ActivityPlotBookDetailsBinding;
import com.rav.raverp.databinding.DialogValidateOtpPlotBookingBindingImpl;
import com.rav.raverp.network.ApiClient;
import com.rav.raverp.network.ApiHelper;
import com.rav.raverp.utils.CommonUtils;
import com.rav.raverp.utils.NetworkUtils;
import com.rav.raverp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlotBookActivityDetails<plotAvailable> extends BaseActivity implements ArrowBackPressed {
    private static final String TAG = PlotBookActivityDetails.class.getSimpleName();
    private Context mContext = PlotBookActivityDetails.this;
    private ActivityPlotBookDetailsBinding binding;
    private ApiHelper apiHelper;
    private LoginModel login;
    private Spinner customer_name_spinner;
    private Spinner plot_booking_plan_name_spinner;
    private Spinner plot_booking_plan_status_spinner, spInstallment, spDays;
    String id;
    int roleId;
    int customerId;
    int StatusTypeId;
    int PlanTypeId;
    String PlotId;
    TextView tvPlotCost;


    LinearLayout linearAdd;

    String selectDay = "0";
    String installmentMonth = "0";

    String otpId;

    TextView tvTimer, tvResend;


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        final MenuItem alertMenuItem = menu.findItem(R.id.actionAdd);
        LinearLayout rootView = (LinearLayout) alertMenuItem.getActionView();
        linearAdd = (LinearLayout) rootView.findViewById(R.id.layoutAdd);
        linearAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PlotBookActivityDetails.this, AddCustomerActivity.class);
                startActivity(intent);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
*/

    LinearLayoutCompat llBookingPlan, llInstallment, llDays;
    View viewBookingPlan, viewInstallment, viewDays;


    String[] installment = {"--Select--", "12 Months", "24 Months", "36 Months"};
    String[] days = {"-Select days-", "7", "15"};

    final List<PlotBookingStatus> getPlotBookingStatusList = new ArrayList<>();
    final PlotBookingStatus getplotbookingStatus = new PlotBookingStatus();

    final List<PlotBookingPlan> getPlotBookingList = new ArrayList<>();
    final PlotBookingPlan getplotbooking = new PlotBookingPlan();
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = putContentView(R.layout.activity_plot_book_details);
        setToolbarTitle("Plot Booking Details");
        showBackArrow();
        setArrowBackPressed(this);
        apiHelper = ApiClient.getClient().create(ApiHelper.class);
        login = MyApplication.getLoginModel();
        id = login.getStrLoginID();
        roleId = login.getIntRoleID();
        customer_name_spinner = (Spinner) findViewById(R.id.customer_name_spinner);
        plot_booking_plan_name_spinner = (Spinner) findViewById(R.id.plot_booking_plan_name_spinner);
        plot_booking_plan_status_spinner = (Spinner) findViewById(R.id.plot_booking_plan_status_spinner);
        spInstallment = (Spinner) findViewById(R.id.spInstallment);
        spDays = (Spinner) findViewById(R.id.spDays);
        llBookingPlan = findViewById(R.id.llBookingPlan);
        llInstallment = findViewById(R.id.llInstallment);
        llDays = findViewById(R.id.llDays);
        viewBookingPlan = findViewById(R.id.viewBookingPlan);
        viewInstallment = findViewById(R.id.viewInstallment);
        viewDays = findViewById(R.id.viewDays);
        tvPlotCost = findViewById(R.id.tvPlotCost);
        // GetCustomer();
        // GetBookingPlan();
        GetBookingStatus();
        GetWalletAmount();
        // GetCustomerAmount();
        getIntentData();


        plot_booking_plan_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {
                    llBookingPlan.setVisibility(View.GONE);
                    viewBookingPlan.setVisibility(View.GONE);
                    llDays.setVisibility(View.GONE);
                    viewDays.setVisibility(View.GONE);

                    llInstallment.setVisibility(View.GONE);
                    viewInstallment.setVisibility(View.GONE);

                    installmentMonth = "0";
                    getPlotCost(installmentMonth);

                    // StatusTypeId = getPlotBookingStatusList.get(position).getIntPlotBookingStatusId();
                } else if (position == 1) {

                    if (Double.parseDouble(binding.txtWalletAmount.getText().toString()) < Double.parseDouble(binding.txtPlotBookingAmount.getText().toString())) {
                        ViewUtils.showErrorDialog(mContext, "Insufficient wallet amount",
                                new DialogActionCallback() {
                                    @Override
                                    public void okAction() {
                                        plot_booking_plan_name_spinner.setSelection(0);
                                        llBookingPlan.setVisibility(View.GONE);
                                        viewBookingPlan.setVisibility(View.GONE);
                                        llDays.setVisibility(View.GONE);
                                        viewDays.setVisibility(View.GONE);

                                        llInstallment.setVisibility(View.GONE);
                                        viewInstallment.setVisibility(View.GONE);
                                    }
                                });

                    } else {
                        getPlotBookingList.clear();
                        llBookingPlan.setVisibility(View.VISIBLE);
                        viewBookingPlan.setVisibility(View.VISIBLE);
                        GetBookingPlan();
                        StatusTypeId = getPlotBookingStatusList.get(position).getIntPlotBookingStatusId();

                        llDays.setVisibility(View.GONE);
                        viewDays.setVisibility(View.GONE);
                    }
                } else {
                 /*   if (Double.parseDouble(binding.txtWalletAmount.getText().toString()) < Double.parseDouble(binding.txtPlotBookingAmount.getText().toString())) {
                        ViewUtils.showErrorDialog(mContext, "Insufficient wallet amount",
                                new DialogActionCallback() {
                                    @Override
                                    public void okAction() {
                                        plot_booking_plan_name_spinner.setSelection(0);
                                    }
                                });
                    } else {*/
                        llBookingPlan.setVisibility(View.GONE);
                        viewBookingPlan.setVisibility(View.GONE);

                        llInstallment.setVisibility(View.GONE);
                        viewInstallment.setVisibility(View.GONE);

                        llDays.setVisibility(View.VISIBLE);
                        viewDays.setVisibility(View.VISIBLE);

                        StatusTypeId = getPlotBookingStatusList.get(position).getIntPlotBookingStatusId();

                        ArrayAdapter<String> a = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, days);
                        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spDays.setAdapter(a);
                    }
               // }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        plot_booking_plan_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    llInstallment.setVisibility(View.GONE);
                    viewInstallment.setVisibility(View.GONE);
                    // PlanTypeId = getPlotBookingList.get(position).getIntPlanTypeId();
                    installmentMonth = "0";
                    getPlotCost(installmentMonth);
                } else if (position == 1) {
                    installmentMonth = "0";
                    getPlotCost(installmentMonth);
                    llInstallment.setVisibility(View.GONE);
                    viewInstallment.setVisibility(View.GONE);
                    PlanTypeId = getPlotBookingList.get(position).getIntPlanTypeId();
                } else {
                    llInstallment.setVisibility(View.VISIBLE);
                    viewInstallment.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> a = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, installment);
                    a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spInstallment.setAdapter(a);
                    PlanTypeId = getPlotBookingList.get(position).getIntPlanTypeId();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spInstallment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    installmentMonth = "0";
                } else if (position == 1) {
                    installmentMonth = "1";
                } else if (position == 2) {
                    installmentMonth = "2";
                } else {
                    installmentMonth = "3";
                }
                getPlotCost(installmentMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectDay = "0";
                } else if (position == 1) {
                    selectDay = "1";
                } else {
                    selectDay = "2";
                }
                installmentMonth = "0";
                getPlotCost(installmentMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plot_booking_plan_status_spinner.getSelectedItemPosition() == 0) {
                    ViewUtils.showToast("Please select booking status");
                } else if (llBookingPlan.getVisibility() == View.VISIBLE && plot_booking_plan_name_spinner.getSelectedItemPosition() == 0) {
                    ViewUtils.showToast("Please select booking plan");
                } else if (llInstallment.getVisibility() == View.VISIBLE && spInstallment.getSelectedItemPosition() == 0) {
                    ViewUtils.showToast("Please select emi plan");
                } else if (llDays.getVisibility() == View.VISIBLE && spDays.getSelectedItemPosition() == 0) {
                    ViewUtils.showToast("Please select hold days");
                } else if (binding.plotBookingPlanStatusSpinner.getSelectedItemPosition() == 1) {
                    String amountforbookedhold = binding.edtAmountForBookedHold.getText().toString().trim();
                    if (CommonUtils.isNullOrEmpty(amountforbookedhold)) {
                        binding.edtAmountForBookedHold.setError(getString(R.string.error_field_required));
                        requestFocus(binding.edtAmountForBookedHold);
                    } else {
                        String plotbookingplan = binding.txtPlotBookingAmount.getText().toString().trim();
                        float book = Float.parseFloat(amountforbookedhold);
                        if (!CommonUtils.isNullOrEmpty(plotbookingplan)) {
                            float ploat = Float.parseFloat(plotbookingplan);
                            if (book == ploat) {
                                if (NetworkUtils.isNetworkConnected()) {
                                    // BookPlot();
                                    getOtp();
                                } else {
                                    ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                                        @Override
                                        public void okAction() {

                                        }
                                    });
                                }

                            } else {
                                Toast.makeText(mContext, "Plot Booking Amount and Amount For Booked must be same", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            binding.txtPlotBookingAmount.setError(getString(R.string.error_field_required));
                            requestFocus(binding.txtPlotBookingAmount);
                        }
                    }
                } else if (binding.plotBookingPlanStatusSpinner.getSelectedItemPosition() == 2) {
                    String amountforbookedhold = binding.edtAmountForBookedHold.getText().toString().trim();
                    if (CommonUtils.isNullOrEmpty(amountforbookedhold)) {
                        binding.edtAmountForBookedHold.setError(getString(R.string.error_field_required));
                        requestFocus(binding.edtAmountForBookedHold);
                    } else {
                        String plotbookingplan = binding.txtPlotBookingAmount.getText().toString().trim();
                        Float book = Float.parseFloat(amountforbookedhold);
                        if (!CommonUtils.isNullOrEmpty(plotbookingplan)) {
                            if (spDays.getSelectedItemPosition() == 1) {
                                if (book >= 5000) {
                                    if (NetworkUtils.isNetworkConnected()) {
                                        // BookPlot();
                                        getOtp();
                                    } else {
                                        ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                                            @Override
                                            public void okAction() {

                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(mContext, "Please Enter Minimum Hold Amount 5000", Toast.LENGTH_SHORT).show();
                                }
                            } else if (spDays.getSelectedItemPosition() == 2) {
                                if (book >= 10000) {
                                    if (NetworkUtils.isNetworkConnected()) {
                                        // BookPlot();
                                        getOtp();
                                    } else {
                                        ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                                            @Override
                                            public void okAction() {

                                            }
                                        });

                                    }
                                } else {
                                    Toast.makeText(mContext, "Please Enter Minimum Hold Amount 10000", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            binding.txtPlotBookingAmount.setError(getString(R.string.error_field_required));
                            requestFocus(binding.txtPlotBookingAmount);
                        }

                    }
                } else if (NetworkUtils.isNetworkConnected()) {
                    //BookPlot();
                    getOtp();
                } else {
                    ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                        @Override
                        public void okAction() {

                        }
                    });

                }

            }
        });

    }

    private void BookPlot(final Dialog dialog, String otp) {
        ViewUtils.startProgressDialog(mContext);
        final Call<PlotBooking> getPlotBookingCall =
                apiHelper.getPlotBooking(id, roleId, PlotId, StatusTypeId, PlanTypeId, binding.edtAmountForBookedHold.getText().toString(), installmentMonth, selectDay, otpId, otp);

        getPlotBookingCall.enqueue(new Callback<PlotBooking>() {
            @Override
            public void onResponse(Call<PlotBooking> call,
                                   Response<PlotBooking> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {

                        dialog.dismiss();
                        ViewUtils.showSuccessDialog(mContext, response.body().getMessage(),
                                new DialogActionCallback() {
                                    @Override
                                    public void okAction() {

                                        Intent intent = new Intent(PlotBookActivityDetails.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });

                    } else {

                        if (response.body().getResponse().equalsIgnoreCase("Failure")) {
                            ViewUtils.showErrorDialog(mContext, response.body().getMessage(),
                                    new DialogActionCallback() {
                                        @Override
                                        public void okAction() {


                                        }
                                    });
                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<PlotBooking> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });


    }


    public void checkNetwork() {
        if (NetworkUtils.isNetworkConnected()) {
            //BookPlot();
        } else {
            ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                @Override
                public void okAction() {
                    checkNetwork();
                }
            });
        }
    }

    private void submitForm() {
       /* if (!validateCustomer()) {
            return;
        }*/

      /*  if (!validateCustomerAmount()) {
            return;
        }*/
        if (!validateAssociateWalletAmount()) {
            return;
        }
        if (!validatePlotNo()) {
            return;
        }
        if (!validatePlotArea()) {
            return;
        }

        if (!validatePlotBookingStatus()) {
            return;
        }
        if (!validatePlotCost()) {
            return;
        }
        if (!validatePlotBookingPlan()) {
            return;
        }
        if (!validatePlotBookingAmount()) {
            return;
        }
        if (!validateAmountForBookedHold()) {
            return;
        }

        checkNetwork();
    }

    private boolean validateCustomer() {
        String Customer = binding.customerNameSpinner.getSelectedItem().toString().trim();
        if (Customer.equals("--Select--")) {
            showMessage("Please Select Customer Name.");
            return false;
        }
        return true;
    }

    private boolean validateCustomerAmount() {
        String customeramount = binding.txtCustomerAmount.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(customeramount)) {
            binding.txtCustomerAmount.setError(getString(R.string.error_field_required));
            requestFocus(binding.txtCustomerAmount);
            return false;
        } else {
            binding.txtCustomerAmount.setError(null);
        }
        return true;
    }

    private boolean validateAssociateWalletAmount() {
        String walletamount = binding.txtWalletAmount.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(walletamount)) {
            binding.txtWalletAmount.setError(getString(R.string.error_field_required));
            requestFocus(binding.txtWalletAmount);
            return false;
        } else {
            binding.txtWalletAmount.setError(null);
        }
        return true;
    }

    private boolean validatePlotNo() {
        String plotno = binding.txtPlotNo.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(plotno)) {
            binding.txtPlotNo.setError(getString(R.string.error_field_required));
            requestFocus(binding.txtPlotNo);
            return false;
        } else {
            binding.txtPlotNo.setError(null);
        }
        return true;
    }

    private boolean validatePlotArea() {
        String plotarea = binding.txtPlotArea.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(plotarea)) {
            binding.txtPlotArea.setError(getString(R.string.error_field_required));
            requestFocus(binding.txtPlotArea);
            return false;
        } else {
            binding.txtPlotArea.setError(null);
        }
        return true;
    }

    private boolean validatePlotCost() {
        if (CommonUtils.isNullOrEmpty(tvPlotCost.getText().toString())) {
            tvPlotCost.setError(getString(R.string.error_field_required));
            requestFocus(tvPlotCost);
            return false;
        } else {
            tvPlotCost.setError(null);
        }
        return true;
    }

    private boolean validatePlotBookingPlan() {
        String bookingplan = binding.plotBookingPlanNameSpinner.getSelectedItem().toString().trim();
        if (binding.plotBookingPlanNameSpinner.getSelectedItemPosition() == 0) {
            showMessage("Please Select Plot Booking Plan.");
            return false;
        }
        return true;
    }

    private boolean validatePlotBookingAmount() {
        String plotbookingplan = binding.txtPlotBookingAmount.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(plotbookingplan)) {
            binding.txtPlotBookingAmount.setError(getString(R.string.error_field_required));
            requestFocus(binding.txtPlotBookingAmount);
            return false;
        } else {
            binding.txtPlotBookingAmount.setError(null);
        }
        return true;
    }

    private boolean validateAmountForBookedHold() {
        String amountforbookedhold = binding.edtAmountForBookedHold.getText().toString().trim();
        if (CommonUtils.isNullOrEmpty(amountforbookedhold)) {
            binding.edtAmountForBookedHold.setError(getString(R.string.error_field_required));
            requestFocus(binding.edtAmountForBookedHold);
            return false;
        } else {
            binding.edtAmountForBookedHold.setError(null);
        }
        return true;
    }

    private boolean validatePlotBookingStatus() {
        String bookingplanbookingstatus = binding.plotBookingPlanStatusSpinner.getSelectedItem().toString().trim();
        if (binding.plotBookingPlanStatusSpinner.getSelectedItemPosition() == 0) {
            showMessage("Please Select Plot Booking Status.");
            return false;
        } else if (binding.plotBookingPlanStatusSpinner.getSelectedItemPosition() == 1) {
            String amountforbookedhold = binding.edtAmountForBookedHold.getText().toString().trim();
            if (CommonUtils.isNullOrEmpty(amountforbookedhold)) {
                binding.edtAmountForBookedHold.setError(getString(R.string.error_field_required));
                requestFocus(binding.edtAmountForBookedHold);
                return false;
            } else {
                String plotbookingplan = binding.txtPlotBookingAmount.getText().toString().trim();
                float book = Float.parseFloat(amountforbookedhold);
                if (!CommonUtils.isNullOrEmpty(plotbookingplan)) {
                    float ploat = Float.parseFloat(plotbookingplan);
                    if (book == ploat) {
                        return true;
                    } else {
                        Toast.makeText(mContext, "Plot Booking Amount and Amount For Booked must be same", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    binding.txtPlotBookingAmount.setError(getString(R.string.error_field_required));
                    requestFocus(binding.txtPlotBookingAmount);
                    return false;
                }

            }
        } else if (binding.plotBookingPlanStatusSpinner.getSelectedItemPosition() == 2) {
            String amountforbookedhold = binding.edtAmountForBookedHold.getText().toString().trim();
            if (CommonUtils.isNullOrEmpty(amountforbookedhold)) {
                binding.edtAmountForBookedHold.setError(getString(R.string.error_field_required));
                requestFocus(binding.edtAmountForBookedHold);
                return false;
            } else {
                String plotbookingplan = binding.txtPlotBookingAmount.getText().toString().trim();
                Float book = Float.parseFloat(amountforbookedhold);
                if (!CommonUtils.isNullOrEmpty(plotbookingplan)) {
                    if (book >= 1000) {
                        return true;
                    } else {
                        Toast.makeText(mContext, "Please Enter Minimum Hold Amount 1000", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    binding.txtPlotBookingAmount.setError(getString(R.string.error_field_required));
                    requestFocus(binding.txtPlotBookingAmount);
                    return false;
                }

            }
        }
        return true;
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null &&
                intent.hasExtra("ploatData")) {
            PlotAvailableModel plotAvailable =
                    (PlotAvailableModel) intent.getSerializableExtra("ploatData");
            binding.setPlotAvailable(plotAvailable);
            PlotId = String.valueOf(plotAvailable.getIntPlotId());

        }
    }


    private void GetCustomer() {

        Call<ApiResponse<List<CustomerPlotBookModel>>> getCustomerPlotBookModelCall =
                apiHelper.getCustomerPlotBookModel(id, roleId);
        getCustomerPlotBookModelCall.enqueue(new Callback<ApiResponse<List<CustomerPlotBookModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CustomerPlotBookModel>>> call,
                                   Response<ApiResponse<List<CustomerPlotBookModel>>> response) {


                if (response.isSuccessful()) {
                    final List<CustomerPlotBookModel> getCustomerList = new ArrayList<>();
                    CustomerPlotBookModel getcustomer = new CustomerPlotBookModel();
                    getcustomer.setStrName("--Select--");
                    getcustomer.setBigIntCustomerId(0);
                    getCustomerList.add(getcustomer);
                    getCustomerList.addAll(response.body().getBody());
                    ArrayAdapter<CustomerPlotBookModel> adapter = new ArrayAdapter<>(PlotBookActivityDetails.this,
                            R.layout.simple_spinner_item, getCustomerList);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    customer_name_spinner.setAdapter(adapter);
                    customer_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) {
                                customerId = getCustomerList.get(position).getBigIntCustomerId();
                                binding.customerAmountLayout.setVisibility(View.VISIBLE);
                                customerId = getCustomerList.get(position).getBigIntCustomerId();
                                GetCustomerAmount();

                            } else {
                                binding.customerAmountLayout.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CustomerPlotBookModel>>> call, Throwable t) {
                if (!call.isCanceled()) {

                }
                t.printStackTrace();
            }
        });
    }

    private void GetBookingPlan() {
        ViewUtils.startProgressDialog(mContext);
        Call<ApiResponse<List<PlotBookingPlan>>> getPlotBookingPlan = apiHelper.getPlotBookingPlan();
        getPlotBookingPlan.enqueue(new Callback<ApiResponse<List<PlotBookingPlan>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PlotBookingPlan>>> call,
                                   Response<ApiResponse<List<PlotBookingPlan>>> response) {

                ViewUtils.endProgressDialog();
                if (response.isSuccessful()) {

                    getplotbooking.setStrPlan("--Select--");
                    getplotbooking.setIntPlanTypeId(0);
                    getPlotBookingList.add(getplotbooking);
                    getPlotBookingList.addAll(response.body().getBody());
                    ArrayAdapter<PlotBookingPlan> adapter = new ArrayAdapter<>(PlotBookActivityDetails.this, R.layout.simple_spinner_item, getPlotBookingList);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    plot_booking_plan_name_spinner.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PlotBookingPlan>>> call, Throwable t) {
                ViewUtils.endProgressDialog();
                if (!call.isCanceled()) {

                }
                t.printStackTrace();
            }
        });
    }

    private void GetBookingStatus() {
        Call<ApiResponse<List<PlotBookingStatus>>> getPlotBookingStatusCall =
                apiHelper.getPlotBookingStatus();
        getPlotBookingStatusCall.enqueue(new Callback<ApiResponse<List<PlotBookingStatus>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<PlotBookingStatus>>> call,
                                   Response<ApiResponse<List<PlotBookingStatus>>> response) {


                if (response.isSuccessful()) {

                    getplotbookingStatus.setStrPlotBookingStatus("--Select--");
                    getplotbookingStatus.setIntPlotBookingStatusId(0);
                    getPlotBookingStatusList.add(getplotbookingStatus);
                    getPlotBookingStatusList.addAll(response.body().getBody());
                    ArrayAdapter<PlotBookingStatus> adapter = new ArrayAdapter<>(PlotBookActivityDetails.this,
                            R.layout.simple_spinner_item, getPlotBookingStatusList);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    plot_booking_plan_status_spinner.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<PlotBookingStatus>>> call, Throwable t) {
                if (!call.isCanceled()) {

                }
                t.printStackTrace();
            }
        });
    }

    private void GetWalletAmount() {

        Call<AssociateWalletAmount> getAssociateWalletAmountCall =
                apiHelper.getAssociateWalletAmountModel(id, roleId);

        getAssociateWalletAmountCall.enqueue(new Callback<AssociateWalletAmount>() {
            @Override
            public void onResponse(Call<AssociateWalletAmount> call,
                                   Response<AssociateWalletAmount> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {

                            //  Double amount = response.body().getAssociateWalletAmounts();
                            binding.txtWalletAmount.setText(response.body().getAssociateWalletAmounts());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AssociateWalletAmount> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });

    }

    private void GetCustomerAmount() {

        final Call<CustomerAmount> getCustomerAmountCall =
                apiHelper.getCustomerAmountModel(id, roleId, customerId);

        getCustomerAmountCall.enqueue(new Callback<CustomerAmount>() {
            @Override
            public void onResponse(Call<CustomerAmount> call,
                                   Response<CustomerAmount> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse().equalsIgnoreCase("Success")) {


                            Double amount = response.body().getCustomerAmount();
                            binding.txtCustomerAmount.setText(String.valueOf(amount));

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerAmount> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });

    }


    private void getPlotCost(String month) {

        ViewUtils.startProgressDialog(mContext);
        Call<PlotCostModal> getAssociateWalletAmountCall =
                apiHelper.getPlotCost(id, roleId, PlotId, month);

        getAssociateWalletAmountCall.enqueue(new Callback<PlotCostModal>() {
            @Override
            public void onResponse(Call<PlotCostModal> call,
                                   Response<PlotCostModal> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        PlotCostModal plotCostModal = response.body();
                        if (plotCostModal.getResponse().equalsIgnoreCase("Success")) {

                            tvPlotCost.setText(plotCostModal.getBody().get(0).getNumericplotamount());
                            binding.txtPlotBookingAmount.setText(plotCostModal.getBody().get(0).getBookingamount());

                            // Double amount = response.body().getAssociateWalletAmounts();
                            // binding.txtWalletAmount.setText(String.valueOf(amount));


                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PlotCostModal> call, Throwable t) {
                ViewUtils.endProgressDialog();
                if (!call.isCanceled()) {

                }
                t.printStackTrace();
            }
        });

    }

    private void getOtp() {
        ViewUtils.startProgressDialog(mContext);
        final Call<SendOtpForPlotBookingModel> getCustomerAmountCall =
                apiHelper.getOtp(id, roleId);

        getCustomerAmountCall.enqueue(new Callback<SendOtpForPlotBookingModel>() {
            @Override
            public void onResponse(Call<SendOtpForPlotBookingModel> call,
                                   Response<SendOtpForPlotBookingModel> response) {

                ViewUtils.endProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SendOtpForPlotBookingModel sendOtpForPlotBookingModel = response.body();
                        if (sendOtpForPlotBookingModel.getResponse().equalsIgnoreCase("Success")) {

                            //  tvPlotCost.setText(plotCostModal.getBody().get(0).getNumericplotamount());
                            //  binding.txtPlotBookingAmount.setText(plotCostModal.getBody().get(0).getBookingamount());
                            otpId = String.valueOf(sendOtpForPlotBookingModel.getBody().get(0).getBigintmobileotpid());
                            showFilterDialogValidateOtp();
                            countDownTimer = new CountDownTimer(120000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    tvResend.setVisibility(View.GONE);
                                    tvTimer.setVisibility(View.VISIBLE);
                                    tvTimer.setText("" + String.format("%d : %d ",
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                                }

                                public void onFinish() {
                                    tvResend.setVisibility(View.VISIBLE);
                                    tvTimer.setVisibility(View.GONE);
                                }
                            }.start();

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SendOtpForPlotBookingModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    ViewUtils.endProgressDialog();
                }
                t.printStackTrace();
            }
        });

    }

    private void showFilterDialogValidateOtp() {
        final Dialog filterDialog = new Dialog(this);
        final DialogValidateOtpPlotBookingBindingImpl binding = DataBindingUtil.inflate(LayoutInflater.from((this)),
                R.layout.dialog_validate_otp_plot_booking, null, false);
        //filterDialog.setContentView(R.layout.dialog_validate_otp_plot_booking);
        filterDialog.setContentView(binding.getRoot());
        filterDialog.setCancelable(false);
        filterDialog.setCanceledOnTouchOutside(false);


        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 70);
        Window window = filterDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(inset);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        filterDialog.show();

        ImageView imgcross = filterDialog.findViewById(R.id.imgcross);
        final EditText etOtp = filterDialog.findViewById(R.id.etOtp);
        Button btnsubmit = filterDialog.findViewById(R.id.btnsubmit);
        tvTimer = filterDialog.findViewById(R.id.tvTimer);
        tvResend = filterDialog.findViewById(R.id.tvResend);


        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtp();

/*
                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        tvResend.setVisibility(View.GONE);
                        tvTimer.setVisibility(View.VISIBLE);
                        tvTimer.setText("" + millisUntilFinished / 1000);

                    }

                    public void onFinish() {
                        tvResend.setVisibility(View.VISIBLE);
                        tvTimer.setVisibility(View.GONE);
                    }
                }.start();
*/
            }
        });

        imgcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOtp.getText().toString().isEmpty()) {
                    ViewUtils.showToast("Please enter Otp");
                } else if (NetworkUtils.isNetworkConnected()) {
                    BookPlot(filterDialog, etOtp.getText().toString().trim());
                } else {
                    ViewUtils.showOfflineDialog(mContext, new DialogActionCallback() {
                        @Override
                        public void okAction() {
                            checkNetwork();
                        }
                    });

                }
            }
        });


    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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


